package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Represents an entry in the architecture list.
 * It has a name and a boolean value to indicate if the component is missing.
 */
public class ArchitectureEntry extends ListEntry{

    /**
     * The texture for missing components.
     */
    public static final Identifier COMPONENT_MISSING = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/control_unit/controlunit_gui_entry_missing.png");
    /**
     * The texture for found components.
     */
    public static final Identifier COMPONENT_FOUND = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/control_unit/controlunit_gui_entry_found.png");


    /**
     * The height of the entry.
     */
    private static final int ENTRY_HEIGHT = 20;

    /**
     * The width of the entry.
     */
    private static final int ENTRY_WIDTH = 120;

    private final String name;
    private final boolean missing;
    /**
     * Constructor for the ArchitectureEntry.
     * @param name the name of the component
     * @param missing a boolean value to indicate if the component is missing.
     *                Is displayed as a red cross if false and a green checkmark if true.
     */
    public ArchitectureEntry(String name, boolean missing) {
        this.name = name;
        this.missing = missing;
    }

    /**
     * Renders the entry.
     * @param context the draw context
     * @param mouseX the x position of the mouse
     * @param mouseY the y position of the mouse
     * @param delta the time since the last tick
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = this.getX();
        int j = this.getY();
        if (missing) {
            context.drawTexture(COMPONENT_MISSING, i, j, 0, 0, ENTRY_WIDTH, ENTRY_HEIGHT, ENTRY_WIDTH,
                ENTRY_HEIGHT);
        } else {
            context.drawTexture(COMPONENT_FOUND, i, j, 0, 0, ENTRY_WIDTH, ENTRY_HEIGHT, ENTRY_WIDTH,
                ENTRY_HEIGHT);
        }
        MinecraftClient client = MinecraftClient.getInstance();
        context.drawText(client.textRenderer, Text.literal(name), this.x + 2, this.y + 2, 0x555555, false);
    }

    /**
     * Getter for the height of the entry.
     * @return the height of the entry
     */
    @Override
    public int getHeight() {
        return ENTRY_HEIGHT;
    }

}
