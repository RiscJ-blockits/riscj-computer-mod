package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.instructionset.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the execution of microinstructions. Gets called by the {@link SimulationSequenceHandler} whenever
 * a microinstrution is supposed to be executed. Distinguishes between the different types of microinstructions and
 * holds the BlockControllers of the associated computer blocks as well as a map of the register controllers to perform
 * the operations on the computer blocks.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class Executor implements IExecutor {

    /**
     * Contains the block controllers of the associated computer blocks.
     */
    private List<BlockController> blockControllers;
    /**
     * Map of the register controllers for faster access and resolving string references.
     */
    private Map<String, RegisterController> registerControllerMap;

    /**
     * Constructor. Initializes the {@link BlockController}s list and the {@link RegisterController}s map.
     * @param blockControllers Controllers of the associated computer blocks.
     */
    public Executor(List<BlockController> blockControllers) {
        this.blockControllers = blockControllers;
        registerControllerMap = new HashMap<>();

        for (BlockController blockController : blockControllers) {
            if (blockController.getControllerType() == BlockControllerType.REGISTER) {
                RegisterController registerController = (RegisterController) blockController;
                registerControllerMap.put(registerController.getRegisterType(), registerController);
            }
        }
        
    }

    /**
     * Executes a memory instruction.
     * @param memoryInstruction Memory instruction to be executed.
     */
    public void execute(MemoryInstruction memoryInstruction){
        //ToDo
    }

    /**
     * Executes a conditioned instruction.
     * @param conditionedInstruction Conditioned instruction to be executed.
     */
    public void execute(ConditionedInstruction conditionedInstruction) {

    }

    /**
     * Executes an alu instruction.
     * @param aluInstruction Alu instruction to be executed.
     */
    public void execute(AluInstruction aluInstruction) {

    }

    /**
     * Executes a data movement instruction.
     * @param dataMovementInstruction Data movement instruction to be executed.
     */
    public void execute(DataMovementInstruction dataMovementInstruction) {

    }
}
