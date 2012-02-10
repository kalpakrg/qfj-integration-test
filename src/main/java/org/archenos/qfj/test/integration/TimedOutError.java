package org.archenos.qfj.test.integration;

public final class TimedOutError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TimedOutError() {

    }

    public TimedOutError(String message) {
        super(message);
    }

    public TimedOutError(Throwable cause) {
        super(cause);
    }

    public TimedOutError(String message, Throwable cause) {
        super(message, cause);
    }
}
