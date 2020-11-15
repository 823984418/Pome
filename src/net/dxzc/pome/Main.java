package net.dxzc.pome;


import net.dxzc.pome.materials.DiffuseMaterial;
import net.dxzc.pome.materials.MirrorMaterial;
import net.dxzc.pome.meshs.Sphere;
import net.dxzc.pome.renderers.DebugRenderer;
import net.dxzc.pome.renderers.MultiThreadPathTracingRenderer;
import net.dxzc.pome.renderers.PathTracingRenderer;
import net.dxzc.pome.scenes.BaseScene;
import net.dxzc.pome.scenes.BvhScene;
import net.dxzc.pome.scenes.LoseScene;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void load(List<Mesh> meshes, String path, float r, float g, float b, float lr, float lg, float lb) {
        DiffuseMaterial material = new DiffuseMaterial();
        material.color.set(r, g, b);
        material.light.set(lr, lg, lb);
        load(meshes, path, material);
    }

    public static void load(List<Mesh> meshes, String path, Material material) {
        String model = Kits.readFile(path);
        Kits.loadTriangle(meshes, new Scanner(model), material);
    }

    public static void main(String[] args) throws Exception {
        int width = 512;
        int height = 512;
        FrameBuffer frame = new FrameBuffer(width, height);
        List<Mesh> list = new LinkedList<>();
        MirrorMaterial mirror = new MirrorMaterial();
        mirror.color.set(0.93f, 0.81f, 0.17f);
        load(list, "cornellbox\\light.obj", 0.725f, 0.71f, 0.68f, 47.8348f, 38.5664f, 31.0808f);
        load(list, "cornellbox\\left.obj", 0.63f, 0.065f, 0.05f, 0, 0, 0);
        //load(list, "cornellbox\\left.obj", mirror);
        load(list, "cornellbox\\right.obj", 0.14f, 0.45f, 0.091f, 0, 0, 0);
        load(list, "cornellbox\\floor.obj", 0.725f, 0.71f, 0.68f, 0, 0, 0);
        //load(list, "cornellbox\\shortbox.obj", 0.725f, 0.71f, 0.68f, 0, 0, 0);
        DiffuseMaterial material = new DiffuseMaterial();
        material.color.set(0.725f, 0.71f, 0.68f);
        Sphere sphere = new Sphere(mirror);
        sphere.center.x = 185;
        sphere.center.y = 80;
        sphere.center.z = 169;
        sphere.radius = 80;
        sphere.init();
        list.add(sphere);
        load(list, "cornellbox\\tallbox.obj", 0.725f, 0.71f, 0.68f, 0, 0, 0);
        int size = list.size();
        BaseScene scene = new BvhScene(list.toArray(new Mesh[size]), 0, size);
        //scene.backgroundR = 0.3f;
        //scene.backgroundG = 0.3f;
        //scene.backgroundB = 0.3f;
        scene.camera.set(278, 273, -800);
        scene.forward.set(0, 0, 1);
        float w = 0.35f;
        scene.rightward.set(-w, 0, 0);
        scene.upward.set(0, w * width / height, 0);
        Renderer renderer;
        if (PathTracingRenderer.DEBUG) {
            renderer = new PathTracingRenderer();
        } else {
            renderer = new MultiThreadPathTracingRenderer();
        }
        long begin = System.nanoTime();
        renderer.render(scene, frame);
        long end = System.nanoTime();
        long d = end - begin;
        long us = d / 1000;
        long ms = us / 1000;
        long s = ms / 1000;
        long min = s / 60;
        long h = min / 60;
        long day = h / 24;
        System.out.println(day + "-" + h % 24 + ":" + min % 60 + ":" + s % 60 + "-" + ms % 1000);
        ImageIO.write(frame.toImage(), "png", new File("D:\\code\\Pome\\1.png"));
    }

}
