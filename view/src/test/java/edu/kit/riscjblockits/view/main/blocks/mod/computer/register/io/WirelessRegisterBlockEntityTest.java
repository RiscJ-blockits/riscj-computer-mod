package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.WirelessRegisterBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestSetupMain.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WirelessRegisterBlockEntityTest {

    static WirelessRegisterBlockEntity wirelessRegisterBlockEntity;

    @BeforeAll
    static void beforeAll() {
        wirelessRegisterBlockEntity = new WirelessRegisterBlockEntity(new BlockPos(0,0,0), RISCJ_blockits.WIRELESS_REGISTER_BLOCK.getDefaultState());
        wirelessRegisterBlockEntity.setController();
    }

    @Order(2)
    @Test
    void createController() {
        assertEquals(BlockControllerType.REGISTER, wirelessRegisterBlockEntity.createController().getControllerType());
    }

}
