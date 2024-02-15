package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_ACTIVE;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_SPEED;

/**
 * Represents the data and state of a system clock. Every computer has one.
 */
public class SystemClockModel extends BlockModel implements ISimulationTimingObserveable {

    private List<ISimulationTimingObserver> modeObservers;
    private int clockSpeed;
    private ClockMode mode;
    private boolean activeTick;

    /**
     * Constructor. Returns the model for a system clock.
     * Initializes attributes. Default clock mode is the Step mode.
     */
    public SystemClockModel() {
        modeObservers = new ArrayList<>();
        activeTick = false;
        clockSpeed = 0;
        mode = ClockMode.STEP;
        setType(ModelType.CLOCK);
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
        clockData.set(CLOCK_SPEED, new DataStringEntry(String.valueOf(clockSpeed)));
        clockData.set(CLOCK_MODE, new DataStringEntry(mode.toString()));
        if (activeTick) {
            clockData.set(CLOCK_ACTIVE, new DataStringEntry("true"));         //ToDo hier wäre ein boolean data entry schlau, wollen wir glaub eh nicht speichern
        } else {
            clockData.set(CLOCK_ACTIVE, new DataStringEntry("false"));
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

    /**
     * Notifies all observers about a state change.
     */
    @Override
    public void notifyObservers() {
        for (ISimulationTimingObserver observer: modeObservers) {
            observer.updateObservedState();
        }
    }

    public int getClockSpeed() {
        return clockSpeed;
    }

    /**
     * Setter for the clock speed. Notifies the observers about the state change.
     * @param clockSpeed the new clock speed.
     */
    public void setClockSpeed(int clockSpeed) {
        this.clockSpeed = clockSpeed;
        notifyObservers();
        setUnqueriedStateChange(true);
    }

    public ClockMode getClockMode() {
        return mode;
    }

    /**
     * Setter for the active tick attribute.
     * @param activeTick the new active tick attribute.
     */
    public void setActiveTick(boolean activeTick) {
        this.activeTick = activeTick;
        setUnqueriedStateChange(true);
    }

    /**
     * Setter for the clock mode. Notifies the observers about the state change.
     * @param mode the new clock mode.
     */
    public void setClockMode(ClockMode mode) {
        this.mode = mode;
        notifyObservers();
        setUnqueriedStateChange(true);
    }

}
