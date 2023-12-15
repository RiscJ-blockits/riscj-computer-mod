package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;

public class ProgrammingController extends BlockController{
    protected ProgrammingController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected BlockModel getBlockModel() {
        return null;
    }
}
