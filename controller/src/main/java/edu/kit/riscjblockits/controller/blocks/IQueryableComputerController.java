package edu.kit.riscjblockits.controller.blocks;

/**
 * This interface defines the methods that a BlockController provides to the ClusterHandler/ClusterArchitectureHandler.
 */
public interface IQueryableComputerController {

    /**
     * Getter for the Type of the controller.
     * @return The type of the controller.
     */
    BlockControllerType getControllerType();

    /**
     * Method to stop the visualization of the block of the controller.
     */
    void stopVisualisation();

}
