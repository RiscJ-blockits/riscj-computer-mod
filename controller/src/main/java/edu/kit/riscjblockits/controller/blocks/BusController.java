package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.BusModel;

public class BusController extends ComputerBlockController{
    public BusController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected BlockModel createBlockModel() {
        return new BusModel();
    }

    @Override
    public boolean isBus() {
        return true;
    }
}
