package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.WirelessRegisterController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class WirelessRegisterBlockEntity extends ComputerBlockEntity {


    public WirelessRegisterBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.WIRELESS_REGISTER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected ComputerBlockController createController() {
        return new WirelessRegisterController(this);
    }

    public void incrementFrequence() {
        if (((WirelessRegisterController) getController()) != null) {
            System.out.println("Incremented frequence.");
            ((WirelessRegisterController) getController()).incrementFrequence();
        }
    }
}
