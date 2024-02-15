package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.blocks.RegisterModel.UNASSIGNED_REGISTER;
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
    /**
     * The screen handler for the register screen.
     */
    protected RegisterScreenHandler registerScreenHandler;
    private MinecraftClient client;
    private int cachedInvChangeCount;
    /**
     * The widget for the list of registers.
     */
    protected RegisterListWidget registerList;
    private int width;
    private int height;
    private final List<Element> children = new ArrayList<>();
    /**
     * The position of the block entity.
     */
    protected BlockPos pos;

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
        pos = registerScreenHandler.getBlockEntity().getPos();
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
            RegisterEntry entry = new RegisterEntry(register, true, false, this);
            entries.add(entry);
        }
        for (String register: registerScreenHandler.getRegisters(DataConstants.REGISTER_FOUND)) {
            RegisterEntry entry;
            if(register.equals(registerScreenHandler.getCurrentRegister())){
                entry = new RegisterEntry(register, false, true, this);
            } else {
                entry = new RegisterEntry(register, false, false, this);
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

        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight - 166) / 2;
        context.drawTexture(TEXTURE, i, j, 1, 1, 147, 166);

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        context.drawText(textRenderer, Text.literal(TO_DO_TEXT), i + 8, j + 8, 0x555555, false);

        for (Element element : this.children) {
            ((Drawable) element).render(context, mouseX, mouseY, delta);
        }
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
        this.children.forEach((element) -> element.mouseClicked(mouseX, mouseY, button));
        return registerList.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Handles the mouse scrolling.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param horizontalAmount The horizontal scroll amount.
     * @param verticalAmount The vertical scroll amount.
     * @return True if the scrolling was successful, false otherwise.
     */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return registerList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    /**
     * Handles the mouse dragging.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @return True if the dragging was successful, false otherwise.
     */
    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return true;//mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    /**
     * Add a child widget.
     * @param child The child widget.
     */
    protected void addChild(Element child) {
        this.children.add(child);
    }
    /**
     * Initial situation: The selected button represents this registers selection.
     * Result: This register is [NOT_ASSIGNED]
     */
    public void deselectRegister() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeString(UNASSIGNED_REGISTER);
        ClientPlayNetworking.send(NetworkingConstants.SYNC_REGISTER_SELECTION, buf);
    }

    /**
     * Initial situation: The selected button represents a missing register Type.
     * Result: Register is configured to be the selected register
     * @param name the name of the assigned register
     */
    public void assignRegister(String name) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeString(name);
        ClientPlayNetworking.send(NetworkingConstants.SYNC_REGISTER_SELECTION, buf);
    }

}
