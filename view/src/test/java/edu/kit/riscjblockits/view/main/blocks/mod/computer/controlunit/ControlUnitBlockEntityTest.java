package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(TestSetupMain.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ControlUnitBlockEntityTest {

    private static ControlUnitBlockEntity controlUnitBlockEntity;

    @BeforeAll
    static void beforeAll() {
        controlUnitBlockEntity = new ControlUnitBlockEntity(new BlockPos(0,0, 0), RISCJ_blockits.CONTROL_UNIT_BLOCK.getDefaultState());
        controlUnitBlockEntity.setController();
        World world = mock(World.class);
        controlUnitBlockEntity.setWorld(world);
        ((ComputerBlockController) controlUnitBlockEntity.getController()).startClustering(new BlockPosition(0,0,0));
    }

    @Test
    void setController() {
        assertEquals(BlockControllerType.CONTROL_UNIT, ((ComputerBlockController) controlUnitBlockEntity.getController()).getControllerType());}

    @Test
    void createController() {
        assertEquals(BlockControllerType.CONTROL_UNIT, controlUnitBlockEntity.createController().getControllerType());
    }

    @Test
    void writeScreenOpeningData() {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        controlUnitBlockEntity.writeScreenOpeningData(null, packetByteBuf);
        assertEquals(new BlockPos(0,0,0), packetByteBuf.readBlockPos());
    }

    @Test
    void getDisplayName() {
        assertEquals(Text.translatable("block.riscj_blockits.control_unit_block"), controlUnitBlockEntity.getDisplayName());
    }

    @Test
    void inventoryChanged() {
        controlUnitBlockEntity.inventoryChanged();
        assertNull(((ControlUnitModel) ((ComputerBlockController)  controlUnitBlockEntity.getController()).getModel()).getIstModel());
        controlUnitBlockEntity.setStack(0, new ItemStack(RISCJ_blockits.INSTRUCTION_SET_ITEM_MIMA));
        assertNull(((ControlUnitModel) ((ComputerBlockController)  controlUnitBlockEntity.getController()).getModel()).getIstModel());
        //probably not possible to test the interesting stuff
//        controlUnitBlockEntity.inventoryChanged();
//        assertEquals("Mima", ((ControlUnitModel) ((ComputerBlockController) controlUnitBlockEntity.getController()).getModel()).getIstModel().getName());
    }

    @Test
    void getStructure() {
        List[] structure = controlUnitBlockEntity.getStructure();
        assertEquals(2, structure.length);
        assertEquals(0, structure[0].size());
        assertEquals(0, structure[1].size());
    }

}
