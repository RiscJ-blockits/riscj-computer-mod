package edu.kit.riscjblockits.model.blocks;

//ToDo vereinheitliche Enums

/**
 *  This enum defines the available types of models.
 *  It is used to differentiate between different types of block controllers.
 */
public enum ModelType {
    /**
     * The model type for the control unit.
     */
    CONTROL_UNIT,
    /**
     * The model type for the arithmetic logic unit.
     */
    CLOCK,
    /**
     * The model type for the arithmetic logic unit.
     */
    ALU,
    /**
     * The model type for the memory.
     */
    MEMORY,
    /**
     * The model type for the register.
     */
    REGISTER,
    /**
     * The model type for the bus.
     */
    BUS

}
