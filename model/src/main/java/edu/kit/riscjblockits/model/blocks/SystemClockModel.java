package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.ClockMode;
import edu.kit.riscjblockits.model.ISimulationTimingObserveable;
import edu.kit.riscjblockits.model.ISimulationTimingObserver;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.ArrayList;
import java.util.List;

public class SystemClockModel extends BlockModel implements ISimulationTimingObserveable {

    private List<ISimulationTimingObserver> modeObservers;
    private int clockSpeed;
    private ClockMode mode;

    private boolean activeTick;

    public SystemClockModel() {
        modeObservers = new ArrayList<>();
        activeTick = false;
        //ToDo remove Test Code
        clockSpeed = 1;
        mode = ClockMode.MC_TICK;
        setType(ModelType.CLOCK);
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    @Override
    public IDataElement getData() {
        return null;
    }

    @Override
    public void registerObserver(ISimulationTimingObserver observer) {
        modeObservers.add(observer);
    }

    @Override
    public void unregisterObserver(ISimulationTimingObserver observer) {
        modeObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ISimulationTimingObserver observer: modeObservers) {
            observer.updateObservedState();
        }
    }

    public int getClockSpeed() {
        return clockSpeed;
    }

    public ClockMode getClockMode() {
        return mode;
    }

    public void setActiveTick(boolean activeTick) {
        this.activeTick = activeTick;
    }

    public void setClockMode(ClockMode mode) {
        this.mode = mode;
        notifyObservers();
    }

}
