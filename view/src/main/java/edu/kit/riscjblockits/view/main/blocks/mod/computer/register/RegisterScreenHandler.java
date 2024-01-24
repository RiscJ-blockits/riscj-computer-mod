package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;

public class RegisterScreenHandler extends ModScreenHandler {

    RegisterBlockEntity blockEntity;

    public RegisterScreenHandler(int syncId, PlayerInventory inventory, BlockEntity blockEntity) {
        super(RISCJ_blockits.REGISTER_SCREEN_HANDLER, syncId);
        this.blockEntity = (RegisterBlockEntity) blockEntity;
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        addPlayerInventorySlots(inventory);
    }

    public RegisterScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }
    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
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

    public String getRegisterValue() {
        NbtCompound nbt = blockEntity.createNbt();
        blockEntity.writeNbt(nbt);
        if (!nbt.contains("modData")) {
            return "";
        }
        IDataElement data = new NbtDataConverter(nbt.get("modData")).getData();
        if (!data.isContainer()) {
            return "";
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals("value")) {
                return ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            }
        }
        return "";
    }

}
