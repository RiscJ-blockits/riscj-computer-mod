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
     * Creates a new WirelessRegisterController.
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
        if (this.getController() == null) {
            this.setController();
        }
        World world = this.getWorld();
        BlockPos pos = this.getPos();
        if (!world.isClient) {
            if (((WirelessRegisterController)this.getController()).getConnectedPos() == null) {
                return;
            }
            BlockPosition connectedBlockPos = ((WirelessRegisterController)this.getController()).getConnectedPos();
            BlockPos conPos = new BlockPos((int)connectedBlockPos.getX(), (int)connectedBlockPos.getY(), (int)connectedBlockPos.getZ());
            //Todo instanceof entfernen
            if (!(world.getBlockEntity(conPos) instanceof WirelessRegisterBlockEntity)) {
                ((WirelessRegisterController) this.getController())
                        .setWirelessNeighbourPosition(new BlockPosition(pos.getX(), pos.getY(), pos.getZ()));
                conPos = pos;
            }
            if (!conPos.equals(pos)) {
                WirelessRegisterController connectedController = ((WirelessRegisterController)((ModBlockEntity)world.getBlockEntity(conPos)).getController());
                ((WirelessRegisterController)this.getController()).setRegisterModel(connectedController);
            }
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, ComputerBlockEntity entity) {
        ComputerBlockEntity.tick(world, pos, state, entity);
        if (world.isClient) {
            return;
        }
        ((WirelessRegisterBlockEntity) world.getBlockEntity(pos)).syncRegister();
    }
}
