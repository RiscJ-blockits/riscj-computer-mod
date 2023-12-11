package edu.kit.riscjblockits.view.main.blocks;

import edu.kit.riscjblockits.view.main.Registrariat;
import edu.kit.riscjblockits.view.main.blocks.modblock.ModblockEntity;
import edu.kit.riscjblockits.controller.clustering.PreClustering;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TestBlockEntity extends ModblockEntity {

    public static final String ID = "TestBlockEntity";
    BlockPos position;
    BlockPos[] neighborsGroup;

    PreClustering preClustering;

    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(Registrariat.TEST_BLOCK_ENTITY, pos, state);
        position = pos;
        clusteringTest();
    }

    private void clusteringTest() {
        boolean found = false;
        preClustering = new PreClustering();
        if (getWorld().getBlockEntity(position.west()).getType() == Registrariat.TEST_BLOCK_ENTITY) {
            //Mod Block found -> combine our Clustering with theirs
            preClustering = preClustering.combine(((TestBlockEntity) getWorld().getBlockEntity(position.west())).getPreClustering());
            found = true;
        }
        if (getWorld().getBlockEntity(position.east()).getType() == Registrariat.TEST_BLOCK_ENTITY) {
            //Mod Block found

        }
        if (getWorld().getBlockEntity(position.north()).getType() == Registrariat.TEST_BLOCK_ENTITY) {
            //Mod Block found

        }
        if (getWorld().getBlockEntity(position.south()).getType() == Registrariat.TEST_BLOCK_ENTITY) {
            //Mod Block found

        }
        if (getWorld().getBlockEntity(position.up()).getType() == Registrariat.TEST_BLOCK_ENTITY) {
            //Mod Block found

        }
        if (getWorld().getBlockEntity(position.down()).getType() == Registrariat.TEST_BLOCK_ENTITY) {
            //Mod Block found

        }

    }


    public World getWorld() {
        return this.getWorld();
    }

    public PreClustering getPreClustering() {
        return preClustering;
    }

}
