package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;

/**
 * Defines all block controllers that can be queried by the simulation.
 */
public interface IQueryableSimController extends IQueryableComputerController {

    //ToDo nicht im Entwurfs wiki
    public void activateVisualisation();


    //ToDo nicht im Entwurfs wiki
    public void stopVisualisation();

}
