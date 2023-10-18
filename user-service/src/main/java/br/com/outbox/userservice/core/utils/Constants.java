package br.com.outbox.userservice.core.utils;

public final class Constants {

    private Constants() {

    }

    public static final String EMPTY = "";
    public static final Integer FIRST_ATTEMPT = 1;
    public static final Integer MAX_ATTEMPT = 5;
    public static final Integer ATTEMPT_NEXT_EXECUTION_IN_MINS = 1;
}
