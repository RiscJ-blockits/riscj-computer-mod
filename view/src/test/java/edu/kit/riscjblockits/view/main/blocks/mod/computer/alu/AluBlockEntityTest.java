package edu.kit.riscjblockits.view.main.blocks.mod.computer.alu;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(TestSetupMain.class)
class AluBlockEntityTest {

    private static AluBlockEntity aluBlockEntity;

    @BeforeAll
    static void beforeAll() {
        aluBlockEntity = new AluBlockEntity(new BlockPos(0,0, 0), RISCJ_blockits.ALU_BLOCK.getDefaultState());
        aluBlockEntity.setController();
        World world = mock(World.class);
        aluBlockEntity.setWorld(world);
        ((ComputerBlockController) aluBlockEntity.getController()).startClustering(new BlockPosition(0,0,0));
    }

    @Test
    void createController() {
        assertEquals(BlockControllerType.ALU, ((ComputerBlockController) aluBlockEntity.createController()).getControllerType());
    }

}
