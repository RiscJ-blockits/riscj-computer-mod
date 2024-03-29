package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;

/**
 * Defines all Controllers.
 * Every Mod block Entity has a Controller.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public abstract class BlockController implements IUserInputReceivableController {

    /**
     * Type of the controller.
     * It is used to differentiate in a List of Controllers.
     */
    private BlockControllerType controllerType;

    /**
     * Default Constructor. Creates an Undefined Controller.
     * @param controllerType The type of the controller.
     */
    protected BlockController(BlockControllerType controllerType) {
        this.controllerType = controllerType;
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * Triggered by a reload of the minecraft block or a user input.
     * @param data The data to be set.
     */
    public void setData(IDataElement data) {}

    /**
     * Getter for the controller type.
     * @return The controller type.
     */
    public BlockControllerType getControllerType() {
        return controllerType;
    }

    /**
     * Setter for the controller type.
     * @param controllerType Sets a new controller type.
     */
    protected void setControllerType(BlockControllerType controllerType) {
        this.controllerType = controllerType;
    }

}
