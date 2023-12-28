package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;

import java.util.List;
import java.util.Objects;

public class SimulationSequenceHandler implements Runnable {

    public SimulationSequenceHandler(List<BlockController> blockControllers) {

    }

    @Override
    public void run() {
        System.out.println("run test");
//        try {
//            wait(600);
//        } catch (InterruptedException e) {
//            System.out.println("wait failed");
//            throw new RuntimeException(e);
//        }

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
