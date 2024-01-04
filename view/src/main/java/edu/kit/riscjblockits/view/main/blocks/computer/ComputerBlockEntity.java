package edu.kit.riscjblockits.view.main.blocks.computer;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableComputerController;
import edu.kit.riscjblockits.model.blocks.IQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;
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

/** BlockEntity for all @link ComputerBlocks.
 * Every {@link ComputerBlock} has its own unique ComputerBlockEntity during runtime.
 */
public abstract class ComputerBlockEntity extends ModBlockEntity implements IConnectableComputerBlockEntity,
    IGoggleQueryable {

    /**
     * TODO javadoc - Ask Nils what this does
     * @return
     */
    protected abstract IUserInputReceivableComputerController createController();

    /**
     * TODO javadoc - Ask Nils/Leon what this does
     * @return
     */
    public static void tick(World world, BlockPos pos, BlockState state, ComputerBlockEntity entity) {
        if (!world.isClient && entity.getController() != null) {
            ((ComputerBlockController)entity.getController()).tick();
        }
    }
    /**
     * The block's representation in the model holds the block's data.
     */
    private IQueryableBlockModel model;

    /**
     * Creates a new ComputerBlockEntity with the given settings.
     * @param type The type of the block entity.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public ComputerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setType(EntityType.CONNECTABLE);
    }

    /**
     * Get the {@link BlockController} of this block's neighbours, which are {@link edu.kit.riscjblockits.view.main.blocks.bus.BusBlock}.
     * @return all BlockControllers of this block's neighbours, which are BusBlocks.
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

    /**
     * Sets the model for this block.
     * @param model
     */
    public void setBlockModel(IQueryableBlockModel model) {
        this.model = model;
    }

    /**
     * Passes the onBroken call to the {@link BlockController}, for the {@link BlockController} to handle it.
     */
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

    public IDataElement getBlockEntityData() {
        return null;
    }

}
