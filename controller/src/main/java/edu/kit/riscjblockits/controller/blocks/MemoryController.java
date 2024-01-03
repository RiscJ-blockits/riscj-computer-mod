package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.Value;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.MemoryModel;
import edu.kit.riscjblockits.model.data.IDataElement;

public class MemoryController extends ComputerBlockController {
    public MemoryController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
        setControllerType(BlockControllerType.MEMORY);
    }

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
