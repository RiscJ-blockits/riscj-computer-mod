package edu.kit.riscjblockits.view.main.blocks.mod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

/**
 * Every Handled Screen needs a ScreenHandler.
 * This class is the base class for all ScreenHandlers in this mod.
 * It extends the base minecraft ScreenHandler.
 */
public abstract class ModScreenHandler extends ScreenHandler {

    /**
     * The block entity that this screen handler is bound to.
     */
    private final ModBlockEntity blockEntity;

    /**
     * Creates a new ModScreenHandler with the given type, sync id and block entity.
     * @param type The type of the screen handler.
     * @param syncId The sync id of the screen handler.
     * @param blockEntity The block entity that this screen handler is bound to.
     */
    protected ModScreenHandler(@Nullable ScreenHandlerType<?> type,
                               int syncId, ModBlockEntity blockEntity) {
        super(type, syncId);
        this.blockEntity = blockEntity;
    }

    /**
     * Needs to be overridden by the extending classes if they want quick move to work.
     * @param player The player that wants to quickly move an item.
     * @param slot The slot that the player wants to quickly move to.
     * @return in this base version always an empty item stack.
     */
    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    /**
     * Checks if the player can use the block.
     * @param player the player that wants to use the block.
     * @return always true, because everyone can use our blocks.
     */
    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    /**
     * Version for normal screens.
     * We display the player inventory on the bottom of all our block screens.
     * This adds the player inventory slots to the screen.
     * @param playerInventory the player inventory.
     */
    protected void addPlayerInventorySlots(PlayerInventory playerInventory) {
        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    /**
     * Version for large screens.
     * We display the player inventory on the bottom of all our block screens.
     * This adds the player inventory slots to the screen.
     * @param playerInventory the player inventory.
     */
    protected void addPlayerInventorySlotsLarge(PlayerInventory playerInventory) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }
        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
        }
    }

    /**
     * The screens need access to the block entity for setting and getting data.
     * @return The block entity that this screen handler is bound to.
     */
    public ModBlockEntity getBlockEntity() {
        return blockEntity;
    }

}
