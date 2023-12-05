package edu.kit.riscjblockits.model.viewrepresentations.bussystem;

import edu.kit.riscjblockits.model.viewrepresentations.StateQueryableInterface;

import java.util.ArrayList;
import java.util.List;

public class Bussystem implements StateQueryableInterface {
    private List<Bus> busList = new ArrayList<Bus>();
}
