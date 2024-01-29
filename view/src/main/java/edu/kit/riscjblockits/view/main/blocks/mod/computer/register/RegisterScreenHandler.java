package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;

public class RegisterScreenHandler extends ModScreenHandler {

    public RegisterScreenHandler(int syncId, PlayerInventory inventory, ModBlockEntity blockEntity) {
        super(RISCJ_blockits.REGISTER_SCREEN_HANDLER, syncId, blockEntity);
        addPlayerInventorySlots(inventory);
    }

    public RegisterScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, (ModBlockEntity) inventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public String getRegisterValue() {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return "";
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return "";
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(REGISTER_VALUE)) {
                return ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            }
        }
        return "";
    }

    public List<String> getRegisters(String key) {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return new ArrayList<String>();
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return new ArrayList<String>();
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(REGISTER_REGISTERS)) {
                IDataElement regData = ((IDataContainer) data).get(REGISTER_REGISTERS);
                for (String s2 : ((IDataContainer) regData).getKeys()) {
                    if (s2.equals(key)) {
                        List<String> registers = new ArrayList<>(List.of(
                            ((IDataStringEntry) ((IDataContainer) regData).get(s2)).getContent().split(" ")));
                        int i = 0;
                        while( i < registers.size()) {
                            if (registers.get(i).equals(RegisterModel.DEFAULT_REGISTER)) {
                                registers.remove(i);
                            }else {
                                i++;
                            }
                        }
                        return registers;
                    }
                }
            }
        }
        return new ArrayList<String>();
    }

}
