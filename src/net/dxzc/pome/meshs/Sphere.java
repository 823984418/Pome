package net.dxzc.pome.meshs;

import net.dxzc.pome.*;

public class Sphere extends BaseMesh {

    public Sphere(Material material) {
        super(material);
    }

    public double radius;

    public final PointData center = new PointData();

    private double centerX;

    private double centerY;

    private double centerZ;

    private double area;

    private double lightLevel;

    public void init() {
        centerX = center.x;
        centerY = center.y;
        centerZ = center.z;
        area = 4f *  Math.PI * radius * radius;
        lightLevel = material.getLightLevel(center) * area;
    }

    @Override
    public double getLightLevel() {
        return lightLevel;
    }

    @Override
    public void getBounds(Double3 min, Double3 max) {
        min.set(centerX - radius, centerY - radius, centerZ - radius);
        max.set(centerX + radius, centerY + radius, centerZ + radius);
    }

    @Override
    public double sampleLight(BaseRandom random, PointBuffer pointBuffer) {
        random.nextTarget();
        pointBuffer.buffer1 = random.double3.x * radius;
        pointBuffer.buffer2 = random.double3.y * radius;
        pointBuffer.buffer3 = random.double3.z * radius;
        return 1 / area;
    }

    @Override
    public void intersect(Ray ray, PointBuffer pointBuffer) {
        double originX = ray.originX;
        double originY = ray.originY;
        double originZ = ray.originZ;
        double directionX = ray.directionX;
        double directionY = ray.directionY;
        double directionZ = ray.directionZ;
        double longX = originX - centerX;
        double longY = originY - centerY;
        double longZ = originZ - centerZ;
        double r = radius;
        double a = directionX * directionX + directionY * directionY + directionZ * directionZ;
        double b = 2 * (directionX * longX + directionY * longY + directionZ * longZ);
        double c = longX * longX + longY * longY + longZ * longZ - r * r;
        double delta = b * b - 4 * a * c;
        if (delta < 0) {
            return;
        }
        delta =  Math.sqrt(delta);
        double t = (-b - delta) / (2 * a);
        if (t > ray.minTime && t < ray.maxTime) {
            ray.maxTime = t;
            pointBuffer.mesh = this;
            pointBuffer.buffer1 = longX + directionX * t;
            pointBuffer.buffer2 = longY + directionY * t;
            pointBuffer.buffer3 = longZ + directionZ * t;
            return;
        }
        t = (-b + delta) / (2 * a);
        if (t > ray.minTime && t < ray.maxTime) {
            ray.maxTime = t;
            pointBuffer.mesh = this;
            pointBuffer.buffer1 = longX + directionX * t;
            pointBuffer.buffer2 = longY + directionY * t;
            pointBuffer.buffer3 = longZ + directionZ * t;
        }
    }

    @Override
    public boolean occluded(Ray ray) {
        double originX = ray.originX;
        double originY = ray.originY;
        double originZ = ray.originZ;
        double directionX = ray.directionX;
        double directionY = ray.directionY;
        double directionZ = ray.directionZ;
        double longX = originX - centerX;
        double longY = originY - centerY;
        double longZ = originZ - centerZ;
        double r = radius;
        double a = directionX * directionX + directionY * directionY + directionZ * directionZ;
        double b = 2 * (directionX * longX + directionY * longY + directionZ * longZ);
        double c = longX * longX + longY * longY + longZ * longZ - r * r;
        double delta = b * b - 4 * a * c;
        if (delta < 0) {
            return false;
        }
        delta =  Math.sqrt(delta);
        double t1 = (-b - delta) / (2 * a);
        if (t1 > ray.minTime && t1 < ray.maxTime) {
            return true;
        }
        double t2 = (-b + delta) / (2 * a);
        return t2 > ray.minTime && t2 < ray.maxTime;
    }

    @Override
    public void initPointData(PointBuffer pointBuffer, PointData pointData) {
        pointData.set(center);
        double normalX = pointBuffer.buffer1;
        double normalY = pointBuffer.buffer2;
        double normalZ = pointBuffer.buffer3;
        pointData.x = centerX + normalX;
        pointData.y = centerY + normalY;
        pointData.z = centerZ + normalZ;
        double invNormal = 1 /  Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        pointData.normalX = normalX * invNormal;
        pointData.normalY = normalY * invNormal;
        pointData.normalZ = normalZ * invNormal;
        if (Math.abs(normalX) > 0.999) {
            pointData.tangentX = -normalY;
            pointData.tangentY = -normalX;
            pointData.tangentZ = 0;
        } else {
            pointData.tangentX = 0;
            pointData.tangentY = -normalZ;
            pointData.tangentZ = normalY;
        }
    }

}
