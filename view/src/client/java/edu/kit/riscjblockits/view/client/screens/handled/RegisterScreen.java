package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.client.screens.widgets.RegSelectWidget;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * The screen for the register block.
 * In it, you can see the value of the register and change the register type.
 */
public class RegisterScreen extends HandledScreen<RegisterScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/register/register_block_gui.png");

    /**
     * In this widget, you can change the register type. It displays all available register types.
     */
    private final RegSelectWidget regSelectWidget = new RegSelectWidget();

    /**
     * Whether the regSelectWidget is open or not.
     */
    private boolean narrow;
    private Text registerValue = Text.literal("0");

    /**
     * Constructor for the register screen.
     * In it, you can see the value of the register and change the register type.
     * @param handler The handler for the screen
     * @param inventory The player inventory
     * @param title The title of the screen
     */
    public RegisterScreen(RegisterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    /**
     * Initializes the screen.
     * Gets called when the screen is opened.
     * It sends a request to the server to update the register value.
     * It initializes the regSelectWidget and the button to open it.
     */
    @Override
    protected void init() {
        super.init();
        ClientPlayNetworking.send(NetworkingConstants.REQUEST_DATA, PacketByteBufs.create().writeBlockPos(handler.getBlockEntity().getPos()));
        this.narrow = this.width < 379;
        assert this.client != null;
        this.regSelectWidget.initialize(this.width, this.height, this.client, this.narrow, this.handler);
        this.addDrawableChild(new TexturedButtonWidget(this.x + 5, this.height / 2 - 49, 20, 18, RegSelectWidget.BUTTON_TEXTURES, button -> {
            this.regSelectWidget.toggleOpen();
            this.x = this.regSelectWidget.findLeftEdge(this.width, this.backgroundWidth);
            button.setPosition(this.x + 5, this.height / 2 - 49);
        }));
        this.addSelectableChild(this.regSelectWidget);
        this.setInitialFocus(this.regSelectWidget);
        this.titleX = 29;
    }

    /**
     * Draws the background of the screen.
     * @param context The context to draw in.
     * @param delta Not specified in the documentation.
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
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    /**
     * Draws the foreground of the screen.
     * Gets called every frame.
     * @param context The context to draw in.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param delta Not specified in the documentation.
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(this.regSelectWidget.isOpen() && this.narrow) {          //ToDo hier ist viel duplizierter Code
            this.renderBackground(context, mouseX, mouseY, delta);
        } else {
            super.render(context, mouseX, mouseY, delta);
        }
        this.regSelectWidget.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
        context.drawText(textRenderer, registerValue, this.x + 64, this.y + 39, 0x555555, false);
    }

    /**
     * Updates the screen every tick.
     * The register value and the list of available registers are updated.
     */
    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        this.regSelectWidget.update();
        String regValue = handler.getRegisterValue();
        registerValue = Text.literal(regValue.substring(Math.max(regValue.length() - 8, 0)));
    }

}
