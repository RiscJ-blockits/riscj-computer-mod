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
    public static final String REGISTER_FOUND = "found";
    public static final String REGISTER_MISSING = "missing";
    public static final String REGISTER_TYPE = "type";
    public static final String REGISTER_WORD_LENGTH = "word";

    //SystemClock Constants
    public static final String CLOCK_SPEED = "speed";
    public static final String CLOCK_MODE = "mode";
    public static final String CLOCK_ACTIVE = "activeTick";

    //ControlUnit Constants
    public static final String CONTROL_CLUSTERING = "clustering";
    public static final String CONTROL_IST_MODEL = "istModel";

    /**
     * NBT-Tag of the InstructionSet Item.
     */
    public static final String CONTROL_IST_ITEM = "riscj_blockits.instructionSet";  //Don't change this!
    public static final String CONTROL_ITEM_PRESENT = "istModelPresent";

    //Bus Constants
    public static final String BUS_ACTIVE = "active";
    public static final String BUS_DATA = "presentData";

    //Alu Constants
    public static final String ALU_OPERATION = "operation";
    public static final String ALU_OPERAND1 = "operand1";
    public static final String ALU_OPERAND2 = "operand2";
    public static final String ALU_RESULT = "result";

}
