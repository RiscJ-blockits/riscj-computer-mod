package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.simulation.SimulationTimeHandler;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.ISimulationTimingObserver;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;
import edu.kit.riscjblockits.model.data.IDataElement;

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
        simStarted = true;
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
     * If the block is reloaded, a new model is created, and it gets set to its old data from its NBT value.
     * Is also used for setting a new clock mode.
     * @param data  The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        //ToDo
        ((SystemClockModel) getModel()).setClockMode(null);
    }

    public void registerModelObserver(ISimulationTimingObserver simulationTimingObserver) {
        ((SystemClockModel) getModel()).registerObserver(simulationTimingObserver);
    }

}
