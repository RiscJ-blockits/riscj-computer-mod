package edu.kit.riscjblockits.view.main.blocks.computer;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableComputerController;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.view.main.blocks.EntityType;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;


/**
 * Blocks that connect with a bus or are a bus. They have a controller.
 * @author ujiqk
 * @version 1.0 */
public abstract class ComputerBlockEntity extends ModBlockEntity implements IConnectableComputerBlockEntity,
    IGoggleQueryable {

    public abstract String getInfo();

    protected abstract IUserInputReceivableComputerController createController();

    public static void tick(World world, BlockPos pos, BlockState state, ComputerBlockEntity entity) {
        if (!world.isClient && entity.getController() != null) {
            ((ComputerBlockController)entity.getController()).tick();
        }
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
    public List<ComputerBlockController> getComputerNeighbours() {
        List<ComputerBlockController> neigbhours = new ArrayList<>();
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
                if (((ComputerBlockEntity) entity).getModblockType() == EntityType.BUS) {
                    //ToDo cast entfernen
                    neigbhours.add((ComputerBlockController) ((ComputerBlockEntity) entity).getController());
                }
            }
        }
        return neigbhours;
    }

    public void setBlockModel(BlockModel model) {
        this.model = model;
    }

    public void onBroken() {
        if (!world.isClient) {
            ((ComputerBlockController)getController()).onBroken();
        }
        //ToDo controller must be there
    }

    @Override
    public Text getGoggleText() {
        return Text.empty();
    }

}
