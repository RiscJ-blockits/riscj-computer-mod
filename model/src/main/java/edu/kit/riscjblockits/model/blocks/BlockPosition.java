package edu.kit.riscjblockits.model.blocks;

/**
 * Custom Class to save a position inside a minecraft world.
 */
public class BlockPosition {

    /**
     * x coordinate of the position
     */
    private double x;
    /**
     * y coordinate of the position
     */
    private double y;
    /**
     * z coordinate of the position
     */
    private double z;

    /**
     * true if the block on the position is a bus
     */
    private boolean isBus;

    /**
     * creates a new BlockPosition
     * @param x x coordinate of the position
     * @param y y coordinate of the position
     * @param z z coordinate of the position
     */
    public BlockPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        isBus = false;
    }

    /**
     * Getter for the x coordinate
     * @return x coordinate of the position
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for the y coordinate
     * @return y coordinate of the position
     */
    public double getY() {
        return y;
    }

    /**
     * Getter for the z coordinate
     * @return z coordinate of the position
     */
    public double getZ() {
        return z;
    }

    /**
     * Setter for the x coordinate
     * @param x x coordinate of the position
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Setter for the y coordinate
     * @param y y coordinate of the position
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Setter for the z coordinate
     * @param z z coordinate of the position
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Getter for the isBus attribute
     * @return true if the block on the position is a bus
     */
    public boolean isBus() {
        return isBus;
    }

    /**
     * Setter for the isBus attribute
     * @param bus true if the block on the position is a bus
     */
    public void setBus(boolean bus) {
        isBus = bus;
    }

    /**
     * compares two BlockPositions if they are equal
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
     * calculates the hashcode of the BlockPosition
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

}
