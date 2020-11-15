package net.dxzc.pome.scenes;

import net.dxzc.pome.*;

import java.util.Arrays;

public class LoseScene extends BaseScene {

    public LoseScene(Mesh[] meshes) {
        this(meshes, 0, meshes.length);
    }

    public LoseScene(Mesh[] meshes, int from, int to) {
        meshes = this.meshes = Arrays.copyOfRange(meshes, from, to);
        lights = new float[meshes.length];
        float light = 0;
        for (int i = 0; i < meshes.length; i++) {
            light += meshes[i].getLightLevel();
            lights[i] = light;
        }
        allLight = light;
    }

    private final float allLight;

    private final Mesh[] meshes;

    private final float[] lights;

    @Override
    public float sampleLight(BaseRandom random, PointBuffer pointBuffer) {
        float n = random.nextFloat() * allLight;
        float ls = 0;
        for (int i = 0; i < meshes.length; i++) {
            float l = lights[i];
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
