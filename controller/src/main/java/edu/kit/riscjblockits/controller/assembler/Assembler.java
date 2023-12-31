package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.Memory;
import edu.kit.riscjblockits.model.Value;
import edu.kit.riscjblockits.model.instructionset.Instruction;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class represents an assembler that translates assembly code into machine code
 * it will write the machine code to a memory
 */
public class Assembler {

    private static final Pattern LABEL_COMMAND_PATTERN = Pattern.compile(" *(?:(?<label>\\w+):)? *(?<command>\\w.*)? *");
    /**
     * the {@link InstructionSetModel} that is used for the assembly
     */
    private final InstructionSetModel instructionSetModel;


    /**
     * the {@link Memory} to write the assembled code to
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

    private final Map<String, Value> labels;

    private final Pattern addressChangePattern;

    /**
     * Constructor for an {@link Assembler}
     * will create a new {@link Memory} with the address and word size of the {@link InstructionSetModel}
     * @param instructionSetModel the instruction set model to use for the assembly
     */
    public Assembler(InstructionSetModel instructionSetModel) {
        labels = new HashMap<>();
        this.instructionSetModel = instructionSetModel;
        int memoryAddressSize = instructionSetModel.getMemoryAddressSize();
        int memoryWordSize = instructionSetModel.getMemoryWordSize();

        calculatedMemoryAddressSize = memoryAddressSize / 8 + (memoryAddressSize % 8 > 0 ? 1 : 0);
        calculatedMemoryWordSize = memoryWordSize / 8 + (memoryWordSize % 8 > 0 ? 1 : 0);

        memory = new Memory(
            calculatedMemoryAddressSize,
            calculatedMemoryWordSize
        );
        currentAddress = new Value(new byte[calculatedMemoryAddressSize]);
        addressChangePattern = Pattern.compile(instructionSetModel.getAddressChangeRegex());
    }

    /**
     * Assembles the given assembly code and writes it to the {@link Memory}
     * @param assemblyCode the assembly code to assemble
     * @throws AssemblyException if the assembly code cant be assembled
     */
    public void assemble(String assemblyCode) throws AssemblyException {
        String[] lines = assemblyCode.split("\n");
        // precompile to get labels in the future
        extractLabelPositions(lines);
        for (String line : lines) {
            // skip empty lines
            if (line.matches(" *"))
                continue;

            // check if line is address change
            Matcher matcher = addressChangePattern.matcher(line);
            if (matcher.matches()) {
                String address = matcher.group("address");
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
     * Gets the {@link Command} for a given line
     * will also detect and save labels
     * @param line the line to get the command for
     * @return the command for the given line
     * @throws AssemblyException if the command cant be assembled
     */
    private Command getCommandForLine(String line) throws AssemblyException {
        Matcher matcher = LABEL_COMMAND_PATTERN.matcher(line);

        if (!matcher.matches()) {
            throw new AssemblyException("Invalid line");
        }

        String label = matcher.group("label");
        String command = matcher.group("command");

        if (label != null) {
            labels.put(label, currentAddress);
        }

        String[] cmd = command.split(" +,?");
        Instruction instruction = instructionSetModel.getInstruction(cmd[0]);
        if (instruction == null) {
            throw new AssemblyException("Unknown instruction");
        }
        String[] arguments = Arrays.copyOfRange(cmd, 1, cmd.length);
        writeLabelsToArguments(arguments);
        return new Command(instruction, arguments);
    }

    private void extractLabelPositions(String[] lines) {
        // keep own address, so assembling does get messed up
        Value currentAddress = new Value(new byte[calculatedMemoryAddressSize]);
        for (String line : lines) {
            // skip empty lines
            if (line.matches(" *"))
                continue;

            // check if line is address change
            Matcher matcher = addressChangePattern.matcher(line);
            if (matcher.matches()) {
                String address = matcher.group("address");
                currentAddress = ValueExtractor.extractValue(address, calculatedMemoryAddressSize);
                continue;
            }

            Matcher labelMatcher = LABEL_COMMAND_PATTERN.matcher(line);
            if (!labelMatcher.matches()) {
                continue;
            }
            String label = labelMatcher.group("label");
            if (label != null) {
                labels.put(label, currentAddress);
            }
            // increment memory address
            currentAddress = currentAddress.getIncrementedValue();
        }
    }

    private void writeLabelsToArguments(String[] arguments) {
        for (int i = 0; i < arguments.length; i++) {
            String argument = arguments[i];
            if (labels.containsKey(argument)) {
                arguments[i] = "0x" + labels.get(argument).getHexadecimalValue();
            }
        }
    }

    /**
     * Gets the {@link Memory} that was written to if code was assembled
     * @return the memory that was written to
     */
    public Memory getMemory() {
        return memory;
    }

}
