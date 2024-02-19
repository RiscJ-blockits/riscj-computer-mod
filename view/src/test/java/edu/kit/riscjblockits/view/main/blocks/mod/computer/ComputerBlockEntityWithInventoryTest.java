package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestSetupMain.class)
class ComputerBlockEntityWithInventoryTest {

    @Disabled
    @Test
    void getItems() {
        ComputerBlockEntityWithInventory entity = new ControlUnitBlockEntity(new BlockPos(0,0, 0), RISCJ_blockits.CONTROL_UNIT_BLOCK.getDefaultState());
        //ToDo
    }
}