package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockModelTest {

    @Test
    void position() {
        BlockPosition blockPosition = new BlockPosition(0, 0, 0);
        BlockModel blockModel = new BlockModel() {
            @Override
            public IDataElement getData() {
                return null;
            }
        };

        blockModel.setPosition(blockPosition);
        assertEquals(blockPosition, blockModel.getPosition());
    }

    @Test
    void type() {
        BlockModel blockModel = new BlockModel() {
            @Override
            public IDataElement getData() {
                return null;
            }
        };

        blockModel.setType(ModelType.MEMORY);
        assertEquals(ModelType.MEMORY, blockModel.getType());
    }


    @Test
    void hasUnqueriedStateChange() {
        BlockModel blockModel = new BlockModel() {
            @Override
            public IDataElement getData() {
                return null;
            }
        };

        blockModel.setUnqueriedStateChange(true);
        assertTrue(blockModel.hasUnqueriedStateChange());
    }

    @Test
    void onStateQuery() {
        BlockModel blockModel = new BlockModel() {
            @Override
            public IDataElement getData() {
                return null;
            }
        };

        blockModel.setUnqueriedStateChange(true);
        blockModel.onStateQuery();
        assertFalse(blockModel.hasUnqueriedStateChange());
    }

    @Test
    void onStateChange() {
        BlockModel blockModel = new BlockModel() {
            @Override
            public IDataElement getData() {
                return null;
            }
        };

        blockModel.setUnqueriedStateChange(false);
        blockModel.onStateChange();
        assertTrue(blockModel.hasUnqueriedStateChange());
    }

    @Test
    void visualisationState() {
        BlockModel blockModel = new BlockModel() {
            @Override
            public IDataElement getData() {
                return null;
            }
        };

        blockModel.setVisualisationState(true);
        assertTrue(blockModel.getVisualisationState());
    }
}