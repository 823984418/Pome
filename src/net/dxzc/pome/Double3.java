package net.dxzc.pome;

public final class Double3 {

    public double x;

    public double y;

    public double z;

    public void set(Double3 v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public void set(double vx, double vy, double vz) {
        x = vx;
        y = vy;
        z = vz;
    }

}
