package br.com.outbox.userservice.core.dto;

import br.com.outbox.userservice.core.enums.EUserAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private UserDTO user;
    private EUserAction action;
    private String httpMethod;
    private String url;
    private LocalDateTime createdAt;
}
