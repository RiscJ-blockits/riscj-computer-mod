package edu.kit.riscjblockits.controller.simulation;

import edu.kit.riscjblockits.controller.blocks.*;
import edu.kit.riscjblockits.model.blocks.*;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.*;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
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

    private RegisterController registerController4;
    private RegisterController registerController5;
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
        ((MemoryModel) (memoryController.getModel())).setMemory(new Memory(4, 12));
        memoryController.writeMemory(Value.fromHex("00", 4), Value.fromHex("0456", 12));
        AluController aluController = new AluController(getBlockEntityMock());
        ((AluModel) aluController.getModel()).setOperand1(Value.fromHex("0100", 12));
        ((AluModel) aluController.getModel()).setOperand2(Value.fromHex("0023", 12));
        RegisterController registerController1 = new RegisterController(getBlockEntityMock());
        ((RegisterModel) registerController1.getModel()).setRegisterType("R1");
        registerController1.setNewValue(Value.fromHex("0100", 12));
        RegisterController registerController2 = new RegisterController(getBlockEntityMock());
        ((RegisterModel) registerController2.getModel()).setRegisterType("R2");
        registerController2.setNewValue(Value.fromHex("0023", 12));
        RegisterController registerController3 = new RegisterController(getBlockEntityMock());
        ((RegisterModel) registerController3.getModel()).setRegisterType("R3");
        registerController3.setNewValue(Value.fromHex("0345", 12));
        RegisterController registerController4 = new RegisterController(getBlockEntityMock());
        ((RegisterModel) registerController4.getModel()).setRegisterType("R4");
        registerController4.setNewValue(Value.fromHex("1111", 12));
        RegisterController registerController5 = new RegisterController(getBlockEntityMock());
        ((RegisterModel) registerController5.getModel()).setRegisterType("R5");
        registerController5.setNewValue(Value.fromHex("1101", 12));

        List<IQueryableSimController> blockControllers = new LinkedList<>();
        blockControllers.add(memoryController);
        blockControllers.add(aluController);
        blockControllers.add(registerController1);
        blockControllers.add(registerController2);
        blockControllers.add(registerController3);
        blockControllers.add(registerController4);
        blockControllers.add(registerController5);

        this.executor = new Executor(blockControllers);
        this.memoryController = memoryController;
        this.aluController = aluController;
        this.registerController1 = registerController1;
        this.registerController2 = registerController2;
        this.registerController3 = registerController3;
        this.registerController4 = registerController4;
        this.registerController5 = registerController5;

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void executeAluInstructionBaseCase() {

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

        AluInstruction aluInstruction = new AluInstruction(new String[]{"R1", "R2"}, "R2", "r", null, "ADD");

        executor.execute(aluInstruction);

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0123", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

    }

    @Test
    void executeAluInstructionWithMemoryInstruction() {

        assertEquals(Value.fromHex("0456", 12), memoryController.getValue(Value.fromHex("00", 4)));
        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

        MemoryInstruction memoryInstruction = new MemoryInstruction(new String[]{"0000000000000000"}, "R3", "r");
        AluInstruction aluInstruction = new AluInstruction(new String[]{"R1", "R2"}, "R2", "r", memoryInstruction, "ADD");

        executor.execute(aluInstruction);

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0123", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0456", 12), registerController3.getValue());


    }

    @Test
    void executeDataMovementInstructionBaseCase() {

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

        DataMovementInstruction dataMovementInstruction = new DataMovementInstruction(new String[]{"R1"}, "R2", "r", null);

        executor.execute(dataMovementInstruction);

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0100", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

    }

    @Test
    void executeConditionedInstructionBaseCaseSignedConditionTrue() {

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());
        assertEquals(Value.fromHex("1111", 12), registerController4.getValue());
        assertEquals(Value.fromHex("1101", 12), registerController5.getValue());

        InstructionCondition condition = new InstructionCondition("s<", "R5", "R4");
        ConditionedInstruction conditionedInstruction = new ConditionedInstruction(new String[]{"R1"}, "R2", "", null, condition);

        executor.execute(conditionedInstruction);

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0100", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

    }

    @Test
    void executeConditionedInstructionBaseCaseSignedConditionFalse() {

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());
        assertEquals(Value.fromHex("1111", 12), registerController4.getValue());
        assertEquals(Value.fromHex("1101", 12), registerController5.getValue());

        InstructionCondition condition = new InstructionCondition("s<", "R4", "R5");
        ConditionedInstruction conditionedInstruction = new ConditionedInstruction(new String[]{"R1"}, "R2", "", null, condition);

        executor.execute(conditionedInstruction);

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

    }

    @Test
    void executeConditionedInstructionBaseCaseUnsignedConditionTrue() {

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

        InstructionCondition condition = new InstructionCondition("u<", "R1", "R3");
        ConditionedInstruction conditionedInstruction = new ConditionedInstruction(new String[]{"R1"}, "R2", "", null, condition);

        executor.execute(conditionedInstruction);

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0100", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

    }

    @Test
    void executeConditionedInstructionBaseCaseUnsignedConditionFalse() {

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

        InstructionCondition condition = new InstructionCondition("u<", "R3", "R1");
        ConditionedInstruction conditionedInstruction = new ConditionedInstruction(new String[]{"R1"}, "R2", "", null, condition);

        executor.execute(conditionedInstruction);

        assertEquals(Value.fromHex("0100", 12), registerController1.getValue());
        assertEquals(Value.fromHex("0023", 12), registerController2.getValue());
        assertEquals(Value.fromHex("0345", 12), registerController3.getValue());

    }

    @Test
    void executeMemoryInstructionBaseCase() {
        executeAluInstructionWithMemoryInstruction();
    }
}