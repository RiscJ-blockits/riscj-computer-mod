package edu.kit.riscjblockits.view.main.blocks.mod;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a block from our mod in the game.
 * Every Block in the world of the same type is one class.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public abstract class ModBlock extends BlockWithEntity {

    /**
     * Default strength of all blocks.
     */
    private static final float BLOCK_STRENGTH = 1.0f;

    /**
     * Creates a new Modblock with the given settings.
     * @param settings The settings for the block as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    protected ModBlock(Settings settings) {
        super(settings);
    }

    /**
     * Creates a new ModBlock with default settings.
     */
    protected ModBlock() {
        super(FabricBlockSettings.create().strength(BLOCK_STRENGTH));
    }

    /**
     * This method is called when the block is right-clicked.
     * See {@link net.minecraft.block.Block#onUse(BlockState, World, BlockPos, PlayerEntity, Hand, BlockHitResult)}.
     * We additionally open the GUI of the Block if it has one.
     * @param state the block state.
     * @param world the minecraft world the block is placed in.
     * @param pos the position the block is placed at.
     * @param player the player that right-clicked the block.
     * @param hand the hand the player used to right-click the block.
     * @param hit the hit result of the raycast.
     * @return {@link ActionResult#SUCCESS} if the block has a Screenhandler or the world is client side,
     * {@link ActionResult#PASS} otherwise.
     */
    @Deprecated(forRemoval = false)
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        //ToDo Leon: check if this is needed
        //Controller must be there when block is clicked
//        world.getBlockEntity(pos).setWorld(world);
//        ((ModBlockEntity)world.getBlockEntity(pos)).setController();
        // open Gui if a Screenhandlerfactory is present for the block
        if (!world.isClient) {
            //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
            //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                //With this call, the server will request the client to open the appropriate Screenhandler.
                player.openHandledScreen(screenHandlerFactory);
            } else {
                return ActionResult.PASS;
            }
        }
        return ActionResult.SUCCESS;
    }

    /**
     * See {@link net.minecraft.block.AbstractBlock#onStateReplaced(BlockState, World, BlockPos, BlockState, boolean)}.
     * @param state the old block state.
     * @param world the minecraft world the block is placed in.
     * @param pos the position of the block.
     * @param newState the new block state.
     * @param moved true if the block was moved, false otherwise.
     *
     * @deprecated is marked as deprecated in the minecraft source code.
     */
    @Deprecated(forRemoval = false)
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ImplementedInventory) {
                ItemScatterer.spawn(world, pos, (Inventory)(blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
        //ToDo Leon: check if this is needed
        //Controller must be there when block is broken
//        if (!world.isClient && state.getBlock() != newState.getBlock()) {
//            BlockEntity blockEntity = world.getBlockEntity(pos);
//            if (blockEntity instanceof ComputerBlockEntity) {
//                world.getBlockEntity(pos).setWorld(world);
//                ((ModBlockEntity)world.getBlockEntity(pos)).setController();
//            }
//        }
    }

    /**
     * Returns the render type of the block.
     * @param state the block state. is ignored.
     * @return the render type of the blocks is always {@link BlockRenderType#MODEL}.
     *
     * @deprecated is marked as deprecated in the minecraft source code.
     */
    @Deprecated(forRemoval = false)
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /**
     * This method is called when the block is placed in the world.
     * The Block initializes the creation of his Block Controller.
     * @param world The minecraft world the block is placed in.
     * @param pos The position the block is placed at.
     * @param state ToDo ??
     * @param placer The entity that placed the block.
     * @param itemStack The itemstack that was used to place the block.
     */
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
                         ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        //assert: entity already exists
        world.getBlockEntity(pos).setWorld(world);
        ((ModBlockEntity)world.getBlockEntity(pos)).setController();
    }

}
