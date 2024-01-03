package edu.kit.riscjblockits.controller.blocks;

/**
 * Defines all Computer Controllers that take part in the simulation and can be queried by the view.
 */
public interface IUserInputReceivableComputerController extends IUserInputReceivableController {

    /**
     * Must be called every tick from the view.
     */
    void tick();       //tick == UserInput

    /**
     * Must be called when the block is broken.
     */
    void onBroken();

}
