package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;

/**
 * This class represents a block entity for a terminal block in Minecraft.
 * It extends the {@link ComputerBlockEntity} class and implements the {@link ExtendedScreenHandlerFactory} interface.
 * It is responsible for managing the display of text on the screen of the block.
 */
public class TerminalBlockEntity extends ComputerBlockEntity implements ExtendedScreenHandlerFactory  {

    /**
     * The string that should be displayed on the screen. Is only used on the client.
     */
    private String persistentText;             //only in the client

    /**
     * The constructor of the block entity.
     * @param pos the position of the block entity in the minecraft world.
     * @param state the state of the block entity.
     */
    public TerminalBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.TEXT_OUTPUT_BLOCK_ENTITY, pos, state);
        persistentText = "";
        //Sync the input value to the model
        ServerPlayNetworking.registerGlobalReceiver(
            NetworkingConstants.SYNC_TERMINAL_INPUT, (server, player, handler, buf, responseSender) -> {
                BlockPos blockPos = buf.readBlockPos();
                String value = buf.readString();
                server.execute(() -> {
                    BlockEntity be = player.getWorld().getBlockEntity(blockPos);
                    NbtCompound nbt = new NbtCompound();
                    assert be != null;
                    ((TerminalBlockEntity) be).writeNbt(nbt);
                    NbtCompound subNbt = (NbtCompound) nbt.get(MOD_DATA);
                    assert subNbt != null;
                    subNbt.putString(REGISTER_VALUE, value);
                    be.readNbt(nbt);
                });
            });
    }

    @Override
    protected ComputerBlockController createController() {
        return new RegisterController(this);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.riscj_blockits.text_output_block");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TerminalScreenHandler(syncId, playerInventory, this);
    }

    /**
     * @return The string that should be displayed on the screen.
     */
    public String getDisplayedString() {
        if (this.persistentText == null) {
            return "";
        }
        return this.persistentText;
    }

    /**
     * Is used here to read new data from the model and update the persistentText.
     * @param nbt The nbt data that should be read from.
     */
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (world != null && world.isClient) {         //we are in the client
            String newValue = getRegisterValue(nbt);
            persistentText = persistentText + translateHexToAscii(newValue);
        }
    }

    /**
     * Translates a hex string to an ascii string and removes leading zeros.
     * @param hexStr The hex string that should be translated.
     * @return The translated ascii string.
     */
    public String translateHexToAscii(String hexStr) {
        hexStr = hexStr.replaceFirst("^0+", ""); // remove leading zeros
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str;
            if(i + 2 <= hexStr.length()){
                str = hexStr.substring(i, i + 2);
            } else {
                str = hexStr.substring(i);
            }
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    /**
     * Reads the register value from register block nbt data.
     * @param nbt The nbt data that should be read from.
     * @return The register value. A hex string.
     */
    private String getRegisterValue(NbtCompound nbt) {
        String value = "";
        if (!nbt.contains(MOD_DATA)) {
            return value;
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return value;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(REGISTER_VALUE)) {
                value = ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            }
        }
        return value;
    }

}
