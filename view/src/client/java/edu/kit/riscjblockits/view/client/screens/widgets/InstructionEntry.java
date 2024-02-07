package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class InstructionEntry extends ListEntry{

    private static final int ENTRY_HEIGHT = 11;
    private static final int ENTRY_WIDTH = 113;
    private static final int OFFSET = 34;
    private final String arguments;
    private final String identifier;


    public InstructionEntry(String identifier, String arguments) {
        this.arguments = arguments;
        this.identifier = identifier;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();

        context.drawText(client.textRenderer, this.identifier, this.x + 1, this.y + 1, 0xffffff, false);
        context.drawText(client.textRenderer, this.arguments, this.x + OFFSET, this.y + 1 , 0xffffff, false);
    }

    @Override
    public int getHeight() {
        return ENTRY_HEIGHT;
    }
}
