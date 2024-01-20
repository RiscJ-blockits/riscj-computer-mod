package edu.kit.riscjblockits.view.client.renderlistener;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.IGoggleQueryable;
import edu.kit.riscjblockits.view.main.items.goggles.GogglesItem;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;


/**
 * This class is responsible for rendering the UI when the player is looking at a block that implements {@link IGoggleQueryable}.
 */
public final class GoggleUI {
    private boolean visible;
    private final TextRenderer textRenderer;
    private final MinecraftClient minecraft;

    /**
     * Creates a new GoggleUI.
     * will save the minecraft instance and the text renderer instance.
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
        HitResult crosshairTarget = minecraft.crosshairTarget;
        // check if goggles are worn, only visible when worn
        visible = false;
        minecraft.player.getArmorItems().forEach(itemStack -> {
            if (itemStack.getItem() instanceof GogglesItem) {
                //visible.set(true);
                visible = true;
            }
        });
        if (!visible) {
            return;
        }

        if (crosshairTarget.getType() != HitResult.Type.BLOCK) {
            return;
        }
        BlockHitResult target = (BlockHitResult) crosshairTarget;

        BlockPos pos = target.getBlockPos();
        BlockEntity blockEntity = minecraft.world.getBlockEntity(target.getBlockPos());
        Block block = minecraft.world.getBlockState(pos).getBlock();
        if (blockEntity == null) {
            return;
        }

        if (!(blockEntity instanceof IGoggleQueryable goggleQueriable)) {
            return;
        }

        drawUI(context, goggleQueriable.getGoggleText());
    }

    /**
     * Draws the UI.
     * @param context the drawing context
     * @param text the text to draw
     */
    private void drawUI(DrawContext context, Text text) {
        int textHeight = textRenderer.fontHeight;
        int width = context.drawText(textRenderer, text, 30, 10, 0xFF255281, false);
        context.drawBorder(7, 5, width, 8 + textHeight, 0xFF0E6E7C);
        context.fillGradient(7, 5, 7 + width, 13 + textHeight, 0xA7008E74, 0xA714A1B4);

    }
}