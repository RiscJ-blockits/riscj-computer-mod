package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.client.TestSetupClient;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestSetupClient.class)
@ExtendWith(TestSetupMain.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TerminalBlockEntityTest {

    static TerminalBlockEntity terminalBlockEntity;

    @BeforeAll
    static void beforeAll() {
        terminalBlockEntity = new TerminalBlockEntity(new BlockPos(10, 0, 0), RISCJ_blockits.TEXT_OUTPUT_BLOCK.getDefaultState());
        terminalBlockEntity.setController();
    }

    @Test
    void translateHexToAscii() {
        TerminalBlockEntity terminalBlockEntity =
            new TerminalBlockEntity(new BlockPos(0, 0, 0), RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        String test1 = "2b";
        String test2 = "2b2bA66666666BA";
        String test3 = "2";
        assertEquals("+", terminalBlockEntity.translateHexToAscii(test1));
        assertEquals("++¦fffk\n", terminalBlockEntity.translateHexToAscii(test2));
        assertEquals("\u0002", terminalBlockEntity.translateHexToAscii(test3));
    }

    @Order(2)
    @Test
    void createController() {
        assertEquals(BlockControllerType.REGISTER, terminalBlockEntity.createController().getControllerType());
    }

    @Test
    void writeScreenOpeningData() {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        terminalBlockEntity.writeScreenOpeningData(null, packetByteBuf);
        assertEquals(10, packetByteBuf.readBlockPos().getX());
    }

    @Test
    void getDisplayName() {
        assertEquals(Text.translatable("block.riscj_blockits.text_output_block"), terminalBlockEntity.getDisplayName());
    }

    @Order(1)
    @Test
    void getDisplayedString() {
        World world = mock(World.class);
        when(world.isClient()).thenReturn(true);
        terminalBlockEntity.setWorld(world);
        //
        assertEquals("", terminalBlockEntity.getDisplayedString());
        NbtCompound nbt = new NbtCompound();
        NbtCompound subNbt = new NbtCompound();
        subNbt.putString(REGISTER_VALUE, "44");
        subNbt.putString(REGISTER_WORD_LENGTH, "2");
        nbt.put(MOD_DATA, subNbt);
        terminalBlockEntity.readNbt(nbt);
        //world.isClient is still false why?? FixMe
        //assertEquals("D", terminalBlockEntity.getDisplayedString());
    }

    @Test
    void writeNbt() {
        NbtCompound nbt = new NbtCompound();
        terminalBlockEntity.writeNbt(nbt);
        assertEquals(1, nbt.getKeys().size());
        NbtCompound subNbt = nbt.getCompound(MOD_DATA);
        assertEquals(9, subNbt.getKeys().size());
    }

    @Order(1)
    @Test
    void onBroken() {
        World world = mock(World.class);
        when(world.getBlockEntity(new BlockPos(0, 0, 0))).thenReturn(terminalBlockEntity);
        terminalBlockEntity.setWorld(world);
        ((ComputerBlockController) terminalBlockEntity.getController()).startClustering(new BlockPosition(0, 0, 0));
        assertEquals(3, ((ComputerBlockController) terminalBlockEntity.getController()).getClusterHandler().getBlocks().size());
        assertEquals(1, ((ComputerBlockController) terminalBlockEntity.getController()).getClusterHandler().getBusSystemModel().getBusGraph().size());
        terminalBlockEntity.onBroken();
        assertEquals(0, ((ComputerBlockController) terminalBlockEntity.getController()).getClusterHandler().getBusSystemModel().getBusGraph().size());
    }

}
