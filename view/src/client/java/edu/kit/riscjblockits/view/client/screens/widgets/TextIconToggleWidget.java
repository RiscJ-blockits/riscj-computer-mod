package edu.kit.riscjblockits.view.client.screens.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TextIconToggleWidget extends ButtonWidget {
    public static Identifier TEXTURE_SELECTED = new Identifier(RISCJ_blockits.MODID, "textures/gui/register/reg_select_button.png");
    public static Identifier TEXTURE_UNSELECTED = new Identifier(RISCJ_blockits.MODID, "textures/gui/register/reg_unselect_button.png");
    public static final int DEFAULT_WIDTH = 113;
    public static final int DEFAULT_HEIGHT = 20;
    private boolean selected = false;

    protected TextIconToggleWidget(Text message,
                                   PressAction onPress) {
        super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, message, onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        //super.renderButton(context, mouseX, mouseY, delta);
        int i = this.getX();
        int j = this.getY();
        //RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();

        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 100.0f);

        if(this.selected) {
            context.drawTexture(TEXTURE_SELECTED, i, j, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        } else {
            context.drawTexture(TEXTURE_UNSELECTED, i, j, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        }

        MinecraftClient client = MinecraftClient.getInstance();
        context.drawText(client.textRenderer, this.getMessage(), getX()+5, getY()+7, 0x555555, false);

        context.getMatrices().pop();
        RenderSystem.enableDepthTest();
    }

    @Override
    public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        //int i = this.getX() + 2;
        //int j = this.getX() + this.getWidth();
        //int k = this.getX() + this.getWidth() / 2;
        context.drawText(textRenderer, this.getMessage(), getX()+2, getY()+2, color, false);
        //TextIconButtonWidget.WithText.drawScrollableText(context, textRenderer, this.getMessage(), k, i, this.getY(), j, this.getY() + this.getHeight(), color);
        //rethinking this
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        //this.renderButton(context, mouseX, mouseY, delta);
    }

    public void toggleSelected(boolean selected) {
        this.selected = selected;
    }
}
