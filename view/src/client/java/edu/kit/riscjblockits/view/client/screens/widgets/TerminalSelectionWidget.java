package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TerminalSelectionWidget extends RegSelectWidget{

    private static final Identifier IOM_BUTTON_TEXTURE = new Identifier("riscj_blockits", "textures/gui/register/io/tab_unselected.png");
    private static final Identifier IOM_BUTTON_HIGHLIGHTED_TEXTURE = new Identifier("riscj_blockits", "textures/gui/register/io/tab_selected.png");
    private static final int IOM_BUTTON_WIDTH = 35;
    private static final int IOM_BUTTON_HEIGHT = 26;
    public static final String MODE = "Mode"; //TODO make it clenaer with translatable (?)
    public static final String OUT = "Out";
    public static final String IN = "In";
    private IconButtonWidget inButton;
    private IconButtonWidget outButton;
    private IconButtonWidget modeButton;


    public TerminalSelectionWidget(){
        super();
    }

    @Override
    public void initialize(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow,
                           RegisterScreenHandler registerScreenHandler) {
        super.initialize(parentWidth, parentHeight, client, narrow, registerScreenHandler);

        int i = (this.parentWidth - 147) / 2 - this.leftOffset - (IOM_BUTTON_WIDTH - 2) + 1;
        int j = (this.parentHeight - 166) / 2 + 17;

        inButton = new IconButtonWidget(i, j, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, (button) -> {
            //set Mode
            //TODO implement
        }, IOM_BUTTON_TEXTURE);

        outButton = new IconButtonWidget(i, j + IOM_BUTTON_HEIGHT, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, (button) -> {
            //set  MOde
            //TODO implement
        }, IOM_BUTTON_TEXTURE);

        modeButton = new IconButtonWidget(i, j + IOM_BUTTON_HEIGHT * 2, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, (button) -> {
            //set Mode
            //TODO implement
        }, IOM_BUTTON_TEXTURE);

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

        /*switch (this.registerScreenHandler.getTerminalBlockEntity().getMode()){ //TODO GET mode
            case TerminalBlockEntity.MODE_IN: //TODO check modes
                context.drawTexture(IOM_BUTTON_HIGHLIGHTED_TEXTURE, i, j, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT);
                break;
            case TerminalBlockEntity.MODE_OUT:
                context.drawTexture(IOM_BUTTON_HIGHLIGHTED_TEXTURE, i, j + IOM_BUTTON_HEIGHT, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT);
                break;
            case TerminalBlockEntity.MODE_MODE:
                context.drawTexture(IOM_BUTTON_HIGHLIGHTED_TEXTURE, i, j + IOM_BUTTON_HEIGHT * 2, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT, IOM_BUTTON_WIDTH, IOM_BUTTON_HEIGHT);
                break;
        }
        //TODO implement @Leon
         */

        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 100.0f);

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        context.drawText(textRenderer, IN, i + 8, j + (IOM_BUTTON_HEIGHT - 6) / 2, 0xffffff, false);
        context.drawText(textRenderer,
            OUT, i + 8, j + IOM_BUTTON_HEIGHT + (IOM_BUTTON_HEIGHT - 6) / 2, 0xffffff, false);
        context.drawText(textRenderer,
            Text.literal(MODE), i + 8, j + IOM_BUTTON_HEIGHT * 2 + (IOM_BUTTON_HEIGHT - 6) / 2, 0xffffff, false); //TODO Replace with translateable

        context.getMatrices().pop();

    }
}
