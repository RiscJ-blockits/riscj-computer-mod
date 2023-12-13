package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;

public class SimulationTimeHandler {

    private SimulationSequenceHandler simulationSequenceHandler;
    public SimulationTimeHandler(BlockController[] blockControllers) {
    }

    public void tick(){
        simulationSequenceHandler.simulate();
    }
}
