package edu.kit.riscjblockits.view.client.screens.widgets.text;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.kit.riscjblockits.view.main.RISCJ_blockits.MOD_ID;

/**
 * A widget that displays a text edit with syntax highlighting for the assembler.
 */
public class AssemblerSyntaxTextEditWidget extends TextEditWidget {
    private static final Identifier UNDER_LINE_TEXTURE = new Identifier(MOD_ID, "textures/gui/general/syntax_error_underline.png");
    private static final int UNDER_LINE_HEIGHT = 1;
    private static final Pattern LABEL_PATTERN = Pattern.compile("\\w+:");
    private static final Pattern COMMAND_PATTERN = Pattern.compile("(\\w+)");
    private static final Pattern ARGUMENT_PATTERN_FIRST= Pattern.compile(" +\\w+");
    private static final Pattern ARGUMENT_PATTERN_AFTER_COM = Pattern.compile(" *[, ] *(-?\\w+(\\(?\\w+\\))?)");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("[;#].*");
    private static final int LABEL_COLOR = 0xf5bf69;
    private static final int COMMAND_COLOR = 0xcb6d2f;
    private static final int ARGUMENT_COLOR = 0x9876aa;
    private static final int COMMENT_COLOR = 0x52954e;
    private HashMap<String, Integer> instructionArgumentCountMap = new HashMap<>();

    /**
     * A widget that displays a text edit with syntax highlighting for the assembler.
     * @param textRenderer The text renderer.
     * @param x The x position.
     * @param y The y position.
     * @param width The width.
     * @param height The height.
     */
    public AssemblerSyntaxTextEditWidget(TextRenderer textRenderer, int x, int y, int width, int height) {
        super(textRenderer, x, y, width, height);
    }
    public void setInstructionArgumentCountMap(HashMap<String, Integer> instructionArgumentCountMap) {
        this.instructionArgumentCountMap = instructionArgumentCountMap;
    }

    @Override
    protected void drawLine(DrawContext context, String line, int windowStartX, int i, int displayX, int displayY) {
        int pos = 0;
        int x = displayX;
        int renderedInfront = 0;
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
                String command = commandMatcher.group(0);
                // if the command is not in the map, it is not a valid command
                if (start < closestStart && instructionArgumentCountMap.containsKey(command)){
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
                renderedInfront += closestStart;
                line = line.substring(closestStart);
            }
            // draw next match
            if (closestType == TokenType.COMMENT) {
                int start = commentMatcher.start() - renderedInfront;
                int end = commentMatcher.end() - renderedInfront;
                if (windowStartX < pos + end - start + renderedInfront)
                    x += drawComment(context, line.substring(Math.max(windowStartX - pos - renderedInfront, 0), end - start), x, displayY);
                pos += end;
            }
            if (closestType == TokenType.LABEL) {
                int start = labelMatcher.start() - renderedInfront;
                int end = labelMatcher.end() - renderedInfront;
                if (windowStartX < pos + end - start + renderedInfront)
                    x += drawLabel(context, line.substring(Math.max(windowStartX - pos - renderedInfront, 0), end - start), x, displayY);
                pos += end;
            }
            if (closestType == TokenType.COMMAND) {
                int start = commandMatcher.start() - renderedInfront;
                int end = commandMatcher.end() - renderedInfront;
                int nX = x;
                String command = line.substring(start, end);
                if (windowStartX < pos + end - start + renderedInfront)
                    nX += drawCommand(context, line.substring(Math.max(windowStartX - pos - renderedInfront, 0), end - start), nX, displayY);
                pos += end;
                String rest = line.substring(end);

                // match, count and draw arguments
                int argumentCount = 0;
                Matcher argumentMatcher = ARGUMENT_PATTERN_FIRST.matcher(rest);
                while (argumentMatcher.find() && argumentMatcher.start() == 0) {
                    end = argumentMatcher.end();
                    if (windowStartX < pos + end + renderedInfront)
                        nX += drawArgument(context, rest.substring(Math.max(windowStartX - pos - renderedInfront, 0), end), nX, displayY);
                    pos += end;
                    rest = rest.substring(end);
                    argumentMatcher = ARGUMENT_PATTERN_AFTER_COM.matcher(rest);
                    argumentCount++;
                }
                // if the command is in the map (could not be due to threading), check if the argument count is correct
                if (instructionArgumentCountMap.containsKey(command) && argumentCount > instructionArgumentCountMap.get(command)) {
                    markError(context, x, displayY, nX);
                }
                x = nX;

            }

            if (pos >= line.length()) {
                return;
            }
            line = line.substring(pos);
        }
    }

    private void markError(DrawContext context, int x, int displayY, int x2) {
        context.drawTexture(UNDER_LINE_TEXTURE, x, displayY + LINE_HEIGHT - UNDER_LINE_HEIGHT - 1, 0,0, 0, x2 - x, UNDER_LINE_HEIGHT, 276, 59);
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
