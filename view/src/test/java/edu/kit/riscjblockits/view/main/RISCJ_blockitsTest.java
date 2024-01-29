package edu.kit.riscjblockits.view.main;

import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static edu.kit.riscjblockits.view.main.RISCJ_blockits.MODID;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(TestSetupMain.class)
class RISCJ_blockitsTest {

    @Test
    void testRegistryBlocks() {
        //See id all blocks are registered
        assertNotNull(Registries.BLOCK.get(new Identifier(MODID, "alu_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MODID, "bus_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MODID, "control_unit_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MODID, "memory_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MODID, "programming_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MODID, "register_block")));
        assertNotNull(Registries.BLOCK.get(new Identifier(MODID, "system_clock_block")));
    }

    @Test
    void testRegistryItems() {
        //See id all items are registered
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "alu_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "bus_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "control_unit_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "memory_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "programming_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "register_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "system_clock_block")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "googles")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "instruction_set_mima")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "instruction_set_risc")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "manual")));
        assertNotNull(Registries.ITEM.get(new Identifier(MODID, "program")));
    }

    @Test
    void testRegestryEntitys() {
        // Assertions to verify that all block entities are registered
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MODID, "alu_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MODID, "bus_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MODID, "control_unit_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MODID, "memory_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MODID, "programming_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MODID, "register_block_entity")));
        assertNotNull(Registries.BLOCK_ENTITY_TYPE.get(new Identifier(RISCJ_blockits.MODID, "system_clock_block_entity")));
    }

    @Test
    void testRegestryGroup() {
        // Assertions to verify that the item group is registered
        assertNotNull(Registries.ITEM_GROUP.get(new Identifier(RISCJ_blockits.MODID, "computer_components")));
    }

}
