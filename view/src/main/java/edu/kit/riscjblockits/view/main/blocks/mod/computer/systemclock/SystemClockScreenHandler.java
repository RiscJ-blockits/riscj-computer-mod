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

    public int getSystemClockSpeed() {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return 0;
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return 0;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CLOCK_SPEED)) {
                return Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent());
            }
        }
        return 0;
    }

    public String getSystemClockMode() {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return STEP.toString();
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return STEP.toString();
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CLOCK_MODE)) {
                return ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            }
        }
        return STEP.toString();
    }

}
