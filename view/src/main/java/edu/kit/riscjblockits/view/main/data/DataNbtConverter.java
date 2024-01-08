package edu.kit.riscjblockits.view.main.data;


import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.data.IDataVisitor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;

/**
 * Converts IDataElement to NbtCompound.
 * Does this by recursively visiting the IDataElements.
 */
public class DataNbtConverter implements IDataVisitor {

    /**
     * The NbtCompound
     */
    private NbtElement compound;

    /**
     * Creates a new DataNbtConverter with the given IDataElement
     *
     * @param element The IDataElement to convert
     */
    public DataNbtConverter(IDataElement element) {
        compound = new NbtCompound();
        element.receive(this);
    }

    /**
     * Visits a DataContainer and recursively visits its children
     *
     * @param container The DataContainer to visit
     */
    @Override
    public void visit(IDataContainer container) {
        container.getKeys().forEach(key -> {
            IDataElement element = container.get(key);
            NbtElement nbt = new DataNbtConverter(element).getNbtElement();
            ((NbtCompound) compound).put(key, nbt);
        });
    }

    /**
     * Visits a DataStringEntry and creates a NbtString
     *
     * @param element The DataStringEntry to visit
     */
    @Override
    public void visit(IDataStringEntry element) {
        compound = NbtString.of(element.getContent());
    }

    /**
     * Gets the NbtElement
     * @return The NbtElement
     */
    public NbtElement getNbtElement() {
        return compound;
    }
}
