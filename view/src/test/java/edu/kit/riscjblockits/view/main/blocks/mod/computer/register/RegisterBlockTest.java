package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestSetupMain.class)
class RegisterBlockTest {

    @Test
    void createRegisterBlock() {
        RegisterBlock registerBlock = new RegisterBlock();
        assertEquals(0.0F, registerBlock.getHardness());
        registerBlock = new RegisterBlock(FabricBlockSettings.create().hardness(1.0F));
        assertEquals(1.0F, registerBlock.getHardness());
    }

    @Test
    void createBlockEntity() {
        RegisterBlock registerBlock = new RegisterBlock();
        RegisterBlockEntity registerBlockEntity = (RegisterBlockEntity) registerBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert registerBlockEntity != null;
        assertFalse(registerBlockEntity.hasWorld());
        assertFalse(registerBlockEntity.isActive());
    }

    @Test
    void onUse() {
        RegisterBlock registerBlock = new RegisterBlock();
        World world = mock(World.class);
        when(world.isClient()).thenReturn(false);
        RegisterBlockEntity registerBlockEntity = (RegisterBlockEntity) registerBlock.createBlockEntity(new BlockPos(0,0,0), null);
        when(world.getBlockEntity(new BlockPos(0,0,0))).thenReturn(registerBlockEntity);
        assertEquals(ActionResult.SUCCESS, registerBlock.onUse(null, world, new BlockPos(0,0,0), mock(PlayerEntity.class), null, null));
    }

}
