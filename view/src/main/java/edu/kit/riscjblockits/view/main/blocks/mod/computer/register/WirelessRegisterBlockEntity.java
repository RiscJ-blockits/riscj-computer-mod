package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.io.WirelessRegisterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class defines a register entity that can change its value based on another register somewhere else in the world.
 */
public class WirelessRegisterBlockEntity extends RegisterBlockEntity {

    /**
     * Creates a new WirelessRegisterBlockEntity.
     * @param pos The position of the block in the minecraft world for which the entity should be created.
     * @param state The state of the minecraft block for which the entity should be created.
     */
    public WirelessRegisterBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.WIRELESS_REGISTER_BLOCK_ENTITY, pos, state);
    }

    /**
     * Creates a specific Controller.
     * @return A new WirelessRegisterController.
     */
    @Override
    protected ComputerBlockController createController() {
        return new WirelessRegisterController(this);
    }

    /**
     * This method syncs the register with the connected register.
     */
    public void syncRegister() {
        World world = this.getWorld();
        BlockPos pos = this.getPos();
        if (!world.isClient) {
            if (((WirelessRegisterController)this.getController()).getConnectedPos() == null) {
                return;
            }
            BlockPosition connectedBlockPos = ((WirelessRegisterController)this.getController()).getConnectedPos();
            BlockPos conPos = new BlockPos((int)connectedBlockPos.getX(), (int)connectedBlockPos.getY(), (int)connectedBlockPos.getZ());
            if (!conPos.equals(pos) && world.getBlockEntity(conPos) != null) {
                WirelessRegisterController connectedController = ((WirelessRegisterController)((ModBlockEntity)world.getBlockEntity(conPos)).getController());
                ((WirelessRegisterController)this.getController()).setRegisterModel(connectedController);
            }
        }
    }

    /**
     * Is called every tick.
     * Syncs the register with the connected registers.
     * @param world the world in which the block is placed
     * @param pos the position of the block in the minecraft world
     * @param state the state of the minecraft block
     * @param entity the block entity
     */
    public static void tick(World world, BlockPos pos, BlockState state, ComputerBlockEntity entity) {
        ComputerBlockEntity.tick(world, pos, state, entity);
        if (world.isClient) {
            return;
        }
        ((WirelessRegisterBlockEntity) world.getBlockEntity(pos)).syncRegister();
    }

}
