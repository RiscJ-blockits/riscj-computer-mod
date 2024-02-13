package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.ArrayList;
import java.util.List;

public class ArchiCheckStub_ClusterHandler extends ClusterHandler {

    List<IQueryableClusterController> blockController;

    public ArchiCheckStub_ClusterHandler(List<IQueryableClusterController> blockController) {
        super(new RegisterController(new ArchiCheckStub_Entity(new BlockPosition(0,0,0), new ArrayList<>())));
        this.blockController = blockController;
    }

    public List<IQueryableClusterController> getBlocks() {
        return blockController;
    }

    @Override
    public boolean isNeighbourPosition(BlockController block1, BlockController block2) {
        return true;
    }

}
