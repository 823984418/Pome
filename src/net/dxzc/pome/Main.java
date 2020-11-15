package net.dxzc.pome;


import net.dxzc.pome.materials.DiffuseMaterial;
import net.dxzc.pome.materials.MirrorMaterial;
import net.dxzc.pome.meshs.Sphere;
import net.dxzc.pome.renderers.DebugRenderer;
import net.dxzc.pome.renderers.MultiThreadPathTracingRenderer;
import net.dxzc.pome.renderers.PathTracingRenderer;
import net.dxzc.pome.scenes.BaseScene;
import net.dxzc.pome.scenes.BvhScene;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        SceneBuilder builder = new SceneBuilder();
        DiffuseMaterial light = new DiffuseMaterial();
        light.light.set(47.8348f, 38.5664f, 31.0808f);
        DiffuseMaterial base = new DiffuseMaterial();
        base.color.set(0.725f, 0.71f, 0.68f);
        DiffuseMaterial red = new DiffuseMaterial();
        red.color.set(0.63f, 0.065f, 0.05f);
        DiffuseMaterial blue = new DiffuseMaterial();
        blue.color.set(0.14f, 0.45f, 0.091f);
        MirrorMaterial mirror = new MirrorMaterial();
        mirror.color.set(1, 1, 1);
        builder.material(light).loadFile("cornellbox\\light.obj").pollMaterial();
        builder.material(base).loadFile("cornellbox\\floor.obj").pollMaterial();
        builder.material(red).loadFile("cornellbox\\left.obj").pollMaterial();
        builder.material(blue).loadFile("cornellbox\\right.obj").pollMaterial();
        //builder.material(base).loadFile("cornellbox\\tallbox.obj").pollMaterial();

        builder.material(mirror).sphere(400, 100, 350, 100).pollMaterial();

        builder.transfor(
                Transfor.lookAt(
                        0, 0, 0,
                        0, 0, 1,
                        0, 1, 0).append(
                        Transfor.scale(1600, 1600, 1600)).append(
                        Transfor.translate(150, -56, 300))
        );

        builder.material(base).loadFile("bunny\\bunny.obj").pollMaterial();

        builder.pollTransfor();

        int width = 512;
        int height = 512;
        FrameBuffer frame = new FrameBuffer(width, height);
        BaseScene scene = new BvhScene(builder.getAndClear());
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
        //renderer = new DebugRenderer();

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

    public static void main0(String[] args) throws Exception {
        int width = 512;
        int height = 512;
        FrameBuffer frame = new FrameBuffer(width, height);
        List<Mesh> list = new LinkedList<>();
        MirrorMaterial mirror = new MirrorMaterial();
        mirror.color.set(0.93f, 0.81f, 0.17f);
        Kits.load(list, "cornellbox\\light.obj", 0.725f, 0.71f, 0.68f, 47.8348f, 38.5664f, 31.0808f);
        Kits.load(list, "cornellbox\\left.obj", 0.63f, 0.065f, 0.05f, 0, 0, 0);
        //load(list, "cornellbox\\left.obj", mirror);
        Kits.load(list, "cornellbox\\right.obj", 0.14f, 0.45f, 0.091f, 0, 0, 0);
        Kits.load(list, "cornellbox\\floor.obj", 0.725f, 0.71f, 0.68f, 0, 0, 0);
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
        Kits.load(list, "cornellbox\\tallbox.obj", 0.725f, 0.71f, 0.68f, 0, 0, 0);
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
