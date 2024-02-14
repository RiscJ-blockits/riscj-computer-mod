package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * A RegSelectWidget is a widget that displays a list of register types.
 * It is used inside the RegisterScreen to allow the user to select a register type
 */
public class RegSelectWidget extends ExtendableWidget {

    /**
     * The texture of the widget.
     */
    public static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MOD_ID,"textures/gui/register/reg_select_widget.png");
    /**
     * The texture of the button.
     */
    public static final ButtonTextures BUTTON_TEXTURES = new ButtonTextures(new Identifier("recipe_book/button"), new Identifier("recipe_book/button_highlighted"));
    private static final String TO_DO_TEXT = "Select Register";
    private RegisterScreenHandler registerScreenHandler;
    private MinecraftClient client;
    private int cachedInvChangeCount;
    private RegisterListWidget registerList;
    private final List<Element> children = new ArrayList<>();

    /**
     * Creates a new RegSelectWidget.
     */
    public RegSelectWidget() {
        //do nothing
    }

    /**
     * Adds all the components to the widget.
     * @param parentWidth The width of the parent.
     * @param parentHeight The height of the parent.
     * @param client The client.
     * @param narrow Only used by minecraft.
     * @param registerScreenHandler The screen handler for all the widgets.
     */
    public void initialize(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow, RegisterScreenHandler registerScreenHandler) {
        super.initialize(parentWidth, parentHeight, narrow, TEXTURE);

        this.client = client;
        this.registerScreenHandler = registerScreenHandler;
        assert client.player != null;
        client.player.currentScreenHandler = registerScreenHandler;
        this.cachedInvChangeCount = client.player.getInventory().getChangeCount();

        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight - 166) / 2;

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
            if(register.equals(registerScreenHandler.getCurrentRegister())){
                entry = new RegisterEntry(register, false, true, pos);
            } else {
                entry = new RegisterEntry(register, false, false, pos);
            }
            entries.add(entry);
        }
        return entries;
    }
    @Override
    protected void reset() {
        this.leftOffset = this.narrow ? 0 : 86;
        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight - 166) / 2;
        registerList.setPosition(i +8, j+ 18);
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

        this.registerList.render(context, mouseX, mouseY, delta);

        context.getMatrices().pop();
    }

    /**
     * Updates the widget.
     * Is called every tick.
     */
    public void update(){
        if (!this.open) {
            return;
        }
        assert this.client.player != null;
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
        return registerList.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return registerList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
