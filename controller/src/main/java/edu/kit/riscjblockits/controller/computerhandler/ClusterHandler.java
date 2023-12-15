package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.model.BusSystemModel;
import edu.kit.riscjblockits.model.blocks.BusModel;

import java.util.List;

/**
 * Holds one computer cluster maximum (might be incomplete)
 */
public class ClusterHandler implements IArchitectureCheckable {

    private List<BlockController> clusterBlocks;
    BusSystemModel busSystemModel;
    public ClusterHandler(BlockController blockController) {
        clusterBlocks.add(blockController);

    }

    public void combine() {



    }
}