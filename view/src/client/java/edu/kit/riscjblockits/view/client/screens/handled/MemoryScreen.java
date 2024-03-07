package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.client.screens.widgets.MemoryListWidget;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.memory.MemoryScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * This class represents the screen of the memory block in the game.
 * It can display the contents of the memory in the computer.
 */
public class MemoryScreen extends HandledScreen<MemoryScreenHandler> {

    /**
     * The background texture of the screen.
     */
    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/memory_block/memory_block_gui.png");
    public static final int SCREEN_LINES = 9;

    /**
     * The list widget that displays the memory contents.
     */
    private final MemoryListWidget memoryListWidget;
    private TextFieldWidget inputBox;

    /**
     * Creates a new MemoryScreen with the given settings.
     * It can display the contents of the memory in the computer.
     * @param handler The handler of the screen.
     * @param inventory The inventory of the player that opened the screen.
     * @param title The title of the screen.
     */
    public MemoryScreen(MemoryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 222;
        this.backgroundWidth = 176;
        playerInventoryTitleY += 56;
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - backgroundHeight) / 2;
        memoryListWidget = new MemoryListWidget(handler, i + 7, j + 26, 150, 99);
    }

    /**
     * Initializes the screen.
     * Adds the list widget to the screen.
     */
    @Override
    protected void init() {
        super.init();
        ClientPlayNetworking.send(NetworkingConstants.REQUEST_DATA, PacketByteBufs.create().writeBlockPos(handler.getBlockEntity().getPos()));
        memoryListWidget.updatePos(this.x + 7, this.y + 26);
        // add the edit box widget to the screen
        inputBox = new TextFieldWidget(textRenderer, this.x + 108, this.y + 126, 61, 11, Text.literal(""));
        inputBox.setMaxLength(10);
        addDrawableChild(inputBox);
        inputBox.setFocused(false);
    }

    /**
     * Draws the background of the screen.
     * @param context The context to draw in.
     * @param delta Not specified in the minecraft documentation.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     */
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    /**
     * Draws the foreground of the screen.
     * @param context The context to draw in.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param delta The time since the last frame.
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        memoryListWidget.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    /**
     * Handles a mouse scroll event.
     * We use it to scroll the memory list widget.
     * @param mouseX the X coordinate of the mouse
     * @param mouseY the Y coordinate of the mouse
     * @param horizontalAmount the horizontal scroll amount
     * @param verticalAmount the vertical scroll amount
     * @return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
     */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        memoryListWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // close the screen if the escape key is pressed
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            assert this.client != null;
            assert this.client.player != null;
            this.client.player.closeHandledScreen();
        } else if (keyCode == GLFW.GLFW_KEY_ENTER) { //Send Data on Enter
            String address = inputBox.getText();
            try {
                long addressInt = Long.parseLong(address, 16);
                if (addressInt >= 0 && addressInt < handler.getMemorySize()-SCREEN_LINES) {
                    memoryListWidget.jumpToLine(addressInt);
                }
                else if(addressInt >= handler.getMemorySize()- SCREEN_LINES && addressInt <= handler.getMemorySize()){
                    memoryListWidget.jumpToLine(handler.getMemorySize()-SCREEN_LINES);
                }
            } catch (NumberFormatException e) {
                // do nothing
            }
            inputBox.setText("");
        }
        // return true if the edit box is focused or the edit box is focused --> suppress all other key presses (e.g. "e")
        if (this.inputBox.keyPressed(keyCode, scanCode, modifiers) || this.inputBox.isFocused()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}
