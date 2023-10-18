package br.com.outbox.userservice.core.mapper;

import br.com.outbox.userservice.core.model.Outbox;
import lombok.experimental.UtilityClass;

import static br.com.outbox.userservice.core.enums.EOutboxStatus.PENDING;
import static br.com.outbox.userservice.core.utils.Constants.FIRST_ATTEMPT;

@UtilityClass
public class OutboxMapper {

    public Outbox toOutbox(String topic, String payload) {
        return Outbox
            .builder()
            .topic(topic)
            .payload(payload)
            .status(PENDING)
            .attempt(FIRST_ATTEMPT)
            .build();
    }
}
