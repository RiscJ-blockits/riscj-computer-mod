package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.Value;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
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
    protected IControllerQueryableBlockModel createBlockModel() {
        return new MemoryModel();
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        //ToDo
    }

    /**
     * Returns the value at the given address.
     * @param address The address to read from.
     * @return The value at the given address.
     */
    public Value getValue(Value address) {
        //ToDo
        return null;
    }

    /**
     * Writes the given value to the given address.
     * @param address The address to write to.
     * @param value The value to write.
     */
    public void writeMemory(Value address, Value value) {
        //ToDo
    }

}
