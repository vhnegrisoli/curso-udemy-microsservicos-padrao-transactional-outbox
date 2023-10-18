package br.com.outbox.userservice.core.service;

import br.com.outbox.userservice.core.dto.ProducerResponse;
import br.com.outbox.userservice.core.mapper.OutboxMapper;
import br.com.outbox.userservice.core.model.Outbox;
import br.com.outbox.userservice.core.producer.KafkaProducer;
import br.com.outbox.userservice.core.repository.OutboxRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static br.com.outbox.userservice.core.enums.EOutboxStatus.*;
import static br.com.outbox.userservice.core.utils.Constants.*;

@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = { @Lazy })
public class OutboxService {

    private final KafkaProducer producer;

    @Lazy
    private final OutboxRepository outboxRepository;

    private final EntityManager entityManager;

    public void create(String topic, String payload) {
        CompletableFuture.runAsync(() -> {
            var outbox = OutboxMapper.toOutbox(topic, payload);
            save(outbox);
        });
    }

    @Transactional
    public void sendEvent(Outbox outbox) {
        log.info("Trying to send outbox {} for attempt: {}", outbox.getId(), outbox.getAttempt());
        var producerResponse = producer.sendEvent(outbox.getPayload(), outbox.getTopic());
        outbox.setErrorMessage(producerResponse.errorMessage());
        handleOutbox(producerResponse, outbox);
    }

    private void handleOutbox(ProducerResponse producerResponse, Outbox outbox) {
        if (producerResponse.success()) {
            handleSentOutbox(outbox);
        } else if (outbox.getAttempt() >= MAX_ATTEMPT) {
            handleErrorOutbox(outbox);
        } else {
            handlePendingOutbox(outbox);
        }
        save(outbox);
    }

    private void handleSentOutbox(Outbox outbox) {
        outbox.setStatus(SENT);
        outbox.setErrorMessage(null);
    }

    private void handleErrorOutbox(Outbox outbox) {
        log.info("Outbox {} reached max attempt: {} - ERROR", outbox.getId(), outbox.getAttempt());
        outbox.setStatus(ERROR);
    }

    private void handlePendingOutbox(Outbox outbox) {
        var increment = outbox.getAttempt() + BigInteger.ONE.intValue();
        log.info("Incrementing outbox {} attempt from {} to {}", outbox.getId(), outbox.getAttempt(), increment);
        outbox.setAttempt(increment);
        outbox.setAttemptExecution(LocalDateTime.now().plusMinutes(ATTEMPT_NEXT_EXECUTION_IN_MINS));
        outbox.setStatus(PENDING);
    }

    @Transactional
    public void resumeOutbox() {
        var qtyToDelete = outboxRepository.countByStatus(SENT);
        if (qtyToDelete > BigInteger.ZERO.intValue()) {
            log.info("Deleting {} outbox events", qtyToDelete);
            outboxRepository.deleteByStatus(SENT);
        }

        outboxRepository
            .findByStatusOrderByCreatedAt(PENDING)
            .stream()
            .filter(this::isAvailableOutboxToSend)
            .forEach(this::sendEvent);
    }

    private boolean isAvailableOutboxToSend(Outbox outbox) {
        return LocalDateTime.now().isAfter(outbox.getAttemptExecution())
            && outbox.getAttempt() > FIRST_ATTEMPT;
    }

    @Transactional
    public void restartOutbox(Integer id) {
        outboxRepository
            .findById(id)
            .ifPresentOrElse(outbox -> {
                    outbox.setAttempt(FIRST_ATTEMPT);
                    outbox.setStatus(PENDING);
                    save(outbox);
                },
                () -> {
                    throw new RuntimeException("Outbox not found by ID!");
                });
    }

    @Transactional
    private void save(Outbox outbox) {
        outboxRepository.saveAndFlush(outbox);
        entityManager.clear();
    }
}
