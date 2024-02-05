package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.simulation.SimulationTimeHandler;
import edu.kit.riscjblockits.model.blocks.ClockMode;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.ISimulationTimingObserver;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;

import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_SPEED;

/**
 * The controller for a system clock block entity.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class SystemClockController extends ComputerBlockController {

    /**
     * The simulation time handler that is responsible for the simulation of the computer this controller is part of.
     * Is null if the computer is not completely built.
     */
    private SimulationTimeHandler simulationTimeHandler;

    /**
     * Indicates if the simulation has started.
     */
    private boolean simStarted;

    /**
     * Creates a new SystemClockController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public SystemClockController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity, BlockControllerType.CLOCK);
        simStarted = false;
    }

    /**
     * Creates the system clock specific model.
     * @return The model for the system clock.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new SystemClockModel();
    }

    /**
     * Sets the simulation time handler that is responsible for the simulation of the computer this controller is part of.
     * Is called by the cluster handler when the computer is completely built.
     * It needs to be set so that ticks can be forwarded to the simulation time handler.
     * @param simulationTimeHandler The simulation time handler.
     */
    public void setSimulationTimeHandler(SimulationTimeHandler simulationTimeHandler) {
        this.simulationTimeHandler = simulationTimeHandler;
        simStarted = simulationTimeHandler != null;
    }

    /**
     * Called every tick by the system clock entity from this controller.
     * Used to start the next simulation step.
     */
    @Override
    public void tick() {
        if (simStarted) {
            simulationTimeHandler.onMinecraftTick();
        }
    }

    /**
     * Called when the system clock is powered. (0 -> 1)
     * Used to start the next simulation step. when in step mode
     */
    public void onUserTickTriggered() {
        if (simStarted) {
            simulationTimeHandler.onUserTickTrigger();
        }
    }

    /**
     * If the block is reloaded, a new model is created, and it gets set to its old data from its NBT value.
     * Is also used for setting a new clock mode.
     * @param data  The data that should be set.
     *              Data Format:    key: "speed", value: clockSpeed as String
     *                              key: "mode", value: mode as String
     */
    @Override
    public void setData(IDataElement data) {
        if (!data.isContainer()) {
            return;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CLOCK_SPEED)) {
                String value = ((IDataStringEntry) ((IDataContainer) data).get(CLOCK_SPEED)).getContent();
                ((SystemClockModel) getModel()).setClockSpeed(Integer.parseInt(value));          //ToDo error handling hier nötig?
            } else if (s.equals(CLOCK_MODE)) {
                String value = ((IDataStringEntry) ((IDataContainer) data).get(CLOCK_MODE)).getContent();
                ((SystemClockModel) getModel()).setClockMode(ClockMode.valueOf(value));         //ToDo error handling hier nötig?
            }
        }
    }

    public void registerModelObserver(ISimulationTimingObserver simulationTimingObserver) {
        ((SystemClockModel) getModel()).registerObserver(simulationTimingObserver);
    }

    public int getClockSpeed() {
        return ((SystemClockModel) getModel()).getClockSpeed();
    }

    public ClockMode getClockMode() {
        return ((SystemClockModel) getModel()).getClockMode();
    }

}
