package edu.kit.riscjblockits.controller.simulation;


import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.instructionset.IExecutableMicroInstruction;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;

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
    private List<IQueryableSimController> blockControllers;
    /**
     * Instruction set model that holds all information on how to execute code based on the instruction set.
     */
    private IQueryableInstructionSetModel instructionSetModel;            //FixMe: not an Interface, more in this class
    /**
     * Controller of the program counter register.
     */
    private RegisterController programCounterController;
    /**
     * Controller of the instruction register.
     */
    private RegisterController iarController;
    /**
     * Controller of the memory.
     */
    private MemoryController memoryController;
    /**
     * Executor for the microinstructions.
     */
    private Executor executor;

    /**
     * Constructor. Initializes the {@link BlockController}s list, hands it over to the executor, sets the first
     * run phase to FETCH and the counter to zero and initializes the instruction set model and the memory controller.
     * @param blockControllers Controllers of the associated computer blocks.
     */
    public SimulationSequenceHandler(List<IQueryableSimController> blockControllers) {
        this.blockControllers = blockControllers;
        this.executor = new Executor(blockControllers);
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

        //ToDo: Null-Exception werfen und an entsprechender Stelle abfangen
        String programCounterTag = instructionSetModel.getProgramCounter();
        for(IQueryableSimController blockController: blockControllers) {
            if (Objects.requireNonNull(blockController.getControllerType()) == BlockControllerType.REGISTER) {
                if (((RegisterController) blockController).getRegisterType().equals(programCounterTag)) {
                    programCounterController = (RegisterController) blockController;
                }
            }
        }

    }

    /**
     * Runs the execution of the current instruction. Gets called by the {@link SimulationTimeHandler} whenever the next
     * simulation tick is executed. During the first call of execution of a new instruction fills the MicroInstructions
     * array with the necessary microinstructions.
     * Then runs the next microinstruction of the current instruction.
     */
    @Override
    public void run() {
        System.out.println("run test");
        //get memory address from IAR
        //Value pcValue = programCounterController.getValue();
        //get content from memory at address

        //get MicroInstructions

        //------------
       switch (runPhase) {
            case FETCH -> fetch();
            case EXECUTE -> execute();
        }
    }

    /**
     * Executes one step of the fetch phase of the instruction execution. The fetch phase needs multiple steps
     * represented as microinstructions which are defined in the instruction set.
     * If the last step of the fetch phase is executed, the execution phase is entered.
     */
    private void fetch(){
        //ToDo: Rethink if fetch instructions could be a class attribute set in the constructor.
        //ToDo: Check if an instruction initialization method might be useful.
        IExecutableMicroInstruction[] fetchInstructions = null; // = instructionSetModel.     ;           //ToDo get fetch Instruction
        executeMicroInstruction(fetchInstructions[phaseCounter]);
        phaseCounter++;
        if (phaseCounter > fetchInstructions.length) {
            phaseCounter = 0;
            runPhase = RunPhase.EXECUTE;
        }
        //ToDo: put next Instructions in microInstructions Array

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
        if (phaseCounter > microInstructions.length) {
            phaseCounter = 0;
            runPhase = RunPhase.FETCH;
        }
    }

    /**
     * Execute microinstruction using a visitor pattern.
     * @param instruction Microinstruction to execute.
     */
    private void executeMicroInstruction(IExecutableMicroInstruction instruction) {
        //ToDo

        instruction.execute(executor);

    }

    /**
     * Defines the two phases of the instruction execution.
     */
    private enum RunPhase{
        FETCH,
        EXECUTE
    }
}
