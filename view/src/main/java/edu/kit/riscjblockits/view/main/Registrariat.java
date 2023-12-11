package edu.kit.riscjblockits.view.main;

import edu.kit.riscjblockits.view.main.blocks.TestBlock;
import edu.kit.riscjblockits.view.main.blocks.TestBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.TestBlockItem;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Registrariat {
    public static final String MODID = "riscj_blockits";
    public static final Block TEST_BLOCK = new TestBlock();
    public static final TestBlockItem TEST_BLOCK_ITEM = new TestBlockItem(TEST_BLOCK);
    public static BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY;

    public Registrariat() {
        //Block für das Spiel registrieren
        Registry.register(Registries.BLOCK, new Identifier(MODID, "example_block"), TEST_BLOCK);
        Registry.register(Registries.ITEM, new Identifier(MODID, "example_block"), TEST_BLOCK_ITEM);
        //BlockEntity
        TEST_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "demo_block_entity"),
            FabricBlockEntityTypeBuilder.create(TestBlockEntity::new, TEST_BLOCK).build());
    }
}
