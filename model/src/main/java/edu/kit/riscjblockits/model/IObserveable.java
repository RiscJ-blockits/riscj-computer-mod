package edu.kit.riscjblockits.model;

public interface IObserveable {
    void registerObserver(IObserver observer);
    void unregisterObserver(IObserver observer);

    void notifyObservers();
}
