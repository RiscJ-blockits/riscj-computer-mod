package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

public class IconButtonWidget extends ButtonWidget {

    protected static final NarrationSupplier DEFAULT_NARRATION_SUPPLIER = textSupplier -> (MutableText)textSupplier.get();
    private final Identifier texture;
    public IconButtonWidget(int x, int y, int width, int height, PressAction onPress, Identifier texture) {
        super(x, y, width, height, null, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(texture, getX(), getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
    }
}
