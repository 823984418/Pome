package net.dxzc.pome;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public final class FrameBuffer {

    public FrameBuffer(int width, int height) {
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

    private static int value(float v) {
        return (int) (Math.max(0, Math.min(1, Math.pow(v, 0.6))) * 255);
    }

    private static int color(float r, float g, float b) {
        return (0xFF << 24) + (value(r) << 16) + (value(g) << 8) + value(b);
    }

    public BufferedImage toImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] buff = new int[width * height];
        for (int i = 0; i < buff.length; i++) {
            buff[i] = color(buffer[i * 3], buffer[i * 3 + 1], buffer[i * 3 + 2]);
        }
        image.setRGB(0, 0, width, height, buff, 0, width);
        return image;
    }

    @Deprecated
    public void saveAsBmp(OutputStream outputStream) throws IOException {
        int size = width * height * 3;
        int fileSize = 54 + size;
        byte[] head = new byte[]{
                'B', 'M',
                (byte) (fileSize), (byte) (fileSize >>> 8), (byte) (fileSize >>> 16), (byte) (fileSize >>> 24),
                0, 0, 0, 0,
                54, 0, 0, 0,
                40, 0, 0, 0,
                (byte) (width), (byte) (width >>> 8), (byte) (width >>> 16), (byte) (width >>> 24),
                (byte) (height), (byte) (height >>> 8), (byte) (height >>> 16), (byte) (height >>> 24),
                1, 0,
                24, 0,
                0, 0, 0, 0,
                (byte) (size), (byte) (size >>> 8), (byte) (size >>> 16), (byte) (size >>> 24),
                (byte) (2953), (byte) (2953 >>> 8), (byte) (2953 >>> 16), (byte) (2953 >>> 24),
                (byte) (2953), (byte) (2953 >>> 8), (byte) (2953 >>> 16), (byte) (2953 >>> 24),
                0, 0, 0, 0,
                0, 0, 0, 0,
        };
        outputStream.write(head);
        float[] buffer = this.buffer;
        for (int i = 0; i < size; ) {
            float r = buffer[i++];
            float g = buffer[i++];
            float b = buffer[i++];
            int gr = Math.max(0, Math.min((int) (r * 255), 255));
            int gg = Math.max(0, Math.min((int) (g * 255), 255));
            int gb = Math.max(0, Math.min((int) (b * 255), 255));
            outputStream.write(gr);
            outputStream.write(gg);
            outputStream.write(gb);
        }
    }

    @Deprecated
    public void saveAsHdr(OutputStream outputStream) throws IOException {
        outputStream.write(("FORMAT=32-bit_rle_rgbe\n\n" +
                "-Y " + height + " -X " + width + "\n").getBytes(StandardCharsets.US_ASCII));
        float[] buffer = this.buffer;
        int size = width * height * 3;
        for (int i = 0; i < size; ) {
            float r = buffer[i++];
            float g = buffer[i++];
            float b = buffer[i++];
            float v = Math.max(r, Math.max(g, b));
            if (v >= 1e-32f) {
                int e = Math.getExponent(v);
                double m = 256 / Math.pow(2, e);
                outputStream.write((int) (r * m));
                outputStream.write((int) (g * m));
                outputStream.write((int) (b * m));
                outputStream.write(e + 128);
            } else {
                outputStream.write(0);
                outputStream.write(0);
                outputStream.write(0);
                outputStream.write(0);
            }
        }
    }

}
