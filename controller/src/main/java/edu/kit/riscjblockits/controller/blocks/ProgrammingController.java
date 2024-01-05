package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.assembler.Assembler;
import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.model.data.*;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * The controller for a Programming block entity.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class ProgrammingController extends BlockController implements IAssemblerController {

    /**
     * Assembles the given code and stores the result in the given data container.
     * @param code The code that should be assembled.
     * @param instructionSetData  The data container with the instruction set that should be used to assemble the code.
     * @throws AssemblyException
     * @return The assembled code.
     */
    public IDataElement assemble(String code, IDataElement instructionSetData)
        throws AssemblyException {


        InputStream instructionSetStream = getInputStream(instructionSetData);
        Assembler assembler;
        try {
            assembler = new Assembler(InstructionSetBuilder.buildInstructionSetModel(instructionSetStream));
        } catch (UnsupportedEncodingException e) {
            throw new AssemblyException("Instruction set is not readable");
        }
        assembler.assemble(code);
        return assembler.getMemoryData();
    }

    private InputStream getInputStream(IDataElement instructionSetData) throws AssemblyException {
        if (!instructionSetData.isContainer()) {
            throw new AssemblyException("Instruction set data is not a container");
        }
        IDataElement instructionSetElement = ((IDataContainer) instructionSetData).get("instructionSetJSON");

        if (!instructionSetElement.isEntry()) {
            throw new AssemblyException("Instruction set data does not contain instructionSetJSON");
        }
        IDataEntry instructionSetEntry = (IDataEntry) instructionSetElement;
        if (instructionSetEntry.getType() != DataType.STRING) {
            throw new AssemblyException("Instruction set data does not contain instructionSetJSON");
        }

        String instructionSetJSON = ((IDataStringEntry) instructionSetEntry).getContent();

        InputStream instructionSetStream = new ByteArrayInputStream(instructionSetJSON.getBytes(StandardCharsets.UTF_8));
        return instructionSetStream;
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        //ToDo
    }

}
