package edu.kit.riscjblockits.view.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import edu.kit.riscjblockits.view.client.screens.widgets.CategoryEntry;
import edu.kit.riscjblockits.view.client.screens.widgets.ScrollableListWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.ScrollableTextWidget;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ManualScreen extends Screen {

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/manual/manual_gui.png");
    private final int backgroundHeight;
    private final int backgroundWidth;

    ScrollableTextWidget textWidget;
    ScrollableListWidget<CategoryEntry> categoryList;

    public ManualScreen(Text title) {
        super(title);
        this.backgroundHeight = 180;
        this.backgroundWidth = 277;

    }

    @Override
    protected void init() {

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;


        textWidget = new ScrollableTextWidget(this.textRenderer, x + 150, y + 40, 100, 100);
        textWidget.setText(I18n.translate("manual.introduction"));
        addDrawableChild(textWidget);


        addCategories(x + 25, y + 40, 100, 120);

        super.init();
    }

    private void addCategories(int x, int y, int width, int height) {
        List<CategoryEntry> categories = new ArrayList<>();
        categories.add(new CategoryEntry(0, 0, "manual.introduction", "manual.introduction.text", textWidget, textRenderer));
        categories.add(new CategoryEntry(0, 0, "manual.controlunit", "manual.controlunit.text", textWidget, textRenderer,
                RISCJ_blockits.CONTROL_UNIT_BLOCK.asItem().getDefaultStack()));
        categories.add(new CategoryEntry(0, 0, "manual.register", "manual.register.text", textWidget, textRenderer,
                RISCJ_blockits.REGISTER_BLOCK.asItem().getDefaultStack()));

        categories.add(new CategoryEntry(0, 0, "manual.alu", "manual.alu.text", textWidget, textRenderer,
                RISCJ_blockits.ALU_BLOCK.asItem().getDefaultStack()));

        categories.add(new CategoryEntry(0, 0, "manual.bus", "manual.bus.text", textWidget, textRenderer,
                RISCJ_blockits.BUS_BLOCK.asItem().getDefaultStack()));

        categories.add(new CategoryEntry(0, 0, "manual.programming", "manual.programming.text", textWidget, textRenderer,
                RISCJ_blockits.PROGRAMMING_BLOCK.asItem().getDefaultStack()));

        categories.add(new CategoryEntry(0, 0, "manual.memory", "manual.memory.text", textWidget, textRenderer,
                RISCJ_blockits.MEMORY_BLOCK.asItem().getDefaultStack()));

        categories.add(new CategoryEntry(0, 0, "manual.memory", "manual.memory.text", textWidget, textRenderer,
                RISCJ_blockits.GOGGLES_ITEM.getDefaultStack()));


        categories.get(0).select();
        categoryList = new ScrollableListWidget<>(categories, x, y, width, height, 0);
        addDrawableChild(categoryList);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }


    /**
     * Renders the background of the screen.
     * @param context the drawing context
     * @param mouseX the x position of the mouse
     * @param mouseY the y position of the mouse
     * @param delta the time delta since the last frame
     */
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        // minecraft doing minecraft things
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
    }



}