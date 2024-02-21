package edu.kit.riscjblockits.view.main.blocks.mod.programming;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestSetupMain.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProgrammingBlockEntityTest {

    private static ProgrammingBlockEntity programmingBlock;

    @Test
    @Order(1)
    void createController() {
        programmingBlock = new ProgrammingBlockEntity(new BlockPos(0,0, 1), RISCJ_blockits.PROGRAMMING_BLOCK.getDefaultState());
        programmingBlock.setController();
        assertEquals(BlockControllerType.PROGRAMMING, ((BlockController) programmingBlock.getController()).getControllerType());
    }

    @Test
    @Order(2)
    void getDisplayName() {
        assertEquals(Text.translatable("block.riscj_blockits.programming_block"), programmingBlock.getDisplayName());
    }

    @Test
    void assemble() {
        assertDoesNotThrow(() -> programmingBlock.assemble());
    }

    @Order(3)
    @Test
    void setCode() {
        programmingBlock.setCode("ADD 5");
    }

    @Test
    @Order(4)
    void getCode() {
        assertEquals("ADD 5", programmingBlock.getCode());
    }

    @Order(4)
    @Test
    void writeNbt() {
        NbtCompound nbt = new NbtCompound();
        programmingBlock.writeNbt(nbt);
        assertEquals("ADD 5", nbt.getString("code"));
        programmingBlock.setCode("");
        programmingBlock.readNbt(nbt);
        assertEquals("ADD 5", programmingBlock.getCode());
    }

    @Order(4)
    @Test
    void writeScreenOpeningData() {
        PacketByteBuf buf = PacketByteBufs.create();
        programmingBlock.writeScreenOpeningData(null, buf);
        assertEquals(new BlockPos(0,0,1), buf.readBlockPos());
        assertEquals("ADD 5", buf.readString());
    }

}
