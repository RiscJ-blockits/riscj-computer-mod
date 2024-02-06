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

public class MemoryScreenHandler extends ModScreenHandler {

    private final Inventory inventory;
    private  final MemoryBlockEntity blockEntity;

    public MemoryScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, (ModBlockEntity) inventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    public MemoryScreenHandler(int syncId, PlayerInventory playerInventory, ModBlockEntity blockEntity) {
        super(RISCJ_blockits.MEMORY_BLOCK_SCREEN_HANDLER, syncId, blockEntity);
        checkSize(((Inventory) blockEntity), 1);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.blockEntity = ((MemoryBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 135, 6));

        addPlayerInventorySlotsLarge(playerInventory);

        addListener(new ScreenHandlerListener() {           //listener for changes in the inventory
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                if (slotId == 0) {
                    ((MemoryBlockEntity) blockEntity).inventoryChanged();
                }
            }
            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                //do nothing
            }
        });
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
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
     * @return the size of the memory in adressable units
     */
    public int getMemorySize(){
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
