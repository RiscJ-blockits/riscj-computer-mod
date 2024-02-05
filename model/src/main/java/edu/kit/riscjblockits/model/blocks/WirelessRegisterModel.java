package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import java.util.HashMap;

public class WirelessRegisterModel extends RegisterModel {
    private int frequence;

    private static HashMap<Integer, Value> wirelessValue = new HashMap<>();

    public WirelessRegisterModel() {
        super();
        frequence = 0;
        if (wirelessValue.isEmpty()) {
            for (int i = 0; i < 16; i++) {
                wirelessValue.put(i, new Value());
            }
        }
        super.setValue(wirelessValue.get(frequence));
        setUnqueriedStateChange(false);
    }

    @Override
    public void setValue(Value value) {
        wirelessValue.put(frequence, value);
        super.setValue(wirelessValue.get(frequence));
    }

    @Override
    public IDataElement getData() {
        super.setValue(wirelessValue.get(frequence));
        setUnqueriedStateChange(false);
        IDataElement regData = super.getData();
        //ToDo add frequence to regData
        //ToDo add wirelessValue to regData
        return regData;
    }

    @Override
    public Value getValue() {
        super.setValue(wirelessValue.get(frequence));
        setUnqueriedStateChange(false);
        return super.getValue();
    }

    public void incrementFrequence() {
        frequence++;
        if (frequence > 15) {
            frequence = 0;
        }
        System.out.println("Frequence: " + frequence);
    }
}
