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
 * It is a NormalRegister that can sync its data with another wireless neighbor.
 */
public class WirelessRegisterModel extends RegisterModel {

    /**
     * The register model that is shared with the wireless neighbor and holds the data of both registers.
     */
    private SynchronizedRegisterModel registerModel;

    /**
     * The position of the wireless neighbor.
     */
    private BlockPosition wirelessNeighbourPosition;

    /**
     * Creates a new WirelessRegisterModel.
     */
    public WirelessRegisterModel() {
        super();
        registerModel = new SynchronizedRegisterModel();
        registerModel.registerObserver(this);
    }

    /**
     * Sets the position of the wireless neighbor.
     * @param wirelessNeighbourPosition The position of the wireless neighbor.
     */
    public void setWirelessNeighbourPosition(BlockPosition wirelessNeighbourPosition) {
        this.wirelessNeighbourPosition = wirelessNeighbourPosition;
    }

    /**
     * Sets the position of the associated block in the minecraft world.
     * @param position The position of the associated block in the minecraft world.
     */
    @Override
    public void setPosition(BlockPosition position) {
        super.setPosition(position);
    }

    /**
     * Sets the register model.
     * @param registerModel The register model that holds the data of the register.
     */
    public void setRegisterModel(SynchronizedRegisterModel registerModel) {
        this.registerModel = registerModel;
        registerModel.registerObserver(this);
    }

    /**
     * Getter for the register model.
     * @return The register model that holds the data of the register.
     */
    public SynchronizedRegisterModel getRegisterModel() {
        return registerModel;
    }

    /**
     * Sets the data of the register.
     * @param value The data of the register.
     */
    @Override
    public void setValue(Value value) {
        registerModel.notifyObservers();
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
        registerModel.notifyObservers();
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
            connectedPos.set(REGISTER_WIRELESS_YPOS, new DataStringEntry(String.valueOf(-300)));
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
        if (wirelessNeighbourPosition != null) {
            return wirelessNeighbourPosition;
        } else  {
            return new BlockPosition(0, -300, 0);
        }
    }

    /**
     * Sets whether this block has a state change that has not been queried by the view yet.
     */
    public void update() {
        setUnqueriedStateChange(true);
    }

    /**
     * Notifies the syncronized register model that the wireless register model has been broken.
     */
    public void onBroken() {
        registerModel.removeObserver(this);
    }
}
