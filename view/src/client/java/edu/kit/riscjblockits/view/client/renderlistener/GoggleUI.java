package edu.kit.riscjblockits.view.client.renderlistener;

import edu.kit.riscjblockits.view.main.blocks.computer.IGoggleQueryable;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class GoggleUI {
    boolean visible = true;
    private final TextRenderer textRenderer;
    private final MinecraftClient minecraft;
    public GoggleUI() {
        minecraft = MinecraftClient.getInstance();
        textRenderer = minecraft.textRenderer;
    }
    public void onRenderGameOverlay(DrawContext context) {
        HitResult crosshairTarget = minecraft.crosshairTarget;

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

        drawUI(context, goggleQueriable.getGoggleText(), block.asItem().getDefaultStack());
    }

    private void drawUI(DrawContext context, Text text, ItemStack stack) {
        int textHeight = textRenderer.fontHeight;
        int width = context.drawText(textRenderer, text, 30, 10, 0xFF17FF77, false);
        context.drawItem(stack, 8, 5);
        context.drawBorder(7, 5, width, textHeight, 0xFFFF7700);
        context.fill(7, 5, 7 + width, 8 + textHeight, 0x77FF7700);

    }
}