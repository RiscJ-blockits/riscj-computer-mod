package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.view.client.TestSetupClient;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestSetupClient.class)
class ComputerBlockEntityTest {

//    @BeforeAll
//    static void setUp() {
//        SharedConstants.createGameVersion();
//        Bootstrap.initialize();
//        RISCJ_blockitsClient riscJ_blockitsClient = new RISCJ_blockitsClient();
//        riscJ_blockitsClient.onInitializeClient();
//    }

    @Test
    void updateUIClientSide() {
        ComputerBlockEntity entity = new RegisterBlockEntity(new BlockPos(0,0, 0), RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        World world = mock(World.class);
        when(world.isClient()).thenReturn(true);
        ComputerBlockEntity.tick(world, new BlockPos(0,0, 0), RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), entity);
        //should not crash on the client
    }

}