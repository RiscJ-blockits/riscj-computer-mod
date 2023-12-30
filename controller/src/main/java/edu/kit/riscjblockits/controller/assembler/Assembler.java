package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.Memory;
import edu.kit.riscjblockits.model.Value;
import edu.kit.riscjblockits.model.instructionset.Instruction;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class represents an assembler that translates assembly code into machine code
 */
public class Assembler {

    /**
     * the instruction set model that is used for the assembly
     */
    private final InstructionSetModel instructionSetModel;


    /**
     * the memory to write the assembled code to
     */
    private final Memory memory;

    /**
     * the current address to write to
     */
    private Value currentAddress;

    /**
     * the size of the memory address in bytes
     */
    private int calculatedMemoryAddressSize;

    /**
     * the size of the memory word in bytes
     */
    private int calculatedMemoryWordSize;

    /**
     * Constructor for an assembler
     * @param instructionSetModel the instruction set model to use for the assembly
     */
    public Assembler(InstructionSetModel instructionSetModel) {
        this.instructionSetModel = instructionSetModel;
        int memoryAddressSize = instructionSetModel.getMemoryAddressSize();
        int memoryWordSize = instructionSetModel.getMemoryWordSize();

        calculatedMemoryAddressSize = memoryAddressSize / 8 + memoryAddressSize % 8 > 0 ? 1 : 0;
        calculatedMemoryWordSize = memoryWordSize / 8 + memoryWordSize % 8 > 0 ? 1 : 0;

        memory = new Memory(
            calculatedMemoryAddressSize,
            calculatedMemoryWordSize
        );
        currentAddress = new Value(new byte[calculatedMemoryAddressSize]);
    }

    /**
     * Assembles the given assembly code and writes it to the memory
     * @param assemblyCode the assembly code to assemble
     * @throws AssemblyException if the assembly code cant be assembled
     */
    public void assemble(String assemblyCode) throws AssemblyException {
        Pattern addressChangePattern = Pattern.compile(instructionSetModel.getAddressChangeRegex());
        String[] lines = assemblyCode.split("\n");
        for (String line : lines) {
            // skip empty lines
            if (line.matches(" *"))
                continue;

            // check if line is address change
            Matcher matcher = addressChangePattern.matcher(line);
            if (matcher.matches()) {
                String address = matcher.group(1);
                currentAddress = ValueExtractor.extractValue(address, calculatedMemoryAddressSize);
                continue;
            }

            // assemble command
            Command command = getCommandForLine(line);

            // write command to memory
            memory.setValue(currentAddress, command.asValue());

            // increment memory address
            currentAddress = currentAddress.getIncrementedValue();
        }
    }

    /**
     * Gets the command for a given line
     * @param line the line to get the command for
     * @return the command for the given line
     * @throws AssemblyException if the command cant be assembled
     */
    private Command getCommandForLine(String line) throws AssemblyException {
        String[] cmd = line.split(" ");
        Instruction instruction = instructionSetModel.getInstruction(cmd[0]);
        if (instruction == null) {
            throw new AssemblyException("Unknown instruction");
        }
        String[] arguments = Arrays.copyOfRange(cmd, 1, cmd.length);
        return new Command(instruction, arguments);
    }

    /**
     * Gets the memory that was written to if assembled
     * @return the memory that was written to
     */
    public Memory getMemory() {
        return memory;
    }

}
