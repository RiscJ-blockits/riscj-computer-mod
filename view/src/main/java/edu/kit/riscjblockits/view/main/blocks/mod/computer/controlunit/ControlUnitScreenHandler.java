package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.*;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;

public class ControlUnitScreenHandler extends ModScreenHandler {

    private final Inventory inventory;

    public ControlUnitScreenHandler(int syncId, PlayerInventory playerInventory, ModBlockEntity blockEntity) {
        super(RISCJ_blockits.CONTROL_UNIT_SCREEN_HANDLER, syncId, blockEntity);

        checkSize(((Inventory) blockEntity), 1);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);

        this.addSlot(new Slot(inventory, 0, 8, 18));

        addPlayerInventorySlotsLarge(playerInventory);

        addListener(new ScreenHandlerListener() {           //listener for changes in the inventory
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                if (slotId == 0) {
                    ((ControlUnitBlockEntity) blockEntity).inventoryChanged();
                }
            }
            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                //do nothing
            }
        });
    }

    public ControlUnitScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId,playerInventory, (ModBlockEntity) playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
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

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public String getClusteringData() {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return "";
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return "";
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CONTROL_CLUSTERING)) {
                return ((IDataStringEntry) ((IDataContainer) ((IDataContainer) data).get(CONTROL_CLUSTERING)).get("missingRegisters")).getContent();
            }
        }
        return "";
    }

    /**
     * Stub for getting the Blocks needed/missing for the Architecture depending on the given key.
     * @return
     */
    public List[] getStructure(){
        List<String> listFound = new ArrayList<>();
        List<String> listMissing = new ArrayList<>();
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return null;
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return null;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CONTROL_CLUSTERING)) {
                IDataContainer clusteringData = (IDataContainer) ((IDataContainer) data).get(CONTROL_CLUSTERING);
                for (String s2 : clusteringData.getKeys()) {
                    switch (s2) {
                        //ToDo reformat ugly code
                        case CLUSTERING_MISSING_REGISTERS:
                            listMissing.addAll(List.of(((IDataStringEntry) clusteringData.get(s2)).getContent().split(" ")));
                            listMissing.remove("");
                            break;
                        case CLUSTERING_FOUND_REGISTERS:
                            listFound.addAll(List.of(((IDataStringEntry) clusteringData.get(s2)).getContent().split(" ")));
                            listFound.remove("");
                            break;
                        case CLUSTERING_FOUND_CONTROL_UNIT:
                            String found = ((IDataStringEntry) clusteringData.get(s2)).getContent();
                            if (found.equals("1")) {
                                listFound.add("ControlUnit");
                            } else {
                                listMissing.add("ControlUnit");
                            }
                            break;
                        case CLUSTERING_FOUND_ALU:
                            String foundALU = ((IDataStringEntry) clusteringData.get(s2)).getContent();
                            if (foundALU.equals("1")) {
                                listFound.add("ALU");
                            } else {
                                listMissing.add("ALU");
                            }
                            break;
                        case CLUSTERING_FOUND_MEMORY:
                            String foundMemory = ((IDataStringEntry) clusteringData.get(s2)).getContent();
                            if (foundMemory.equals("1")) {
                                listFound.add("Memory");
                            } else {
                                listMissing.add("Memory");
                            }
                            break;
                        case CLUSTERING_FOUND_CLOCK:
                            String foundClock = ((IDataStringEntry) clusteringData.get(s2)).getContent();
                            if (foundClock.equals("1")) {
                                listFound.add("Clock");
                            } else {
                                listMissing.add("Clock");
                            }
                            break;
                        default:
                            break;
                    }
                }
                System.out.println("ClusteringData: " + clusteringData);
            }
        }
        return new List[]{listMissing, listFound};
    }

    /**
     * Get the InstructionSet Type.
     * @return The name of the InstructionSet.
     */
    public String getInstructionSetType(){
        if(inventory.getStack(0).isEmpty() || !inventory.getStack(0).hasNbt() || !inventory.getStack(0).getNbt().contains(CONTROL_IST_ITEM)) {
            return "";
        }

        NbtElement nbt = inventory.getStack(0).getOrCreateNbt().get(CONTROL_IST_ITEM);

        if(nbt == null) {
            return "";
        }

        IDataElement instructionSetData = new NbtDataConverter(nbt).getData();

        InstructionSetModel instructionSet;
        try {
            instructionSet = InstructionSetBuilder.buildInstructionSetModel(((IDataStringEntry) instructionSetData).getContent());
        } catch (UnsupportedEncodingException e) {
            return "";
        }

        return instructionSet.getName();
    }

}
