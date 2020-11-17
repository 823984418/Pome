package net.dxzc.pome.scenes;

import net.dxzc.pome.*;

import java.util.concurrent.atomic.AtomicInteger;

public class CountScene extends Scene {

    public CountScene(Scene scene) {
        this.scene = scene;
    }

    private final Scene scene;

    public final AtomicInteger sampleLightCount = new AtomicInteger();

    public final AtomicInteger intersectCount = new AtomicInteger();

    public final AtomicInteger occludedCount = new AtomicInteger();


    @Override
    public void getCamera(Float3 camera) {
        scene.getCamera(camera);
    }

    @Override
    public void getForward(Float3 forward) {
        scene.getForward(forward);
    }

    @Override
    public void getUpward(Float3 upward) {
        scene.getUpward(upward);
    }

    @Override
    public void getRightward(Float3 rightward) {
        scene.getRightward(rightward);
    }

    @Override
    public void getBackground(Float3 background) {
        scene.getBackground(background);
    }

    @Override
    public float sampleLight(BaseRandom random, PointBuffer pointBuffer) {
        sampleLightCount.incrementAndGet();
        return scene.sampleLight(random, pointBuffer);
    }

    @Override
    public void intersect(Ray ray, PointBuffer pointBuffer) {
        intersectCount.incrementAndGet();
        scene.intersect(ray, pointBuffer);
    }

    @Override
    public boolean occluded(Ray ray) {
        occludedCount.incrementAndGet();
        return scene.occluded(ray);
    }

}
