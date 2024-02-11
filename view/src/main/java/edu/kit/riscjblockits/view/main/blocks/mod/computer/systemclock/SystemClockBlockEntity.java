package edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static edu.kit.riscjblockits.model.blocks.ClockMode.STEP;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_SPEED;
import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;

/**
 * This class represents a system clock entity from our mod in the game.
 * Every system clock has its own unique SystemClockBlockEntity while it is loaded.
 */
public class SystemClockBlockEntity extends ComputerBlockEntity implements ExtendedScreenHandlerFactory {

    private boolean powered;

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

    @Override
    public Text getGoggleText() {

        return Text.translatable("block.riscj_blockits.system_clock_block")
                .append("\n")
                .append(Text.translatable("riscj_blockits.clockmode"))
                .append(": " + getSystemClockMode() + "\n")
                .append(Text.translatable("riscj_blockits.clockspeed"))
                .append(": " + getSystemClockSpeed());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SystemClockScreenHandler(syncId, playerInventory, this);
    }

    public void setPowered(boolean powered) {
        if (powered && !this.powered) {
            ((SystemClockController) getController()).onUserTickTriggered();
        }
        this.powered = powered;
    }

    public int getSystemClockSpeed() {
        NbtCompound nbt = this.createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return 0;
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return 0;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CLOCK_SPEED)) {
                return Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent());
            }
        }
        return 0;
    }

    public String getSystemClockMode() {
        NbtCompound nbt = this.createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return STEP.toString();
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return STEP.toString();
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CLOCK_MODE)) {
                return ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            }
        }
        return STEP.toString();
    }


}
