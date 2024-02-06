package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.ArrayList;
import java.util.List;

public class ArchiCheckStub_Entity implements IConnectableComputerBlockEntity {

    private final BlockPosition pos;

    List<ComputerBlockController> neighbours;

    public ArchiCheckStub_Entity(BlockPosition position, List<ComputerBlockController> neighbours) {
        pos = position;
        this.neighbours = neighbours;
    }

    public ArchiCheckStub_Entity() {
        pos = new BlockPosition(0,0,0);
        neighbours = new ArrayList<>();
    }

    @Override
    public void setBlockModel(IViewQueryableBlockModel model) {

    }

    @Override
    public List<ComputerBlockController> getComputerNeighbours() {
        return neighbours;
    }

    @Override
    public BlockPosition getBlockPosition() {
        return pos;
    }

    @Override
    public IDataElement getBlockEntityData() {
        return null;
    }

    public void neighborUpdate() {}
}
