package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;

public class MemoryController extends BlockController {
    protected MemoryController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected BlockModel getBlockModel() {
        return null;
    }

    public static class RegisterController {
    }
}
