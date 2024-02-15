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

/**
 * The manual screen.
 * In it, you can see the manual for the mod.
 * It contains a list with multiple entries, and a text widget which displays the text for the selected entry.
 */
public class ManualScreen extends Screen {

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/manual/manual_gui.png");
    private final int backgroundHeight;
    private final int backgroundWidth;

    /**
     * The text widget which displays the text for the selected entry.
     */
    private ScrollableTextWidget textWidget;

    /**
     * The list with all the categories.
     */
    private ScrollableListWidget<CategoryEntry> categoryList;

    /**
     * Constructor for the manual screen.
     * In it, you can see the manual for the mod.
     * @param title the title of the screen
     */
    public ManualScreen(Text title) {
        super(title);
        this.backgroundHeight = 180;
        this.backgroundWidth = 277;
    }

    /**
     * Initializes the manual screen.
     * Adds the text widget and the category list.
     */
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

    /**
     * Adds all categories to the manual screen.
     */
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

        categories.add(new CategoryEntry(0, 0, "manual.systemclock", "manual.systemclock.text", textWidget, textRenderer,
                RISCJ_blockits.SYSTEM_CLOCK_BLOCK.asItem().getDefaultStack()));

        categories.add(new CategoryEntry(0, 0, "manual.goggles", "manual.goggles.text", textWidget, textRenderer,
                RISCJ_blockits.GOGGLES_ITEM.getDefaultStack()));

        categories.add(new CategoryEntry(0, 0, "manual.redstoneinput", "manual.redstoneinput.text", textWidget, textRenderer,
                RISCJ_blockits.REDSTONE_INPUT_BLOCK_ITEM.getDefaultStack()));

        categories.add(new CategoryEntry(0, 0, "manual.quantumstateregister", "manual.quantumstateregister.text", textWidget, textRenderer,
                RISCJ_blockits.WIRELESS_REGISTER_BLOCK_ITEM.getDefaultStack()));

        categories.add(new CategoryEntry(0, 0, "manual.terminal", "manual.terminal.text", textWidget, textRenderer,
                RISCJ_blockits.TEXT_OUTPUT_BLOCK_ITEM.getDefaultStack()));

        categories.add(new CategoryEntry(0, 0, "manual.instruction_set", "manual.instruction_set.text", textWidget, textRenderer,
                RISCJ_blockits.INSTRUCTION_SET_ITEM_MIMA.getDefaultStack()));

        categories.get(0).select();
        categoryList = new ScrollableListWidget<>(categories, x, y, width, height, 0);
        addDrawableChild(categoryList);
    }

    /**
     * Renders in the background of the screen.
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
