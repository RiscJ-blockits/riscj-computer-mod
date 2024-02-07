package edu.kit.riscjblockits.view.main;

import net.minecraft.util.Identifier;

public abstract class NetworkingConstants {
    public static final Identifier EXAMPLE = new Identifier(RISCJ_blockits.MODID, "example");

    public static final Identifier SYNC_BLOCK_ENTITY_DATA = new Identifier(RISCJ_blockits.MODID, "sync_block_entity_data");

    public static final Identifier SYNC_PROGRAMMING_CODE = new Identifier(RISCJ_blockits.MODID, "sync_programming_code");
    public static final Identifier ASSEMBLE_PROGRAMMING_CODE = new Identifier(RISCJ_blockits.MODID, "assemble_programming_code");
    public static final Identifier SYNC_TERMINAL_INPUT = new Identifier(RISCJ_blockits.MODID, "sync_terminal_input");
    public static final Identifier SYNC_REGISTER_SELECTION = new Identifier(RISCJ_blockits.MODID, "sync_register_selection");

    /**
     * Identifies the data packet from the client to the server to set the current clock speed and mode.
     */
    public static final Identifier SYNC_CLOCK_MODE_SELECTION = new Identifier(RISCJ_blockits.MODID, "sync_clock_mode_selection");

    public static final Identifier REQUEST_DATA = new Identifier(RISCJ_blockits.MODID, "request_data");
    public static final Identifier OPEN_MANUAL_SCREEN = new Identifier(RISCJ_blockits.MODID, "open_manual_screen");
}
