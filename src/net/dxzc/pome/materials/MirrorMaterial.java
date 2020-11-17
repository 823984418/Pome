package net.dxzc.pome.materials;

import net.dxzc.pome.BaseRandom;
import net.dxzc.pome.Double3;
import net.dxzc.pome.PointData;

public class MirrorMaterial extends BaseMaterial {

    public final Double3 color = new Double3();

    public double delta = 0.0001;

    @Override
    public double sampleDiffuseInput(BaseRandom random, PointData pointData, Double3 output, Double3 input) {
        double normalX = pointData.normalX;
        double normalY = pointData.normalY;
        double normalZ = pointData.normalZ;
        double invNormal = 1 /  Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        normalX *= invNormal;
        normalY *= invNormal;
        normalZ *= invNormal;
        double outputX = output.x;
        double outputY = output.y;
        double outputZ = output.z;
        double invOutput = 1 /  Math.sqrt(outputX * outputX + outputY * outputY + outputZ * outputZ);
        outputX *= invOutput;
        outputY *= invOutput;
        outputZ *= invOutput;
        double p = 2 * (normalX * outputX + normalY * outputY + normalZ * outputZ);
        double inputX = outputX - p * normalX;
        double inputY = outputY - p * normalY;
        double inputZ = outputZ - p * normalZ;
        input.set(inputX, inputY, inputZ);
        return 1 / (delta * delta *  Math.PI);
    }

    @Override
    public double sampleDiffuseOutput(BaseRandom random, PointData pointData, Double3 input, Double3 output) {
        double normalX = pointData.normalX;
        double normalY = pointData.normalY;
        double normalZ = pointData.normalZ;
        double invNormal = 1 /  Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        normalX *= invNormal;
        normalY *= invNormal;
        normalZ *= invNormal;
        double inputX = input.x;
        double inputY = input.y;
        double inputZ = input.z;
        double invInput = 1 /  Math.sqrt(inputX * inputX + inputY * inputY + inputZ * inputZ);
        inputX *= invInput;
        inputY *= invInput;
        inputZ *= invInput;
        double p = 2 * (normalX * inputX + normalY * inputY + normalZ * inputZ);
        double outputX = inputX - p * normalX;
        double outputY = inputY - p * normalY;
        double outputZ = inputZ - p * normalZ;
        output.set(outputX, outputY, outputZ);
        return 1 / (delta * delta *  Math.PI);
    }

    @Override
    public void getDiffuse(PointData pointData, Double3 input, Double3 output, Double3 power) {
        double normalX = pointData.normalX;
        double normalY = pointData.normalY;
        double normalZ = pointData.normalZ;
        double invNormal = 1 /  Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        normalX *= invNormal;
        normalY *= invNormal;
        normalZ *= invNormal;
        double inputX = input.x;
        double inputY = input.y;
        double inputZ = input.z;
        double invInput = 1 /  Math.sqrt(inputX * inputX + inputY * inputY + inputZ * inputZ);
        inputX *= invInput;
        inputY *= invInput;
        inputZ *= invInput;
        double outputX = output.x;
        double outputY = output.y;
        double outputZ = output.z;
        double invOutput = 1 /  Math.sqrt(outputX * outputX + outputY * outputY + outputZ * outputZ);
        outputX *= invOutput;
        outputY *= invOutput;
        outputZ *= invOutput;
        double needNormalX = (inputX - outputX) / 2;
        double needNormalY = (inputY - outputY) / 2;
        double needNormalZ = (inputZ - outputZ) / 2;
        double invNeedNormal = 1 /  Math.sqrt(needNormalX * needNormalX + needNormalY * needNormalY + needNormalZ * needNormalZ);
        needNormalX *= invNeedNormal;
        needNormalY *= invNeedNormal;
        needNormalZ *= invNeedNormal;
        double t = normalX * needNormalX + normalY * needNormalY + normalZ * needNormalZ;
        double n = (1 - t * t) * 4 / delta;
        if (n > 1) {
            power.set(0, 0, 0);
        } else {
            double pow = 1 / (delta * delta *  Math.PI);
            power.set(pow * color.x, pow * color.y, pow * color.z);
        }
    }

}
