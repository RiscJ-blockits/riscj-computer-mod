package edu.kit.riscjblockits.view.main.blocks.mod.programming;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.items.instructionset.InstructionSetItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;


/**
 * This class provides functionality to sync the programming screen between client and server.
 * It will add all slots and buttons on the screen.
 * provides functionality to handle button clicks on the screen.
 */
public class ProgrammingScreenHandler extends ModScreenHandler {

    /**
     * The id of the "assemble" button.
     */
    private static final int ASSEMBLE_BUTTON_ID = 0;

    /**
     * The block entity, that created this screenHandler.
     */
    private ProgrammingBlockEntity blockEntity = null;

    /**
     * The inventory of the programming block.
     */
    private Inventory inventory;


    /**
     * Creates a new ProgrammingScreenHandler.
     * This constructor is used by the client-side to create a new screenHandler.
     * @param syncId the sync id
     * @param playerInventory the player inventory
     * @param buf the packet buffer, additional information is to be loaded from
     */
    public ProgrammingScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(RISCJ_blockits.PROGRAMMING_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(1);
        addInstructionSetSlot();
        addProgramSlot();
        this.addPlayerInventorySlots(playerInventory);
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
        this(syncId, playerInventory, (PacketByteBuf) null);
        this.inventory = inventory;
        this.blockEntity = blockEntity;
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
        // TODO: show error
    }

    /**
     * will add the slot for the program-Item to the screen.
     */
    private void addProgramSlot() {
        this.addSlot(new Slot(this.inventory, 0, 20, 20) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(RISCJ_blockits.PROGRAM_ITEM);
            }
        });
    }

    /**
     * will add the slot for the instruction set to the screen.
     */
    private void addInstructionSetSlot() {
        this.addSlot(new Slot(this.inventory, 0, 20, 20) {
            public boolean canInsert(ItemStack stack) {
                return stack.getItem().getClass() == InstructionSetItem.class;
            }
        });
    }
}
