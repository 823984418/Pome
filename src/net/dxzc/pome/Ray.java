package net.dxzc.pome;

public final class Ray {

    public float originX;

    public float originY;

    public float originZ;

    public float directionX;

    public float directionY;

    public float directionZ;

    public float minTime;

    public float maxTime;


    public float invDirectionX;

    public float invDirectionY;

    public float invDirectionZ;

    public void init() {
        invDirectionX = 1 / directionX;
        invDirectionY = 1 / directionY;
        invDirectionZ = 1 / directionZ;
    }

}
