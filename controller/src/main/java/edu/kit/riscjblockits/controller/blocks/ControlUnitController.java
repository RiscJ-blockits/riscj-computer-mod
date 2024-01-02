package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;

public class ControlUnitController extends BlockController{
    public ControlUnitController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected BlockModel createBlockModel() {
        return null;
    }

    @Override
    public boolean isBus() {
        return false;
    }
}
