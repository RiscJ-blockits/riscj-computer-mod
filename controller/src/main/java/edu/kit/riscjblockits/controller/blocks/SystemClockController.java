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
        if (getClockMode() == ClockMode.REALTIME) {
            spawnEffect(IConnectableComputerBlockEntity.ComputerEffect.SMOKE);
        }
    }

    /**
     * Called when the system clock is powered. (0 -> 1)
     * Used to start the next simulation step. When in step mode
     */
    public void onUserTickTriggered() {
        if (simStarted) {
            simulationTimeHandler.onUserTickTrigger();
        }
        ((SystemClockModel) getModel()).setActiveTick(true);
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
        super.setData(data);
        if (!data.isContainer()) {
            return;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CLOCK_SPEED)) {
                String value = ((IDataStringEntry) ((IDataContainer) data).get(CLOCK_SPEED)).getContent();
                try {
                    ((SystemClockModel) getModel()).setClockSpeed(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    ((SystemClockModel) getModel()).setClockSpeed(1);
                }
            } else if (s.equals(CLOCK_MODE)) {
                String value = ((IDataStringEntry) ((IDataContainer) data).get(CLOCK_MODE)).getContent();
                try {
                    ((SystemClockModel) getModel()).setClockMode(ClockMode.valueOf(value));
                } catch (IllegalArgumentException e) {
                    ((SystemClockModel) getModel()).setClockMode(ClockMode.STEP);
                }
            }
        }
    }

    /**
     * Setter for the simulation mode in the model can be used if the simulation mode got changed during the simulation.
     * @param mode The new simulation mode.
     */
    public void setSimulationMode(ClockMode mode) {
        ((SystemClockModel) getModel()).setClockMode(mode);
    }

    /**
     * Registers an observer for the simulation timing.
     * @param simulationTimingObserver The observer to register.
     */
    public void registerModelObserver(ISimulationTimingObserver simulationTimingObserver) {
        ((SystemClockModel) getModel()).registerObserver(simulationTimingObserver);
    }

    /**
     * Getter for the clock speed.
     * @return The clock speed.
     */
    public int getClockSpeed() {
        return ((SystemClockModel) getModel()).getClockSpeed();
    }

    /**
     * Getter for the clock mode.
     * @return The clock mode.
     */
    public ClockMode getClockMode() {
        return ((SystemClockModel) getModel()).getClockMode();
    }

}
