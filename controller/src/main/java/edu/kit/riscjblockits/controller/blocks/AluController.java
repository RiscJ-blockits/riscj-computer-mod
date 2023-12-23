package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.AluModel;
import edu.kit.riscjblockits.model.blocks.BlockModel;

public class AluController extends BlockController {
    public AluController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected BlockModel createBlockModel() {
        return new AluModel();
    }

    //@Override
    public boolean isBus() {
        return false;
    }

    @Override
    public Object getModel() {
        return null;
    }
}
