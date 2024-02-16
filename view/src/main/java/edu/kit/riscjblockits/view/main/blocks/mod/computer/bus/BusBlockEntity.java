package edu.kit.riscjblockits.view.main.blocks.mod.computer.bus;

import edu.kit.riscjblockits.controller.blocks.BusController;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.EntityType;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.BUS_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;

/**
 * This class represents a bus entity from our mod in the game.
 * Every bus has its own unique BusBlockEntity while it is loaded.
 */
public class BusBlockEntity extends ComputerBlockEntity {

    /**
     * Creates a new BusBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public BusBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.BUS_BLOCK_ENTITY, pos, state);
        setType(EntityType.CONNECTABLE);
    }

    /**
     * Every entity needs its own controller.
     * @return An BusController bound to this entity.
     */
    @Override
    protected ComputerBlockController createController() {
        return new BusController(this);
    }

    /**
     * This method updates the block state of the bus.
     */
    public void updateBlockState() {
        assert world != null;   //when states are updated, the world is already loaded
        if (world.isClient || getController() == null || world.getBlockState(pos) == null) {
            return;
        }
        List<BlockPosition> neighbours = ((BusController) getController()).getBusSystemNeighbors();
        if (neighbours == null) {
            return;
        }
        BlockState state = world.getBlockState(pos).with(BusBlock.NORTH, getSideState(neighbours, pos.north()))
                .with(BusBlock.EAST, getSideState(neighbours, pos.east()))
                .with(BusBlock.SOUTH, getSideState(neighbours, pos.south()))
                .with(BusBlock.WEST, getSideState(neighbours, pos.west()))
                .with(BusBlock.UP, getSideState(neighbours, pos.up()))
                .with(BusBlock.DOWN, getSideState(neighbours, pos.down()));
        world.setBlockState(pos, state);
    }

    /**
     * Method to get the state of a side of the bus.
     * @param neighbours The list of neighbors of the bus.
     * @param pos The position of the NeighbourBlock at the side of the bus.
     * @return The state of the side.
     */
    private BusBlock.Side getSideState(List<BlockPosition> neighbours, BlockPos pos) {
        BusBlock.Side side = BusBlock.Side.NONE;
        if (listContainsPos(neighbours, pos)) {
            if (this.isActive() && ((ComputerBlockEntity) world.getBlockEntity(pos)).isActive()) {
                side = BusBlock.Side.ACTIVE;
            } else {
                side = BusBlock.Side.PRESENT;
            }
        }
        return side;
    }

    /**
     * This method checks if a list of BlockPositions contains a specific position.
     * @param pos1 The list of BlockPositions.
     * @param pos2 The position that should be checked.
     * @return True if the list contains the position, false otherwise.
     */
    private boolean listContainsPos(List<BlockPosition> pos1, BlockPos pos2) {
        for (BlockPosition pos : pos1) {
            if (pos.getX() == pos2.getX() && pos.getY() == pos2.getY() && pos.getZ() == pos2.getZ()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Text getGoggleText() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        Text busData = Text.translatable("riscj_blockits.no_bus_data");

        if (nbt.contains(MOD_DATA) && (nbt.getCompound(MOD_DATA).contains(BUS_DATA))) {
            busData = Text.of(nbt.getCompound(MOD_DATA).getString(BUS_DATA));
        }

        return Text.translatable("block.riscj_blockits.bus_block")
                .append("\n")
                .append(Text.translatable("block.riscj_blockits.bus_data"))
                .append(": ")
                .append(busData);
    }

    /**
     * Gets called every tick.
     * Used to update ui elements.
     */
    @Override
    public void updateUI() {
        super.updateUI();
        updateBlockState();
    }

}
