package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.IORegisterController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import static edu.kit.riscjblockits.model.blocks.IORegisterModel.REDSTONE_INPUT;

public class RedstoneInputBlockEntity extends ComputerBlockEntity {
    public RedstoneInputBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.REDSTONE_INPUT_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected ComputerBlockController createController() {
        return new IORegisterController(this, true, REDSTONE_INPUT);
    }


}
