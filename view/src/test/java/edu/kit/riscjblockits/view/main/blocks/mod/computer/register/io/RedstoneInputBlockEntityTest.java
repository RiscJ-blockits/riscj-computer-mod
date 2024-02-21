package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestSetupMain.class)
class RedstoneInputBlockEntityTest {

    static RedstoneInputBlockEntity redstoneInputBlockEntity;

    @BeforeAll
    static void beforeAll() {
        redstoneInputBlockEntity = new RedstoneInputBlockEntity(new BlockPos(0,0,0), RISCJ_blockits.REDSTONE_INPUT_BLOCK.getDefaultState());
        redstoneInputBlockEntity.setController();
    }

    @Test
    void setRedstonePower() {
        redstoneInputBlockEntity.setRedstonePower(15);
        assertEquals("00000F", ((RegisterModel) ((ComputerBlockController) redstoneInputBlockEntity.getController()).getModel()).getValue().getHexadecimalValue());
        redstoneInputBlockEntity.setRedstonePower(0);
        assertEquals("000000", ((RegisterModel) ((ComputerBlockController) redstoneInputBlockEntity.getController()).getModel()).getValue().getHexadecimalValue());
        redstoneInputBlockEntity.setRedstonePower(-99);
        assertEquals("00009D", ((RegisterModel) ((ComputerBlockController) redstoneInputBlockEntity.getController()).getModel()).getValue().getHexadecimalValue());
        redstoneInputBlockEntity.setRedstonePower(5555);
        assertEquals("0015B3", ((RegisterModel) ((ComputerBlockController) redstoneInputBlockEntity.getController()).getModel()).getValue().getHexadecimalValue());
    }

}
