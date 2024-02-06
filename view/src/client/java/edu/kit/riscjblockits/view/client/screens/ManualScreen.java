package edu.kit.riscjblockits.view.client.screens;

import edu.kit.riscjblockits.view.client.screens.widgets.CategoryEntry;
import edu.kit.riscjblockits.view.client.screens.widgets.ScrollableListWidget;
import edu.kit.riscjblockits.view.client.screens.widgets.ScrollableTextWidget;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ManualScreen extends Screen {


    ScrollableTextWidget textWidget;
    ScrollableListWidget<CategoryEntry> categoryList;

    public ManualScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        textWidget = new ScrollableTextWidget(this.textRenderer, this.width/2, 0, this.width/2, this.height);
        textWidget.setText(I18n.translate("manual.introduction"));
        addDrawableChild(textWidget);

        addCategories();

        super.init();
    }

    private void addCategories() {
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


        categories.get(0).select();
        categoryList = new ScrollableListWidget<>(categories, 0, 0, this.width/2, this.height, 20);
        addDrawableChild(categoryList);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }


    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }


}
