package edu.kit.riscjblockits.view.main;

import edu.kit.riscjblockits.view.main.blocks.alu.AluBlock;
import edu.kit.riscjblockits.view.main.blocks.alu.AluBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.bus.BusBlock;
import edu.kit.riscjblockits.view.main.blocks.bus.BusBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.controlunit.ControlUnitBlock;
import edu.kit.riscjblockits.view.main.blocks.controlunit.ControlUnitBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.memory.MemoryBlock;
import edu.kit.riscjblockits.view.main.blocks.memory.MemoryBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.programming.ProgrammingBlock;
import edu.kit.riscjblockits.view.main.blocks.programming.ProgrammingBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.programming.ProgrammingScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.register.RegisterBlock;
import edu.kit.riscjblockits.view.main.blocks.register.RegisterBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.systemclock.SystemClockBlock;
import edu.kit.riscjblockits.view.main.blocks.systemclock.SystemClockBlockEntity;
import edu.kit.riscjblockits.view.main.items.goggles.GogglesItem;
import edu.kit.riscjblockits.view.main.items.program.ProgramItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RISCJ_blockits implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("riscj_blockits");
	public static final String MODID = "riscj_blockits";
	private static final float BLOCK_STRENGTH = 1.0f;

	// define Blocks
	public static final Block ALU_BLOCK = new AluBlock(FabricBlockSettings.create().strength(BLOCK_STRENGTH));
	public static final Block BUS_BLOCK = new BusBlock(FabricBlockSettings.create().strength(BLOCK_STRENGTH));
	public static final Block CONTROL_UNIT_BLOCK = new ControlUnitBlock(FabricBlockSettings.create().strength(BLOCK_STRENGTH));
	public static final Block MEMORY_BLOCK = new MemoryBlock(FabricBlockSettings.create().strength(BLOCK_STRENGTH));
	public static final Block PROGRAMMING_BLOCK = new ProgrammingBlock(FabricBlockSettings.create().strength(BLOCK_STRENGTH));
	public static final Block REGISTER_BLOCK = new RegisterBlock(FabricBlockSettings.create().strength(BLOCK_STRENGTH));
	public static final Block SYSTEM_CLOCK_BLOCK = new SystemClockBlock(FabricBlockSettings.create().strength(BLOCK_STRENGTH));

	// define Block-Items
	public static final BlockItem ALU_BLOCK_ITEM = new BlockItem(ALU_BLOCK, new Item.Settings());
	public static final BlockItem BUS_BLOCK_ITEM = new BlockItem(BUS_BLOCK, new Item.Settings());
	public static final BlockItem CONTROL_UNIT_BLOCK_ITEM = new BlockItem(CONTROL_UNIT_BLOCK, new Item.Settings());
	public static final BlockItem MEMORY_BLOCK_ITEM = new BlockItem(MEMORY_BLOCK, new Item.Settings());
	public static final BlockItem PROGRAMMING_BLOCK_ITEM = new BlockItem(PROGRAMMING_BLOCK, new Item.Settings());
	public static final BlockItem REGISTER_BLOCK_ITEM = new BlockItem(REGISTER_BLOCK, new Item.Settings());
	public static final BlockItem SYSTEM_CLOCK_BLOCK_ITEM = new BlockItem(SYSTEM_CLOCK_BLOCK, new Item.Settings());

	// define Block-Entities
	public static BlockEntityType<AluBlockEntity> ALU_BLOCK_ENTITY;
	public static BlockEntityType<BusBlockEntity> BUS_BLOCK_ENTITY;
	public static BlockEntityType<ControlUnitBlockEntity> CONTROL_UNIT_BLOCK_ENTITY;
	public static BlockEntityType<MemoryBlockEntity> MEMORY_BLOCK_ENTITY;
	public static BlockEntityType<ProgrammingBlockEntity> PROGRAMMING_BLOCK_ENTITY;
	public static BlockEntityType<RegisterBlockEntity> REGISTER_BLOCK_ENTITY;
	public static BlockEntityType<SystemClockBlockEntity> SYSTEM_CLOCK_BLOCK_ENTITY;

	// define Items
	public static final Item PROGRAM_ITEM = new ProgramItem(new Item.Settings().maxCount(1));
	public static final Item GOGGLES_ITEM = new GogglesItem(new Item.Settings().maxCount(1));
	public static final Item INSTRUCTION_SET_ITEM = new GogglesItem(new Item.Settings().maxCount(1));
	public static final Item MANUAL_ITEM = new GogglesItem(new Item.Settings().maxCount(1));


	public static ScreenHandlerType<ProgrammingScreenHandler> PROGRAMMING_SCREEN_HANDLER;



	@Override
	public void onInitialize() {
		// register ScreenHandlers
		PROGRAMMING_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(MODID, "programming_screen"), ProgrammingScreenHandler::new);


		// register Blocks
		Registry.register(Registries.BLOCK, new Identifier(MODID, "alu_block"), ALU_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "bus_block"), BUS_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "control_unit_block"), CONTROL_UNIT_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "memory_block"), MEMORY_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "programming_block"), PROGRAMMING_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "register_block"), REGISTER_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "system_clock_block"), SYSTEM_CLOCK_BLOCK);

		// register Block-Items
		Registry.register(Registries.ITEM, new Identifier(MODID, "alu_block"), ALU_BLOCK_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MODID, "bus_block"), BUS_BLOCK_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MODID, "control_unit_block"), CONTROL_UNIT_BLOCK_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MODID, "memory_block"), MEMORY_BLOCK_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MODID, "programming_block"), PROGRAMMING_BLOCK_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MODID, "register_block"), REGISTER_BLOCK_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MODID, "system_clock_block"), SYSTEM_CLOCK_BLOCK_ITEM);

		// register Items
		Registry.register(Registries.ITEM, new Identifier(MODID, "goggles"), GOGGLES_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MODID, "instruction_set"), INSTRUCTION_SET_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MODID, "manual"), MANUAL_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MODID, "program"), PROGRAM_ITEM);

		// register Block-Entities
		ALU_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "alu_block_entity"),
				FabricBlockEntityTypeBuilder.create(AluBlockEntity::new, ALU_BLOCK).build());
		BUS_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "bus_block_entity"),
				FabricBlockEntityTypeBuilder.create(BusBlockEntity::new, BUS_BLOCK).build());
		CONTROL_UNIT_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "control_unit_block_entity"),
				FabricBlockEntityTypeBuilder.create(ControlUnitBlockEntity::new, CONTROL_UNIT_BLOCK).build());
		MEMORY_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "memory_block_entity"),
				FabricBlockEntityTypeBuilder.create(MemoryBlockEntity::new, MEMORY_BLOCK).build());
		PROGRAMMING_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "programming_block_entity"),
				FabricBlockEntityTypeBuilder.create(ProgrammingBlockEntity::new, PROGRAMMING_BLOCK).build());
		REGISTER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "register_block_entity"),
				FabricBlockEntityTypeBuilder.create(RegisterBlockEntity::new, REGISTER_BLOCK).build());
		SYSTEM_CLOCK_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "system_clock_block_entity"),
				FabricBlockEntityTypeBuilder.create(SystemClockBlockEntity::new, SYSTEM_CLOCK_BLOCK).build());

		// register the Item-Group
		Registry.register(Registries.ITEM_GROUP, new Identifier(MODID, "computer_components"), ITEM_GROUP);
	}

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(ALU_BLOCK_ITEM))
			.displayName(Text.translatable("itemGroup.riscj_blockits.computer_components"))
			.entries((context, entries) -> {
				entries.add(ALU_BLOCK_ITEM);
				entries.add(BUS_BLOCK_ITEM);
				entries.add(CONTROL_UNIT_BLOCK_ITEM);
				entries.add(MEMORY_BLOCK_ITEM);
				entries.add(PROGRAMMING_BLOCK_ITEM);
				entries.add(REGISTER_BLOCK_ITEM);
				entries.add(SYSTEM_CLOCK_BLOCK_ITEM);

				entries.add(INSTRUCTION_SET_ITEM);
				entries.add(GOGGLES_ITEM);
				entries.add(PROGRAM_ITEM);
				entries.add(MANUAL_ITEM);
			})
			.build();
}