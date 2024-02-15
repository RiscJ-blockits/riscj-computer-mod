package edu.kit.riscjblockits.view.client.screens.widgets.text;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblerSyntaxTextEditWidget extends TextEditWidget {

    private static final Pattern LABEL_PATTERN = Pattern.compile("\\w+:");
    private static final Pattern COMMAND_PATTERN = Pattern.compile("\\w+ (?:\\w+[, ] *)*");
    private static final Pattern ARGUMENT_PATTERN_AFTER_COM = Pattern.compile("\\w+");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("[;#].*");
    private static final int LABEL_COLOR = 0x00FF00;
    private static final int COMMAND_COLOR = 0x00FF00;
    private static final int ARGUMENT_COLOR = 0x00FF00;
    private static final int COMMENT_COLOR = 0x00FF00;

    public AssemblerSyntaxTextEditWidget(TextRenderer textRenderer, int x, int y, int width, int height) {
        super(textRenderer, x, y, width, height);
    }

    @Override
    protected void drawLine(DrawContext context, String line, int windowStartX, int i, int displayY) {
        int pos = 0;
        Matcher matcher = LABEL_PATTERN.matcher(line);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            context.drawText(textRenderer, line.substring(start, end), windowStartX + start, displayY, LABEL_COLOR, false);
        }
    }
}
