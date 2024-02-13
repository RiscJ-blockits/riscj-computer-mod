package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.client.screens.widgets.IconButtonWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.InstructionsWidget;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static edu.kit.riscjblockits.model.data.DataConstants.PROGRAMMING_BLOCK_CODE;

/**
 * This class represents the programming screen.
 * It will be opened when a player uses a programming block.
 * It will be closed when the player closes the screen.
 * It provides a text field to enter the code.
 */
public class ProgrammingScreen extends HandledScreen<ProgrammingScreenHandler> {

    /**
     * The background texture of the screen.
     */
    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/programming/programming_block_gui.png");
    private static final Identifier ASSEMBLE_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/programming/write_button_unpressed.png");
    private static final Identifier INSTRUCTIONS_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/programming/instructions_button.png");

    /**
     * Can display information about all available instructions.
     */
    private final InstructionsWidget instructionsWidget = new InstructionsWidget();

    /**
     * The button that is used to assemble the code.
     */
    private IconButtonWidget assembleButton;

    /**
     * The edit box widget that is used to enter the code.
     */
    private EditBoxWidget editBox;
    private boolean codeHasChanged = false;

    /**
     * Specifies whether the instructionsWidget is open or not.
     */
    private boolean narrow;

    /**
     * Creates a new ProgrammingScreen.
     * @param handler the handler of the screen
     * @param inventory the player inventory
     * @param title the title of the screen
     */
    public ProgrammingScreen(ProgrammingScreenHandler handler, PlayerInventory inventory, Text title) {
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
        this.narrow = this.width < 379;
        // add the edit box widget to the screen
        editBox = new EditBoxWidget(textRenderer, this.x + 8, this.y + 18, 129, 91, Text.translatable("programming_pretext"), Text.of("Code"));
        addDrawableChild(editBox);
        editBox.setFocused(false);
        editBox.setText(handler.getCode());
        instructionsWidget.initialize(this.width, this.height - backgroundHeight, this.client, this.narrow, this.handler);
        // add the assembly button to the screen
        assembleButton = new IconButtonWidget(
                this.x + 151, this.y + 63,
                15, 25,
                button -> {
                    syncCode(editBox.getText());
                    assert client != null;
                    assert client.interactionManager != null;
                    client.interactionManager.clickButton(handler.syncId, ProgrammingScreenHandler.ASSEMBLE_BUTTON_ID);
                },
                ASSEMBLE_BUTTON_TEXTURE
        );
        addDrawableChild(assembleButton);
        IconButtonWidget instructionSetButton = new IconButtonWidget(
            this.x + 8, this.y + 111,
            13, 13,
            button -> {
                instructionsWidget.toggleOpen();
                this.x = this.instructionsWidget.findLeftEdge(this.width, this.backgroundWidth);
                button.setPosition(this.x + 8, this.y + 111);
                this.editBox.setPosition(this.x + 8, this.y + 18);
                this.assembleButton.setPosition(this.x + 151, this.y + 63);
            },
            INSTRUCTIONS_BUTTON_TEXTURE
        );
        addDrawableChild(instructionSetButton);
        handler.enableSyncing();
    }

    /**
     * Renders the screen. Is called every frame.
     * @param context the drawing context
     * @param mouseX the x position of the mouse
     * @param mouseY the y position of the mouse
     * @param delta the time delta since the last frame
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(this.instructionsWidget.isOpen() && this.narrow) {
            this.renderBackground(context, mouseX, mouseY, delta);
        } else {
            super.render(context, mouseX, mouseY, delta);
        }
        instructionsWidget.render(context, mouseX, mouseY, delta);
        // render the tooltip of the button if the mouse is over it
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    /**
     * Renders in the background of the screen.
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
        int x = this.x;
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
        nbt.putString(PROGRAMMING_BLOCK_CODE, text);
        packet.writeNbt(nbt);
        packet.writeBlockPos(handler.getBlockEntity().getPos());
        ClientPlayNetworking.send(NetworkingConstants.SYNC_PROGRAMMING_CODE, packet);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // close the screen if the escape key is pressed
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            syncCode(editBox.getText());
            assert this.client != null;
            assert this.client.player != null;
            this.client.player.closeHandledScreen();
        }
        // return true if the edit box is focused or the edit box is focused --> suppress all other key presses (e.g. "e")
        if (this.editBox.keyPressed(keyCode, scanCode, modifiers) || this.editBox.isFocused()) {
            codeHasChanged = true;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * Is called every tick while the screen is open. Used to update the instructionsWidget.
     */
    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        this.instructionsWidget.update();
    }

}
