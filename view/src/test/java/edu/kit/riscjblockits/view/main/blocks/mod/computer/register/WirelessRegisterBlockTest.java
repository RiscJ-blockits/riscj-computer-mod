package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestSetupMain.class)
class WirelessRegisterBlockTest {

    @Test
    void createWirelessRegisterBlock() {
        WirelessRegisterBlock wirelessRegisterBlock = new WirelessRegisterBlock();
        assertEquals(0.0F, wirelessRegisterBlock.getHardness());
    }

    @Test
    void createBlockEntity() {
        WirelessRegisterBlock wirelessRegisterBlock = new WirelessRegisterBlock();
        WirelessRegisterBlockEntity wirelessRegisterBlockEntity = (WirelessRegisterBlockEntity) wirelessRegisterBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert wirelessRegisterBlockEntity != null;
        assertFalse(wirelessRegisterBlockEntity.isActive());
        assertFalse(wirelessRegisterBlockEntity.hasWorld());
    }

    @Test
    void onUse() {
        WirelessRegisterBlock wirelessRegisterBlock = new WirelessRegisterBlock();
        World world = mock(World.class);
        when(world.isClient()).thenReturn(false);
        PlayerEntity player = mock(PlayerEntity.class);
        when(player.getStackInHand(Hand.MAIN_HAND)).thenReturn(new ItemStack(RISCJ_blockits.MANUAL_ITEM));
        WirelessRegisterBlockEntity wirelessRegisterBlockEntity = (WirelessRegisterBlockEntity) wirelessRegisterBlock.createBlockEntity(new BlockPos(0,0,0), null);
        when(world.getBlockEntity(new BlockPos(0,0,0))).thenReturn(wirelessRegisterBlockEntity);
        assertEquals(ActionResult.SUCCESS, wirelessRegisterBlock.onUse(null, world, new BlockPos(0,0,0), player, Hand.MAIN_HAND, null));
        when(world.isClient()).thenReturn(true);
        assertEquals(ActionResult.SUCCESS, wirelessRegisterBlock.onUse(null, world, new BlockPos(0,0,0), player, Hand.MAIN_HAND, null));
    }

}
