package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.view.client.TestSetupClient;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    @Disabled
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
    public void translateHexToAscii() {
        TerminalBlockEntity terminalBlockEntity =
            new TerminalBlockEntity(new BlockPos(0, 0, 0), RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        String test1 = "2b";
        String test2 = "2b2bA66666666BA";
        String test3 = "2";
        assertEquals("+", terminalBlockEntity.translateHexToAscii(test1));
        assertEquals("++¦fffk\n", terminalBlockEntity.translateHexToAscii(test2));
        assertEquals("\u0002", terminalBlockEntity.translateHexToAscii(test3));
    }

}