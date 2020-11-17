package net.dxzc.pome;

public final class PointData {

    public double x;

    public double y;

    public double z;

    public double normalX;

    public double normalY;

    public double normalZ;

    public double tangentX;

    public double tangentY;

    public double tangentZ;

    public double buffer1;

    public double buffer2;

    public double buffer3;

    public double buffer4;

    public double buffer5;

    public double buffer6;

    public double buffer7;

    public double buffer8;

    public double buffer9;

    public void set(PointData a) {
        x = a.x;
        y = a.y;
        z = a.z;
        normalX = a.normalX;
        normalY = a.normalY;
        normalZ = a.normalZ;
        tangentX = a.tangentX;
        tangentY = a.tangentY;
        tangentZ = a.tangentZ;
        buffer1 = a.buffer1;
        buffer2 = a.buffer2;
        buffer3 = a.buffer4;
        buffer4 = a.buffer4;
        buffer5 = a.buffer5;
        buffer6 = a.buffer6;
        buffer7 = a.buffer7;
        buffer8 = a.buffer8;
        buffer9 = a.buffer9;
    }

    public void set(PointData a, double ap) {
        buffer1 = a.buffer1 * ap;
        buffer2 = a.buffer2 * ap;
        buffer3 = a.buffer4 * ap;
        buffer4 = a.buffer4 * ap;
        buffer5 = a.buffer5 * ap;
        buffer6 = a.buffer6 * ap;
        buffer7 = a.buffer7 * ap;
        buffer8 = a.buffer8 * ap;
        buffer9 = a.buffer9 * ap;
    }

    public void set(PointData a, PointData b, double ap, double bp) {
        buffer1 = a.buffer1 * ap + b.buffer1 * bp;
        buffer2 = a.buffer2 * ap + b.buffer2 * bp;
        buffer3 = a.buffer4 * ap + b.buffer3 * bp;
        buffer4 = a.buffer4 * ap + b.buffer4 * bp;
        buffer5 = a.buffer5 * ap + b.buffer5 * bp;
        buffer6 = a.buffer6 * ap + b.buffer6 * bp;
        buffer7 = a.buffer7 * ap + b.buffer7 * bp;
        buffer8 = a.buffer8 * ap + b.buffer8 * bp;
        buffer9 = a.buffer9 * ap + b.buffer9 * bp;
    }

    public void set(PointData a, PointData b, PointData c, double ap, double bp, double cp) {
        buffer1 = a.buffer1 * ap + b.buffer1 * bp + c.buffer1 * cp;
        buffer2 = a.buffer2 * ap + b.buffer2 * bp + c.buffer2 * cp;
        buffer3 = a.buffer4 * ap + b.buffer3 * bp + c.buffer3 * cp;
        buffer4 = a.buffer4 * ap + b.buffer4 * bp + c.buffer4 * cp;
        buffer5 = a.buffer5 * ap + b.buffer5 * bp + c.buffer5 * cp;
        buffer6 = a.buffer6 * ap + b.buffer6 * bp + c.buffer6 * cp;
        buffer7 = a.buffer7 * ap + b.buffer7 * bp + c.buffer7 * cp;
        buffer8 = a.buffer8 * ap + b.buffer8 * bp + c.buffer8 * cp;
        buffer9 = a.buffer9 * ap + b.buffer9 * bp + c.buffer9 * cp;
    }

}
