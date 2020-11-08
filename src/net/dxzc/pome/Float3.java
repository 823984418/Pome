package net.dxzc.pome;

public final class Float3 {

    public float x;

    public float y;

    public float z;

    public void set(Float3 v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public void set(float vx, float vy, float vz) {
        x = vx;
        y = vy;
        z = vz;
    }

}
