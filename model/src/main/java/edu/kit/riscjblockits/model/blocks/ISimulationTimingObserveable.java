package edu.kit.riscjblockits.model.blocks;

public interface ISimulationTimingObserveable {
    void registerObserver(ISimulationTimingObserver observer);
    void unregisterObserver(ISimulationTimingObserver observer);

    void notifyObservers();
}
