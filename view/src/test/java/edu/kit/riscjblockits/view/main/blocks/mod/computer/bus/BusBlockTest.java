package edu.kit.riscjblockits.view.main.blocks.mod.computer.bus;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(TestSetupMain.class)
class BusBlockTest {

    @Test
    void createBusBlock() {
        BusBlock busBlock = new BusBlock(FabricBlockSettings.create().hardness(3.0F));
        assertEquals(3.0F, busBlock.getHardness());
    }

    @Test
    void createBlockEntity() {
        BusBlock busBlock = new BusBlock();
        BusBlockEntity busBlockEntity = (BusBlockEntity) busBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert busBlockEntity != null;
        assertEquals(0, busBlockEntity.getPos().getX());
        assertFalse(busBlockEntity.isActive());
        assertFalse(busBlockEntity.hasWorld());
    }

}
