package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.TerminalScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.TerminalBlockEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * The screen that is displayed when the player opens the terminal block.
 * It contains an edit box widget to enter char that are saved inside the block.
 * Only the last char is saved.
 * It can display the last chars inside the model.
 */
public class TerminalScreen extends HandledScreen<TerminalScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/programming/programming_block_gui.png");

    /**
     * The edit box widget that is used to enter chars.
     */
    private EditBoxWidget inputBox;

    /**
     * The string that should be displayed on the screen.
     */
    private String output = "";

    /**
     * A Minecraft class that is used to display the output string.
     */
    private MultilineText outputtedText;

    /**
     * The constructor of the screen.
     * @param handler the handler of the screen
     * @param inventory the inventory of the player
     * @param title the title of the screen
     */
    public TerminalScreen(TerminalScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 222;
        this.backgroundWidth = 176;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    /**
     * Initializes the screen.
     * Adds the edit box widget to the screen.
     * Adds the Text to the screen.
     */
    @Override
    protected void init() {
        super.init();
        // add the edit box widget to the screen
        inputBox = new EditBoxWidget(textRenderer, this.x + 7, this.y + 17, 137, 93, Text.literal(""), Text.of(""));
        inputBox.setMaxLength(1);
        addDrawableChild(inputBox);
        inputBox.setFocused(false);
        //add text to the screen
        output = ((TerminalBlockEntity) handler.getBlockEntity()).getDisplayedString();
        outputtedText = MultilineText.create(textRenderer, Text.literal(output), width - 20);
        handler.enableSyncing();
    }

    /**
     * Renders the screen. Gets called every frame as long as the screen is open.
     * We use this method to update the output string.
     * @param context the drawing context
     * @param mouseX the x position of the mouse
     * @param mouseY the y position of the mouse
     * @param delta the time delta since the last frame
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        // render the tooltip of the button if the mouse is over it
        drawMouseoverTooltip(context, mouseX, mouseY);
        outputtedText.drawWithShadow(context, 10, height / 2, 16, 0xffffff);
    }

    /**
     * Renders the background of the screen.
     * @param context the drawing context
     * @param mouseX the x position of the mouse
     * @param mouseY the y position of the mouse
     * @param delta the time delta since the last frame
     */
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    /**
     * Will check whether the edit-box is focused.
     * @param mouseX the X coordinate of the mouse
     * @param mouseY the Y coordinate of the mouse
     * @param button the mouse button number
     * @return Minecraft default handling of mouse clicks
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // set the edit box to focus if the mouse is over it while clicking
        inputBox.setFocused(inputBox.isMouseOver(mouseX, mouseY));
        // return the default handling of mouse clicks
        return super.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Is called when a key is pressed. We use it to send the inputted char on enter.
     * @param keyCode the named key code of the event as described in the {@link org.lwjgl.glfw.GLFW GLFW} class
     * @param scanCode the unique/platform-specific scan code of the keyboard input
     * @param modifiers a GLFW bitfield describing the modifier keys that are held down (see <a href="https://www.glfw.org/docs/3.3/group__mods.html">GLFW Modifier key flags</a>)
     * @return Minecraft default handling of key presses
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // close the screen if the escape key is pressed
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            assert this.client != null;
            assert this.client.player != null;
            this.client.player.closeHandledScreen();
        }
        //Send Data on Enter
        else if (keyCode == GLFW.GLFW_KEY_ENTER) {
            sendData(inputBox.getText());
            //ToDo reset Text inside the box
        }
        // return true if the edit box is focused or the edit box is focused --> suppress all other key presses (e.g. "e")
        if (this.inputBox.keyPressed(keyCode, scanCode, modifiers) || this.inputBox.isFocused()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * Is called every tick. We use it to update the output string from the block entity.
     */
    @Override
    public void handledScreenTick() {
        output = ((TerminalBlockEntity) handler.getBlockEntity()).getDisplayedString();
        outputtedText = MultilineText.create(textRenderer, Text.literal(output) , width - 20);
    }

    /**
     * Sends the inputted char converted to a hex value to the server.
     * @param text the inputted char.
     */
    private void sendData(String text) {
        if (text.length() != 1) {
            return;
        }
        char input = text.charAt(0);
        String hexInput = Integer.toHexString((int) input);
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeBlockPos(handler.getBlockEntity().getPos());
        packet.writeString(hexInput);
        ClientPlayNetworking.send(NetworkingConstants.SYNC_TERMINAL_INPUT, packet);
    }

}
