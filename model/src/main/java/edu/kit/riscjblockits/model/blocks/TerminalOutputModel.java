package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_OUTPUT;

/**
 * Represents the data and state of a terminal output register.
 * Stores the output of the terminal and provides it to the view.
 */
public class TerminalOutputModel extends RegisterModel {

    private String output;

    /**
     * Constructor. Returns the model for a terminal output register.
     * Initializes the output with an empty string.
     */
    public TerminalOutputModel() {
        super();
        output = "";
    }

    /**
     * Setter for the value of the register.
     * Also starts an update of the view.
     * @param value The value to set the register to.
     */
    @Override
    public void setValue(Value value) {
        output = output + translateHexToAscii(value.getHexadecimalValue());
        super.setValue(value);
    }

    private String translateHexToAscii(String hexStr) {
        hexStr = hexStr.replaceFirst("^0+", ""); // remove leading zeros
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str;
            if(i + 2 <= hexStr.length()){
                str = hexStr.substring(i, i + 2);
            } else {
                str = hexStr.substring(i);
            }
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    @Override
    public IDataElement getData() {
        Data regData = (Data) super.getData();
        regData.set(REGISTER_TERMINAL_OUTPUT, new DataStringEntry(output));
        return regData;
    }

}
