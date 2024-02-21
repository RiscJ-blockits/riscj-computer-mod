package edu.kit.riscjblockits.view.main.blocks.mod.programming;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestSetupMain.class)
class ProgrammingBlockTest {

    @Test
    void createProgrammingBlock() {
        ProgrammingBlock programmingBlock = new ProgrammingBlock();
        assertEquals(5.0F, programmingBlock.getHardness());
        programmingBlock = new ProgrammingBlock(ProgrammingBlock.Settings.create().hardness(1.0F));
        assertEquals(1.0F, programmingBlock.getHardness());
    }

    @Test
    void createBlockEntity() {
        ProgrammingBlock programmingBlock = new ProgrammingBlock();
        ProgrammingBlockEntity programmingBlockEntity = (ProgrammingBlockEntity) programmingBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert programmingBlockEntity != null;
        assertFalse(programmingBlockEntity.hasWorld());
    }

}
