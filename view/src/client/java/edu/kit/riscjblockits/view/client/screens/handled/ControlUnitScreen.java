package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.client.screens.widgets.ArchitectureEntry;
import edu.kit.riscjblockits.view.client.screens.widgets.ArchitectureListWidget;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ControlUnitScreen extends HandledScreen<ControlUnitScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/control_unit/control_unit_gui.png");
    private ArchitectureListWidget architectureList;
    private Text cu1 = Text.literal("-");        //Testcode

    public ControlUnitScreen(ControlUnitScreenHandler handler, PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 256;
        this.backgroundWidth = 176;
        playerInventoryTitleY += 56;
    }

    @Override
    protected void init() {
        super.init();
        this.architectureList = new ArchitectureListWidget(handler, this.x + 8, this.y + 19, 160, 240, 6); //TODO fix values


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
        this.architectureList.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        //Testcode
        cu1 = Text.literal(handler.getClusteringData());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        architectureList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

}
