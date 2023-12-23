package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationTimeHandler {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private SimulationSequenceHandler simulationSequenceHandler;
    private List<BlockController> blocks;
    public SimulationTimeHandler(List<BlockController> blockControllers) {
        blocks = blockControllers;
    }

    public void tick(){
        simulationSequenceHandler = new SimulationSequenceHandler(blocks);
        executorService.execute(simulationSequenceHandler);
    }
}
