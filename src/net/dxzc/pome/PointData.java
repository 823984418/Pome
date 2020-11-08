package net.dxzc.pome;

public final class PointData {

    public float x;

    public float y;

    public float z;

    public float normalX;

    public float normalY;

    public float normalZ;

    public float tangentX;

    public float tangentY;

    public float tangentZ;

    public float buffer1;

    public float buffer2;

    public float buffer3;

    public float buffer4;

    public float buffer5;

    public float buffer6;

    public float buffer7;

    public float buffer8;

    public float buffer9;

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

    public void set(PointData a, float ap) {
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

    public void set(PointData a, PointData b, float ap, float bp) {
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

    public void set(PointData a, PointData b, PointData c, float ap, float bp, float cp) {
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
