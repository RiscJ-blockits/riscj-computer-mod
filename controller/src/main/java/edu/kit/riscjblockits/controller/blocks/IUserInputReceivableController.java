package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;

//Zugriff vom View auf den Controller
public interface IUserInputReceivableController {

    /**
     * If the block is reloaded a new model is created and it gets set to its old data from its NBT value.
     * @param data
     */
    void setData(IDataElement data);
}
