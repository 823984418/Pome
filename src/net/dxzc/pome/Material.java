package net.dxzc.pome;

public abstract class Material {

    public abstract float getLightLevel(PointData pointData);

    public abstract void getDiffuse(PointData pointData, Float3 input, Float3 output, Float3 power);

    public abstract void getLight(PointData pointData, Float3 out, Float3 light);

    public abstract float sampleDiffuseOutput(BaseRandom random,PointData pointData, Float3 input, Float3 output);

    public abstract float sampleDiffuseInput(BaseRandom random,PointData pointData, Float3 output, Float3 input);

    public abstract float sampleLightOutput(BaseRandom random,PointData pointData, Float3 output);

}
