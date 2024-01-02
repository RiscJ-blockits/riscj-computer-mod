package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.ClockMode;
import edu.kit.riscjblockits.model.IObserver;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationTimeHandler implements IObserver {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private SimulationSequenceHandler simulationSequenceHandler;
    private int clockSpeed;
    private ClockMode clockMode;
    private SystemClockModel systemClockModel;
    private int minecraftTickCounter = 0;

    public SimulationTimeHandler(List<BlockController> blockControllers) {
        simulationSequenceHandler = new SimulationSequenceHandler(blockControllers);
        //Register us as an SystemClockModel Observer
        for(BlockController blockController: blockControllers) {
            if (blockController.getControllerType() == BlockControllerType.CLOCK) {
                systemClockModel = (SystemClockModel) blockController.getModel();
                systemClockModel.registerObserver(this);
            }
        }
        updateObservedState();
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

    /**
     * Always get Clock State using an observer pattern.
     */
    @Override
    public void updateObservedState() {
        clockSpeed = systemClockModel.getClockSpeed();
        clockMode = systemClockModel.getClockMode();
    }
}
