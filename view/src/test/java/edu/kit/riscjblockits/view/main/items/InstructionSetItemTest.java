package edu.kit.riscjblockits.view.main.items;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import edu.kit.riscjblockits.view.main.items.instructionset.InstructionSetItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(TestSetupMain.class)
public class InstructionSetItemTest {

    private static InstructionSetItem mimaInstSetItem;

    @BeforeAll
    static void beforeAll() {
        mimaInstSetItem = new InstructionSetItem(new Item.Settings().maxCount(1),
            RISCJ_blockits.class.getClassLoader().getResourceAsStream("instructionSet/instructionSetMIMA.jsonc"));
    }

    @Test
    void inventoryTick() {
        ItemStack itemStack = new ItemStack(mimaInstSetItem, 1);
        World world = mock(World.class);
        Entity entity = mock(Entity.class);
        mimaInstSetItem.inventoryTick(itemStack, world, entity, 0, true);
        assertNotNull(itemStack.getNbt());
    }
}
