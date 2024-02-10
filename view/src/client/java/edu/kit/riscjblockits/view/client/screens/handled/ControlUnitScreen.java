package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.view.client.screens.widgets.ArchitectureEntry;
import edu.kit.riscjblockits.view.client.screens.widgets.ArchitectureListWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.MIMAExWidget;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the screen of the control unit block in the game.
 * It can display the contents of the cluster and the instruction set.
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
    private boolean isMima;

    /**
     * Creates a new ControlUnitScreen with the given settings.
     * It can display the contents of the cluster and the instruction set.
     * @param handler The handler of the screen.
     * @param inventory The inventory of the player that opened the screen.
     * @param title The title of the screen.
     */
    public ControlUnitScreen(ControlUnitScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 222;
        this.backgroundWidth = 176;
        playerInventoryTitleY += 56;
        isMima = false;
    }

    /**
     * Initializes the screen.
     * Adds all the widgets and buttons to the screen.
     */
    @Override
    protected void init() {
        super.init();
        ClientPlayNetworking.send(NetworkingConstants.REQUEST_DATA, PacketByteBufs.create().writeBlockPos(handler.getBlockEntity().getPos()));
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
     * Draws the background of the screen.
     * @param context The context to draw in.
     * @param delta
     * @param mouseX the x position of the mouse
     * @param mouseY the y position of the mouse
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
     * Draws the screen.
     * Is called every frame.
     * Is used to update the component list.
     * @param context The context to draw in.
     * @param mouseX the x position of the mouse
     * @param mouseY the y position of the mouse
     * @param delta the time since the last tick
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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        architectureList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    /**
     * @return The entries of the architecture list.
     * Contains all components of the cluster and all missing components specified in the instruction set.
     */
    public List<ArchitectureEntry> fetchEntries() {
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
