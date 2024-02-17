package edu.kit.riscjblockits.model.blocks;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the synchronized RegisterModel.
 */
public class SynchronizedRegisterModel extends RegisterModel {

    /**
     * The wireless register models that are registered to this synchronized register model.
     */
    private List<WirelessRegisterModel> wirelessRegisterModels;

    /**
     * Creates a new SyncronizedRegisterModel.
     */
    public SynchronizedRegisterModel() {
        super();
        wirelessRegisterModels = new ArrayList<>();
    }

    /**
     * Registers a wireless register model to this synchronized register model.
     * @param wirelessRegisterModel The wireless register model to register.
     */
    public void registerObserver(WirelessRegisterModel wirelessRegisterModel) {
        if (wirelessRegisterModels.contains(wirelessRegisterModel)) {
            return;
        }
        wirelessRegisterModels.add(wirelessRegisterModel);
    }

    /**
     * Removes a wireless register model from this synchronized register model.
     * @param wirelessRegisterModel The wireless register model to remove.
     */
    public void removeObserver(WirelessRegisterModel wirelessRegisterModel) {
        wirelessRegisterModels.remove(wirelessRegisterModel);
        for (WirelessRegisterModel model : wirelessRegisterModels) {
            model.setWirelessNeighbourPosition(getWirelessNeighbourPosition(model));
        }
    }

    /**
     * Notifies all wireless register models that are registered to this synchronized register model.
     */
    public void notifyObservers() {
        for (WirelessRegisterModel wirelessRegisterModel : wirelessRegisterModels) {
            wirelessRegisterModel.update();
        }
    }

    /**
     * Returns a new position of the wireless neighbor.
     * @param wirelessRegisterModel The wireless register model to get the wireless neighbor position for.
     * @return The new position of the wireless neighbor.
     */
    public BlockPosition getWirelessNeighbourPosition(WirelessRegisterModel wirelessRegisterModel) {
        if (wirelessRegisterModels.size() <= 1) {
            return null;
        }
        int index = wirelessRegisterModels.indexOf(wirelessRegisterModel) - 1;
        if (index < 0) {
            index = wirelessRegisterModels.size() - 1;
        }
        return wirelessRegisterModels.get(index).getPosition();
    }
}
