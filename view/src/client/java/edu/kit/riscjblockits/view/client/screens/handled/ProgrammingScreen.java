package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;


/**
 * This class represents the programming screen.
 * It will be opened when a player uses a programming block.
 * It will be closed when the player closes the screen.
 * It provides a text field to enter the code.
 */
public class ProgrammingScreen extends HandledScreen<ProgrammingScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/programming/programming_block_gui.png");
    private static final Identifier ASSEMBLE_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/programming/write_button_unpressed.png");


    /**
     * The edit box widget that is used to enter the code.
     */
    private EditBoxWidget editBox;

    /**
     * The button that is used to assemble the code.
     */
    private ButtonWidget assembleButton;

    private boolean codeHasChanged = false;


    /**
     * Creates a new ProgrammingScreen.
     * @param handler the handler of the screen
     * @param inventory the player inventory
     * @param title the title of the screen
     */
    public ProgrammingScreen(ProgrammingScreenHandler handler, PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 222;
        this.backgroundWidth = 176;
        this.playerInventoryTitleY = this.backgroundHeight - 94;

    }


    /**
     * Initializes the screen.
     * Adds the edit box widget to the screen.
     * Adds the button to the screen.
     */
    @Override
    protected void init() {
        super.init();
        // add the edit box widget to the screen
        editBox = new EditBoxWidget(textRenderer, this.x + 7, this.y + 17, 137, 93, Text.translatable("programming_pretext"), Text.of("Code"));
        addDrawableChild(editBox);
        editBox.setFocused(false);
        editBox.setText(handler.getCode());

        // add the button to the screen
        assembleButton = TextIconButtonWidget.builder(Text.of(""), (buttonWidget) -> {
            syncCode(editBox.getText());
            client.interactionManager.clickButton(handler.syncId, ProgrammingScreenHandler.ASSEMBLE_BUTTON_ID);
        }, true).texture(ASSEMBLE_BUTTON_TEXTURE, 15, 25).dimension(15, 25).build();
        assembleButton.setPosition(this.x + 151, this.y + 63);
        addDrawableChild(assembleButton);
        handler.enableSyncing();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // render the tooltip of the button if the mouse is over it
        drawMouseoverTooltip(context, mouseX, mouseY);
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
     * @return minecrafts default handling of mouse clicks
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // set the edit box to focused if the mouse is over it while clicking
        editBox.setFocused(editBox.isMouseOver(mouseX, mouseY));
        // sync the code when the edit box is unfocused
        if (!editBox.isFocused() && codeHasChanged) {
            syncCode(editBox.getText());
        }
        // return the default handling of mouse clicks
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void syncCode(String text) {
        PacketByteBuf packet = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.putString("code", text);
        packet.writeNbt(nbt);
        packet.writeBlockPos(handler.getBlockEntity().getPos());
        ClientPlayNetworking.send(NetworkingConstants.SYNC_PROGRAMMING_CODE, packet);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // close the screen if the escape key is pressed
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            syncCode(editBox.getText());
            this.client.player.closeHandledScreen();
        }
        // return true if the edit box is focused or the edit box is focused --> suppress all other key presses (e.g. "e")
        if (this.editBox.keyPressed(keyCode, scanCode, modifiers) || this.editBox.isFocused()) {
            codeHasChanged = true;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
