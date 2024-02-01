package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControlUnitBlockEntityTest {

    ControlUnitBlockEntity controlUnitBlockEntity;

    @BeforeAll
    static void setUp() {
        SharedConstants.createGameVersion();
        Bootstrap.initialize();
    }

    @Test
    void testControlUnit1() {
        controlUnitBlockEntity = new ControlUnitBlockEntity(new BlockPos(0,0, 0), RISCJ_blockits.CONTROL_UNIT_BLOCK.getDefaultState());
        controlUnitBlockEntity.setController();
        assertEquals(BlockControllerType.CONTROL_UNIT, ((ComputerBlockController) controlUnitBlockEntity.getController()).getControllerType());
    }

}