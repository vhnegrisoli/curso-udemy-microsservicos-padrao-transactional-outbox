package br.com.outbox.userservice.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EUserAction {

    CREATE_USER("log-create-user"),
    UPDATE_USER("log-update-user"),
    DELETE_USER("log-delete-user");

    private String topic;
}
