package edu.kit.riscjblockits.view.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.view.client.screens.widgets.IconButtonWidget;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EditBoxWidget;
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

/**
 * This class represents the Instruction Set Item Screen. With it, the player can modify the Instruction Set on the Item.
 */
public class InstructionSetScreen extends Screen {

    private static final Identifier WRITE_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/instructionset/save_button.png");

    private static final Identifier RESTORE_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/instructionset/reset_button.png");
    /**
     * The tag that is used to store user input temporary data in the NBT.
     */
    private static final String TEMP_IST_NBT_TAG = "temp_IstString";
    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/instructionset/instructionset_gui.png");

    /**
     * The edit box widget that is used to enter the code.
     */
    private EditBoxWidget inputBox;
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
    private Hand currentHand;

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
                istString = nbt.getString(s);
                edited = true;
                break;
            }
        }
        // add the edit box widget to the screen
        inputBox = new EditBoxWidget(textRenderer, x + 9, y +8,  229, 143,Text.literal(""), Text.of(""));
        addDrawableChild(inputBox);
        inputBox.setText(istString);
        //ToDo don't jump to the end of the input box
        inputBox.setFocused(false);
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
            PacketByteBuf buf = PacketByteBufs.create().writeNbt(nbt);
            buf.writeString(currentHand.toString());
            ClientPlayNetworking.send(NetworkingConstants.SYNC_IST_INPUT, buf);     //sync data to server
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
            //errorText = MultilineText.create(textRenderer,Text.of(e.getMessage()) , 182 * 2);
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
        PacketByteBuf buf = PacketByteBufs.create().writeNbt(nbt);
        buf.writeString(currentHand.toString());
        ClientPlayNetworking.send(NetworkingConstants.SYNC_IST_INPUT, buf);     //sync data to server
        edited = false;
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

}
