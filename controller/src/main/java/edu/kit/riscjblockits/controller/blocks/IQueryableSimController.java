package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;

/**
 * Defines all block controllers that can be queried by the simulation.
 */
public interface IQueryableSimController extends IQueryableComputerController {

    /** //ToDo nicht im Entwurfs wiki
     * Method to start the visualisation of the block of the controller.
     */
    public void activateVisualisation();

    /** //ToDo nicht im Entwurfs wiki
     * Method to stop the visualisation of the block of the controller.
     */
    public void stopVisualisation();

}
