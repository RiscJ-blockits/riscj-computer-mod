package edu.kit.riscjblockits.controller.simulation;

import edu.kit.riscjblockits.controller.assembler.Assembler;
import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.controller.blocks.AluController;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.ClockMode;
import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.busgraph.IBusSystem;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_MEMORY;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_PROGRAMM_ITEM;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RiscInstructionTest {

    private MemoryController memoryController;
    private SimulationSequenceHandler simulationSequenceHandler;
    private SystemClockController systemClockController;
    private IQueryableInstructionSetModel instructionSetModel;

    private IConnectableComputerBlockEntity getBlockEntityMock() {
        IConnectableComputerBlockEntity blockEntity = new IConnectableComputerBlockEntity() {
            @Override
            public void setBlockModel(IViewQueryableBlockModel model) {
                //
            }

            @Override
            public List<ComputerBlockController> getComputerNeighbours() {
                return new LinkedList<>();
            }

            @Override
            public BlockPosition getBlockPosition() {
                return new BlockPosition(0, 0, 0);
            }

            @Override
            public void spawnEffect(ComputerEffect effect) {
                //
            }
        };
        return blockEntity;
    }

    private IBusSystem getBusSystemMock() {
        return new IBusSystem() {

            @Override
            public void setBusDataPath(BlockPosition startPos, BlockPosition endPos, Value presentData) {

            }

            @Override
            public void resetVisualisation() {

            }

            @Override
            public void activateVisualisation() {

            }
        };
    }


    private static InstructionSetModel buildInstructionSetModelRiscV() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetRiscV.jsonc");
        try {
            return InstructionSetBuilder.buildInstructionSetModel(is);
        } catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCode(String code) throws AssemblyException {
        Assembler assembler = new Assembler(instructionSetModel);
        assembler.assemble(code);
        IDataContainer container1 = new Data();
        IDataContainer container2 = new Data();
        container1.set(MEMORY_PROGRAMM_ITEM, assembler.getMemoryData());
        container2.set(MEMORY_MEMORY, container1);
        memoryController.setData(container2);

    }

    @BeforeEach
    public void init() {
        instructionSetModel = buildInstructionSetModelRiscV();
        List<IQueryableSimController> simControllers = new ArrayList<>();
        memoryController = new MemoryController(getBlockEntityMock());
        simControllers.add(memoryController);
        ControlUnitController controlUnitController = new ControlUnitController(getBlockEntityMock());
        ((ControlUnitModel) controlUnitController.getModel()).setIstModel(instructionSetModel);
        simControllers.add(controlUnitController);
        simControllers.add(new AluController(getBlockEntityMock()));
        systemClockController = new SystemClockController(getBlockEntityMock());
        simControllers.add(systemClockController);
        for (String registerType : instructionSetModel.getRegisterNames()) {
            RegisterController registerController = new RegisterController(getBlockEntityMock());
            ((RegisterModel) registerController.getModel()).setRegisterType(registerType);
            simControllers.add(registerController);
        }
        simulationSequenceHandler = new SimulationSequenceHandler(simControllers, getBusSystemMock(), () -> {});


    }

    private void runSimulation() {
        systemClockController.setSimulationMode(ClockMode.REALTIME);
        while (systemClockController.getClockMode() == ClockMode.REALTIME) {
            simulationSequenceHandler.run();
        }
    }

    @Test
    public void test_add_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 0
                addi t1, zero, 0
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 0
                addi t1, zero, 1
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 0
                addi t1, zero, -1
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 0
                addi t1, zero, 2047
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 1
                addi t1, zero, 0
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 1
                addi t1, zero, 1
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000002", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 1
                addi t1, zero, -1
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 1
                addi t1, zero, 2047
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000800", val.getHexadecimalValue());
    }


    @Test
    public void test_add_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, -1
                addi t1, zero, 0
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_add_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, -1
                addi t1, zero, 1
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_add_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, -1
                addi t1, zero, -1
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFE", val.getHexadecimalValue());
    }


    @Test
    public void test_add_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, -1
                addi t1, zero, 2047
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 2047
                addi t1, zero, 0
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 2047
                addi t1, zero, 1
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000800", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 2047
                addi t1, zero, -1
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_add_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t0, zero, 2047
                addi t1, zero, 2047
                add t0 t0 t1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000FFE", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i0_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t0 t0 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t0 t0 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_ineg1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t0 t0 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i2047_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t0 t0 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i0_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t0 t0 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t0 t0 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_ineg1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t0 t0 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i2047_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t0 t0 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i0_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t0 t0 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t0 t0 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_ineg1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t0 t0 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i2047_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t0 t0 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i0_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t0 t0 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t0 t0 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_ineg1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t0 t0 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i2047_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t0 t0 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


}
