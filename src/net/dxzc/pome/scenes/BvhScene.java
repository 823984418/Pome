package net.dxzc.pome.scenes;

import net.dxzc.pome.*;

import java.util.Arrays;

public class BvhScene extends BaseScene {

    public BvhScene(Mesh[] meshes) {
        this(meshes, 0, meshes.length);
    }

    public BvhScene(Mesh[] meshes, int from, int to) {
        if (meshes == null) {
            throw new NullPointerException();
        }
        if (from != to) {
            rootNode = new Node(meshes, from, to);
        }
    }

    private Node rootNode;

    @Override
    public float sampleLight(BaseRandom random, PointBuffer pointBuffer) {
        Node node = rootNode;
        if (node == null) {
            return 0;
        }
        float all = node.lightLevel;
        if (all <= 0) {
            return 0;
        }
        for (float light = all; ; ) {
            Mesh mesh = node.mesh;
            if (mesh != null) {
                pointBuffer.mesh = mesh;
                return light / all * mesh.sampleLight(random, pointBuffer);
            }
            float t = random.nextFloat() * light;
            Node left = node.left;
            float leftLight = left.lightLevel;
            if (t < leftLight) {
                node = left;
                light = leftLight;
            } else {
                node = node.right;
                light = node.lightLevel;
            }
        }
    }

    @Override
    public void intersect(Ray ray, PointBuffer pointBuffer) {
        Node node = rootNode;
        if (node == null) {
            return;
        }
        if (!Float.isNaN(node.near(ray))) {
            node.intersectNoCheck(ray, pointBuffer);
        }
    }

    @Override
    public boolean occluded(Ray ray) {
        Node node = rootNode;
        if (node == null) {
            return false;
        }
        if (!Float.isNaN(node.near(ray))) {
            return node.occludedNoCheck(ray);
        }
        return false;
    }


    private static final class Node {

        private Node(Mesh[] meshes, int from, int to) {
            Float3 min = new Float3();
            Float3 max = new Float3();
            if (from + 1 == to) {
                Mesh mesh = meshes[from];
                mesh.getBounds(min, max);
                minX = min.x;
                minY = min.y;
                minZ = min.z;
                maxX = max.x;
                maxY = max.y;
                maxZ = max.z;
                lightLevel = mesh.getLightLevel();
                this.mesh = mesh;
            } else {
                float centerMinX = Float.POSITIVE_INFINITY;
                float centerMinY = Float.POSITIVE_INFINITY;
                float centerMinZ = Float.POSITIVE_INFINITY;
                float centerMaxX = Float.NEGATIVE_INFINITY;
                float centerMaxY = Float.NEGATIVE_INFINITY;
                float centerMaxZ = Float.NEGATIVE_INFINITY;
                for (int i = from; i < to; i++) {
                    Mesh mesh = meshes[i];
                    mesh.getBounds(min, max);
                    float centerX = (min.x + max.x) / 2;
                    float centerY = (min.y + max.y) / 2;
                    float centerZ = (min.z + max.z) / 2;
                    centerMinX = Math.min(centerMinX, centerX);
                    centerMinY = Math.min(centerMinY, centerY);
                    centerMinZ = Math.min(centerMinZ, centerZ);
                    centerMaxX = Math.max(centerMaxX, centerX);
                    centerMaxY = Math.max(centerMaxY, centerY);
                    centerMaxZ = Math.max(centerMaxZ, centerZ);
                }
                float dX = centerMaxX - centerMinX;
                float dY = centerMaxY - centerMinY;
                float dZ = centerMaxZ - centerMinZ;
                int maxD;
                if (dX > dY) {
                    if (dX > dZ) {
                        maxD = 0;
                    } else {
                        maxD = 2;
                    }
                } else {
                    if (dY > dZ) {
                        maxD = 1;
                    } else {
                        maxD = 2;
                    }
                }
                switch (maxD) {
                    case 0:
                        Arrays.sort(meshes, from, to, (a, b) -> {
                            a.getBounds(min, max);
                            float aCenterX = min.x + max.x;
                            b.getBounds(min, max);
                            float bCenterX = min.x + max.x;
                            return Float.compare(aCenterX, bCenterX);
                        });
                        break;
                    case 1:
                        Arrays.sort(meshes, from, to, (a, b) -> {
                            a.getBounds(min, max);
                            float aCenterY = min.y + max.y;
                            b.getBounds(min, max);
                            float bCenterY = min.y + max.y;
                            return Float.compare(aCenterY, bCenterY);
                        });
                        break;
                    case 2:
                        Arrays.sort(meshes, from, to, (a, b) -> {
                            a.getBounds(min, max);
                            float aCenterZ = min.z + max.z;
                            b.getBounds(min, max);
                            float bCenterZ = min.z + max.z;
                            return Float.compare(aCenterZ, bCenterZ);
                        });
                        break;
                }
                int mid = (from + to) / 2;
                Node left = this.left = new Node(meshes, from, mid);
                Node right = this.right = new Node(meshes, mid, to);
                minX = Math.min(left.minX, right.minX);
                minY = Math.min(left.minY, right.minY);
                minZ = Math.min(left.minZ, right.minZ);
                maxX = Math.max(left.maxX, right.maxX);
                maxY = Math.max(left.maxY, right.maxY);
                maxZ = Math.max(left.maxZ, right.maxZ);
                lightLevel = left.lightLevel + right.lightLevel;
            }
        }

        float minX;
        float minY;
        float minZ;
        float maxX;
        float maxY;
        float maxZ;
        Node left;
        Node right;
        Mesh mesh;
        float lightLevel;

        private float near(Ray ray) {
            float i = Float.NEGATIVE_INFINITY;
            float o = Float.POSITIVE_INFINITY;
            float invX = ray.invDirectionX;
            float invY = ray.invDirectionY;
            float invZ = ray.invDirectionZ;
            float x = ray.originX;
            float y = ray.originY;
            float z = ray.originZ;
            float inX, inY, inZ;
            float outX, outY, outZ;
            if (invX > 0) {
                inX = (minX - x) * invX;
                outX = (maxX - x) * invX;
            } else {
                inX = (maxX - x) * invX;
                outX = (minX - x) * invX;
            }
            if (invY > 0) {
                inY = (minY - y) * invY;
                outY = (maxY - y) * invY;
            } else {
                inY = (maxY - y) * invY;
                outY = (minY - y) * invY;
            }
            if (invZ > 0) {
                inZ = (minZ - z) * invZ;
                outZ = (maxZ - z) * invZ;
            } else {
                inZ = (maxZ - z) * invZ;
                outZ = (minZ - z) * invZ;
            }
            float input = Math.max(inX, Math.max(inY, inZ));
            float output = Math.min(outX, Math.min(outY, outZ));
            if (input <= output && input < ray.maxTime && output > ray.minTime) {
                return input;
            }
            return Float.NaN;
        }

        private void intersectNoCheck(Ray ray, PointBuffer pointBuffer) {
            if (mesh != null) {
                mesh.intersect(ray, pointBuffer);
            } else {
                Node left = this.left;
                Node right = this.right;
                float leftNear = left.near(ray);
                if (Float.isNaN(leftNear)) {
                    float rightNear = right.near(ray);
                    if (Float.isNaN(rightNear)) {

                    } else {
                        right.intersectNoCheck(ray, pointBuffer);
                    }
                } else {
                    float rightNear = right.near(ray);
                    if (Float.isNaN(rightNear)) {
                        left.intersectNoCheck(ray, pointBuffer);
                    } else {
                        float oldTime = ray.maxTime;
                        if (leftNear < rightNear) {
                            left.intersectNoCheck(ray, pointBuffer);
                            if (oldTime != ray.maxTime) {
                                if (Float.isNaN(right.near(ray))) {
                                    return;
                                }
                            }
                            right.intersectNoCheck(ray, pointBuffer);
                        } else {
                            right.intersectNoCheck(ray, pointBuffer);
                            if (oldTime != ray.maxTime) {
                                if (Float.isNaN(left.near(ray))) {
                                    return;
                                }
                            }
                            left.intersectNoCheck(ray, pointBuffer);
                        }
                    }
                }
            }
        }

        private boolean occludedNoCheck(Ray ray) {
            if (mesh != null) {
                return mesh.occluded(ray);
            } else {
                Node left = this.left;
                Node right = this.right;
                float leftNear = left.near(ray);
                float rightNear = right.near(ray);
                if (Float.isNaN(leftNear)) {
                    if (Float.isNaN(rightNear)) {
                        return false;
                    } else {
                        return right.occludedNoCheck(ray);
                    }
                } else {
                    if (Float.isNaN(rightNear)) {
                        return left.occludedNoCheck(ray);
                    } else {
                        if (leftNear < rightNear) {
                            return left.occludedNoCheck(ray) ||
                                    right.occludedNoCheck(ray);
                        } else {
                            return right.occludedNoCheck(ray) ||
                                    left.occludedNoCheck(ray);
                        }
                    }
                }
            }
        }

    }

}
