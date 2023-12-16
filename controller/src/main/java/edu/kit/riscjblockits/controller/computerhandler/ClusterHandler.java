package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.model.BusSystemModel;
import edu.kit.riscjblockits.model.blocks.BusModel;

import java.util.List;

/**
 * Holds one computer cluster maximum (might be incomplete)
 */
public class ClusterHandler implements IArchitectureCheckable {

    private List<BlockController> blocks;
    private List<BlockController> busBlocks;
    BusSystemModel busSystemModel;
    public ClusterHandler(BlockController blockController) {
        if (blockController.isBus()) {
            busBlocks.add(blockController);
        } else {
            blocks.add(blockController);
        }
        busSystemModel = new BusSystemModel(blockController.getBlockPosition());
        List<BlockController> neighbourBlockControllers = blockController.getNeighbours();
    }

    public void combine() {



    }
}