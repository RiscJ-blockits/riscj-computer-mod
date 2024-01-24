package edu.kit.riscjblockits.model.instructionset;

import java.util.List;

//zwischen Controller und InstructionSet
public interface IQueryableInstructionSetModel {

    /**
     * Getter for the registers of the instruction set.
     * @return Registers of the instruction set.
     */
    String[] getAluRegisters();

    /**
     * Getter for the name of the instruction set.
     * @return Name of the instruction set.
     */
    String getName();

    /**
     * Getter for the integer registers of the instruction set.
     * @param key Key of the register.
     * @return Integer register matching the key.
     */
    Integer getIntegerRegister(String key);

    /**
     * Getter for the program counter register of the instruction set.
     * @return Program counter register of the instruction set.
     */
    String getProgramCounter();

    /**
     * Getter for the word size of the instruction set memory.
     * @return Word size of the instruction set memory.
     */
    int getMemoryWordSize();

    /**
     * Getter for the address size of the instruction set memory.
     * @return Address size of the instruction set memory.
     */
    int getMemoryAddressSize();

    /**
     * Getter for an instruction specified by its command keyword.
     * @param s Command keyword of the instruction.
     * @return Instruction matching the command keyword.
     */
    Instruction getInstruction(String s);

    /**
     * Checks if a string is the address change specification of the instruction set.
     * @param s String to check.
     * @return True if the string is the address change specification of the instruction set, false otherwise.
     */
    boolean isAddressChange(String s);

    /**
     * Resolves an address change label to an address.
     * @param s The address change label.
     * @return The address matching the label.
     */
    String getChangedAddress(String s);

    /**
     * Getter for the program start label.
     * @return The program start label.
     */
    String getProgramStartLabel();

    /**
     * Checks if a string is a data storage command.
     * @param s The string to check.
     * @return True if the string is a data storage command, false otherwise.
     */
    boolean isDataStorageCommand(String s);

    /**
     * Resolves a data storage command to the data that shall be stored in the needed format.
     * @param s The data storage command.
     * @return The data to be stored.
     */
    public String getStorageCommandData(String s);

    /**
     * Getter for the fetch phase length of the instruction set.
     * @return Fetch phase length of the instruction set.
     */
    int getFetchPhaseLength();

    /**
     * Getter for a microinstruction of the fetch phase of the instruction set.
     * @param index Index of the microinstruction in the fetch phase.
     * @return The microinstruction at the specified index.
     */
    IExecutableMicroInstruction getFetchPhaseStep(int index);

    /**
     * ToDo nicht im Entwurf
     * @return Returns the names of all registers.
     */
    List<String> getRegisterNames();

}
