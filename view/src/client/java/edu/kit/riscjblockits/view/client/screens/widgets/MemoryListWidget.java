package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.client.screens.handled.MemoryScreen;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.memory.MemoryScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class MemoryListWidget implements Drawable {

    private static final Identifier SCROLLER_TEXTURE = new Identifier("container/creative_inventory/scroller");
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int ENTRY_HEIGHT = 11;
    private static final int ENTRY_WIDTH = 74;
    private static final int ENTRY_OFFSET = 2;
    private static final int SCROLLBAR_OFFSET = 3;
    private static final int ENTRY_AMOUNT = 9;
    private static final int SCROLL_MULTIPLIER = 4;
    private final MemoryScreenHandler handler;
    private int x;
    private int y;
    private final int width;
    private final int height;
    private int scrollPosition;


    public MemoryListWidget(MemoryScreenHandler handler, int x, int y, int width, int height) {
        this.handler = handler;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        context.enableScissor(this.x, this.y, this.x + this.width, this.y + this.height);

        for (int i = 0 ; i <  ENTRY_AMOUNT; i++) {

            int line = (scrollPosition / ENTRY_HEIGHT + i);
            // only draw an entry when it is between top and bottom of scroll widget
            if (line * ENTRY_HEIGHT >= scrollPosition && line * ENTRY_HEIGHT < scrollPosition + height) {

                context.drawText(textRenderer, Text.literal(line + ""), this.x + 2, this.y + 2 + i * ENTRY_HEIGHT, 0x555555, false);
                String lineContent = handler.getMemoryLine(line).substring(Math.max(handler.getMemoryLine(line).length() - 8, 0));
                context.drawText(textRenderer, Text.literal(lineContent), this.x + 2 + ENTRY_WIDTH + ENTRY_OFFSET, this.y + 2 + i * ENTRY_HEIGHT, 0x555555, false);
            }
        }

        context.disableScissor();

        context.drawGuiTexture(SCROLLER_TEXTURE, this.x + this.width + SCROLLBAR_OFFSET, this.y + getScrollbarPosition(), SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);

    }

    private int getScrollbarPosition() {
        return MathHelper.clamp(((scrollPosition * height) / (getContentsHeight() - this.height) ), 0, this.height - SCROLLBAR_HEIGHT);
    }

    protected int getContentsHeight() {
        return Math.max(this.handler.getMemorySize() * ENTRY_HEIGHT, ENTRY_HEIGHT * ENTRY_AMOUNT);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollPosition -= (int) (SCROLL_MULTIPLIER * verticalAmount);
        if (scrollPosition < 0) {
            scrollPosition = 0;
        } else if (scrollPosition > getContentsHeight() - height) {
            scrollPosition = getContentsHeight() - height;
        }
        return true;
    }

    public void updatePos(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
