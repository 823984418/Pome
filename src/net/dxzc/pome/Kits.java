package net.dxzc.pome;

import net.dxzc.pome.materials.DiffuseMaterial;
import net.dxzc.pome.meshs.Triangle;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public static void loadTriangle(List<Mesh> model, Scanner input, Material material) {
        ArrayList<Float3> positions = new ArrayList<>();
        positions.add(null);
        while (input.hasNext()) {
            switch (input.next()) {
                case "v":
                    Float3 pos = new Float3();
                    pos.x = input.nextFloat();
                    pos.y = input.nextFloat();
                    pos.z = input.nextFloat();
                    positions.add(pos);
                    break;
                case "f":
                    Triangle triangle = new Triangle(material);
                    Float3 a = positions.get(input.nextInt());
                    Float3 b = positions.get(input.nextInt());
                    Float3 c = positions.get(input.nextInt());
                    triangle.aData.x = a.x;
                    triangle.aData.y = a.y;
                    triangle.aData.z = a.z;
                    triangle.bData.x = b.x;
                    triangle.bData.y = b.y;
                    triangle.bData.z = b.z;
                    triangle.cData.x = c.x;
                    triangle.cData.y = c.y;
                    triangle.cData.z = c.z;
                    triangle.init();
                    model.add(triangle);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    public static void load(List<Mesh> meshes, String path, float r, float g, float b, float lr, float lg, float lb) {
        DiffuseMaterial material = new DiffuseMaterial();
        material.color.set(r, g, b);
        material.light.set(lr, lg, lb);
        load(meshes, path, material);
    }

    public static void load(List<Mesh> meshes, String path, Material material) {
        String model = readFile(path);
        loadTriangle(meshes, new Scanner(model), material);
    }

}
