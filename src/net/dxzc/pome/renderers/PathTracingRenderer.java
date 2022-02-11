package net.dxzc.pome.renderers;

import net.dxzc.pome.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PathTracingRenderer extends Renderer {

    public static final boolean DEBUG = true;

    public int minCount = 5;
    public int maxCount = 100;
    public double error = 0.2;
    public int maxDepth = 100;
    public double diffuseMax = 1;


    @Override
    public void render(Scene scene, Image image) {
        Image debugBuffer0 = null;
        Image debugBuffer1 = null;
        Image debugBuffer2 = null;
        if (DEBUG) {
            debugBuffer0 = new Image(image.width, image.height);
            debugBuffer1 = new Image(image.width, image.height);
            debugBuffer2 = new Image(image.width, image.height);
        }
        Double3 var1 = new Double3();
        Double3 var2 = new Double3();
        Double3 var3 = new Double3();


        BaseRandom random = new BaseRandom();
        Ray ray = new Ray();
        PointBuffer pointBuffer = new PointBuffer();
        PointData pointData = new PointData();
        PointData lightData = new PointData();

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

        int minCount = this.minCount;
        int maxCount = this.maxCount;
        double vars = error * error;
        int maxDepth = this.maxDepth;
        double diffuseMax = this.diffuseMax;

        int width = image.width;
        int height = image.height;
        for (int y = 0; y < height; y++) {
            if (true || DEBUG) {
                System.out.printf("%2.1f\n", y * 100.0 / height);
            }
            for (int x = 0; x < width; x++) {
                // 计算标准化设备坐标
                double sdx = 2.0 * x / width - 1;
                double sdy = 1 - 2.0 * y / height;

                double sumR = 0, sumG = 0, sumB = 0;

                // 样本亮度的和
                double sum = 0;

                // 采样次数
                int count = 0;

                // 样本亮度的平方和
                double square = 0;


                int depths = 0;

                // 样本方差的期望 = (square - sum*sum/count)/(count-1)
                // 平均值的方差期望 = 样本方差的期望 / count
                // 要求平均值的方差 <= vars
                // 因此当 square * count - sum * sum > vars * count * count * (n - 1) 需要继续采样
                while (count < minCount || (square * count - sum * sum) > vars * count * count * (count - 1) && count < maxCount) {
                    double rx = (random.nextDouble() * 2 - 1) / width;
                    double ry = (random.nextDouble() * 2 - 1) / height;
                    double dx = sdx + rx;
                    double dy = sdy + ry;

                    // 采样得到的颜色
                    double r = 0, g = 0, b = 0;

                    //累计权重
                    double pR = 1, pG = 1, pB = 1;

                    // 投射起点
                    double originX = camera.x;
                    double originY = camera.y;
                    double originZ = camera.z;

                    // 投射方向
                    double directionX = forward.x + dx * rightward.x + dy * upward.x;
                    double directionY = forward.y + dx * rightward.y + dy * upward.y;
                    double directionZ = forward.z + dx * rightward.z + dy * upward.z;

                    // 当前深度
                    int depth = 0;

                    double dR = 0;
                    double dG = 0;
                    double dB = 0;

                    for (; ; ) {

                        // 投射光线
                        ray.originX = originX;
                        ray.originY = originY;
                        ray.originZ = originZ;
                        ray.directionX = directionX;
                        ray.directionY = directionY;
                        ray.directionZ = directionZ;
                        ray.minTime = 0.01;
                        ray.maxTime = Double.POSITIVE_INFINITY;
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
                        } else {
                            if (dR > diffuseMax || dG > diffuseMax || dB > diffuseMax) {
                                var1.set(-directionX, -directionY, -directionZ);
                                mesh.getLight(pointData, var1, var2);
                                r += pR * Math.max(dR - diffuseMax, 0) / dR * var2.x;
                                g += pG * Math.max(dG - diffuseMax, 0) / dG * var2.y;
                                b += pB * Math.max(dB - diffuseMax, 0) / dB * var2.z;
                            }
                        }

                        // 碰撞坐标
                        double pointX = pointData.x;
                        double pointY = pointData.y;
                        double pointZ = pointData.z;

                        // 采样光源通过对此点的贡献

                        // 采样一个光源
                        double lightPdf = scene.sampleLight(random, pointBuffer);
                        Mesh light = pointBuffer.mesh;
                        if (light != null) {
                            light.initPointData(pointBuffer, lightData);
                            // 光源位置
                            double lightX = lightData.x;
                            double lightY = lightData.y;
                            double lightZ = lightData.z;

                            // 计算光源可见性
                            ray.originX = pointX;
                            ray.originY = pointY;
                            ray.originZ = pointZ;
                            double lightDirX = lightX - pointX;
                            double lightDirY = lightY - pointY;
                            double lightDirZ = lightZ - pointZ;
                            ray.directionX = lightDirX;
                            ray.directionY = lightDirY;
                            ray.directionZ = lightDirZ;
                            ray.minTime = 0.001;
                            ray.maxTime = 0.999;
                            ray.init();
                            if (!scene.occluded(ray)) {
                                // 计算光源贡献

                                // 得到归一化的光源法线
                                double lightNormalX = lightData.normalX;
                                double lightNormalY = lightData.normalY;
                                double lightNormalZ = lightData.normalZ;
                                double invLightNormal = 1 / Math.sqrt(lightNormalX * lightNormalX
                                        + lightNormalY * lightNormalY + lightNormalZ * lightNormalZ);
                                lightNormalX *= invLightNormal;
                                lightNormalY *= invLightNormal;
                                lightNormalZ *= invLightNormal;

                                // 距离平方
                                double lightDirs = lightDirX * lightDirX + lightDirY * lightDirY + lightDirZ * lightDirZ;
                                // 距离 * cos光源
                                double lightDot = lightDirX * lightNormalX + lightDirY * lightNormalY + lightDirZ * lightNormalZ;
                                // cos光源 / 光源采样概率 / 距离平方
                                double pow = Math.abs(lightDot) / (Math.sqrt(lightDirs) * lightDirs) / lightPdf;

                                // 计算光源亮度
                                var1.set(-lightDirX, -lightDirY, -lightDirZ);
                                light.getLight(lightData, var1, var2);
                                double lightR = var2.x;
                                double lightG = var2.y;
                                double lightB = var2.z;

                                // 计算光源贡献率
                                var1.set(-lightDirX, -lightDirY, -lightDirZ);
                                var2.set(-directionX, -directionY, -directionZ);
                                mesh.getDiffuse(pointData, var1, var2, var3);

                                // 计算光源贡献
                                r += pR * Math.min(var3.x, diffuseMax) * lightR * pow;
                                g += pG * Math.min(var3.y, diffuseMax) * lightG * pow;
                                b += pB * Math.min(var3.z, diffuseMax) * lightB * pow;
                            }
                        }

                        var1.set(-directionX, -directionY, -directionZ);
                        double nextPdf = mesh.sampleDiffuseInput(random, pointData, var1, var2);
                        if (nextPdf <= 0) {
                            break;
                        }
                        double nextDirectionX = var2.x;
                        double nextDirectionY = var2.y;
                        double nextDirectionZ = var2.z;
                        double nextDirection = Math.sqrt(nextDirectionX * nextDirectionX + nextDirectionY * nextDirectionY
                                + nextDirectionZ * nextDirectionZ);
                        if (nextDirection < 0.01) {
                            break;
                        }
                        var1.x = nextDirectionX;
                        var1.y = nextDirectionY;
                        var1.z = nextDirectionZ;
                        var2.x = -directionX;
                        var2.y = -directionY;
                        var2.z = -directionZ;
                        mesh.getDiffuse(pointData, var1, var2, var3);
                        double pow = 1 / nextPdf;
                        dR = var3.x;
                        dG = var3.y;
                        dB = var3.z;
                        pR *= dR * pow;
                        pG *= dG * pow;
                        pB *= dB * pow;

                        depth++;
                        if (DEBUG) {
                            depths++;
                        }

                        // 计算轮盘赌概率
                        double pC = Math.min(Math.pow((pR + pG + pB) / 3, 0.1), 1);

                        // 测试轮盘赌和最大深度
                        if (random.nextDouble() >= pC || depth >= maxDepth) {
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
                    double p = r + g + b;
                    sum += p;
                    count++;
                    square += p * p;

                }
                image.set(x, y, sumR / count, sumG / count, sumB / count);
                if (DEBUG) {
                    double debug0 = 0.001 * count;
                    double debug1 = 0.1 * depths / count;
                    double debug2 = Math.sqrt((square - sum * sum / count) / (count - 1) / count);
                    debugBuffer0.set(x, y, debug0, debug0, debug0);
                    debugBuffer1.set(x, y, debug1, debug1, debug1);
                    debugBuffer2.set(x, y, debug2, debug2, debug2);
                }
            }
        }
        if (DEBUG) {
            try {
                ImageIO.write(ImageHelper.toJavaImage(debugBuffer0), "png", new File("debug0.png"));
                ImageIO.write(ImageHelper.toJavaImage(debugBuffer1), "png", new File("debug1.png"));
                ImageIO.write(ImageHelper.toJavaImage(debugBuffer2), "png", new File("debug2.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
