package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;


/**
 * Represents one instruction in the InstructionListWidget of the InstructionsWidget.
 */
public class InstructionEntry extends ListEntry {

    private static final int ENTRY_HEIGHT = 11;
    private static final int ENTRY_WIDTH = 113;
    private static final int OFFSET = 34;
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

        if(this.identifier.length() > 5){
            String shortenedIdentifier = this.identifier.substring(0, 4) +"...";
            context.drawText(client.textRenderer, shortenedIdentifier, this.x + 1, this.y + 1, 0xffffff, false);
        }else {
            context.drawText(client.textRenderer, this.identifier, this.x + 1, this.y + 1, 0xffffff, false);
        }

        if (this.arguments.length() > 15) {
            String shortenedArguments = this.arguments.substring(0, 14) + "...";
            context.drawText(client.textRenderer, shortenedArguments, this.x + OFFSET, this.y + 1, 0xffffff, false);
        } else {
            context.drawText(client.textRenderer, this.arguments, this.x + OFFSET, this.y + 1, 0xffffff, false);
        }
    }

    @Override
    public int getHeight() {
        return ENTRY_HEIGHT;
    }

    /**
     * Getter for the width of the entry.
     * @return The width of the entry.
     */
    @Override
    public int getWidth() {
        return ENTRY_WIDTH;
    }

    /**
     * Method drawing a tooltip.
     * @param context The context tooltip.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param delta time since drawn the last time.
     */
    public void drawTooltip(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if(this.isMouseOver(mouseX, mouseY)) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0f, 0.0f, 100.0f);

            context.drawTooltip(client.textRenderer, Text.literal(this.identifier + ": " + this.arguments), mouseX, mouseY);

            context.getMatrices().pop();
        }
    }

}
