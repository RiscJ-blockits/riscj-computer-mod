package edu.kit.riscjblockits.model.blocks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockPositionTest {

    @Test
    void setX() {
        BlockPosition blockPosition = new BlockPosition();
        blockPosition.setX(5);
        assertEquals(5, blockPosition.getX());
    }

    @Test
    void setY() {
        BlockPosition blockPosition = new BlockPosition();
        blockPosition.setY(5);
        assertEquals(5, blockPosition.getY());
    }

    @Test
    void setZ() {
        BlockPosition blockPosition = new BlockPosition();
        blockPosition.setZ(5);
        assertEquals(5, blockPosition.getZ());
    }

}
