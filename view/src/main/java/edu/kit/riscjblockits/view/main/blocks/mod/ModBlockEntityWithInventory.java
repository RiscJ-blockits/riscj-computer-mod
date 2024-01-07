package edu.kit.riscjblockits.view.main.blocks.mod;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

/**
 * This class represents a block entity from our mod that has a Minecraft inventory.
 * An inventory allows the block to store items.
 * Every Block has its own unique BlockEntity while it is loaded.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public abstract class ModBlockEntityWithInventory extends ModBlockEntity implements ImplementedInventory {

    /**
     * This list contains all items that are stored in the inventory.
     * {@link ItemStack}
     */
    protected final DefaultedList<ItemStack> items;

    /**
     * Creates a new Block Entity with an Inventory with the given settings.
     * @param type Defines what block entity should be created.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     * @param inventorySize The size of the inventory.
     */
    public ModBlockEntityWithInventory(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state);
        items = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    /**
     * Returns the list of items that are stored in the inventory.
     * @return The list of items that are stored in the inventory.
     */
    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

}
