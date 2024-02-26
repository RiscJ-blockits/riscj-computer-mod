package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestSetupMain.class)
class ControlUnitBlockTest {

    @Test
    void createControlUnitBlock() {
        ControlUnitBlock controlUnitBlock = new ControlUnitBlock();
        assertEquals(0.0F, controlUnitBlock.getHardness());
        controlUnitBlock = new ControlUnitBlock(FabricBlockSettings.create().hardness(1.0F));
        assertEquals(1.0F, controlUnitBlock.getHardness());
    }

}
