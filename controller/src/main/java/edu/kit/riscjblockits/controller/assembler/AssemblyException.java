package edu.kit.riscjblockits.controller.assembler;

/**
 * This class represents an exception thrown when the assembly fails.
 */
public class AssemblyException extends Exception{

    /**
     * Constructor for an assembly exception
     * @param errorMessage the error message
     */
    public AssemblyException(String errorMessage) {
        super(errorMessage);
    }

}
