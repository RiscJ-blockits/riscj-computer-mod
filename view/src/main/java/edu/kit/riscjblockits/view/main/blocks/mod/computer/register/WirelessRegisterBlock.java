package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.controller.blocks.WirelessRegisterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WirelessRegisterBlock extends RegisterBlock {

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
        WirelessRegisterBlockEntity blockEntity = (WirelessRegisterBlockEntity) world.getBlockEntity(pos);
        BlockPosition connectedBlockPos = ((WirelessRegisterController)blockEntity.getController()).getConnectedPos();
        BlockPos conPos = new BlockPos((int)connectedBlockPos.getX(), (int)connectedBlockPos.getY(), (int)connectedBlockPos.getZ());
        WirelessRegisterController connectedController = ((WirelessRegisterController)((ModBlockEntity)world.getBlockEntity(conPos)).getController());
        ((WirelessRegisterController)blockEntity.getController()).setRegisterModel(connectedController);
        super.onUse(state, world, pos, player, hand, hit);

        if (player.getStackInHand(hand).getItem() == RISCJ_blockits.WIRELESS_REGISTER_BLOCK_ITEM) {

            NbtCompound nbt = player.getStackInHand(hand).getNbt();

            if (nbt == null) {
                nbt = new NbtCompound();
            }

            nbt.putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});

            player.getStackInHand(hand).setNbt(nbt);

        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.isClient || itemStack.getNbt() == null) {
            return;
        }
        WirelessRegisterBlockEntity blockEntity = (WirelessRegisterBlockEntity) world.getBlockEntity(pos);
        int[] neighbourPos = itemStack.getNbt().getIntArray("pos");
        BlockPos blockPos = new BlockPos(neighbourPos[0], neighbourPos[1], neighbourPos[2]);
        //Todo instanceof entfernen
        if (!(world.getBlockEntity(blockPos) instanceof WirelessRegisterBlockEntity)) {
            return;
        }
        ((ModBlockEntity)world.getBlockEntity(blockPos)).setController();
        ((WirelessRegisterController) blockEntity.getController())
                .setRegisterModel(((WirelessRegisterController)((ModBlockEntity)world.getBlockEntity(blockPos)).getController()));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            int[] pos = nbt.getIntArray("pos");
            tooltip.add(Text.of("X: " + pos[0] + ", Y: " + pos[1] + ", Z: " + pos[2]));
        }
        super.appendTooltip(stack, world, tooltip, options);
    }

}
