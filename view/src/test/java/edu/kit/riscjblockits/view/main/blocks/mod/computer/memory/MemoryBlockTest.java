package edu.kit.riscjblockits.view.main.blocks.mod.computer.memory;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(TestSetupMain.class)
class MemoryBlockTest {
    @Test
    void createBlockEntity() {
        MemoryBlock memoryBlock = new MemoryBlock();
        MemoryBlockEntity memoryBlockEntity = (MemoryBlockEntity) memoryBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert memoryBlockEntity != null;
        assertFalse(memoryBlockEntity.isActive());
        assertFalse(memoryBlockEntity.hasWorld());
    }

    @Test
    void createBlockEntityWithPos() {
        MemoryBlock memoryBlock = new MemoryBlock();
        MemoryBlockEntity memoryBlockEntity = (MemoryBlockEntity) memoryBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert memoryBlockEntity != null;
        assertFalse(memoryBlockEntity.isActive());
        assertFalse(memoryBlockEntity.hasWorld());
    }

}
