package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.IORegisterController;
import edu.kit.riscjblockits.model.blocks.IORegisterModel;
import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.TickPriority;

public class RedstoneOutputBlockEntity extends ComputerBlockEntity {

    private int power;

    public RedstoneOutputBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.REDSTONE_OUTPUT_BLOCK_ENTITY, pos, state);
        power = 0;
    }

    @Override
    protected ComputerBlockController createController() {
        return new IORegisterController(this, false, IORegisterModel.REDSTONE_OUTPUT);
    }

    public int getRedstonePower() {
        return power;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (getModel() == null || world == null || !getModel().hasUnqueriedStateChange()) return;
        String powerString = ((IDataStringEntry)((IDataContainer) getModel().getData()).get(DataConstants.REGISTER_VALUE)).getContent();
        try {
            power = Integer.parseInt(powerString);
            power = Math.max(Math.min(power, 15), 0);
        } catch (NumberFormatException e) {
            return;
        }
        world.scheduleBlockTick(pos, getCachedState().getBlock(), 0, TickPriority.byIndex(1));      //update the block
        getModel().onStateQuery();
    }

}
