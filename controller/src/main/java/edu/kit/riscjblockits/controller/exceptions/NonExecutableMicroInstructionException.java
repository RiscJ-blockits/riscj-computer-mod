package edu.kit.riscjblockits.controller.exceptions;

/**
 * This exception is thrown when a micro instruction is not executable.
 */
public class NonExecutableMicroInstructionException extends RuntimeException {

    /**
     * Creates a new NonExecutableMicroInstructionException.
     * @param message the message of the exception
     */
    public NonExecutableMicroInstructionException(String message) {
        super(message);
    }

    /**
     * Creates a new NonExecutableMicroInstructionException.
     * @param message the message of the exception
     * @param cause the cause of the exception
     */
    public NonExecutableMicroInstructionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new NonExecutableMicroInstructionException.
     * @param cause the cause of the exception
     */
    public NonExecutableMicroInstructionException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new NonExecutableMicroInstructionException.
     * @param message the message of the exception
     * @param cause the cause of the exception
     * @param enableSuppression whether suppression is enabled or disabled
     * @param writableStackTrace whether the stack trace should be writable
     */
    public NonExecutableMicroInstructionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Creates a new NonExecutableMicroInstructionException.
     */
    public NonExecutableMicroInstructionException() {
    }

}
