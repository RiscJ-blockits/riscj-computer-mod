package edu.kit.riscjblockits.view.main.blocks.mod.programming;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import edu.kit.riscjblockits.view.main.items.instructionset.InstructionSetItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;

/**
 * This class provides functionality to sync the programming screen between client and server.
 * It will add all slots and buttons on the screen.
 * Provides functionality to handle button clicks on the screen.
 */
public class ProgrammingScreenHandler extends ModScreenHandler {

    /**
     * The id of the "assemble" button.
     */
    public static final int ASSEMBLE_BUTTON_ID = 0;

    /**
     * The block entity that created this screenHandler.
     */
    private final ProgrammingBlockEntity blockEntity;

    /**
     * The inventory of the programming block. Slot 0 holds the InstructionSet, Slot 1 the program and Slot 2 the result.
     */
    private final Inventory inventory;

    /**
     * The player that opened the screen.
     */
    private final PlayerEntity player;

    /**
     * Creates a new ProgrammingScreenHandler.
     * The client-side uses this constructor to create a new screenHandler.
     * @param syncId the sync id
     * @param playerInventory the player inventory
     * @param buf the packet buffer, additional information is to be loaded from
     */
    public ProgrammingScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory,
                new SimpleInventory(3),
                (ProgrammingBlockEntity) playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
        // load code from packet buffer
        blockEntity.setCode(buf.readString());
    }

    /**
     * Creates a new ProgrammingScreenHandler.
     * The server-side uses this constructor to create a new screenHandler.
     * @param syncId the sync id
     * @param playerInventory the player inventory
     * @param inventory the inventory of the programming block
     * @param blockEntity the block entity that created this screenHandler
     */
    public ProgrammingScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ProgrammingBlockEntity blockEntity) {
        super(RISCJ_blockits.PROGRAMMING_SCREEN_HANDLER, syncId, blockEntity);
        this.inventory = inventory;
        this.blockEntity = blockEntity;
        this.player = playerInventory.player;
        // add for GUI required Slots
        addInstructionSetSlot();
        addProgramSlots();
        addPlayerInventorySlotsLarge(playerInventory);
    }

    /**
     * Will handle button clicks on the screen.
     * A click on the "assemble" button will assemble the code in the programming block.
     * @param player the player that clicked the button
     * @param id the id of the button
     * @return true if the button click was handled, false otherwise
     */
    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id == ASSEMBLE_BUTTON_ID) {
            try {
                this.blockEntity.assemble();
                syncState();
                return true;
            } catch (AssemblyException e) {
                showError(e.getMessage());
            }
        }
        return false;
    }

    /**
     * Will show an error message on the screen.
     * @param message the message to show
     */
    private void showError(String message) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(message);
        ServerPlayNetworking.send((ServerPlayerEntity) player, NetworkingConstants.SHOW_ASSEMBLER_EXCEPTION, buf);
    }

    /**
     * Adds the slot for the program-Item to the screen.
     */
    private void addProgramSlots() {
        this.addSlot(new Slot(this.inventory, 1, 151, 40) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(RISCJ_blockits.PROGRAM_ITEM);
            }
        });
        this.addSlot(new Slot(this.inventory, 2, 151, 93) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });
    }

    /**
     * Adds the slot for the instruction set to the screen.
     */
    private void addInstructionSetSlot() {
        this.addSlot(new Slot(this.inventory, 0, 151, 18) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem().getClass() == InstructionSetItem.class;
            }
        });
    }

    /**
     * Getter for the code that should currently be displayed in the test editor on the screen.
     * @return a String with the code.
     */
    public String getCode() {
        return blockEntity.getCode();
    }

    /**
     * Get the Instructions offered by the instruction set.
     * @return ArrayList of String[] with the instructions
     */
    public List<String[]> getInstructions() {
        InstructionSetModel instructionSet = buildInstructionSetModel();
        if (instructionSet == null) {
            return new ArrayList<>();
        }
        return instructionSet.getPossibleInstructions();
    }

    /**
     * Called when a player attempts to quickly move an item.
     * @param player The player that wants to quickly move an item.
     * @param invSlot The slot that the player wants to quickly move to.
     * @return the item stack of the item that was moved
     */
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;   //ToDo duplicate code
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
     * Get the example code of the instruction set, from the InstructionSet-Item in the inventory.
     * @return The example code of the instruction set.
     */
    public String getExample() {
        if(inventory.getStack(0).isEmpty() || !inventory.getStack(0).hasNbt() || !inventory.getStack(0).getNbt().contains(CONTROL_IST_ITEM)) {
            return "";
        }
        String ist = inventory.getStack(0).getOrCreateNbt().get("riscj_blockits.instructionSet").asString();
        InstructionSetModel instructionSetModel;
        try {
            instructionSetModel = InstructionSetBuilder.buildInstructionSetModel(ist);
            return instructionSetModel.getExample();
        }catch (UnsupportedEncodingException | InstructionBuildException e) {
            return "";
        }
    }

    /**
     * Get the OpenAI API key from the instruction set.
     * @return An Api Key String.
     */
    public String getOpenAiKey() {
        InstructionSetModel instructionSetModel = buildInstructionSetModel();
        if (instructionSetModel == null) {
            return "";
        }
        return instructionSetModel.getApiKey();
    }

    private InstructionSetModel buildInstructionSetModel() {
        if (inventory.getStack(0).isEmpty() || !inventory.getStack(0).hasNbt()
            || !inventory.getStack(0).getNbt().contains(CONTROL_IST_ITEM)) {
            return null;
        }
        NbtElement nbt = inventory.getStack(0).getOrCreateNbt().get(CONTROL_IST_ITEM);
        if (nbt == null) {
            return null;
        }
        IDataElement instructionSetData = new NbtDataConverter(nbt).getData();
        try {
            return InstructionSetBuilder.buildInstructionSetModel(((IDataStringEntry) instructionSetData).getContent());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
