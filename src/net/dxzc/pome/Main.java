package net.dxzc.pome;


import net.dxzc.pome.materials.DiffuseMaterial;
import net.dxzc.pome.materials.MirrorMaterial;
import net.dxzc.pome.renderers.MultiThreadPathTracingRenderer;
import net.dxzc.pome.renderers.PathTracingRenderer;
import net.dxzc.pome.scenes.BaseScene;
import net.dxzc.pome.scenes.BvhScene;
import net.dxzc.pome.scenes.CountScene;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args) throws Exception {
        SceneBuilder builder = new SceneBuilder();
        DiffuseMaterial light = new DiffuseMaterial();
        light.light.set(47.8348, 38.5664, 31.0808);
        DiffuseMaterial base = new DiffuseMaterial();
        base.color.set(0.725, 0.71, 0.68);
        DiffuseMaterial red = new DiffuseMaterial();
        red.color.set(0.63, 0.065, 0.05);
        DiffuseMaterial blue = new DiffuseMaterial();
        blue.color.set(0.14, 0.45, 0.091);
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
        Image frame = new Image(width, height);
        BaseScene scene = new BvhScene(builder.getAndClear());
        scene.camera.set(278, 273, -800);
        scene.forward.set(0, 0, 1);
        double w = 0.35;
        scene.rightward.set(-w, 0, 0);
        scene.upward.set(0, w * width / height, 0);

        CountScene countScene = new CountScene(scene);
        Scene useScene = scene;
        if (PathTracingRenderer.DEBUG) {
            useScene = countScene;
        }

        Renderer renderer;
        if (PathTracingRenderer.DEBUG) {
            renderer = new PathTracingRenderer();
        } else {
            renderer = new MultiThreadPathTracingRenderer();
        }
        //renderer = new DebugRenderer();

        long begin = System.nanoTime();
        renderer.render(useScene, frame);
        long end = System.nanoTime();
        long d = end - begin;
        long us = d / 1000;
        long ms = us / 1000;
        long s = ms / 1000;
        long min = s / 60;
        long h = min / 60;
        long day = h / 24;
        System.out.println(day + "-" + h % 24 + ":" + min % 60 + ":" + s % 60 + "-" + ms % 1000);
        if (PathTracingRenderer.DEBUG) {
            System.out.println(countScene.sampleLightCount.get());
            System.out.println(countScene.intersectCount.get());
            System.out.println(countScene.occludedCount.get());
        }
        ImageIO.write(ImageHelper.toJavaImage(frame), "png", new File("D:\\code\\Pome\\1.png"));
        ImageHelper.writeAsHdr(frame, new FileOutputStream("1.hdr"));
    }

}
