package edu.kit.riscjblockits.controller.simulation;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.blocks.ClockMode;
import edu.kit.riscjblockits.model.blocks.ISimulationTimingObserver;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;
import edu.kit.riscjblockits.model.busgraph.IBusSystem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handling of the simulation execution timing. Uses the observer pattern to keep track of the clock state as
 * represented in the {@link SystemClockModel}. Depending on the state, the next simulation tick is executed or
 *  the necessary wait time for the next execution is decreased by one step.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class SimulationTimeHandler implements ISimulationTimingObserver, IRealtimeSimulationCallbackReceivable {

    /**
     * Executor service for the simulation sequence handler to run tick execution in a new thread.
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    /**
     * The {@link SimulationSequenceHandler} that executes the simulation tick.
     */
    private final SimulationSequenceHandler simulationSequenceHandler;
    /**
     * Clock speed for the Minecraft tick mode to decide waiting time between executions.
     */
    private int clockSpeed;
    /**
     * Clock mode by which execution timing is decided.
     */
    private ClockMode clockMode;
    /**
     * The {@link SystemClockController} that is holding the model which is observed for changes in the clock state.
     */
    private SystemClockController systemClockController;
    /**
     * Counter for the Minecraft tick mode to decide waiting time between executions.
     */
    private int minecraftTickCounter = 0;
    private boolean realtimeSimulationRunning = false;

    /**
     * Constructor. Creates a new {@link SimulationSequenceHandler} and registers itself as an observer
     * of the {@link SystemClockModel}.
     *
     * @param blockControllers List of all {@link BlockController}s of the associated computer blocks.
     */
    public SimulationTimeHandler(List<IQueryableSimController> blockControllers, IBusSystem busSystem) {
        simulationSequenceHandler = new SimulationSequenceHandler(blockControllers, busSystem, this);
        //Register us as an SystemClockModel Observer
        for(IQueryableSimController blockController: blockControllers) {
            if (blockController.getControllerType() == BlockControllerType.CLOCK) {
                systemClockController = (SystemClockController) blockController;
                ((SystemClockController) blockController).registerModelObserver(this);
            }
        }
        updateObservedState();
    }

    /**
     * Called by the {@link edu.kit.riscjblockits.controller.blocks.SystemClockController} in tick mode to execute the next simulation tick.
     * Runs if Minecraft tick mode is activated.
     */
    public void onMinecraftTick(){
        if(clockMode == ClockMode.MC_TICK && clockSpeed > 0) {
            if (minecraftTickCounter == 0) {
                systemClockController.activateVisualisation();
                runTick();
            }
            minecraftTickCounter = (minecraftTickCounter + 1) % clockSpeed;
        }
    }

    /**
     * Called by the {@link edu.kit.riscjblockits.controller.blocks.SystemClockController} on User input in step mode to execute the next simulation tick.
     * Runs if step mode is activated.
     */
    public void onUserTickTrigger(){
        if (clockMode == ClockMode.STEP) {
            systemClockController.activateVisualisation();
            runTick();
        }
        else if(clockMode == ClockMode.REALTIME && !realtimeSimulationRunning) {
            realtimeSimulationRunning = true;
            runTick();
        }

    }

    /**
     * Called by the {@link SimulationSequenceHandler} when a simulation frame has been complete.
     * Runs if realtime mode is activated.
     */
    public void onSimulationTickComplete(){
        if (clockMode == ClockMode.REALTIME) {
            runTick();
        }
    }

    /**
     * Enqueues the next simulation tick execution in the execution thread.
     */
    private void runTick() {
        executorService.submit(simulationSequenceHandler);
    }

    /**
     * Keeps the clock state updated using the observer pattern.
     */
    @Override
    public void updateObservedState() {
        if(clockMode != ClockMode.REALTIME) {
            realtimeSimulationRunning = false;
        }

        clockSpeed = systemClockController.getClockSpeed();
        clockMode = systemClockController.getClockMode();

        if(clockMode == ClockMode.REALTIME) {
            simulationSequenceHandler.setVisualizationMode(SimulationSequenceHandler.VisualisationMode.FAST);
        }
        else if (clockMode == ClockMode.MC_TICK) {
            simulationSequenceHandler.setVisualizationMode(SimulationSequenceHandler.VisualisationMode.NORMAL);
        }
        else if (clockMode == ClockMode.STEP) {
            simulationSequenceHandler.setVisualizationMode(SimulationSequenceHandler.VisualisationMode.NORMAL);
        }

    }

}
