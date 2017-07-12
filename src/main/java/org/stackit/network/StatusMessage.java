package org.stackit.network;

public enum StatusMessage {

    // success
    SUCCESS("success"),
    ERROR("error"),

    INVALID_TOKEN("The provided token is invalid"),
    BAD_REQUEST("Bad request"),
    INVALID_CREDENTIALS("Bad credentials"),
    BAD_MEDIA("Bad media"),
    ELEMENT_NOT_FOUND("Not found");

    String message;

    StatusMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
