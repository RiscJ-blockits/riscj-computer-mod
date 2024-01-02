package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IQueryableBlockModel;

//Zugriff vom View auf den Controller (Für Computer Blöcke)
public interface IUserInputReceivableComputerController extends IUserInputReceivableController {
    //tick == UserInput
    void tick();

    void onBroken();

    IQueryableBlockModel getModel();
}
