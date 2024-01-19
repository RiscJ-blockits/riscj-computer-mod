package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;

public interface IViewQueryableBlockModel extends IQueryableBlockModel {

    /**
     * returns whether the state of the block has changed since the last query
     * @return true if the state has changed, false otherwise
     */
    boolean hasUnqueriedStateChange();              //ToDo brauchen wir das?

    /**
     * Returns the data the view needs to display.
     * @return the data
     */
    IDataElement getData();

}
