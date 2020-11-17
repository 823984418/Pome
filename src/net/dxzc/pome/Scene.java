package net.dxzc.pome;

public abstract class Scene {

    public abstract void getCamera(Double3 camera);

    public abstract void getForward(Double3 forward);

    public abstract void getUpward(Double3 upward);

    public abstract void getRightward(Double3 rightward);

    public abstract void getBackground(Double3 background);

    public abstract double sampleLight(BaseRandom random, PointBuffer pointBuffer);

    public abstract void intersect(Ray ray, PointBuffer pointBuffer);

    public abstract boolean occluded(Ray ray);

}
