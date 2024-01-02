package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.instructionset.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Executor implements IExecutor {

    private List<ComputerBlockController> blockControllers;
    private Map<String, RegisterController> registerControllerMap;

    public Executor(List<ComputerBlockController> blockControllers) {
        this.blockControllers = blockControllers;
        registerControllerMap = new HashMap<>();

        for (BlockController blockController : blockControllers) {
            if (blockController.getControllerType() == BlockControllerType.REGISTER) {
                RegisterController registerController = (RegisterController) blockController;
                registerControllerMap.put(registerController.getRegisterType(), registerController);
            }
        }
        
    }

    public void execute(MemoryInstruction memoryInstruction){
        //ToDo
    }

    public void execute(ConditionedInstruction conditionedInstruction) {

    }

    public void execute(AluInstruction aluInstruction) {

    }

    public void execute(DataMovementInstruction dataMovementInstruction) {

    }
}
