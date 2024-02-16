package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * A RegisterEntry is a widget that represents a register in a list.
 * It can be selected and deselected.
 */
public class RegisterEntry extends ListEntry {

    /**
     * The height of a register entry.
     */
    public static final int ENTRY_HEIGHT = 20;
    private final TextIconToggleWidget selectButton;
    private boolean missing;
    private boolean currentReg;
    private final String name;
    private final RegSelectWidget parent;

    /**
     * Creates a new RegisterEntry.
     * @param name The name of the register.
     * @param missing true if the register is missing
     * @param currentReg true if the register is the current register
     * @param parent The parent RegSelectWidget.
     */
    public RegisterEntry(String name, boolean missing, boolean currentReg, RegSelectWidget parent) {
        this.name = name;
        this.parent = parent;
        this.missing = missing;
        this.currentReg = currentReg;
        this.selectButton = new TextIconToggleWidget(Text.literal(name), button -> {
            if(RegisterEntry.this.missing) {
                RegisterEntry.this.assignRegister(RegisterEntry.this.name);
            } else if (!RegisterEntry.this.currentReg) {
                RegisterEntry.this.assignRegister(RegisterEntry.this.name);
            } else {
                RegisterEntry.this.deselectRegister();
            }
        }, !missing, currentReg);
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(isMouseOver(mouseX, mouseY)){
            return this.selectButton.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.selectButton.isMouseOver(mouseX, mouseY);
    }

    public Object getName() {
        return this.name;
    }

    public boolean isMissing() {
        return this.missing;
    }

    public boolean isCurrentReg() {
        return this.currentReg;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }

    public void setCurrentReg(boolean currentReg) {
        this.currentReg = currentReg;
    }

    /**
     * Updates the state of the register entry.
     * @param missing true if the register is missing
     * @param currentReg true if the register is the current register
     */
    public void update(boolean missing, boolean currentReg){
        this.missing = missing;
        this.currentReg = currentReg;
        this.selectButton.update(!this.missing, this.currentReg);
    }

    /**
     * Initial situation: The selected button represents this registers selection.
     * Result: This register is [NOT_ASSIGNED]
     */
    public void deselectRegister() {
        parent.deselectRegister();
    }

    /**
     * Initial situation: The selected button represents a missing register Type.
     * Result: Register is configured to be the selected register.
     * @param name the name of the assigned register
     */
    public void assignRegister(String name) {
        parent.assignRegister(name);
    }
}
