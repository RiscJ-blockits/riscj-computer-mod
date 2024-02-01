package edu.kit.riscjblockits.view.main.blocks.mod.computer.memory;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntityWithInventory;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_MEMORY;

/**
 * This class represents a memory entity from our mod in the game.
 * Every memory block has its own unique MemoryBlockEntity while it is loaded.
 */
public class MemoryBlockEntity extends ComputerBlockEntityWithInventory implements ExtendedScreenHandlerFactory{

    private static final int PROGRAM_SLOT = 1;

    /**
     * Creates a new MemoryBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public MemoryBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.MEMORY_BLOCK_ENTITY, pos, state, PROGRAM_SLOT);
    }

    /**
     * Every entity needs its own controller.
     * @return An MemoryController bound to this entity.
     */
    @Override
    protected ComputerBlockController createController() {
        return new MemoryController(this);
    }


    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    //ToDo
    @Override
    public Text getDisplayName() {
        return Text.literal("Memory");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MemoryScreenHandler(syncId, playerInventory, this);
    }

    /**
     * When the Instruction Set changes, the controller needs to be notified.
     */
    @Override
    public void inventoryChanged() {
        if (getController() != null) {             //only on the server
            if (getItems().get(0).getCount() == 0) {        //Item is removed when there are zero 'air' items
                Data cuData = new Data();
                cuData.set(MEMORY_MEMORY, null);
                getController().setData(cuData);
            } else {
                NbtCompound programmNbt = getItems().get(0).getNbt();
                Data memData = new Data();
                if (programmNbt == null) {
                    return;         //if the programm item has no data on it, we don't need to do anything
                }
                NbtDataConverter converter = new NbtDataConverter(programmNbt);
                memData.set(MEMORY_MEMORY, converter.getData());
                getController().setData(memData);
            }
        }
    }

}
