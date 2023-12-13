package edu.kit.riscjblockits.view.main.blocks.mod;

import edu.kit.riscjblockits.view.main.blocks.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public abstract class ModBlockEntityWithInventory extends ModBlockEntity implements ImplementedInventory {
    protected final DefaultedList<ItemStack> items;
    public ModBlockEntityWithInventory(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state);
        items = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
}
