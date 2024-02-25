package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.memory.MemoryScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * The MemoryListWidget class represents a widget that displays a list of memory entries.
 * It implements the Drawable interface for rendering purposes.
 */
public class MemoryListWidget implements Drawable {

    /**
     * The number of entries to display on the same page.
     */
    public static final int ENTRY_AMOUNT = 9;
    private static final Identifier SCROLLER_TEXTURE = new Identifier("container/creative_inventory/scroller");
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int ENTRY_HEIGHT = 11;
    private static final int ENTRY_WIDTH = 74;
    private static final int ENTRY_OFFSET = 2;
    private static final int SCROLLBAR_OFFSET = 1;
    private static final int SCROLL_MULTIPLIER = 4;

    /**
     * The handler for the memory screen.
     */
    private final MemoryScreenHandler handler;
    private int x;
    private int y;
    private final int width;
    private final int height;

    /**
     * The current scroll position.
     */
    private long scrollPosition;

    /**
     * Constructor for the MemoryListWidget.
     * @param handler The handler for the memory screen.
     * @param x The x position of the widget.
     * @param y The y position of the widget.
     * @param width The width of the widget.
     * @param height The height of the widget.
     */
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

        for (int i = 0; i <  ENTRY_AMOUNT; i++) {

            long line = (scrollPosition / ENTRY_HEIGHT + i);
            // only draw an entry when it is between top and bottom of the scroll widget
            if ((line + 1) * ENTRY_HEIGHT >= scrollPosition && line * ENTRY_HEIGHT < scrollPosition + height) {
                String addresHexString = Long.toHexString(line).toUpperCase();
                context.drawText(textRenderer, Text.literal("0".repeat(addresHexString.length() % 2) + addresHexString),
                        this.x + 2, this.y + 2 + i * ENTRY_HEIGHT, 0xffffff, true);
                String lineContent = handler.getMemoryLine(line).substring(Math.max(handler.getMemoryLine(line).length() - 8, 0));
                context.drawText(textRenderer, Text.literal(lineContent),
                    this.x + 2 + ENTRY_WIDTH + ENTRY_OFFSET, this.y + 2 + i * ENTRY_HEIGHT, 0xffffff, true);
            }
        }
        context.disableScissor();
        context.drawGuiTexture(SCROLLER_TEXTURE, this.x + this.width + SCROLLBAR_OFFSET,
            (this.y + getScrollbarPosition()), SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);
    }

    private int getScrollbarPosition() {
        return (int) MathHelper.clamp((((scrollPosition * this.height) / (getContentsHeight()))), 0, this.height - SCROLLBAR_HEIGHT);
    }

    protected long getContentsHeight() {
        return Math.max(this.handler.getMemorySize() * ENTRY_HEIGHT, ENTRY_HEIGHT * ENTRY_AMOUNT);
    }

    /**
     * Handles a mouse scroll event.
     * Changes the memory lines displayed based on the scroll amount.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param horizontalAmount The horizontal scroll amount.
     * @param verticalAmount The vertical scroll amount.
     */
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollPosition -= (long) (SCROLL_MULTIPLIER * verticalAmount);
        if (scrollPosition < 0) {
            scrollPosition = 0;
        } else if (scrollPosition > getContentsHeight() - height) {
            scrollPosition = getContentsHeight() - height;
        }
        updateQueriedLine();
    }

    /**
     * Updates the position of the widget.
     * @param x The x position of the widget.
     * @param y The y position of the widget.
     */
    public void updatePos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Jumps to a specific line in the memory screen.
     * Used by the search funktion.
     * @param line The line to jump to.
     */
    public void jumpToLine(long line) {
        if (line < 0 || line > handler.getMemorySize()) {
            return;
        }
        scrollPosition = line * ENTRY_HEIGHT;
        updateQueriedLine();
    }

    private void updateQueriedLine() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(handler.getBlockEntity().getPos());
        buf.writeLong(scrollPosition / ENTRY_HEIGHT);
        ClientPlayNetworking.send(NetworkingConstants.SYNC_MEMORY_LINE_QUERY, buf);
    }

}
