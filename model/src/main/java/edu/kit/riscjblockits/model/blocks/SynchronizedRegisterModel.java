package edu.kit.riscjblockits.model.blocks;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the synchronized RegisterModel.
 * It is used to synchronize the wireless register models.
 */
public class SynchronizedRegisterModel extends RegisterModel {

    /**
     * The wireless register models that are registered to this synchronized register model.
     */
    private List<WirelessRegisterModel> wirelessRegisterModels;

    /**
     * Creates a new SynchronizedRegisterModel.
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
        wirelessRegisterModel.update();
        for (WirelessRegisterModel model : wirelessRegisterModels) {
            model.setConnected(wirelessRegisterModels.size() > 1);
        }
    }

    /**
     * Removes a wireless register model from this synchronized register model.
     * @param wirelessRegisterModel The wireless register model to remove.
     */
    public void removeObserver(WirelessRegisterModel wirelessRegisterModel) {
        wirelessRegisterModels.remove(wirelessRegisterModel);
        for (WirelessRegisterModel model : wirelessRegisterModels) {
            model.setWirelessNeighbourPosition(wirelessRegisterModels.get(0).getPosition());
            model.setConnected(wirelessRegisterModels.size() > 1);
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

}
