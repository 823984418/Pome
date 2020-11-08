package net.dxzc.pome;

public final class Sampler {

    public float sum;

    public int count;

    public float square;

    public Sampler sample(float n) {
        sum += n;
        count++;
        square += n * n;
        return this;
    }

    public float getAverage() {
        return sum / count;
    }

    public float getVariance() {
        return (square - sum * sum / count) / count;
    }

    public float getStandardVariance() {
        return (square - sum * sum / count) / (count - 1);
    }

    public float need(float var) {
        return getStandardVariance() / var - count;
    }

}
