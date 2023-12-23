package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;

public class MemoryController extends BlockController {
    public MemoryController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected BlockModel createBlockModel() {
        return null;
    }
}
