package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.view.client.screens.widgets.ArchitectureEntry;
import edu.kit.riscjblockits.view.client.screens.widgets.ArchitectureListWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.MIMAExWidget;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ControlUnitScreen extends HandledScreen<ControlUnitScreenHandler> {

    private static final Identifier TEXTURE =
        new Identifier(RISCJ_blockits.MODID, "textures/gui/control_unit/control_unit_gui.png");
    private static final String MIMA = "MIMA"; //TODO match with the result for getInstructionSetType
    private ArchitectureListWidget architectureList;
    private final MIMAExWidget mimaExWidget = new MIMAExWidget();
    private TexturedButtonWidget expandButton;
    private boolean narrow;
    private boolean isMima;

    public ControlUnitScreen(ControlUnitScreenHandler handler, PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 256;
        this.backgroundWidth = 176;
        playerInventoryTitleY += 56;
        isMima = true;
    }

    @Override
    protected void init() {
        super.init();
        this.narrow = this.width < 379;

        this.mimaExWidget.initialize(this.width, this.height - backgroundHeight, this.narrow);
        expandButton =
            new TexturedButtonWidget(this.x + 5, this.height / 2 - 49, 20, 18, MIMAExWidget.BUTTON_TEXTURES, button -> {
                this.mimaExWidget.toggleOpen();
                this.x = this.mimaExWidget.findLeftEdge(this.width, this.backgroundWidth);
                button.setPosition(this.x + 5, this.height / 2 - 49);
            });

        this.addDrawableChild(expandButton);
        this.addSelectableChild(this.mimaExWidget);
        this.setInitialFocus(this.mimaExWidget);

        this.architectureList =
            new ArchitectureListWidget(this, this.x + 30, this.y + 18, 120, 105, 6);

    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = this.x;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        architectureList.setX(this.x + 30);
        architectureList.setY(this.y + 18);
        this.architectureList.render(context, mouseX, mouseY, delta);

        if (handler.getInstructionSetType().equals(MIMA)) {
            expandButton.visible = true;
            this.mimaExWidget.render(context, mouseX, mouseY, delta);
        } else {
            expandButton.visible = false;
        }
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        architectureList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public List<ArchitectureEntry> fetchEntries() {
        BlockPos pos = this.handler.getBlockEntity().getPos();
        List<ArchitectureEntry> entries = new ArrayList<>();
        List[] data = this.handler.getStructure();
        List<String> listFound = data[1];
        List<String> listMissing = data[0];
        for (String component : listMissing) {
            entries.add(new ArchitectureEntry(component, true));
        }
        for (String component : listFound) {

            if (component.equals(RegisterModel.UNASSIGNED_REGISTER)) {
                continue;
            }
            entries.add(new ArchitectureEntry(component, false));
        }
        return entries;
    }

}
