package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.List;

public interface IConnectableComputerBlockEntity {
    //vom Controller zur Entity
    void setBlockModel(BlockModel model);

    List<ComputerBlockController> getComputerNeighbours();

    BlockPosition getBlockPosition();

}
