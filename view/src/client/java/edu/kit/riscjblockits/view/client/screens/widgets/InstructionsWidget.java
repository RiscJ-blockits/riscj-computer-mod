package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A widget that displays the available instructions.
 */
public class InstructionsWidget extends ExtendableWidget {

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MOD_ID,"textures/gui/programming/instructions_widget.png");
    private static final String TO_DO_TEXT = "Available Instructions";
    private static final String ID_TITLE = "ID";
    private static final String ARGUMENTS_TITLE = "Arguments";
    private static final int LIST_WIDTH = 113;
    private static final int LIST_HEIGHT = 130;
    private static final int LIST_OFFSETX = 8;

    private static final int LIST_OFFSETY = 28;

    /**
     * A scrollable list of {@link InstructionEntry}s.
     */
    private InstructionListWidget instructionList;

    /**
     * A widget that displays the available instructions.
     */
    public InstructionsWidget() {
        //nothing to initialize
    }

    /**
     * Initializes the widget.
     * @param parentWidth The width of the parent.
     * @param parentHeight The height of the parent.
     * @param client The Minecraft client.
     * @param narrow Whether the widget is open.
     * @param handler The handler for the screen.
     */
    public void initialize(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow, ProgrammingScreenHandler handler) {
        super.initialize(parentWidth, parentHeight, narrow, TEXTURE);
        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight) / 2;
        this.handler = handler;
        this.instructionList = new InstructionListWidget(this.getEntries(), i + LIST_OFFSETX, j + LIST_OFFSETY, LIST_WIDTH, LIST_HEIGHT);
        this.open = false;
    }

    /**
     * Getter for the entries.
     * @return Gets the entries from the {@link ProgrammingScreenHandler}.
     */
    private List<InstructionEntry> getEntries() {
        List<InstructionEntry> entries = new ArrayList<>();
        List<String[]> instructions = ((ProgrammingScreenHandler) handler).getInstructions();

        for(String[] instruction : instructions) {
            String id = instruction[0];
            String arguments = instruction[1];
            InstructionEntry entry = new InstructionEntry(id, arguments);
            entries.add(entry);
        }
        //sort the entries with their id
        entries.sort(Comparator.comparing(InstructionEntry::getId));
        return entries;
    }

    /**
     * Renders the widget.
     * @param context The context to render in.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param delta Not specified in the documentation.
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.isOpen()) {
            return;
        }

        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 1, 1, 147, 166);
        MinecraftClient client = MinecraftClient.getInstance();
        context.drawText(client.textRenderer, TO_DO_TEXT, i + 7, j + 6, 0x555555, false);
        context.drawText(client.textRenderer, ID_TITLE, i + 9, j + 19, 0xffffff, false);
        context.drawText(client.textRenderer, ARGUMENTS_TITLE, i + 42, j + 19, 0xffffff, false);
        this.instructionList.render(context, mouseX, mouseY, delta);
    }

    /**
     * Updates the widget with new entries.
     */
    public void update() {
        this.instructionList.updateEntries(this.getEntries());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return instructionList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

}
