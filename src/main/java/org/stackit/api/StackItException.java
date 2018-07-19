package org.stackit.api;

public class StackItException extends RuntimeException {
    public StackItException() {
    }

    public StackItException(String message) {
        super(message);
    }

    public StackItException(String message, Throwable cause) {
        super(message, cause);
    }

    public StackItException(Throwable cause) {
        super(cause);
    }

    public StackItException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
