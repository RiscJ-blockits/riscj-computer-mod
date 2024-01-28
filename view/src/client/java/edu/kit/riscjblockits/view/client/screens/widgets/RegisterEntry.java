package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;

public class RegisterEntry extends ListEntry {

    public static final int ENTRY_HEIGHT = 20;
    private final TextIconToggleWidget selectButton;
    private boolean missing;
    private String name;

    public RegisterEntry(String name, boolean missing) {
        this.name = name;
        this.missing = missing;
        this.selectButton = new TextIconToggleWidget(Text.literal(name), button -> {
            RegisterEntry.this.missing = !RegisterEntry.this.missing;
            ((TextIconToggleWidget) button).toggleSelected(RegisterEntry.this.missing);
            //TODO set other Button missing
        });


    }

    @Override
    public void render(DrawContext context, int mouseX,
                       int mouseY, float delta) {
        selectButton.setX(this.getX());
        selectButton.setY(this.getY());
        this.selectButton.render(context, mouseX, mouseY, delta);
    }

    @Override
    public int getHeight() {
       return ENTRY_HEIGHT;
    }


    public void update() {
        //TODO set Button inactive when selcted for current Register       this.selectButton.active = ;
        this.selectButton.toggleSelected(this.missing);
    }
}
