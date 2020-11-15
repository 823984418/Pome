package net.dxzc.pome.materials;

import net.dxzc.pome.Float3;
import net.dxzc.pome.PointData;

public class DiffuseMaterial extends BaseMaterial {

    public final Float3 color = new Float3();

    public final Float3 light = new Float3();

    @Override
    public float getLightLevel(PointData pointData) {
        return light.x + light.y + light.z;
    }

    @Override
    public void getLight(PointData pointData, Float3 out, Float3 light) {
        light.set(this.light);
    }

    @Override
    public void getDiffuse(PointData pointData, Float3 input, Float3 output, Float3 power) {
        float normalX = pointData.normalX;
        float normalY = pointData.normalY;
        float normalZ = pointData.normalZ;
        float normal = (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        float inputX = input.x;
        float inputY = input.y;
        float inputZ = input.z;
        float i = (float) Math.sqrt(inputX * inputX + inputY * inputY + inputZ * inputZ);
        float in = normalX * inputX + normalY * inputY + normalZ * inputZ;
        float out = normalX * output.x + normalY * output.y + normalZ * output.z;
        if (in > 0 ^ out > 0) {
            power.set(color);
            double n = Math.abs(in) / Math.PI / normal / i;
            power.x *= n;
            power.y *= n;
            power.z *= n;
        } else {
            power.set(0, 0, 0);
        }
    }

}
