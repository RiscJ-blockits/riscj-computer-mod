package edu.kit.riscjblockits.model;

public interface ISimulationTimingObserveable {
    void registerObserver(ISimulationTimingObserver observer);
    void unregisterObserver(ISimulationTimingObserver observer);

    void notifyObservers();
}
