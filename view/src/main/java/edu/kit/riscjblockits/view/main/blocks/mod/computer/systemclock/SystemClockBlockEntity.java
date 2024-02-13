package edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock.SystemClockBlock.CURSORPOS;
import static edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock.SystemClockBlock.MAX_CURSORPOS;

/**
 * This class represents a system clock entity from our mod in the game.
 * Every system clock has its own unique SystemClockBlockEntity while it is loaded.
 */
public class SystemClockBlockEntity extends ComputerBlockEntity implements ExtendedScreenHandlerFactory {

    private boolean powered;
    private int cursorSide = 0;
    private int tickCounter = 0;

    /**
     * Creates a new SystemClockBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public SystemClockBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.SYSTEM_CLOCK_BLOCK_ENTITY, pos, state);
    }

    /**
     * Every entity needs its own controller.
     * @return An SystemClockController bound to this entity.
     */
    @Override
    protected ComputerBlockController createController() {
        return new SystemClockController(this);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("System Clock");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SystemClockScreenHandler(syncId, playerInventory, this);
    }

    public void setPowered(boolean powered) {
        if (powered && !this.powered) {
            if (getController() == null)
                return;
            ((SystemClockController) getController()).onUserTickTriggered();
        }
        this.powered = powered;
    }



    @Override
    public void updateUI() {
        if (getModel() != null && ((SystemClockModel) getModel()).getVisualisationState())
            updateCursor();

        super.updateUI();



    }

    private void updateCursor() {
        if (world != null) {
            cursorSide = (cursorSide + 1) % (MAX_CURSORPOS + 1);
            world.setBlockState(pos, world.getBlockState(pos).with(CURSORPOS, cursorSide));
        }
    }
}
