package edu.kit.riscjblockits.view.main.blocks.programming;

import net.minecraft.text.Text;

public class Line{
    public String content = "";

    public Line() {

    }

    void insert(String text, int position) {
        StringBuilder builder = new StringBuilder(content);
        builder.insert(position, text);
        content = builder.toString();
    }

    String cut(int from, int to) {
        StringBuilder builder = new StringBuilder(content);
        String ret = content.substring(from, to);
        builder.delete(from, to);
        content = builder.toString();
        return ret;
    }

    public Text getText() {
        return Text.of(content);
    }

    public Text getText(int windowStartWidth, int windowWidth) {
        if (windowStartWidth > content.length()) {
            return Text.empty();
        }
        int end = windowStartWidth + windowWidth;
        if (end > content.length())
            end = content.length();
        return Text.of(content.substring(windowStartWidth, end));
    }
}
