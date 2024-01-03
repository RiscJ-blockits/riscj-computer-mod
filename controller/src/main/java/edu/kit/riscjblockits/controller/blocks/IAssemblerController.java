package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.model.data.IDataContainer;

//Zugriff von der Programming-Entity auf den Controller
public interface IAssemblerController extends IUserInputReceivableController {
    void assemble(String code, IDataContainer instructionSetData, IDataContainer memoryData) throws AssemblyException;

}
