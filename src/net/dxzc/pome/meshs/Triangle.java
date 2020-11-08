package net.dxzc.pome.meshs;

import net.dxzc.pome.*;

public class Triangle extends BaseMesh {

    public Triangle(Material material) {
        super(material);
    }

    public final PointData aData = new PointData();

    public final PointData bData = new PointData();

    public final PointData cData = new PointData();

    private float aX;
    private float aY;
    private float aZ;
    private float bX;
    private float bY;
    private float bZ;
    private float cX;
    private float cY;
    private float cZ;
    private float e1X;
    private float e1Y;
    private float e1Z;
    private float e2X;
    private float e2Y;
    private float e2Z;
    private float normalX;
    private float normalY;
    private float normalZ;
    public float tangentX;
    public float tangentY;
    public float tangentZ;
    private float aLevel;
    private float bLevel;
    private float cLevel;
    private float area;
    private float lightLevel;

    public void init() {
        aX = aData.x;
        aY = aData.y;
        aZ = aData.z;
        bX = bData.x;
        bY = bData.y;
        bZ = bData.z;
        cX = cData.x;
        cY = cData.y;
        cZ = cData.z;
        e1X = bX - aX;
        e1Y = bY - aY;
        e1Z = bZ - aZ;
        e2X = cX - aX;
        e2Y = cY - aY;
        e2Z = cZ - aZ;
        normalX = e1Y * e2Z - e1Z * e2Y;
        normalY = e1Z * e2X - e1X * e2Z;
        normalZ = e1X * e2Y - e1Y * e2X;
        tangentX = cX - bX;
        tangentY = cY - bY;
        tangentZ = cZ - bZ;
        aLevel = material.getLightLevel(aData);
        bLevel = material.getLightLevel(bData);
        cLevel = material.getLightLevel(cData);
        float norm = (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        float invNorm = 1 / norm;
        aData.normalX = bData.normalX = cData.normalX = (normalX *= invNorm);
        aData.normalY = bData.normalY = cData.normalY = (normalY *= invNorm);
        aData.normalZ = bData.normalZ = cData.normalZ = (normalZ *= invNorm);
        float invTangent = 1 / (float) Math.sqrt(tangentX * tangentX + tangentY * tangentY + tangentZ * tangentZ);
        aData.tangentX = bData.tangentX = cData.tangentX = (tangentX *= invTangent);
        aData.tangentY = bData.tangentY = cData.tangentY = (tangentY *= invTangent);
        aData.tangentZ = bData.tangentZ = cData.tangentZ = (tangentZ *= invTangent);
        area = norm / 2;
        lightLevel = (aLevel + bLevel + cLevel) / 3 * area;
    }

    @Override
    public float getLightLevel() {
        return lightLevel;
    }

    @Override
    public void getBounds(Float3 min, Float3 max) {
        min.x = Math.min(aX, Math.min(bX, cX));
        min.y = Math.min(aY, Math.min(bY, cY));
        min.z = Math.min(aZ, Math.min(bZ, cZ));
        max.x = Math.max(aX, Math.max(bX, cX));
        max.y = Math.max(aY, Math.max(bY, cY));
        max.z = Math.max(aZ, Math.max(bZ, cZ));
    }

    @Override
    public float sampleLight(BaseRandom random, PointBuffer pointBuffer) {
        float aLevel = this.aLevel;
        float bLevel = this.bLevel;
        float cLevel = this.cLevel;
        random.nextWUV(aLevel, bLevel, cLevel);
        Float3 wuv = random.float3;
        float w = pointBuffer.buffer1 = wuv.x;
        float u = pointBuffer.buffer2 = wuv.y;
        float v = pointBuffer.buffer3 = wuv.z;
        return (w * aLevel + u * bLevel + v * cLevel) / lightLevel;
    }

    @Override
    public void intersect(Ray ray, PointBuffer pointBuffer) {
        float e1X = this.e1X, e1Y = this.e1Y, e1Z = this.e1Z;
        float e2X = this.e2X, e2Y = this.e2Y, e2Z = this.e2Z;
        float aX = this.aX, aY = this.aY, aZ = this.aZ;
        float oX = ray.originX, oY = ray.originY, oZ = ray.originZ;
        float dX = ray.directionX, dY = ray.directionY, dZ = ray.directionZ;

        float pX = dY * e2Z - dZ * e2Y, pY = dZ * e2X - dX * e2Z, pZ = dX * e2Y - dY * e2X;
        float det = e1X * pX + e1Y * pY + e1Z * pZ;
        float tX = oX - aX, tY = oY - aY, tZ = oZ - aZ;
        if (det < 0) {
            det = -det;
            tX = -tX;
            tY = -tY;
            tZ = -tZ;
        }
        float u = tX * pX + tY * pY + tZ * pZ;
        if (u <= 0 || u >= det) {
            return;
        }
        float qX = tY * e1Z - tZ * e1Y, qY = tZ * e1X - tX * e1Z, qZ = tX * e1Y - tY * e1X;
        float v = dX * qX + dY * qY + dZ * qZ;
        if (v <= 0 || u + v >= det) {
            return;
        }
        float invDet = 1 / det;
        float time = (qX * e2X + qY * e2Y + qZ * e2Z) * invDet;
        if (time <= ray.minTime || time >= ray.maxTime) {
            return;
        }
        u *= invDet;
        v *= invDet;
        pointBuffer.mesh = this;
        pointBuffer.buffer1 = 1 - u - v;
        pointBuffer.buffer2 = u;
        pointBuffer.buffer3 = v;
        ray.maxTime = time;
    }

    @Override
    public boolean occluded(Ray ray) {
        float e1X = this.e1X, e1Y = this.e1Y, e1Z = this.e1Z;
        float e2X = this.e2X, e2Y = this.e2Y, e2Z = this.e2Z;
        float aX = this.aX, aY = this.aY, aZ = this.aZ;
        float oX = ray.originX, oY = ray.originY, oZ = ray.originZ;
        float dX = ray.directionX, dY = ray.directionY, dZ = ray.directionZ;

        float pX = dY * e2Z - dZ * e2Y, pY = dZ * e2X - dX * e2Z, pZ = dX * e2Y - dY * e2X;
        float det = e1X * pX + e1Y * pY + e1Z * pZ;
        float tX = oX - aX, tY = oY - aY, tZ = oZ - aZ;
        if (det < 0) {
            det = -det;
            tX = -tX;
            tY = -tY;
            tZ = -tZ;
        }
        float u = tX * pX + tY * pY + tZ * pZ;
        if (u <= 0 || u >= det) {
            return false;
        }
        float qX = tY * e1Z - tZ * e1Y, qY = tZ * e1X - tX * e1Z, qZ = tX * e1Y - tY * e1X;
        float v = dX * qX + dY * qY + dZ * qZ;
        if (v <= 0 || u + v >= det) {
            return false;
        }
        float time = (qX * e2X + qY * e2Y + qZ * e2Z) / det;
        if (time <= ray.minTime || time >= ray.maxTime) {
            return false;
        }
        return true;
    }

    @Override
    public void initPointData(PointBuffer pointBuffer, PointData pointData) {
        float w = pointBuffer.buffer1;
        float u = pointBuffer.buffer2;
        float v = pointBuffer.buffer3;
        pointData.x = aX * w + bX * u + cX * v;
        pointData.y = aY * w + bY * u + cY * v;
        pointData.z = aZ * w + bZ * u + cZ * v;
        pointData.normalX = normalX;
        pointData.normalY = normalY;
        pointData.normalZ = normalZ;
        pointData.tangentX = tangentX;
        pointData.tangentY = tangentY;
        pointData.tangentZ = tangentZ;
        pointData.set(aData, bData, cData, w, u, v);
    }

}
