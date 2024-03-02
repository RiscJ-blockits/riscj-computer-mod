package edu.kit.riscjblockits.view.main;

import net.minecraft.util.Identifier;

/**
 * This class contains all the networking constants used in the mod.
 * They act like address when we send data between the client and the server.
 */
public abstract class NetworkingConstants {

    /**
     * Used by the server when it sends its data to the client.
     * A nbt with data is sent as a parameter.
     * The position of the block must be sent as a parameter.
     */
    public static final Identifier SYNC_BLOCK_ENTITY_DATA = new Identifier(RISCJ_blockits.MOD_ID, "sync_block_entity_data");
    /**
     * Used by the client when it wants to send code inputted from the player to the server for assembly.
     * The position of the block must be sent as a parameter.
     */
    public static final Identifier SYNC_PROGRAMMING_CODE_C2S = new Identifier(RISCJ_blockits.MOD_ID, "sync_programming_code");
    public static final Identifier SYNC_PROGRAMMING_CODE_S2C = new Identifier(RISCJ_blockits.MOD_ID, "sync_programming_code_server");

    /**
     * Used by the server to send exceptions while assembly back to the client.
     */
    public static final Identifier SHOW_ASSEMBLER_EXCEPTION = new Identifier(RISCJ_blockits.MOD_ID, "assemble_programming_code");
    /**
     * Used by the client to send ascii text inputted from the player to the server.
     */
    public static final Identifier SYNC_TERMINAL_INPUT = new Identifier(RISCJ_blockits.MOD_ID, "sync_terminal_input");
    /**
     * Used by the client to send the chosen register to the model.
     * The register type is sent as a string.
     * The position of the block must be sent as a parameter.
     */
    public static final Identifier SYNC_REGISTER_SELECTION = new Identifier(RISCJ_blockits.MOD_ID, "sync_register_selection");
    /**
     * Identifies the data packet from the client to the server to set the current clock speed and mode.
     */
    public static final Identifier SYNC_CLOCK_MODE_SELECTION = new Identifier(RISCJ_blockits.MOD_ID, "sync_clock_mode_selection");
    /**
     * Used when the client wants to request block data from the server.
     * The position of the block must be sent as a parameter.
     */
    public static final Identifier REQUEST_DATA = new Identifier(RISCJ_blockits.MOD_ID, "request_data");
    /**
     * Is called by the server when it wants to open the manual screen on the client.
     */
    public static final Identifier OPEN_MANUAL_SCREEN = new Identifier(RISCJ_blockits.MOD_ID, "open_manual_screen");
    /**
     * Is called by the server when it wants to open the Instruction Set edit screen on the client.
     */
    public static final Identifier OPEN_IST_SCREEN = new Identifier(RISCJ_blockits.MOD_ID, "open_ist_screen");
    /**
     * Used by the client to send the edited Instruction Set to the server.
     * A String with the json is sent as a parameter.
     */
    public static final Identifier SYNC_IST_INPUT = new Identifier(RISCJ_blockits.MOD_ID, "sync_ist_input");
    /**
     * Used by the client to tell the server which memory data to send.
     */
    public static final Identifier SYNC_MEMORY_LINE_QUERY = new Identifier(RISCJ_blockits.MOD_ID, "sync_memory_line_query");
    public static final Identifier SYNC_PROGRAMMING_CODE_CONFIRMATION_S2C = new Identifier(RISCJ_blockits.MOD_ID, "sync_programming_code_confirmation_server");
    public static final Identifier SYNC_PROGRAMMING_CODE_CONFIRMATION_C2S = new Identifier(RISCJ_blockits.MOD_ID, "sync_programming_code_confirmation");
    //
    public static final Identifier SYNC_IST_TEXT_C2S = new Identifier(RISCJ_blockits.MOD_ID, "sync_ist_text");
    public static final Identifier SYNC_TEMP_TEXT_C2S = new Identifier(RISCJ_blockits.MOD_ID, "sync_ist_text_server");
    public static final Identifier SYNC_TEMP_TEXT_CONFIRMATION_C2S = new Identifier(RISCJ_blockits.MOD_ID, "sync_ist_text_confirmation_server");
    public static final Identifier SYNC_IST_TEXT_CONFIRMATION_C2S = new Identifier(RISCJ_blockits.MOD_ID, "sync_ist_text_confirmation");
    /**
     * This class should not be instantiated.
     * @throws IllegalStateException if the class is instantiated.
     */
    private NetworkingConstants() {
        throw new IllegalStateException("Utility class");
    }

}
