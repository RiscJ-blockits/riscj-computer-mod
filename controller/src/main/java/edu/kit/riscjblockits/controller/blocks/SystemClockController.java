package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.computerhandler.SimulationTimeHandler;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;

public class SystemClockController extends ComputerBlockController {

    private SimulationTimeHandler simulationTimeHandler;
    private boolean simStarted;

    public SystemClockController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
        setControllerType(BlockControllerType.CLOCK);
        simStarted = false;
    }

    @Override
    protected BlockModel createBlockModel() {
        return new SystemClockModel();
    }

    public void setSimulationTimeHandler(SimulationTimeHandler simulationTimeHandler) {
        this.simulationTimeHandler = simulationTimeHandler;
        simStarted = true;
    }

    @Override
    public void tick() {
        if (simStarted) {
            simulationTimeHandler.onMinecraftTick();
        }
    }
}
