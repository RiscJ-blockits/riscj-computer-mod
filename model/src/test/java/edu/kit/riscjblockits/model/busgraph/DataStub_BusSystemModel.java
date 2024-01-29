package edu.kit.riscjblockits.model.busgraph;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

public class DataStub_BusSystemModel extends BusSystemModel{

    Value presentData;

    boolean activeVisualization;
    public DataStub_BusSystemModel(Value presentData, boolean activeVisualization) {
        super();
        this.presentData = presentData;
        this.activeVisualization = activeVisualization;
    }

    @Override
    public Value getPresentData() {
        return presentData;
    }

    @Override
    public boolean getActiveVisualization(BlockPosition blockPosition) {
        return activeVisualization;
    }

}
