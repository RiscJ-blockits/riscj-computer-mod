package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.TerminalOutputModel;

/**
 * The controller for a terminal output block entity.
 */
public class TerminalOutputController extends RegisterController {

    /**
     * Creates a new TerminalOutputController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public TerminalOutputController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
    }

    /**
     * Creates the TerminalOutput-specific model.
     * @return The model for the TerminalOutput.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new TerminalOutputModel();
    }

}
