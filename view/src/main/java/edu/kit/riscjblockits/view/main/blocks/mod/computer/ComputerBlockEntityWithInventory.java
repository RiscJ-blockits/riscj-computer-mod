package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.view.main.blocks.mod.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

/**
 * This class provides a default implementation for a {@link ComputerBlockEntity} with an inventory.
 */
public abstract class ComputerBlockEntityWithInventory extends ComputerBlockEntity implements ImplementedInventory {

    /**
     * The items in the inventory of this {@link ComputerBlock}.
     */
    protected final DefaultedList<ItemStack> items;

    /**
     * Creates a new ComputerBlockEntityWithInventory with the given settings.
     * @param type The type of the block entity.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the  ComputerBlock.
     * @param inventorySize The size of the inventory.
     */
    public ComputerBlockEntityWithInventory(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state);
        items = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    /**
     * Get the Items in the inventory of this {@link ComputerBlock}.
     * @return the Items in the inventory of this {@link ComputerBlock}.
     */
    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    /**
     * Reads the items from the given {@link NbtCompound}.
     * Reads block information from the given {@link NbtCompound}.
     * @param nbt
     */
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
        inventoryChanged();
    }

    /**
     * Writes inventory and block information to the given {@link NbtCompound}.
     * @param nbt
     */
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, items);
    }

    /**
     * Must be called by the block-specific screen handler if the inventory has changed.
     * Is called when the block is loaded.
     */
    public void inventoryChanged() {
        //do nothing
    }

}
