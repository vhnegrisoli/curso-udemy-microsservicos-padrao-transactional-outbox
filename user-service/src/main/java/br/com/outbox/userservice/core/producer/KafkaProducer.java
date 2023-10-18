package br.com.outbox.userservice.core.producer;

import br.com.outbox.userservice.core.dto.ProducerResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerResponse sendEvent(String payload, String topic) {
        try {
            log.info("Sending event to topic {} with data {}", topic, payload);
            return kafkaTemplate
                .send(topic, payload)
                .handle((result, ex) -> handleProducerResponse(result, ex, topic, payload))
                .get();
        } catch (Exception ex) {
            log.error("Error trying to send data to topic {} with data {} - {}",
                topic, payload, ex.getCause().getMessage());
            return new ProducerResponse(false, ex.getCause().getMessage());
        }
    }

    private ProducerResponse handleProducerResponse(SendResult<String, String> result,
                                           Throwable ex,
                                           String topic,
                                           String payload) {
        if (isEmpty(ex)) {
            log.info("Data produced successfully!");
            return new ProducerResponse(true, null);
        } else {
            log.error("Error after producing data to topic {} with data {} - {}",
                topic, payload, ex.getCause().getMessage());
            return new ProducerResponse(false, ex.getCause().getMessage());
        }
    }
}

