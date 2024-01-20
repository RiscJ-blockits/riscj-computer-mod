package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.blocks.AluModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import java.math.BigInteger;

/**
 * The controller for an ALU block entity.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class AluController extends ComputerBlockController {

    /**
     * Creates a new AluController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public AluController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
        setControllerType(BlockControllerType.ALU);
    }

    /**
     * Creates the Alu specific model.
     * @return The model for the Alu.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new AluModel();
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        //ToDo
        ((AluModel) getModel()).setOperation(null);
    }

    /**
     * Executes the alu operation by accessing the model and setting all relevant values
     * @param operation Alu operation to execute
     */
    public void executeAluOperation(String operation) {
        //ToDo
        /*

        "None",
        "ADD",
        "RR",
        "AND",
        "OR",
        "XOR",
        "NEG",
        "JMN"

        */

        Value operand1 = ((AluModel) getModel()).getOperand1();
        Value operand2 = ((AluModel) getModel()).getOperand2();
        Value result = null;

        switch (operation) {
            case "ADD":
                result = add(operand1, operand2);
                break;
            case "RR":
                result = rr(operand1, operand2);
                break;
            case "AND":
                result = and(operand1, operand2);
                break;
            case "OR":
                result = or(operand1, operand2);
                break;
            case "XOR":
                result = xor(operand1, operand2);
                break;
            case "NEG":
                result = neg(operand1, operand2);
                break;
            default:
                //null by default, considering exception
                break;
        }

        ((AluModel) getModel()).setResult(result);
    }

    private Value add(Value operand1, Value operand2) {

        byte[] array1 = operand1.getByteValue();
        byte[] array2 = operand2.getByteValue();

        //Algorithm by Copilot Chat
        int maxLength = Math.max(array1.length, array2.length);
        byte[] result = new byte[maxLength]; // +1 for possible carry-over

        int carry = 0;
        for (int i = 0; i < maxLength; i++) {
            int value1 = i < array1.length ? Byte.toUnsignedInt(array1[array1.length - 1 - i]) : 0;
            int value2 = i < array2.length ? Byte.toUnsignedInt(array2[array2.length - 1 - i]) : 0;
            int sum = value1 + value2 + carry;
            result[result.length - 1 - i] = (byte) (sum & 0xFF); // Keep only the last 8 bits
            carry = sum >>> 8; // Shift the remaining bits to get the carry
        }

        return new Value(result);

    }

    private Value rr(Value operand1, Value operand2) {

            byte[] array1 = operand1.getByteValue();

            byte[] result = new byte[array1.length];

            int lsb = array1[array1.length - 1] & 1;

            BigInteger bigInt = new BigInteger(array1);
            bigInt = bigInt.shiftRight(1);
            lsb = lsb << 7;
            result = bigInt.toByteArray();
            result [0] = (byte) (result[0] + (byte) lsb);

            return new Value(result);
    }

    private Value and(Value operand1, Value operand2) {

        byte[] array1 = operand1.getByteValue();
        byte[] array2 = operand2.getByteValue();

        byte[] result = new byte[Math.max(array1.length, array2.length)];

        for (int i = 0; i < Math.min(array1.length, array2.length); i++) {
            result[i] = (byte) (array1[i] & array2[i]);
        }

        return new Value(result);
    }

    private Value or(Value operand1, Value operand2) {
        byte[] array1 = operand1.getByteValue();
        byte[] array2 = operand2.getByteValue();

        byte[] result = new byte[Math.max(array1.length, array2.length)];

        for (int i = 0; i < Math.min(array1.length, array2.length); i++) {
            result[i] = (byte) (array1[i] | array2[i]);
        }

        return new Value(result);
    }

    private Value xor(Value operand1, Value operand2) {
        byte[] array1 = operand1.getByteValue();
        byte[] array2 = operand2.getByteValue();

        byte[] result = new byte[Math.max(array1.length, array2.length)];

        for (int i = 0; i < Math.min(array1.length, array2.length); i++) {
            result[i] = (byte) (array1[i] ^ array2[i]);
        }

        return new Value(result);
    }

    private Value neg(Value operand1, Value operand2) {

        byte[] array1 = operand1.getByteValue();
        byte[] result = new byte[array1.length];

        for (int i = 0; i < array1.length; i++) {
            result[i] = (byte) ~array1[i];
        }

        return new Value(result);

    }

}
