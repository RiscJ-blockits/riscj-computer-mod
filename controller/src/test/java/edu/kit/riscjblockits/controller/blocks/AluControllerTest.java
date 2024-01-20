package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.AluModel;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link AluController}
 *
 * results are checked with windows calculator in programmer mode
 */
class AluControllerTest {

    private IConnectableComputerBlockEntity getBlockEntityMock() {
        IConnectableComputerBlockEntity blockEntity = new IConnectableComputerBlockEntity() {
            @Override
            public void setBlockModel(IQueryableBlockModel model) {

            }

            @Override
            public List<ComputerBlockController> getComputerNeighbours() {
                return new LinkedList<>();
            }

            @Override
            public BlockPosition getBlockPosition() {
                return new BlockPosition(0,0,0);
            }

            @Override
            public IDataElement getBlockEntityData() {
                return null;
            }
        };
        return blockEntity;
    }

    @Test
    void executeAluOperationADD() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("01", 2));
        model.setOperand2(Value.fromHex("02", 2));

        controller.executeAluOperation("ADD");

        assertEquals("0003", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationADD_negative() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("FFF2", 2));
        model.setOperand2(Value.fromHex("03", 2));

        controller.executeAluOperation("ADD");

        assertEquals("FFF5", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationNONE() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("01", 2));
        model.setOperand2(Value.fromHex("02", 2));

        controller.executeAluOperation("None");

        assertEquals("0000", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationRR() {


        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("03", 2));
        model.setOperand2(Value.fromHex("02", 2));

        controller.executeAluOperation("RR");

        byte[] big = new BigInteger(new byte[] {1,2}).toByteArray();
        byte[] result = new byte[2];

        System.arraycopy(big, 0, result, 0, 2);

        assertEquals("1000000000000001", model.getResult().getBinaryValue());
    }

    @Test
    void executeAluOperationAND() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("03", 2));
        model.setOperand2(Value.fromHex("05", 2));

        controller.executeAluOperation("AND");

        assertEquals(Value.fromHex("01", 2), model.getResult());
    }

    @Test
    void executeAluOperationOR() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("03", 2));
        model.setOperand2(Value.fromHex("05", 2));

        controller.executeAluOperation("OR");

        assertEquals(Value.fromHex("07", 2), model.getResult());
    }

    @Test
    void executeAluOperationXOR() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("03", 2));
        model.setOperand2(Value.fromHex("05", 2));

        controller.executeAluOperation("XOR");

        assertEquals(Value.fromHex("06", 2), model.getResult());
    }

    @Test
    void executeAluOperationNEG() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("03", 2));
        model.setOperand2(Value.fromHex("05", 2));

        controller.executeAluOperation("NEG");

        assertEquals(Value.fromBinary("1111111111111100", 2), model.getResult());
    }

    @Test
    void executeAluOperationSUB_negative() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("03", 2));
        model.setOperand2(Value.fromHex("05", 2));

        controller.executeAluOperation("SUB");

        assertEquals("FFFE", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationSUB() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("05", 2));
        model.setOperand2(Value.fromHex("03", 2));

        controller.executeAluOperation("SUB");

        assertEquals("0002", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMUL() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("03", 2));
        model.setOperand2(Value.fromHex("05", 2));

        controller.executeAluOperation("MUL");

        assertEquals("000F", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMUL_neg() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("FFFC", 2));
        model.setOperand2(Value.fromHex("02", 2));

        controller.executeAluOperation("MUL");

        assertEquals("FFF8", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMUL_big() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("7FFC", 2));
        model.setOperand2(Value.fromHex("03", 2));

        controller.executeAluOperation("MUL");

        assertEquals("7FF4", model.getResult().getHexadecimalValue());
    }

    //TODO check mulh tests
    @Test
    void executeAluOperationMULH() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("03", 2));
        model.setOperand2(Value.fromHex("05", 2));

        controller.executeAluOperation("MULH");

        assertEquals("000F", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMULH_neg() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("FFFC", 2));
        model.setOperand2(Value.fromHex("02", 2));

        controller.executeAluOperation("MULH");

        assertEquals("00F8", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMULH_big() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("7FFC", 2));
        model.setOperand2(Value.fromHex("03", 2));

        controller.executeAluOperation("MULH");

        assertEquals("00F4", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationDIV() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("20", 2));
        model.setOperand2(Value.fromHex("08", 2));

        controller.executeAluOperation("DIV");

        assertEquals("0004", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationDIV_underflow() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("04", 2));
        model.setOperand2(Value.fromHex("08", 2));

        controller.executeAluOperation("DIV");

        assertEquals("0000", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationDIV_byZero() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("04", 2));
        model.setOperand2(Value.fromHex("00", 2));

        controller.executeAluOperation("DIV");

        assertEquals("FFFF", model.getResult().getHexadecimalValue());
    }

}