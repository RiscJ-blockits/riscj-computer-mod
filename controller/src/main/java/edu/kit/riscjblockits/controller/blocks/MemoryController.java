package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.MemoryModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_MEMORY;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_PROGRAMM_ITEM;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_QUERY_LINE;

/**
 * The controller for a Memory block entity.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class MemoryController extends ComputerBlockController {

    /**
     * Creates a new MemoryController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public MemoryController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity, BlockControllerType.MEMORY);
    }

    /**
     * Creates the Memory specific model.
     * @return The model for the Memory.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new MemoryModel();
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        super.setData(data);
        if (!data.isContainer()) {
            return;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(MEMORY_MEMORY)) {
                if (((IDataContainer) data).get(s) == null) {           //programm Item has been removed
                    ((MemoryModel) getModel()).setMemory(null);
                    onUpdate();
                    return;
                }
                IDataContainer memoryData;
                try {
                    memoryData = (IDataContainer) ((IDataContainer) ((IDataContainer) data).get(s)).get(MEMORY_PROGRAMM_ITEM);
                } catch (ClassCastException e) {
                    return;     //silent fail with malformed data
                }
                if (memoryData.getKeys().isEmpty()) {
                    return;     //happens during a world load when the nbt is loaded
                }
                Memory memory = Memory.fromData(memoryData);
                boolean newMemory = ((MemoryModel) getModel()).setMemory(memory);
                if (newMemory) onUpdate();
            } else if (s.equals(MEMORY_QUERY_LINE)) {
                long line = Long.parseLong(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent());
                ((MemoryModel) getModel()).setMemoryQueryLine(line);
            }
        }
    }

    /**
     * Method to call checkFinished in the associated clusterHandler.
     */
    private void onUpdate() {
        ClusterHandler clusterHandler = getClusterHandler();
        if (clusterHandler != null)
            clusterHandler.checkFinished();
    }

    /**
     * Returns the value at the given address.
     * @param address The address to read from.
     * @return The value at the given address.
     */
    public Value getValue(Value address) {
        //ToDo do checks?
        return ((MemoryModel) getModel()).getMemoryAt(address);
    }

    /**
     * Writes the given value to the given address.
     * @param address The address to write to.
     * @param value The value to write.
     */
    public void writeMemory(Value address, Value value) {
        //ToDo do checks?
        ((MemoryModel) getModel()).setMemoryAt(address, value);
    }

    /**
     * Getter for the initial program counter.
     * @return The initial program counter.
     */
    public Value getInitialProgramCounter() {
        return ((MemoryModel) getModel()).getInitialProgramCounter();
    }

    public boolean isMemorySet() {
        return ((MemoryModel) getModel()).isMemorySet();
    }

}
