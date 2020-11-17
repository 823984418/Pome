package net.dxzc.pome;

public final class Transfor {

    private static final double EPSILON = 0.000001;

    public static Transfor lookAt(
            double eyeX, double eyeY, double eyeZ,
            double centerX, double centerY, double centerZ,
            double upX, double upY, double upZ) {

        double z0 = eyeX - centerX;
        double z1 = eyeY - centerY;
        double z2 = eyeZ - centerZ;
        double len = 1 /  Math.sqrt(z0 * z0 + z1 * z1 + z2 * z2);
        z0 *= len;
        z1 *= len;
        z2 *= len;
        double x0 = upY * z2 - upZ * z1;
        double x1 = upZ * z0 - upX * z2;
        double x2 = upX * z1 - upY * z0;
        len =  Math.sqrt(x0 * x0 + x1 * x1 + x2 * x2);

        if (len == 0) {
            x0 = 0;
            x1 = 0;
            x2 = 0;
        } else {
            len = 1 / len;
            x0 *= len;
            x1 *= len;
            x2 *= len;
        }

        double y0 = z1 * x2 - z2 * x1;
        double y1 = z2 * x0 - z0 * x2;
        double y2 = z0 * x1 - z1 * x0;
        len =  Math.sqrt(y0 * y0 + y1 * y1 + y2 * y2);

        if (len == 0) {
            y0 = 0;
            y1 = 0;
            y2 = 0;
        } else {
            len = 1 / len;
            y0 *= len;
            y1 *= len;
            y2 *= len;
        }

        return new Transfor(
                x0, x1, x2, -(x0 * eyeX + x1 * eyeY + x2 * eyeZ),
                y0, y1, y2, -(y0 * eyeX + y1 * eyeY + y2 * eyeZ),
                z0, z1, z2, -(z0 * eyeX + z1 * eyeY + z2 * eyeZ)
        );
    }

    public static Transfor translate(double dX, double dY, double dZ) {
        return new Transfor(
                1, 0, 0, dX,
                0, 1, 0, dY,
                0, 0, 1, dZ
        );
    }

    public static Transfor scale(double dX, double dY, double dZ) {
        return new Transfor(
                dX, 0, 0, 0,
                0, dY, 0, 0,
                0, 0, dZ, 0
        );
    }

    public static final Transfor ONE = new Transfor(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0
    );

    public Transfor(
            double xx, double xy, double xz, double xa,
            double yx, double yy, double yz, double ya,
            double zx, double zy, double zz, double za) {
        this.xx = xx;
        this.xy = xy;
        this.xz = xz;
        this.xa = xa;
        this.yx = yx;
        this.yy = yy;
        this.yz = yz;
        this.ya = ya;
        this.zx = zx;
        this.zy = zy;
        this.zz = zz;
        this.za = za;
    }

    private final double xx, xy, xz, xa;
    private final double yx, yy, yz, ya;
    private final double zx, zy, zz, za;

    public Transfor append(Transfor a) {
        return new Transfor(
                a.xx * xx + a.xy * yx + a.xz * zx,
                a.xx * xy + a.xy * yy + a.xz * zy,
                a.xx * xz + a.xy * yz + a.xz * zz,
                a.xx * xa + a.xy * ya + a.xz * za + a.xa,

                a.yx * xx + a.yy * yx + a.yz * zx,
                a.yx * xy + a.yy * yy + a.yz * zy,
                a.yx * xz + a.yy * yz + a.yz * zz,
                a.yx * xa + a.yy * ya + a.yz * za + a.ya,

                a.zx * xx + a.zy * yx + a.zz * zx,
                a.zx * xy + a.zy * yy + a.zz * zy,
                a.zx * xz + a.zy * yz + a.zz * zz,
                a.zx * xa + a.zy * ya + a.zz * za + a.za
        );
    }

    public double getX(Double3 o) {
        return xx * o.x + xy * o.y + xz * o.z + xa;
    }

    public double getY(Double3 o) {
        return yx * o.x + yy * o.y + yz * o.z + ya;
    }

    public double getZ(Double3 o) {
        return zx * o.x + zy * o.y + zz * o.z + za;
    }

    public double getX(double ox, double oy, double oz) {
        return xx * ox + xy * oy + xz * oz + xa;
    }

    public double getY(double ox, double oy, double oz) {
        return yx * ox + yy * oy + yz * oz + ya;
    }

    public double getZ(double ox, double oy, double oz) {
        return zx * ox + zy * oy + zz * oz + za;
    }

    public void get(Double3 o, Double3 v) {
        double ox = o.x, oy = o.y, oz = o.z;
        v.set(
                xx * ox + xy * oy + xz * oz + xa,
                yx * ox + yy * oy + yz * oz + ya,
                zx * ox + zy * oy + zz * oz + za
        );
    }

    public void get(double ox, double oy, double oz, Double3 v) {
        v.set(
                xx * ox + xy * oy + xz * oz + xa,
                yx * ox + yy * oy + yz * oz + ya,
                zx * ox + zy * oy + zz * oz + za
        );
    }

    @Override
    public String toString() {
        return String.format("" +
                        "[%6.2f,%6.2f,%6.2f,%6.2f]\n" +
                        "|%6.2f,%6.2f,%6.2f,%6.2f|\n" +
                        "|%6.2f,%6.2f,%6.2f,%6.2f|",
                xx, xy, xz, xa, yx, yy, yz, ya, zx, zy, zz, za);
    }
}
