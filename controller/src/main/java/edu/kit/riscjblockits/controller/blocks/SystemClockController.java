package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.computerhandler.SimulationTimeHandler;
import edu.kit.riscjblockits.model.blocks.BlockModel;

public class SystemClockController extends BlockController {

    SimulationTimeHandler simulationTimeHandler;

    protected SystemClockController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected BlockModel createBlockModel() {
        return null;
    }

    public void setSimulationTimeHandler(SimulationTimeHandler simulationTimeHandler) {
        this.simulationTimeHandler = simulationTimeHandler;
    }

    public void tick(){
        simulationTimeHandler.tick();
    }
}
