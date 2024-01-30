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

public class RegisterScreen extends HandledScreen<RegisterScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/register/register_block_gui.png");
    private final RegSelectWidget regSelectWidget = new RegSelectWidget();
    private boolean narrow;

    private Text registerValue = Text.literal("0");

    public RegisterScreen(RegisterScreenHandler handler, PlayerInventory inventory,
                          Text title) {
        super(handler, inventory, title);

    }

    @Override
    protected void init() {
        super.init();
        ClientPlayNetworking.send(NetworkingConstants.REQUEST_DATA, PacketByteBufs.create().writeBlockPos(handler.getBlockEntity().getPos()));
        this.narrow = this.width < 379;
                this.regSelectWidget.initalize(this.width, this.height, this.client, this.narrow, this.handler);
        this.addDrawableChild(new TexturedButtonWidget(this.x + 5, this.height / 2 - 49, 20, 18, RegSelectWidget.BUTTON_TEXTURES, button -> {
            this.regSelectWidget.toggleOpen();
            this.x = this.regSelectWidget.findLeftEdge(this.width, this.backgroundWidth);
            button.setPosition(this.x + 5, this.height / 2 - 49);
        }));
        this.addSelectableChild(this.regSelectWidget);
        this.setInitialFocus(this.regSelectWidget);
        this.titleX = 29;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = this.x;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(this.regSelectWidget.isOpen() && this.narrow) {
            this.renderBackground(context, mouseX, mouseY, delta);
        } else {
            super.render(context, mouseX, mouseY, delta);
        }
        this.regSelectWidget.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
        context.drawText(textRenderer, registerValue, this.x + 64, this.y + 39, 0x555555, false);

    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        this.regSelectWidget.update();
        String regValue = handler.getRegisterValue();
        registerValue = Text.literal(regValue.substring(Math.max(regValue.length() - 8, 0)));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
