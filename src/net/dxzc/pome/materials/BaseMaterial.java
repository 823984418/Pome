package net.dxzc.pome.materials;

import net.dxzc.pome.BaseRandom;
import net.dxzc.pome.Double3;
import net.dxzc.pome.Material;
import net.dxzc.pome.PointData;

public abstract class BaseMaterial extends Material {

    @Override
    public double getLightLevel(PointData pointData) {
        return 0;
    }

    @Override
    public void getLight(PointData pointData, Double3 out, Double3 light) {
        light.x = 0;
        light.y = 0;
        light.z = 0;
    }

    @Override
    public double sampleDiffuseOutput(BaseRandom random, PointData pointData, Double3 input, Double3 output) {
        double normalX = pointData.normalX;
        double normalY = pointData.normalY;
        double normalZ = pointData.normalZ;
        double in = normalX * input.x + normalY * input.y + normalZ * input.z;
        if (in > 0) {
            normalX = -normalX;
            normalY = -normalY;
            normalZ = -normalZ;
        }
        double pdf = random.nextCosineHalfTarget(normalX, normalY, normalZ);
        output.set(random.double3);
        return pdf;
    }

    @Override
    public double sampleDiffuseInput(BaseRandom random, PointData pointData, Double3 output, Double3 input) {
        double normalX = pointData.normalX;
        double normalY = pointData.normalY;
        double normalZ = pointData.normalZ;
        double out = normalX * output.x + normalY * output.y + normalZ * output.z;
        if (out > 0) {
            normalX = -normalX;
            normalY = -normalY;
            normalZ = -normalZ;
        }
        double pdf = random.nextCosineHalfTarget(normalX, normalY, normalZ);
        input.set(random.double3);
        return pdf;
    }

    @Override
    public double sampleLightOutput(BaseRandom random, PointData pointData, Double3 output) {
        random.nextTarget();
        output.set(random.double3);
        return 0.25 / Math.PI;
    }

}
