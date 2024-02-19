package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

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
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;

import java.io.UnsupportedEncodingException;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;

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
     * Getter for the instruction set type.
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

}
