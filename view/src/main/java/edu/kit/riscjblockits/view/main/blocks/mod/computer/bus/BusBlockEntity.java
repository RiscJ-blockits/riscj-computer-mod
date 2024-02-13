package edu.kit.riscjblockits.view.main.blocks.mod.computer.bus;

import edu.kit.riscjblockits.controller.blocks.BusController;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.EntityType;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ConnectingComputerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.List;

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
        if (world.isClient || getController() == null || world.getBlockState(pos) == null) {
            return;
        }
        List<BlockPosition> neighbours = ((BusController) getController()).getBusSystemNeighbors();
        if (neighbours == null) {
            return;
        }
        BlockState state = world.getBlockState(pos).with(BusBlock.NORTH, listContainsPos(neighbours, pos.north()) ?
                        ((ComputerBlockEntity) world.getBlockEntity(pos.north())).isActive() && this.isActive() ?
                                BusBlock.Side.ACTIVE : BusBlock.Side.PRESENT : BusBlock.Side.NONE)
                .with(BusBlock.EAST, listContainsPos(neighbours, pos.east()) ?
                        ((ComputerBlockEntity) world.getBlockEntity(pos.east())).isActive() && this.isActive() ?
                                BusBlock.Side.ACTIVE : BusBlock.Side.PRESENT : BusBlock.Side.NONE)
                .with(BusBlock.SOUTH, listContainsPos(neighbours, pos.south()) ?
                        ((ComputerBlockEntity) world.getBlockEntity(pos.south())).isActive() && this.isActive() ?
                                BusBlock.Side.ACTIVE : BusBlock.Side.PRESENT : BusBlock.Side.NONE)
                .with(BusBlock.WEST, listContainsPos(neighbours, pos.west()) ?
                        ((ComputerBlockEntity) world.getBlockEntity(pos.west())).isActive() && this.isActive() ?
                                BusBlock.Side.ACTIVE : BusBlock.Side.PRESENT : BusBlock.Side.NONE)
                .with(BusBlock.UP, listContainsPos(neighbours, pos.up()) ?
                        ((ComputerBlockEntity) world.getBlockEntity(pos.up())).isActive() && this.isActive() ?
                                BusBlock.Side.ACTIVE : BusBlock.Side.PRESENT : BusBlock.Side.NONE)
                .with(BusBlock.DOWN, listContainsPos(neighbours, pos.down()) ?
                        ((ComputerBlockEntity) world.getBlockEntity(pos.down())).isActive() && this.isActive() ?
                                BusBlock.Side.ACTIVE : BusBlock.Side.PRESENT : BusBlock.Side.NONE);
        world.setBlockState(pos, state);
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
