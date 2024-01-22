package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class ControlUnitScreenHandler extends ScreenHandler {

    public ControlUnitScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(RISCJ_blockits.CONTROL_UNIT_SCREEN_HANDLER, syncId);
    }

    public ControlUnitScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(RISCJ_blockits.CONTROL_UNIT_SCREEN_HANDLER, syncId);



        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }
    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        //TODO copy over the standard quickmove code
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
