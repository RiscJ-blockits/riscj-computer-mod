package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.client.screens.widgets.IconButtonWidget;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
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

import static edu.kit.riscjblockits.model.blocks.ClockMode.REALTIME;
import static edu.kit.riscjblockits.model.blocks.RegisterModel.UNASSIGNED_REGISTER;

public class SystemClockScreen extends HandledScreen<SystemClockScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/system_clock/system_clock_gui.png");
    private static final Identifier MODE_BUTTON_TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/system_clock/system_clock_button.png");
    private static final String MODE_TEXTURE = "textures/gui/system_clock/system_clock_lever_%d.png";
    private static final int MODE_BUTTON_SIZE = 6;
    private static final int[] MODE_BUTTON_X_OFFSETS = {86, 84, 90, 98, 109, 122, 135, 146, 154, 160};
    private static final int[] MODE_BUTTON_Y_OFFSETS = {62, 49, 37, 28, 20, 18, 20, 28, 37, 49};
    private static final int LEVER_X_OFFSET = 96;
    private static final int LEVER_Y_OFFSET = 23;
    private static final int LEVER_WIDTH = 58;
    private static final int LEVER_HEIGHT = 39;
    private static final ArrayList<IconButtonWidget> modeButtons = new ArrayList<>(10);
    private static final int SPEED_TEXTFIELD_OFFSET_X = 9;
    private static final int SPEED_TEXTFIELD_OFFSET_Y = 18;
    private Identifier
        leverTexture = new Identifier(RISCJ_blockits.MODID, "textures/gui/system_clock/system_clock_lever_0.png");

    private Text clockSpeed = Text.literal("0");
    private Text clockMode = Text.literal("");

    public SystemClockScreen(SystemClockScreenHandler handler, PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        int mode = handler.getSystemClockMode();
        leverTexture = new Identifier(RISCJ_blockits.MODID, String.format(MODE_TEXTURE, mode));

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        for(int i = 0; i < 10; i++) {
            int setMode = i;
            modeButtons.add(new IconButtonWidget(x + MODE_BUTTON_X_OFFSETS[setMode], y + MODE_BUTTON_Y_OFFSETS[setMode], MODE_BUTTON_SIZE, MODE_BUTTON_SIZE, button -> {
                handler.setSystemClockMode(setMode);
                leverTexture = new Identifier(RISCJ_blockits.MODID, String.format(MODE_TEXTURE, setMode));
            }, MODE_BUTTON_TEXTURE));

            addDrawableChild(modeButtons.get(i));
        }



        //loop thorugh Screen Elements
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawText(textRenderer, clockSpeed, x + SPEED_TEXTFIELD_OFFSET_X, y + SPEED_TEXTFIELD_OFFSET_Y, 0xffffff, false);
        context.drawTexture(leverTexture, x + LEVER_X_OFFSET, y + LEVER_Y_OFFSET, 0, 0, LEVER_WIDTH, LEVER_HEIGHT, LEVER_WIDTH, LEVER_HEIGHT);
        for(int i = 0; i < 10; i++) {
            modeButtons.get(i).setPosition(x + MODE_BUTTON_X_OFFSETS[i], y + MODE_BUTTON_Y_OFFSETS[i]);
        }
        //context.drawCenteredTextWithShadow(textRenderer, clockMode, width / 2, height / 3, 0xffffff);
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        clockSpeed = Text.literal(handler.getSystemClockSpeed());
        clockMode = Text.literal(handler.getSystemClockMode() + "");
    }

    //Stub für nicolas
    private void updateModel(int speed, String mode) {
        //TODO update this to only need a single int
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(handler.getBlockEntity().getPos());
       //mode = String.valueOf(REALTIME);
        buf.writeString(mode);
        buf.writeInt(speed);
        ClientPlayNetworking.send(NetworkingConstants.SYNC_CLOCK_MODE_SELECTION, buf);
    }

}
