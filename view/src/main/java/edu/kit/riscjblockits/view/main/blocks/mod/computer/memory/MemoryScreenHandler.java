package edu.kit.riscjblockits.view.main.blocks.mod.computer.memory;

import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;

import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_ADDRESS;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_MEMORY;
import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;

/**
 * This class represents the screen handler for the memory block.
 * It is used to handle the interaction between the memory screen and the memory block entity.
 */
public class MemoryScreenHandler extends ModScreenHandler {

    /**
     * The inventory of the memory entity.
     */
    private final Inventory inventory;

    /**
     * Is set to true after the screen is opened.
     */
    private boolean opened = false;

    /**
     * Creates a new MemoryScreenHandler with the given settings.
     * @param syncId the syncId
     * @param inventory the player inventory
     * @param buf the buffer with the block position
     */
    public MemoryScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, (ModBlockEntity) inventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    /**
     * Creates a new MemoryScreenHandler for the given ModBlockEntity.
     * @param syncId the syncId
     * @param playerInventory the inventory of the player opening the screen
     * @param blockEntity the block entity opening the screen.
     */
    public MemoryScreenHandler(int syncId, PlayerInventory playerInventory, ModBlockEntity blockEntity) {
        super(RISCJ_blockits.MEMORY_BLOCK_SCREEN_HANDLER, syncId, blockEntity);
        checkSize(((Inventory) blockEntity), 1);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.addSlot(new Slot(inventory, 0, 135, 6) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(RISCJ_blockits.PROGRAM_ITEM);
            }
        });
        addPlayerInventorySlotsLarge(playerInventory);
        if (inventory.getStack(0).getCount() == 0) {
            opened = true;
        }
        addListener(new ScreenHandlerListener() {           //listener for changes in the inventory
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                if (slotId == 0) {
                    //On SlotUpdate gets sometimes called on screen open even when the item is not changed.
                    // We don't want to update the memory in this case because it would reset the simulation
                    if (!opened) {
                        opened = true;
                        return;
                    }
                    ((MemoryBlockEntity) blockEntity).inventoryChanged();
                }
            }
            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                //do nothing
            }
        });
    }

    /**
     * Called when a player attempts to quickly move an item.
     * @param player The player that wants to quickly move an item.
     * @param invSlot The slot that the player wants to quickly move to.
     * @return the item stack of the item that was moved
     */
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;       //ToDo duplicated code
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    /**
     * Used to get a line of memory to display.
     * @param address the line of the memory to get
     * @return a string with the address and the value of the memory at the given line separated by a space
     */
    public String getMemoryLine(int address) {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return "0";
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return "0";
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(MEMORY_MEMORY)) {
                int addressSize = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(DataConstants.MEMORY_ADDRESS)).getContent());
                Value value = Value.fromDecimal(String.valueOf(address), addressSize);          //ToDo Value should hav an fromInt
                IDataElement memLine = ((IDataContainer) ((IDataContainer) data).get(s)).get(value.getHexadecimalValue());
                if (memLine.isEntry()) {
                    return ((IDataStringEntry) memLine).getContent();
                }
                return "0";
            }
        }
        return "0";
    }


    /**
     * Getter for the memory size of the memory block entity.
     * @return the size of the memory in addressable units
     */
    public int getMemorySize() {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return 9;
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return 9;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(MEMORY_ADDRESS)) {
                return (int) Math.pow(Math.pow(2, 8), Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent()));
            }
        }
        return 9;
    }

}
