package edu.kit.riscjblockits.controller.exceptions;

public class NonExecutableMicroInstructionException extends RuntimeException {
    public NonExecutableMicroInstructionException(String message) {
        super(message);
    }

    public NonExecutableMicroInstructionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExecutableMicroInstructionException(Throwable cause) {
        super(cause);
    }

    public NonExecutableMicroInstructionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NonExecutableMicroInstructionException() {
    }
}
