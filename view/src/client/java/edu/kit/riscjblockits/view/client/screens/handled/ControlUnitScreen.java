package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The screen for the control unit.
 * It can display information about the mima architecture and missing and available components.
 */
public class ControlUnitScreen extends HandledScreen<ControlUnitScreenHandler> {

    private static final Identifier TEXTURE =
        new Identifier(RISCJ_blockits.MODID, "textures/gui/control_unit/control_unit_gui.png");
    private static final String MIMA = "MIMA";
    private static final String RISCV = "RiscV";
    private ArchitectureListWidget architectureList;
    private final MIMAExWidget mimaExWidget = new MIMAExWidget();
    private TexturedButtonWidget expandButton;
    private boolean narrow;

    /**
     * Represents a control unit screen for a game.
     * It can display information about the mima architecture and missing and available components.
     * @param handler The control unit screen handler.
     * @param inventory The player's inventory that opened the screen.
     * @param title The title text of the screen.
     */
    public ControlUnitScreen(ControlUnitScreenHandler handler, PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 222;
        this.backgroundWidth = 176;
        playerInventoryTitleY += 56;
    }

    /**
     * Initializes the control unit screen.
     * Is called by minecraft when the screen is opened.
     * We add the List with components, the expanded button and the MIMA Widget.
     */
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

    /**
     * Draws the background of the control unit screen. Is called by minecraft when the screen is opened.
     * @param context The draw context.
     * @param delta The time since the last tick.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     */
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = this.x;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    /**
     * Is called by minecraft every frame when the screen is opened.
     * @param context The draw context.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param delta The time since the last frame.
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        this.architectureList.updateEntries(fetchEntries());
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

    /**
     * Handles a mouse click. Is called by minecraft.
     * We use it to enable scrolling in the architecture list.
     * @param mouseX the X coordinate of the mouse
     * @param mouseY the Y coordinate of the mouse
     * @param horizontalAmount the horizontal scroll amount
     * @param verticalAmount the vertical scroll amount
     * @return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
     */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        architectureList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    /**
     * Fetches the entries for the architecture list that displays which components are there and which are missing.
     * The entries are fetched from the handler.
     * @return a list of architecture entries.
     */
    public List<ArchitectureEntry> fetchEntries() {
        List<ArchitectureEntry> entries = new ArrayList<>();
        List[] data = this.handler.getStructure();
        List<String> listFound = data[1];
        List<String> listMissing = data[0];
        for (String component : listMissing) {
            entries.add(new ArchitectureEntry(component, true));
        }
        //we need to find identical entries that happen when you assign the same register multiple times.
        Map<String, Integer> map = new HashMap<>();
        for (String s : listFound) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        List<String> identicalEntries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                identicalEntries.add(entry.getKey());
            }
        }
        for (String component : listFound) {
            if (component.equals(RegisterModel.UNASSIGNED_REGISTER)) {
            } else if (identicalEntries.contains(component)) {
                entries.add(new ArchitectureEntry(component, true));
            } else {
                entries.add(new ArchitectureEntry(component, false));
            }
        }
        return entries;
    }

}
