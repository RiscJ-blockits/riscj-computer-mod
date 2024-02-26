package edu.kit.riscjblockits.view.main.blocks.mod.computer.bus;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;

@ExtendWith(TestSetupMain.class)
class BusBlockEntityTest {

    private static BusBlockEntity busEntity;

    @BeforeAll
    static void beforeAll() {
        busEntity = new BusBlockEntity(new BlockPos(0,0, 0), RISCJ_blockits.BUS_BLOCK.getDefaultState());
        busEntity.setController();
        World world = mock(World.class);
        busEntity.setWorld(world);
        ((ComputerBlockController) busEntity.getController()).startClustering(new BlockPosition(0,0,0));
    }
    

}
