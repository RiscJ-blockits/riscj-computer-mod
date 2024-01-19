package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;

/**
 * Defines all Assembler Controllers that can be queried by the view.
 */
public interface IAssemblerController extends IUserInputReceivableController {

    /**
     * Assembles the given code and stores the result in the given data container.
     * @param code The code that should be assembled.
     * @param instructionSetData  The data container with the instruction set that should be used to assemble the code.
     * @throws AssemblyException
     * @return The assembled code.
     */
    IDataElement assemble(String code, IDataElement instructionSetData) throws AssemblyException;

}
