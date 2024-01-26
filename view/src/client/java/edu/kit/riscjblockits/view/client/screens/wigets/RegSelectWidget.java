package edu.kit.riscjblockits.view.client.screens.wigets;

import edu.kit.riscjblockits.view.client.screens.handled.RegisterScreen;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.recipebook.RecipeGroupButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class RegSelectWidget implements Drawable, Element, Selectable {
    public static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID,"textures/gui/register/reg_select_widget.png");
    public static final ButtonTextures BUTTON_TEXTURES = new ButtonTextures(new Identifier(RISCJ_blockits.MODID,
        "textures/gui/register/button.png"), new Identifier(RISCJ_blockits.MODID,"textures/gui/register/button_highlighted.png")); //path does not work fsr!!! :( TODO fix path
    private int parentWidth;
    private int parentHeight;
    private final List<ButtonWidget> regButtons = Lists.newArrayList();
    private ToggleButtonWidget toggleNeededButton; // future implementation
    private RegisterScreenHandler registerScreenHandler;
    private MinecraftClient client;
    private boolean open;
    private boolean narrow;
    private int leftOffset;
    private int cachedInvChangeCount;

    private ScrollableWidget registerList;
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private float scrollPosition;
    private boolean scrolling;
    public RegSelectWidget() {
    }

    public void initalize(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow, RegisterScreenHandler registerScreenHandler) {
        this.client = client;
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        this.narrow = narrow;
        this.registerScreenHandler = registerScreenHandler;
        client.player.currentScreenHandler = registerScreenHandler;
        this.cachedInvChangeCount = client.player.getInventory().getChangeCount();

        int i = (this.parentWidth - 147) / 2 - this.leftOffset + 14;
        int j = (this.parentHeight - 166) / 2 + 14;
        //this.registerList = new ScrollableWidget(i, j, 140, 158, Text.literal("Register List"));

        this.open = false;
        if(this.open) {
            this.reset();
        }
    }

    public void toggleOpen() {
        this.setOpen(!this.open);
    }
    
    private void setOpen(boolean opened) {
        if (opened) {
            this.reset();
        }
        this.open = opened;
        if(!opened) {
            
        }
        this.sendSelectDataPacket();
    }

    private void reset() {
        this.leftOffset = this.narrow ? 0 : 86;
        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight - 166) / 2;
        //loop through registers and add buttons
    }

    private void sendSelectDataPacket() {
        //TODO implement
    }

    public int findLeftEdge(int width, int backgroundWidth) {
        int i = this.open && !this.narrow ? 177 + (width - backgroundWidth - 200) / 2 : (width - backgroundWidth) / 2;
        return i;
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.isOpen()) {
            return;
        }
        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 100.0f);
        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight - 166) / 2;
        context.drawTexture(TEXTURE, i, j, 1, 1, 147, 166);
        //loop through registers and render buttons
        context.getMatrices().pop();
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public SelectionType getType() {
        return this.open ? Selectable.SelectionType.HOVERED : Selectable.SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    public boolean isOpen() {
        return this.open;
    }

    public void update(){
        if (!this.open) {
            return;
        }
        if (this.cachedInvChangeCount != this.client.player.getInventory().getChangeCount()) {
            this.cachedInvChangeCount = this.client.player.getInventory().getChangeCount();
        }
    }

}
