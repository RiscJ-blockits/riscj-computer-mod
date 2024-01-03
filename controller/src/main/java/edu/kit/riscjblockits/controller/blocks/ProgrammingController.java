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


public class ProgrammingController extends BlockController implements IAssemblerController {

    public ProgrammingController() {

    }

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

    @Override
    public void setData(IDataElement data) {

    }
}
