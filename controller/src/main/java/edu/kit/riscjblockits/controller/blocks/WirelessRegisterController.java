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

    public void setRegisterModel(WirelessRegisterController registerController) {
        ((WirelessRegisterModel)getModel()).setRegisterModel(
                ((WirelessRegisterModel)registerController.getModel()).getRegisterModel());
    }

    @Override
    public void tick() {
        super.tick();
    }
}
