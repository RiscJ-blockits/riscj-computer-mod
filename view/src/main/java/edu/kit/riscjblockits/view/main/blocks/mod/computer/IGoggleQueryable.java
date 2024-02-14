package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import net.minecraft.text.Text;

/**
 * Represents a block entity which provides information for the goggle.
 */
public interface IGoggleQueryable {

    /**
     * Getter for the Text who should be displayed by the goggle.
     * @return the Text displayed by the google.
     */
    Text getGoggleText();

}
