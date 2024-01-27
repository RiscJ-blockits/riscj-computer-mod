package edu.kit.riscjblockits.controller.simulation;

import edu.kit.riscjblockits.controller.blocks.*;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.AluInstruction;
import edu.kit.riscjblockits.model.instructionset.MemoryInstruction;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExecutorTest {

    private Executor executor;
    private AluController aluController;
    private RegisterController registerController1;
    private RegisterController registerController2;
    private RegisterController registerController3;
    private MemoryController memoryController;

    //copied from Nils @ AluControllerTest
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

    @BeforeEach
    void setUp() {

        MemoryController memoryController = new MemoryController(getBlockEntityMock());
        memoryController.writeMemory(Value.fromHex("0x0", 4), Value.fromHex("0x456", 12));
        AluController aluController = new AluController(getBlockEntityMock());
        RegisterController registerController1 = new RegisterController(getBlockEntityMock());
        ((RegisterModel) registerController1.getModel()).setRegisterType("R1");
        registerController1.setNewValue(Value.fromHex("0x100", 12));
        RegisterController registerController2 = new RegisterController(getBlockEntityMock());
        ((RegisterModel) registerController2.getModel()).setRegisterType("R2");
        registerController2.setNewValue(Value.fromHex("0x023", 12));
        RegisterController registerController3 = new RegisterController(getBlockEntityMock());
        ((RegisterModel) registerController3.getModel()).setRegisterType("R2");
        registerController3.setNewValue(Value.fromHex("0x345", 12));

        List<IQueryableSimController> blockControllers = new LinkedList<>();
        blockControllers.add(memoryController);
        blockControllers.add(aluController);
        blockControllers.add(registerController1);
        blockControllers.add(registerController2);

        this.executor = new Executor(blockControllers);
        this.memoryController = memoryController;
        this.aluController = aluController;
        this.registerController1 = registerController1;
        this.registerController2 = registerController2;
        this.registerController3 = registerController3;

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void executeAluInstructionBaseCase() {

        assertEquals(Value.fromHex("0x100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0x023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0x345", 12), registerController3.getValue());

        AluInstruction aluInstruction = new AluInstruction(new String[]{"R1", "R2"}, "R2", "r", null, "ADD");

        executor.execute(aluInstruction);

        assertEquals(Value.fromHex("0x123", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0x023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0x345", 12), registerController3.getValue());

    }

    @Test
    void executeAluInstructionWithMemoryInstruction() {

        assertEquals(Value.fromHex("0x456", 12), memoryController.getValue(Value.fromHex("0x0", 4)));
        assertEquals(Value.fromHex("0x100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0x023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0x345", 12), registerController3.getValue());

        MemoryInstruction memoryInstruction = new MemoryInstruction(new String[]{"0x0"}, "R3", "r");
        AluInstruction aluInstruction = new AluInstruction(new String[]{"R1", "R2"}, "R2", "r", memoryInstruction, "ADD");

        executor.execute(aluInstruction);

        assertEquals(Value.fromHex("0x123", 12), registerController1.getValue());
        assertEquals(Value.fromHex("ox456", 12), registerController3.getValue());


    }

    @Test
    void executeDataMovementInstructionBaseCase() {
    }

    @Test
    void executeConditionedInstructionBaseCase() {
    }

    @Test
    void executeMemoryInstructionBaseCase() {
    }
}