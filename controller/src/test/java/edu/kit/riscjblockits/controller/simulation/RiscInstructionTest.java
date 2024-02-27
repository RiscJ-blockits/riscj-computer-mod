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
            ((RegisterModel) registerController.getModel()).setWordLength(4);
            ((RegisterModel) registerController.getModel()).setRegisterType(registerType);
            simControllers.add(registerController);
        }
        simulationSequenceHandler = new SimulationSequenceHandler(simControllers, getBusSystemMock(), () -> {
        });


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
                addi t1, zero, 0
                addi t2, zero, 0
                add t0 t1 t2
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
                addi t1, zero, 0
                addi t2, zero, 1
                add t0 t1 t2
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
                addi t1, zero, 0
                addi t2, zero, -1
                add t0 t1 t2
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
                addi t1, zero, 0
                addi t2, zero, 2047
                add t0 t1 t2
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
                addi t1, zero, 1
                addi t2, zero, 0
                add t0 t1 t2
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
                addi t1, zero, 1
                addi t2, zero, 1
                add t0 t1 t2
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
                addi t1, zero, 1
                addi t2, zero, -1
                add t0 t1 t2
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
                addi t1, zero, 1
                addi t2, zero, 2047
                add t0 t1 t2
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
                addi t1, zero, -1
                addi t2, zero, 0
                add t0 t1 t2
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
                addi t1, zero, -1
                addi t2, zero, 1
                add t0 t1 t2
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
                addi t1, zero, -1
                addi t2, zero, -1
                add t0 t1 t2
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
                addi t1, zero, -1
                addi t2, zero, 2047
                add t0 t1 t2
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
                addi t1, zero, 2047
                addi t2, zero, 0
                add t0 t1 t2
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
                addi t1, zero, 2047
                addi t2, zero, 1
                add t0 t1 t2
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
                addi t1, zero, 2047
                addi t2, zero, -1
                add t0 t1 t2
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
                addi t1, zero, 2047
                addi t2, zero, 2047
                add t0 t1 t2
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
                addi t0 t1 0
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
                addi t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_ineg1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i2047_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i0_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t0 t1 1
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
                addi t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000002", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_ineg1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i2047_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000800", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i0_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t0 t1 -1
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
                addi t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_ineg1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFE", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i2047_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i0_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t0 t1 2047
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
                addi t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000800", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_ineg1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_addi_i2047_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000FFE", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_and_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_and_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_and_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_and_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_and_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                and t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_sll_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                sll t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sll_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                sll t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sll_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                sll t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sll_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                sll t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sll_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                sll t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sll_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                sll t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000002", val.getHexadecimalValue());
    }


    @Test
    public void test_sll_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                sll t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sll_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                sll t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slli_i0_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                slli t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slli_i1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                slli t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slli_ineg1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                slli t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_slli_i2047_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                slli t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_slli_i0_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                slli t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slli_i1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                slli t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000002", val.getHexadecimalValue());
    }


    @Test
    public void test_slli_ineg1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                slli t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFE", val.getHexadecimalValue());
    }


    @Test
    public void test_slli_i2047_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                slli t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000FFE", val.getHexadecimalValue());
    }


    @Test
    public void test_lui_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                lui t0 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_lui_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                lui t0 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00001000", val.getHexadecimalValue());
    }


    @Test
    public void test_lui_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                lui t0 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF000", val.getHexadecimalValue());
    }


    @Test
    public void test_lui_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                lui t0 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("007FF000", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i0_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                slti t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                slti t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_ineg1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                slti t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i2047_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                slti t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i0_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                slti t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                slti t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_ineg1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                slti t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i2047_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                slti t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i0_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                slti t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                slti t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_ineg1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                slti t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i2047_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                slti t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i0_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                slti t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                slti t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_ineg1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                slti t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slti_i2047_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                slti t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i0_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                sltiu t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                sltiu t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_ineg1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                sltiu t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i2047_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                sltiu t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i0_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                sltiu t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                sltiu t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_ineg1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                sltiu t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i2047_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                sltiu t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i0_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                sltiu t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                sltiu t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_ineg1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                sltiu t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i2047_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                sltiu t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i0_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                sltiu t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                sltiu t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_ineg1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                sltiu t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltiu_i2047_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                sltiu t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i0_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                xori t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                xori t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_ineg1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                xori t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i2047_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                xori t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i0_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                xori t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                xori t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_ineg1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                xori t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFE", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i2047_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                xori t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i0_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                xori t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                xori t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFE", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_ineg1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                xori t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i2047_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                xori t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF800", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i0_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                xori t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                xori t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_ineg1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                xori t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF800", val.getHexadecimalValue());
    }


    @Test
    public void test_xori_i2047_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                xori t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i0_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                ori t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                ori t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_ineg1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                ori t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i2047_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                ori t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i0_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                ori t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                ori t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_ineg1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                ori t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i2047_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                ori t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i0_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                ori t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                ori t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_ineg1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                ori t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i2047_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                ori t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i0_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                ori t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                ori t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_ineg1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                ori t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_ori_i2047_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                ori t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i0_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                andi t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                andi t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_ineg1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                andi t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i2047_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                andi t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i0_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                andi t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                andi t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_ineg1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                andi t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i2047_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                andi t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i0_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                andi t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                andi t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_ineg1_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                andi t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i2047_neg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                andi t0 t1 -1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i0_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                andi t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                andi t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_ineg1_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                andi t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_andi_i2047_2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                andi t0 t1 2047
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_srli_i0_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                srli t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srli_i1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                srli t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_srli_ineg1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                srli t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_srli_i2047_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                srli t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_srli_i0_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                srli t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srli_i1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                srli t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srli_ineg1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                srli t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7FFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_srli_i2047_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                srli t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000003FF", val.getHexadecimalValue());
    }


    @Test
    public void test_srai_i0_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                srai t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srai_i1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                srai t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_srai_ineg1_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                srai t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_srai_i2047_0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                srai t0 t1 0
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_srai_i0_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                srai t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srai_i1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                srai t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srai_ineg1_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                srai t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_srai_i2047_1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                srai t0 t1 1
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000003FF", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF801", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000002", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF802", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFE", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF800", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000800", val.getHexadecimalValue());
    }


    @Test
    public void test_sub_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                sub t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_slt_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                slt t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sltu_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                sltu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFE", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFE", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF800", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF800", val.getHexadecimalValue());
    }


    @Test
    public void test_xor_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                xor t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srl_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                srl t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srl_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                srl t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srl_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                srl t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srl_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                srl t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srl_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                srl t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_srl_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                srl t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srl_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                srl t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_srl_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                srl t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sra_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                sra t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sra_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                sra t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sra_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                sra t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sra_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                sra t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sra_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                sra t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_sra_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                sra t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sra_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                sra t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_sra_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                sra t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_or_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                or t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF801", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF801", val.getHexadecimalValue());
    }


    @Test
    public void test_mul_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                mul t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("003FF001", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_mulh_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                mulh t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhsu_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                mulhsu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFE", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FE", val.getHexadecimalValue());
    }


    @Test
    public void test_mulhu_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                mulhu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_div_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_div_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_div_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_div_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFF801", val.getHexadecimalValue());
    }


    @Test
    public void test_div_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                div t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00200400", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_divu_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                divu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_rem_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                rem t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i0_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 0
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i0_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 1
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i0_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, -1
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i0_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 0
                addi t2, zero, 2047
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 0
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 1
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, -1
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 1
                addi t2, zero, 2047
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_ineg1_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 0
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFFFFFFF", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_ineg1_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 1
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_ineg1_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, -1
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_ineg1_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, -1
                addi t2, zero, 2047
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000003FF", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i2047_i0() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 0
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i2047_i1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 1
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i2047_ineg1() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, -1
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("000007FF", val.getHexadecimalValue());
    }


    @Test
    public void test_remu_i2047_i2047() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            .text
            main:
                addi t1, zero, 2047
                addi t2, zero, 2047
                remu t0 t1 t2
                addi t6, zero, x
                sw t0, 0(t6)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }

    @Test
    public void test_fadd_s_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("40000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("402B74BC", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3E92250E", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("402B74BC", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("4056E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F76E579", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3E92250E", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F76E579", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fadd_s_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fadd.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BFB6ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BFD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF2DD2F2", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FDB76BC", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F2DD2F2", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("4019301B", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BFDB76BC", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("C019301B", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsub_s_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsub.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("40346B2B", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF999158", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF02B684", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF999158", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF02B684", val.getHexadecimalValue());
    }


    @Test
    public void test_fmul_s_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmul.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F02B684", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFC00000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F1878C2", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("002CC862", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BFB32186", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("004B30B2", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("C0166164", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7ED9E693", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BED9E693", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80200000", val.getHexadecimalValue());
    }


    @Test
    public void test_fdiv_s_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fdiv.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsqrt_s_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                fsqrt.s ft0 ft1
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsqrt_s_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                fsqrt.s ft0 ft1
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsqrt_s_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                fsqrt.s ft0 ft1
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FA5DB8A", val.getHexadecimalValue());
    }


    @Test
    public void test_fsqrt_s_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                fsqrt.s ft0 ft1
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("5F5866B4", val.getHexadecimalValue());
    }


    @Test
    public void test_fsqrt_s_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                fsqrt.s ft0 ft1
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FFC00000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BFD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnj_s_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnj.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BFD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BFD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BFD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BFD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjn_s_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjn.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("80000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BFD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("FF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fsgnjx_s_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fsgnjx.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmin_s_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmin.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3F800000", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("3FD6E979", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("7F36ED79", val.getHexadecimalValue());
    }


    @Test
    public void test_fmax_s_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fmax.s ft0 ft1 ft2
                addi t0, zero, x
                fsw ft0, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("BF36ED79", val.getHexadecimalValue());
    }

    @Test
    public void test_feq_si_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_feq_si_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                feq.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_flt_si_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                flt.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x00000000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x00000000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x00000000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x00000000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x00000000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x00000000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x3f800000_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x3f800000_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x3f800000_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x3f800000_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x3f800000_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3f800000
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x3fd6e979_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x3fd6e979_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x3fd6e979_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x3fd6e979_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x3fd6e979_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x3fd6e979
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x7f36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x7f36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x7f36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x7f36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0x7f36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0x7f36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000000", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0xbf36ed79_i0x00000000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x00000000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0xbf36ed79_i0x3f800000() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3f800000
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0xbf36ed79_i0x3fd6e979() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x3fd6e979
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0xbf36ed79_i0x7f36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0x7f36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


    @Test
    public void test_fle_si_i0xbf36ed79_i0xbf36ed79() throws AssemblyException {
        setCode("""
            .data
            x: .word 0
            i0: .word 0xbf36ed79
            i1: .word 0xbf36ed79
            .text
            main:
                addi t0, zero, i0
                flw ft1, 0(t0)
                addi t0, zero, i1
                flw ft2, 0(t0)
                fle.s s1 ft1 ft2
                addi t0, zero, x
                sw s1, 0(t0)
            ebreak
            """);
        runSimulation();
        Value val = memoryController.getValue(Value.fromHex("00", 4));
        assertEquals("00000001", val.getHexadecimalValue());
    }


}
