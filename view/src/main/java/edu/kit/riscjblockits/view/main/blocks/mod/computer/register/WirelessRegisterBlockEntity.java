package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableComputerController;
import edu.kit.riscjblockits.controller.blocks.WirelessRegisterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WirelessRegisterBlockEntity extends RegisterBlockEntity {


    public WirelessRegisterBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.WIRELESS_REGISTER_BLOCK_ENTITY, pos, state);
    }

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
            WirelessRegisterController connectedController = ((WirelessRegisterController)((ModBlockEntity)world.getBlockEntity(conPos)).getController());
            ((WirelessRegisterController)this.getController()).setRegisterModel(connectedController);
        }
    }
}
