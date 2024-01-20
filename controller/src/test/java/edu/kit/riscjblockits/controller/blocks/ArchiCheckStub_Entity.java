package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.ArrayList;
import java.util.List;

public class ArchiCheckStub_Entity implements IConnectableComputerBlockEntity {

    private final BlockPosition pos;
    public ArchiCheckStub_Entity(BlockPosition position) {
        pos = position;
    }

    @Override
    public void setBlockModel(IQueryableBlockModel model) {

    }

    @Override
    public List<ComputerBlockController> getComputerNeighbours() {
        return new ArrayList<>();
    }

    @Override
    public BlockPosition getBlockPosition() {
        return pos;
    }

    @Override
    public IDataElement getBlockEntityData() {
        return null;
    }
}
