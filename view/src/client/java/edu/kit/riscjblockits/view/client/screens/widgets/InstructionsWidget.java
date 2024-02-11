package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.client.screens.handled.ProgrammingScreen;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class InstructionsWidget extends ExtendableWidget{

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID,"textures/gui/programming/instructions_widget.png");
    private static final String TO_DO_TEXT = "Available Instructions";
    private static final String ID_TITLE = "ID";
    private static final String ARGUMENTS_TITLE = "Arguments";
    private InstructionListWidget instructionList;

    public InstructionsWidget() {

    }

    public void initialize(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow, ProgrammingScreenHandler handler) {
        super.initialize(parentWidth, parentHeight, narrow, TEXTURE);
        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight) / 2;
        this.handler = handler;
        this.instructionList = new InstructionListWidget(this.getEntries(), i + 8, j + 18, 113, 130);
        this.open = false;
    }

    private List<InstructionEntry> getEntries() {
        List<InstructionEntry> entries = new ArrayList<>();
        entries.add(new InstructionEntry("ID", "Arguments"));
        List<String[]> instructions = ((ProgrammingScreenHandler) handler).getInstructions();
        for(String[] instruction : instructions) {
            String id = instruction[0];
            String arguments = instruction[1];
            InstructionEntry entry = new InstructionEntry(id, arguments);
            entries.add(entry);
        }
        return entries;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.isOpen()) {
            return;
        }
        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 100.0f);

        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 1, 1, 147, 166);

        MinecraftClient client = MinecraftClient.getInstance();
        context.drawText(client.textRenderer, TO_DO_TEXT, i + 7, j + 6, 0x555555, false);
        //context.drawText(client.textRenderer, ID_TITLE, i + 9, j + 19, 0xffffff, false);
        //context.drawText(client.textRenderer, ARGUMENTS_TITLE, i + 42, j + 19, 0xffffff, false);

        this.instructionList.render(context, mouseX, mouseY, delta);

        context.getMatrices().pop();

    }

    public void update() {
        this.instructionList.updateEntries(this.getEntries());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return instructionList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
