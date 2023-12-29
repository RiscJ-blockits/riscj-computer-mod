package edu.kit.riscjblockits.view.main.blocks;

public enum EntityType {
    BUS,
    /**
     * Alle block of the computer except the bus.
     */
    CONNECTABLE,
    /**
     * This type is used for blocks that are not part of the computer.
     */
    UNCONNECTABLE,
}
