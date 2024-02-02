package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.client.screens.widgets.ArchitectureEntry;
import edu.kit.riscjblockits.view.client.screens.widgets.ArchitectureListWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.MIMAExWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.RegSelectWidget;
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

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/control_unit/control_unit_gui.png");
    private ArchitectureListWidget architectureList;
    private final MIMAExWidget mimaExWidget = new MIMAExWidget();
    private boolean narrow;
    private boolean isMima;
    private Text cu1 = Text.literal("-");        //Testcode

    public ControlUnitScreen(ControlUnitScreenHandler handler, PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 256;
        this.backgroundWidth = 176;
        playerInventoryTitleY += 56;
        isMima = true; //TODO get the instruction set
    }

    @Override
    protected void init() {
        super.init();
        this.narrow = this.width < 379;
        if(isMima) {
            this.mimaExWidget.initalize(this.width, this.height - backgroundHeight, this.narrow);

            this.addDrawableChild(new TexturedButtonWidget(this.x + 5, this.height / 2 - 49, 20, 18, MIMAExWidget.BUTTON_TEXTURES, button -> {
                this.mimaExWidget.toggleOpen();
                this.x = this.mimaExWidget.findLeftEdge(this.width, this.backgroundWidth);
                button.setPosition(this.x + 5, this.height / 2 - 49);
            }));
            this.addSelectableChild(this.mimaExWidget);
            this.setInitialFocus(this.mimaExWidget);
        }

        this.architectureList = new ArchitectureListWidget(this, this.x + 30, this.y + 18, 120, 108, 6); //TODO fix values

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
        drawMouseoverTooltip(context, mouseX, mouseY);

        architectureList.setX(this.x + 30);
        architectureList.setY(this.y + 18);
        this.architectureList.render(context, mouseX, mouseY, delta);

        if(isMima) {
            this.mimaExWidget.render(context, mouseX, mouseY, delta);
        }
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

    public List<ArchitectureEntry> fetchEntries() {
        BlockPos pos = this.handler.getBlockEntity().getPos();
        List<ArchitectureEntry> entries = new ArrayList<>();
        for (String component: this.handler.getStructure("missing")) { //TODO fix key, cooperate with Leon
            entries.add(new ArchitectureEntry(component, true));
        }
        for (String component: this.handler.getStructure("found")) {   //TODO fix key, cooperate with Leon
            entries.add(new ArchitectureEntry(component, false));
        }
        return entries;
    }

}
