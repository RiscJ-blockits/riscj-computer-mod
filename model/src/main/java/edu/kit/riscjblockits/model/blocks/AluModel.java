package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
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

    public AluModel() {
        setType(ModelType.ALU);
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    /**
     * Getter for the data the view needs for ui.
     * @return Data Format: key: "operation", value: "operation"
     *                      key: "operand1", value: operand1 as String
     *                      key: "operand2", value: operand2 as String
     *                      key: "result", value: result as String
     */
    @Override
    public IDataElement getData() {
        Data aluData = new Data();
        if (operation != null) {
            aluData.set("operation", new DataStringEntry(operation));
        }
        if (operand1 != null) {
            aluData.set("operand1", new DataStringEntry(operand1.getHexadecimalValue()));
        }
        if (operand2 != null) {
            aluData.set("operand2", new DataStringEntry(operand2.getHexadecimalValue()));
        }
        if (result != null) {
            aluData.set("result", new DataStringEntry(result.getHexadecimalValue()));
        }
        return aluData;
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
