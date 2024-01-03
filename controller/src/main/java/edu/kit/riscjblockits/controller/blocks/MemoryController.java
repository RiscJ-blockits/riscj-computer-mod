package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.Value;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.MemoryModel;
import edu.kit.riscjblockits.model.data.IDataElement;

/**
 * The controller for a Memory block entity.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class MemoryController extends ComputerBlockController {

    /**
     * Creates a new MemoryController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public MemoryController(IConnectableComputerBlockEntity blockEntity) {
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

    @Override
    public void setData(IDataElement data) {

    }

    public Value getValue(Value address) {
        return null;
    }

    public void writeMemory(Value address, Value value) {

    }

}
