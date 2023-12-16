package edu.kit.riscjblockits.view.main.blocks.computer;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.IQueryableBlockEntity;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.EntityType;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;


/**
 * Blocks that connect with a bus or are a bus. They have a controller.
 * @author ujiqk
 * @version 1.0 */
public abstract class ComputerBlockEntity extends ModBlockEntity implements IQueryableBlockEntity {

    public static void tick(World world, BlockPos pos, BlockState state, ComputerBlockEntity be) {
        //if (be.model.hasUnqueriedChange())
        //    be.updateDataFromModel();
    }
    private BlockModel model;
    public ComputerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setType(EntityType.CONNECTABLE);
    }

    /**
     * Get the neighbours of this block that are bus blocks.
     * The Bus Entity needs to overwrite this method to return all neighbour computer blocks.
     * @return
     */
    public List<BlockController> getComputerNeighbours() {

        if (world.getBlockEntity(getPos().down()).getType() ) {


        }

        world.getBlockEntity(getPos().down())





        return null;
    }

    public void setBlockModel(BlockModel model) {
        this.model = model;
    }

    public void onBroken() {
        //ToDo controller must be there
        //getController().onBroken();
    }



}
