package edu.kit.riscjblockits.controller.blocks;

/**
 * Defines all block controllers that can be queried by the simulation.
 */
public interface IQueryableSimController extends IQueryableComputerController {

    /**
     * Method to start the visualization of the block of the controller.
     */
    void activateVisualisation();

}
