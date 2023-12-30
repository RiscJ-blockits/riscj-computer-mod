package edu.kit.riscjblockits.view.main.blocks.bus;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BusController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.EntityType;
import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
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
        setType(EntityType.BUS);
    }

    /**
     * Every entity needs its own controller.
     * @return An BusController bound to this entity.
     */
    @Override
    protected BlockController createController() {
        return new BusController(this);
    }

    /**
     * ToDo
     * @return
     */
    @Override
    public String getInfo() {
        return null;
    }

    /**
     * Get the neighbors of this block that are bus blocks or are computer blocks.
     * @return The controllers from the neighboring blocks that are bus blocks or are computer blocks.
     */
    @Override
    public List<BlockController> getComputerNeighbours() {
        List<BlockController> neigbhours = new ArrayList<>();
        List<BlockEntity> blockEntities = new ArrayList<>();
        World world = getWorld();

        blockEntities.add(world.getBlockEntity(getPos().down()));
        blockEntities.add(world.getBlockEntity(getPos().up()));
        blockEntities.add(world.getBlockEntity(getPos().south()));
        blockEntities.add(world.getBlockEntity(getPos().north()));
        blockEntities.add(world.getBlockEntity(getPos().east()));
        blockEntities.add(world.getBlockEntity(getPos().west()));
        //test blocks
        for (BlockEntity entity:blockEntities) {
            if (entity instanceof ComputerBlockEntity) {               //FixMe instanceof schöner machen
                if (((ComputerBlockEntity) entity).getModblockType() == EntityType.BUS ||
                ((ComputerBlockEntity) entity).getModblockType() == EntityType.CONNECTABLE) {
                    neigbhours.add(((ComputerBlockEntity) entity).getController());
                }
            }
        }
        return neigbhours;
    }

}
