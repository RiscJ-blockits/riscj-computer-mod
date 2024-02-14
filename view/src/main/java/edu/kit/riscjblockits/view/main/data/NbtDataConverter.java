package edu.kit.riscjblockits.view.main.data;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtByteArray;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtEnd;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.visitor.NbtElementVisitor;

/**
 * Converts NbtCompound to IDataElement.
 * Does this by recursively visiting the NbtElements.
 */
public class NbtDataConverter implements NbtElementVisitor {

    /**
     * The data element
     */
    private IDataElement data;

    /**
     * Creates a new NbtDataConverter with the given NbtCompound
     *
     * @param compound The NbtCompound to convert
     */
    public NbtDataConverter(NbtElement compound) {
        this.data = new Data();
        compound.accept(this);
    }

    /**
     * Visits a NbtString and creates a DataStringEntry
     * @param element The NbtString to visit
     */
    @Override
    public void visitString(NbtString element) {
        data = new DataStringEntry(element.asString());
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtByte
     * @param element The NbtByte to visit
     */
    @Override
    public void visitByte(NbtByte element) {
        //NOT SUPPORTED BY US
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtShort
     * @param element The NbtShort to visit
     */
    @Override
    public void visitShort(NbtShort element) {
        //NOT SUPPORTED BY US
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtInt
     * @param element The NbtInt to visit
     */
    @Override
    public void visitInt(NbtInt element) {
        //NOT SUPPORTED BY US
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtLong
     * @param element The NbtLong to visit
     */
    @Override
    public void visitLong(NbtLong element) {
        //NOT SUPPORTED BY US
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtFloat
     * @param element The NbtFloat to visit
     */
    @Override
    public void visitFloat(NbtFloat element) {
        //NOT SUPPORTED BY US
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtDouble
     * @param element The NbtDouble to visit
     */
    @Override
    public void visitDouble(NbtDouble element) {
        //NOT SUPPORTED BY US
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtByteArray
     * @param element The NbtByteArray to visit
     */
    @Override
    public void visitByteArray(NbtByteArray element) {
        //NOT SUPPORTED BY US
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtIntArray
     * @param element The NbtIntArray to visit
     */
    @Override
    public void visitIntArray(NbtIntArray element) {
        //NOT SUPPORTED BY US
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtLongArray
     * @param element The NbtLongArray to visit
     */
    @Override
    public void visitLongArray(NbtLongArray element) {
        //NOT SUPPORTED BY US
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtList
     * @param element The NbtList to visit
     */
    @Override
    public void visitList(NbtList element) {
        //NOT SUPPORTED BY US
    }

    /**
     * Visits a NbtCompound
     * Creates a new Data and visits all keys
     * @param compound The NbtCompound to visit
     */
    @Override
    public void visitCompound(NbtCompound compound) {
        compound.getKeys().forEach(key -> {
            NbtElement element = compound.get(key);
            IDataElement dataElement = new NbtDataConverter(element).getData();
            ((IDataContainer) data).set(key, dataElement);
        });
    }

    /** NOT SUPPORTED BY US
     * Visits a NbtEnd
     * @param element The NbtEnd to visit
     */
    @Override
    public void visitEnd(NbtEnd element) {
        //NOT SUPPORTED BY US
    }

    /**
     * Returns the converted IDataElement
     * @return The converted IDataElement
     */
    public IDataElement getData() {
        return data;
    }

}
