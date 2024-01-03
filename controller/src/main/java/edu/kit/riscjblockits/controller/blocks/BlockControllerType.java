package edu.kit.riscjblockits.controller.blocks;

/**
 * The type of block controller. It is used to differentiate between different types of block controllers.
 */
public enum BlockControllerType {
    UNDEFINED,
    /**
     * The controller for a system clock.
     */
    CLOCK,
    /**
     * The controller for a control unit.
     */
    CONTROL_UNIT,
    /**
     * The controller for a register.
     */
    REGISTER,
    /**
     * The controller for a main memory.
     */
    MEMORY,
    /**
     * The controller for a bus.
     */
    BUS,

}
