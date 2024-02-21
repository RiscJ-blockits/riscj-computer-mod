package edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;
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

import static edu.kit.riscjblockits.model.blocks.ClockMode.REALTIME;
import static edu.kit.riscjblockits.model.blocks.ClockMode.STEP;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_SPEED;
import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;
import static edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock.SystemClockBlock.CURSORPOS;
import static edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock.SystemClockBlock.MAX_CURSORPOS;

/**
 * This class represents a system clock entity from our mod in the game.
 * Every system clock has its own unique SystemClockBlockEntity while it is loaded.
 */
public class SystemClockBlockEntity extends ComputerBlockEntity implements ExtendedScreenHandlerFactory {

    /**
     * Determines if the system clock is receiving redstone power.
     */
    private boolean powered;

    /**
     * The position of the clock hand on the texture inside the world.
     */
    private int cursorSide = 0;

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
     * @return A SystemClockController bound to this entity.
     */
    @Override
    protected ComputerBlockController createController() {
        return new SystemClockController(this);
    }

    /**
     * Called when the screen is opened.
     * We send the position to the screen.
     * @param player the player that is opening the screen
     * @param buf    the packet buffer to write the data to
     */
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    /**
     * On the top of the display is a heading text.
     * @return The display name of the system clock. Is a translatable text.
     */
    @Override
    public Text getDisplayName() {
        return Text.translatable("block.riscj_blockits.system_clock_block");
    }

    @Override
    public Text getGoggleText() {
        double speed = getSystemClockSpeed();
        for (int i = 0; i < SystemClockScreenHandler.SECONDS_TRANSLATIONS.length; i++) {
            if (speed == SystemClockScreenHandler.SECONDS_TRANSLATIONS[i][0]) {
                speed = SystemClockScreenHandler.SECONDS_TRANSLATIONS[i][1];
                break;
            }
        }
        String speedString = String.valueOf(speed);
        if (getSystemClockMode().equals(REALTIME.toString())) {
            speedString = "NaN";
        }
        return Text.translatable("block.riscj_blockits.system_clock_block")
                .append("\n")
                .append(Text.translatable("riscj_blockits.clockmode"))
                .append(": " + getSystemClockMode() + "\n")
                .append(Text.translatable("riscj_blockits.clockspeed"))
                .append(": " + speedString);
    }

    /**
     * Creates a new SystemClockScreenHandler for the player.
     * @param syncId The id of the screen.
     * @param playerInventory The inventory of the player.
     * @param player The player that is opening the screen.
     * @return super.createMenu(syncId, playerInventory, player);
     */
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SystemClockScreenHandler(syncId, playerInventory, this);
    }

    /**
     * Is called when the redstone power state of the block changes.
     * Updates the controller.
     * @param powered if the block is powered or not.
     */
    public void setPowered(boolean powered) {
        if (powered && !this.powered) {
            if (getController() == null) return;
            ((SystemClockController) getController()).onUserTickTriggered();
            updateCursor();
            assert world != null;
            world.setBlockState(pos, world.getBlockState(pos).with(RISCJ_blockits.ACTIVE_STATE_PROPERTY, true));
        }
        this.powered = powered;
    }

    /**
     * Getter for the current speed from the model.
     * @return The current speed of the system clock.
     */
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

    /**
     * Getter for the current mode from the model.
     * @return The current mode of the system clock.
     */
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

    /**
     * Is called every tick.
     * Updates the in world texture.
     */
    @Override
    public void updateUI() {
        if (getModel() != null && ((SystemClockModel) getModel()).getVisualisationState() && world != null) {
            updateCursor();
            world.setBlockState(pos, world.getBlockState(pos).with(RISCJ_blockits.ACTIVE_STATE_PROPERTY, true));
        } else if (getModel() != null && world != null) {
            world.setBlockState(pos, world.getBlockState(pos).with(RISCJ_blockits.ACTIVE_STATE_PROPERTY, false));
        }
    }

    /**
     * Sets the clock hand in the in world texture to the next position.
     */
    private void updateCursor() {
        if (world != null) {
            cursorSide = (cursorSide + 1) % (MAX_CURSORPOS + 1);
            world.setBlockState(pos, world.getBlockState(pos).with(CURSORPOS, cursorSide));
        }
    }

}
