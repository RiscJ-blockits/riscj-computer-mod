package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableComputerController;
import edu.kit.riscjblockits.model.blocks.IQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.EntityType;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.bus.BusBlock;
import edu.kit.riscjblockits.view.main.data.DataNbtConverter;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;

/** BlockEntity for all @link ComputerBlocks.
 * Every {@link ComputerBlock} has its own unique ComputerBlockEntity during runtime.
 */
public abstract class ComputerBlockEntity extends ModBlockEntity implements IConnectableComputerBlockEntity,
    IGoggleQueryable {

    /**
     * The block's representation in the model holds the block's data.
     */
    private IViewQueryableBlockModel model;

    /**
     * Will create a new {@link ComputerBlockController} for this block.
     */
    protected abstract IUserInputReceivableComputerController createController();

    /**
     * Only exists on the client side. Holds the data displayed on the client.
     */
    private IDataElement data;

    /**
     * Method that Minecraft calls every tick.
     * Will call the {@link ComputerBlockController#tick()} method.
     */
    public static void tick(World world, BlockPos pos, BlockState state, ComputerBlockEntity entity) {
        if(!world.isClient) {               //used to make sure we always have a controller
            entity.setController();         //this could eat a lot of performance
        }
        if (!world.isClient && entity.getController() != null) {
            ((IUserInputReceivableComputerController)entity.getController()).tick();
        }
        entity.updateUI();
        entity.syncToClient();
    }


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
     * Get the {@link BlockController} of this block's neighbors, which are {@link BusBlock}.
     * Method is only overwritten in the BusEntity.
     * @return all BlockControllers of this block's neighbors, which are BusBlocks.
     */
    public List<ComputerBlockController> getComputerNeighbours() {
        List<ComputerBlockController> neigbhours = new ArrayList<>();
        List<BlockEntity> blockEntities = new ArrayList<>();
        World world = getWorld();
        assert world != null;       //because controllers only exist in the server, this is always true.
        blockEntities.add(world.getBlockEntity(getPos().down()));
        blockEntities.add(world.getBlockEntity(getPos().up()));
        blockEntities.add(world.getBlockEntity(getPos().south()));
        blockEntities.add(world.getBlockEntity(getPos().north()));
        blockEntities.add(world.getBlockEntity(getPos().east()));
        blockEntities.add(world.getBlockEntity(getPos().west()));
        //
        for (BlockEntity entity:blockEntities) {
            if (entity instanceof ComputerBlockEntity) {               //FixMe instanceof schöner machen (geht das)
                if (((ComputerBlockEntity) entity).getModblockType() == EntityType.CONNECTABLE) {
                    if (((ComputerBlockEntity) entity).getController() == null                //don't start clustering too early when chunk is still loading
                        ||((ComputerBlockController) ((ComputerBlockEntity) entity).getController()).getClusterHandler() == null) {
                        //do nothing
                    } else {
                        neigbhours.add((ComputerBlockController) ((ComputerBlockEntity) entity).getController());
                    }
                }
            }
        }
        return neigbhours;
    }

    /**
     * Sets the model for this block.
     * @param model The model for this block.
     */
    public void setBlockModel(IViewQueryableBlockModel model) {
        this.model = model;
    }

    /**
     * Passes the onBroken call to the {@link BlockController}, for the {@link BlockController} to handle it.
     */
    public void onBroken() {
        assert world != null;
        if (!world.isClient && getController() != null) {
            ((IUserInputReceivableComputerController)getController()).onBroken();
        }
    }

    /**
     * Update the block state of neighbourBusses.
     */
    public void neighborUpdate() {
        world.getBlockState(pos).updateNeighbors(world, pos, 1);
    }

    /**
     * Gets the Text that should be displayed when the player is looking at this block.
     * @return The Text that should be displayed when the player is looking at this block.
     */
    @Override
    public Text getGoggleText() {
        return Text.of("Goggle Text " + getPos().toString());
    }

    @Override
    public IDataElement getBlockEntityData() {
        return null;
    }

    /**
     * Getter for the model of this block.
     * @return
     */
    protected IQueryableBlockModel getModel() {
        return model;
    }

    /** Nicht im Entwurf
     * Gets called every tick.
     * Used to update ui elements.
     */
    public void updateUI() {
        //ToDo hasUnqueriedStateChange die richtige Variable um aktivität zu messen?
        if (world != null && getModel() != null && getModel().hasUnqueriedStateChange()) {
            if (getModel().hasUnqueriedStateChange()) {
                world.setBlockState(pos, world.getBlockState(pos).with(RISCJ_blockits.ACTIVE_STATE_PROPERTY, true));
            } else {
                world.setBlockState(pos, world.getBlockState(pos).with(RISCJ_blockits.ACTIVE_STATE_PROPERTY, false));
            }
        }
    }


    /**
     * Gets called every tick.
     * Syncs the block entity nbt data to the client.
     */
    private void syncToClient() {
        if (world == null || world.isClient || model == null)
            return;
        if (model.hasUnqueriedStateChange()) {
            if (world.getPlayers().isEmpty()) {
               return;       //we are too early in the loading process
            }
            NbtCompound nbt = new NbtCompound();
            writeNbt(nbt);

            world.getPlayers().forEach(
                    player -> {
                        // reset reader Index, to make sure multiple players can receive the same packet
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeBlockPos(pos);
                        buf.writeNbt(nbt);
                        ServerPlayNetworking.send((ServerPlayerEntity) player,
                            NetworkingConstants.SYNC_BLOCK_ENTITY_DATA, buf);

                    });


            model.onStateQuery();
        }
    }

    /**
     * Method to write block data to a nbt compound.
     * Does different things on the client and on the server side.
     * @param nbt The nbt data that should be written to.
     */
    @Override
    public void writeNbt(NbtCompound nbt) {
        if (getModel() != null) {                       //we are in the server, so we send the data in the model
            nbt.put(MOD_DATA, new DataNbtConverter(getModel().getData()).getNbtElement());
        }
        if (world != null && world.isClient && data != null) {          //we are in the client, so we send local data
            nbt.put(MOD_DATA, new DataNbtConverter(data).getNbtElement());
        }
        super.writeNbt(nbt);
        markDirty();
    }

    /**
     * Method to read block data from a nbt compound.
     * Does different things on the client and on the server side.
     * @param nbt The nbt data that should be read from.
     */
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (world != null && world.isClient &&  nbt.contains(MOD_DATA)) {     //we are in the client and want to save the data
            data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        }
    }

    //todo nicht im Entwurfs wiki
    public void requestData() {
        getController().setData(new Data());
    }

}
