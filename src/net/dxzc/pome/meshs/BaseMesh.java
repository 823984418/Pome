package net.dxzc.pome.meshs;

import net.dxzc.pome.*;

public abstract class BaseMesh extends Mesh {

    public BaseMesh(Material material) {
        this.material = material;
    }

    public final Material material;

    @Override
    public void getLight(PointData pointData, Double3 output, Double3 light) {
        material.getLight(pointData, output, light);
    }

    @Override
    public void getDiffuse(PointData pointData, Double3 input, Double3 output, Double3 power) {
        material.getDiffuse(pointData, input, output, power);
    }

    @Override
    public double sampleDiffuseOutput(BaseRandom random, PointData pointData, Double3 input, Double3 output) {
        return material.sampleDiffuseOutput(random, pointData, input, output);
    }

    @Override
    public double sampleDiffuseInput(BaseRandom random, PointData pointData, Double3 output, Double3 input) {
        return material.sampleDiffuseInput(random, pointData, output, input);
    }

    @Override
    public double sampleLightOutput(BaseRandom random, PointData pointData, Double3 output) {
        return material.sampleLightOutput(random, pointData, output);
    }

}
