package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.computerhandler.SimulationTimeHandler;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;

/**
 * The controller for a system clock block entity.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class SystemClockController extends BlockController {

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
    public SystemClockController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
        setControllerType(BlockControllerType.CLOCK);
        simStarted = false;
    }

    /**
     * Creates the system clock specific model.
     * @return The model for the system clock.
     */
    @Override
    protected BlockModel createBlockModel() {
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

}
