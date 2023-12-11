package edu.kit.riscjblockits.view.main.blocks;

import edu.kit.riscjblockits.view.main.blocks.modblock.Modblock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class TestBlock extends Modblock {
    public TestBlock() {

    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TestBlockEntity(pos, state);
    }


}
