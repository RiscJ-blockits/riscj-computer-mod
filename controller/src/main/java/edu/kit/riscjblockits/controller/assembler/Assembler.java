package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstruction;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import java.math.BigInteger;
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
    private static final Pattern LABEL_COMMAND_PATTERN = Pattern.compile(" *(?:(?<label>\\w+):)? *(?<command>[^;# ][^;#]*)? *(?:[;#].*)?");
    private static final Pattern ARGUMENT_REGISTER_PATTERN = Pattern.compile("-?\\d*\\((?<register>\\w+)\\)");
    private static final Pattern RELATIVE_LABEL_PATTERN = Pattern.compile("~\\[\\w+]");

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
        calculatedMemoryAddressSize = instructionSetModel.getMemoryAddressSize();
        calculatedMemoryWordSize = instructionSetModel.getMemoryWordSize();


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

            Matcher matcher = LABEL_COMMAND_PATTERN.matcher(line);

            if (!matcher.matches()) {
                throw new AssemblyException("Invalid line");
            }

            String label = matcher.group("label");
            String cmd = matcher.group("command");

            // line only contains a label --> next line
            if (cmd == null) {
                continue;
            }


            // check if line is data
            if (instructionSetModel.isDataStorageCommand(cmd)) {
                String unsplitDirtyData;
                try {
                    unsplitDirtyData = instructionSetModel.getStorageCommandData(cmd);
                } catch (IllegalArgumentException e) {
                    throw new AssemblyException("Invalid data: "+e.getMessage());
                }

                for (String dirtyData : unsplitDirtyData.split(";")) {
                    // split data into value and length
                    String[] data = dirtyData.split("~");
                    if (data.length != 2) {
                        throw new AssemblyException("Invalid data");
                    }
                    String cleanData = data[0];
                    int cleanLength = Integer.parseInt(data[1]);
                    // extract value and trim to length
                    Value value = ValueExtractor.extractValue(cleanData, calculatedMemoryWordSize);
                    String valueBinary = value.getBinaryValue();
                    Value trimmedValue = Value.fromBinary(valueBinary.substring(valueBinary.length() - cleanLength), calculatedMemoryWordSize);
                    // write trimmed value to memory
                    memory.setValue(currentAddress, trimmedValue);
                    currentAddress = currentAddress.getIncrementedValue();
                }
                continue;
            }

            // assemble command
            Command command = getCommandForLine(cmd);

            // write command to memory
            memory.setValue(currentAddress, command.asValue());

            // increment memory address
            currentAddress = currentAddress.getIncrementedValue();
        }

    }


    /**
     * Gets the {@link Command} for a given line
     * will also detect and save labels
     * @param command the line to get the command for
     * @return the command for the given line
     * @throws AssemblyException if the command cant be assembled
     */
    private Command getCommandForLine(String command) throws AssemblyException {

        String[] cmd = command.split(" *,? +");
        IQueryableInstruction instruction = instructionSetModel.getInstruction(cmd[0]);
        if (instruction == null) {
            throw new AssemblyException("Unknown instruction " + cmd[0]);
        }
        String[] arguments = Arrays.copyOfRange(cmd, 1, cmd.length);
        writeLabelsToArguments(arguments);
        makeLabelsRelative(arguments, instruction.getArguments());
        writeRegistersToArguments(arguments);
        return new Command(instruction, arguments);
    }

    private void makeLabelsRelative(String[] arguments, String[] arguments1) throws AssemblyException {
        for (int i = 0; i < arguments.length; i++) {
            String argument = arguments[i];
            // check if argument is a label --> replace with address
            if (RELATIVE_LABEL_PATTERN.matcher(arguments1[i]).matches()) {

                Value value = ValueExtractor.extractValue(argument, calculatedMemoryAddressSize);

                if (value == null) {
                    throw new AssemblyException("Invalid label " + argument);
                }

                BigInteger targetInt = new BigInteger(value.getByteValue());
                BigInteger currentInt = new BigInteger(currentAddress.getByteValue());

                BigInteger offset = targetInt
                        .subtract(currentInt)       // subtract current position to get difference
                        .subtract(BigInteger.ONE);  // subtract one to get the correct offset
                arguments[i] = "0x" + ValueExtractor.extractValue(offset.toString(), calculatedMemoryAddressSize).getHexadecimalValue();
            }
        }

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
            if (instructionSetModel.isDataStorageCommand(line)) {
                String unsplitDirtyData = instructionSetModel.getStorageCommandData(line);
                for (String dirtyData : unsplitDirtyData.split(",")) {
                    localCurrentAddress = localCurrentAddress.getIncrementedValue();
                }
                continue;
            }

            Matcher labelMatcher = LABEL_COMMAND_PATTERN.matcher(line);
            if (!labelMatcher.matches()) {
                continue;
            }
            String label = labelMatcher.group("label");
            if (label != null) {
                labels.put(label, localCurrentAddress);
                if (instructionSetModel.getProgramStartLabel().equals(label)) {
                    memory.setInitialProgramCounter(localCurrentAddress);
                }
            }

            // check if there is a command in the line or just a label
            String cmd = labelMatcher.group("command");

            // line only contains a label --> next line
            if (cmd == null) {
                continue;
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
            // check if argument is a register with offset --> replace register with address, fill with leading zeros to match even hex length
            Matcher matcher = ARGUMENT_REGISTER_PATTERN.matcher(argument);
            if (matcher.matches()) {
                String register = matcher.group("register");
                Integer registerInt = instructionSetModel.getIntegerRegister(register);
                String hex = Integer.toHexString(registerInt);
                arguments[i] = argument.replaceFirst("\\(\\w+\\)", "(0x" + "0".repeat(hex.length()%2) + hex + ")");
                continue;
            }
            // check if argument is an Integer register --> replace with address, fill with leading zeros to match even hex length
            Integer register = instructionSetModel.getIntegerRegister(argument);
            if (register != null) {
                String hex = Integer.toHexString(register);
                arguments[i] = "0x" + "0".repeat(hex.length()%2) + hex;
                continue;
            }
            // check if argument is a Float register --> replace with address, fill with leading zeros to match even hex length
            register = instructionSetModel.getFloatRegister(argument);
            if (register != null) {
                String hex = Integer.toHexString(register);
                arguments[i] = "0x" + "0".repeat(hex.length()%2) + hex;
                continue;
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
