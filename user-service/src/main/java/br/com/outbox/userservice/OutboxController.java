package br.com.outbox.userservice;

import br.com.outbox.userservice.core.service.OutboxService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/outbox")
public class OutboxController {

    private OutboxService outboxService;

    @GetMapping("/topic/{topic}/payload/{payload}")
    public void createOutbox(@PathVariable String topic,
                             @PathVariable String payload) {
        outboxService.create(topic, payload);
    }

    @GetMapping("/restart/{id}")
    public void createOutbox(@PathVariable Integer id) {
        outboxService.restartOutbox(id);
    }
}
