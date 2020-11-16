package net.dxzc.pome;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public final class Image {

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        buffer = new float[width * height * 3];
    }

    public final int width;

    public final int height;

    public final float[] buffer;

    public void set(int x, int y, float r, float g, float b) {
        int off = 3 * (x + y * width);
        buffer[off] = r;
        buffer[off + 1] = g;
        buffer[off + 2] = b;
    }

}
