package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.Value;
import edu.kit.riscjblockits.model.data.IDataElement;

public class AluModel extends BlockModel{

    /**
     * Current alu operation
     */
    private String operation;

    /**
     * First alu operand
     */
    private Value operand1;

    /**
     * Second alu operand
     */
    private Value operand2;

    /**
     * Result of the operation
     */
    private Value result;


    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }
}
