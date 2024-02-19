package edu.kit.riscjblockits.view.main.blocks.mod;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(TestSetupMain.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ImplementedInventoryTest {

    private static ImplementedInventory inventory;
    private void init() {
        inventory = ImplementedInventory.of(DefaultedList.ofSize(9, ItemStack.EMPTY));
    }


    @Test
    @Order(1)
    void canPlayerUse() {
        init();
        assertTrue(inventory.canPlayerUse(mock(PlayerEntity.class)));
    }

    @Test
    void size() {
        assertEquals(9, inventory.size());
    }

    @Test
    @Order(2)
    void isEmpty() {
        assertTrue(inventory.isEmpty());
    }

    @Test
    @Order(3)
    void getStack() {
        setStack();
        assertEquals(RISCJ_blockits.MANUAL_ITEM, inventory.getStack(0).getItem());
    }

    @Test
    @Order(4)
    void removeStack() {
        inventory.removeStack(0);
        assertTrue(inventory.isEmpty());

    }

    void setStack() {
        inventory.setStack(0, new ItemStack(RISCJ_blockits.MANUAL_ITEM));
    }

    @Test
    void markDirty() {
        inventory.markDirty();
    }

}