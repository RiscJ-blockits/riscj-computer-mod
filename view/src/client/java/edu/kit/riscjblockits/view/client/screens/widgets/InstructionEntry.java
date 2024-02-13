package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.NotNull;

/**
 * A widget that displays on instruction.
 */
public class InstructionEntry extends ListEntry{

    private static final int OFFSET = 32;

    /**
     * The argument of the instruction.
     */
    private final String arguments;

    /**
     * The identifier of the instruction.
     */
    private final String identifier;

    /**
     * Constructor for the instruction entry.
     * @param identifier The identifier of the instruction.
     * @param arguments The argument of the instruction.
     */
    public InstructionEntry(String identifier, String arguments) {
        this.arguments = arguments;
        this.identifier = identifier;
    }

    /**
     * Renders the instruction entry.
     * @param context The context to render in.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param delta Not specified in the documentation.
     */
    @Override
    public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        context.drawText(client.textRenderer, this.identifier, this.x + 1, this.y + 1, 0xffffff, false);
        context.drawText(client.textRenderer, this.arguments, this.x + OFFSET, this.y + 1 , 0xffffff, false);
    }

}
