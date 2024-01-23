package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ImplementedInventory;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntityWithInventory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a control unit entity from our mod in the game.
 * Every control unit has its own unique ControlUnitBlockEntity while it is loaded.
 */
public class ControlUnitBlockEntity extends ComputerBlockEntityWithInventory implements ImplementedInventory,
    ExtendedScreenHandlerFactory {

    private static final int INSTRUCTION_SET_SLOT = 0;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ComputerBlockController> connectedBlocks;
    private DefaultedList<ComputerBlockController> missingBlocks;

    /**
     * Creates a new ControlUnitBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public ControlUnitBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.CONTROL_UNIT_BLOCK_ENTITY, pos, state, 1);
    }

    /**
     * Every entity needs its own controller.
     * @return An ControlUnitController bound to this entity.
     */
    @Override
    protected ComputerBlockController createController() {
        return new ControlUnitController(this);
    }

    /**
     * ToDo
     * @return
     */
    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    /**
     * ToDo
     * @param player
     * @return
     */
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    /**
     * ToDo
     * @return
     */
    @Override
    public Text getDisplayName() {
        return Text.literal("Control Unit");
    }

    /**
     * ToDo
     * @param syncId
     * @param playerInventory
     * @param player
     * @return
     */
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ControlUnitScreenHandler(syncId, playerInventory, this);
    }

}
