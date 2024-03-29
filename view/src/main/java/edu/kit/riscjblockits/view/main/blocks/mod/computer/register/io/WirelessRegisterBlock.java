package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.controller.blocks.io.WirelessRegisterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class defines a register block that can change its value based on another register somewhere else in the world.
 */
public class WirelessRegisterBlock extends RegisterBlock {

    /**
     * Determines if the register is connected or not.
     */
    private static final BooleanProperty CONNECTED = RISCJ_blockits.WIRELESS_CONNECTED_PROPERTY;

    /**
     * Creates a new WirelessRegisterBlock with default settings.
     */
    public WirelessRegisterBlock() {
        super();
        setDefaultState(getDefaultState().with(CONNECTED, false));
    }

    /**
     * Creates a new RedstoneInputBlockEntity.
     * @param pos The position of the block in the minecraft world for which the entity should be created.
     * @param state The state of the minecraft block for which the entity should be created.
     * @return A new RedstoneInputBlockEntity bound to the given block position.
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WirelessRegisterBlockEntity(pos, state);
    }

    /**
     * This method is called when the block is right-clicked.
     * We override it because unlike a normal register, a redstone output block has no screen.
     * @param state The state of the minecraft block.
     * @param world The world in which the block is placed.
     * @param pos The position of the block in the minecraft world.
     * @param player The player that right-clicked on the block.
     * @param hand The hand with which the player right-clicked on the block.
     * @param hit The hit result of the ray cast that was used to determine the block that was right-clicked on.
     * @return A PASS action result to indicate that we don't want to do anything when the block is right-clicked.
     */
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        if (player.getStackInHand(hand).getItem() == RISCJ_blockits.WIRELESS_REGISTER_BLOCK_ITEM) {
            NbtCompound nbt = player.getStackInHand(hand).getNbt();
            if (nbt == null) {
                nbt = new NbtCompound();
            }
            nbt.putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
            player.getStackInHand(hand).setNbt(nbt);
            return ActionResult.SUCCESS;
        }
        super.onUse(state, world, pos, player, hand, hit);
        return ActionResult.SUCCESS;
    }

    /**
     * This method is called when the block is placed in the world.
     * @param world The minecraft world the block is placed in.
     * @param pos The position the block is placed at.
     * @param state The state of the block.
     * @param placer The entity that placed the block.
     * @param itemStack The itemstack that was used to place the block.
     */
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.isClient || itemStack.getNbt() == null) {
            return;
        }
        WirelessRegisterBlockEntity blockEntity = (WirelessRegisterBlockEntity) world.getBlockEntity(pos);
        int[] neighbourPos = itemStack.getNbt().getIntArray("pos");
        assert blockEntity != null;
        ((WirelessRegisterController) blockEntity.getController())
                .setWirelessNeighbourPosition(new BlockPosition(neighbourPos[0], neighbourPos[1], neighbourPos[2]));
    }

    /**
     * This method is called when the block is right-clicked with a tool.
     * @param stack The itemstack that was used to right-click on the block.
     * @param world The world in which the block is placed.
     * @param tooltip The list of tooltips that should be displayed.
     * @param options The tooltip context options.
     */
    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            int[] pos = nbt.getIntArray("pos");
            tooltip.add(Text.of("X: " + pos[0] + ", Y: " + pos[1] + ", Z: " + pos[2]));
        }
        super.appendTooltip(stack, world, tooltip, options);
    }

    /**
     * We need to register the tick method in the block entity.
     * @param world the minecraft world the block is placed in.
     * @param state the state of the block.
     * @param type the type of the block entity.
     * @return the block entity ticker for the block entity.
     * @param <T> the type of the block entity.
     */
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, be) -> WirelessRegisterBlockEntity.tick(world1, pos, state1, (ComputerBlockEntity) be);
    }

    /**
     * Is called by minecraft. We add our custom block state.
     * @param builder {@link net.minecraft.block.Block#appendProperties(StateManager.Builder)} for this block.
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CONNECTED);
    }

}
