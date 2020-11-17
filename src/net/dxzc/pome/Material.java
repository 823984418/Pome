package net.dxzc.pome;

public abstract class Material {

    public abstract double getLightLevel(PointData pointData);

    public abstract void getDiffuse(PointData pointData, Double3 input, Double3 output, Double3 power);

    public abstract void getLight(PointData pointData, Double3 out, Double3 light);

    public abstract double sampleDiffuseOutput(BaseRandom random, PointData pointData, Double3 input, Double3 output);

    public abstract double sampleDiffuseInput(BaseRandom random, PointData pointData, Double3 output, Double3 input);

    public abstract double sampleLightOutput(BaseRandom random,PointData pointData, Double3 output);

}
