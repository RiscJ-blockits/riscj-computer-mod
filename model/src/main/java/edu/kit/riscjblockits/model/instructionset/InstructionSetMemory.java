package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model for the memory of the instruction set.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class InstructionSetMemory {

    /**
     * Length of a word in bits.
     */
    @SerializedName(value = "word_length")
    private final int wordLength;

    /**
     * Length of an address in bits.
     */
    @SerializedName(value = "address_length")
    private final int addressLenght;

    /**
     * Access delay in ticks.
     */
    @SerializedName(value = "access_delay")
    private final int accessDelay;

    /**
     * Byte order of the memory.
     */
    @SerializedName(value = "byte_order")
    private final String byteOrder;

    /**
     * List of possible opcode lengths in bits.
     */
    @SerializedName(value = "possible_opcode_lengths")
    private final List<Integer> possibleOpcodeLengths;

    /**
     * Position of the opcode in the instruction.
     */
    @SerializedName(value = "opcode_position")
    private final String opcodePosition;

    public InstructionSetMemory(int wordLength, int addressLenght, int accessDelay, String byteOrder, List<Integer> possibleOpcodeLengths, String opcodePosition) {
        this.wordLength = wordLength;
        this.addressLenght = addressLenght;
        this.accessDelay = accessDelay;
        this.byteOrder = byteOrder;
        this.possibleOpcodeLengths = possibleOpcodeLengths;
        this.opcodePosition = opcodePosition;
    }


    /**
     * Getter for the word size.
     * @return The word size in bits.
     */
    public int getWordSize() {
        return wordLength;
    }

    /**
     * Getter for the address size.
     * @return The address size in bits.
     */
    public int getAddressSize() {
        return addressLenght;
    }

    /**
     * Getter for the access delay.
     * @return The access delay in ticks.
     */
    public int getAccessDelay() {
        return accessDelay;
    }

    /**
     * Getter for the byte order.
     * @return The byte order as a string.
     */
    public String getByteOrder() {
        return byteOrder;
    }

    /**
     * Getter for the possible opcode lengths.
     * @return The possible opcode lengths as a list of integers.
     */
    public List<Integer> getPossibleOpcodeLengths() {
        return possibleOpcodeLengths;
    }

    /**
     * Getter for the opcode position.
     * @return The opcode position as a string.
     */
    public String getOpcodePosition() {
        return opcodePosition;
    }
}
