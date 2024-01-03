package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.ClockMode;
import edu.kit.riscjblockits.model.IObserveable;
import edu.kit.riscjblockits.model.IObserver;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.ArrayList;
import java.util.List;

public class SystemClockModel extends BlockModel implements IObserveable {

    private List<IObserver> modeObservers;
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
    public void writeDataRequest(IDataElement dataElement) {

    }


    @Override
    public void registerObserver(IObserver observer) {
        modeObservers.add(observer);
    }

    @Override
    public void unregisterObserver(IObserver observer) {
        modeObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer: modeObservers) {
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
}
