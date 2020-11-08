package net.dxzc.pome.renderers;

import net.dxzc.pome.*;

public class PathTracingRenderer extends Renderer {

    public int minCount = 10;
    public int maxCount = 10000;
    public float error = 0.05f;
    public int maxDepth = 1000;


    @Override
    public void render(Scene scene, FrameBuffer frameBuffer) {
        Float3 var1 = new Float3();
        Float3 var2 = new Float3();
        Float3 var3 = new Float3();


        BaseRandom random = new BaseRandom();
        Ray ray = new Ray();
        PointBuffer pointBuffer = new PointBuffer();
        PointData pointData = new PointData();
        PointData lightData = new PointData();

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

        int minCount = this.minCount;
        int maxCount = this.maxCount;
        float vars = error * error;
        int maxDepth = this.maxDepth;

        int width = frameBuffer.width;
        int height = frameBuffer.height;
        for (int y = 0; y < height; y++) {
            System.out.println(y * 100 / height);
            for (int x = 0; x < width; x++) {
                // 计算标准化设备坐标
                float dx = 2f * x / width - 1;
                float dy = 1 - 2f * y / height;

                float sumR = 0, sumG = 0, sumB = 0;
                float sum = 0;
                int count = 0;
                float square = 0;
                while (count < minCount || (square - sum * sum / count) > (count - 1) * count * vars && count < maxCount) {
                    // 采样得到的颜色
                    float r = 0, g = 0, b = 0;

                    //累计权重
                    float pR = 1, pG = 1, pB = 1;

                    // 投射起点
                    float originX = camera.x;
                    float originY = camera.y;
                    float originZ = camera.z;

                    // 投射方向
                    float directionX = forward.x + dx * rightward.x + dy * upward.x;
                    float directionY = forward.y + dx * rightward.y + dy * upward.y;
                    float directionZ = forward.z + dx * rightward.z + dy * upward.z;

                    // 当前深度
                    int depth = 0;

                    for (; ; ) {

                        // 投射光线
                        ray.originX = originX;
                        ray.originY = originY;
                        ray.originZ = originZ;
                        ray.directionX = directionX;
                        ray.directionY = directionY;
                        ray.directionZ = directionZ;
                        ray.minTime = 0.01f;
                        ray.maxTime = Float.POSITIVE_INFINITY;
                        ray.init();
                        pointBuffer.clear();
                        scene.intersect(ray, pointBuffer);
                        Mesh mesh = pointBuffer.mesh;

                        // 检查投射命中情况
                        if (mesh == null) {
                            r += pR * background.x;
                            g += pG * background.y;
                            b += pB * background.z;
                            break;
                        }

                        mesh.initPointData(pointBuffer, pointData);

                        // 深度为0时是第一次碰撞，需要额外计算碰撞点光源
                        if (depth == 0) {
                            var1.set(-directionX, -directionY, -directionZ);
                            mesh.getLight(pointData, var1, var2);
                            r += var2.x;
                            g += var2.y;
                            b += var2.z;
                        }

                        // 碰撞坐标
                        float pointX = pointData.x;
                        float pointY = pointData.y;
                        float pointZ = pointData.z;

                        float normalX = pointData.normalX;
                        float normalY = pointData.normalY;
                        float normalZ = pointData.normalZ;
                        float invNormal = 1 / (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
                        normalX *= invNormal;
                        normalY *= invNormal;
                        normalZ *= invNormal;

                        // 采样光源通过对此点的贡献

                        // 采样一个光源
                        float lightPdf = scene.sampleLight(random, pointBuffer);
                        Mesh light = pointBuffer.mesh;
                        if (light != null) {
                            light.initPointData(pointBuffer, lightData);
                            // 光源位置
                            float lightX = lightData.x;
                            float lightY = lightData.y;
                            float lightZ = lightData.z;

                            // 计算光源可见性
                            ray.originX = pointX;
                            ray.originY = pointY;
                            ray.originZ = pointZ;
                            float lightDirX = lightX - pointX;
                            float lightDirY = lightY - pointY;
                            float lightDirZ = lightZ - pointZ;
                            ray.directionX = lightDirX;
                            ray.directionY = lightDirY;
                            ray.directionZ = lightDirZ;
                            ray.minTime = 0.001f;
                            ray.maxTime = 0.999f;
                            ray.init();
                            if (!scene.occluded(ray)) {
                                // 计算光源贡献
                                float lightNormalX = lightData.normalX;
                                float lightNormalY = lightData.normalY;
                                float lightNormalZ = lightData.normalZ;
                                float invLightNormal = 1 / (float) Math.sqrt(lightNormalX * lightNormalX
                                        + lightNormalY * lightNormalY + lightNormalZ * lightNormalZ);
                                lightNormalX *= invLightNormal;
                                lightNormalY *= invLightNormal;
                                lightNormalZ *= invLightNormal;
                                float lightDirs = lightDirX * lightDirX + lightDirY * lightDirY + lightDirZ * lightDirZ;
                                float lightDot = lightDirX * lightNormalX + lightDirY * lightNormalY + lightDirZ * lightNormalZ;
                                float normalDot = normalX * lightDirX + normalY * lightDirY + normalZ * lightDirZ;
                                float pow = Math.abs(normalDot * lightDot) / (lightDirs * lightDirs) / lightPdf;

                                // 计算光源亮度
                                var1.set(-lightDirX, -lightDirY, -lightDirZ);
                                light.getLight(lightData, var1, var2);
                                float lightR = var2.x;
                                float lightG = var2.y;
                                float lightB = var2.z;

                                // 计算光源贡献率
                                var1.set(-lightDirX, -lightDirY, -lightDirZ);
                                var2.set(-directionX, -directionY, -directionZ);
                                mesh.getDiffuse(pointData, var1, var2, var3);

                                // 计算光源贡献
                                r += pR * var3.x * lightR * pow;
                                g += pG * var3.y * lightG * pow;
                                b += pB * var3.z * lightB * pow;
                            }
                        }

                        var1.set(-directionX, -directionY, -directionZ);
                        float nextPdf = mesh.sampleDiffuseInput(random, pointData, var1, var2);
                        if (nextPdf <= 0) {
                            break;
                        }
                        float nextDirectionX = var2.x;
                        float nextDirectionY = var2.y;
                        float nextDirectionZ = var2.z;
                        float nextDirection = (float) Math.sqrt(nextDirectionX * nextDirectionX + nextDirectionY * nextDirectionY
                                + nextDirectionZ * nextDirectionZ);
                        if (nextDirection < 0.01f) {
                            break;
                        }
                        var1.x = nextDirectionX;
                        var1.y = nextDirectionY;
                        var1.z = nextDirectionZ;
                        var2.x = -directionX;
                        var2.y = -directionY;
                        var2.z = -directionZ;
                        mesh.getDiffuse(pointData, var1, var2, var3);
                        float pow = Math.abs(normalX * nextDirectionX + normalY * nextDirectionY + normalZ * nextDirectionZ)
                                / nextDirection / nextPdf;
                        pR *= var3.x * pow;
                        pG *= var3.y * pow;
                        pB *= var3.z * pow;


                        depth++;

                        // 计算轮盘赌概率
                        float pC = Math.min(((pR + pG + pB) / 3 + 1) / 2, 1);

                        // 测试轮盘赌和最大深度
                        if (random.nextFloat() > pC || depth >= maxDepth) {
                            r += pR * background.x;
                            g += pG * background.z;
                            b += pB * background.z;
                            break;
                        }


                        // 权重上叠加轮盘赌概率
                        pR /= pC;
                        pG /= pC;
                        pB /= pC;

                        // 补偿轮盘赌损失
                        r -= pR * (1 - pC) * background.x;
                        g -= pG * (1 - pC) * background.z;
                        b -= pB * (1 - pC) * background.z;

                        //填充下根光线
                        originX = pointX;
                        originY = pointY;
                        originZ = pointZ;
                        directionX = -nextDirectionX;
                        directionY = -nextDirectionY;
                        directionZ = -nextDirectionZ;

                    }


                    sumR += r;
                    sumG += g;
                    sumB += b;
                    float p = r + g + b;
                    sum += p;
                    count++;
                    square += p * p;
                }
                frameBuffer.set(x, y, sumR / count, sumG / count, sumB / count);
            }
        }
    }

}
