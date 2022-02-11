package net.dxzc.pome;

public final class Image {

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        buffer = new double[width * height * 3];
    }

    public final int width;

    public final int height;

    public final double[] buffer;

    public void set(int x, int y, double r, double g, double b) {
        int off = 3 * (x + y * width);
        buffer[off] = r;
        buffer[off + 1] = g;
        buffer[off + 2] = b;
    }

}
