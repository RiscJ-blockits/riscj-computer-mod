package edu.kit.riscjblockits.view.client.screens.wigets;

import com.google.common.collect.Lists;
import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
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
    private final RegisterScreenHandler registerScreenHandler;


    public RegisterListWidget(int x, int y, int width, int height, RegisterScreenHandler registerScreenHandler) {
        super(x,y,width,height, Text.literal(""));
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.registerScreenHandler = registerScreenHandler;
        children = Lists.newArrayList();
        for (String register: registerScreenHandler.getRegisters(DataConstants.REGISTER_MISSING)) {
            this.addEntry(new RegisterEntry(register, true));
            System.out.println("Added Register");
        }
        for (String register: registerScreenHandler.getRegisters(DataConstants.REGISTER_FOUND)) {
            this.addEntry(new RegisterEntry(register, false));
            System.out.println("Added Register");
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //render Buttons
        context.enableScissor(x, y, x + width, y + height);
        this.renderContents(context, mouseX, mouseY, delta);
        context.disableScissor();


        renderButton(context, mouseX, mouseY, delta);
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
        return (double) 140 / getContentsHeight();
    }

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
        int startIndex = 0;
        int endIndex = this.children.size() - 1;
        if(this.children.size() > 7){
            startIndex = (int) (((getContentsHeight() * getScrollAmount()) - 70) / 20);
            endIndex = (int) (((getContentsHeight() * getScrollAmount()) + 70) / 20);
        }

        for (int i = startIndex; i < endIndex; i++) {
            x = this.x + 8;
            y = this.y + 19 + (i - startIndex) * ENTRY_HEIGHT;
            children.get(i).render(context,x, y, mouseX, mouseY, delta); //adjust to be exchangable type
        }
    }

    public void addEntry(RegisterEntry entry) {
        this.children.add(entry);
    }

    private double getScrollAmount(){
        return (this.getScrollY() - this.y) / (this.height  - SCROLLBAR_HEIGHT );
    }

    @Override
    public void drawBox(DrawContext context) {

    }

    public void update() {
        this.children.clear();
        for (String register: registerScreenHandler.getRegisters(DataConstants.REGISTER_MISSING)) {
            this.addEntry(new RegisterEntry(register, true));
            System.out.println("Added Register");
        }
        for (String register: registerScreenHandler.getRegisters(DataConstants.REGISTER_FOUND)) {
            this.addEntry(new RegisterEntry(register, false));
            System.out.println("Added Register");
        }
    }
}
