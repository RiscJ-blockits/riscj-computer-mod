package edu.kit.riscjblockits.view.main.data;

import edu.kit.riscjblockits.controller.data.IDataStringEntry;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;

public class DataStringEntry implements IDataStringEntry, IDataNBTConvertable {

    private String content;

    public DataStringEntry(String string) {
        content = string;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public NbtElement toNbt() {
        return NbtString.of(content);
    }
}
