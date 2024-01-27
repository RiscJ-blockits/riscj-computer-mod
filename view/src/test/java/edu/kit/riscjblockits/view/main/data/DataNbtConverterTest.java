package edu.kit.riscjblockits.view.main.data;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import net.minecraft.nbt.NbtCompound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataNbtConverterTest {

    @Test
    void convertNbtAndData() {
        Data data = new Data();
        data.set("key", new DataStringEntry("test"));
        NbtCompound nbt = (NbtCompound) new DataNbtConverter(data).getNbtElement();

        Data newData = (Data) new NbtDataConverter(nbt).getData();
        assertEquals("test", ((IDataStringEntry) newData.get("key")).getContent());
    }
    
}