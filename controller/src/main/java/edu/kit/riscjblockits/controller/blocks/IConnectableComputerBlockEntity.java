package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.List;

public interface IConnectableComputerBlockEntity {
    //vom Controller zur Entity
    void setBlockModel(IQueryableBlockModel model);

    List<ComputerBlockController> getComputerNeighbours();

    BlockPosition getBlockPosition();

    /**
     * The Controllers can get initial Data from NBT
     * @return
     */
    IDataElement getBlockEntityData();

}
