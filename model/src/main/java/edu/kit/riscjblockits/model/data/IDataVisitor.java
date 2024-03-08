package edu.kit.riscjblockits.model.data;

/**
 * Holds data for handling between View and Controller and View and Model.
 */
public interface IDataVisitor {
    /**
     * Will visit a container.
     * @param element The container to be visited.
     */
    void visit(IDataContainer element);

    /**
     * Will visit an entry.
     * @param element The entry to be visited.
     */
    void visit(IDataStringEntry element);
}
