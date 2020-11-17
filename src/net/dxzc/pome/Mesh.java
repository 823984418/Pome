package net.dxzc.pome;

public abstract class Mesh {

    public abstract double getLightLevel();

    public abstract void getBounds(Double3 min, Double3 max);

    public abstract double sampleLight(BaseRandom random, PointBuffer pointBuffer);

    public abstract void intersect(Ray ray, PointBuffer pointBuffer);

    public abstract boolean occluded(Ray ray);

    public abstract void getDiffuse(PointData pointData, Double3 input, Double3 output, Double3 power);

    public abstract void getLight(PointData pointData, Double3 output, Double3 light);

    public abstract double sampleDiffuseOutput(BaseRandom random, PointData pointData, Double3 input, Double3 output);

    public abstract double sampleDiffuseInput(BaseRandom random, PointData pointData, Double3 output, Double3 input);

    public abstract double sampleLightOutput(BaseRandom random, PointData pointData, Double3 output);

    public abstract void initPointData(PointBuffer pointBuffer, PointData pointData);

}
