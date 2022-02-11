package net.dxzc.pome.meshs;

import net.dxzc.pome.*;

public class Triangle extends BaseMesh {

    public Triangle(Material material) {
        super(material);
    }

    public final PointData aData = new PointData();

    public final PointData bData = new PointData();

    public final PointData cData = new PointData();

    private double aX;
    private double aY;
    private double aZ;
    private double bX;
    private double bY;
    private double bZ;
    private double cX;
    private double cY;
    private double cZ;
    private double e1X;
    private double e1Y;
    private double e1Z;
    private double e2X;
    private double e2Y;
    private double e2Z;
    private double normalX;
    private double normalY;
    private double normalZ;
    public double tangentX;
    public double tangentY;
    public double tangentZ;
    private double aLevel;
    private double bLevel;
    private double cLevel;
    private double area;
    private double lightLevel;

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
        double norm =  Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        double invNorm = 1 / norm;
        aData.normalX = bData.normalX = cData.normalX = (normalX *= invNorm);
        aData.normalY = bData.normalY = cData.normalY = (normalY *= invNorm);
        aData.normalZ = bData.normalZ = cData.normalZ = (normalZ *= invNorm);
        double invTangent = 1 /  Math.sqrt(tangentX * tangentX + tangentY * tangentY + tangentZ * tangentZ);
        aData.tangentX = bData.tangentX = cData.tangentX = (tangentX *= invTangent);
        aData.tangentY = bData.tangentY = cData.tangentY = (tangentY *= invTangent);
        aData.tangentZ = bData.tangentZ = cData.tangentZ = (tangentZ *= invTangent);
        area = norm / 2;
        lightLevel = (aLevel + bLevel + cLevel) / 3 * area;
    }

    @Override
    public double getLightLevel() {
        return lightLevel;
    }

    @Override
    public void getBounds(Double3 min, Double3 max) {
        min.x = Math.min(aX, Math.min(bX, cX));
        min.y = Math.min(aY, Math.min(bY, cY));
        min.z = Math.min(aZ, Math.min(bZ, cZ));
        max.x = Math.max(aX, Math.max(bX, cX));
        max.y = Math.max(aY, Math.max(bY, cY));
        max.z = Math.max(aZ, Math.max(bZ, cZ));
    }

    @Override
    public double sampleLight(BaseRandom random, PointBuffer pointBuffer) {
        double aLevel = this.aLevel;
        double bLevel = this.bLevel;
        double cLevel = this.cLevel;
        random.nextWUV(aLevel, bLevel, cLevel);
        Double3 wuv = random.double3;
        double w = pointBuffer.buffer1 = wuv.x;
        double u = pointBuffer.buffer2 = wuv.y;
        double v = pointBuffer.buffer3 = wuv.z;
        return (w * aLevel + u * bLevel + v * cLevel) / lightLevel;
    }

    @Override
    public void intersect(Ray ray, PointBuffer pointBuffer) {
        double e1X = this.e1X, e1Y = this.e1Y, e1Z = this.e1Z;
        double e2X = this.e2X, e2Y = this.e2Y, e2Z = this.e2Z;
        double aX = this.aX, aY = this.aY, aZ = this.aZ;
        double oX = ray.originX, oY = ray.originY, oZ = ray.originZ;
        double dX = ray.directionX, dY = ray.directionY, dZ = ray.directionZ;

        double pX = dY * e2Z - dZ * e2Y, pY = dZ * e2X - dX * e2Z, pZ = dX * e2Y - dY * e2X;
        double det = e1X * pX + e1Y * pY + e1Z * pZ;
        double tX = oX - aX, tY = oY - aY, tZ = oZ - aZ;
        if (det < 0) {
            det = -det;
            tX = -tX;
            tY = -tY;
            tZ = -tZ;
        }
        double u = tX * pX + tY * pY + tZ * pZ;
        if (u <= 0 || u >= det) {
            return;
        }
        double qX = tY * e1Z - tZ * e1Y, qY = tZ * e1X - tX * e1Z, qZ = tX * e1Y - tY * e1X;
        double v = dX * qX + dY * qY + dZ * qZ;
        if (v <= 0 || u + v >= det) {
            return;
        }
        double invDet = 1 / det;
        double time = (qX * e2X + qY * e2Y + qZ * e2Z) * invDet;
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
        double e1X = this.e1X, e1Y = this.e1Y, e1Z = this.e1Z;
        double e2X = this.e2X, e2Y = this.e2Y, e2Z = this.e2Z;
        double aX = this.aX, aY = this.aY, aZ = this.aZ;
        double oX = ray.originX, oY = ray.originY, oZ = ray.originZ;
        double dX = ray.directionX, dY = ray.directionY, dZ = ray.directionZ;

        double pX = dY * e2Z - dZ * e2Y, pY = dZ * e2X - dX * e2Z, pZ = dX * e2Y - dY * e2X;
        double det = e1X * pX + e1Y * pY + e1Z * pZ;
        double tX = oX - aX, tY = oY - aY, tZ = oZ - aZ;
        if (det < 0) {
            det = -det;
            tX = -tX;
            tY = -tY;
            tZ = -tZ;
        }
        double u = tX * pX + tY * pY + tZ * pZ;
        if (u <= 0 || u >= det) {
            return false;
        }
        double qX = tY * e1Z - tZ * e1Y, qY = tZ * e1X - tX * e1Z, qZ = tX * e1Y - tY * e1X;
        double v = dX * qX + dY * qY + dZ * qZ;
        if (v <= 0 || u + v >= det) {
            return false;
        }
        double time = (qX * e2X + qY * e2Y + qZ * e2Z) / det;
        if (time <= ray.minTime || time >= ray.maxTime) {
            return false;
        }
        return true;
    }

    @Override
    public void initPointData(PointBuffer pointBuffer, PointData pointData) {
        double w = pointBuffer.buffer1;
        double u = pointBuffer.buffer2;
        double v = pointBuffer.buffer3;
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
