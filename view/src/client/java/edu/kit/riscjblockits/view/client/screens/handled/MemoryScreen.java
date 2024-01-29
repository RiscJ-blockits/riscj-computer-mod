package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.memory.MemoryScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MemoryScreen extends HandledScreen<MemoryScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/memory_block/memory_block_gui.png");
    private Text memory1 = Text.literal("-");        //Testcode
    private Text memory2 = Text.literal("-");        //Testcode
    private Text memory3 = Text.literal("-");        //Testcode

    public MemoryScreen(MemoryScreenHandler handler, PlayerInventory inventory,
                        Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 256;
        this.backgroundWidth = 176;
        playerInventoryTitleY += 56;
    }

    @Override
    protected void init() {
        super.init();

        //intit Screen Elements
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
        //Testcode
        context.drawCenteredTextWithShadow(textRenderer, memory1, width / 2, height / 2, 0xffffff);
        context.drawCenteredTextWithShadow(textRenderer, memory2, width / 2, height / 3, 0xffffff);
        context.drawCenteredTextWithShadow(textRenderer, memory3, width / 2, height / 4, 0xffffff);
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        //Testcode
        memory1 = Text.literal(handler.getMemoryLine(0));
        memory2 = Text.literal(handler.getMemoryLine(1));
        memory3 = Text.literal(handler.getMemoryLine(2));
    }

}
