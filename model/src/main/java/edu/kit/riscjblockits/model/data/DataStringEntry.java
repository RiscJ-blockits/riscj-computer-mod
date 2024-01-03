package edu.kit.riscjblockits.model.data;

import edu.kit.riscjblockits.model.data.IDataStringEntry;

/**
 * This class represents a string entry in the data tree.
 * Allows for conversion to NBT.
 */
public class DataStringEntry implements IDataStringEntry {

    /**
     * The content of this string entry.
     */
    private String content;

    /**
     * Creates a new string entry with the given content.
     *
     * @param string The content of the string entry.
     */
    public DataStringEntry(String string) {
        content = string;
    }

    /**
     * Gets the content of this string entry.
     * @return The content of this string entry.
     */
    @Override
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of this string entry.
     * @param content The new content of this string entry.
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Receives a {@link IDataVisitor} and calls the visit method on it.
     * @param visitor The visitor to be visited.
     */
    @Override
    public void receive(IDataVisitor visitor) {
        visitor.visit(this);
    }
}
