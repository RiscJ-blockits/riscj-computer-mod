package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableComputerController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestSetupMain.class)
class ComputerBlockEntityWithInventoryTest {

    static ComputerBlockEntityWithInventory entity;

    @BeforeAll
    static void beforeAll() {
        entity = new ComputerBlockEntityWithInventory(RISCJ_blockits.TEXT_OUTPUT_BLOCK_ENTITY, new BlockPos(0,0,0), RISCJ_blockits.TEXT_OUTPUT_BLOCK.getDefaultState(), 3) {
            @Override
            protected IUserInputReceivableComputerController createController() {
                return null;
            }
        };
    }

    @Test
    void getItems() {
        entity.items.set(0, new ItemStack(RISCJ_blockits.MANUAL_ITEM));
        assertEquals(1, entity.getItems().get(0).getCount());
        entity.items.clear();
        assertEquals(0, entity.getItems().get(0).getCount());
    }

    @Test
    void inventoryChanged() {
        entity.inventoryChanged();
        assertEquals(3, entity.getItems().size());
    }

    @Test
    void readNbt() {
        entity.items.set(0, new ItemStack(RISCJ_blockits.TEXT_OUTPUT_BLOCK));
        NbtCompound nbt = new NbtCompound();
        entity.writeNbt(nbt);
        assertEquals(1, nbt.getKeys().size());
        ComputerBlockEntityWithInventory entity2 = new ComputerBlockEntityWithInventory(RISCJ_blockits.TEXT_OUTPUT_BLOCK_ENTITY, new BlockPos(0,0,0), RISCJ_blockits.TEXT_OUTPUT_BLOCK.getDefaultState(), 3) {
            @Override
            protected IUserInputReceivableComputerController createController() {
                return null;
            }
        };
        entity2.readNbt(nbt);
        assertEquals(1, entity2.getItems().get(0).getCount());
    }

}
