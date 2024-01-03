package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.assembler.Assembler;
import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.model.data.DataType;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataEntry;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
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
     * @param memoryData The data container that should be used to store the assembled code.
     * @throws AssemblyException
     */
    public void assemble(String code, IDataContainer instructionSetData, IDataContainer memoryData)
        throws AssemblyException {

        IDataElement instructionSetElement = instructionSetData.get("instructionSetJSON");
        if (!instructionSetElement.isEntry()) {
            return;
        }
        IDataEntry instructionSetEntry = (IDataEntry) instructionSetElement;
        if (instructionSetEntry.getType() != DataType.STRING) {
            return;
        }

        String instructionSetJSON = ((IDataStringEntry) instructionSetEntry).getContent();

        InputStream instructionSetStream = new ByteArrayInputStream(instructionSetJSON.getBytes(StandardCharsets.UTF_8));
        Assembler assembler;
        try {
            assembler = new Assembler(InstructionSetBuilder.buildInstructionSetModel(instructionSetStream));
        } catch (UnsupportedEncodingException e) {
            return;
        }
        assembler.assemble(code);
        assembler.getMemory().writeToData(memoryData);
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
