package edu.kit.riscjblockits.view.main.blocks.programming;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ProgrammingScreenHandler extends ScreenHandler {

    private static final int ASSEMBLE_BUTTON_ID = 0;
    private ProgrammingBlockEntity blockEntity = null;
    private Inventory inventory;
    private PlayerInventory playerInventory;

    // ClientSide
    public ProgrammingScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(RISCJ_blockits.PROGRAMMING_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(1);
        this.playerInventory = playerInventory;
        addInstructionSetSlot();
        addProgramSlot();
        this.addPlayerInventorySlots(playerInventory);
    }

    // ServerSide
    public ProgrammingScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ProgrammingBlockEntity blockEntity) {
        this(syncId, playerInventory, (PacketByteBuf) null);
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        this.blockEntity = blockEntity;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }

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

    private void showError(String message) {
        // TODO: show error
    }

    private void addProgramSlot() {
        this.addSlot(new Slot(this.inventory, 0, 20, 20) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(RISCJ_blockits.PROGRAM_ITEM);
            }
        });
    }

    private void addInstructionSetSlot() {
        this.addSlot(new Slot(this.inventory, 0, 20, 20) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(RISCJ_blockits.INSTRUCTION_SET_ITEM);
            }
        });
    }

    private void addPlayerInventorySlots(PlayerInventory playerInventory) {
        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

    }
}
