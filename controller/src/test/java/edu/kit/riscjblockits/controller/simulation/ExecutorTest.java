package edu.kit.riscjblockits.controller.simulation;

import edu.kit.riscjblockits.controller.blocks.*;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class ExecutorTest {

    private Executor executor;

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

        IQueryableSimController memoryController = new MemoryController(getBlockEntityMock());
        IQueryableSimController aluController = new AluController(getBlockEntityMock());
        IQueryableSimController registerController1 = new RegisterController(getBlockEntityMock());
        ((RegisterModel) registerController1.getModel()).setRegisterType("R1");
        IQueryableSimController registerController2 = new RegisterController(getBlockEntityMock());
        ((RegisterModel) registerController2.getModel()).setRegisterType("R2");

        List<IQueryableSimController> blockControllers = new LinkedList<>();
        blockControllers.add(memoryController);
        blockControllers.add(aluController);
        blockControllers.add(registerController1);
        blockControllers.add(registerController2);

        this.executor = new Executor(blockControllers);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void executeAluInstructionBaseCase() {
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