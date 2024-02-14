package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import edu.kit.riscjblockits.view.main.items.instructionset.InstructionSetItem;
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

import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_ALU;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_CLOCK;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_CONTROL_UNIT;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_MEMORY;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_MISSING_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_CLUSTERING;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_ITEM_PRESENT;
import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;

/**
 * This class represents a control unit screen handler from our mod in the game.
 * It is used to handle the interaction between the control unit screen and the entity.
 */
public class ControlUnitScreenHandler extends ModScreenHandler {

    /**
     * The inventory of the control unit entity.
     */
    private final Inventory inventory;

    /**
     * Is set to true after the screen is opened.
     */
    private boolean opened = false;

    /**
     * Constructor for the control unit screen handler.
     * @param syncId The sync id.
     * @param playerInventory The inventory of the player that opens the screen.
     * @param blockEntity The block entity of the control unit.
     */
    public ControlUnitScreenHandler(int syncId, PlayerInventory playerInventory, ModBlockEntity blockEntity) {
        super(RISCJ_blockits.CONTROL_UNIT_SCREEN_HANDLER, syncId, blockEntity);
        checkSize(((Inventory) blockEntity), 1);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.addSlot(new Slot(inventory, 0, 8, 18) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof InstructionSetItem;
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
                    ((ControlUnitBlockEntity) blockEntity).inventoryChanged();
                }
            }
            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                //do nothing
            }
        });
    }

    /**
     * Constructor for the control unit screen handler.
     * @param syncId The sync id.
     * @param playerInventory The inventory of the player that opens the screen.
     * @param buf The packet byte buffer. Must have a block position inside.
     */
    public ControlUnitScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, (ModBlockEntity) playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
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
     * Creates a List that contains all components of the cluster and all missing components specified in the instruction set.
     * @return The entries of the architecture list. List[0] contains all missing components, List[1] contains all found components.
     */
    public List<String>[] getStructure() {
        List<String> listFound = new ArrayList<>();
        List<String> listMissing = new ArrayList<>();
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return new List[]{listMissing, listFound};
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return new List[]{listMissing, listFound};
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CONTROL_CLUSTERING)) {
                IDataContainer clusteringData = (IDataContainer) ((IDataContainer) data).get(CONTROL_CLUSTERING);
                for (String s2 : clusteringData.getKeys()) {
                    switch (s2) {
                        //ToDo I am ugly code please reformat me
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
            } else if (s.equals(CONTROL_ITEM_PRESENT) && ( ((IDataStringEntry) ((IDataContainer) data).get(CONTROL_ITEM_PRESENT)).getContent().equals("false") )) {
                return new List[]{new ArrayList<>(), new ArrayList<>()};

            }
        }
        return new List[]{listMissing, listFound};
    }

    /**
     * @return The name of the InstructionSet or "" if the fetch fails.
     */
    public String getInstructionSetType() {
        if (inventory.getStack(0).isEmpty() || !inventory.getStack(0).hasNbt() || !inventory.getStack(0).getNbt().contains(CONTROL_IST_ITEM)) {
            return "";
        }
        NbtElement nbt = inventory.getStack(0).getOrCreateNbt().get(CONTROL_IST_ITEM);
        if (nbt == null) {
            return "";
        }
        IDataElement instructionSetData = new NbtDataConverter(nbt).getData();
        InstructionSetModel instructionSet;
        try {
            instructionSet = InstructionSetBuilder.buildInstructionSetModel(((IDataStringEntry) instructionSetData).getContent());
        } catch (UnsupportedEncodingException | InstructionBuildException e) {
            return "";
        }
        return instructionSet.getName();
    }

}
