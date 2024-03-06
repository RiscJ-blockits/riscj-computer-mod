package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.AluModel;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AluControllerTest {

    private AluController aluController;

    @BeforeEach
    void init() {
        aluController = new AluController(new ArchiCheckStub_Entity());
    }

    @Test
    void setData() {
        Data aluData = new Data();
        aluData.set("operation", new DataStringEntry("add"));
        aluController.setData(aluData);
        AluModel aluModel = (AluModel) aluController.getModel();
        aluData = (Data) aluModel.getData();
        assertEquals("add", ((IDataStringEntry) (aluData.get("operation"))).getContent());
    }

    private IConnectableComputerBlockEntity getBlockEntityMock() {
        IConnectableComputerBlockEntity blockEntity = new IConnectableComputerBlockEntity() {

            @Override
            public void setBlockModel(IViewQueryableBlockModel model) {

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
            public void spawnEffect(ComputerEffect effect) {
                //
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

    @Test
    void executeAluOperationMULH() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("03", 2));
        model.setOperand2(Value.fromHex("05", 2));

        controller.executeAluOperation("MULH");

        assertEquals("0000", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMULH_neg() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("FFFC", 2));
        model.setOperand2(Value.fromHex("02", 2));

        controller.executeAluOperation("MULH");

        assertEquals("FFFF", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMULH_big() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("7FFC", 2));
        model.setOperand2(Value.fromHex("03", 2));

        controller.executeAluOperation("MULH");

        assertEquals("0001", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMULHU() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("03", 2));
        model.setOperand2(Value.fromHex("05", 2));

        controller.executeAluOperation("MULHU");

        assertEquals("0000", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMULHU_neg() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("FFFF", 2));
        model.setOperand2(Value.fromHex("0FFF", 2));

        controller.executeAluOperation("MULHU");

        assertEquals("0FFE", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMULHSU() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("FFFF", 2));
        model.setOperand2(Value.fromHex("0FFF", 2));

        controller.executeAluOperation("MULHSU");

        assertEquals("FFFF", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationMULHSU_neg() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("0FFF", 2));
        model.setOperand2(Value.fromHex("FFFF", 2));

        controller.executeAluOperation("MULHSU");

        assertEquals("0FFE", model.getResult().getHexadecimalValue());
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

    @Test
    void executeAluOperationDIV_neg() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("FFFE", 2));
        model.setOperand2(Value.fromHex("B2", 2));

        controller.executeAluOperation("DIV");

        assertEquals("0000", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationDIVU() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("20", 2));
        model.setOperand2(Value.fromHex("08", 2));

        controller.executeAluOperation("DIVU");

        assertEquals("0004", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationDIVU_underflow() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("04", 2));
        model.setOperand2(Value.fromHex("08", 2));

        controller.executeAluOperation("DIVU");

        assertEquals("0000", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationDIVU_byZero() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("04", 2));
        model.setOperand2(Value.fromHex("00", 2));

        controller.executeAluOperation("DIVU");

        assertEquals("FFFF", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationDIVU_neg() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("FFFE", 2));
        model.setOperand2(Value.fromHex("B2", 2));

        controller.executeAluOperation("DIVU");

        assertEquals("0170", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationSRA() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromBinary("1000000011111011", 2));
        model.setOperand2(Value.fromHex("02", 2));

        controller.executeAluOperation("SRA");

        assertEquals("1110000000111110", model.getResult().getBinaryValue());
    }

    @Test
    void executeAluOperationSRA_noSign() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromBinary("0000000011111011", 2));
        model.setOperand2(Value.fromHex("02", 2));

        controller.executeAluOperation("SRA");

        assertEquals("0000000000111110", model.getResult().getBinaryValue());
    }

    @Test
    void executeAluOperationSRA_moreThan5Bits() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromBinary("0000000011111011", 2));
        model.setOperand2(Value.fromBinary("0000000011100001", 2));

        controller.executeAluOperation("SRA");

        assertEquals("0000000001111101", model.getResult().getBinaryValue());
    }

    @Test
    void executeAluOperationSRL() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromBinary("1000000011111011", 2));
        model.setOperand2(Value.fromHex("02", 2));

        controller.executeAluOperation("SRL");

        assertEquals("0010000000111110", model.getResult().getBinaryValue());
    }

    @Test
    void executeAluOperationSRL_noSign() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromBinary("0000000011111011", 2));
        model.setOperand2(Value.fromHex("02", 2));

        controller.executeAluOperation("SRL");

        assertEquals("0000000000111110", model.getResult().getBinaryValue());
    }

    @Test
    void executeAluOperationSRL_moreThan5Bits() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromBinary("0000000011111011", 2));
        model.setOperand2(Value.fromBinary("0000000011100001", 2));

        controller.executeAluOperation("SRL");

        assertEquals("0000000001111101", model.getResult().getBinaryValue());
    }

    @Test
    void executeAluOperationSLL() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromBinary("0000000011111011", 2));
        model.setOperand2(Value.fromBinary("0000000000000011", 2));

        controller.executeAluOperation("SLL");

        assertEquals("0000011111011000", model.getResult().getBinaryValue());
    }

    @Test
    void executeAluOperationSLL_msb() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromBinary("1100000011111011", 2));
        model.setOperand2(Value.fromBinary("0000000000000011", 2));

        controller.executeAluOperation("SLL");

        assertEquals("0000011111011000", model.getResult().getBinaryValue());
    }

    @Test
    void executeAluOperationREM() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("32AC", 2));
        model.setOperand2(Value.fromHex("1234", 2));

        controller.executeAluOperation("REM");

        assertEquals("0E44", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationREM_neg() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("FFAC", 2));
        model.setOperand2(Value.fromHex("0012", 2));

        controller.executeAluOperation("REM");

        assertEquals("FFF4", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationREMU() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("32AC", 2));
        model.setOperand2(Value.fromHex("1234", 2));

        controller.executeAluOperation("REM");

        assertEquals("0E44", model.getResult().getHexadecimalValue());
    }

    @Test
    void executeAluOperationREMU_neg() {

        AluController controller = new AluController(getBlockEntityMock());

        AluModel model = (AluModel) controller.getModel();
        model.setOperand1(Value.fromHex("FFAC", 2));
        model.setOperand2(Value.fromHex("0012", 2));

        controller.executeAluOperation("REMU");

        assertEquals("0004", model.getResult().getHexadecimalValue());
    }
}