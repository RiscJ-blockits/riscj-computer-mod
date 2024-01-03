package edu.kit.riscjblockits.view.main.blocks.alu;

import edu.kit.riscjblockits.controller.blocks.*;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AluBlockEntity extends ComputerBlockEntity {

    public AluBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.ALU_BLOCK_ENTITY, pos, state);
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    protected IUserInputReceivableComputerController createController() {
        return new AluController(this);
    }

}
