package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.model.data.IDataContainer;

/**
 * Defines all Assembler Controllers that can be queried by the view.
 */
public interface IAssemblerController extends IUserInputReceivableController {

    /**
     * Assembles the given code and stores the result in the given data container.
     * @param code The code that should be assembled.
     * @param instructionSetData  The data container with the instruction set that should be used to assemble the code.
     * @param memoryData The data container that should be used to store the assembled code.
     * @throws AssemblyException
     */
    void assemble(String code, IDataContainer instructionSetData, IDataContainer memoryData) throws AssemblyException;

}
