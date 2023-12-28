package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationTimeHandler {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private SimulationSequenceHandler simulationSequenceHandler;
    private List<BlockController> blocks;
    public SimulationTimeHandler(List<BlockController> blockControllers) {
        blocks = blockControllers;
    }

    /**
     * Called by the {@link edu.kit.riscjblockits.controller.blocks.SystemClockController} in tick mode to execute the next simulation tick.
     * Runs if we are in McTick mode.
     */
    public void onMinecraftTick(){
        if(clockMode == ClockMode.MC_TICK) {
            if (minecraftTickCounter == 0)
                runTick();
            minecraftTickCounter = (minecraftTickCounter + 1) % clockSpeed;
        }
    }

    /**
     * Called by the {@link edu.kit.riscjblockits.controller.blocks.SystemClockController} on User input in step mode to execute the next simulation tick.
     * Runs if we are in Step mode.
     */
    public void onUserTickTrigger(){
        if (clockMode == ClockMode.STEP) {
            runTick();
        }
    }

    /**
     * Called by the {@link SimulationSequenceHandler} when a SimulationFrame has been complete.
     * Runs if we are in Realtime mode.
     */
    public void onSimulationTickComplete(){
        if (clockMode == ClockMode.REALTIME) {
            runTick();
        }
    }

    private void runTick() {
        //ToDo @Leon no check if the previous tick has completed. Necessary?
        executorService.execute(simulationSequenceHandler);
    }
}
