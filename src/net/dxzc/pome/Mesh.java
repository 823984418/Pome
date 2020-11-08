package net.dxzc.pome;

public abstract class Mesh {

    public abstract float getLightLevel();

    public abstract void getBounds(Float3 min, Float3 max);

    public abstract float sampleLight(BaseRandom random, PointBuffer pointBuffer);

    public abstract void intersect(Ray ray, PointBuffer pointBuffer);

    public abstract boolean occluded(Ray ray);

    public abstract void getDiffuse(PointData pointData, Float3 input, Float3 output, Float3 power);

    public abstract void getLight(PointData pointData, Float3 output, Float3 light);

    public abstract float sampleDiffuseOutput(BaseRandom random, PointData pointData, Float3 input, Float3 output);

    public abstract float sampleDiffuseInput(BaseRandom random, PointData pointData, Float3 output, Float3 input);

    public abstract float sampleLightOutput(BaseRandom random, PointData pointData, Float3 output);

    public abstract void initPointData(PointBuffer pointBuffer, PointData pointData);

}
