package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.client.screens.widgets.IconButtonWidget;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock.SystemClockBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock.SystemClockScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static edu.kit.riscjblockits.model.blocks.ClockMode.MC_TICK;
import static edu.kit.riscjblockits.model.blocks.ClockMode.REALTIME;
import static edu.kit.riscjblockits.model.blocks.ClockMode.STEP;

/**
 * The screen for the system clock block.
 * In it, you can see the speed of the system clock and change the system clock mode.
 */
public class SystemClockScreen extends HandledScreen<SystemClockScreenHandler> {

    /**
     * The screen has ten buttons for the different modes.
     * The first button is for step mode, the last for real time mode and the rest for the different mc tick modes.
     * The representing ticks one computer tick should take can be configured here.
     */
    private static final int[][] MODE_TRANSLATIONS = {{0,0}, {1,80}, {2,40}, {3,20}, {4,15}, {5,10}, {6,5}, {7,2}, {8,1}, {9,0}};
    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/system_clock/system_clock_gui.png");
    private static final Identifier MODE_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/system_clock/system_clock_button.png");
    private static final String MODE_TEXTURE = "textures/gui/system_clock/system_clock_lever_%d.png";
    private static final int MODE_BUTTON_SIZE = 6;
    private static final int[] MODE_BUTTON_X_OFFSETS = {86, 84, 90, 98, 109, 122, 135, 146, 154, 160};
    private static final int[] MODE_BUTTON_Y_OFFSETS = {62, 49, 37, 28, 20, 18, 20, 28, 37, 49};
    private static final int LEVER_X_OFFSET = 96;
    private static final int LEVER_Y_OFFSET = 23;
    private static final int LEVER_WIDTH = 58;
    private static final int LEVER_HEIGHT = 39;
    private static final int SPEED_TEXT_FIELD_OFFSET_X = 9;
    private static final int SPEED_TEXT_FIELD_OFFSET_Y = 18;
    private final ArrayList<IconButtonWidget> modeButtons = new ArrayList<>(10);

    private Identifier leverTexture = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/system_clock/system_clock_lever_0.png");
    private Text clockSpeed = Text.literal("0");

    /**
     * Constructor for the system clock screen.
     * In it, you can see the speed of the system clock and change the system clock mode.
     * @param handler The handler for the screen.
     * @param inventory The player inventory.
     * @param title The title of the screen.
     */
    public SystemClockScreen(SystemClockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    /**
     * Initializes the screen.
     * It sends a request to the server to update the data of the block entity.
     * It also initializes the buttons for the different modes.
     */
    @Override
    protected void init() {
        super.init();
        ClientPlayNetworking.send(NetworkingConstants.REQUEST_DATA, PacketByteBufs.create().writeBlockPos(handler.getBlockEntity().getPos()));
        int mode = getButtonStep();
        leverTexture = new Identifier(RISCJ_blockits.MOD_ID, String.format(MODE_TEXTURE, mode));
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        for(int i = 0; i < 10; i++) {
            int setMode = i;
            modeButtons.add(new IconButtonWidget(x + MODE_BUTTON_X_OFFSETS[setMode], y + MODE_BUTTON_Y_OFFSETS[setMode], MODE_BUTTON_SIZE, MODE_BUTTON_SIZE, button -> {
                updateModel(setMode);
                leverTexture = new Identifier(RISCJ_blockits.MOD_ID, String.format(MODE_TEXTURE, setMode));
            }, MODE_BUTTON_TEXTURE));
            addDrawableChild(modeButtons.get(i));
        }
    }

    /**
     * Draws the background of the screen.
     * @param context The context to draw in.
     * @param delta Not specified in the documentation.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
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
     * Draws the foreground of the screen.
     * Is called every frame.
     * Draws the clock pointer in the correct position.
     * @param context The context to draw in.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param delta Not specified in the documentation.
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawText(textRenderer, clockSpeed, x + SPEED_TEXT_FIELD_OFFSET_X, y + SPEED_TEXT_FIELD_OFFSET_Y, 0xffffff, false);
        context.drawTexture(leverTexture, x + LEVER_X_OFFSET, y + LEVER_Y_OFFSET, 0, 0, LEVER_WIDTH, LEVER_HEIGHT, LEVER_WIDTH, LEVER_HEIGHT);
        for(int i = 0; i < 10; i++) {
            modeButtons.get(i).setPosition(x + MODE_BUTTON_X_OFFSETS[i], y + MODE_BUTTON_Y_OFFSETS[i]);
        }
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    /**
     * Updates the screen every tick.
     */
    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        clockSpeed = Text.literal(String.valueOf(((SystemClockBlockEntity)handler.getBlockEntity()).getSystemClockSpeed())); //FixMe: why two times?
        clockSpeed = Text.literal(String.valueOf(SystemClockScreenHandler.SECONDS_TRANSLATIONS[getButtonStep()][1]));
    }

    /**
     * Updates the model with the new step and mode.
     * @param step The new step. Zero is step mode, 9 is real time mode, 1-8 is mc tick mode.
     */
    private void updateModel(int step) {
        if (step < 0 || step > 9) {
            return;
        }
        String clockMode = String.valueOf(MC_TICK);
        int speed = 0;
        switch (step) {
            case 0:         //Step mode
                clockMode = String.valueOf(STEP);
                break;
            case 9:         //Real time mode//Real time mode
                clockMode = String.valueOf(REALTIME);
                break;
            default:
                speed = MODE_TRANSLATIONS[step][1];
                break;
        }
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(handler.getBlockEntity().getPos());
        buf.writeString(clockMode);
        buf.writeInt(speed);
        ClientPlayNetworking.send(NetworkingConstants.SYNC_CLOCK_MODE_SELECTION, buf);
    }

    /**
     * Gets the step of the button that is currently selected.
     * The Information is taken from the handler.
     * @return the step of the button that is currently selected.
     */
    private int getButtonStep() {
        String mode = ((SystemClockBlockEntity) handler.getBlockEntity()).getSystemClockMode();
        int speed = ((SystemClockBlockEntity) handler.getBlockEntity()).getSystemClockSpeed();
        if (mode.equals(String.valueOf(STEP))) {
            return 0;
        } else if (mode.equals(String.valueOf(REALTIME))) {
            return 9;
        } else {
            for (int i = 1; i < 9; i++) {
                if (speed == MODE_TRANSLATIONS[i][1]) {
                    return i;
                }
            }
        }
        return 0;
    }

}
