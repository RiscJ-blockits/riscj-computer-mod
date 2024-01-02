package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.data.IDataElement;
import edu.kit.riscjblockits.model.blocks.AluModel;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;

public class AluController extends ComputerBlockController {
    public AluController(IConnectableComputerBlockEntity blockEntity) {
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
    public IViewQueryableBlockModel getModel() {
        return null;
    }

    @Override
    public void setData(IDataElement data) {

    }
}
