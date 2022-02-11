package net.dxzc.pome.renderers;

import net.dxzc.pome.Image;
import net.dxzc.pome.Renderer;
import net.dxzc.pome.Scene;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class MultiThreadPathTracingRenderer extends Renderer {

    public int threadCount = Math.max(Runtime.getRuntime().availableProcessors() - 1, 1);

    @Override
    public void render(Scene scene, Image image) {
        int count = threadCount;
        List<Future<Image>> list = new LinkedList<>();
        int width = image.width;
        int height = image.height;
        for (int i = 0; i < count; i++) {
            FutureTask<Image> task = new FutureTask<>(() -> {
                Image buffer = new Image(width, height);
                PathTracingRenderer renderer = new PathTracingRenderer();
                renderer.render(scene, buffer);
                return buffer;
            });
            new Thread(task).start();
            list.add(task);
        }
        double invCount = 1.0 / count;
        double[] bufferArray = image.buffer;
        for (Future<Image> i : list) {
            try {
                Image load = i.get();
                double[] loadArray = load.buffer;
                for (int t = 0; t < width * height * 3; t++) {
                    bufferArray[t] += loadArray[t] * invCount;
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
