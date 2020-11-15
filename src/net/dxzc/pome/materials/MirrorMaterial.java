package net.dxzc.pome.materials;

import net.dxzc.pome.BaseRandom;
import net.dxzc.pome.Float3;
import net.dxzc.pome.PointData;

public class MirrorMaterial extends BaseMaterial {

    public final Float3 color = new Float3();

    public float delta = 0.0001f;

    @Override
    public float sampleDiffuseInput(BaseRandom random, PointData pointData, Float3 output, Float3 input) {
        float normalX = pointData.normalX;
        float normalY = pointData.normalY;
        float normalZ = pointData.normalZ;
        float invNormal = 1 / (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        normalX *= invNormal;
        normalY *= invNormal;
        normalZ *= invNormal;
        float outputX = output.x;
        float outputY = output.y;
        float outputZ = output.z;
        float invOutput = 1 / (float) Math.sqrt(outputX * outputX + outputY * outputY + outputZ * outputZ);
        outputX *= invOutput;
        outputY *= invOutput;
        outputZ *= invOutput;
        float p = 2 * (normalX * outputX + normalY * outputY + normalZ * outputZ);
        float inputX = outputX - p * normalX;
        float inputY = outputY - p * normalY;
        float inputZ = outputZ - p * normalZ;
        input.set(inputX, inputY, inputZ);
        return 1 / (delta * delta * (float) Math.PI);
    }

    @Override
    public float sampleDiffuseOutput(BaseRandom random, PointData pointData, Float3 input, Float3 output) {
        float normalX = pointData.normalX;
        float normalY = pointData.normalY;
        float normalZ = pointData.normalZ;
        float invNormal = 1 / (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        normalX *= invNormal;
        normalY *= invNormal;
        normalZ *= invNormal;
        float inputX = input.x;
        float inputY = input.y;
        float inputZ = input.z;
        float invInput = 1 / (float) Math.sqrt(inputX * inputX + inputY * inputY + inputZ * inputZ);
        inputX *= invInput;
        inputY *= invInput;
        inputZ *= invInput;
        float p = 2 * (normalX * inputX + normalY * inputY + normalZ * inputZ);
        float outputX = inputX - p * normalX;
        float outputY = inputY - p * normalY;
        float outputZ = inputZ - p * normalZ;
        output.set(outputX, outputY, outputZ);
        return 1 / (delta * delta * (float) Math.PI);
    }

    @Override
    public void getDiffuse(PointData pointData, Float3 input, Float3 output, Float3 power) {
        float normalX = pointData.normalX;
        float normalY = pointData.normalY;
        float normalZ = pointData.normalZ;
        float invNormal = 1 / (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        normalX *= invNormal;
        normalY *= invNormal;
        normalZ *= invNormal;
        float inputX = input.x;
        float inputY = input.y;
        float inputZ = input.z;
        float invInput = 1 / (float) Math.sqrt(inputX * inputX + inputY * inputY + inputZ * inputZ);
        inputX *= invInput;
        inputY *= invInput;
        inputZ *= invInput;
        float outputX = output.x;
        float outputY = output.y;
        float outputZ = output.z;
        float invOutput = 1 / (float) Math.sqrt(outputX * outputX + outputY * outputY + outputZ * outputZ);
        outputX *= invOutput;
        outputY *= invOutput;
        outputZ *= invOutput;
        float needNormalX = (inputX - outputX) / 2;
        float needNormalY = (inputY - outputY) / 2;
        float needNormalZ = (inputZ - outputZ) / 2;
        float invNeedNormal = 1 / (float) Math.sqrt(needNormalX * needNormalX + needNormalY * needNormalY + needNormalZ * needNormalZ);
        needNormalX *= invNeedNormal;
        needNormalY *= invNeedNormal;
        needNormalZ *= invNeedNormal;
        float t = normalX * needNormalX + normalY * needNormalY + normalZ * needNormalZ;
        float n = (1 - t * t) * 4 / delta;
        if (n > 1) {
            power.set(0, 0, 0);
        } else {
            float pow = 1 / (delta * delta * (float) Math.PI);
            power.set(pow * color.x, pow * color.y, pow * color.z);
        }
    }

}
