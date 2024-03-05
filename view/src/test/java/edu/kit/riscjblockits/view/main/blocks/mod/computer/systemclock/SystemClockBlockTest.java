package edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestSetupMain.class)
class SystemClockBlockTest {

    @Test
    void createSystemClockBlock() {
        SystemClockBlock systemClockBlock = new SystemClockBlock();
        assertEquals(0.0F, systemClockBlock.getHardness());
        systemClockBlock = new SystemClockBlock(FabricBlockSettings.create().hardness(1.0F));
        assertEquals(1.0F, systemClockBlock.getHardness());
    }

    @Test
    void createBlockEntity() {
        SystemClockBlock systemClockBlock = new SystemClockBlock();
        SystemClockBlockEntity systemClockBlockEntity = (SystemClockBlockEntity) systemClockBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert systemClockBlockEntity != null;
        assertFalse(systemClockBlockEntity.hasWorld());
        assertFalse(systemClockBlockEntity.isActive());
    }

    @Test
    void scheduledTick() {
        //only tests if the method is called without errors because no good way to assert the result
        SystemClockBlock systemClockBlock = new SystemClockBlock();
        SystemClockBlockEntity systemClockBlockEntity = (SystemClockBlockEntity) systemClockBlock.createBlockEntity(new BlockPos(0,0,0), null);
        ServerWorld world = mock(ServerWorld.class);
        when(world.getBlockEntity(new BlockPos(0,0,0))).thenReturn(systemClockBlockEntity);
        when(world.isReceivingRedstonePower(new BlockPos(0,0,0))).thenReturn(true);
        systemClockBlock.scheduledTick(null, world, new BlockPos(0,0,0), null);
    }

}
