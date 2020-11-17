package net.dxzc.pome.scenes;

import net.dxzc.pome.*;

import java.util.Arrays;

public class LoseScene extends BaseScene {

    public LoseScene(Mesh[] meshes) {
        this(meshes, 0, meshes.length);
    }

    public LoseScene(Mesh[] meshes, int from, int to) {
        meshes = this.meshes = Arrays.copyOfRange(meshes, from, to);
        lights = new double[meshes.length];
        double light = 0;
        for (int i = 0; i < meshes.length; i++) {
            light += meshes[i].getLightLevel();
            lights[i] = light;
        }
        allLight = light;
    }

    private final double allLight;

    private final Mesh[] meshes;

    private final double[] lights;

    @Override
    public double sampleLight(BaseRandom random, PointBuffer pointBuffer) {
        double n = random.nextDouble() * allLight;
        double ls = 0;
        for (int i = 0; i < meshes.length; i++) {
            double l = lights[i];
            ls += l;
            if (ls >= l) {
                pointBuffer.mesh = meshes[i];
                return meshes[i].sampleLight(random, pointBuffer) * l / allLight;
            }
        }
        return 0;
    }

    @Override
    public void intersect(Ray ray, PointBuffer pointBuffer) {
        for (int i = 0; i < meshes.length; i++) {
            meshes[i].intersect(ray, pointBuffer);
        }
    }

    @Override
    public boolean occluded(Ray ray) {
        for (int i = 0; i < meshes.length; i++) {
            if (meshes[i].occluded(ray)) {
                return true;
            }
        }
        return false;
    }
}
