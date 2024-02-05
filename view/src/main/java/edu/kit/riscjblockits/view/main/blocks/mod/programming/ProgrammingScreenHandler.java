package edu.kit.riscjblockits.view.main.blocks.mod.programming;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitBlockEntity;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import edu.kit.riscjblockits.view.main.items.instructionset.InstructionSetItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;


/**
 * This class provides functionality to sync the programming screen between client and server.
 * It will add all slots and buttons on the screen.
 * provides functionality to handle button clicks on the screen.
 */
public class ProgrammingScreenHandler extends ModScreenHandler {

    /**
     * The id of the "assemble" button.
     */
    public static final int ASSEMBLE_BUTTON_ID = 0;

    /**
     * The block entity, that created this screenHandler.
     */
    private final ProgrammingBlockEntity blockEntity;

    /**
     * The inventory of the programming block.
     */
    private final Inventory inventory;


    /**
     * Creates a new ProgrammingScreenHandler.
     * This constructor is used by the client-side to create a new screenHandler.
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
     * This constructor is used by the server-side to create a new screenHandler.
     * @param syncId the sync id
     * @param playerInventory the player inventory
     * @param inventory the inventory of the programming block
     * @param blockEntity the block entity that created this screenHandler
     */
    public ProgrammingScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ProgrammingBlockEntity blockEntity) {
        super(RISCJ_blockits.PROGRAMMING_SCREEN_HANDLER,syncId, blockEntity);
        this.inventory = inventory;
        this.blockEntity = blockEntity;
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
                System.out.println("Assembler-Error: " + e.getMessage());
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
        // TODO: show error
    }

    /**
     * will add the slot for the program-Item to the screen.
     */
    private void addProgramSlots() {
        this.addSlot(new Slot(this.inventory, 1, 151, 40) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(RISCJ_blockits.PROGRAM_ITEM);
            }
        });
        this.addSlot(new Slot(this.inventory, 2, 151, 93) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });
    }

    /**
     * will add the slot for the instruction set to the screen.
     */
    private void addInstructionSetSlot() {
        this.addSlot(new Slot(this.inventory, 0, 151, 18) {
            public boolean canInsert(ItemStack stack) {
                return stack.getItem().getClass() == InstructionSetItem.class;
            }
        });
    }

    public String getCode() {
        return blockEntity.getCode();
    }

    /**
     * Get the Instructions offered by the instruction set.
     * @return ArrayList of String[] with the instructions
     */
    public List<String[]> getInstructions() {
        if(inventory.getStack(0).isEmpty() || !inventory.getStack(0).hasNbt() || !inventory.getStack(0).getNbt().contains(CONTROL_IST_ITEM)) {
            return new ArrayList<>();
        }

        NbtElement nbt = inventory.getStack(0).getOrCreateNbt().get(CONTROL_IST_ITEM);

        if(nbt == null) {
            return new ArrayList<>();
        }

        IDataElement instructionSetData = new NbtDataConverter(nbt).getData();

        InstructionSetModel instructionSet;
        try {
            instructionSet = InstructionSetBuilder.buildInstructionSetModel(((IDataStringEntry) instructionSetData).getContent());
        } catch (UnsupportedEncodingException e) {
            return new ArrayList<>();
        }

        return instructionSet.getPossibleInstructions();
    }
}
