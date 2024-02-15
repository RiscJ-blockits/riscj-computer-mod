package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_XPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_YPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_ZPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;

/**
 * This class represents the model for a wireless register.
 * It is a normalregister that can sync its data with another wireless neighbor.
 */
public class WirelessRegisterModel extends RegisterModel {

    /**
     * The register model that is shared with the wireless neighbor and holds the data of both registers.
     */
    private RegisterModel registerModel;

    /**
     * The position of the wireless neighbor.
     */
    private BlockPosition wirelessNeighbourPosition;

    /**
     * Creates a new WirelessRegisterModel.
     */
    public WirelessRegisterModel() {
        super();
        registerModel = new RegisterModel();
        wirelessNeighbourPosition = new BlockPosition(0,-300,0);
    }

    /**
     * Sets the position of the associated block in the minecraft world.
     * @param position The position of the associated block in the minecraft world.
     */
    @Override
    public void setPosition(BlockPosition position) {
        super.setPosition(position);
        if (wirelessNeighbourPosition.equals(new BlockPosition(0,-300,0))) {
            wirelessNeighbourPosition = position;
        }
    }

    /**
     * Sets the BlockPosition of the wireless neighbour.
     * @param wirelessNeighbourPosition The BlockPosition of the wireless neighbour.
     */
    public void setWirelessNeighbourPosition(BlockPosition wirelessNeighbourPosition) {
        this.wirelessNeighbourPosition = wirelessNeighbourPosition;
    }

    /**
     * Sets the register model.
     * @param registerModel The register model that holds the data of the register.
     */
    public void setRegisterModel(RegisterModel registerModel) {
        this.registerModel = registerModel;
    }

    /**
     * Getter for the register model.
     * @return The register model that holds the data of the register.
     */
    public RegisterModel getRegisterModel() {
        return registerModel;
    }

    /**
     * Sets the data of the register.
     * @param value The data of the register.
     */
    @Override
    public void setValue(Value value) {
        registerModel.setValue(value);
    }


    /**
     * Getter for the data of the register.
     * @return The data of the register.
     */
    @Override
    public Value getValue() {
        return registerModel.getValue();
    }

    /**
     * Getter for the word length of the register.
     * @param wordLength The word length of the register.
     */
    @Override
    public void setWordLength(int wordLength) {
        registerModel.setWordLength(wordLength);
    }

    /**
     * Getter for the word length of the register.
     * @return The word length of the register.
     */
    @Override
    public IDataElement getData() {
        Data regData = (Data) super.getData();
        Data connectedPos = new Data();
        if (wirelessNeighbourPosition == null) {
            connectedPos.set(REGISTER_WIRELESS_XPOS, new DataStringEntry(String.valueOf(0)));
            connectedPos.set(REGISTER_WIRELESS_YPOS, new DataStringEntry(String.valueOf(0)));
            connectedPos.set(REGISTER_WIRELESS_ZPOS, new DataStringEntry(String.valueOf(0)));
        } else {
            connectedPos.set(REGISTER_WIRELESS_XPOS, new DataStringEntry(String.valueOf(wirelessNeighbourPosition.getX())));
            connectedPos.set(REGISTER_WIRELESS_YPOS, new DataStringEntry(String.valueOf(wirelessNeighbourPosition.getY())));
            connectedPos.set(REGISTER_WIRELESS_ZPOS, new DataStringEntry(String.valueOf(wirelessNeighbourPosition.getZ())));
        }
        regData.set(REGISTER_WIRELESS, connectedPos);
        regData.set(REGISTER_WORD_LENGTH, new DataStringEntry(String.valueOf(registerModel.getWordLength())));
        regData.set(REGISTER_VALUE, new DataStringEntry(registerModel.getValue().getHexadecimalValue()));
        return regData;
    }

    /**
     * Getter for the BlockPosition of the wireless neighbor.
     * @return The BlockPosition of the wireless neighbor.
     */
    public BlockPosition getWirelessNeighbourPosition() {
        return wirelessNeighbourPosition;
    }

}
