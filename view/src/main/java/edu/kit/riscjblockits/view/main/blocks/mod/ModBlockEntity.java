package edu.kit.riscjblockits.view.main.blocks.mod;

import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        //FixMe Controller needs to get created when reload
    }

    /**
     * Every modblock entity needs its own custom controller.
     * @return The Controller of this block entity.
     */
    protected abstract IUserInputReceivableController createController();

    /**
     * The method is called when the block is placed in the world.
     * It creates the controller for the block if it is called from the server context.
     */
    public void setController() {
        assert world != null;
        if (!world.isClient && controller == null) {
            controller = createController();
        }
    }

    /**
     * ToDo
     * @param nbt
     */
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        controller.setData(new NbtDataConverter(nbt).getData());
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
