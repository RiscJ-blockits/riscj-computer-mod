package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Is a button that can display a text and on the left side three different icon depending on the state.
 */
public class TextIconToggleWidget extends ButtonWidget {
    /**
     * The texture of the selected state.
     */
    public static final Identifier TEXTURE_SELECTED =
        new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/register/reg_select_button.png");
    /**
     * The texture for the unselected state.
     */
    public static final Identifier TEXTURE_UNSELECTED =
        new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/register/reg_unselect_button.png");
    /**
     * The texture of the disabled state.
     */
    public static final Identifier TEXTURE_CURRENT =
        new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/register/reg_select_button_disabled.png");
    /**
     * The default width of the button.
     */
    public static final int DEFAULT_WIDTH = 113;
    /**
     * The default height of the button.
     */
    public static final int DEFAULT_HEIGHT = 20;
    private boolean selected = false;
    private boolean currentReg;

    /**
     * Constructor for the text icon toggle widget.
     * @param message The message to display on the button.
     * @param onPress The action to perform when the button is pressed.
     * @param selected The selected state of the button.
     * @param currentReg The disabled state of the button. If this is true, the button is not disabled.
     */
    protected TextIconToggleWidget(Text message,
                                   PressAction onPress, boolean selected, boolean currentReg) {
        super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, message, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.selected = selected;
        this.currentReg = currentReg;
    }

    /**
     * Draws the button.
     * @param context The context to draw in.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param delta the time since the last frame.
     */
    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = this.getX();
        int j = this.getY();
        if (this.selected && this.currentReg) {
            context.drawTexture(TEXTURE_CURRENT, i, j, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(),
                this.getHeight());
        } else if (this.selected) {
            context.drawTexture(TEXTURE_SELECTED, i, j, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(),
                this.getHeight());
        } else {
            context.drawTexture(TEXTURE_UNSELECTED, i, j, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(),
                this.getHeight());
        }
        MinecraftClient client = MinecraftClient.getInstance();
        context.drawText(client.textRenderer, this.getMessage(), getX() + 5, getY() + 7, 0x555555, false);
    }

    /**
     * Draws the message on the button.
     * @param context The context to draw in.
     * @param textRenderer The text renderer to use.
     * @param color The color of the text.
     */
    @Override
    public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        context.drawText(textRenderer, this.getMessage(), getX() + 2, getY() + 2, color, false);
    }

    /**
     * Updates the state of the button.
     * Gets called every tick.
     * @param selected The selected state of the button.
     * @param currentReg The disabled state of the button. If this is true, the button is not disabled.
     */
    public void update(boolean selected, boolean currentReg) {
        this.selected = selected;
        this.currentReg = currentReg;
    }
}
