package edu.kit.riscjblockits.model.blocks;

/**
 * The Computer executes instructions at different speeds. This enum defines the available speeds.
 */
public enum ClockMode {
    /**
     * The computer executes instructions as fast as possible.
     */
    REALTIME,

    /**
     * The computer executes instructions based on minecraft tick speed.
     */
    MC_TICK,

    /**
     * The computer executes instructions based on user input.
     */
    STEP,
}
