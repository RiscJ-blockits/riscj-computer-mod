package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.WirelessRegisterModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_XPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_YPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_ZPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;

/**
 * The controller for a WirelessRegisterBlock.
 */
public class WirelessRegisterController extends RegisterController {

    /**
     * Creates a new WirelessRegisterController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public WirelessRegisterController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
    }

    /**
     * Creates the WirelessRegister-specific model.
     * @return The model for the WirelessRegister.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new WirelessRegisterModel();
    }

    /**
     * Sets the shared RegisterModel in the WirelessRegisterModel.
     * @param registerController The controller of the WirelessRegister that is connected with the WirelessRegister.
     */
    public void setRegisterModel(WirelessRegisterController registerController) {
        //wtf?
        ((WirelessRegisterModel)getModel()).setRegisterModel(
                ((WirelessRegisterModel)registerController.getModel()).getRegisterModel());
    }

    /**
     * Sets the Position of the connected WirelessRegister.
     * @param blockPosition The position of the connected WirelessRegister.
     */
    public void setWirelessNeighbourPosition(BlockPosition blockPosition) {
        ((WirelessRegisterModel)getModel()).setWirelessNeighbourPosition(blockPosition);
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
            switch (s) {
                case REGISTER_WORD_LENGTH -> { //ToDo duplicated code: could probably just be removed @Thomas
                    int wordLength;
                    try {
                        wordLength = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    ((WirelessRegisterModel) getModel()).setWordLength(wordLength);
                }
                case REGISTER_VALUE -> { //ToDo duplicated code: could probably just be removed @Thomas
                    int wordLength;
                    Value value;
                    try {
                        wordLength = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(REGISTER_WORD_LENGTH)).getContent());
                        value = Value.fromHex(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent(), wordLength);
                    } catch (ClassCastException | NumberFormatException e) {
                        continue;
                    }
                    ((WirelessRegisterModel) getModel()).setValue(value);
                }
                case REGISTER_WIRELESS -> {
                    IDataContainer position = (IDataContainer) ((IDataContainer) data).get(s);
                    double x, y, z;
                    try {
                        x = Double.parseDouble(((IDataStringEntry) position.get(REGISTER_WIRELESS_XPOS)).getContent());
                        y = Double.parseDouble(((IDataStringEntry) position.get(REGISTER_WIRELESS_YPOS)).getContent());
                        z = Double.parseDouble(((IDataStringEntry) position.get(REGISTER_WIRELESS_ZPOS)).getContent());
                    } catch (NumberFormatException | ClassCastException | NullPointerException e) {
                        continue;
                    }
                    BlockPosition blockPosition = new BlockPosition(x, y, z);
                    ((WirelessRegisterModel) getModel()).setWirelessNeighbourPosition(blockPosition);
                }
                default -> {
                    //do nothing
                }
            }
        }
    }

    /**
     * Getter for the position of the connected WirelessRegister.
      * @return The position of the connected WirelessRegister.
     */
    public BlockPosition getConnectedPos() {
        return ((WirelessRegisterModel) getModel()).getWirelessNeighbourPosition();
    }

    /**
     * Setter for the value inside the register.
     * @param value The new value that should be stored in the register.
     */
    @Override
    public void setNewValue(Value value) {
        //ToDo check input
        ((WirelessRegisterModel)getModel()).setValue(value);
    }

    /**
     * Returns the register type. The types are defined in the Instruction Set Model.
     * @return The type of the register.
     */
    @Override
    public String getRegisterType() {
        return ((WirelessRegisterModel)getModel()).getRegisterType();
    }

    /**
     * Getter for the current value inside the register.
     * @return The Value stored in the register.
     */
    @Override
    public Value getValue() {
        return ((WirelessRegisterModel)getModel()).getValue();
    }

    /**
     * Method to be called when the WirelessRegister is broken.
     */
    @Override
    public void onBroken() {
        super.onBroken();
        ((WirelessRegisterModel)getModel()).onBroken();
    }

}
