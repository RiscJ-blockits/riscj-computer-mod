package edu.kit.riscjblockits.model.data;

import edu.kit.riscjblockits.model.blocks.RegisterModel;

/**
 * Defines constants used as keys in data and nbt elements.
 */
public class DataConstants {
//ToDo write Javadoc
    /**
     * Illegal Constructor. Utility class.
     */
    private DataConstants() {
        throw new IllegalStateException("Utility class");
    }

    //General Constants
    /**
     * Identifies DataElements that are used to store all block-specific data.
     * The format inside is different for every block.
     */
    public static final String MOD_DATA = "modData";

    //Register Constants
    /**
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_REGISTERS = "registers";

    /**
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_VALUE = "value";

    /**
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_FOUND = "found";

    /**
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_MISSING = "missing";

    /**
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_TYPE = "type";

    /**
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_WORD_LENGTH = "word";

    //WirelessRegister Constants
    public static final String REGISTER_WIRELESS_XPOS = "WirelessRegister_XPos";
    public static final String REGISTER_WIRELESS_YPOS = "WirelessRegister_YPos";
    public static final String REGISTER_WIRELESS_ZPOS = "WirelessRegister_ZPos";
    public static final String REGISTER_WIRELESS = "WirelessRegister";
    public static final String REGISTER_IO_TIME = "register_io_time";

    //SystemClock Constants
    /**
     * {@link edu.kit.riscjblockits.model.blocks.SystemClockModel#getData()}
     */
    public static final String CLOCK_SPEED = "speed";

    /**
     * {@link edu.kit.riscjblockits.model.blocks.SystemClockModel#getData()}
     */
    public static final String CLOCK_MODE = "mode";

    /**
     * {@link edu.kit.riscjblockits.model.blocks.SystemClockModel#getData()}
     */
    public static final String CLOCK_ACTIVE = "activeTick";

    //ControlUnit Constants
    /**
     * {@link edu.kit.riscjblockits.model.blocks.ControlUnitModel#getData()}
     */
    public static final String CONTROL_CLUSTERING = "clustering";


    public static final String CONTROL_IST_MODEL = "istModel";

    /**
     * NBT-Tag of the InstructionSet Item.
     */
    public static final String CONTROL_IST_ITEM = "riscj_blockits.instructionSet";  //Don't change this!

    /**
     * {@link edu.kit.riscjblockits.model.blocks.ControlUnitModel#getData()}
     */
    public static final String CONTROL_ITEM_PRESENT = "istModelPresent";

    //Clustering Constants
    public static final String CLUSTERING_MISSING_REGISTERS = "missingRegisters";
    public static final String CLUSTERING_FOUND_REGISTERS = "foundRegisters";
    public static final String CLUSTERING_FOUND_CONTROL_UNIT = "foundControlUnit";
    public static final String CLUSTERING_FOUND_ALU = "foundALU";
    public static final String CLUSTERING_FOUND_MEMORY = "foundMemory";
    public static final String CLUSTERING_FOUND_CLOCK = "foundSystemClock";


    //Bus Constants
    /**
     * {@link edu.kit.riscjblockits.model.blocks.BusModel#getData()}
     */
    public static final String BUS_ACTIVE = "active";

    /**
     * {@link edu.kit.riscjblockits.model.blocks.BusModel#getData()}
     */
    public static final String BUS_DATA = "presentData";

    //Alu Constants
    /**
     * {@link edu.kit.riscjblockits.model.blocks.AluModel#getData()}
     */
    public static final String ALU_OPERATION = "operation";

    /**
     * {@link edu.kit.riscjblockits.model.blocks.AluModel#getData()}
     */
    public static final String ALU_OPERAND1 = "operand1";

    /**
     * {@link edu.kit.riscjblockits.model.blocks.AluModel#getData()}
     */
    public static final String ALU_OPERAND2 = "operand2";

    /**
     * {@link edu.kit.riscjblockits.model.blocks.AluModel#getData()}
     */
    public static final String ALU_RESULT = "result";

    //Memory Constants
    /**
     * {@link edu.kit.riscjblockits.model.blocks.MemoryModel#getData()}
     */
    public static final String MEMORY_MEMORY = "memory";

    /**
     * {@link edu.kit.riscjblockits.model.blocks.MemoryModel#getData()}
     */
    public static final String MEMORY_WORD = "wordSize";

    /**
     * {@link edu.kit.riscjblockits.model.blocks.MemoryModel#getData()}
     */
    public static final String MEMORY_ADDRESS = "addressSize";

    public static final String MEMORY_INITIAL_PC = "initialPC";

    /**
     * NBT-Tag of the Programm Item.
     */
    public static final String MEMORY_PROGRAMM_ITEM = "riscj_blockits.memory";  //Don't change this!

}
