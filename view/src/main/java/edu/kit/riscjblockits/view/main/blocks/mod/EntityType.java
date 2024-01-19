package edu.kit.riscjblockits.view.main.blocks.mod;

/**
 * Defines the type of this entity. Is used to differentiate between different types of entities.
 */
public enum EntityType {
    /**
     * Is used only for bus blocks.
     */
    BUS,
    /**
     * All blocks of the computer except the bus.
     */
    CONNECTABLE,
    /**
     * This type is used for blocks that are not part of the computer.
     */
    UNCONNECTABLE,
}
