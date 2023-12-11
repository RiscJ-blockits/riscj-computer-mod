package edu.kit.riscjblockits.view.main.blocks;

import edu.kit.riscjblockits.view.main.Registrariat;
import edu.kit.riscjblockits.view.main.blocks.modblock.ModblockEntity;
import edu.kit.riscjblockits.controller.clustering.PreClustering;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TestBlockEntity extends ModblockEntity {
    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(Registrariat.TEST_BLOCK_ENTITY, pos, state);
        clusteringTest();
    }

    private void clusteringTest() {
        PreClustering pre = new PreClustering();
        pre.setStatus(5);
        int a = pre.getStatus();
    }
}
