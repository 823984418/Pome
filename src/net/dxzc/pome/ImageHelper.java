package net.dxzc.pome;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ImageHelper {

    public static BufferedImage toJavaImage(Image image) {
        int width = image.width;
        int height = image.height;
        double[] buffer = image.buffer;
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] buff = new int[width * height];
        for (int i = 0; i < buff.length; i++) {
            buff[i] = (0xFF << 24)
                    + ((Math.max(0, Math.min(255, (int) (Math.pow(buffer[i * 3], 0.6) * 256)))) << 16)
                    + ((Math.max(0, Math.min(255, (int) (Math.pow(buffer[i * 3 + 1], 0.6) * 256)))) << 8)
                    + (Math.max(0, Math.min(255, (int) (Math.pow(buffer[i * 3 + 2], 0.6) * 256))));
        }
        out.setRGB(0, 0, width, height, buff, 0, width);
        return out;
    }


    public static void writeAsBmp(Image image, OutputStream outputStream) throws IOException {
        int width = image.width;
        int height = image.height;
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
        double[] buffer = image.buffer;
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                int i = (x + y * width) * 3;
                double r = buffer[i];
                double g = buffer[i + 1];
                double b = buffer[i + 2];
                int gr = Math.max(0, Math.min((int) (Math.pow(r, 0.6) * 256), 255));
                int gg = Math.max(0, Math.min((int) (Math.pow(g, 0.6) * 256), 255));
                int gb = Math.max(0, Math.min((int) (Math.pow(b, 0.6) * 256), 255));
                outputStream.write(gb);
                outputStream.write(gg);
                outputStream.write(gr);
            }
        }
    }

    public static void writeAsHdr(Image image, OutputStream outputStream) throws IOException {
        int width = image.width;
        int height = image.height;
        outputStream.write(("FORMAT=32-bit_rle_rgbe\n\n" +
                "-Y " + height + " -X " + width + "\n").getBytes(StandardCharsets.US_ASCII));
        double[] buffer = image.buffer;
        int size = width * height * 3;
        for (int i = 0; i < size; ) {
            double r = buffer[i++];
            double g = buffer[i++];
            double b = buffer[i++];
            double v = Math.max(r, Math.max(g, b));
            if (v >= 1e-32) {
                int e = Math.getExponent(v) + 1;
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
