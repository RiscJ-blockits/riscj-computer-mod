package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * This class defines all registers in the game.
 */
public class RegisterBlock  extends ComputerBlock {

    /**
     * The shape of the RegisterBlock model. Model as in textured cube, not as in MVC.
     */
    private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 16, 16);

    /**
     * Determines if the register is active or not.
     */
    public static final BooleanProperty ACTIVE = BooleanProperty.of("lit");

    /**
     * Creates a new RegisterBlock with the given settings.
     * @param settings The settings for the block as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    private RegisterBlock(Settings settings) {
        super(settings);
    }

    /**
     * Creates a new RegisterBlock with default settings.
     */
    public RegisterBlock() {
        super(FabricBlockSettings.create().luminance(state -> state.get(ACTIVE) ? 15 : 0).nonOpaque());
        setDefaultState(getDefaultState().with(ACTIVE, false));
    }

    /**
     * Creates a new entity for a register.
     * This method is invoked by minecraft when the block is loaded.
     * @nullable This is marked as nullable for one minecraft internal call but should never return null.
     * See {@link net.minecraft.block.BlockEntityProvider#createBlockEntity(BlockPos, BlockState)}.
     *
     * @param pos The position of the block in the minecraft world for which the entity should be created.
     * @param state The state of the minecraft block for which the entity should be created.
     * @return A new default instance of {@link RegisterBlockEntity}.
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RegisterBlockEntity(pos, state);
    }

    /**
     * Returns the shape of the RegisterBlock model.
     * @param state The state of the minecraft block.
     * @param world The world in which the block is placed.
     * @param pos The position of the block in the minecraft world.
     * @param context The context in which the shape is requested.
     * @return The shape of the RegisterBlock model.
     */
    @Override
    public VoxelShape getOutlineShape(BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    /**
     * This method is called when the player right clicks on the block.
     * It will open the screen for the register block.
     * @param state The state of the minecraft block.
     * @param world The world in which the block is placed.
     * @param pos The position of the block in the minecraft world.
     * @param player The player that right clicked on the block.
     * @param hand The hand with which the player right clicked on the block.
     * @param hit The hit result of the raycast that was used to determine the block that was right clicked on.
     * @return Always returns {@link ActionResult#SUCCESS}.
     */
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = ((RegisterBlockEntity) world.getBlockEntity(pos));

            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    /** Is called by minecraft. We add our custom block state.
     * @param builder
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

}
