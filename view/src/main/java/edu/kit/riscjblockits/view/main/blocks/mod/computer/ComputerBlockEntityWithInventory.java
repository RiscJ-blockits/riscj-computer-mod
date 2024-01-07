package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.view.main.blocks.mod.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
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
}
