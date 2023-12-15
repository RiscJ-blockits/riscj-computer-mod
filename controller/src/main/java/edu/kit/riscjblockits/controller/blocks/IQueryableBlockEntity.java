package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.List;

public interface IQueryableBlockEntity {
    public void setBlockModel(BlockModel model);

    List<BlockController> getComputerNeighbours();

    BlockPosition getBlockPosition();
}
