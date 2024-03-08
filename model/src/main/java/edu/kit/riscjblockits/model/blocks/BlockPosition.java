package edu.kit.riscjblockits.model.blocks;

/**
 * Custom Class to save a position inside a minecraft world.
 * Can also save if at the position is a bus.
 */
public class BlockPosition {

    /**
     * X coordinate of the position.
     */
    private double x;
    /**
     * Y coordinate of the position.
     */
    private double y;
    /**
     * Z coordinate of the position.
     */
    private double z;

    /**
     * True if the block on the position is a bus.
     */
    private boolean isBus;

    /**
     * Default constructor. Sets all coordinates to 0.
     */
    public BlockPosition() {
        x = 0;
        y = 0;
        z = 0;
        isBus = false;
    }

    /**
     * Constructor with coordinates.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public BlockPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        isBus = false;
    }

    /**
     * Getter for the x coordinate.
     * @return x coordinate of the position
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for the y coordinate.
     * @return y coordinate of the position
     */
    public double getY() {
        return y;
    }

    /**
     * Getter for the z coordinate.
     * @return z coordinate of the position
     */
    public double getZ() {
        return z;
    }

    /**
     * Setter for the x coordinate.
     * @param x x coordinate of the position
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Setter for the y coordinate.
     * @param y y coordinate of the position
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Setter for the z coordinate.
     * @param z z coordinate of the position
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Getter for the isBus attribute.
     * @return true if the block on the position is a bus
     */
    public boolean isBus() {
        return isBus;
    }

    /**
     * Setter for the isBus attribute.
     * @param bus true if the block on the position is a bus
     */
    public void setBus(boolean bus) {
        isBus = bus;
    }

    /**
     * Compares two BlockPositions if they are equal.
     * @param o is the BlockPosition to compare
     * @return true if the BlockPositions are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockPosition that = (BlockPosition) o;

        if (Double.compare(x, that.x) != 0) return false;
        if (Double.compare(y, that.y) != 0) return false;
        if (Double.compare(z, that.z) != 0) return false;
        return isBus == that.isBus;
    }

    /**
     * Calculates the hashcode of the BlockPosition.
     * @return the hashcode of the BlockPosition
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (isBus ? 1 : 0);
        return result;
    }

    /**
     * Checks if to BlockPositions are neighbours.
     * @param blockPosition is the first node
     * @param blockPosition1 is the second node
     * @return true if the nodes are connected with an edge
     */
    public static boolean isNeighbourPosition(BlockPosition blockPosition, BlockPosition blockPosition1) {
        if (blockPosition.getX() - blockPosition1.getX() == 1 || blockPosition.getX() - blockPosition1.getX() == -1) {
            return blockPosition.getY() == blockPosition1.getY() && blockPosition.getZ() == blockPosition1.getZ();
        }
        if (blockPosition.getY() - blockPosition1.getY() == 1 || blockPosition.getY() - blockPosition1.getY() == -1) {
            return blockPosition.getX() == blockPosition1.getX() && blockPosition.getZ() == blockPosition1.getZ();
        }
        return (blockPosition.getZ() - blockPosition1.getZ() == 1 || blockPosition.getZ() - blockPosition1.getZ() == -1)
                && blockPosition.getX() == blockPosition1.getX() && blockPosition.getY() == blockPosition1.getY();
    }

}
