package edu.kit.riscjblockits.view.main.blocks.mod.computer.memory;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(TestSetupMain.class)
class MemoryBlockTest {

    @Test
    void createMemoryBlock() {
        MemoryBlock memoryBlock = new MemoryBlock();
        assertEquals(0.0F, memoryBlock.getHardness());
        memoryBlock = new MemoryBlock(FabricBlockSettings.create().hardness(1.0F));
        assertEquals(1.0F, memoryBlock.getHardness());
    }

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
