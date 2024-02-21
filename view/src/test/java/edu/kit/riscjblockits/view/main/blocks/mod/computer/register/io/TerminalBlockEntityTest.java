package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestSetupClient.class)
@ExtendWith(TestSetupMain.class)
class TerminalBlockEntityTest {

    static TerminalBlockEntity terminalBlockEntity;

    @BeforeAll
    static void beforeAll() {
        terminalBlockEntity = new TerminalBlockEntity(new BlockPos(10, 0, 0), RISCJ_blockits.TEXT_OUTPUT_BLOCK.getDefaultState());
        terminalBlockEntity.setController();
    }

    @Disabled("fails because of refactoring")
    @Test
    void readNbt() {
        NbtCompound nbt = new NbtCompound();
        NbtCompound subNbt = new NbtCompound();
        subNbt.putString(REGISTER_VALUE, "2b");
        subNbt.putString(REGISTER_WORD_LENGTH, "5");
        nbt.put(MOD_DATA, subNbt);
        World world = mock(World.class);
        when(world.isClient()).thenReturn(true);
        //world.isClient is still false why??
        TerminalBlockEntity terminalBlockEntity = new TerminalBlockEntity(new BlockPos(0,0, 0), RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        terminalBlockEntity.setWorld(world);
        terminalBlockEntity.readNbt(nbt);
        assertEquals("+", terminalBlockEntity.getDisplayedString());
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

    @Disabled("ToDo")
    @Test
    void writeNbt() {
        NbtCompound nbt = new NbtCompound();
        terminalBlockEntity.writeNbt(nbt);
        assertEquals(0, nbt.getKeys().size());
    }

}
