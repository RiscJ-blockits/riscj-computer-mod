package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeAll;
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
class RedstoneOutputBlockEntityTest {

    static RedstoneOutputBlockEntity redstoneOutputBlockEntity;

    @BeforeAll
    static void beforeAll() {
        redstoneOutputBlockEntity = new RedstoneOutputBlockEntity(new BlockPos(0, 0, 0), RISCJ_blockits.REDSTONE_OUTPUT_BLOCK.getDefaultState());
        redstoneOutputBlockEntity.setController();
    }

    @Order(1)
    @Test
    void getRedstonePower() {
        assertEquals(0, redstoneOutputBlockEntity.getRedstonePower());
    }

    @Test
    void updateUI() {
        World world = mock(World.class);
        when(world.getBlockEntity(new BlockPos(0, 0, 0))).thenReturn(redstoneOutputBlockEntity);
        when(world.getBlockState(new BlockPos(0, 0, 0))).thenReturn(RISCJ_blockits.REDSTONE_OUTPUT_BLOCK.getDefaultState());
        redstoneOutputBlockEntity.setWorld(world);
        redstoneOutputBlockEntity.updateUI();
        assertEquals(0, redstoneOutputBlockEntity.getRedstonePower());
        ((RegisterController) redstoneOutputBlockEntity.getController()).setNewValue(Value.fromHex("08",2));
        assertEquals(0, redstoneOutputBlockEntity.getRedstonePower());
        redstoneOutputBlockEntity.updateUI();
        assertEquals(8, redstoneOutputBlockEntity.getRedstonePower());
        //test edge case
        ((RegisterController) redstoneOutputBlockEntity.getController()).setNewValue(Value.fromHex("0200",4));
        redstoneOutputBlockEntity.updateUI();
        assertEquals(15, redstoneOutputBlockEntity.getRedstonePower());
    }

}
