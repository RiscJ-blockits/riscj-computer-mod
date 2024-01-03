package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.computerhandler.SimulationTimeHandler;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;
import edu.kit.riscjblockits.model.data.IDataElement;

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

    /**
     * If the block is reloaded a new model is created and it gets set to its old data from its NBT value.
     * Is also used for setting a new clock mode.
     * @param data
     */
    @Override
    public void setData(IDataElement data) {

    }
}
