package org.stackit.network;

public enum StatusType {

    ERROR("error", 500),
    SUCCESS("success", 200),

    BAD_REQUEST("Bad request", 400),
    UNAUTHORIZED("Unauthorized", 401),
    FORBIDDEN("Forbidden", 403),
    NOT_FOUND("Not found", 404),
    BAD_MEDIA("Bad media", 415);

    private String statusType;
    private int code;

    StatusType(String statusType, int code) {
        this.statusType = statusType;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.statusType;
    }

    public int getCode(){
        return this.code;
    }
}
