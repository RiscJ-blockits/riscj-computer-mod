package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;

/**
 * The controller for a Programming block entity.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class ProgrammingController extends BlockController{

    /**
     * Creates a new ProgrammingController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public ProgrammingController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
        //ToDo ruft auch ein Clustering auf -> Nein
    }

    /**
     * ToDo braucht der ein Model?? wahrcheinlich nicht
     * @return
     */
    @Override
    protected BlockModel createBlockModel() {
        return null;
    }

}
