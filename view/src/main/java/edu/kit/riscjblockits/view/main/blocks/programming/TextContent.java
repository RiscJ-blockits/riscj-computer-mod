package edu.kit.riscjblockits.view.main.blocks.programming;

import java.util.ArrayList;
import java.util.List;

public class TextContent {
    public static final int windowHeight = 10;
    public static final int windowWidth = 25;

    public final List<Line> lines;

    private int currentLine;
    private int currentColumn;

    public int windowStartHeight = 0;
    public int windowStartWidth = 0;

    public TextContent() {
        lines = new ArrayList();
        lines.add(new Line());
        currentLine = 0;
        currentColumn = 0;
    }

    public static TextContent fromString(String s) {
        TextContent textContent = new TextContent();
        textContent.reLoad(s);
        return textContent;
    }

    public void updateWindow() {
        if (currentLine <= windowStartHeight)
            windowStartHeight = currentLine;

        if (currentLine >= windowStartHeight + windowHeight)
            windowStartHeight = currentLine - windowHeight + 1;

        if (currentColumn < windowStartWidth + windowWidth) {
            windowStartWidth = currentColumn - windowWidth;
            if (windowStartWidth < 0) {
                windowStartWidth = 0;
            }
        }

        if (currentColumn > windowStartWidth + windowWidth)
            windowStartWidth = currentColumn - windowWidth;

        if (windowStartHeight > lines.size() - windowHeight) {
            windowStartHeight = lines.size() - windowHeight;
            if (windowStartHeight < 0) {
                windowStartHeight = 0;
            }
        }

    }

    public void insert(String string) {
        if (!string.contains("\n")) {
            lines.get(currentLine).insert(string, currentColumn);
            currentColumn = currentColumn + string.length();
            updateWindow();
            return;
        }
        String[] splitString;
        if (string.equals("\n")) {
            splitString = new String[]{"", ""};
        }
        else {
            splitString = string.split("\n");
        }
        String afterInsertContent = "";
        if (currentColumn < lines.get(currentLine).content.length() - 1) {
            afterInsertContent = lines.get(currentLine).cut(currentColumn, lines.get(currentLine).content.length());
        }
        for (int i = 0; i < splitString.length; i++) {
            int j = i + currentLine;

            // create new line if necessary
            if ( j >= lines.size()) {
                lines.add(new Line());
                currentColumn = 0;
            }
            Line line = lines.get(j);
            line.insert(splitString[i], currentColumn);
            currentColumn = currentColumn + splitString[i].length();

        }
        lines.get(currentLine + splitString.length - 1).insert(afterInsertContent, currentColumn);
        currentColumn = afterInsertContent.length();
        currentLine = currentLine + splitString.length - 1;
        updateWindow();
    }



    public void delete(int i) {


        int columnDelta = 0;
        int from;
        int to;
        // backspace
        if (i < 0) {
            if (currentColumn == 0) {
                unhookLine(currentLine);
                return;
            }
            from = currentColumn + i ;
            to = currentColumn;
            columnDelta = i;
        }
        // delete
        else {
            if (lines.get(currentLine).content.length() == currentColumn) {
                unhookLine(currentLine + 1);
                return;
            }
            to = currentColumn + i ;
            from = currentColumn ;
        }
        if (from < 0 || to > lines.get(currentLine).content.length())
            return;
        currentColumn = currentColumn + columnDelta;
        lines.get(currentLine).cut(from, to);
        updateWindow();

    }

    private void unhookLine(int line) {
        // cant unhook the only line
        if (lines.size() == 1)
            return;
        if (lines.size() <= line)
            return;
        lines.get(line-1).content = lines.get(line-1).content + lines.get(line).content;
        lines.remove(line);
        currentLine--;
        if (currentLine < 0)
            currentLine = 0;
        currentColumn = lines.get(currentLine).content.length();
        updateWindow();
    }

    public void moveCursor(int i) {
        if (currentColumn + i > lines.get(currentLine).content.length()|| currentColumn + i < 0) {
            return;
        }
        currentColumn = currentColumn + i;
        updateWindow();
    }

    public void moveLine(int i) {
        if (currentLine + i >= lines.size() || currentLine + i < 0) {
            return;
        }
        currentLine = currentLine + i;
        if (lines.get(currentLine).content.length() < currentColumn) {
            currentColumn = lines.get(currentLine).content.length();
        }
        updateWindow();
    }


    public String getWindowedLineContent() {
        return lines.get(currentLine).getText(windowStartWidth, windowWidth).asTruncatedString(currentColumn);
    }

    public int getCurrentLine() {
        return currentLine;
    }

    @Override
    public String toString() {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(currentLine);
        textBuilder.append(',');
        textBuilder.append(currentColumn);
        textBuilder.append(',');
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            textBuilder.append(line.content);
            if (i + 1 != lines.size())
                textBuilder.append("\n");
        }
        return textBuilder.toString();
    }

    public void reLoad(String string) {
        String[] x = string.split(",", 3);
        if (x.length < 3) {
            return;
        }


        string = x[2];
        String[] lines = string.split("\n");
        for (int i=0; i< lines.length; i++) {
            if (this.lines.size() <= i)
                this.lines.add(new Line());
            this.lines.get(i).content = lines[i];
        }

        currentLine = Integer.parseInt(x[0]);
        currentColumn = Integer.parseInt(x[1]);
        // clamp values to prevent an error in data storage to crash the game
        currentLine = Math.max(0, Math.min(this.lines.size() -1, currentLine));
        currentColumn = Math.max(0, Math.min(this.lines.get(currentLine).content.length(), currentColumn));

    }
}