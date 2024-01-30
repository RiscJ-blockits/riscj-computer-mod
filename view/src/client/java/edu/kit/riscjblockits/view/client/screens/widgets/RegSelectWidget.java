package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class RegSelectWidget implements Drawable, Element, Selectable {
    public static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID,"textures/gui/register/reg_select_widget.png");
    //public static final ButtonTextures BUTTON_TEXTURES = new ButtonTextures(new Identifier(RISCJ_blockits.MODID,
        //"textures/gui/register/button.png"), new Identifier(RISCJ_blockits.MODID,"textures/gui/register/button_highlighted.png")); //path does not work fsr!!! :( TODO fix path

    public static final ButtonTextures BUTTON_TEXTURES = new ButtonTextures(new Identifier("recipe_book/button"), new Identifier("recipe_book/button_highlighted"));
    private static final String TO_DO_TEXT = "Select Register";
    private int parentWidth;
    private int parentHeight;
    private List<String> configuredRegisters;
    private List<String> missingRegisters;
    private ToggleButtonWidget toggleNeededButton; // future implementation
    private RegisterScreenHandler registerScreenHandler;
    private MinecraftClient client;
    private boolean open;
    private boolean narrow;
    private int leftOffset;
    private int cachedInvChangeCount;
    private RegisterListWidget registerList;
    private int x;
    private int y;
    private int width;
    private int height;
    private final List<Element> children = new ArrayList<>();

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
        this.leftOffset = this.narrow ? 0 : 86;

        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight - 166) / 2;

        this.x = i;
        this.y = j;

        this.registerList = new RegisterListWidget(getEntries(), i+ 8, j + 18, 113, 140);
        children.add(registerList);
        this.open = false;
    }

    private List<RegisterEntry> getEntries() {
        BlockPos pos = registerScreenHandler.getBlockEntity().getPos();
        List<RegisterEntry> entries = new ArrayList<>();
        for (String register: registerScreenHandler.getRegisters(DataConstants.REGISTER_MISSING)) {
            RegisterEntry entry = new RegisterEntry(register, true, false, pos);
            entries.add(entry);
        }
        for (String register: registerScreenHandler.getRegisters(DataConstants.REGISTER_FOUND)) {
            RegisterEntry entry;
            if(register.equals(registerScreenHandler.getRegisterValue())){
                entry = new RegisterEntry(register, false, true, pos);
            } else {
                entry = new RegisterEntry(register, false, false, pos);
            }
            entries.add(entry);
        }
        return entries;
    }


    public void toggleOpen() {
        this.setOpen(!this.open);
    }
    
    private void setOpen(boolean opened) {
        if (opened) {
            this.reset();
        }
        this.open = opened;
    }

    private void reset() {
        this.leftOffset = this.narrow ? 0 : 86;
        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight - 166) / 2 ;
        registerList.setPosition(i +8, j+ 18);
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

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        context.drawText(textRenderer, Text.literal(TO_DO_TEXT), i + 8, j + 8, 0x555555, false);

        //this.registerList.setPosition(i + 8, j + 18);
        this.registerList.render(context, mouseX, mouseY, delta);

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
        return this.open ? SelectionType.HOVERED : SelectionType.NONE;
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
        registerList.updateEntries(getEntries());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!this.isOpen()) {
            return false;
        }
        boolean success = registerList.mouseClicked(mouseX, mouseY, button);
        registerList.updateEntries(getEntries());
        return success;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return registerList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return true;//mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
