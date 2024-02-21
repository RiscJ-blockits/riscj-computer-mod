package edu.kit.riscjblockits.view.main.blocks.mod.computer.memory;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.model.blocks.MemoryModel;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

@ExtendWith(TestSetupMain.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemoryBlockEntityTest {

    private static MemoryBlockEntity memoryBlockEntity;

    @BeforeAll
    static void beforeAll() {
        memoryBlockEntity = new MemoryBlockEntity(new BlockPos(0,0, 0), RISCJ_blockits.MEMORY_BLOCK.getDefaultState());
        memoryBlockEntity.setController();
        World world = mock(World.class);
        memoryBlockEntity.setWorld(world);
    }


    @Test
    void createController() {
        assertEquals(BlockControllerType.MEMORY, memoryBlockEntity.createController().getControllerType());
    }

    @Test
    void writeScreenOpeningData() {
        PacketByteBuf buf = PacketByteBufs.create();
        memoryBlockEntity.writeScreenOpeningData(null, buf);
        assertEquals(new BlockPos(0,0,0), buf.readBlockPos());
    }

    @Test
    void getDisplayName() {
        assertEquals(Text.translatable("block.riscj_blockits.memory_block"), memoryBlockEntity.getDisplayName());
    }

    @Test
    void inventoryChanged() {
        memoryBlockEntity.inventoryChanged();
        assertFalse(((MemoryModel) ((ComputerBlockController)  memoryBlockEntity.getController()).getModel()).isMemorySet());
        memoryBlockEntity.setStack(0, new ItemStack(RISCJ_blockits.PROGRAM_ITEM));
        assertFalse(((MemoryModel) ((ComputerBlockController)  memoryBlockEntity.getController()).getModel()).isMemorySet());
        memoryBlockEntity.inventoryChanged();
        assertFalse(((MemoryModel) ((ComputerBlockController)  memoryBlockEntity.getController()).getModel()).isMemorySet());
    }

    @Test
    void setMemoryQueryLine() {
        memoryBlockEntity.setMemoryQueryLine(0);
        memoryBlockEntity.setMemoryQueryLine(100);
        memoryBlockEntity.setMemoryQueryLine(-45);  //ToDo this should probably not be possible
    }

}
