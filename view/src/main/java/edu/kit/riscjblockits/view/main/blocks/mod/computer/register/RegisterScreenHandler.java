package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.alu.AluBlockEntity;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_ALU_REGS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_MISSING;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;

/**
 * The {@link ModScreenHandler} for the {@link RegisterBlock} and a {@link  edu.kit.riscjblockits.view.client.screens.handled.RegisterScreen}.
 */
public class RegisterScreenHandler extends ModScreenHandler {

    private ModBlockEntity blockEntity;

    /**
     * Creates a new {@link RegisterScreenHandler} for the given {@link ModBlockEntity}.
     * @param syncId the syncId
     * @param inventory the player inventory
     * @param blockEntity the block entity opening the screen.
     */
    public RegisterScreenHandler(int syncId, PlayerInventory inventory, ModBlockEntity blockEntity) {
        super(RISCJ_blockits.REGISTER_SCREEN_HANDLER, syncId, blockEntity);
        this.blockEntity = blockEntity;
        addPlayerInventorySlots(inventory);
    }

    /**
     * Creates a new {@link RegisterScreenHandler}.
     * @param syncId the syncId
     * @param inventory the player inventory
     * @param buf the buffer with the block position
     */
    public RegisterScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, (ModBlockEntity) inventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    /**
     * Creates a new {@link RegisterScreenHandler}.
     * Used by subclasses who have their own type.
     * @param type the type of the screen handler
     * @param syncId the syncId
     * @param blockEntity the block entity opening the screen.
     */
    protected RegisterScreenHandler(ScreenHandlerType<?> type, int syncId, ModBlockEntity blockEntity) {
        super(type, syncId, blockEntity);
    }

    /**
     * Getter for the value of the currently opened Register from the model.
     * @return The value of the currently opened Register from the model. As a hex string.
     */
    public String getRegisterValue() {
        IDataElement data = getRegisterData();
        if (data == null) {
            return "";
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(REGISTER_VALUE)) {
                return ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            }
        }
        return "";
    }

    /**
     * Getter for the register type of the currently opened RegisterBlock.
     * @return the register Type of the currently opened RegisterBlock.
     */
    public String getCurrentRegister(){
        IDataElement data = getRegisterData();
        if (data == null) {
            return "";
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(REGISTER_TYPE)) {
                return ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            }
        }
        return "";
    }

    /**
     * Getter for the registers inside the cluster.
     * @param key can be {@link edu.kit.riscjblockits.model.data.DataConstants#REGISTER_MISSING} or {@link edu.kit.riscjblockits.model.data.DataConstants#REGISTER_FOUND}.
     * @return All registers inside the cluster matching the given key.
     */
    public List<String> getRegisters(String key) {
        IDataElement data = getRegisterData();
        if (data == null) {
            return new ArrayList<>();
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (!s.equals(REGISTER_REGISTERS)) {
                continue;
            }
            IDataElement regData = ((IDataContainer) data).get(REGISTER_REGISTERS);
            for (String s2 : ((IDataContainer) regData).getKeys()) {
                if (!s2.equals(key)) {
                    continue;
                }
                List<String> registers = new ArrayList<>(List.of(((IDataStringEntry) ((IDataContainer) regData).get(s2)).getContent().split(" ")));
                int i = 0;
                while( i < registers.size()) {
                    if (registers.get(i).equals(RegisterModel.UNASSIGNED_REGISTER) || registers.get(i).isEmpty()) {
                        registers.remove(i);
                    }else {
                        i++;
                    }
                }
                if (key.equals(REGISTER_MISSING)) {
                    registers = removeAluRegs(registers);
                }
                return registers;
            }
        }
        return new ArrayList<>();
    }

    private List<String> removeAluRegs(List<String> registers) {
        if (searchAlu()) {
            return registers;
        }
        IDataElement data = getRegisterData();
        if (data == null) {
            return registers;
        }
        String aluRegs = "";
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(REGISTER_REGISTERS)) {
                aluRegs = ((IDataStringEntry) ((IDataContainer) ((IDataContainer) data).get(s)).get(REGISTER_ALU_REGS)).getContent();
            }
        }
        registers.removeAll(List.of(aluRegs.split(" ")));
        return registers;
    }

    private IDataElement getRegisterData() {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return null;
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return null;
        }
        return data;
    }

    private boolean searchAlu() {
        if (blockEntity == null) {
            return false;
        }
        List<BlockEntity> blockEntities = new ArrayList<>();
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        assert world != null; //the screen gets only opened if the world is not null
        blockEntities.add(world.getBlockEntity(pos.down()));
        blockEntities.add(world.getBlockEntity(pos.up()));
        blockEntities.add(world.getBlockEntity(pos.south()));
        blockEntities.add(world.getBlockEntity(pos.north()));
        blockEntities.add(world.getBlockEntity(pos.east()));
        blockEntities.add(world.getBlockEntity(pos.west()));
        for (BlockEntity entity:blockEntities) {
            if (entity instanceof ComputerBlockEntity computerEntity
                && computerEntity instanceof AluBlockEntity) {
                return true;
            }
        }
        return false;
    }



}
