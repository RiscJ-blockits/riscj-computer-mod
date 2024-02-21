package edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static edu.kit.riscjblockits.model.blocks.ClockMode.REALTIME;
import static edu.kit.riscjblockits.model.blocks.ClockMode.STEP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@ExtendWith(TestSetupMain.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SystemClockBlockEntityTest {

    static SystemClockBlockEntity systemClockBlockEntity;

    @BeforeAll
    static void beforeAll() {
        systemClockBlockEntity = new SystemClockBlockEntity(new BlockPos(0,1,0), null);
        systemClockBlockEntity.setController();
    }

    @Order(2)
    @Test
    void createController() {
        assertEquals(BlockControllerType.CLOCK, systemClockBlockEntity.createController().getControllerType());
    }

    @Test
    void writeScreenOpeningData() {
        PacketByteBuf buf = PacketByteBufs.create();
        systemClockBlockEntity.writeScreenOpeningData(null, buf);
        assertEquals(new BlockPos(0,1,0), buf.readBlockPos());
    }

    @Test
    void getDisplayName() {
        Text text = Text.translatable("block.riscj_blockits.system_clock_block");
        assertEquals(text, systemClockBlockEntity.getDisplayName());
    }

    @Order(1)
    @Test
    void getSystemClockSpeed() {
        ((SystemClockModel) ((SystemClockController) systemClockBlockEntity.getController()).getModel()).setClockSpeed(2);
        assertEquals(2, systemClockBlockEntity.getSystemClockSpeed());
        //not really a unit test...
        assertThrowsExactly(IllegalArgumentException.class, () -> ((SystemClockModel) ((SystemClockController) systemClockBlockEntity.getController()).getModel()).setClockSpeed(-1));
        ((SystemClockModel) ((SystemClockController) systemClockBlockEntity.getController()).getModel()).setClockSpeed(0);
        assertEquals(0, systemClockBlockEntity.getSystemClockSpeed());
    }

    @Order(1)
    @Test
    void getSystemClockMode() {
        ((SystemClockModel) ((SystemClockController) systemClockBlockEntity.getController()).getModel()).setClockMode(STEP);
        assertEquals(STEP.toString(), systemClockBlockEntity.getSystemClockMode());
        ((SystemClockModel) ((SystemClockController) systemClockBlockEntity.getController()).getModel()).setClockMode(REALTIME);
        assertEquals(REALTIME.toString(), systemClockBlockEntity.getSystemClockMode());
    }

}
