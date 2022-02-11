package net.dxzc.pome;

import net.dxzc.pome.meshs.Sphere;
import net.dxzc.pome.meshs.Triangle;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class SceneBuilder {

    public SceneBuilder() {
        transfors.push(Transfor.ONE);
    }

    private final LinkedList<Mesh> meshes = new LinkedList<>();

    private final LinkedList<Transfor> transfors = new LinkedList<>();

    private final LinkedList<Material> materials = new LinkedList<>();

    public SceneBuilder transfor(Transfor transfor) {
        transfors.push(transfors.peek().append(transfor));
        return this;
    }

    public SceneBuilder pollTransfor() {
        transfors.poll();
        return this;
    }

    public SceneBuilder material(Material material) {
        materials.push(material);
        return this;
    }

    public SceneBuilder pollMaterial() {
        materials.poll();
        return this;
    }

    public SceneBuilder clear() {
        meshes.clear();
        transfors.clear();
        transfors.push(Transfor.ONE);
        materials.clear();
        return this;
    }

    public Mesh[] get() {
        return meshes.toArray(new Mesh[meshes.size()]);
    }

    public Mesh[] getAndClear() {
        Mesh[] result = get();
        clear();
        return result;
    }

    public SceneBuilder triangle(
            double ax, double ay, double az,
            double bx, double by, double bz,
            double cx, double cy, double cz) {
        Transfor t = transfors.peek();
        Triangle triangle = new Triangle(materials.peek());
        triangle.aData.x = t.getX(ax, ay, az);
        triangle.aData.y = t.getY(ax, ay, az);
        triangle.aData.z = t.getZ(ax, ay, az);
        triangle.bData.x = t.getX(bx, by, bz);
        triangle.bData.y = t.getY(bx, by, bz);
        triangle.bData.z = t.getZ(bx, by, bz);
        triangle.cData.x = t.getX(cx, cy, cz);
        triangle.cData.y = t.getY(cx, cy, cz);
        triangle.cData.z = t.getZ(cx, cy, cz);
        triangle.init();
        meshes.push(triangle);
        return this;
    }

    public SceneBuilder triangle(Double3 a, Double3 b, Double3 c) {
        triangle(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z);
        return this;
    }

    public SceneBuilder sphere(double cx, double cy, double cz, double r) {
        Transfor t = transfors.peek();
        Sphere sphere = new Sphere(materials.peek());
        sphere.center.x = t.getX(cx, cy, cz);
        sphere.center.y = t.getY(cx, cy, cz);
        sphere.center.z = t.getZ(cx, cy, cz);
        sphere.radius = r;
        sphere.init();
        meshes.push(sphere);
        return this;
    }

    public SceneBuilder load(Scanner input) {
        ArrayList<Double3> positions = new ArrayList<>();
        positions.add(null);
        while (input.hasNext()) {
            String cmd = input.next();
            switch (cmd) {
                case "#":
                    input.nextLine();
                    break;
                case "v":
                    Double3 pos = new Double3();
                    pos.x = input.nextDouble();
                    pos.y = input.nextDouble();
                    pos.z = input.nextDouble();
                    positions.add(pos);
                    break;
                case "f":
                    Double3 a = positions.get(input.nextInt());
                    Double3 b = positions.get(input.nextInt());
                    Double3 c = positions.get(input.nextInt());
                    triangle(a, b, c);
                    break;
                default:
                    throw new RuntimeException(cmd);
            }
        }
        return this;
    }

    public SceneBuilder load(String code) {
        load(new Scanner(code));
        return this;
    }

    public SceneBuilder loadFile(String path) {
        try (InputStream input = new BufferedInputStream(new FileInputStream(path))) {
            byte[] data = new byte[input.available()];
            input.read(data);
            load(new String(data));
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
