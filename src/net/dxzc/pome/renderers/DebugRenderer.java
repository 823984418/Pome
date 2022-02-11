package net.dxzc.pome.renderers;

import net.dxzc.pome.*;

public class DebugRenderer extends Renderer {

    @Override
    public void render(Scene scene, Image image) {

        Double3 var1 = new Double3();
        Double3 var2 = new Double3();
        Double3 var3 = new Double3();

        Ray ray = new Ray();
        PointBuffer pointBuffer = new PointBuffer();
        PointData pointData = new PointData();

        Double3 camera = new Double3();
        scene.getCamera(camera);
        Double3 forward = new Double3();
        scene.getForward(forward);
        Double3 upward = new Double3();
        scene.getUpward(upward);
        Double3 rightward = new Double3();
        scene.getRightward(rightward);
        Double3 background = new Double3();
        scene.getBackground(background);


        int width = image.width;
        int height = image.height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // 计算标准化设备坐标
                double dx = 2.0 * x / width - 1;
                double dy = 1 - 2.0 * y / height;

                // 投射起点
                ray.originX = camera.x;
                ray.originY = camera.y;
                ray.originZ = camera.z;
                // 投射方向
                double directionX = ray.directionX = forward.x + dx * rightward.x + dy * upward.x;
                double directionY = ray.directionY = forward.y + dx * rightward.y + dy * upward.y;
                double directionZ = ray.directionZ = forward.z + dx * rightward.z + dy * upward.z;
                ray.minTime = 0.01f;
                ray.maxTime = Double.POSITIVE_INFINITY;
                ray.init();
                pointBuffer.clear();
                scene.intersect(ray, pointBuffer);
                Mesh mesh = pointBuffer.mesh;
                if (mesh == null) {
                    image.set(x, y, background.x, background.y, background.z);
                    continue;
                }
                mesh.initPointData(pointBuffer, pointData);

                double invDirection = 1 / Math.sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);

                double normalX = pointData.normalX;
                double normalY = pointData.normalY;
                double normalZ = pointData.normalZ;
                double invNormal = 1 / Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
                normalX *= invNormal;
                normalY *= invNormal;
                normalZ *= invNormal;
                double cos = (directionX * normalX + directionY * normalY + directionZ * normalZ) * invDirection;
                if (cos < 0) {
                    normalX = -normalX;
                    normalY = -normalY;
                    normalZ = -normalZ;
                    cos = -cos;
                }
                var1.x = normalX;
                var1.y = normalY;
                var1.z = normalZ;

                var2.x = -directionX;
                var2.y = -directionY;
                var2.z = -directionZ;
                mesh.getDiffuse(pointData, var1, var2, var3);

                var2.x = -directionX;
                var2.y = -directionY;
                var2.z = -directionZ;

                mesh.getLight(pointData, var1, var2);

                image.set(x, y, var2.x + var3.x * cos, var2.y + var3.y * cos, var2.z + var3.z * cos);
            }
        }
    }

}
