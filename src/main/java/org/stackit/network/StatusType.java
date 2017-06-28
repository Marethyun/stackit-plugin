package org.stackit.network;

public enum StatusType {
    ERROR("error"),
    SUCCESS("success");

    private String statusType;

    StatusType(String statusType) {
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return "\"" + statusType + "\"";
    }
}
