package org.stackit;

public final class BundlerDisabledException extends StackItException {
    public BundlerDisabledException() {
    }

    public BundlerDisabledException(String message) {
        super(message);
    }

    public BundlerDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public BundlerDisabledException(Throwable cause) {
        super(cause);
    }

    public BundlerDisabledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
