package edu.kit.riscjblockits.view.main.data;

import edu.kit.riscjblockits.model.data.IDataStringEntry;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;

/**
 * This class represents a string entry in the data tree.
 * Allows for conversion to NBT.
 */
public class DataStringEntry implements IDataStringEntry, IDataNBTConvertable {

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
     * Converts this string entry to a NBT element.
     * @return The NBT element.
     */
    @Override
    public NbtElement toNbt() {
        return NbtString.of(content);
    }
}
