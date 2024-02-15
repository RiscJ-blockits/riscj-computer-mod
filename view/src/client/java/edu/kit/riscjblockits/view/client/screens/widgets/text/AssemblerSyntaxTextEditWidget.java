package edu.kit.riscjblockits.view.client.screens.widgets.text;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblerSyntaxTextEditWidget extends TextEditWidget {

    private static final Pattern LABEL_PATTERN = Pattern.compile("\\w+:");
    private static final Pattern COMMAND_PATTERN = Pattern.compile("(\\w+ +)");
    private static final Pattern ARGUMENT_PATTERN_FIRST= Pattern.compile("\\w+");
    private static final Pattern ARGUMENT_PATTERN_AFTER_COM = Pattern.compile(" *[, ] *\\w+");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("[;#].*");
    private static final int LABEL_COLOR = 0x0000FF;
    private static final int COMMAND_COLOR = 0xFF0000;
    private static final int ARGUMENT_COLOR = 0xFFFF00;
    private static final int COMMENT_COLOR = 0x00FF00;

    public AssemblerSyntaxTextEditWidget(TextRenderer textRenderer, int x, int y, int width, int height) {
        super(textRenderer, x, y, width, height);
    }

    @Override
    protected void drawLine(DrawContext context, String line, int windowStartX, int i, int displayX, int displayY) {
        int pos = 0;
        int x = displayX;
        while (!line.isEmpty()) {

            // find the closest match
            int closestStart = Integer.MAX_VALUE;
            TokenType closestType = null;
            Matcher labelMatcher = LABEL_PATTERN.matcher(line);
            if (labelMatcher.find()) {
                int start = labelMatcher.start();
                if (start < closestStart) {
                    closestStart = start;
                    closestType = TokenType.LABEL;
                }
            }
            Matcher commandMatcher = COMMAND_PATTERN.matcher(line);
            if (commandMatcher.find()) {
                int start = commandMatcher.start();
                if (start < closestStart) {
                    closestStart = start;
                    closestType = TokenType.COMMAND;
                }
            }
            Matcher commentMatcher = COMMENT_PATTERN.matcher(line);
            if (commentMatcher.find()) {
                int start = commentMatcher.start();
                if (start < closestStart) {
                    closestStart = start;
                    closestType = TokenType.COMMENT;
                }
            }
            // no highlightable text found --> render all remaining chars normal
            if (closestType == null) {
                if (windowStartX < pos + line.length())
                    drawNormal(context, line.substring(Math.max(windowStartX - pos, 0)), x, displayY);
                return;
            }
            // render the text before the match
            if (closestStart > 0) {
                if (windowStartX < pos + closestStart)
                    x += drawNormal(context, line.substring(Math.max(windowStartX - pos, 0), closestStart), x, displayY);
                line = line.substring(closestStart);
            }
            // draw next match
            if (closestType == TokenType.COMMENT) {
                int start = commentMatcher.start();
                int end = commentMatcher.end();
                if (windowStartX < pos + end - start)
                    x += drawComment(context, line.substring(Math.max(windowStartX - pos, 0), end - start), x, displayY);
                pos += end;
            }
            if (closestType == TokenType.LABEL) {
                int start = labelMatcher.start();
                int end = labelMatcher.end();
                if (windowStartX < pos + end - start)
                    x += drawLabel(context, line.substring(Math.max(windowStartX - pos, 0), end - start), x, displayY);
                pos += end;
            }
            if (closestType == TokenType.COMMAND) {

                int start = commandMatcher.start();
                int end = commandMatcher.end();
                if (windowStartX < pos + end - start)
                    x += drawCommand(context, line.substring(Math.max(windowStartX - pos, 0), end - start), x, displayY);
                pos += end;
                String rest = line.substring(commandMatcher.end());
                Matcher argumentMatcher = ARGUMENT_PATTERN_FIRST.matcher(rest);
                while (argumentMatcher.find() && argumentMatcher.start() == 0) {
                    end = argumentMatcher.end();
                    if (windowStartX < pos + end)
                        x += drawArgument(context, rest.substring(Math.max(windowStartX - pos, 0), end), x, displayY);
                    pos += end;
                    rest = rest.substring(end);
                    argumentMatcher = ARGUMENT_PATTERN_AFTER_COM.matcher(rest);
                }
            }

            if (pos >= line.length()) {
                return;
            }
            line = line.substring(pos);
        }
    }

    private int drawCommand(DrawContext context, String substring, int x, int displayY) {
        context.drawText(textRenderer, substring, x, displayY, COMMAND_COLOR, false);
        return textRenderer.getWidth(substring);
    }

    private int drawArgument(DrawContext context, String substring, int x, int displayY) {
        context.drawText(textRenderer, substring, x, displayY, ARGUMENT_COLOR, false);
        return textRenderer.getWidth(substring);
    }

    private int drawLabel(DrawContext context, String substring, int x, int displayY) {
        context.drawText(textRenderer, substring, x, displayY, LABEL_COLOR, false);
        return textRenderer.getWidth(substring);
    }

    private int drawComment(DrawContext context, String substring, int x, int displayY) {
        context.drawText(textRenderer, substring, x, displayY, COMMENT_COLOR, false);
        return textRenderer.getWidth(substring);
    }

    private int drawNormal(DrawContext context, String line, int x, int displayY) {
        context.drawText(textRenderer, line, x, displayY, TEXT_COLOR, false);
        return textRenderer.getWidth(line);
    }


    private enum TokenType {
        LABEL, COMMAND, COMMENT
    }
}
