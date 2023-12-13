package edu.kit.riscjblockits.view.main.blocks.bus;


import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class BusBlockEntity extends ComputerBlockEntity {
    public BusBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.BUS_BLOCK_ENTITY, pos, state);
    }
}
