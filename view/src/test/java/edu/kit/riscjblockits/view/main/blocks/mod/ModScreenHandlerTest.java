package edu.kit.riscjblockits.view.main.blocks.mod;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.entity.player.PlayerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(TestSetupMain.class)
class ModScreenHandlerTest {

    private static ModScreenHandler modScreenHandler;
    private static ModBlockEntity modBlockEntity;

    private  void init() {
        modBlockEntity = mock(ModBlockEntity.class);
        modScreenHandler = new ModScreenHandler(null, 0, modBlockEntity) {};
    }

    @Test
    void quickMove() {
        init();
        assertTrue(modScreenHandler.quickMove(mock(PlayerEntity.class), 0).isEmpty());
    }

    @Test
    void canUse() {
        init();
        assertTrue(modScreenHandler.canUse(mock(PlayerEntity.class)));
    }

    @Test
    void getBlockEntity() {
        init();
        assertSame(modScreenHandler.getBlockEntity(), modBlockEntity);
    }

}
