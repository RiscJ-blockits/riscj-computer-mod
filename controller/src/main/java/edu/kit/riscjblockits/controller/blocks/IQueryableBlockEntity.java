package edu.kit.riscjblockits.controller.blocks;

import java.util.List;

public interface IQueryableBlockEntity {
    public void setBlockModel();

    List<BlockController> getComputerNeighbours();
}
