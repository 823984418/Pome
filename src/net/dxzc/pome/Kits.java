package net.dxzc.pome;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Kits {

    public static String readFile(String file) {
        try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
            byte[] data = new byte[input.available()];
            input.read(data);
            return new String(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
