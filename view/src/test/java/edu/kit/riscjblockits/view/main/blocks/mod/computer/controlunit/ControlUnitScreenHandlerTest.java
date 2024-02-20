package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

import edu.kit.riscjblockits.view.client.TestSetupClient;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(TestSetupClient.class)
@ExtendWith(TestSetupMain.class)
class ControlUnitScreenHandlerTest {

    @Disabled("This test is not working because we can't use server-client communication")
    @Test
    void getEmptyInstructionSetType() {
        ComputerBlock controlUnitBlock = (ComputerBlock) RISCJ_blockits.CONTROL_UNIT_BLOCK;
        ComputerBlockEntity cuEntity = (ComputerBlockEntity) controlUnitBlock.createBlockEntity(new BlockPos(0,0,0), RISCJ_blockits.CONTROL_UNIT_BLOCK.getDefaultState());
        ControlUnitScreenHandler controlUnitScreenHandler = new ControlUnitScreenHandler(0, mock(PlayerInventory.class), cuEntity);
        assertEquals("", controlUnitScreenHandler.getInstructionSetType());
    }

    @Disabled("This test is not working because we can't use server-client communication")
    @Test
    void getMIMAInstructionSetType() {
        ComputerBlock controlUnitBlock = (ComputerBlock) RISCJ_blockits.CONTROL_UNIT_BLOCK;
        ControlUnitBlockEntity cuEntity = (ControlUnitBlockEntity) controlUnitBlock.createBlockEntity(new BlockPos(0,0,0), RISCJ_blockits.CONTROL_UNIT_BLOCK.getDefaultState());
        assert cuEntity != null;
        cuEntity.setStack(0, new ItemStack(RISCJ_blockits.INSTRUCTION_SET_ITEM_MIMA));
        ControlUnitScreenHandler controlUnitScreenHandler = new ControlUnitScreenHandler(0, mock(PlayerInventory.class), cuEntity);
        assertEquals("Mima", controlUnitScreenHandler.getInstructionSetType());
    }

}
