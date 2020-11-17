package net.dxzc.pome.materials;

import net.dxzc.pome.Double3;
import net.dxzc.pome.PointData;

public class DiffuseMaterial extends BaseMaterial {

    public final Double3 color = new Double3();

    public final Double3 light = new Double3();

    @Override
    public double getLightLevel(PointData pointData) {
        return light.x + light.y + light.z;
    }

    @Override
    public void getLight(PointData pointData, Double3 out, Double3 light) {
        light.set(this.light);
    }

    @Override
    public void getDiffuse(PointData pointData, Double3 input, Double3 output, Double3 power) {
        double normalX = pointData.normalX;
        double normalY = pointData.normalY;
        double normalZ = pointData.normalZ;
        double normal =  Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        double inputX = input.x;
        double inputY = input.y;
        double inputZ = input.z;
        double i =  Math.sqrt(inputX * inputX + inputY * inputY + inputZ * inputZ);
        double in = normalX * inputX + normalY * inputY + normalZ * inputZ;
        double out = normalX * output.x + normalY * output.y + normalZ * output.z;
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
