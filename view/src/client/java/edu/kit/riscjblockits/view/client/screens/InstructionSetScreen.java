package edu.kit.riscjblockits.view.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.view.client.screens.widgets.IconButtonWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.text.TextEditWidget;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.UnsupportedEncodingException;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;
import static edu.kit.riscjblockits.model.data.DataConstants.INSTRUCTION_SET;
import static edu.kit.riscjblockits.model.data.DataConstants.INSTRUCTION_SET_TEMP;
import static edu.kit.riscjblockits.model.data.DataConstants.TEMP_IST_NBT_TAG;
import static edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingBlockEntity.CHUNK_SIZE;

/**
 * This class represents the Instruction Set Item Screen. With it, the player can modify the Instruction Set on the Item.
 */
public class InstructionSetScreen extends Screen {

    private static final Identifier WRITE_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/instructionset/save_button.png");

    private static final Identifier RESTORE_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/instructionset/reset_button.png");
    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/instructionset/instructionset_gui.png");

    /**
     * The edit box widget that is used to enter the code.
     */
    private TextEditWidget inputBox;
    private final int backgroundHeight;
    private final int backgroundWidth;

    /**
     * The button that is used to write new code to the item.
     */
    private IconButtonWidget writeButton;

    /**
     * The button that is used to restore the code to the last known working version.
     */
    private IconButtonWidget restoreButton;
    private MultilineText errorText;
    private int tickCounter;
    private boolean edited = false;
    private int x;
    private int y;

    /**
     * The hand that holds the item. Cane be the Main Hand or the Offhand.
     */
    private final Hand currentHand;

    /**
     * Creates a new Instruction Set Item Screen. With it, the player can modify the Instruction Set on the Item.
     * @param title the title of the screen
     * @param hand the hand that holds the item
     */
    public InstructionSetScreen(Text title, String hand) {
        super(title);
        this.backgroundHeight = 180;
        this.backgroundWidth = 269;
        if (hand.equals("OFF_HAND")) {
            currentHand = Hand.OFF_HAND;
        } else {
            currentHand = Hand.MAIN_HAND;
        }
    }

    /**
     * Is called on open. Initializes the screen with data and adds the widgets and buttons.
     */
    @Override
    protected void init() {
        super.init();
        this.x = (width - backgroundWidth) / 2;
        this.y = (height - backgroundHeight) / 2;
        //get istString from NBT
        assert this.client != null;
        assert client.player != null;
        NbtCompound nbt = client.player.getStackInHand(currentHand).getOrCreateNbt();
        String istString = "";
        for (String s : nbt.getKeys()) {        //if there is a temp_IstString, use it
            if (s.equals(CONTROL_IST_ITEM)) {
                istString = nbt.getString(CONTROL_IST_ITEM);
            } else if (s.equals(TEMP_IST_NBT_TAG)) {
                if(nbt.getString(s).isEmpty()) continue;
                istString = nbt.getString(s);
                edited = true;
                break;
            }
        }
        // add the edit box widget to the screen
        inputBox = new TextEditWidget(textRenderer, x + 33, y + 9,  212, 136);
        addDrawableChild(inputBox);
        inputBox.setText(istString);
        // add the build button to the screen
        writeButton = new IconButtonWidget(
            x + 251, y + 76,
            12, 12,
            button -> buildIst(inputBox.getText()),
            WRITE_BUTTON_TEXTURE
        );
        addDrawableChild(writeButton);
        // add the restore button to the screen
        restoreButton = new IconButtonWidget(
            x + 251, y + 92,
            12, 12,
            button -> restore(),
            RESTORE_BUTTON_TEXTURE
        );
        addDrawableChild(restoreButton);
    }

    /**
     * Renders the screen. Gets called every frame as long as the screen is open.
     * Updates error text and the "edited" text.
     * @param context the drawing context
     * @param mouseX the x position of the mouse
     * @param mouseY the y position of the mouse
     * @param delta the time delta since the last frame
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (tickCounter > 0) {          //an error has happened
            context.drawText(textRenderer, Text.translatable("ist_error"), this.x + 45, this.y + 157, 0xCC0000, false);
            tickCounter--;
        }
        if (edited) {
            context.drawText(textRenderer, Text.literal("edited"), this.x + 10, this.y + 157, 0xffffff, false);
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        context.drawTexture(TEXTURE, (width - backgroundWidth) / 2, (height - backgroundHeight) / 2, 0, 0,
            backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // set the edit box to focus if the mouse is over it while clicking
        inputBox.setFocused(inputBox.isMouseOver(mouseX, mouseY));
        setFocused(inputBox.isMouseOver(mouseX, mouseY)? inputBox : null);

        // return the default handling of mouse clicks
        return super.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Is called when a key is pressed.
     * Closes the screen if the escape key is pressed.
     * Also send edited data to the server when the escape key is pressed.
     * @param keyCode the named key code of the event as described in the {@link org.lwjgl.glfw.GLFW GLFW} class
     * @param scanCode the unique/platform-specific scan code of the keyboard input
     * @param modifiers a GLFW bitfield describing the modifier keys that are held down (see <a href="https://www.glfw.org/docs/3.3/group__mods.html">GLFW Modifier key flags</a>)
     * @return
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // close the screen if the escape key is pressed
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            assert this.client != null;
            assert this.client.player != null;
            NbtCompound nbt = client.player.getStackInHand(currentHand).getOrCreateNbt();
            nbt.putString(TEMP_IST_NBT_TAG, inputBox.getText());
            client.player.getStackInHand(currentHand).setNbt(nbt);
            client.player.getInventory().markDirty();
            syncTempText(inputBox.getText()); //sync data to server
            this.client.player.closeHandledScreen();
        }
        // return true if the edit box is focused or the edit box is focused --> suppress all other key presses (e.g. "e")
        if (this.inputBox.keyPressed(keyCode, scanCode, modifiers) || this.inputBox.isFocused()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * Tries to build the Instruction Set from the input string and writes it to the nbt data of the item.
     * Sends the new data to the server.
     * Can display an error message if the input string is not valid.
     * @param ist the input string.
     */
    private void buildIst(String ist) {
        InstructionSetModel instructionSetModel;
        try {
            instructionSetModel = InstructionSetBuilder.buildInstructionSetModel(ist);
        }catch (UnsupportedEncodingException | InstructionBuildException e) {
            tickCounter = 100;
            errorText = MultilineText.create(textRenderer,Text.translatable("ist_error"), 200);
            return;
        }
        assert this.client != null;
        assert client.player != null;
        NbtCompound nbt = client.player.getStackInHand(currentHand).getOrCreateNbt();
        nbt.putString(CONTROL_IST_ITEM, ist);
        nbt.remove(TEMP_IST_NBT_TAG);
        client.player.getStackInHand(currentHand).setNbt(nbt);
        client.player.getStackInHand(currentHand).setCustomName(Text.of(instructionSetModel.getName() + " " + I18n.translate("istItem.title")));
        client.player.getInventory().markDirty();
        edited = false;
        syncIstText(ist);
        syncTempText("");
        this.client.player.closeHandledScreen();
    }

    /**
     * Restores the input box to the last known working version.
     */
    private void restore() {
        String istString = "";
        assert this.client != null;
        assert client.player != null;
        NbtCompound nbt = client.player.getStackInHand(currentHand).getOrCreateNbt();
        for (String s : nbt.getKeys()) {        //if there is a temp_IstString, use it
            if (s.equals(CONTROL_IST_ITEM)) {
                istString = nbt.getString(CONTROL_IST_ITEM);
                edited = false;
            }
        }
        inputBox.setText(istString);
    }

    private void syncIstText(String text) {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_IST_TEXT_CONFIRMATION_C2S,
            (client1, handler1, buf, responseSender) -> {
                int requestedChunk = buf.readInt();
                if (requestedChunk * CHUNK_SIZE > text.length()) {
                    ClientPlayNetworking.unregisterGlobalReceiver(NetworkingConstants.SYNC_IST_TEXT_CONFIRMATION_C2S);
                    return;
                }
                sendChunk(requestedChunk, text, INSTRUCTION_SET, NetworkingConstants.SYNC_IST_TEXT_C2S);
            });
        sendChunk(0, text, INSTRUCTION_SET, NetworkingConstants.SYNC_IST_TEXT_C2S);
    }

    private void syncTempText(String text) {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_TEMP_TEXT_CONFIRMATION_C2S,
            (client1, handler1, buf, responseSender) -> {
                int requestedChunk = buf.readInt();
                if (requestedChunk * CHUNK_SIZE > text.length()) {
                    ClientPlayNetworking.unregisterGlobalReceiver(NetworkingConstants.SYNC_TEMP_TEXT_CONFIRMATION_C2S);
                    return;
                }
                sendChunk(requestedChunk, text, INSTRUCTION_SET_TEMP, NetworkingConstants.SYNC_TEMP_TEXT_C2S);
            });
        sendChunk(0, text, INSTRUCTION_SET_TEMP, NetworkingConstants.SYNC_TEMP_TEXT_C2S);
    }

    private void sendChunk(int chunkIndex, String text, String tag, Identifier network) {
        NbtCompound nbt = new NbtCompound();
        int start = chunkIndex * CHUNK_SIZE;
        int end = Math.min((chunkIndex + 1) * CHUNK_SIZE, text.length());
        // can't send as there is no more text
        if (start > end || start > text.length()) {
            return;
        }
        nbt.putInt("chunkIndex", chunkIndex);
        nbt.putString("chunkData", text.substring(start, end));
        //
        NbtCompound container = new NbtCompound();
        container.put(tag, nbt);
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeNbt(container);
        ClientPlayNetworking.send(network, packet);
    }

}
