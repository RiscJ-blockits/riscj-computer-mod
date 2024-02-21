package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestSetupMain.class)
class RegisterBlockEntityTest {

    static RegisterBlockEntity registerBlockEntity;

    @BeforeAll
    static void beforeAll() {
        registerBlockEntity = new RegisterBlockEntity(new BlockPos(10, 0, 0), RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        registerBlockEntity.setController();
    }

    @Order(2)
    @Test
    void createController() {
        assertEquals(BlockControllerType.REGISTER, registerBlockEntity.createController().getControllerType());
    }

    @Test
    void writeScreenOpeningData() {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        registerBlockEntity.writeScreenOpeningData(null, packetByteBuf);
        assertEquals(new BlockPos(10,0,0), packetByteBuf.readBlockPos());
    }

    @Order(1)
    @Test
    void getDisplayName() {
        Text text = Text.literal("Register: " + RegisterModel.UNASSIGNED_REGISTER);
        assertEquals(text, registerBlockEntity.getDisplayName());
        ((RegisterModel) ((RegisterController) registerBlockEntity.getController()).getModel()).setRegisterType("test");
        text = Text.literal("Register: test");
        assertEquals(text, registerBlockEntity.getDisplayName());
    }

}
