package edu.kit.riscjblockits.view.client.renderlistener;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.IGoggleQueryable;
import edu.kit.riscjblockits.view.main.items.goggles.GogglesItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is responsible for rendering the UI when the player is looking at a block that implements {@link IGoggleQueryable}.
 */
public final class GoggleUI {

    private final TextRenderer textRenderer;
    private final MinecraftClient minecraft;

    /**
     * Creates a new GoggleUI.
     * Will save the minecraft instance and the text renderer instance.
     */
    public GoggleUI() {
        minecraft = MinecraftClient.getInstance();
        textRenderer = minecraft.textRenderer;
    }

    /**
     * Method that will be called every frame.
     * Will render the UI if the player is looking at a block that implements {@link IGoggleQueryable}.
     * @param context the drawing context
     */
    public void onRenderGameOverlay(DrawContext context) {
        AtomicBoolean visible = new AtomicBoolean(false);
        HitResult crosshairTarget = minecraft.crosshairTarget;
        // check if goggles are worn, only visible when worn
        visible.set(false);
        assert minecraft.player != null;
        minecraft.player.getArmorItems().forEach(itemStack -> {
            if (itemStack.getItem() instanceof GogglesItem) {
                visible.set(true);
            }
        });
        if (!visible.get()) {
            return;
        }
        assert crosshairTarget != null;
        if (crosshairTarget.getType() != HitResult.Type.BLOCK) {
            return;
        }
        BlockHitResult target = (BlockHitResult) crosshairTarget;
        assert minecraft.world != null;
        BlockEntity blockEntity = minecraft.world.getBlockEntity(target.getBlockPos());
        if (blockEntity == null) {
            return;
        }
        if (!(blockEntity instanceof IGoggleQueryable goggleQueryable)) {
            return;
        }
        drawUI(context, goggleQueryable.getGoggleText());
    }

    /**
     * Draws the UI.
     * Will draw the given text in the top-middle of the screen.
     * Multiple lines will be wrapped.
     * @param context the drawing context
     * @param text the text to draw
     */
    private void drawUI(DrawContext context, Text text) {
        int textHeight = textRenderer.fontHeight;
        int screenWidth = context.getScaledWindowWidth();

        List<OrderedText> lines = textRenderer.wrapLines(text, screenWidth);
        int padding = 2;
        // get the width of the longest line
        int cleanWidth = 0;
        for (OrderedText line : lines) {
            int width = textRenderer.getWidth(line);
            if (width > cleanWidth) {
                cleanWidth = width;
            }
        }
        // calculate the position and width to draw
        int width = cleanWidth + 2 * padding;
        int x = screenWidth/2 - width/2;
        // draw the background
        context.drawBorder(x - padding, 5, width + padding, 8 + textHeight * lines.size(), 0xFF000000);
        context.fill(x - padding, 5, x + width, 13 + textHeight * lines.size(), 0x80000000);
        // draw the text line by line
        for (int i = 0; i < lines.size(); i++) {
            int lineWidth = textRenderer.getWidth(lines.get(i));
            context.drawText(textRenderer, lines.get(i), x + (cleanWidth - lineWidth)/2 + padding, 10 + textHeight * i, 0xFF13FFFD, false);
        }

    }
}
