package net.dxzc.pome;

public final class Ray {

    public double originX;

    public double originY;

    public double originZ;

    public double directionX;

    public double directionY;

    public double directionZ;

    public double minTime;

    public double maxTime;


    public double invDirectionX;

    public double invDirectionY;

    public double invDirectionZ;

    public void init() {
        invDirectionX = 1 / directionX;
        invDirectionY = 1 / directionY;
        invDirectionZ = 1 / directionZ;
    }

}
