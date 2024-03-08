package edu.kit.riscjblockits.controller.simulation;

/**
 * Interface for classes that want to receive callbacks from the SimulationSequenceHandler class
 * after every real-time simulation tick.
 */
public interface IRealtimeSimulationCallbackReceivable {

    /**
     * Called after every real-time simulation tick.
     */
    void onSimulationTickComplete();

}
