package org.stackit;

public final class BundleViolationException extends StackItException {
    public BundleViolationException() {
    }

    public BundleViolationException(String message) {
        super(message);
    }

    public BundleViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BundleViolationException(Throwable cause) {
        super(cause);
    }

    public BundleViolationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
