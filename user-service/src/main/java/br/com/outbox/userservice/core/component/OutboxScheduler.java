package br.com.outbox.userservice.core.component;

import br.com.outbox.userservice.core.service.OutboxService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OutboxScheduler {

    private OutboxService outboxService;

    @Scheduled(fixedDelayString = "${app.scheduling.scheduler}")
    public void resumeOutbox() {
        outboxService.resumeOutbox();
    }
}
