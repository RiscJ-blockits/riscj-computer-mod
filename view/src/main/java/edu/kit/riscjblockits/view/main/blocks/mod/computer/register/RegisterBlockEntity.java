package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
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

/**
 * This class represents a system clock entity from our mod in the game.
 * Every system clock has its own unique SystemClockBlockEntity while it is loaded.
 */
public class RegisterBlockEntity extends ComputerBlockEntity implements ExtendedScreenHandlerFactory {

    /**
     * Creates a new RegisterBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public RegisterBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.REGISTER_BLOCK_ENTITY, pos, state);
    }

    /**
     * Every entity needs its own controller.
     * @return An RegisterController bound to this entity.
     */
    @Override
    protected ComputerBlockController createController() {
        return new RegisterController(this);
    }

    /**
     * ToDo
     * @return
     */
    @Override
    public @Nullable Object getRenderData() {
        return super.getRenderData();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.riscj_blockits.register_block");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new RegisterScreenHandler(syncId, playerInventory, this);
    }

    /**
     * Gets called every tick.
     * Used to update ui elements.
     */
    @Override
    public void updateUI() {
        //ToDo hasUnqueriedStateChange die richtige Variable um aktivität zu messen?
        if (world != null && getModel() != null && getModel().hasUnqueriedStateChange()) {
            if (getModel().hasUnqueriedStateChange()) {
                world.setBlockState(pos, world.getBlockState(pos).with(RegisterBlock.ACTIVE, true));
            } else {
                world.setBlockState(pos, world.getBlockState(pos).with(RegisterBlock.ACTIVE, false));
            }
        }
    }

}
