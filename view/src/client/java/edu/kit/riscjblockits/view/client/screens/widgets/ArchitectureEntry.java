package edu.kit.riscjblockits.view.client.screens.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ArchitectureEntry extends ListEntry{

    public static final Identifier COMPONENT_MISSING = new Identifier(RISCJ_blockits.MODID, "textures/gui/control_unit/controlunit_gui_entry_missing.png");
    public static final Identifier COMPONENT_FOUND = new Identifier(RISCJ_blockits.MODID, "textures/gui/control_unit/controlunit_gui_entry_found.png");
    private final String name;
    private final boolean missing;
    private final int ENTRY_HEIGHT = 20;
    private final int ENTRY_WIDTH = 120;

    public ArchitectureEntry(String name, boolean missing) {
        this.name = name;
        this.missing = missing;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        int i = this.getX();
        int j = this.getY();
        RenderSystem.disableDepthTest();

        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 100.0f);

        if (missing) {
            context.drawTexture(COMPONENT_MISSING, i, j, 0, 0, ENTRY_WIDTH, ENTRY_HEIGHT, ENTRY_WIDTH,
                ENTRY_HEIGHT);
        } else {
            context.drawTexture(COMPONENT_FOUND, i, j, 0, 0, ENTRY_WIDTH, ENTRY_HEIGHT, ENTRY_WIDTH,
                ENTRY_HEIGHT);
        }

        MinecraftClient client = MinecraftClient.getInstance();
        context.drawText(client.textRenderer, Text.literal(name), this.x + 2, this.y + 2, 0x555555, false);

        context.getMatrices().pop();
        RenderSystem.enableDepthTest();


    }

    @Override
    public int getHeight() {
        return ENTRY_HEIGHT;
    }
}
