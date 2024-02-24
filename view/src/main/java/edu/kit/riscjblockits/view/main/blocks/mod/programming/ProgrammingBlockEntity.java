package edu.kit.riscjblockits.view.main.blocks.mod.programming;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableController;
import edu.kit.riscjblockits.controller.blocks.ProgrammingController;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ImplementedInventory;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntityWithInventory;
import edu.kit.riscjblockits.view.main.data.DataNbtConverter;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_PROGRAMM_ITEM;
import static edu.kit.riscjblockits.model.data.DataConstants.PROGRAMMING_BLOCK_CODE;

/**
 * This class represents a programming block entity from our mod in the game.
 * Every programming block has its own unique ProgrammingBlockEntity while it is loaded.
 */
public class ProgrammingBlockEntity extends ModBlockEntityWithInventory implements ExtendedScreenHandlerFactory,
        ImplementedInventory {

    /**
     * The size of the chunks that the code is split into during transport.
     */
    public static final int CHUNK_SIZE = 32000;

    /**
     * The code that is currently in the programming block.
     * Might not be up-to-date with the code in the client's programming screen.
     */
    private String code = "";

    /**
     * Creates a new ProgrammingBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public ProgrammingBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.PROGRAMMING_BLOCK_ENTITY, pos, state, 3);
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_PROGRAMMING_CODE_C2S,
            (server, player, handler, buf, responseSender) -> server.execute(() -> {
                NbtCompound nbt = buf.readNbt();
                BlockPos blockPos = buf.readBlockPos();
                assert nbt != null;
                NbtCompound chunkContainer = nbt.getCompound(PROGRAMMING_BLOCK_CODE);
                int chunkIndex = chunkContainer.getInt("chunkIndex");
                String chunkCode = chunkContainer.getString("chunkData");
                BlockEntity be = player.getServerWorld().getBlockEntity(blockPos);
                if (be instanceof ProgrammingBlockEntity blockEntity) {
                    if (chunkIndex == 0) {
                        blockEntity.setCode("");
                    }
                    blockEntity.setCode(blockEntity.getCode() + chunkCode);
                }
                if (chunkCode.length() < CHUNK_SIZE) {
                    return;
                }
                responseSender.sendPacket(NetworkingConstants.SYNC_PROGRAMMING_CODE_CONFIRMATION_C2S, new PacketByteBuf(Unpooled.buffer())
                        .writeInt(chunkIndex + 1));
            }));
        ServerPlayNetworking.unregisterGlobalReceiver(NetworkingConstants.SYNC_PROGRAMMING_CODE_CONFIRMATION_S2C);
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_PROGRAMMING_CODE_CONFIRMATION_S2C,
                (server, player, handler, buf, responseSender) -> server.execute(() -> {
                    int requestedChunk = buf.readInt();
                    BlockPos blockPos = buf.readBlockPos();
                    BlockEntity be = player.getServerWorld().getBlockEntity(blockPos);
                    if (be instanceof ProgrammingBlockEntity blockEntity) {
                        if (requestedChunk * CHUNK_SIZE > blockEntity.getCode().length()) {
                            return;
                        }
                        blockEntity.sendChunk(player, requestedChunk, blockEntity.getCode());
                    }
                }));
    }

    /**
     * Every entity needs its own controller.
     * @return An ProgrammingController bound to this entity.
     */
    @Override
    protected IUserInputReceivableController createController() {
        return new ProgrammingController();
    }

    /**
     * Will write the data needed to open the screen to the packet buffer.
     *
     * @param p the p that is opening the screen
     * @param openBuf    the packet buffer to write to
     */
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity p, PacketByteBuf openBuf) {
        openBuf.writeBlockPos(pos);

        markDirty();
    }

    private void sendChunk(ServerPlayerEntity player, int chunkIndex, String text) {
        NbtCompound nbt = new NbtCompound();

        int start = chunkIndex * CHUNK_SIZE;
        int end = Math.min((chunkIndex + 1) * CHUNK_SIZE, text.length());
        // cant send as there is no more text
        if (start >= end || start >= text.length()) {
            return;
        }
        nbt.putInt("chunkIndex", chunkIndex);
        nbt.putString("chunkData", text.substring(start, end));


        NbtCompound container = new NbtCompound();
        container.put(PROGRAMMING_BLOCK_CODE, nbt);
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeNbt(container);
        packet.writeBlockPos(getPos());
        ServerPlayNetworking.send(player, NetworkingConstants.SYNC_PROGRAMMING_CODE_S2C, packet);
    }

    /**
     * Will return the display name of the screen.
     *
     * @return the display name of the screen
     */
    @Override
    public Text getDisplayName() {
        return Text.translatable("block.riscj_blockits.programming_block");
    }

    /**
     * Will create the screenHandler for the screen.
     * @param syncId the sync id of the screenHandler
     * @param playerInventory the p inventory
     * @param player the p
     * @return the screenHandler
     */
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ProgrammingScreenHandler(syncId, playerInventory, this,this);
    }

    /**
     * Will assemble the code in the programming block.
     * Code will have to be synced before calling this method.
     *
     * @throws AssemblyException if the code can't be assembled
     */
    public void assemble() throws AssemblyException {
        if (code == null || code.isEmpty() || world == null || world.isClient()) {
            return;
        }
        ItemStack instructionSetStack = getStack(0);
        if (instructionSetStack.isEmpty()) {
            return;
        }

        ItemStack memoryStack = getStack(1);
        if (memoryStack.isEmpty()) {
            return;
        }
        // cant assemble if output slot is not empty
        ItemStack outputStack = getStack(2);
        if (!outputStack.isEmpty()) {
            return;
        }
        NbtCompound nbt = instructionSetStack.getOrCreateNbt();
        IDataElement instructionSetData = new NbtDataConverter(nbt.get(CONTROL_IST_ITEM)).getData();
        IDataElement memoryData = ((ProgrammingController) getController()).assemble(code, instructionSetData);
        memoryStack.setSubNbt(MEMORY_PROGRAMM_ITEM, new DataNbtConverter(memoryData).getNbtElement());
        setStack(2, memoryStack);
        setStack(1, ItemStack.EMPTY);
    }

    /**
     * Will set the code of the programming block.
     *
     * @param code the code to be set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Will return the code of the programming block.
     *
     * @return the code of the programming block
     */
    public String getCode() {
        return code;
    }

    /**
     * Writes the inputted code to the nbt.
     * Calls super.writeNbt(nbt) to write the other data to the nbt.
     * @param nbt the nbt compound to write to.
     */
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString(PROGRAMMING_BLOCK_CODE, code);
    }

    /**
     * Reads previously written code from the nbt.
     * Calls super.readNbt(nbt) to read the other data from the nbt.
     * @param nbt The nbt compound to read from.
     */
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        code = nbt.getString(PROGRAMMING_BLOCK_CODE);
    }

}
