package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ProgrammingControllerTest {

    @Test
    void assemble() throws AssemblyException, IOException {
        // Arrange
        String code = "ADD 16";
        String instructionSetString = new String(
            InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetMIMA.jsonc").readAllBytes());

        IDataElement instructionSetData = new DataStringEntry(instructionSetString);
        ProgrammingController programmingController = new ProgrammingController();

        // Act
        IDataElement result = programmingController.assemble(code, instructionSetData);

        // Assert
        assertNotNull(result);
    }

    @Test
    void assembleInvInst() {
        // Arrange
        String code = "ADD 16";
        String instructionSetString = "surely an invalid instruction set";
        IDataElement instructionSetData = new DataStringEntry(instructionSetString);
        ProgrammingController programmingController = new ProgrammingController();

        // Act
        assertThrows(AssemblyException.class, () -> programmingController.assemble(code, instructionSetData));
    }

    @Test
    void assembleInvData() {
        // Arrange
        String code = "ADD 16";
        IDataElement instructionSetData = new Data();
        ProgrammingController programmingController = new ProgrammingController();

        // Act
        assertThrows(AssemblyException.class, () -> programmingController.assemble(code, instructionSetData));
    }

    @Test
    void dataIsIgnored() {
        ProgrammingController programmingController = new ProgrammingController();
        IDataElement data = new DataStringEntry("data");
        programmingController.setData(data);
    }
}