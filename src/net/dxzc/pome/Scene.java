package net.dxzc.pome;

public abstract class Scene {

    public abstract void getCamera(Float3 camera);

    public abstract void getForward(Float3 forward);

    public abstract void getUpward(Float3 upward);

    public abstract void getRightward(Float3 rightward);

    public abstract void getBackground(Float3 background);

    public abstract float sampleLight(BaseRandom random, PointBuffer pointBuffer);

    public abstract void intersect(Ray ray, PointBuffer pointBuffer);

    public abstract boolean occluded(Ray ray);

}
