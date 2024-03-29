package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.TickPriority;

/**
 * This class defines the block entity for a register that always outputs the redstone power it has stored.
 */
public class RedstoneOutputBlockEntity extends RegisterBlockEntity {

    /**
     * The redstone power that the block should output.
     * Between 0 and 15.
     */
    private int power;

    /**
     * Creates a new RedstoneOutputBlockEntity.
     * @param pos The position of the block in the minecraft world for which the entity should be created.
     * @param state The state of the minecraft block for which the entity should be created.
     */
    public RedstoneOutputBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.REDSTONE_OUTPUT_BLOCK_ENTITY, pos, state);
        power = 0;
    }

    /**
     * Getter for the redstone power that the block should output.
     * @return The redstone power that the block should output.
     */
    public int getRedstonePower() {
        return power;
    }

    /**
     * Is called every tick. Schedule a block tick to update the block when the value of the register changes.
     */
    @Override
    public void updateUI() {
        super.updateUI();
        if (getModel() == null || world == null || !getModel().hasUnqueriedStateChange()) return;
        String powerString = ((IDataStringEntry)((IDataContainer) getModel().getData()).get(DataConstants.REGISTER_VALUE)).getContent();
        int newPower;
        try {
            newPower = Integer.parseInt(powerString, 16);
            newPower = Math.max(Math.min(newPower, 15), 0);
        } catch (NumberFormatException e) {
            return;
        }
        if (newPower == power) return;
        power = newPower;
        world.scheduleBlockTick(pos, getCachedState().getBlock(), 0, TickPriority.byIndex(1));      //update the block
    }

}
