package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.annotations.SerializedName;

public class InstructionSetMemory {
    @SerializedName(value = "word_length")
    int wordLength;
    @SerializedName(value = "address_length")
    int addressLenght;
    @SerializedName(value = "access_delay")
    int accessDelay;
    @SerializedName(value = "byte_order")
    String byteOrder;

    public InstructionSetMemory(int wordLength, int addressLenght, int accessDelay, String byteOrder) {
        this.wordLength = wordLength;
        this.addressLenght = addressLenght;
        this.accessDelay = accessDelay;
        this.byteOrder = byteOrder;
    }
}
