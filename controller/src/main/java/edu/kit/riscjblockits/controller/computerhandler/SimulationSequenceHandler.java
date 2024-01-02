package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.*;
import edu.kit.riscjblockits.model.instructionset.*;

import java.util.List;
import java.util.Objects;

public class SimulationSequenceHandler implements Runnable {

    private int phaseCounter;
    private RunPhase runPhase;
    private MicroInstruction[] microInstructions;
    private List<ComputerBlockController> blockControllers;
    private InstructionSetModel instructionSetModel;
    private RegisterController programCounterController;
    private RegisterController irController;
    private MemoryController memoryController;
    private Executor executor;

    public SimulationSequenceHandler(List<ComputerBlockController> blockControllers) {
        this.blockControllers = blockControllers;
        this.executor = new Executor(blockControllers);
        phaseCounter = 0;
        runPhase = RunPhase.FETCH;
        for(BlockController blockController: blockControllers) {
            if ((blockController.getControllerType()) == BlockControllerType.CONTROLL_UNIT) {
                instructionSetModel = ((ControlUnitController) blockController).getInstructionSetModel();
            }
            if ((blockController.getControllerType()) == BlockControllerType.MEMORY) {
                memoryController = ((MemoryController)blockController);
            }
        }

        //ToDo: Null-Exception werfen und an entsprechender Stelle abfangen
        String programCounterTag = instructionSetModel.getProgramCounter();
        for(BlockController blockController: blockControllers) {
            if (Objects.requireNonNull(blockController.getControllerType()) == BlockControllerType.REGISTER) {
                if (((RegisterController) blockController).getRegisterType().equals(programCounterTag)) {
                    programCounterController = (RegisterController) blockController;
                }
            }
        }

    }

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
     * Needs multiple steps defined as MicroInstructions as it is different in every instruction set
     * multiple phases: multiple MicoInstructions defined in InstructionSet
     */
    private void fetch(){
        //get fetch Instruction from InstructionSet
        MicroInstruction[] fetchInstructions = null; // = instructionSetModel.     ;           //ToDo get fetch Instruction
        executeMicroInstruction(fetchInstructions[phaseCounter]);
        phaseCounter++;
        if (phaseCounter > fetchInstructions.length) {
            phaseCounter = 0;
            runPhase = RunPhase.EXECUTE;
        }
        //ToDo: put next Instructions in microInstructions Array

    }


    /**
     * Gets its steps from the loaded instruction and executes them.
     * multiple phases: One MicroInstruction = one phase
     */
    private void execute(){
        //Instructions for this phase are now microInstructions
        executeMicroInstruction(microInstructions[phaseCounter]);
        phaseCounter++;
        if (phaseCounter > microInstructions.length) {
            phaseCounter = 0;
            runPhase = RunPhase.FETCH;
        }
    }

    /**
     * Execute Instruction using a visitor pattern.
     * @param instruction
     */
    private void executeMicroInstruction(MicroInstruction instruction) {
        //ToDo

        instruction.execute(executor);




    }





    private enum RunPhase{
        FETCH,
        EXECUTE
    }
}
