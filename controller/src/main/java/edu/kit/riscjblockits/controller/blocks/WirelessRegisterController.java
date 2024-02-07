package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.WirelessRegisterModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;

import static edu.kit.riscjblockits.model.data.DataConstants.*;

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
        ((WirelessRegisterModel)getModel()).setWirelessNeighbourPosition(
                registerController.getModel().getPosition());
    }

    public void setWirelessNeighbourPosition(BlockPosition blockPosition) {
        ((WirelessRegisterModel)getModel()).setWirelessNeighbourPosition(blockPosition);
    }

    @Override
    public void tick() {
        super.tick();
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        super.setData(data);
        if (!data.isContainer()) {
            return;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(REGISTER_WIRELESS)) {
                IDataContainer position = (IDataContainer) ((IDataContainer) data).get(s);
                double x = Double.parseDouble(((IDataStringEntry) position.get(REGISTER_WIRELESS_XPOS)).getContent());
                double y = Double.parseDouble(((IDataStringEntry) position.get(REGISTER_WIRELESS_YPOS)).getContent());
                double z = Double.parseDouble(((IDataStringEntry) position.get(REGISTER_WIRELESS_ZPOS)).getContent());
                BlockPosition blockPosition = new BlockPosition(x, y, z);
                ((WirelessRegisterModel) getModel()).setWirelessNeighbourPosition(blockPosition);
            }
        }
    }

    public BlockPosition getConnectedPos() {
        return ((WirelessRegisterModel) getModel()).getWirelessNeighbourPosition();
    }
}
