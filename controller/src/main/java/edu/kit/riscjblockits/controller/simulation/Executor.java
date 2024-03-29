package edu.kit.riscjblockits.controller.simulation;


import edu.kit.riscjblockits.controller.blocks.AluController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.controller.exceptions.NonExecutableMicroInstructionException;
import edu.kit.riscjblockits.model.busgraph.IBusSystem;
import edu.kit.riscjblockits.model.blocks.ClockMode;
import edu.kit.riscjblockits.model.instructionset.AluInstruction;
import edu.kit.riscjblockits.model.instructionset.ConditionedInstruction;
import edu.kit.riscjblockits.model.instructionset.DataMovementInstruction;
import edu.kit.riscjblockits.model.instructionset.IExecutor;
import edu.kit.riscjblockits.model.instructionset.InstructionCondition;
import edu.kit.riscjblockits.model.instructionset.MemoryInstruction;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the execution of microinstructions. Gets called by the {@link SimulationSequenceHandler} whenever
 * a microinstruction is supposed to be executed. Distinguishes between the different types of microinstructions and
 * holds the BlockControllers of the associated computer blocks as well as a map of the register controllers to perform
 * the operations on the computer blocks.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class Executor implements IExecutor {

    /**
     * String representation of the memory visualisation placeholder.
     */
    private static final String MEM_VISUALISATION = "<mem_vis>";

    /**
     * Pattern to match memory access strings.
     */
    private static final Pattern MEMORY_ACCES_PATTERN = Pattern.compile("Mem\\[(?<register>\\w+)]");

    /**
     * Pattern to match binary strings.
     */
    private static final Pattern BINARY_PATTERN = Pattern.compile("[01]+");

    /**
     * Contains the block controllers of the associated computer blocks.
     */
    private final List<IQueryableSimController> blockControllers;

    /**
     * Map of the register controllers for faster access and resolving string references.
     */
    private final Map<String, RegisterController> registerControllerMap;

    /**
     * The word length of the computer.
     */
    private final int wordLength;

    /**
     * The bus system of the computer.
     */
    private final IBusSystem busSystem;

    /**
     * The control unit controller of the computer.
     */
    private ComputerBlockController controlUnitController;

    /**
     * Constructor. Initializes the BlockControllers list and the {@link RegisterController}s map.
     * @param blockControllers Controllers of the associated computer blocks.
     * @param wordLength Word length of the computer.
     * @param busSystem BusSystemModel of the computer.
     */
    public Executor(List<IQueryableSimController> blockControllers, int wordLength, IBusSystem busSystem) {
        this.blockControllers = blockControllers;
        registerControllerMap = new HashMap<>();
        this.wordLength = wordLength;
        this.busSystem = busSystem;

        for (IQueryableSimController blockController : blockControllers) {
            if (blockController.getControllerType() == BlockControllerType.REGISTER) {
                RegisterController registerController = (RegisterController) blockController;
                registerControllerMap.put(registerController.getRegisterType(), registerController);
            }
            if (blockController.getControllerType() == BlockControllerType.CONTROL_UNIT) {
                this.controlUnitController = (ComputerBlockController) blockController;
            }
        }
    }

    /**
     * Executes a memory instruction.
     * @param memoryInstruction Memory instruction to be executed.
     * @throws NonExecutableMicroInstructionException if the instruction is not executable.
     */
    public void execute(MemoryInstruction memoryInstruction) {
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

                if (!to.equals(MEM_VISUALISATION)) {

                    if(flag.equals("r")) {

                        Matcher matcher = MEMORY_ACCES_PATTERN.matcher(from);
                        if (!matcher.matches()) {
                            throw new NonExecutableMicroInstructionException("MemoryInstruction has no valid from value, does not match pattern");
                        }
                        if (!registerControllerMap.containsKey(matcher.group("register"))) {
                            throw new NonExecutableMicroInstructionException("MemoryInstruction has no valid from value, register not found");
                        }

                        RegisterController fromController = registerControllerMap.get(matcher.group("register"));

                        Value fromAddress = fromController.getValue();

                        Value value = ((MemoryController) blockController).getValue(fromAddress);
                        registerControllerMap.get(to).setNewValue(value);

                    } else if(flag.equals("w")) {
                        Value value = registerControllerMap.get(from).getValue();

                        Matcher matcher = MEMORY_ACCES_PATTERN.matcher(to);
                        if (!matcher.matches()) {
                            throw new NonExecutableMicroInstructionException("MemoryInstruction has no valid to value, does not match pattern");
                        }
                        if (!registerControllerMap.containsKey(matcher.group("register"))) {
                            throw new NonExecutableMicroInstructionException("MemoryInstruction has no valid to value, register not found");
                        }

                        RegisterController toController = registerControllerMap.get(matcher.group("register"));

                        Value toAddress = toController.getValue();

                        ((MemoryController) blockController).writeMemory(toAddress, value);

                    }
                    //else do nothing, because memory flag is not set properly
                }
                //visualisation goes here
                blockController.activateVisualisation();
            }
        }
    }

    /**
     * Executes a conditioned instruction.
     * @param conditionedInstruction Conditioned instruction to be executed.
     */
    public void execute(ConditionedInstruction conditionedInstruction) {

        String from = conditionedInstruction.getFrom()[0];
        String to = conditionedInstruction.getTo();

        InstructionCondition condition = conditionedInstruction.getCondition();

        if(checkCondition(condition)) {

            moveData(from, to);

            if(conditionedInstruction.getMemoryInstruction() != null) {
                execute(conditionedInstruction.getMemoryInstruction());
            }
        }

    }

    /**
     * Executes an alu instruction.
     * @param aluInstruction Alu instruction to be executed.
     * @throws NonExecutableMicroInstructionException if the instruction is not executable.
     */
    public void execute(AluInstruction aluInstruction) {
        //check if the instruction is a pause instruction
        if(aluInstruction.getAction().equals("PAUSE")) {
            blockControllers.stream().filter(controller -> controller.getControllerType() == BlockControllerType.CLOCK)
                    .forEach(controller -> ((SystemClockController) controller).setSimulationMode(ClockMode.STEP));

            return;
        }

        for (IQueryableSimController blockController : blockControllers) {
            if (blockController.getControllerType() == BlockControllerType.ALU) {

                String from1 = aluInstruction.getFrom()[0];
                String from2 = aluInstruction.getFrom()[1];
                String to = aluInstruction.getTo();



                if(from1 == null || from1.isBlank()){
                    throw new NonExecutableMicroInstructionException("AluInstruction has no from value");
                } else if(from2 == null){
                    throw new NonExecutableMicroInstructionException("AluInstruction has no from value");
                } else if(to == null || to.isBlank()){
                    throw new NonExecutableMicroInstructionException("AluInstruction has no to value");
                }

                AluController aluController = (AluController) blockController;
                aluController.activateVisualisation();
                aluController.setOperand1(registerControllerMap.get(from1).getValue());
                // only allow for 2nd operand to be empty if the instruction is a unary operation
                if (!from2.isBlank())
                    aluController.setOperand2(registerControllerMap.get(from2).getValue());
                else
                    aluController.setOperand2(Value.fromBinary("0", wordLength,true));

                Value result = ((AluController) blockController).executeAluOperation(aluInstruction.getAction());

                registerControllerMap.get(to).setNewValue(result);
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
        String from = dataMovementInstruction.getFrom()[0];
        String to = dataMovementInstruction.getTo();

        moveData(from, to);

        if (dataMovementInstruction.getMemoryInstruction() != null) {
            execute(dataMovementInstruction.getMemoryInstruction());
        }
    }

    /**
     * Checks the condition of a conditioned instruction.
     * @param condition Condition to be checked.
     * @return True, if the condition is met, false otherwise.
     * @throws NonExecutableMicroInstructionException if the condition is not executable.
     */
    private boolean checkCondition(InstructionCondition condition) {

        String comparisonCondition = condition.getComparator();
        Value firstValue;
        // the first Comparator is a register --> load value from there
        if (registerControllerMap.containsKey(condition.getCompare1())) {
            firstValue = registerControllerMap.get(condition.getCompare1()).getValue();
        } else { // Comparator is not a register --> extract value from binary constant
            if (!BINARY_PATTERN.matcher(condition.getCompare1()).matches()) {
                throw new NonExecutableMicroInstructionException(
                        "DataMovementInstruction has no valid from value, does not match pattern");
            }
            firstValue = Value.fromBinary(condition.getCompare1(), wordLength, true);
        }

        Value secondValue;
        // the second Comparator is a register --> load value from there
        if (registerControllerMap.containsKey(condition.getCompare2())) {
            secondValue = registerControllerMap.get(condition.getCompare2()).getValue();
        } else { // Comparator is not a register --> extract value from binary constant
            if (!BINARY_PATTERN.matcher(condition.getCompare2()).matches()) {
                throw new NonExecutableMicroInstructionException(
                        "DataMovementInstruction has no valid from value, does not match pattern");
            }
            secondValue = Value.fromBinary(condition.getCompare2(), wordLength, true);
        }

        String comparatorType = comparisonCondition.substring(0, 1);

        switch (comparatorType) {
            case "u" -> {
                String comparator = comparisonCondition.substring(1);
                return switch (comparator) {
                    case "==" -> firstValue.equals(secondValue);
                    case "!=" -> !firstValue.equals(secondValue);
                    case "<=" -> firstValue.lowerThanUnsigned(secondValue) || firstValue.equals(secondValue);
                    case "<" -> firstValue.lowerThanUnsigned(secondValue);

                    case ">=" -> firstValue.greaterThanUnsigned(secondValue) || firstValue.equals(secondValue);
                    case ">" -> firstValue.greaterThanUnsigned(secondValue);
                    default -> false;
                };
            }
            case "f" -> {
                String comparator = comparisonCondition.substring(1);
                return switch (comparator) {
                    case "==" -> firstValue.equals(secondValue);
                    case "!=" -> !firstValue.equals(secondValue);
                    case "<=" -> firstValue.lowerThanFloat(secondValue) || firstValue.equals(secondValue);
                    case "<" -> firstValue.lowerThanFloat(secondValue);

                    case ">=" -> firstValue.greaterThanFloat(secondValue) || firstValue.equals(secondValue);
                    case ">" -> firstValue.greaterThanFloat(secondValue);
                    default -> false;
                };
            }
            //signed comparison is default
            default -> {
                // if the comparator is marked as signed, remove the s from the beginning
                String comparator = comparisonCondition.startsWith("s")? comparisonCondition.substring(1) : comparisonCondition;
                return switch (comparator) {
                    case "==" -> firstValue.equals(secondValue);
                    case "!=" -> !firstValue.equals(secondValue);
                    case "<=" -> firstValue.lowerThan(secondValue) || firstValue.equals(secondValue);
                    case "<" -> firstValue.lowerThan(secondValue);

                    case ">=" -> firstValue.greaterThan(secondValue) || firstValue.equals(secondValue);
                    case ">" -> firstValue.greaterThan(secondValue);
                    default -> false;
                };
            }
        }
    }

    /**
     * Moves data from one register to another.
     * @param from from where the data should be moved
     * @param to to where the data should be moved
     * @throws NonExecutableMicroInstructionException if the instruction is not executable.
     */
    private void moveData(String from, String to) {

        boolean visualizeableData = false;

        // only execute if from and to are not empty
        if(from != null && !from.isBlank() && to != null && !to.isBlank()) {
            Value movedValue;
            // from is a register --> load value from there
            if (registerControllerMap.containsKey(from)) {
                movedValue = registerControllerMap.get(from).getValue();
                visualizeableData = true;
            } else { // from is not a register --> extract value from binary constant
                if (!BINARY_PATTERN.matcher(from).matches()) {
                    throw new NonExecutableMicroInstructionException(
                            "Cannot move Data, MicroInstruction has no valid from value, does not match pattern");
                }
                movedValue = Value.fromBinary(from, wordLength, true);
            }

            // check to and set after
            if (!registerControllerMap.containsKey(to)) {
                throw new NonExecutableMicroInstructionException(
                        "Cannot move Data, MicroInstruction has no valid to value, does not match a Register");
            }

            registerControllerMap.get(to).setNewValue(movedValue);

            // visualization of data between components
            if(visualizeableData) {
                busSystem.setBusDataPath(registerControllerMap.get(from).getBlockPosition(), registerControllerMap.get(to).getBlockPosition(), movedValue);
                registerControllerMap.get(from).activateVisualisation();
                registerControllerMap.get(to).activateVisualisation();
            } else { // visualization of loading of immediate
                busSystem.setBusDataPath(controlUnitController.getBlockPosition(), registerControllerMap.get(to).getBlockPosition(), movedValue);
                controlUnitController.activateVisualisation();
                registerControllerMap.get(to).activateVisualisation();
            }
        }
    }

}
