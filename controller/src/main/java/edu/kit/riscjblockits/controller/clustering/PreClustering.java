package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.view.main.blocks.modblock.ModblockEntity;            //FixMe circular dependency
//import net.minecraft.block.entity.BlockEntity;                                      //very dirty workaround

import java.util.ArrayList;

public class PreClustering {
    ArrayList<ModblockEntity> blocks = new ArrayList<ModblockEntity>();

    private int status;
    public PreClustering() {
        status = 1;
    }

    public PreClustering(ModblockEntity entity) {
        status = 0;
        blocks.add(entity);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void addBlocks(ArrayList<ModblockEntity> newBlocks) {
        blocks.addAll(newBlocks);
    }

    public PreClustering combine(PreClustering otherClustering) {
        otherClustering.addBlocks(blocks);
        //add other values
        return otherClustering;
    }
}
