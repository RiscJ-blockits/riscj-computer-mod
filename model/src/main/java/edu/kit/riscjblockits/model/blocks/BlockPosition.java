package edu.kit.riscjblockits.model.blocks;

/**
 * Custom Class to save a position inside a minecraft world.
 */
public class BlockPosition {

    private double x;
    private double y;
    private double z;

    private boolean isBus;

    public BlockPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        isBus = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isBus() {
        return isBus;
    }

    public void setBus(boolean bus) {
        isBus = bus;
    }

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
