package edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import static edu.kit.riscjblockits.model.blocks.ClockMode.STEP;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_SPEED;
import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;

public class SystemClockScreenHandler extends ModScreenHandler {

    public SystemClockScreenHandler(int syncId, PlayerInventory playerInventory, ModBlockEntity blockEntity) {
        super(RISCJ_blockits.SYSTEM_CLOCK_SCREEN_HANDLER, syncId, blockEntity);

        addPlayerInventorySlots(playerInventory);
    }

    public SystemClockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, (ModBlockEntity) playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

}
