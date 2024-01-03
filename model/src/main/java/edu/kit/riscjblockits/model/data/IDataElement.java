package edu.kit.riscjblockits.model.data;

/**
 * Holds data for handling between View and Controller and View and Model
 */
public interface IDataElement {

    /**
     * @return true if this element is a container
     */
    boolean isContainer();

    /**
     * @return true if this element is an entry
     */
    boolean isEntry();
}
