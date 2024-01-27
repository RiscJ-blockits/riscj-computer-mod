package edu.kit.riscjblockits.controller.simulation;


import edu.kit.riscjblockits.controller.blocks.AluController;
import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.controller.exceptions.NonExecutableMicroInstructionException;
import edu.kit.riscjblockits.model.instructionset.*;
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

    /* ToDo: model access happens-before relationship by either volatile or synchronized to secure memory consistency
        in the case of multiple threads accessing the same model due to slow execution*/

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
    public void execute(MemoryInstruction memoryInstruction) {
        //ToDo

        if(memoryInstruction.getFlag().isEmpty()) {
            return;
        }

        for (IQueryableSimController blockController : blockControllers) {
            if (blockController.getControllerType() == BlockControllerType.MEMORY) {

                String from = memoryInstruction.getFrom()[0];
                String to = memoryInstruction.getTo();
                String flag = memoryInstruction.getFlag();

                if(from == null || from.isBlank()){
                    throw new NonExecutableMicroInstructionException("MemoryInstruction has no from value");
                } else if(to == null || to.isBlank()){
                    throw new NonExecutableMicroInstructionException("MemoryInstruction has no to value");
                }


                if(flag.equals("r")) {

                    Value fromAddress = Value.fromBinary(from, from.length());
                    Value value = ((MemoryController) blockController).getValue(fromAddress);
                    registerControllerMap.get(to).setNewValue(value);

                }
                else if(flag.equals("w")) {
                    Value value = registerControllerMap.get(from).getValue();
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

        String from = conditionedInstruction.getFrom()[0];
        String to = conditionedInstruction.getTo();

        if(from == null || from.isBlank()){
            throw new NonExecutableMicroInstructionException("MemoryInstruction has no from value");
        } else if(to == null || to.isBlank()){
            throw new NonExecutableMicroInstructionException("MemoryInstruction has no to value");
        }

        InstructionCondition condition = conditionedInstruction.getCondition();

        if(checkCondition(condition)) {
            Value movedValue = registerControllerMap.get(from).getValue();
            registerControllerMap.get(to).setNewValue(movedValue);

            if(conditionedInstruction.getMemoryInstruction() != null) {
                execute(conditionedInstruction.getMemoryInstruction());
            }
        }


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
        String from = dataMovementInstruction.getFrom()[0];
        String to = dataMovementInstruction.getTo();

        if(from == null || from.isBlank()){
            throw new NonExecutableMicroInstructionException("MemoryInstruction has no from value");
        } else if(to == null || to.isBlank()){
            throw new NonExecutableMicroInstructionException("MemoryInstruction has no to value");
        }

        Value movedValue = registerControllerMap.get(from).getValue();
        registerControllerMap.get(to).setNewValue(movedValue);

        //ToDo Bus-Daten setzen wie und wo?

        if (dataMovementInstruction.getMemoryInstruction() != null) {
            execute(dataMovementInstruction.getMemoryInstruction());
        }
    }

    private boolean checkCondition(InstructionCondition condition) {

        String comparisonCondition = condition.getComparator();
        Value comparator1 = registerControllerMap.get(condition.getCompare1()).getValue();
        Value comparator2 = registerControllerMap.get(condition.getCompare2()).getValue();

        return switch (comparisonCondition) {
            case "==" -> comparator1.equals(comparator2);
            case "!=" -> !comparator1.equals(comparator2);
            case "<=" -> comparator1.lowerThan(comparator2) || comparator1.equals(comparator2);
            case "<" -> comparator1.lowerThan(comparator2);

            case ">=" -> comparator1.greaterThan(comparator2) || comparator1.equals(comparator2);
            case ">" -> comparator1.greaterThan(comparator2);
            default -> false;
        };
    }

}
