package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.model.data.IDataElement;

public class AluModel extends BlockModel{

    /**
     * Current alu operation
     */
    private String operation;

    public String getOperation() {
        return operation;
    }

    public Value getOperand1() {
        return operand1;
    }

    public Value getOperand2() {
        return operand2;
    }

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

    public AluModel() {
        setType(ModelType.ALU);
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    @Override
    public IDataElement getData() {
        return null;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setOperand1(Value operand1) {
        this.operand1 = operand1;
    }

    public void setOperand2(Value operand2) {
        this.operand2 = operand2;
    }

    public Value getResult() {
        return result;
    }

    public void setResult(Value result) {
        this.result = result;
    }

}
