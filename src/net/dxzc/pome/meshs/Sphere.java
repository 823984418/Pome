package net.dxzc.pome.meshs;

import net.dxzc.pome.*;

public class Sphere extends BaseMesh {

    public Sphere(Material material) {
        super(material);
    }

    public float radius;

    public final PointData center = new PointData();

    private float centerX;

    private float centerY;

    private float centerZ;

    private float area;

    private float lightLevel;

    public void init() {
        centerX = center.x;
        centerY = center.y;
        centerZ = center.z;
        area = 4f / 3 * (float) Math.PI * radius * radius * radius;
        lightLevel = material.getLightLevel(center) * area;
    }

    @Override
    public float getLightLevel() {
        return lightLevel;
    }

    @Override
    public void getBounds(Float3 min, Float3 max) {
        min.set(centerX - radius, centerY - radius, centerZ - radius);
        max.set(centerX + radius, centerY + radius, centerZ + radius);
    }

    @Override
    public float sampleLight(BaseRandom random, PointBuffer pointBuffer) {
        random.nextTarget();
        pointBuffer.buffer1 = random.float3.x;
        pointBuffer.buffer2 = random.float3.y;
        pointBuffer.buffer3 = random.float3.z;
        return 1 / area;
    }

    @Override
    public void intersect(Ray ray, PointBuffer pointBuffer) {
        float originX = ray.originX;
        float originY = ray.originY;
        float originZ = ray.originZ;
        float directionX = ray.directionX;
        float directionY = ray.directionY;
        float directionZ = ray.directionZ;
        float longX = originX - centerX;
        float longY = originY - centerY;
        float longZ = originZ - centerZ;
        float r = radius;
        float a = directionX * directionX + directionY * directionY + directionZ * directionZ;
        float b = 2 * (directionX * longX + directionY * longY + directionZ * longZ);
        float c = longX * longX + longY * longY + longZ * longZ - r * r;
        float delta = b * b - 4 * a * c;
        if (delta < 0) {
            return;
        }
        delta = (float) Math.sqrt(delta);
        float t = (-b - delta )/ (2 * a);
        if (t > ray.minTime && t < ray.maxTime) {
            ray.maxTime = t;
            pointBuffer.mesh = this;
            pointBuffer.buffer1 = longX + directionX * t;
            pointBuffer.buffer2 = longY + directionY * t;
            pointBuffer.buffer3 = longZ + directionZ * t;
        }
        t = (-b + delta)/ (2*a);
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
        float originX = ray.originX;
        float originY = ray.originY;
        float originZ = ray.originZ;
        float directionX = ray.directionX;
        float directionY = ray.directionY;
        float directionZ = ray.directionZ;
        float longX = originX - centerX;
        float longY = originY - centerY;
        float longZ = originZ - centerZ;
        float r = radius;
        float a = directionX * directionX + directionY * directionY + directionZ * directionZ;
        float b = 2 * (directionX * longX + directionY * longY + directionZ * longZ);
        float c = longX * longX + longY * longY + longZ * longZ - r * r;
        float delta = b * b - 4 * a * c;
        if (delta < 0) {
            return false;
        }
        delta = (float) Math.sqrt(delta);
        return -b - delta > 0 || -b + delta > 0;
    }

    @Override
    public void initPointData(PointBuffer pointBuffer, PointData pointData) {
        pointData.set(center);
        float normalX = pointBuffer.buffer1;
        float normalY = pointBuffer.buffer2;
        float normalZ = pointBuffer.buffer3;
        pointData.x = centerX + normalX;
        pointData.y = centerY + normalY;
        pointData.z = centerZ + normalZ;
        float invNormal = 1 / (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        pointData.normalX = normalX * invNormal;
        pointData.normalY = normalY * invNormal;
        pointData.normalZ = normalZ * invNormal;
        if (Math.abs(normalX) > 0.999f) {
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
