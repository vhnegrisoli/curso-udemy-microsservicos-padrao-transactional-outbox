package br.com.outbox.userservice.core.component;

import br.com.outbox.userservice.core.model.Outbox;
import br.com.outbox.userservice.core.service.OutboxService;
import jakarta.persistence.PostPersist;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@AllArgsConstructor
public class OutboxTableListener {

    private OutboxService outboxService;

    @PostPersist
    public void postPersist(Outbox outbox) {
        log.info("New outbox created: {}", outbox.toString());
        CompletableFuture.runAsync(() -> outboxService.sendEvent(outbox));
    }
}
