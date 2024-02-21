package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.controller.blocks.io.TerminalInputController;
import edu.kit.riscjblockits.controller.blocks.io.TerminalModeController;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlockEntity;
import edu.kit.riscjblockits.view.main.data.DataNbtConverter;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
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
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_INPUT;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_IN_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_MODE_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_OUT_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMNAL_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;

/**
 * This class represents a block entity for a terminal block in Minecraft.
 * It extends the {@link ComputerBlockEntity} class and implements the {@link ExtendedScreenHandlerFactory} interface.
 * It is responsible for managing the display of text on the screen of the block.
 */
public class TerminalBlockEntity extends RegisterBlockEntity implements ExtendedScreenHandlerFactory  {

    /**
     * The string that should be displayed on the screen. Is only used on the client.
     */
    private String persistentText;             //only in the client
    //the normal register is the mode register
    private TerminalInputController inputController;
    private TerminalModeController modeController;
    private RegisterController outputController;

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
                String text = buf.readString();
                server.execute(() -> {
                    BlockEntity be = player.getWorld().getBlockEntity(blockPos);
                    assert be != null;
                    ((TerminalBlockEntity) be).addInput(text);
                });
            });
    }

    @Override
    protected ComputerBlockController createController() {
        inputController = new TerminalInputController(this);
        outputController = new RegisterController(this);
        modeController = new TerminalModeController(this, inputController, outputController);
        return modeController;
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
     * Getter for the Terminal output text.
     * @return The string that should be displayed on the screen.
     */
    public String getDisplayedString() {
        if (this.persistentText == null) {
            return "";
        }
        return this.persistentText;
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

    private void addInput(String s) {
        inputController.addInput(s);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (getModel() != null) {                       //we are in the server, so we send the data in the model
            nbt.put(MOD_DATA, new DataNbtConverter(collectData()).getNbtElement());
        }
        markDirty();
    }

    private IDataElement collectData() {
        IDataContainer collectData = new Data();
        IDataContainer outData = (IDataContainer) outputController.getModel().getData();
        IDataContainer inData = (IDataContainer) inputController.getModel().getData();
        IDataContainer modeData = (IDataContainer) modeController.getModel().getData();
        collectData.set(REGISTER_TERMNAL_MODE, modeData.get(REGISTER_VALUE));
        collectData.set(REGISTER_TERMINAL_INPUT, inData.get(REGISTER_VALUE));
        collectData.set(REGISTER_TERMINAL_IN_TYPE, inData.get(REGISTER_TYPE));
        collectData.set(REGISTER_TERMINAL_OUT_TYPE, outData.get(REGISTER_TYPE));
        collectData.set(REGISTER_TERMINAL_MODE_TYPE, modeData.get(REGISTER_TYPE));
        collectData.set(REGISTER_VALUE, outData.get(REGISTER_VALUE));
        collectData.set(REGISTER_WORD_LENGTH, outData.get(REGISTER_WORD_LENGTH));
        collectData.set(REGISTER_REGISTERS, outData.get(REGISTER_REGISTERS));
        return collectData;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (world != null && world.isClient) {         //we are in the client
            String newValue = getRegisterValue(nbt);
            persistentText = persistentText + translateHexToAscii(newValue);
        }
    }

    @Override
    public void onBroken() {
        super.onBroken();
        assert world != null;
        if (!world.isClient && getController() != null) {       //break additional controllers
            inputController.onBroken();
            outputController.onBroken();
        }
    }

    @Override
    public Text getGoggleText() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        String typeIn = "";
        String typeOut = "";
        String typeMode = "";
        String value = "";
        if (nbt.contains(MOD_DATA)) {
            nbt = nbt.getCompound(MOD_DATA);
        }
        if (nbt.contains(REGISTER_VALUE)) {
            value = nbt.getString(REGISTER_VALUE);
        }
        if (nbt.contains(REGISTER_TERMINAL_IN_TYPE)) {
            typeIn = nbt.getString(REGISTER_TERMINAL_IN_TYPE);
        }
        if (nbt.contains(REGISTER_TERMINAL_OUT_TYPE)) {
            typeOut = nbt.getString(REGISTER_TERMINAL_OUT_TYPE);
        }
        if (nbt.contains(REGISTER_TERMINAL_MODE_TYPE)) {
            typeMode = nbt.getString(REGISTER_TERMINAL_MODE_TYPE);
        }

        return Text.translatable("block.riscj_blockits.text_output_block")
            .append("\n")
            .append(Text.translatable("riscj_blockits.register_type"))
            .append(": " + typeIn + "\n")
            .append(Text.translatable("riscj_blockits.register_type"))
            .append(": " + typeOut + "\n")
            .append(Text.translatable("riscj_blockits.register_type"))
            .append(": " + typeMode + "\n")
            .append(Text.translatable("riscj_blockits.register_value"))
            .append(": " + value);
    }

    @Override
    public void syncToClient() {
        if (world == null || world.isClient || getModel() == null) return;
        if (getModel().hasUnqueriedStateChange() || ((IViewQueryableBlockModel) inputController.getModel()).hasUnqueriedStateChange()
            || ((IViewQueryableBlockModel) outputController.getModel()).hasUnqueriedStateChange()) {
            if (world.getPlayers().isEmpty()) {
                return;       //we are too early in the loading process
            }
            NbtCompound nbt = new NbtCompound();
            writeNbt(nbt);
            world.getPlayers().forEach(
                player -> {
                    // reset reader Index, to make sure multiple players can receive the same packet
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeBlockPos(pos);
                    buf.writeNbt(nbt);
                    ServerPlayNetworking.send((ServerPlayerEntity) player,
                        NetworkingConstants.SYNC_BLOCK_ENTITY_DATA, buf);});
            getModel().onStateQuery();
            ((IViewQueryableBlockModel) inputController.getModel()).onStateQuery();
            ((IViewQueryableBlockModel) outputController.getModel()).onStateQuery();
        }
    }

}
