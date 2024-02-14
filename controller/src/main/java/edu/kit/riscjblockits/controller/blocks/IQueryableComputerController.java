package edu.kit.riscjblockits.controller.blocks;

public interface IQueryableComputerController {

    /**
     * Getter for the Type of the controller.
     * @return The type of the controller.
     */
    BlockControllerType getControllerType();


    /** //ToDo nicht im Entwurf
     * Method to stop the visualisation of the block of the controller.
     */
    void stopVisualisation();

}
