package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ArchitectureEntry extends ListEntry{

    public static final Identifier COMPONENT_MISSING_ICON = new Identifier(RISCJ_blockits.MODID, "textures/gui/general/cancel.png");
    public static final Identifier COMPONENT_FOUND_ICON = new Identifier(RISCJ_blockits.MODID, "textures/gui/general/confirm.png");
    public static final Identifier COMPONENT_BACKGROUND = new Identifier(RISCJ_blockits.MODID, "textures/gui/general/selected.png");
    private final String name;
    private final boolean missing;
    private final int ENTRY_HEIGHT = 20;

    public ArchitectureEntry(String name, boolean missing) {
        this.name = name;
        this.missing = missing;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(COMPONENT_BACKGROUND, this.getX() + 16, this.getY(), 0, 0, 128, 16);
        if(missing) {
            context.drawTexture(COMPONENT_MISSING_ICON, this.getX(), this.getY(), 0, 0, 16, 16);
        } else {
            context.drawTexture(COMPONENT_FOUND_ICON, this.getX(), this.getY(), 0, 0, 16, 16);
        }
        MinecraftClient client = MinecraftClient.getInstance();
        context.drawText(client.textRenderer, Text.literal(name), this.x + 2, this.y + 2, 0x555555, false);
    }
}
