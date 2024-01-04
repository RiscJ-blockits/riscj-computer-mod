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
    int wordLength;

    /**
     * Length of an address in bits.
     */
    @SerializedName(value = "address_length")
    int addressLenght;

    /**
     * Access delay in ticks.
     */
    @SerializedName(value = "access_delay")
    int accessDelay;

    /**
     * Byte order of the memory.
     */
    @SerializedName(value = "byte_order")
    String byteOrder;

    /**
     * List of possible opcode lengths in bits.
     */
    @SerializedName(value = "possible_opcode_lengths")
    List<Integer> possibleOpcodeLengths;

    /**
     * Position of the opcode in the instruction.
     */
    @SerializedName(value = "opcode_position")
    String opcodePosition;

    public InstructionSetMemory(int wordLength, int addressLenght, int accessDelay, String byteOrder) {
        this.wordLength = wordLength;
        this.addressLenght = addressLenght;
        this.accessDelay = accessDelay;
        this.byteOrder = byteOrder;
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
}
