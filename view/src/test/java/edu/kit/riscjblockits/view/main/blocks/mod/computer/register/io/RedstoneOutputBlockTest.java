package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestSetupMain.class)
class RedstoneOutputBlockTest {

    @Test
    void createRedstoneOutputBlock() {
        RedstoneOutputBlock redstoneOutputBlock = new RedstoneOutputBlock();
        assertEquals(0.0F, redstoneOutputBlock.getHardness());
    }

    @Test
    void createBlockEntity() {
        RedstoneOutputBlock redstoneOutputBlock = new RedstoneOutputBlock();
        RedstoneOutputBlockEntity redstoneOutputBlockEntity = (RedstoneOutputBlockEntity) redstoneOutputBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert redstoneOutputBlockEntity != null;
        assertFalse(redstoneOutputBlockEntity.isActive());
        assertFalse(redstoneOutputBlockEntity.hasWorld());
    }

    @Test
    void emitsRedstonePower() {
        assertTrue(new RedstoneOutputBlock().emitsRedstonePower(null));
    }

    @Test
    void getStrongRedstonePower() {
        World world = mock(World.class);
        RedstoneOutputBlock redOutBlock = (RedstoneOutputBlock) RISCJ_blockits.REDSTONE_OUTPUT_BLOCK;
        RedstoneOutputBlockEntity redOutEntity = mock(RedstoneOutputBlockEntity.class);
        when(world.getBlockEntity(new BlockPos(0,0,0))).thenReturn(redOutEntity);
        assert redOutEntity != null;
        when(redOutEntity.getRedstonePower()).thenReturn(5);
        assertEquals(5, redOutBlock.getStrongRedstonePower(null, world, new BlockPos(0,0,0), null));
    }

    @Test
    void getWeakRedstonePower() {
        World world = mock(World.class);
        RedstoneOutputBlock redOutBlock = (RedstoneOutputBlock) RISCJ_blockits.REDSTONE_OUTPUT_BLOCK;
        RedstoneOutputBlockEntity redOutEntity = (RedstoneOutputBlockEntity) redOutBlock.createBlockEntity(new BlockPos(0,0,0), RISCJ_blockits.REDSTONE_OUTPUT_BLOCK.getDefaultState());
        when(world.getBlockEntity(new BlockPos(0,0,0))).thenReturn(redOutEntity);
        assert redOutEntity != null;
        assertEquals(0, redOutBlock.getWeakRedstonePower(null, world, new BlockPos(0,0,0), null));
    }

}
