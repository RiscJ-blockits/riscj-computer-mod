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
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
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
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_PROGRAMMING_CODE, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                NbtCompound nbt = buf.readNbt();
                BlockPos blockPos = buf.readBlockPos();
                assert nbt != null;
                String newCode = nbt.getString(PROGRAMMING_BLOCK_CODE);
                if (player.getServerWorld().getBlockEntity(blockPos) instanceof ProgrammingBlockEntity blockEntity) {
                    blockEntity.setCode(newCode);
                }
            });
        });
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
     * @param player the player that is opening the screen
     * @param buf    the packet buffer to write to
     */
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeString(code);
        markDirty();
    }

    /**
     * Will return the display name of the screen.
     *
     * @return the display name of the screen
     */
    @Override
    public Text getDisplayName() {
        return Text.of("Program Editor");
    }

    /**
     * Will create the screenHandler for the screen.
     * @param syncId the sync id of the screenHandler
     * @param playerInventory the player inventory
     * @param player the player
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
