package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;

/**
 * Defines all Controllers that can receive user input from the view.
 */
public interface IUserInputReceivableController {

    /**
     * If the block is reloaded, a new model is created, and it gets set to its old data from its NBT value.
     * @param data The data that should be set.
     */
    void setData(IDataElement data);

}
