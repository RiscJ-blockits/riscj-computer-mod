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
import net.minecraft.item.ItemStack;
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
public class MemoryBlockEntity extends ComputerBlockEntityWithInventory implements ExtendedScreenHandlerFactory {

    /**
     * The slot in the inventory where the program is stored.
     */
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

    /**
     * Called when the screen is opened.
     * We send the position to the screen.
     * @param player the player that is opening the screen
     * @param buf    the packet buffer to write the data to
     */
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    /**
     * Gets the headline of the memory screen.
     * @return The name on the memory screen.
     */
    @Override
    public Text getDisplayName() {
        return Text.literal("Memory");
    }

    /**
     * Creates a new MemoryScreenHandler for the memory block.
     * @param syncId The id of the screen.
     * @param playerInventory The inventory of the player that opened the screen.
     * @param player The player that opened the screen.
     * @return A new MemoryScreenHandler.
     */
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MemoryScreenHandler(syncId, playerInventory, this);
    }

    /**
     * This method is called when the inventory of the memory block is changed.
     * It updates the data of the controller with the new memory data.
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
                    return;         //if the program item has no data on it, we don't need to do anything
                }
                NbtDataConverter converter = new NbtDataConverter(programmNbt);
                memData.set(MEMORY_MEMORY, converter.getData());
                getController().setData(memData);
            }
        }
    }

    @Override
    public Text getGoggleText() {
        ItemStack program = getItems().get(0);
        Text programText;
        if (program.isEmpty()) {
            programText = Text.translatable("riscj_blockits.program_none");
        } else {
            programText = program.getName();
        }
        return Text.translatable("block.riscj_blockits.memory_block")
                .append("\n")
                .append(Text.translatable("riscj_blockits.program"))
                .append(": ")
                .append(programText);
    }
}
