package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.io.WirelessRegisterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Order(1)
    @Test
    @Disabled("ToDo: i dont know why it does not work")
    void syncRegister() {
        World world = mock(World.class);
        when(world.isClient()).thenReturn(false);
        wirelessRegisterBlockEntity.setWorld(world);
        when(world.getBlockEntity(new BlockPos(0,0,0))).thenReturn(wirelessRegisterBlockEntity);
        WirelessRegisterBlockEntity wirelessRegisterBlockEntity2 = new WirelessRegisterBlockEntity(new BlockPos(1,0,0), RISCJ_blockits.WIRELESS_REGISTER_BLOCK.getDefaultState());
        wirelessRegisterBlockEntity2.setController();
        wirelessRegisterBlockEntity2.setWorld(world);
        when(world.getBlockEntity(new BlockPos(1,0,0))).thenReturn(wirelessRegisterBlockEntity2);
        //setup ready
        ((WirelessRegisterController) wirelessRegisterBlockEntity.getController()).setWirelessNeighbourPosition(new BlockPosition(1,0,0));
        ((WirelessRegisterController) wirelessRegisterBlockEntity2.getController()).setNewValue(Value.fromHex("2b",2));
        wirelessRegisterBlockEntity.syncRegister();
        wirelessRegisterBlockEntity2.syncRegister();
        ((WirelessRegisterController) wirelessRegisterBlockEntity2.getController()).setNewValue(Value.fromHex("4b",2));
        //
        assertEquals(Value.fromHex("2b",2), ((WirelessRegisterController) wirelessRegisterBlockEntity.getController()).getValue());
    }

}
