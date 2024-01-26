package edu.kit.riscjblockits.view.client.screens.wigets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;
import edu.kit.riscjblockits.view.client.screens.wigets.TextIconToggleWidget;

import java.util.List;

public class RegisterEntry { //implement Interfaces
    private final TextIconToggleWidget selectButton;
    private boolean missing;
    private String name;

    public RegisterEntry(String name, boolean missing) {
        this.name = name;
        this.missing = missing;
        this.selectButton = new TextIconToggleWidget(Text.literal(name), button -> {
            this.missing = !this.missing;
            ((TextIconToggleWidget) button).toggleSelected(this.missing);
            //TODO set other Button missing
        });
    }

    public void render(DrawContext context,int x, int y, int mouseX,
                       int mouseY, float tickDelta) {
        this.selectButton.setX(x);
        this.selectButton.setY(y);
        this.selectButton.render(context, mouseX, mouseY, tickDelta);
    }

    public void update() {
        //TODO set Button inactive when selcted for current Register       this.selectButton.active = ;
        this.selectButton.toggleSelected(this.missing);
    }
}
