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
        float in = normalX * input.x + normalY * input.y + normalZ * input.z;
        float out = normalX * output.x + normalY * output.y + normalZ * output.z;
        if (in > 0 ^ out > 0) {
            power.set(color);
            power.x /= Math.PI;
            power.y /= Math.PI;
            power.z /= Math.PI;
        } else {
            power.set(0, 0, 0);
        }
    }

}
