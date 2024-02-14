package edu.kit.riscjblockits.view.client.screens.widgets.text;

public class Line {
    private String content = "";

    public Line() {
    }

    public Line(String lineString) {
        content = lineString;
    }

    public void insert(String text, int position) {
        StringBuilder builder = new StringBuilder(content);
        builder.insert(position, text);
        content = builder.toString();
    }

    public String cut(int from, int to) {
        StringBuilder builder = new StringBuilder(content);
        String ret = content.substring(from, to);
        builder.delete(from, to);
        content = builder.toString();
        return ret;
    }

    public String getContent() {
        return content;
    }

    public String getContent(int windowStartWidth) {
        if (windowStartWidth > content.length()) {
            return "";
        }
        return content.substring(windowStartWidth);
    }

    public String getContentUntil(int windowEndWidth) {
        return content.substring(0, windowEndWidth);
    }

    public void setContent(String s) {
        content = s;
    }
}
