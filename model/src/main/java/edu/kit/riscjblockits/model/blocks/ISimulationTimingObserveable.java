package edu.kit.riscjblockits.model.blocks;

/**
 * Interface for ClockTime classes who should be observed form the Simulation.
 */
public interface ISimulationTimingObserveable {
    /**
     * Method to register an ISimulationTimingObserveable.
     * @param observer The observer to register.
     */
    void registerObserver(ISimulationTimingObserver observer);

    /**
     * Method to unregister an ISimulationTimingObserveable.
     * @param observer The observer to unregister.
     */
    void unregisterObserver(ISimulationTimingObserver observer);

    /**
     * Method to notify all observers.
     */
    void notifyObservers();
}
