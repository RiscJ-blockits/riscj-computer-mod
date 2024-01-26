package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;

//Zugriff vom View auf das Model
public interface IQueryableBlockModel {
    ModelType getType();

    IDataElement getData();

    boolean hasUnqueriedStateChange();

}
