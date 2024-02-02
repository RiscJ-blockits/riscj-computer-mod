package edu.kit.riscjblockits.model.blocks;

//ToDo nicht im Entwurf

/**
 * This class represents the state of the visualisation of a block.
 * It can be active or inactive.
 */
public class VisualisationState {

    /**
     * States if the block is currently working and should be active.
     */
    private boolean isActive;

    /**
     * Creates a new VisualisationState.
     * The state is inactive by default.
     */
    public VisualisationState() {
        isActive = false;
    }

    public VisualisationState(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
