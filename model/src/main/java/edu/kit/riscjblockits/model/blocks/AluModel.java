package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.model.data.IDataElement;

import static edu.kit.riscjblockits.model.data.DataConstants.ALU_OPERAND1;
import static edu.kit.riscjblockits.model.data.DataConstants.ALU_OPERAND2;
import static edu.kit.riscjblockits.model.data.DataConstants.ALU_OPERATION;
import static edu.kit.riscjblockits.model.data.DataConstants.ALU_RESULT;

/**
 * Represents an alu model. Every alu in the game has one.
 */
public class AluModel extends BlockModel{

    /**
     * Current alu operation.
     */
    private String operation;

    /**
     * First alu operand.
     */
    private Value operand1;

    /**
     * Second alu operand.
     */
    private Value operand2;

    /**
     * Result of the operation.
     */
    private Value result;

    /**
     * Constructor. Return a model for an alu block.
     */
    public AluModel() {
        setType(ModelType.ALU);
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
            aluData.set(ALU_OPERATION, new DataStringEntry(operation));
        }
        if (operand1 != null) {
            aluData.set(ALU_OPERAND1, new DataStringEntry(operand1.getHexadecimalValue()));
        }
        if (operand2 != null) {
            aluData.set(ALU_OPERAND2, new DataStringEntry(operand2.getHexadecimalValue()));
        }
        if (result != null) {
            aluData.set(ALU_RESULT, new DataStringEntry(result.getHexadecimalValue()));
        }
        setUnqueriedStateChange(false);
        return aluData;
    }

    /**
     * See {@link edu.kit.riscjblockits.controller.blocks.AluController#executeAluOperation(String)} for implemented operations.
     */
    public void setOperation(String operation) {
        this.operation = operation;
        setUnqueriedStateChange(true);
    }

    /**
     * @param operand1 First operand of the alu.
     */
    public void setOperand1(Value operand1) {
        this.operand1 = operand1;
        setUnqueriedStateChange(true);
    }

    /**
     * @param operand2 Second operand of the alu.
     */
    public void setOperand2(Value operand2) {
        this.operand2 = operand2;
        setUnqueriedStateChange(true);
    }

    /**
     * @return The result of the alu operation.
     */
    public Value getResult() {
        return result;
    }

    /**
     * @param result The result of the alu operation.
     */
    public void setResult(Value result) {
        this.result = result;
        setUnqueriedStateChange(true);
    }

}
