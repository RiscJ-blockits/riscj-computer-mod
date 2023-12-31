package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.MemoryModel;

/**
 * The controller for a Memory block entity.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class MemoryController extends BlockController {

    /**
     * Creates a new MemoryController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public MemoryController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
        setControllerType(BlockControllerType.MEMORY);
    }

    /**
     * Creates the Memory specific model.
     * @return The model for the Memory.
     */
    @Override
    protected BlockModel createBlockModel() {
        return new MemoryModel();
    }
}
