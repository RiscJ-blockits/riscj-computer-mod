package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;

/**
 * Defines all Controllers.
 * Every Mod block Entity has a Controller.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public abstract class BlockController {

    /**
     * Type of the controller.
     * It is used to differentiate in a List of Controllers.
     */
    private BlockControllerType controllerType;

    /**
     * Default Constructor. Creates an Undefined Controller.
     */
    protected BlockController() {
        controllerType = BlockControllerType.UNDEFINED;
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * Triggered by a reload of the minecraft block or a user input.
     * @param data
     */
    public void setData(IDataElement data) {
        //ToDo
    }

    /**
     * @return The controller type.
     */
    public BlockControllerType getControllerType() {
        return controllerType;
    }

    /**
     * @param controllerType Sets a new controller type.
     */
    protected void setControllerType(BlockControllerType controllerType) {
        this.controllerType = controllerType;
    }

}
