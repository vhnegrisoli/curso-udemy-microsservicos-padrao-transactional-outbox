package br.com.outbox.logservice.core.dto;

import br.com.outbox.logservice.core.enums.EUserAction;
import br.com.outbox.logservice.core.enums.EUserStatus;

public record LogFilters(
    String username,
    String fullName,
    String document,
    String email,
    EUserStatus status,
    EUserAction action,
    String httpMethod,
    String url
) {
}
