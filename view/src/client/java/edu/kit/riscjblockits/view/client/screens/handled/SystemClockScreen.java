package edu.kit.riscjblockits.view.client.screens.handled;

import com.mojang.blaze3d.systems.RenderSystem;
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

import static edu.kit.riscjblockits.model.blocks.ClockMode.REALTIME;
import static edu.kit.riscjblockits.model.blocks.RegisterModel.UNASSIGNED_REGISTER;

public class SystemClockScreen extends HandledScreen<SystemClockScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/system_clock/system_clock_gui.png");

    private Text clockSpeed = Text.literal("0");
    private Text clockMode = Text.literal("");

    public SystemClockScreen(SystemClockScreenHandler handler, PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
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
        context.drawCenteredTextWithShadow(textRenderer, clockSpeed, width / 2, height / 2, 0xffffff);
        context.drawCenteredTextWithShadow(textRenderer, clockMode, width / 2, height / 3, 0xffffff);
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        clockSpeed = Text.literal(handler.getSystemClockSpeed());
        clockMode = Text.literal(handler.getSystemClockMode());
    }

    //Stub für nicolas
    private void updateModel(int speed, String mode) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(handler.getBlockEntity().getPos());
       //mode = String.valueOf(REALTIME);
        buf.writeString(mode);
        buf.writeInt(speed);
        ClientPlayNetworking.send(NetworkingConstants.SYNC_CLOCK_MODE_SELECTION, buf);
    }

}
