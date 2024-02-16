package edu.kit.riscjblockits.view.client.screens.widgets.text;

/**
 * This class represents a line of text.
 * it allows to insert and cut text at a specific position.
 */
public class Line {
    private String content = "";

    /**
     * Creates a new line with an empty content.
     */
    public Line() {
    }

    /**
     * Creates a new line with the given content.
     * @param lineString The initial content of the line.
     */
    public Line(String lineString) {
        content = lineString;
    }

    /**
     * Inserts the given text at the given position.
     * @param text The text to insert.
     * @param position The position to insert the text at.
     */
    public void insert(String text, int position) {
        StringBuilder builder = new StringBuilder(content);
        builder.insert(position, text);
        content = builder.toString();
    }

    /**
     * Cuts the text from the given position to the given position.
     * @param from The start position.
     * @param to The end position.
     * @return The cut text.
     */
    public String cut(int from, int to) {
        StringBuilder builder = new StringBuilder(content);
        String ret = content.substring(from, to);
        builder.delete(from, to);
        content = builder.toString();
        return ret;
    }

    /**
     * returns the content of the line.
     * @return The content of the line.
     */
    public String getContent() {
        return content;
    }

    /**
     * returns the content of the line starting from the given position.
     * @param windowStartWidth The position to start from.
     * @return The content of the line starting from the given position.
     */
    public String getContent(int windowStartWidth) {
        if (windowStartWidth > content.length()) {
            return "";
        }
        return content.substring(windowStartWidth);
    }

    /**
     * returns the content of the line until the given position.
     * @param windowEndWidth The position to end at.
     * @return The content of the line until the given position.
     */
    public String getContentUntil(int windowEndWidth) {
        if (windowEndWidth > content.length()) {
            return content;
        }
        return content.substring(0, windowEndWidth);
    }

    public void setContent(String s) {
        content = s;
    }

    /**
     * returns the length of the word before the given position.
     * @param cursorX The position to start from.
     * @return The length of the word before the given position.
     */
    public int getPreviousWordLength(int cursorX) {
        // find next char that is not a space
        int i = cursorX - 1;
        for (; i >= 0; i--) {
            if (content.charAt(i) != ' ') {
                break;
            }
        }
        // from here on find the next space
        for (; i >= 0; i--) {
            if (content.charAt(i) == ' ') {
                return cursorX - i - 1;
            }
        }
        return 0;
    }

    /**
     * returns the length of the word after the given position.
     * @param cursorX The position to start from.
     * @return The length of the word after the given position.
     */
    public int getNextWordLength(int cursorX) {
        // find next char that is not a space
        int i = cursorX;
        for (; i < content.length() - cursorX; i++) {
            if (content.charAt(i) != ' ') {
                break;
            }
        }
        // from here on find the next space
        for (; i < content.length() - cursorX; i++) {
            if (content.charAt(i) == ' ') {
                return cursorX + i;
            }
        }
        return 0;
    }
}
