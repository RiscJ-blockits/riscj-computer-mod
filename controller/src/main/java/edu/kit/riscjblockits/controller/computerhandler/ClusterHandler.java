package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;

import java.util.List;

/**
 * Holds one computer cluster maximum (might be incomplete)
 */
public class ClusterHandler implements IArchitectureCheckable {

    public ClusterHandler(BlockController blockController) {
    }
    //check clustering options --> reference class needed or not
    public ClusterHandler combine(List<ClusterHandler> clusterHandlers) {
        return null;
    }
}