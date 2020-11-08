package net.dxzc.pome.meshs;

import net.dxzc.pome.*;

public abstract class BaseMesh extends Mesh {

    public BaseMesh(Material material) {
        this.material = material;
    }

    public final Material material;

    @Override
    public void getLight(PointData pointData, Float3 output, Float3 light) {
        material.getLight(pointData, output, light);
    }

    @Override
    public void getDiffuse(PointData pointData, Float3 input, Float3 output, Float3 power) {
        material.getDiffuse(pointData, input, output, power);
    }

    @Override
    public float sampleDiffuseOutput(BaseRandom random, PointData pointData, Float3 input, Float3 output) {
        return material.sampleDiffuseOutput(random, pointData, input, output);
    }

    @Override
    public float sampleDiffuseInput(BaseRandom random, PointData pointData, Float3 output, Float3 input) {
        return material.sampleDiffuseInput(random, pointData, output, input);
    }

    @Override
    public float sampleLightOutput(BaseRandom random, PointData pointData, Float3 output) {
        return material.sampleLightOutput(random, pointData, output);
    }

}
