package br.com.outbox.logservice.core.consumer;

import br.com.outbox.logservice.core.service.EventService;
import br.com.outbox.logservice.core.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final JsonUtil jsonUtil;
    private final EventService eventService;

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}",
        topics = {
            "log-create-user",
            "log-update-user",
            "log-delete-user"
        }
    )
    public void consumeLogUserTopic(ConsumerRecord<String, String> record) {
        var payload = record.value();
        log.info("Receiving success event {} from topic: {}", record.topic(), payload);
        var event = jsonUtil.toEvent(payload);
        eventService.saveEvent(event);
    }
}
