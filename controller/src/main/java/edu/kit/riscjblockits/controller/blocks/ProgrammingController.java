package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.assembler.Assembler;
import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.model.data.DataType;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataEntry;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;

/**
 * The controller for a Programming block entity.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class ProgrammingController extends BlockController implements IAssemblerController {

    /**
     * Constructor for the ProgrammingController.
     */
    public ProgrammingController() {
        super(BlockControllerType.PROGRAMMING);
    }

    /**
     * Assembles the given code and stores the result in the given data container.
     * @param code The code that should be assembled.
     * @param instructionSetData  The data container with the instruction set that should be used to assemble the code.
     * @throws AssemblyException If the code could not be assembled.
     * @return The assembled code.
     */
    public IDataElement assemble(String code, IDataElement instructionSetData)
        throws AssemblyException {

        InputStream instructionSetStream = getInputStream(instructionSetData);
        Assembler assembler;
        try {
            assembler = new Assembler(InstructionSetBuilder.buildInstructionSetModel(instructionSetStream));
        } catch (InstructionBuildException e) {
            throw new AssemblyException("Instruction set is not readable");
        }
        assembler.assemble(code);
        return assembler.getMemoryData();
    }

    private InputStream getInputStream(IDataElement instructionSetElement) throws AssemblyException {
        if (!instructionSetElement.isEntry()) {
            throw new AssemblyException("Instruction set data does not contain " + CONTROL_IST_ITEM);
        }
        IDataEntry instructionSetEntry = (IDataEntry) instructionSetElement;
        if (instructionSetEntry.getType() != DataType.STRING) {
            throw new AssemblyException("Instruction set data does not contain " + CONTROL_IST_ITEM);
        }

        String instructionSetJSON = ((IDataStringEntry) instructionSetEntry).getContent();

        return new ByteArrayInputStream(instructionSetJSON.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        //data is not used in this controller
    }

}
