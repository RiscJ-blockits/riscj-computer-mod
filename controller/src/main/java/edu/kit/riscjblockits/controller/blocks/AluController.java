package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.blocks.AluModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

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
        MIMA:
        "None",
        "ADD",
        "RR",
        "AND",
        "OR",
        "XOR",
        "NEG",
        "JMN"
        */

        /*
        RISC-V:
        "AND",
        "OR",
        "ADD",
        "SUB",
        "XOR",
        "SLL",
        "SRL",
        "SRA",
        "MUL",
        "MULH",										//Unsigned-Operationen sind nötig für die Unterscheidung
        "MULHSU",
        "MULHU",
        "DIV",
        "DIVU",
        "REM",
        "REMU"
         */


        Value operand1 = ((AluModel) getModel()).getOperand1();
        Value operand2 = ((AluModel) getModel()).getOperand2();
        Value result = null;

        switch (operation) {
            case "None":
                result = Value.fromBinary("0", operand1.getByteValue().length);
                break;
            case "ADD":
                result = add(operand1, operand2);
                break;
            case "SUB":
                result = sub(operand1, neg(operand2, null));
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
            case "SLL":
                result = sll(operand1, operand2);
                break;
            case "SRL":
                result = srl(operand1, operand2);
                break;
            case "SRA":
                result = sra(operand1, operand2);
                break;
            case "MUL":
                result = mul(operand1, operand2);
                break;
            case "MULH":
                result = mulh(operand1, operand2);
                break;
            case "MULHSU":
                result = mulhsu(operand1, operand2);
                break;
            case "MULHU":
                result = mulhu(operand1, operand2);
                break;
            case "DIV":
                result = div(operand1, operand2);
                break;
            case "DIVU":
                result = divu(operand1, operand2);
                break;
            case "REM":
                result = rem(operand1, operand2);
                break;
            case "REMU":
                result = remu(operand1, operand2);
                break;
            default:
                //null by default, considering exception
                break;
        }

        ((AluModel) getModel()).setResult(result);
    }

    private Value remu(Value operand1, Value operand2) {
        return null;
    }

    private Value rem(Value operand1, Value operand2) {
        return null;
    }

    private Value divu(Value operand1, Value operand2) {
        return null;
    }

    private Value div(Value operand1, Value operand2) {
        return null;
    }

    private Value mulhu(Value operand1, Value operand2) {
        return null;
    }

    private Value mulhsu(Value operand1, Value operand2) {
        return null;
    }

    private Value mulh(Value operand1, Value operand2) {
        return null;
    }

    private Value mul(Value operand1, Value operand2) {
        return null;
    }

    private Value sra(Value operand1, Value operand2) {
        return null;
    }

    private Value srl(Value operand1, Value operand2) {
        return null;
    }

    private Value sll(Value operand1, Value operand2) {
        return null;
    }

    private Value sub(Value operand1, Value operand2) {
        byte[] array1 = operand1.getByteValue();
        BigInteger bigInt1 = new BigInteger(array1);

        byte[] array2 = operand2.getByteValue();
        BigInteger bigInt2 = new BigInteger(array2);

        BigInteger result = bigInt1.subtract(bigInt2);

        return new Value(result.toByteArray());
    }

    /**
     * Add two values
     * @param operand1 first value
     * @param operand2 second value
     * @return sum of operand1 and operand2
     */
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

    /**
     * Rotate first value right by one
     * @param operand1 first value
     * @param operand2 second value, unused
     * @return rotated operand1
     */
    private Value rr(Value operand1, Value operand2) {

            byte[] array1 = operand1.getByteValue();

            byte[] result = new byte[array1.length];

            int lsb = array1[array1.length - 1] & 1;

            BigInteger bigInt = new BigInteger(array1);
            bigInt = bigInt.shiftRight(1);
            lsb = lsb << 7;
            byte[] shortRes = bigInt.toByteArray();
            System.arraycopy(shortRes, 0, result, result.length - shortRes.length, shortRes.length);
            result[0] = (byte) (result[0] + (byte) lsb);

            return new Value(result);
    }

    /**
     * Logical conjunction of two values
     * @param operand1 first value
     * @param operand2 second value
     * @return conjunction of operand1 and operand2
     */
    private Value and(Value operand1, Value operand2) {

        byte[] array1 = operand1.getByteValue();
        byte[] array2 = operand2.getByteValue();

        byte[] result = new byte[Math.max(array1.length, array2.length)];

        for (int i = 0; i < Math.min(array1.length, array2.length); i++) {
            result[i] = (byte) (array1[i] & array2[i]);
        }

        return new Value(result);
    }

    /**
     * Logical disjunction of two values
     * @param operand1 first value
     * @param operand2 second value
     * @return disjunction of operand1 and operand2
     */
    private Value or(Value operand1, Value operand2) {
        byte[] array1 = operand1.getByteValue();
        byte[] array2 = operand2.getByteValue();

        byte[] result = new byte[Math.max(array1.length, array2.length)];

        for (int i = 0; i < Math.min(array1.length, array2.length); i++) {
            result[i] = (byte) (array1[i] | array2[i]);
        }

        return new Value(result);
    }

    /**
     * Logical exclusive disjunction of two values
     * @param operand1 first value
     * @param operand2 second value
     * @return exclusive disjunction of operand1 and operand2
     */
    private Value xor(Value operand1, Value operand2) {
        byte[] array1 = operand1.getByteValue();
        byte[] array2 = operand2.getByteValue();

        byte[] result = new byte[Math.max(array1.length, array2.length)];

        for (int i = 0; i < Math.min(array1.length, array2.length); i++) {
            result[i] = (byte) (array1[i] ^ array2[i]);
        }

        return new Value(result);
    }

    /**
     * Logical negation of first value
     * @param operand1 first value
     * @param operand2 second value, unused
     * @return negated operand1
     */
    private Value neg(Value operand1, Value operand2) {

        byte[] array1 = operand1.getByteValue();
        byte[] result = new byte[array1.length];

        for (int i = 0; i < array1.length; i++) {
            result[i] = (byte) ~array1[i];
        }

        return new Value(result);

    }

}
