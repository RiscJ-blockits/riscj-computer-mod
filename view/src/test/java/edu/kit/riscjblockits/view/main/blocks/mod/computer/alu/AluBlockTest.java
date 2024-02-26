package edu.kit.riscjblockits.view.main.blocks.mod.computer.alu;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestSetupMain.class)
class AluBlockTest {

    @Test
    void createAluBlock() {
        AluBlock aluBlock = new AluBlock(FabricBlockSettings.create().resistance(3.0F).hardness(3.0F));
        assertEquals(3.0F, aluBlock.getHardness());
    }

    @Test
    void createBlockEntity() {
        AluBlock aluBlock = new AluBlock();
        AluBlockEntity aluBlockEntity = (AluBlockEntity) aluBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert aluBlockEntity != null;
        assertEquals(0, aluBlockEntity.getPos().getX());
        assertFalse(aluBlockEntity.isActive());
        assertFalse(aluBlockEntity.hasWorld());
    }

}
