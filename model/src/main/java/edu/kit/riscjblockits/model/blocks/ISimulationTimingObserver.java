package edu.kit.riscjblockits.model.blocks;

/**
 * Interface for classes that want to observe the state of a ClockTime class.
 */
public interface ISimulationTimingObserver {
    /**
     * Method to update the observed state.
     */
    void updateObservedState();
}
