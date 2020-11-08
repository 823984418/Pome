package net.dxzc.pome.materials;

import net.dxzc.pome.BaseRandom;
import net.dxzc.pome.Float3;
import net.dxzc.pome.Material;
import net.dxzc.pome.PointData;

public abstract class BaseMaterial extends Material {

    @Override
    public float getLightLevel(PointData pointData) {
        return 0;
    }

    @Override
    public void getLight(PointData pointData, Float3 out, Float3 light) {
        light.x = 0;
        light.y = 0;
        light.z = 0;
    }

    @Override
    public float sampleDiffuseOutput(BaseRandom random, PointData pointData, Float3 input, Float3 output) {
        random.nextTarget();
        output.set(random.float3);
        float normalX = pointData.normalX;
        float normalY = pointData.normalY;
        float normalZ = pointData.normalZ;
        float in = normalX * input.x + normalY * input.y + normalZ * input.z;
        float out = normalX * output.x + normalY * output.y + normalZ * output.z;
        if (in > 0 == out > 0) {
            output.x = -output.x;
            output.y = -output.y;
            output.z = -output.z;
        }
        return 0.5f / (float) Math.PI;
    }

    @Override
    public float sampleDiffuseInput(BaseRandom random, PointData pointData, Float3 output, Float3 input) {
        random.nextTarget();
        input.set(random.float3);
        float normalX = pointData.normalX;
        float normalY = pointData.normalY;
        float normalZ = pointData.normalZ;
        float in = normalX * input.x + normalY * input.y + normalZ * input.z;
        float out = normalX * output.x + normalY * output.y + normalZ * output.z;
        if (in > 0 == out > 0) {
            input.x = -input.x;
            input.y = -input.y;
            input.z = -input.z;
        }
        return 0.5f / (float) Math.PI;
    }

    @Override
    public float sampleLightOutput(BaseRandom random, PointData pointData, Float3 output) {
        random.nextTarget();
        output.set(random.float3);
        return 0.25f / (float) Math.PI;
    }

}
