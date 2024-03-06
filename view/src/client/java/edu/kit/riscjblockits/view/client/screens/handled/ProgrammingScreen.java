package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.view.client.screens.widgets.DualTexturedIconButtonWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.IconButtonWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.InstructionsWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.text.AssemblerSyntaxTextEditWidget;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.ai.AiProgrammer;
import edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingScreenHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.PROGRAMMING_BLOCK_CODE;
import static edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingBlockEntity.CHUNK_SIZE;

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
    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/programming/programming_block_gui.png");
    private static final Identifier ASSEMBLE_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/programming/write_button_unpressed.png");
    private static final Identifier ASSEMBLE_BUTTON_TEXTURE_FAILED = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/programming/write_button_failed.png");
    private static final Identifier INSTRUCTIONS_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/programming/instructions_button.png");
    private static final Identifier EXAMPLE_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/programming/example_button.png");
    private static final Identifier AI_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/programming/ai_button.png");


    /**
     * Can display information about all available instructions.
     */
    private final InstructionsWidget instructionsWidget = new InstructionsWidget();

    /**
     * The button that is used to assemble the code.
     */
    private final DualTexturedIconButtonWidget assembleButton;

    /**
     * The button that is used to load example code.
     */
    private IconButtonWidget exampleButton;
    private IconButtonWidget aiButton;

    /**
     * The edit box widget that is used to enter the code.
     */
    private AssemblerSyntaxTextEditWidget editBox;
    private boolean codeHasChanged = false;

    /**
     * Specifies whether the instructionsWidget is open or not.
     */
    private boolean narrow;


    private AiProgrammer aiProgrammer;

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
        assembleButton = new DualTexturedIconButtonWidget(
                0, 0,
                15, 25,
                button -> {
                    syncCode(editBox.getText());
                    assert client != null;
                    assert client.interactionManager != null;
                    client.interactionManager.clickButton(handler.syncId, ProgrammingScreenHandler.ASSEMBLE_BUTTON_ID);
                },
                ASSEMBLE_BUTTON_TEXTURE,
                ASSEMBLE_BUTTON_TEXTURE_FAILED
        );
        ClientPlayNetworking.unregisterGlobalReceiver(NetworkingConstants.SYNC_PROGRAMMING_CODE_S2C);
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_PROGRAMMING_CODE_S2C,
                (client, handler1, buf, responseSender) -> client.execute(() -> {
                    NbtCompound nbt = buf.readNbt();
                    BlockPos blockPos = buf.readBlockPos();
                    assert nbt != null;
                    NbtCompound chunkContainer = nbt.getCompound(PROGRAMMING_BLOCK_CODE);
                    int chunkIndex = chunkContainer.getInt("chunkIndex");
                    String chunkCode = chunkContainer.getString("chunkData");


                    if (chunkIndex == 0) {
                        editBox.setText("");
                    }
                    editBox.setText(editBox.getText() + chunkCode);
                    if (chunkCode.length() < CHUNK_SIZE) {
                        return;
                    }
                    responseSender.sendPacket(NetworkingConstants.SYNC_PROGRAMMING_CODE_CONFIRMATION_S2C, new PacketByteBuf(Unpooled.buffer())
                            .writeInt(chunkIndex + 1).writeBlockPos(blockPos));
                }));
        ClientPlayNetworking.send(NetworkingConstants.SYNC_PROGRAMMING_CODE_CONFIRMATION_S2C, new PacketByteBuf(Unpooled.buffer())
                .writeInt(0).writeBlockPos(handler.getBlockEntity().getPos()));
        editBox = new AssemblerSyntaxTextEditWidget(MinecraftClient.getInstance().textRenderer, this.x + 32, this.y + 19, 112, 89);
        editBox.setText(handler.getCode());

    }

    private void showError(String s) {
        assembleButton.setTooltip((MutableText) Text.of(s));
        assembleButton.setTexture(false);
    }

    private void removeError() {
        assembleButton.setTooltip((MutableText) null);
        assembleButton.setTexture(true);
    }

    /**
     * Initializes the screen.
     * Adds the edit box widget to the screen.
     * Add the button to the screen.
     */
    @Override
    protected void init() {
        super.init();
        this.narrow = this.width < 379;
        // set the position of the edit box
        editBox.setX(this.x + 32);
        editBox.setY(this.y + 19);
        addDrawableChild(editBox);
        editBox.setFocused(false);
        instructionsWidget.initialize(this.width, this.height - backgroundHeight, this.client, this.narrow, this.handler);
        addDrawableChild(instructionsWidget);
        // add the assembly button to the screen
        assembleButton.setX(this.x + 151);
        assembleButton.setY(this.y + 63);
        addDrawableChild(assembleButton);
        //ad Example Button
        exampleButton = new IconButtonWidget(this.x + 25, this.y + 111, 13, 13, button ->{
            editBox.setText(this.handler.getExample());
        }, EXAMPLE_BUTTON_TEXTURE);
        addDrawableChild(exampleButton);

        IconButtonWidget instructionSetButton = new IconButtonWidget(
            this.x + 8, this.y + 111,
            13, 13,
            button -> {
                instructionsWidget.toggleOpen();
                this.x = this.instructionsWidget.findLeftEdge(this.width, this.backgroundWidth);
                button.setPosition(this.x + 8, this.y + 111);
                this.editBox.setPosition(this.x + 8, this.y + 18);
                this.assembleButton.setPosition(this.x + 151, this.y + 63);
                this.editBox.setPosition(this.x + 32, this.y + 19);
                this.exampleButton.setPosition(this.x + 25, this.y + 111);
                this.aiButton.setPosition(this.x + 50, this.y + 111);
            },
            INSTRUCTIONS_BUTTON_TEXTURE
        );
        addDrawableChild(instructionSetButton);
        //add Ai Button
        aiButton = new IconButtonWidget(this.x + 50, this.y + 111, 13, 13, button ->{
            String text = editBox.getText();
            String aiText = aiProgrammer.queryAi(text);
            editBox.setText(text + aiText);
        }, AI_BUTTON_TEXTURE);
        addDrawableChild(aiButton);

        handler.enableSyncing();
        ClientPlayNetworking.unregisterGlobalReceiver(NetworkingConstants.SHOW_ASSEMBLER_EXCEPTION);
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SHOW_ASSEMBLER_EXCEPTION, (client1, handler1, buf, responseSender) -> {
            showError(buf.readString());
        });
        // set selection focus to edit box
        setFocused(editBox);
    }

    private HashMap<String, Integer> getArgumentCountMap() {
        HashMap<String, Integer> argumentCountMap = new HashMap<>();
        List<String[]> instructions;

        // can only fill map if instructionSet is parsable
        try {
            instructions = (handler).getInstructions();
        }catch (InstructionBuildException e) {
            return argumentCountMap;
        }

        for (String[] instruction : instructions) {
            argumentCountMap.put(instruction[0], instruction[1].split(", ?").length);
        }
        return argumentCountMap;
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
        exampleButton.visible = !this.handler.getExample().isEmpty();
        aiButton.visible = aiProgrammer != null;
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
        setFocused(editBox.isMouseOver(mouseX, mouseY)? editBox : null);

        // sync the code when the edit box is unfocused
        if (!editBox.isFocused() && codeHasChanged) {
            syncCode(editBox.getText());
        }
        // return the default handling of mouse clicks
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void syncCode(String text) {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_PROGRAMMING_CODE_CONFIRMATION_C2S,
                (client1, handler1, buf, responseSender) -> {
                    int requestedChunk = buf.readInt();
                    if (requestedChunk * CHUNK_SIZE > text.length()) {
                        ClientPlayNetworking.unregisterGlobalReceiver(NetworkingConstants.SYNC_PROGRAMMING_CODE_CONFIRMATION_C2S);
                        return;
                    }
                    sendChunk(requestedChunk, text);
                });

        sendChunk(0, text);
    }

    private void sendChunk(int chunkIndex, String text) {
        NbtCompound nbt = new NbtCompound();

        int start = chunkIndex * CHUNK_SIZE;
        int end = Math.min((chunkIndex + 1) * CHUNK_SIZE, text.length());
        // cant send as there is no more text
        if (start > end || start > text.length()) {
            return;
        }
        nbt.putInt("chunkIndex", chunkIndex);
        nbt.putString("chunkData", text.substring(start, end));


        NbtCompound container = new NbtCompound();
        container.put(PROGRAMMING_BLOCK_CODE, nbt);
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeNbt(container);
        packet.writeBlockPos(handler.getBlockEntity().getPos());
        ClientPlayNetworking.send(NetworkingConstants.SYNC_PROGRAMMING_CODE_C2S, packet);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        removeError();
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
        this.editBox.setInstructionArgumentCountMap(getArgumentCountMap());
        if (aiProgrammer == null) {
            String key = handler.getOpenAiKey();
            if (key != null && !key.isEmpty()) {
                aiProgrammer = new AiProgrammer(key);
            }
        }
    }

}
