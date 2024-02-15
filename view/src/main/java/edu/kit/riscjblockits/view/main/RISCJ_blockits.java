package edu.kit.riscjblockits.view.main;

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
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.WirelessRegisterBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.WirelessRegisterBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.RedstoneInputBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.RedstoneInputBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.RedstoneOutputBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.RedstoneOutputBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.TerminalScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.TerminalBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.TerminalBlockEntity;
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
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_SPEED;
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
    public static final String MOD_ID = "riscj_blockits";
    /**
     * Logger for writing text to the console and the log file.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
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
     * This attribute defines all Redstone-Output blocks.
     * A Redstone-Output-Block with default settings.
     */
    public static final Block REDSTONE_OUTPUT_BLOCK = new RedstoneOutputBlock();
    /**
     * This attribute defines all Redstone-Input blocks.
     * A Redstone-Input-Block with default settings.
     */
    public static final Block REDSTONE_INPUT_BLOCK = new RedstoneInputBlock();
    /**
     * This attribute defines all Wireless-Register blocks.
     * A Wireless-Register-Block with default settings.
     */
    public static final Block WIRELESS_REGISTER_BLOCK = new WirelessRegisterBlock();
    /**
     * This attribute defines all Text-Output blocks.
     * A Text-Output-Block with default settings.
     */
    public static final Block TEXT_OUTPUT_BLOCK = new TerminalBlock();
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
     * This attribute defines all Redstone-Output items.
     * A Redstone-Output-Block-Item with the default settings for block items.
     */
    public static final BlockItem REDSTONE_OUTPUT_BLOCK_ITEM =
        new BlockItem(REDSTONE_OUTPUT_BLOCK, new Item.Settings());
    /**
     * This attribute defines all Redstone-Input items.
     * A Redstone-Input-Block-Item with the default settings for block items.
     */
    public static final BlockItem REDSTONE_INPUT_BLOCK_ITEM = new BlockItem(REDSTONE_INPUT_BLOCK, new Item.Settings());
    /**
     * This attribute defines all Wireless-Register items.
     * A Wireless-Register-Block-Item with the default settings for block items.
     */
    public static final BlockItem WIRELESS_REGISTER_BLOCK_ITEM =
        new BlockItem(WIRELESS_REGISTER_BLOCK, new Item.Settings());
    /**
     * This attribute defines all Text-Output items.
     * A Text-Output-Block-Item with the default settings for block items.
     */
    public static final BlockItem TEXT_OUTPUT_BLOCK_ITEM = new BlockItem(TEXT_OUTPUT_BLOCK, new Item.Settings());
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
     * The Type of the Redstone-Output-Block-Entity.
     * Every Redstone-Output-Block gets its own Redstone-Output-Block-Entity when it is placed.
     */
    public static BlockEntityType<RedstoneOutputBlockEntity> REDSTONE_OUTPUT_BLOCK_ENTITY;
    /**
     * The Type of the Redstone-Input-Block-Entity.
     * Every Redstone-Input-Block gets its own Redstone-Input-Block-Entity when it is placed.
     */
    public static BlockEntityType<RedstoneInputBlockEntity> REDSTONE_INPUT_BLOCK_ENTITY;
    /**
     * The Type of the Wireless-Register-Block-Entity.
     * Every Wireless-Register-Block gets its own Wireless-Register-Block-Entity when it is placed.
     */
    public static BlockEntityType<WirelessRegisterBlockEntity> WIRELESS_REGISTER_BLOCK_ENTITY;
    /**
     * The Type of the Text-Output-Block-Entity.
     * Every Text-Output-Block gets its own Text-Output-Block-Entity when it is placed.
     */
    public static BlockEntityType<TerminalBlockEntity> TEXT_OUTPUT_BLOCK_ENTITY;
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
    /**
     * This attribute defines all MiMa instruction set items.
     * It is linked to the instruction set file for MiMa.
     */
    public static final Item INSTRUCTION_SET_ITEM_MIMA = new InstructionSetItem(new Item.Settings().maxCount(1),
        RISCJ_blockits.class.getClassLoader().getResourceAsStream("instructionSetMIMA.jsonc"));
    /**
     * This attribute defines all mima with IO support instruction set items.
     * It is linked to the instruction set file for mima with IO support.
     */
    public static final Item INSTRUCTION_SET_ITEM_MIMA_IO = new InstructionSetItem(new Item.Settings().maxCount(1),
        RISCJ_blockits.class.getClassLoader().getResourceAsStream("instructionSet/instructionSetMIMA_IO.jsonc"));
    /**
     * This attribute defines all RISC-V instruction set items.
     * It is linked to the instruction set file for RISC-V.
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
     * It is the handler for the screen of the ProgrammingBlock.
     */
    public static ScreenHandlerType<ProgrammingScreenHandler> PROGRAMMING_SCREEN_HANDLER;
    /**
     * This attribute defines the ScreenHandlerType for the RegisterScreenHandler.
     * It is the handler for the screen of the RegisterBlock.
     */
    public static ScreenHandlerType<RegisterScreenHandler> REGISTER_SCREEN_HANDLER =
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(MOD_ID, "register_screen"),
            new ExtendedScreenHandlerType<>(RegisterScreenHandler::new));
    /**
     * This attribute defines the ScreenHandlerType for the ControlUnitScreenHandler.
     * It is the handler for the screen of the ControlUnitBlock.
     */
    public static ScreenHandlerType<ControlUnitScreenHandler> CONTROL_UNIT_SCREEN_HANDLER =
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(MOD_ID, "control_unit_screen"),
            new ExtendedScreenHandlerType<>(ControlUnitScreenHandler::new));
    /**
     * This attribute defines the ScreenHandlerType for the MemoryScreenHandler.
     * It is the handler for the screen of the MemoryBlock.
     */
    public static ScreenHandlerType<MemoryScreenHandler> MEMORY_BLOCK_SCREEN_HANDLER =
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(MOD_ID, "memory_block_screen"),
            new ExtendedScreenHandlerType<>(MemoryScreenHandler::new));
    /**
     * This attribute defines the ScreenHandlerType for the SystemClockScreenHandler.
     * It is the handler for the screen of the SystemClockBlock.
     */
    public static ScreenHandlerType<SystemClockScreenHandler> SYSTEM_CLOCK_SCREEN_HANDLER =
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(MOD_ID, "system_clock_screen"),
            new ExtendedScreenHandlerType<>(SystemClockScreenHandler::new));
    /**
     * This attribute defines the ScreenHandlerType for the TerminalScreenHandler.
     * It is the handler for the screen of the TerminalBlock.
     */
    public static ScreenHandlerType<TerminalScreenHandler> TERMINAL_SCREEN_HANDLER =
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(MOD_ID, "terminal_screen"),
            new ExtendedScreenHandlerType<>(TerminalScreenHandler::new));
    /**
     * This attribute defines the item-group for all items and blocks of this mod.
     * The item-group is used to group items and blocks in the creative inventory.
     */
    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
        .icon(() -> new ItemStack(PROGRAM_ITEM))
        .displayName(Text.translatable("itemGroup.riscj_blockits.computer_components"))
        .entries((context, entries) -> {
            entries.add(ALU_BLOCK_ITEM);
            entries.add(BUS_BLOCK_ITEM);
            entries.add(CONTROL_UNIT_BLOCK_ITEM);
            entries.add(MEMORY_BLOCK_ITEM);
            entries.add(PROGRAMMING_BLOCK_ITEM);
            entries.add(REGISTER_BLOCK_ITEM);
            entries.add(SYSTEM_CLOCK_BLOCK_ITEM);
            entries.add(REDSTONE_OUTPUT_BLOCK_ITEM);
            entries.add(REDSTONE_INPUT_BLOCK_ITEM);
            entries.add(WIRELESS_REGISTER_BLOCK_ITEM);
            entries.add(TEXT_OUTPUT_BLOCK_ITEM);
            //Items
            entries.add(INSTRUCTION_SET_ITEM_MIMA);
            entries.add(INSTRUCTION_SET_ITEM_MIMA_IO);
            entries.add(INSTRUCTION_SET_ITEM_RISCV);
            entries.add(GOGGLES_ITEM);
            entries.add(PROGRAM_ITEM);
            entries.add(MANUAL_ITEM);
        })
        .build();
    /**
     * This method is called when the mod is initialized.
     * The call happens while the game is loading a world.
     * It registers all blocks, items, block-entities and item-groups.
     */
    @Override
    public void onInitialize() {
        // register ScreenHandlers
        PROGRAMMING_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerExtended(new Identifier(MOD_ID, "programming_screen"),
                ProgrammingScreenHandler::new);
        // register Blocks
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "alu_block"), ALU_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "bus_block"), BUS_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "control_unit_block"), CONTROL_UNIT_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "memory_block"), MEMORY_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "programming_block"), PROGRAMMING_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "register_block"), REGISTER_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "system_clock_block"), SYSTEM_CLOCK_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "redstone_output_block"), REDSTONE_OUTPUT_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "redstone_input_block"), REDSTONE_INPUT_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "wireless_register_block"), WIRELESS_REGISTER_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "text_output_block"), TEXT_OUTPUT_BLOCK);
        // register Block-Items
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "alu_block"), ALU_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "bus_block"), BUS_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "control_unit_block"), CONTROL_UNIT_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "memory_block"), MEMORY_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "programming_block"), PROGRAMMING_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "register_block"), REGISTER_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "system_clock_block"), SYSTEM_CLOCK_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "redstone_output_block"), REDSTONE_OUTPUT_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "redstone_input_block"), REDSTONE_INPUT_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "wireless_register_block"),
            WIRELESS_REGISTER_BLOCK_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "text_output_block"), TEXT_OUTPUT_BLOCK_ITEM);
        // register Items
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "goggles"), GOGGLES_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "instruction_set_mima"), INSTRUCTION_SET_ITEM_MIMA);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "instruction_set_mima_io"),
            INSTRUCTION_SET_ITEM_MIMA_IO);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "instruction_set_riscv"), INSTRUCTION_SET_ITEM_RISCV);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "manual"), MANUAL_ITEM);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "program"), PROGRAM_ITEM);
        // register Block-Entities
        ALU_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "alu_block_entity"),
            FabricBlockEntityTypeBuilder.create(AluBlockEntity::new, ALU_BLOCK).build());
        BUS_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "bus_block_entity"),
            FabricBlockEntityTypeBuilder.create(BusBlockEntity::new, BUS_BLOCK).build());
        CONTROL_UNIT_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "control_unit_block_entity"),
                FabricBlockEntityTypeBuilder.create(ControlUnitBlockEntity::new, CONTROL_UNIT_BLOCK).build());
        MEMORY_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "memory_block_entity"),
                FabricBlockEntityTypeBuilder.create(MemoryBlockEntity::new, MEMORY_BLOCK).build());
        PROGRAMMING_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "programming_block_entity"),
                FabricBlockEntityTypeBuilder.create(ProgrammingBlockEntity::new, PROGRAMMING_BLOCK).build());
        REGISTER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "register_block_entity"),
                FabricBlockEntityTypeBuilder.create(RegisterBlockEntity::new, REGISTER_BLOCK).build());
        SYSTEM_CLOCK_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "system_clock_block_entity"),
                FabricBlockEntityTypeBuilder.create(SystemClockBlockEntity::new, SYSTEM_CLOCK_BLOCK).build());
        REDSTONE_OUTPUT_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "redstone_output_block_entity"),
                FabricBlockEntityTypeBuilder.create(RedstoneOutputBlockEntity::new, REDSTONE_OUTPUT_BLOCK).build());
        REDSTONE_INPUT_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "redstone_input_block_entity"),
                FabricBlockEntityTypeBuilder.create(RedstoneInputBlockEntity::new, REDSTONE_INPUT_BLOCK).build());
        WIRELESS_REGISTER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "wireless_register_block_entity"),
                FabricBlockEntityTypeBuilder.create(WirelessRegisterBlockEntity::new, WIRELESS_REGISTER_BLOCK).build());
        TEXT_OUTPUT_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "text_output_block_entity"),
                FabricBlockEntityTypeBuilder.create(TerminalBlockEntity::new, TEXT_OUTPUT_BLOCK).build());
        // register the Item-Group
        Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "computer_components"), ITEM_GROUP);
        //Register  Client-Server Networking
        ServerPlayNetworking.registerGlobalReceiver(
            NetworkingConstants.SYNC_REGISTER_SELECTION, (server, player, handler, buf, responseSender) -> {
                BlockPos pos = buf.readBlockPos();
                String selectedRegister = buf.readString();
                server.execute(() -> {
                    BlockEntity be = player.getWorld().getBlockEntity(pos);
                    NbtCompound nbt = new NbtCompound();
                    assert be != null;
                    ((RegisterBlockEntity) be).writeNbt(nbt);
                    NbtCompound subNbt = (NbtCompound) nbt.get(MOD_DATA);
                    assert subNbt != null;
                    subNbt.putString(REGISTER_TYPE, selectedRegister);
                    be.readNbt(nbt);
                });
            }
        );
        ServerPlayNetworking.registerGlobalReceiver(
            NetworkingConstants.SYNC_CLOCK_MODE_SELECTION, (server, player, handler, buf, responseSender) -> {
                BlockPos pos = buf.readBlockPos();
                String clockMode = buf.readString();
                int clockSpeed = buf.readInt();
                server.execute(() -> {
                    BlockEntity be = player.getWorld().getBlockEntity(pos);
                    NbtCompound nbt = new NbtCompound();
                    assert be != null;
                    ((SystemClockBlockEntity) be).writeNbt(nbt);
                    NbtCompound subNbt = (NbtCompound) nbt.get(MOD_DATA);
                    assert subNbt != null;
                    subNbt.putString(CLOCK_SPEED, String.valueOf(clockSpeed));
                    subNbt.putString(CLOCK_MODE, clockMode);
                    be.readNbt(nbt);
                });
            }
        );
        ServerPlayNetworking.registerGlobalReceiver(        //the view wants to get data
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
}
