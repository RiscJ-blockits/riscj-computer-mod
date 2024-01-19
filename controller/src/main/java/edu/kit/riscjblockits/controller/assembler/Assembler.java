package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstruction;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;


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

    /**
     * regex pattern to separate a lines label and command
     */
    private static final Pattern LABEL_COMMAND_PATTERN = Pattern.compile(" *(?:(?<label>\\w+):)? *(?<command>\\w.*)? *");

    /**
     * the {@link IQueryableInstructionSetModel} that is used for the assembly
     */
    private final IQueryableInstructionSetModel instructionSetModel;


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

    /**
     * the Map of all labels and their addresses labeling to
     */
    private final Map<String, Value> labels;

    /**
     * Constructor for an {@link Assembler}
     * will create a new {@link Memory} with the address and word size of the {@link IQueryableInstructionSetModel}
     * @param instructionSetModel the instruction set model to use for the assembly
     */
    public Assembler(IQueryableInstructionSetModel instructionSetModel) {
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

            if (instructionSetModel.isAddressChange(line)) {
                String address = instructionSetModel.getChangedAddress(line);
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
        IQueryableInstruction instruction = instructionSetModel.getInstruction(cmd[0]);
        if (instruction == null) {
            throw new AssemblyException("Unknown instruction");
        }
        String[] arguments = Arrays.copyOfRange(cmd, 1, cmd.length);
        writeLabelsToArguments(arguments);
        return new Command(instruction, arguments);
    }

    /**
     * extracts the addresses of all labels in the assembly code.
     * this is done by iterating over all commands, as well as respecting
     * address changes.
     *
     * @param lines the lines of assembly that are to be label checked
     */
    private void extractLabelPositions(String[] lines) {
        // keep own address, so assembling does get messed up
        Value localCurrentAddress = new Value(currentAddress.getByteValue());
        for (String line : lines) {
            // skip empty lines
            if (line.matches(" *"))
                continue;

            // check if line is address change
            if (instructionSetModel.isAddressChange(line)) {
                String address = instructionSetModel.getChangedAddress(line);
                localCurrentAddress = ValueExtractor.extractValue(address, calculatedMemoryAddressSize);
                continue;
            }

            Matcher labelMatcher = LABEL_COMMAND_PATTERN.matcher(line);
            if (!labelMatcher.matches()) {
                continue;
            }
            String label = labelMatcher.group("label");
            if (label != null) {
                labels.put(label, localCurrentAddress);
            }
            // increment memory address
            localCurrentAddress = localCurrentAddress.getIncrementedValue();
        }
    }

    /**
     * uses the detected Labels to fill those, when given as an argument
     *
     * @param arguments array of arguments that may have labels, in need to be replaced
     */
    private void writeLabelsToArguments(String[] arguments) {
        for (int i = 0; i < arguments.length; i++) {
            String argument = arguments[i];
            // check if argument is a label --> replace with address
            if (labels.containsKey(argument)) {
                arguments[i] = "0x" + labels.get(argument).getHexadecimalValue();
            }
        }
    }

    /**
     * writes the instruction-set's register addresses to the arguments
     *
     * @param arguments array of arguments that may have registers, in need to be replaced
     */
    private void writeRegistersToArguments(String[] arguments) {
        // for each argument:
        for (int i = 0; i < arguments.length; i++) {
            String argument = arguments[i];
            // check if argument is an Integer register --> replace with address
            Integer register = instructionSetModel.getIntegerRegister(argument);
            if (register != null) {
                arguments[i] = "0x" + Integer.toHexString(register);
                continue;
            }
            // check if argument is a Float register --> replace with address
            register = instructionSetModel.getFloatRegister(argument);
            if (register != null) {
                arguments[i] = "0x" + Integer.toHexString(register);
            }
        }
    }

    /**
     * Gets Data of the {@link Memory} that was written to if code was assembled
     * @return the memory that was written to
     */
    public IDataElement getMemoryData() {
        return memory.getData();
    }
}
