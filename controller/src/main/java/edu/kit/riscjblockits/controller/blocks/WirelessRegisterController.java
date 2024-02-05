package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.WirelessRegisterModel;

public class WirelessRegisterController extends RegisterController {

    public WirelessRegisterController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new WirelessRegisterModel();
    }

    public void incrementFrequence() {
        ((WirelessRegisterModel) getModel()).incrementFrequence();
    }
}
