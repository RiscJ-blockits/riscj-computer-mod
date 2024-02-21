package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(TestSetupMain.class)
class ConnectingComputerBlockTest {

    static ConnectingComputerBlock connectingComputerBlock = new ConnectingComputerBlock(3) {
        @Nullable
        @Override
        public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return null;
        }
    };

    @Test
    void isTransparent() {
        assertFalse(connectingComputerBlock.isTransparent(null, null, null));
    }

    @Test
    void toStringEnum() {
        assertEquals("none", ConnectingComputerBlock.Side.NONE.toString());
        assertEquals("present", ConnectingComputerBlock.Side.PRESENT.toString());
    }

}
