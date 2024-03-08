package edu.kit.riscjblockits.model.data;

/**
 * Holds data for handling between View and Controller and View and Model.
 */
public interface IDataElement {

    /**
     * Checks if this element is a container.
     * @return true if this element is a container
     */
    boolean isContainer();

    /**
     * Checks if this element is an entry.
     * @return true if this element is an entry
     */
    boolean isEntry();

    /**
     * Will receive a visitor.
     * @param visitor The visitor to be visited.
     */
    void receive(IDataVisitor visitor);
}
