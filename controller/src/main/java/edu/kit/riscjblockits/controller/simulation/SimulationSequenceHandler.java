package edu.kit.riscjblockits.controller.simulation;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.controller.exceptions.NonExecutableMicroInstructionException;
import edu.kit.riscjblockits.model.busgraph.IBusSystem;
import edu.kit.riscjblockits.model.instructionset.DataMovementInstruction;
import edu.kit.riscjblockits.model.instructionset.IExecutableMicroInstruction;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstruction;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import java.util.List;
import java.util.Objects;

/**
 * Handles the fetching and execution of the instructions. Gets called by the {@link SimulationTimeHandler} whenever
 * the next simulation tick is supposed to be executed.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class SimulationSequenceHandler implements Runnable {

    /**
     * Counts the number of executed instructions in the current phase.
     */
    private int phaseCounter;

    /**
     * Defines the current phase of the instruction execution, including fetching the instruction and
     * executing its microinstructions.
     */
    private RunPhase runPhase;
    /**
     * Contains the microinstructions of the current instruction.
     */
    private IExecutableMicroInstruction[] microInstructions;
    /**
     * Contains the block controllers of the associated computer blocks.
     */
    private final List<IQueryableSimController> blockControllers;
    /**
     * Instruction set model that holds all information on how to execute code based on the instruction set.
     */
    private IQueryableInstructionSetModel instructionSetModel;
    /**
     * Controller of the program counter register.
     */
    private RegisterController programCounterController;
    /**
     * Controller of the memory.
     */
    private MemoryController memoryController;
    /**
     * Executor for the microinstructions.
     */
    private final Executor executor;

    /**
     * The bus system of the computer.
     */
    private final IBusSystem busSystem;

    /**
     * The callback receivable for the simulation sequence handler to notify after every real-time simulation tick.
     */
    private final IRealtimeSimulationCallbackReceivable callbackReceivable;

    /**
     * The current visualisation mode of the simulation sequence handler.
     */
    private VisualisationMode visualisationMode = VisualisationMode.NORMAL;

    /**
     * Constructor. Initializes the {@link BlockController}s list, hands it over to the executor, sets the first
     * run phase to FETCH and the counter to zero and initializes the instruction set model and the memory controller.
     * @param blockControllers Controllers of the associated computer blocks.
     */
    public SimulationSequenceHandler(List<IQueryableSimController> blockControllers, IBusSystem busSystem, IRealtimeSimulationCallbackReceivable callbackReceivable) {
        this.blockControllers = blockControllers;
        this.busSystem = busSystem;
        this.callbackReceivable = callbackReceivable;

        phaseCounter = 0;
        runPhase = RunPhase.FETCH;
        for(IQueryableSimController blockController: blockControllers) {
            if ((blockController.getControllerType()) == BlockControllerType.CONTROL_UNIT) {
                instructionSetModel = ((ControlUnitController) blockController).getInstructionSetModel();
            }
            if ((blockController.getControllerType()) == BlockControllerType.MEMORY) {
                memoryController = ((MemoryController)blockController);
            }
        }

        //ToDo: Catch exception somewhere
        if (instructionSetModel == null) {
            throw new NullPointerException("No instruction set model found");
        }

        String programCounterTag = instructionSetModel.getProgramCounter();
        for(IQueryableSimController blockController: blockControllers) {
            if (Objects.requireNonNull(blockController.getControllerType()) == BlockControllerType.REGISTER) {
                if (((RegisterController) blockController).getRegisterType().equals(programCounterTag)) {
                    programCounterController = (RegisterController) blockController;
                    // set the initial program counter value --> if not set, set to zero
                    Value pcValue = memoryController.getInitialProgramCounter();
                    if (pcValue == null) {
                        pcValue = new Value(new byte[instructionSetModel.getMemoryAddressSize()]);
                    }
                    programCounterController.setNewValue(pcValue);
                }
            }
        }
        this.executor = new Executor(blockControllers, instructionSetModel.getMemoryWordSize(), busSystem);

    }

    /**
     * Runs the execution of the current instruction. Gets called by the {@link SimulationTimeHandler} whenever the next
     * simulation tick is executed. During the first call of execution of a new instruction fills the MicroInstructions
     * array with the necessary microinstructions.
     * Then runs the next microinstruction of the current instruction.
     */
    @Override
    public void run() {
        //get memory address from IAR
        //Value pcValue = programCounterController.getValue();
        //get content from memory at address

        //get MicroInstructions

        //------------
        resetVisualisation();
        switch (runPhase) {
            case FETCH -> fetch();
            case EXECUTE -> execute();
        }

        callbackReceivable.onSimulationTickComplete();
    }

    /**
     * Executes one step of the fetch phase of the instruction execution. The fetch phase needs multiple steps
     * represented as microinstructions which are defined in the instruction set.
     * If the last step of the fetch phase is executed, the execution phase is entered.
     */
    private void fetch(){
        // load the execution's microinstructions internally before fetch, so the memory address is still the same
        if (phaseCounter == 0) {
            IQueryableInstruction instruction = instructionSetModel.getInstructionFromBinary(memoryController.getValue(programCounterController.getValue()).getBinaryValue());
            if (instruction == null) {
                microInstructions = new IExecutableMicroInstruction[] {new DataMovementInstruction(new String[1], "", "", null)};
            } else {
                microInstructions = instruction.getExecution();
                System.out.println("loading from: " + programCounterController.getValue().getHexadecimalValue());
            }
        }

        // execute the current fetch phase step
        executeMicroInstruction(instructionSetModel.getFetchPhaseStep(phaseCounter));

        phaseCounter++;
        // if no more fetch phase steps are defined, the execution phase is entered
        if (phaseCounter >= instructionSetModel.getFetchPhaseLength()) {
            phaseCounter = 0;
            runPhase = RunPhase.EXECUTE;
        }

    }

    /**
     * Gets the current microinstruction from the loaded instruction and executes it.
     * For every microinstruction, a cue from the simulation time handler is necessary. Each microinstruction
     * therefore is executed individually.
     */
    private void execute(){
        //One full instruction consists of multiple microinstructions
        executeMicroInstruction(microInstructions[phaseCounter]);
        phaseCounter++;
        if (phaseCounter >= microInstructions.length) {
            System.out.println("execution phase finished");
            phaseCounter = 0;
            runPhase = RunPhase.FETCH;
        }
    }

    /**
     * Resets the visualization of the computer blocks and the bus system.
     */
    public void resetVisualisation() {

        if(visualisationMode == VisualisationMode.FAST) {
            return;
        }

        for (IQueryableSimController blockController : blockControllers) {
            blockController.stopVisualisation();
        }
        busSystem.resetVisualisation();
    }

    /**
     * Activates the visualization of all computer blocks and the bus system.
     */
    public void fullVisualisation() {
        for (IQueryableSimController blockController : blockControllers) {
            blockController.activateVisualisation();
        }
        busSystem.activateVisualisation();
    }

    /**
     * Sets the visualisation mode.
     * @param mode The visualisation mode to set.
     */
    public void setVisualizationMode(VisualisationMode mode) {
        this.visualisationMode = mode;
        if(visualisationMode == VisualisationMode.OFF || visualisationMode == VisualisationMode.NORMAL) {
            resetVisualisation();
        }
        else if(visualisationMode == VisualisationMode.FAST) {
            fullVisualisation();
        }
    }

    /**
     * Execute microinstruction using a visitor pattern.
     * @param instruction Microinstruction to execute.
     */
    private void executeMicroInstruction(IExecutableMicroInstruction instruction) {
        //ToDo consider exception handling (stop execution or just keep running the next like it is done now)
        try {
            instruction.execute(executor);
        } catch (NonExecutableMicroInstructionException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Defines the two phases of the instruction execution.
     */
    private enum RunPhase{
        FETCH,
        EXECUTE
    }

    /**
     * Defines the visualisation modes.
     */
    public enum VisualisationMode {
        FAST,
        NORMAL,
        OFF
    }

}
