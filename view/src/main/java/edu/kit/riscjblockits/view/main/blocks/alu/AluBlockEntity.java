package edu.kit.riscjblockits.view.main.blocks.alu;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class AluBlockEntity extends ComputerBlockEntity {
    public AluBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.ALU_BLOCK_ENTITY, pos, state);
    }
}
