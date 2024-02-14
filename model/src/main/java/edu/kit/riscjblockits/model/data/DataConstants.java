package edu.kit.riscjblockits.model.data;

import edu.kit.riscjblockits.model.blocks.RegisterModel;

/**
 * Defines constants used as keys in data and nbt elements.
 */
public final class DataConstants {

    //General Constants
    /**
     * Identifies DataElements that are used to store all block-specific data.
     * The format inside is different for every block.
     */
    public static final String MOD_DATA = "modData";

    //Register Constants
    /**
     * Identifier for the register data.
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_REGISTERS = "registers";

    /**
     * Identifier for register values.
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_VALUE = "value";

    /**
     * Identifier for found registers.
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_FOUND = "found";

    /**
     * Identifier for missing registers.
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_MISSING = "missing";

    /**
     * Identifier for the register type.
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_TYPE = "type";

    /**
     * Identifier for the word length of the register.
     * {@link RegisterModel#getData()}
     */
    public static final String REGISTER_WORD_LENGTH = "word";

    //WirelessRegister Constants
    /**
     * Identifier for the x position of the wireless register.
     */
    public static final String REGISTER_WIRELESS_XPOS = "WirelessRegister_XPos";

    /**
     * Identifier for the y position of the wireless register.
     */
    public static final String REGISTER_WIRELESS_YPOS = "WirelessRegister_YPos";

    /**
     * Identifier for the z position of the wireless register.
     */
    public static final String REGISTER_WIRELESS_ZPOS = "WirelessRegister_ZPos";

    /**
     * Identifier for the name of the wireless register.
     */
    public static final String REGISTER_WIRELESS = "WirelessRegister";

    public static final String REGISTER_TERMNAL_MODE = "terminal_mode";
    public static final String REGISTER_TERMINAL_INPUT = "terminal_input";
    public static final String REGISTER_TERMINAL_IN_TYPE = "terminal_in_type";
    public static final String REGISTER_TERMINAL_OUT_TYPE = "terminal_out_type";

    //SystemClock Constants
    /**
     * Identifier for the speed of the system clock.
     * {@link edu.kit.riscjblockits.model.blocks.SystemClockModel#getData()}
     */
    public static final String CLOCK_SPEED = "speed";

    /**
     * Identifier for the mode of the system clock.
     * {@link edu.kit.riscjblockits.model.blocks.SystemClockModel#getData()}
     */
    public static final String CLOCK_MODE = "mode";

    /**
     * Identifier for the active tick state of the system clock.
     * {@link edu.kit.riscjblockits.model.blocks.SystemClockModel#getData()}
     */
    public static final String CLOCK_ACTIVE = "activeTick";

    //ControlUnit Constants
    /**
     * Identifier for the clustering data for the control unit.
     */
    public static final String CONTROL_CLUSTERING = "clustering";

    /**
     * Identifier for the InstructionSetModel of the control unit.
     */
    public static final String CONTROL_IST_MODEL = "istModel";

    /**
     * NBT-Tag of the InstructionSet Item.
     */
    //Don't change this!
    public static final String CONTROL_IST_ITEM = "riscj_blockits.instructionSet";

    /**
     * Identifier for whether the control unit has an InstructionSetModel.
     * {@link edu.kit.riscjblockits.model.blocks.ControlUnitModel#getData()}
     */
    public static final String CONTROL_ITEM_PRESENT = "istModelPresent";

    //Clustering Constants
    /**
     * Identifier for the missing registers.
     */
    public static final String CLUSTERING_MISSING_REGISTERS = "missingRegisters";
    /**
     * Identifier for the found registers.
     */
    public static final String CLUSTERING_FOUND_REGISTERS = "foundRegisters";
    /**
     * Identifier for the found control unit.
     */
    public static final String CLUSTERING_FOUND_CONTROL_UNIT = "foundControlUnit";
    /**
     * Identifier for the found alu.
     */
    public static final String CLUSTERING_FOUND_ALU = "foundALU";
    /**
     * Identifier for the found memory.
     */
    public static final String CLUSTERING_FOUND_MEMORY = "foundMemory";
    /**
     * Identifier for the found system clock.
     */
    public static final String CLUSTERING_FOUND_CLOCK = "foundSystemClock";


    //Bus Constants
    /**
     * Active flag for the bus.
     * {@link edu.kit.riscjblockits.model.blocks.BusModel#getData()}
     */
    public static final String BUS_ACTIVE = "active";

    /**
     * Present data flag on the bus.
     * {@link edu.kit.riscjblockits.model.blocks.BusModel#getData()}
     */
    public static final String BUS_DATA = "presentData";

    //Alu Constants
    /**
     * Identifier for the operation of the alu.
     * {@link edu.kit.riscjblockits.model.blocks.AluModel#getData()}
     */
    public static final String ALU_OPERATION = "operation";

    /**
     * Identifier for the operand1 of the alu.
     * {@link edu.kit.riscjblockits.model.blocks.AluModel#getData()}
     */
    public static final String ALU_OPERAND1 = "operand1";

    /**
     * Identifier for the operand2 of the alu.
     * {@link edu.kit.riscjblockits.model.blocks.AluModel#getData()}
     */
    public static final String ALU_OPERAND2 = "operand2";

    /**
     * Identifier for the result of the alu.
     * {@link edu.kit.riscjblockits.model.blocks.AluModel#getData()}
     */
    public static final String ALU_RESULT = "result";

    //Memory Constants
    /**
     * Identifier for the memory data.
     * {@link edu.kit.riscjblockits.model.blocks.MemoryModel#getData()}
     */
    public static final String MEMORY_MEMORY = "memory";

    /**
     * Identifier for the word size of the memory.
     * {@link edu.kit.riscjblockits.model.blocks.MemoryModel#getData()}
     */
    public static final String MEMORY_WORD = "wordSize";

    /**
     * Identifier for the address size of the memory.
     * {@link edu.kit.riscjblockits.model.blocks.MemoryModel#getData()}
     */
    public static final String MEMORY_ADDRESS = "addressSize";

    /**
     * Identifier for the initial program counter register of the memory.
     */
    public static final String MEMORY_INITIAL_PC = "initialPC";

    /**
     * NBT-Tag of the Programm Item.
     */
    //Don't change this!
    public static final String MEMORY_PROGRAMM_ITEM = "riscj_blockits.memory";

    //ProgrammingBlock Constants
    /**
     * Used inside the nbt of the programming block to store inputted code.
     */
    public static final String PROGRAMMING_BLOCK_CODE = "code";

    /**
     * Illegal Constructor. Utility class.
     * @throws IllegalStateException if called.
     */
    private DataConstants() {
        throw new IllegalStateException("Utility class");
    }

}
