package net.dxzc.pome.renderers;

import net.dxzc.pome.*;

public class DebugRenderer extends Renderer {

    @Override
    public void render(Scene scene, FrameBuffer frameBuffer) {

        Float3 var1 = new Float3();
        Float3 var2 = new Float3();
        Float3 var3 = new Float3();

        BaseRandom random = new BaseRandom();
        Ray ray = new Ray();
        PointBuffer pointBuffer = new PointBuffer();
        PointData pointData = new PointData();

        Float3 camera = new Float3();
        scene.getCamera(camera);
        Float3 forward = new Float3();
        scene.getForward(forward);
        Float3 upward = new Float3();
        scene.getUpward(upward);
        Float3 rightward = new Float3();
        scene.getRightward(rightward);
        Float3 background = new Float3();
        scene.getBackground(background);


        int width = frameBuffer.width;
        int height = frameBuffer.height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // 计算标准化设备坐标
                float dx = 2f * x / width - 1;
                float dy = 1 - 2f * y / height;

                // 投射起点
                ray.originX = camera.x;
                ray.originY = camera.y;
                ray.originZ = camera.z;
                // 投射方向
                float directionX = ray.directionX = forward.x + dx * rightward.x + dy * upward.x;
                float directionY = ray.directionY = forward.y + dx * rightward.y + dy * upward.y;
                float directionZ = ray.directionZ = forward.z + dx * rightward.z + dy * upward.z;
                ray.minTime = 0.01f;
                ray.maxTime = Float.POSITIVE_INFINITY;
                ray.init();
                pointBuffer.clear();
                scene.intersect(ray, pointBuffer);
                Mesh mesh = pointBuffer.mesh;
                if (mesh == null) {
                    frameBuffer.set(x, y, background.x, background.y, background.z);
                    continue;
                }
                mesh.initPointData(pointBuffer, pointData);

                float invDirection = 1 / (float) Math.sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);

                float normalX = pointData.normalX;
                float normalY = pointData.normalY;
                float normalZ = pointData.normalZ;
                float invNormal = 1 / (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
                normalX *= invNormal;
                normalY *= invNormal;
                normalZ *= invNormal;
                float cos = (directionX * normalX + directionY * normalY + directionZ * normalZ) * invDirection;
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

                frameBuffer.set(x, y, var2.x + var3.x * cos, var2.y + var3.y * cos, var2.z + var3.z * cos);
            }
        }
    }

}
