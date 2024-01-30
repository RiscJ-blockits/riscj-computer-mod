package edu.kit.riscjblockits.view.main;

import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.alu.AluBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.alu.AluBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.bus.BusBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.bus.BusBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.memory.MemoryBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.memory.MemoryBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.memory.MemoryScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock.SystemClockBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock.SystemClockBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock.SystemClockScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingScreenHandler;
import edu.kit.riscjblockits.view.main.items.goggles.GogglesItem;
import edu.kit.riscjblockits.view.main.items.instructionset.InstructionSetItem;
import edu.kit.riscjblockits.view.main.items.manual.ManualItem;
import edu.kit.riscjblockits.view.main.items.program.ProgramItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;

/**
 * This class is the main class of the mod.
 * It is used to register all blocks, items, block-entities and item-groups.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class RISCJ_blockits implements ModInitializer {

	/**
	 * The mod-id of this mod. It is used by Minecraft and Fabric Loom to correctly identify parts of this mod.
	 */
	public static final String MODID = "riscj_blockits";
	/**
	 * Logger for writing text to the console and the log file.
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	/**
	 * This attribute defines the active state of a block.
	 */
	public static final BooleanProperty ACTIVE_STATE_PROPERTY = BooleanProperty.of("active");

	// define Blocks
	/**
	 * This attribute defines all ALU blocks.
	 * An ALU-Block with default settings.
	 */
	public static final Block ALU_BLOCK = new AluBlock();
	/**
	 * This attribute defines all Bus blocks.
	 * A Bus-Block with default settings.
	 */
	public static final Block BUS_BLOCK = new BusBlock();
	/**
	 * This attribute defines all Control-Unit blocks.
	 * A Control-Unit-Block with default settings.
	 */
	public static final Block CONTROL_UNIT_BLOCK = new ControlUnitBlock();
	/**
	 * This attribute defines all Memory blocks.
	 * A Memory-Block with default settings.
	 */
	public static final Block MEMORY_BLOCK = new MemoryBlock();
	/**
	 * This attribute defines all Programming blocks.
	 * A Programming-Block with default settings.
	 */
	public static final Block PROGRAMMING_BLOCK = new ProgrammingBlock();
	/**
	 * This attribute defines all Register blocks.
	 * A Register-Block with default settings.
	 */
	public static final Block REGISTER_BLOCK = new RegisterBlock();
	/**
	 * This attribute defines all System-Clock blocks.
	 * A System-Clock-Block with default settings.
	 */
	public static final Block SYSTEM_CLOCK_BLOCK = new SystemClockBlock();

	// define Block-Items
	/**
	 * This attribute defines all ALU items.
	 * An ALU-Block-Item with the default settings for block items.
	 */
	public static final BlockItem ALU_BLOCK_ITEM = new BlockItem(ALU_BLOCK, new Item.Settings());
	/**
	 * This attribute defines all Bus items.
	 * A Bus-Block-Item with the default settings for block items.
	 */
	public static final BlockItem BUS_BLOCK_ITEM = new BlockItem(BUS_BLOCK, new Item.Settings());
	/**
	 * This attribute defines all Control-Unit items.
	 * A Control-Unit-Block-Item with the default settings for block items.
	 */
	public static final BlockItem CONTROL_UNIT_BLOCK_ITEM = new BlockItem(CONTROL_UNIT_BLOCK, new Item.Settings());
	/**
	 * This attribute defines all Memory items.
	 * A Memory-Block-Item with the default settings for block items.
	 */
	public static final BlockItem MEMORY_BLOCK_ITEM = new BlockItem(MEMORY_BLOCK, new Item.Settings());
	/**
	 * This attribute defines all Programming items.
	 * A Programming-Block-Item with the default settings for block items.
	 */
	public static final BlockItem PROGRAMMING_BLOCK_ITEM = new BlockItem(PROGRAMMING_BLOCK, new Item.Settings());
	/**
	 * This attribute defines all Register items.
	 * A Register-Block-Item with the default settings for block items.
	 */
	public static final BlockItem REGISTER_BLOCK_ITEM = new BlockItem(REGISTER_BLOCK, new Item.Settings());
	/**
	 * This attribute defines all System-Clock items.
	 * A System-Clock-Block-Item with the default settings for block items.
	 */
	public static final BlockItem SYSTEM_CLOCK_BLOCK_ITEM = new BlockItem(SYSTEM_CLOCK_BLOCK, new Item.Settings());

    // Define Block-Entities. Every placed block from the mod has its own unique block-entity.
	/**
	 * The Type of the ALU-Block-Entity. Every ALU-Block gets its own ALU-Block-Entity when it is placed.
	 */
	public static BlockEntityType<AluBlockEntity> ALU_BLOCK_ENTITY;
	/**
	 * The Type of the Bus-Block-Entity. Every Bus-Block gets its own Bus-Block-Entity when it is placed.
	 */
	public static BlockEntityType<BusBlockEntity> BUS_BLOCK_ENTITY;
	/**
	 * The Type of the Control-Unit-Block-Entity.
	 * Every Control-Unit-Block gets its own Control-Unit-Block-Entity when it is placed.
	 */
	public static BlockEntityType<ControlUnitBlockEntity> CONTROL_UNIT_BLOCK_ENTITY;
	/**
	 * The Type of the Memory-Block-Entity. Every Memory-Block gets its own Memory-Block-Entity when it is placed.
	 */
	public static BlockEntityType<MemoryBlockEntity> MEMORY_BLOCK_ENTITY;
	/**
	 * The Type of the Programming-Block-Entity.
	 * Every Programming-Block gets its own Programming-Block-Entity when it is placed.
	 */
	public static BlockEntityType<ProgrammingBlockEntity> PROGRAMMING_BLOCK_ENTITY;
	/**
	 * The Type of the Register-Block-Entity. Every Register-Block gets its own Register-Block-Entity when it is placed.
	 */
	public static BlockEntityType<RegisterBlockEntity> REGISTER_BLOCK_ENTITY;
	/**
	 * The Type of the System-Clock-Block-Entity.
	 * Every System-Clock-Block gets its own System-Clock-Block-Entity when it is placed.
	 */
	public static BlockEntityType<SystemClockBlockEntity> SYSTEM_CLOCK_BLOCK_ENTITY;

	// define Items
	/**
	 * This attribute defines all Program items.
	 * A Program-Item with the default settings for items but only stackable up to 1.
	 */
	public static final Item PROGRAM_ITEM = new ProgramItem(new Item.Settings().maxCount(1));
	/**
	 * This attribute defines all Goggles items.
	 * A Goggles-Item with the default settings for items but only stackable up to 1.
	 */
	public static final Item GOGGLES_ITEM = new GogglesItem(new Item.Settings().maxCount(1));
	//ToDo right location for .json
	/**
	 * This attribute defines all MiMa instruction set items.
	 * these are linked to the instruction set file for MiMa.
	 */
	public static final Item INSTRUCTION_SET_ITEM_MIMA = new InstructionSetItem(new Item.Settings().maxCount(1),
			RISCJ_blockits.class.getClassLoader().getResourceAsStream("instructionSetMIMA.jsonc"));
	/**
	 * This attribute defines all RISC-V instruction set items.
	 * these are linked to the instruction set file for RISC-V.
	 */
	public static final Item INSTRUCTION_SET_ITEM_RISCV = new InstructionSetItem(new Item.Settings().maxCount(1),
			RISCJ_blockits.class.getClassLoader().getResourceAsStream("instructionSet/instructionSetRiscV.jsonc"));
    /**
     * This attribute defines all Manual items.
     * A Manual-Item with the default settings for items but only stackable up to 1.
     */
    public static final Item MANUAL_ITEM = new ManualItem(new Item.Settings().maxCount(1));

	/**
	 * This attribute defines the ScreenHandlerType for the ProgrammingScreenHandler.
	 */
	public static ScreenHandlerType<ProgrammingScreenHandler> PROGRAMMING_SCREEN_HANDLER;


	/**
	 * This attribute defines the TagKey to identify computerBlocks
	 */
	public static final TagKey<Block> COMPUTER_BLOCK_TAG = new TagKey<>(RegistryKeys.BLOCK,new Identifier(MODID, "computer_blocks"));


	public static  ScreenHandlerType<RegisterScreenHandler> REGISTER_SCREEN_HANDLER =
		Registry.register(Registries.SCREEN_HANDLER, new Identifier(MODID, "register_screen"),
			new ExtendedScreenHandlerType<>(RegisterScreenHandler::new));

	public static  ScreenHandlerType<ControlUnitScreenHandler> CONTROL_UNIT_SCREEN_HANDLER =
		Registry.register(Registries.SCREEN_HANDLER, new Identifier(MODID, "control_unit_screen"),
			new ExtendedScreenHandlerType<>(ControlUnitScreenHandler::new));

	public static  ScreenHandlerType<MemoryScreenHandler> MEMORY_BLOCK_SCREEN_HANDLER =
		Registry.register(Registries.SCREEN_HANDLER, new Identifier(MODID, "memory_block_screen"),
			new ExtendedScreenHandlerType<>(MemoryScreenHandler::new));

	public static  ScreenHandlerType<SystemClockScreenHandler> SYSTEM_CLOCK_SCREEN_HANDLER =
		Registry.register(Registries.SCREEN_HANDLER, new Identifier(MODID, "system_clock_screen"),
			new ExtendedScreenHandlerType<>(SystemClockScreenHandler::new));



	/**
	 * This method is called when the mod is initialized.
	 * The call happens while the game is loading a world.
	 * It registers all blocks, items, block-entities and item-groups.
	 */
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
		Registry.register(Registries.ITEM, new Identifier(MODID, "instruction_set_mima"), INSTRUCTION_SET_ITEM_MIMA);
		Registry.register(Registries.ITEM, new Identifier(MODID, "instruction_set_riscv"), INSTRUCTION_SET_ITEM_RISCV);
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

		ServerPlayNetworking.registerGlobalReceiver(
			NetworkingConstants.SYNC_REGISTER_SELECTION, (server, player, handler, buf, responseSender) -> {
				BlockPos pos = buf.readBlockPos();
				String selectedRegister = buf.readString();

				server.execute(() -> {
					BlockEntity be = player.getWorld().getBlockEntity(pos);
					NbtCompound nbt = new NbtCompound();
					((RegisterBlockEntity) be).writeNbt(nbt);
					NbtCompound subNbt = (NbtCompound) nbt.get(MOD_DATA);
					subNbt.putString(REGISTER_TYPE, selectedRegister);
					be.readNbt(nbt);
				});
			}
		);
		ServerPlayNetworking.registerGlobalReceiver(		//the view wants to get data
			NetworkingConstants.REQUEST_DATA, (server, player, handler, buf, responseSender) -> {
				BlockPos pos = buf.readBlockPos();
				server.execute(() -> {
					ComputerBlockEntity be = (ComputerBlockEntity) player.getWorld().getBlockEntity(pos);
                    assert be != null;
                    be.requestData();
				});

			}
		);
	}

	/**
	 * This attribute defines the item-group for all items and blocks of this mod.
	 * The item-group is used to group items and blocks in the creative inventory.
	 */
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
				//Items
				entries.add(INSTRUCTION_SET_ITEM_MIMA);
				entries.add(INSTRUCTION_SET_ITEM_RISCV);
				entries.add(GOGGLES_ITEM);
				entries.add(PROGRAM_ITEM);
				entries.add(MANUAL_ITEM);
			})
			.build();
}
