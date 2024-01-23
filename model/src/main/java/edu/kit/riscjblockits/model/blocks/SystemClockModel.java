package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
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
        clockSpeed = 0;
        mode = ClockMode.MC_TICK;
        setType(ModelType.CLOCK);
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    /**
     * Getter for the data the view needs for ui.
     * @return Data Format: key: "speed", value: clockSpeed as String
     *                      key: "mode", value: mode as String
     *                      key: "activeTick", value: activeTick as String
     */
    @Override
    public IDataElement getData() {
        Data clockData = new Data();
        clockData.set("speed", new DataStringEntry(String.valueOf(clockSpeed)));
        clockData.set("mode", new DataStringEntry(mode.toString()));
        if (activeTick) {
            clockData.set("activeTick", new DataStringEntry("true"));         //ToDo hier wäre ein boolean data entry schlau, wollen wir glaub eh nicht speichern
        } else {
            clockData.set("activeTick", new DataStringEntry("false"));
        }
        return clockData;
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

    public void setClockSpeed(int clockSpeed) {
        this.clockSpeed = clockSpeed;
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
