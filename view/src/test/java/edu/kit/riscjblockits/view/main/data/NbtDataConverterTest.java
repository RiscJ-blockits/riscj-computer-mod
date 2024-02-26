package edu.kit.riscjblockits.view.main.data;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NbtDataConverterTest {

    @Test
    void nbtDataConverterTest() {
        NbtCompound nbtElement = new NbtCompound();
        nbtElement.put("key", NbtString.of("test"));
        IDataContainer data = (IDataContainer) new NbtDataConverter(nbtElement).getData();
        assertEquals("test", ((IDataStringEntry) data.get("key")).getContent());
    }

    @Test
    void unsupportedTypesTest() {
        NbtCompound nbtElement = new NbtCompound();
        nbtElement.put("key", NbtString.of("test"));
        nbtElement.put("key2", NbtByte.of((byte) 1));
        nbtElement.put("key3", NbtShort.of((short) 1));
        nbtElement.put("key4", NbtInt.of(7));
        nbtElement.put("key5", NbtLong.of(8));
        nbtElement.put("key6", NbtFloat.of(9));
        nbtElement.put("key7", NbtDouble.of(10));
        //nbtElement.put("key8", NbtByteArray.of(new byte[]{1, 2, 3})); //not possible
        nbtElement.put("key9", new NbtIntArray(new int[]{1, 2, 3}));
        nbtElement.put("key10", new NbtLongArray(new long[]{1, 2, 3}));
        //nbtElement.put("key11", new NbtList().set(0, NbtString.of("test"))); //not possible
        //nbtElement.put("key12", new NbtEnd()); //not possible
        //
        IDataContainer data = (IDataContainer) new NbtDataConverter(nbtElement).getData();
        assertEquals("test", ((IDataStringEntry) data.get("key")).getContent());
    }

}