package edu.kit.riscjblockits.controller.simulation;


import edu.kit.riscjblockits.controller.blocks.AluController;
import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.instructionset.AluInstruction;
import edu.kit.riscjblockits.model.instructionset.ConditionedInstruction;
import edu.kit.riscjblockits.model.instructionset.DataMovementInstruction;
import edu.kit.riscjblockits.model.instructionset.IExecutor;
import edu.kit.riscjblockits.model.instructionset.MemoryInstruction;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

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
    private List<IQueryableSimController> blockControllers;

    /**
     * Map of the register controllers for faster access and resolving string references.
     */
    private Map<String, RegisterController> registerControllerMap;

    /**
     * Constructor. Initializes the {@link BlockController}s list and the {@link RegisterController}s map.
     * @param blockControllers Controllers of the associated computer blocks.
     */
    public Executor(List<IQueryableSimController> blockControllers) {
        this.blockControllers = blockControllers;
        registerControllerMap = new HashMap<>();

        for (IQueryableSimController blockController : blockControllers) {
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

        //ToDo: check memory flag handling
        if(memoryInstruction.getFlag().isEmpty()) {
            return;
        }

        for (IQueryableSimController blockController : blockControllers) {
            if (blockController.getControllerType() == BlockControllerType.MEMORY) {

                if(memoryInstruction.getFlag().equals("r")) {
                    //ToDo: check format with assembler
                    String from = memoryInstruction.getFrom()[0];
                    Value fromAddress = Value.fromBinary(from, from.length());
                    Value value = ((MemoryController) blockController).getValue(fromAddress);

                    String to = memoryInstruction.getTo();
                    registerControllerMap.get(to).setNewValue(value);
                }
                else if(memoryInstruction.getFlag().equals("w")) {

                    String from = memoryInstruction.getFrom()[0];
                    Value value = registerControllerMap.get(from).getValue();

                    String to = memoryInstruction.getFrom()[0];
                    //ToDo: check if binary or hex or other
                    ((MemoryController) blockController).writeMemory(Value.fromBinary(to, 4), value);

                }
                //else do nothing, because memory flag is not set properly
            }
        }

        //ToDo Bus-Daten setzen wie und wo?
    }

    /**
     * Executes a conditioned instruction.
     * @param conditionedInstruction Conditioned instruction to be executed.
     */
    public void execute(ConditionedInstruction conditionedInstruction) {
        //ToDo
        registerControllerMap.get(null).setNewValue(null);


        //ToDo Bus-Daten setzen wie und wo?

    }

    /**
     * Executes an alu instruction.
     * @param aluInstruction Alu instruction to be executed.
     */
    public void execute(AluInstruction aluInstruction) {
        //ToDo

        for (IQueryableSimController blockController : blockControllers) {
            if (blockController.getControllerType() == BlockControllerType.ALU) {
                ((AluController) blockController).executeAluOperation(aluInstruction.getAction());
            }
        }

        if (aluInstruction.getMemoryInstruction() != null) {
            execute(aluInstruction.getMemoryInstruction());
        }

    }

    /**
     * Executes a data movement instruction.
     * @param dataMovementInstruction Data movement instruction to be executed.
     */
    public void execute(DataMovementInstruction dataMovementInstruction) {
        //ToDo

        Value movedValue = registerControllerMap.get(dataMovementInstruction.getFrom()[0]).getValue();
        registerControllerMap.get(dataMovementInstruction.getTo()).setNewValue(movedValue);

        //ToDo Bus-Daten setzen wie und wo?

        if (dataMovementInstruction.getMemoryInstruction() != null) {
            execute(dataMovementInstruction.getMemoryInstruction());
        }
    }

}
