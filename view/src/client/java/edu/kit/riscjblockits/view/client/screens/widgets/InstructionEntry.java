package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class InstructionEntry extends ListEntry{

    private static final int ENTRY_HEIGHT = 11;
    private static final int ENTRY_WIDTH = 113;
    private static final int OFFSET = 34;
    private final String arguments;
    private final String identifier;
    private final InstructionListWidget parent;

    public InstructionEntry(String identifier, String arguments, InstructionListWidget parent) {
        this.arguments = arguments;
        this.identifier = identifier;
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
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

        /*
        if(this.isMouseOver(mouseX, mouseY)) {
            //context.disableScissor();

            context.getMatrices().push();
            context.getMatrices().translate(0.0f, 0.0f, 100.0f);

            context.drawTooltip(client.textRenderer, Text.literal(this.identifier + ": " + this.arguments), mouseX, mouseY);

            context.getMatrices().pop();

            //context.enableScissor(parent.getX(), parent.getY(), parent.getX() + parent.getWidth(), parent.getY() + parent.getHeight());
        }*/


    }

    @Override
    public int getHeight() {
        return ENTRY_HEIGHT;
    }

    @Override
    public int getWidth() {
        return ENTRY_WIDTH;
    }

    public void drawTooltip(DrawContext context, int mouseX, int mouseY, float delta){
        MinecraftClient client = MinecraftClient.getInstance();

        if(this.isMouseOver(mouseX, mouseY)) {
            //context.disableScissor();

            context.getMatrices().push();
            context.getMatrices().translate(0.0f, 0.0f, 100.0f);

            context.drawTooltip(client.textRenderer, Text.literal(this.identifier + ": " + this.arguments), mouseX, mouseY);

            context.getMatrices().pop();

            //context.enableScissor(parent.getX(), parent.getY(), parent.getX() + parent.getWidth(), parent.getY() + parent.getHeight());
        }
    }
}
