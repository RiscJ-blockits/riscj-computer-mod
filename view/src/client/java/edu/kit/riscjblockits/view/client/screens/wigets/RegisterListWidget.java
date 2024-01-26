package edu.kit.riscjblockits.view.client.screens.wigets;

import com.google.common.collect.Lists;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class RegisterListWidget extends ScrollableWidget {

    public static final Identifier SCROLLBAR = new Identifier(RISCJ_blockits.MODID, "textures/gui/general/scroller.png");

    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int ENTRY_HEIGHT = 20;
    private final List<RegisterEntry> children; //Adjust to be exchangable type
    private static final int maxScrollPosition = 159;
    private ToggleButtonWidget toggleNeededButton;
    private int x;
    private int y;
    private int width;
    private int height;




    public RegisterListWidget(MinecraftClient minecraftClient, int x, int y, int width, int height) {
        super(x,y,width,height, Text.literal(""));
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        children = Lists.newArrayList();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //render Buttons
        context.enableScissor(x, y, x + width, y + height);
        this.renderContents(context, mouseX, mouseY, delta);
        context.disableScissor();
        //render Scrollbar
        int x = this.x + 128;
        int y = this.y - SCROLLBAR_HEIGHT/2 + ((int) this.getScrollAmount()) * (140);
        context.drawGuiTexture(SCROLLBAR, x, y, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    protected int getContentsHeight() {
        return this.children.size() * ENTRY_HEIGHT;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return ENTRY_HEIGHT / 2.0;
    }

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
        int startIndex = 0;
        int endIndex = this.children.size() - 1;
        if(this.children.size() > 7){
            startIndex = (int) (((getContentsHeight() * getScrollAmount()) - 70) / 20);
            endIndex = (int) (((getContentsHeight() * getScrollAmount()) + 70) % 20);
        }

        for (int i = startIndex; i < endIndex; i++) {
            //TODO check if entry is visible (maybe)
            x = this.x + 8;
            y = this.y + 19 + i * ENTRY_HEIGHT;
            children.get(i).render(context,x, y, mouseX, mouseY, delta); //adjust to be exchangable type
        }
    }

    public void addEntry(RegisterEntry entry) {
        this.children.add(entry);
    }

    private double getScrollAmount(){
        return (this.getScrollY() - this.y - SCROLLBAR_HEIGHT/2.0) / (this.height - this.y - SCROLLBAR_HEIGHT/2.0);
    }
}
