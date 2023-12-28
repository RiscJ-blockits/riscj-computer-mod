package edu.kit.riscjblockits.model.blocks;

public class SystemClockModel extends BlockModel{
    @Override
    public boolean getHasUnqueriedStateChange() {
        return false;
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
}
