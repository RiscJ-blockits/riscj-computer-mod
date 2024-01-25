package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
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
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a control unit entity from our mod in the game.
 * Every control unit has its own unique ControlUnitBlockEntity while it is loaded.
 */
public class ControlUnitBlockEntity extends ComputerBlockEntityWithInventory implements
    ExtendedScreenHandlerFactory {

    private static final int INSTRUCTION_SET_SLOT = 1;
    private DefaultedList<ComputerBlockController> connectedBlocks;
    private DefaultedList<ComputerBlockController> missingBlocks;

    /**
     * Creates a new ControlUnitBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public ControlUnitBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.CONTROL_UNIT_BLOCK_ENTITY, pos, state, INSTRUCTION_SET_SLOT);
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

    /**
     * When the Instruction Set changes, the controller needs to be notified.
     */
    @Override
    public void inventoryChanged() {
        if (getController() != null) {             //only on the server
            System.out.println("IST Item changed");
            if (getItems().get(0).getCount() == 0) {        //Item is removed when there are zero 'air' items
                Data cuData = new Data();
                cuData.set("istModel", null);
                getController().setData(cuData);
            } else {
                NbtCompound istNbt = getItems().get(0).getNbt();
                Data cuData = new Data();
                NbtDataConverter converter = new NbtDataConverter(istNbt);
                cuData.set("istModel", converter.getData());
                getController().setData(cuData);
            }
        }
    }

    /**
     * Gets called every tick.
     * Used to update ui elements.
     */
    @Override
    public void updateUI() {
        if (getItems().get(0).getCount() == 0 && getModel() != null
                && ((IDataStringEntry) ((IDataContainer) getModel().getData()).get("istModelPresent")).getContent().equals("false")) {
            ItemScatterer.spawn(world, pos, (this));        //drop the IstItem if it is rejected
        }
    }

}
