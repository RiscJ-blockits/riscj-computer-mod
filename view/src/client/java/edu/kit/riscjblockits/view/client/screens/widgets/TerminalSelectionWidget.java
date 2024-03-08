package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.TerminalScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io.TerminalScreenHandler.DisplayMode;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.blocks.RegisterModel.UNASSIGNED_REGISTER;

/**
 * A widget that allows the user to select a terminal register.
 */
public class TerminalSelectionWidget extends RegSelectWidget{

    /**
     * The identifier for the "mode" register.
     */
    public static final String MODE = "Mode";   //TODO make it clenaer with translatable (?)
    /**
     * The identifier for the "Out" register.
     */
    public static final String OUT = "Out";
    /**
     * The identifier for the "In" register.
     */
    public static final String IN = "In";
    private static final Identifier IOM_BUTTON_TEXTURE = new Identifier("riscj_blockits", "textures/gui/register/io/tab_unselected.png");
    private static final Identifier IOM_BUTTON_HIGHLIGHTED_TEXTURE = new Identifier("riscj_blockits", "textures/gui/register/io/tab_selected.png");
    private static final int IOM_BUTTON_WIDTH = 35;
    private static final int IOM_BUTTON_HEIGHT = 26;
    private IconButtonWidget inButton;
    private IconButtonWidget outButton;
    private IconButtonWidget modeButton;
    private DisplayMode displayMode;

    /**
     * Creates a new TerminalSelectionWidget.
     */
    public TerminalSelectionWidget(){
        super();
        displayMode = DisplayMode.IN;
    }

    @Override
    public void initialize(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow,
                           RegisterScreenHandler registerScreenHandler) {
        super.initialize(parentWidth, parentHeight, client, narrow, registerScreenHandler);

        int i = (this.parentWidth - 147) / 2 - this.leftOffset - (IOM_BUTTON_WIDTH - 2) + 1;
        int j = (this.parentHeight - 166) / 2 + 17;

        inButton = new IconButtonWidget(i, j, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, button -> displayMode = DisplayMode.IN, IOM_BUTTON_TEXTURE);

        outButton = new IconButtonWidget(i, j + IOM_BUTTON_HEIGHT, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, button -> displayMode = DisplayMode.OUT, IOM_BUTTON_TEXTURE);

        modeButton = new IconButtonWidget(i, j + IOM_BUTTON_HEIGHT * 2, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, button -> displayMode = DisplayMode.MODE, IOM_BUTTON_TEXTURE);

        addChild(inButton);
        addChild(outButton);
        addChild(modeButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = (this.parentWidth - 147) / 2 - this.leftOffset - (IOM_BUTTON_WIDTH - 2) + 1;
        int j = (this.parentHeight - 166) / 2 + 17;

        super.render(context, mouseX, mouseY, delta);
        if(!this.isOpen()) {
            return;
        }
        switch (this.displayMode){
            case IN:
                context.drawTexture(IOM_BUTTON_HIGHLIGHTED_TEXTURE, i, j, 0,0, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT);
                break;
            case OUT:
                context.drawTexture(IOM_BUTTON_HIGHLIGHTED_TEXTURE, i, j + IOM_BUTTON_HEIGHT, 0, 0, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT);
                break;
            case MODE:
                context.drawTexture(IOM_BUTTON_HIGHLIGHTED_TEXTURE, i, j + IOM_BUTTON_HEIGHT * 2, 0, 0, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT);
                break;
            default: break;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        context.drawText(textRenderer, IN, i + 8, j + (IOM_BUTTON_HEIGHT - 6) / 2, 0xffffff, false);
        context.drawText(textRenderer,
            OUT, i + 8, j + IOM_BUTTON_HEIGHT + (IOM_BUTTON_HEIGHT - 6) / 2, 0xffffff, false);
        context.drawText(textRenderer,
            Text.literal(MODE), i + 8, j + IOM_BUTTON_HEIGHT * 2 + (IOM_BUTTON_HEIGHT - 6) / 2, 0xffffff, false);
    }

    @Override
    public void deselectRegister() {
        assignRegister(UNASSIGNED_REGISTER);
    }

    @Override
    public void assignRegister(String name) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        switch (displayMode){
            case IN:
                buf.writeString(IN +"-"+ name);
                break;
            case OUT:
                buf.writeString(OUT +"-"+ name);
                break;
            case MODE:
                buf.writeString(MODE +"-"+ name);
                break;
        }
        ClientPlayNetworking.send(NetworkingConstants.SYNC_REGISTER_SELECTION, buf);
    }

    @Override
    public void update(){
        super.update();
        registerList.updateEntries(getEntries());
    }

    private List<RegisterEntry> getEntries() {
        List<RegisterEntry> entries = new ArrayList<>();
        for (String register: registerScreenHandler.getRegisters(DataConstants.REGISTER_MISSING)) {
            RegisterEntry entry = new RegisterEntry(register, true, false, this);
            entries.add(entry);
        }
        for (String register: registerScreenHandler.getRegisters(DataConstants.REGISTER_FOUND)) {
            RegisterEntry entry;
            if(register.equals(((TerminalScreenHandler) registerScreenHandler).getCurrentRegister(displayMode))){
                entry = new RegisterEntry(register, false, true, this);
            } else {
                entry = new RegisterEntry(register, false, false, this);
            }
            entries.add(entry);
        }
        return entries;
    }

}
