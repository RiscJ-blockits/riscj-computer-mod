package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.MemoryModel;

public class MemoryController extends BlockController {
    public MemoryController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
        setControllerType(BlockControllerType.MEMORY);
    }

    @Override
    protected BlockModel createBlockModel() {
        return new MemoryModel();
    }
}
