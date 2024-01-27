package edu.kit.riscjblockits.view.main.blocks.mod;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestSetupMain.class)
class ModBlockEntityTest {

    @Test
    void getControllerOnPlaced() {
        BlockPos pos = new BlockPos(0,0,0);
        World world = mock(World.class);

        ModBlock block = new RegisterBlock();
        ModBlockEntity entity = (ModBlockEntity) block.createBlockEntity(pos, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());

        when(world.getBlockEntity(pos)).thenReturn(entity);
        block.onPlaced(world, pos, RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), null, null);

        assert entity != null;
        assertEquals(BlockControllerType.REGISTER, ((BlockController) entity.getController()).getControllerType());
    }

    @Test
    void getControllerOnNbt() {
        BlockPos pos = new BlockPos(0,0,0);
        World world = mock(World.class);

        ModBlock block = new RegisterBlock();
        ModBlockEntity entity = (ModBlockEntity) block.createBlockEntity(pos, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());

        when(world.getBlockEntity(pos)).thenReturn(entity);
        NbtCompound nbt = mock(NbtCompound.class);
        assert entity != null;
        entity.readNbt(nbt);
        assertEquals(BlockControllerType.REGISTER, ((BlockController) entity.getController()).getControllerType());
    }

}
