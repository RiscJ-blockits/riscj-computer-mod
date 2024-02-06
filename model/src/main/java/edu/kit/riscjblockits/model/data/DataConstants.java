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

    /**
     * {@link edu.kit.riscjblockits.controller.blocks.ControlUnitController#setData(IDataElement)}
     */
    public static final String CONTROL_IST_MODEL = "istModel";

    /**
     * NBT-Tag of the InstructionSet Item.
     */
    public static final String CONTROL_IST_ITEM = "riscj_blockits.instructionSet";  //Don't change this!

    /**
     * {@link edu.kit.riscjblockits.model.blocks.ControlUnitModel#getData()}
     */
    public static final String CONTROL_ITEM_PRESENT = "istModelPresent";

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

    /**
     * NBT-Tag of the Programm Item.
     */
    public static final String MEMORY_PROGRAMM_ITEM = "riscj_blockits.memory";  //Don't change this!

}
