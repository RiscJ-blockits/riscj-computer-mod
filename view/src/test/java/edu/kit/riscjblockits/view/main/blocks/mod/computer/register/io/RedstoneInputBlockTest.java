package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestSetupMain.class)
class RedstoneInputBlockTest {

    @Test
    void createRedstoneInputBlock() {
        RedstoneInputBlock redstoneInputBlock = new RedstoneInputBlock();
        assertEquals(0.0F, redstoneInputBlock.getHardness());
    }

    @Test
    void createBlockEntity() {
        RedstoneInputBlock redstoneInputBlock = new RedstoneInputBlock();
        RedstoneInputBlockEntity redstoneInputBlockEntity = (RedstoneInputBlockEntity) redstoneInputBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert redstoneInputBlockEntity != null;
        assertNull(redstoneInputBlockEntity.getPos());
        assertFalse(redstoneInputBlockEntity.isActive());
        assertFalse(redstoneInputBlockEntity.hasWorld());
    }

}
