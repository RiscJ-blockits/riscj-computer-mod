package edu.kit.riscjblockits.view.main.blocks.mod;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

/**
 * This class represents a block entity from our mod in the game.
 * Every Block has its own unique BlockEntity while it is loaded.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public abstract class ModBlockEntity extends BlockEntity {

    private static final String CONTROLLER_NBT_TAG = "riscj_blockits.controller";
    /**
     * The controller of this block entity.
     * It can be null if the block is in the process of being loaded.
     * The controller handles the computer logic of the block and the communication with the model.
     * Is null on the client side.
     */

    private IUserInputReceivableController controller;

    /**
     * The type of this entity. Is used to differentiate between different types of entities.
     */
    private EntityType entitytype;

    /**
     * Creates a new Block Entity with the given settings.
     * @param type Defines what block entity should be created.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    protected ModBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        entitytype = EntityType.UNCONNECTABLE;
        markDirty();
    }

    /**
     * Every modblock entity needs its own custom controller.
     * @return The Controller of this block entity.
     */
    protected abstract IUserInputReceivableController createController();

    /**
     * The method is called when the block is placed in the world.
     * ToDo anders als Entwurf
     *
     */
    public void setController() {
        //We need the controller when the world is still loading, but the clustering can only start when the world is finished loading.
        //If we place a new block, both things can happen at the same time.
        if (world != null && world.isClient) {
            return;
        }
        if (world == null && controller == null) {
            controller = createController();
        }
        else if (world != null && controller == null && entitytype == EntityType.CONNECTABLE) {
            controller = createController();
            ((ComputerBlockController) controller).startClustering(new BlockPosition(pos.getX(), pos.getY(), pos.getZ()));
        } else if ((world != null && controller != null
                    && entitytype == EntityType.CONNECTABLE
                    && ((ComputerBlockController) controller).getBlockPosition() == null)) {
            ((ComputerBlockController) controller).startClustering(new BlockPosition(pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    /**
     * Method to read block data from a nbt compound.
     * @param nbt The nbt data that should be read from.
     */
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        setController();
        if (controller != null) {       //controller is only in the server not null
            if (!nbt.contains("modData")) {
                return;
            }
            IDataElement newData = new NbtDataConverter(nbt.get("modData")).getData();
            controller.setData(newData);        //when we are loaded, we get our initial data from the nbt
        }
    }

    public BlockPosition getBlockPosition() {
        BlockPos pos = getPos();
        return new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * @return Returns the type of this block entity.
     */
    public EntityType getModblockType() {
        return entitytype;
    }

    /**
     * @return Returns the controller of this block entity. If in client context, it returns null.
     */
    public IUserInputReceivableController getController() {
        if (world.isClient) {
            return null;
        }
        return controller;
    }

    /**
     * Sets the type of this block entity.
     * @param type
     */
    public void setType(EntityType type) {
        this.entitytype = type;
    }

}
