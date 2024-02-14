package edu.kit.riscjblockits.view.main;

import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static edu.kit.riscjblockits.view.main.RISCJ_blockits.MOD_ID;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(TestSetupMain.class)
class RISCJ_blockitsTest {

    @Test
    void testRegistryBlocks() {
        //See id all blocks are registered
        assertNotNull(Registries.BLOCK.get(new Identifier(MOD_ID, "alu_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MOD_ID, "bus_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MOD_ID, "control_unit_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MOD_ID, "memory_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MOD_ID, "programming_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MOD_ID, "register_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MOD_ID, "system_clock_block")));
    }

    @Test
    void testRegistryItems() {
        //See id all items are registered
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "alu_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "bus_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "control_unit_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "memory_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "programming_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "register_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "system_clock_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "googles")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "instruction_set_mima")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "instruction_set_risc")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "manual")));
        assertNotNull(Registries.ITEM.get(new Identifier(MOD_ID, "program")));
    }

    @Test
    void testRegestryEntitys() {
        // Assertions to verify that all block entities are registered
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MOD_ID, "alu_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MOD_ID, "bus_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MOD_ID, "control_unit_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MOD_ID, "memory_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MOD_ID, "programming_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MOD_ID, "register_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MOD_ID, "system_clock_block_entity")));
    }

    @Test
    void testRegestryGroup() {
        // Assertions to verify that the item group is registered
        assertNotNull(Registries.ITEM_GROUP.get(new Identifier(RISCJ_blockits.MOD_ID, "computer_components")));
    }

}
