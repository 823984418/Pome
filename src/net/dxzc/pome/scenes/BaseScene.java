package net.dxzc.pome.scenes;

import net.dxzc.pome.Double3;
import net.dxzc.pome.Scene;

public abstract class BaseScene extends Scene {

    public BaseScene() {
        camera.set(0, 0, 0);
        forward.set(0, 0, -1);
        upward.set(0, 1, 0);
        rightward.set(1, 0, 0);
    }

    public double backgroundR;

    public double backgroundG;

    public double backgroundB;

    public final Double3 camera = new Double3();

    public final Double3 forward = new Double3();

    public final Double3 upward = new Double3();

    public final Double3 rightward = new Double3();

    @Override
    public void getBackground(Double3 background) {
        background.x = backgroundR;
        background.y = backgroundG;
        background.z = backgroundB;
    }

    @Override
    public void getCamera(Double3 camera) {
        camera.set(this.camera);
    }

    @Override
    public void getForward(Double3 forward) {
        forward.set(this.forward);
    }

    @Override
    public void getUpward(Double3 upward) {
        upward.set(this.upward);
    }

    @Override
    public void getRightward(Double3 rightward) {
        rightward.set(this.rightward);
    }

}
