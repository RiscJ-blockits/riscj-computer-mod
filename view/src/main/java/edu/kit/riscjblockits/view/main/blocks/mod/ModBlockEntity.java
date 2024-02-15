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

import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;

/**
 * This class represents a block entity from our mod in the game.
 * Every Block has its own unique BlockEntity while it is loaded.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public abstract class ModBlockEntity extends BlockEntity {

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

    /** ToDo anders als im Entwurf
     * this method is responsible for creating the controller and starting the clustering.
     * It does this at different times depending if the world is loading or the block is placed at runtime.
     * is called at different events to cover all cases.
     * */
    public void setController() {
        //We need the controller when the world is still loading, but the clustering can only start when the world is finished loading.
        //If we place a new block, both things can happen at the same time.
        if (world != null && world.isClient) {
            return;
        }
        if (world == null && controller == null) {
            controller = createController();
        } else if (world != null && controller == null && entitytype == EntityType.CONNECTABLE) {
            controller = createController();
            ((ComputerBlockController) controller).startClustering(new BlockPosition(pos.getX(), pos.getY(), pos.getZ()));
        } else if ((world != null && controller != null
                    && entitytype == EntityType.CONNECTABLE
                    && ((ComputerBlockController) controller).getBlockPosition() == null)) {
            ((ComputerBlockController) controller).startClustering(new BlockPosition(pos.getX(), pos.getY(), pos.getZ()));
        } else if (controller == null) {
            controller = createController();
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
        if (controller != null) {               //the controller is in this stage only in the server not null
            if (!nbt.contains(MOD_DATA)) {
                return;
            }
            IDataElement newData = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
            controller.setData(newData);        //when we are loaded, we get our initial data from the nbt
        }
    }

    /**
     * Getter for the position.
     * @return The Block Position of this block entity.
     */
    public BlockPosition getBlockPosition() {
        BlockPos pos = getPos();
        return new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Getter for the type of this block entity.
     * @return Returns the type of this block entity.
     */
    public EntityType getModblockType() {
        return entitytype;
    }

    /**
     * Getter for the controller of this block entity.
     * @return Returns the controller of this block entity. If in client context, it returns null.
     */
    public IUserInputReceivableController getController() {
        if (world != null && world.isClient) {
            return null;
        }
        return controller;
    }

    /**
     * Sets the type of this block entity.
     * @param type {@link EntityType} of this block entity.
     */
    public void setType(EntityType type) {
        this.entitytype = type;
    }

}
