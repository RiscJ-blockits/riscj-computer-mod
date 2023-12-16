package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;

public class ProgrammingController extends BlockController{
    public ProgrammingController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
        //ToDo ruft auch ein Clustering auf
    }

    /**
     * braucht der ein Model??
     * @return
     */
    @Override
    protected BlockModel createBlockModel() {
        return null;
    }
}
