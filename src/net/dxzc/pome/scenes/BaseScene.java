package net.dxzc.pome.scenes;

import net.dxzc.pome.Float3;
import net.dxzc.pome.Scene;

public abstract class BaseScene extends Scene {

    public BaseScene() {
        camera.set(0, 0, 0);
        forward.set(0, 0, -1);
        upward.set(0, 1, 0);
        rightward.set(1, 0, 0);
    }

    public final Float3 camera = new Float3();

    public final Float3 forward = new Float3();

    public final Float3 upward = new Float3();

    public final Float3 rightward = new Float3();

    @Override
    public void getCamera(Float3 camera) {
        camera.set(this.camera);
    }

    @Override
    public void getForward(Float3 forward) {
        forward.set(this.forward);
    }

    @Override
    public void getUpward(Float3 upward) {
        upward.set(this.upward);
    }

    @Override
    public void getRightward(Float3 rightward) {
        rightward.set(this.rightward);
    }

}
