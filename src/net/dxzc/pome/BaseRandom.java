package net.dxzc.pome;

import java.util.concurrent.atomic.AtomicLong;

public class BaseRandom {

    private long seed;

    /**
     * Creates a new random number generator. This constructor sets
     * the seed of the random number generator to a value very likely
     * to be distinct from any other invocation of this constructor.
     */
    public BaseRandom() {
        this(seedUniquifier() ^ System.nanoTime());
    }

    private static long seedUniquifier() {
        // L'Ecuyer, "Tables of Linear Congruential Generators of
        // Different Sizes and Good Lattice Structure", 1999
        for (; ; ) {
            long current = seedUniquifier.get();
            long next = current * 1181783497276652981L;
            if (seedUniquifier.compareAndSet(current, next))
                return next;
        }
    }

    private static final AtomicLong seedUniquifier
            = new AtomicLong(8682522807148012L);

    /**
     * Creates a new random number generator using a single {@code long} seed.
     * The seed is the initial value of the internal state of the pseudorandom
     * number generator which is maintained by method {@link #next}.
     *
     * <p>The invocation {@code new Random(seed)} is equivalent to:
     * <pre> {@code
     * Random rnd = new Random();
     * rnd.setSeed(seed);}</pre>
     *
     * @param seed the initial seed
     * @see #setSeed(long)
     */
    public BaseRandom(long seed) {
        setSeed(seed);
    }

    private static long initialScramble(long seed) {
        return (seed ^ 0x5DEECE66DL) & (1L << 48) - 1;
    }

    /**
     * Sets the seed of this random number generator using a single
     * {@code long} seed. The general contract of {@code setSeed} is
     * that it alters the state of this random number generator object
     * so as to be in exactly the same state as if it had just been
     * created with the argument {@code seed} as a seed. The method
     * {@code setSeed} is implemented by class {@code Random} by
     * atomically updating the seed to
     * <pre>{@code (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1)}</pre>
     *
     * <p>The implementation of {@code setSeed} by class {@code Random}
     * happens to use only 48 bits of the given seed. In general, however,
     * an overriding method may use all 64 bits of the {@code long}
     * argument as a seed value.
     *
     * @param seed the initial seed
     */
    public void setSeed(long seed) {
        this.seed = initialScramble(seed);
    }

    /**
     * Generates the next pseudorandom number. Subclasses should
     * override this, as this is used by all other methods.
     *
     * <p>The general contract of {@code next} is that it returns an
     * {@code int} value and if the argument {@code bits} is between
     * {@code 1} and {@code 32} (inclusive), then that many low-order
     * bits of the returned value will be (approximately) independently
     * chosen bit values, each of which is (approximately) equally
     * likely to be {@code 0} or {@code 1}. The method {@code next} is
     * implemented by class {@code Random} by atomically updating the seed to
     * <pre>{@code (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1)}</pre>
     * and returning
     * <pre>{@code (int)(seed >>> (48 - bits))}.</pre>
     * <p>
     * This is a linear congruential pseudorandom number generator, as
     * defined by D. H. Lehmer and described by Donald E. Knuth in
     * <i>The Art of Computer Programming,</i> Volume 2:
     * <i>Seminumerical Algorithms</i>, section 3.2.1.
     *
     * @param bits random bits
     * @return the next pseudorandom value from this random number
     * generator's sequence
     * @since 1.1
     */
    protected int next(int bits) {
        long nextSeed;
        nextSeed = (seed * 0x5DEECE66DL + 0xBL) & (1L << 48) - 1;
        seed = nextSeed;
        return (int) (nextSeed >>> (48 - bits));
    }

    /**
     * Generates random bytes and places them into a user-supplied
     * byte array.  The number of random bytes produced is equal to
     * the length of the byte array.
     *
     * <p>The method {@code nextBytes} is implemented by class {@code Random}
     * as if by:
     * <pre> {@code
     * public void nextBytes(byte[] bytes) {
     *   for (int i = 0; i < bytes.length; )
     *     for (int rnd = nextInt(), n = Math.min(bytes.length - i, 4);
     *          n-- > 0; rnd >>= 8)
     *       bytes[i++] = (byte)rnd;
     * }}</pre>
     *
     * @param bytes the byte array to fill with random bytes
     * @throws NullPointerException if the byte array is null
     * @since 1.1
     */
    public void nextBytes(byte[] bytes) {
        for (int i = 0, len = bytes.length; i < len; )
            for (int rnd = nextInt(),
                 n = Math.min(len - i, Integer.SIZE / Byte.SIZE);
                 n-- > 0; rnd >>= Byte.SIZE)
                bytes[i++] = (byte) rnd;
    }

    /**
     * Returns the next pseudorandom, uniformly distributed {@code int}
     * value from this random number generator's sequence. The general
     * contract of {@code nextInt} is that one {@code int} value is
     * pseudorandomly generated and returned. All 2<sup>32</sup> possible
     * {@code int} values are produced with (approximately) equal probability.
     *
     * <p>The method {@code nextInt} is implemented by class {@code Random}
     * as if by:
     * <pre> {@code
     * public int nextInt() {
     *   return next(32);
     * }}</pre>
     *
     * @return the next pseudorandom, uniformly distributed {@code int}
     * value from this random number generator's sequence
     */
    public int nextInt() {
        return next(32);
    }

    /**
     * Returns a pseudorandom, uniformly distributed {@code int} value
     * between 0 (inclusive) and the specified value (exclusive), drawn from
     * this random number generator's sequence.  The general contract of
     * {@code nextInt} is that one {@code int} value in the specified range
     * is pseudorandomly generated and returned.  All {@code bound} possible
     * {@code int} values are produced with (approximately) equal
     * probability.  The method {@code nextInt(int bound)} is implemented by
     * class {@code Random} as if by:
     * <pre> {@code
     * public int nextInt(int bound) {
     *   if (bound <= 0)
     *     throw new IllegalArgumentException("bound must be positive");
     *
     *   if ((bound & -bound) == bound)  // i.e., bound is a power of 2
     *     return (int)((bound * (long)next(31)) >> 31);
     *
     *   int bits, val;
     *   do {
     *       bits = next(31);
     *       val = bits % bound;
     *   } while (bits - val + (bound-1) < 0);
     *   return val;
     * }}</pre>
     *
     * <p>The hedge "approximately" is used in the foregoing description only
     * because the next method is only approximately an unbiased source of
     * independently chosen bits.  If it were a perfect source of randomly
     * chosen bits, then the algorithm shown would choose {@code int}
     * values from the stated range with perfect uniformity.
     * <p>
     * The algorithm is slightly tricky.  It rejects values that would result
     * in an uneven distribution (due to the fact that 2^31 is not divisible
     * by n). The probability of a value being rejected depends on n.  The
     * worst case is n=2^30+1, for which the probability of a reject is 1/2,
     * and the expected number of iterations before the loop terminates is 2.
     * <p>
     * The algorithm treats the case where n is a power of two specially: it
     * returns the correct number of high-order bits from the underlying
     * pseudo-random number generator.  In the absence of special treatment,
     * the correct number of <i>low-order</i> bits would be returned.  Linear
     * congruential pseudo-random number generators such as the one
     * implemented by this class are known to have short periods in the
     * sequence of values of their low-order bits.  Thus, this special case
     * greatly increases the length of the sequence of values returned by
     * successive calls to this method if n is a small power of two.
     *
     * @param bound the upper bound (exclusive).  Must be positive.
     * @return the next pseudorandom, uniformly distributed {@code int}
     * value between zero (inclusive) and {@code bound} (exclusive)
     * from this random number generator's sequence
     * @throws IllegalArgumentException if bound is not positive
     * @since 1.2
     */
    public int nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");

        int r = next(31);
        int m = bound - 1;
        if ((bound & m) == 0)  // i.e., bound is a power of 2
            r = (int) ((bound * (long) r) >> 31);
        else {
            for (int u = r;
                 u - (r = u % bound) + m < 0;
                 u = next(31))
                ;
        }
        return r;
    }

    /**
     * Returns the next pseudorandom, uniformly distributed {@code long}
     * value from this random number generator's sequence. The general
     * contract of {@code nextLong} is that one {@code long} value is
     * pseudorandomly generated and returned.
     *
     * <p>The method {@code nextLong} is implemented by class {@code Random}
     * as if by:
     * <pre> {@code
     * public long nextLong() {
     *   return ((long)next(32) << 32) + next(32);
     * }}</pre>
     * <p>
     * Because class {@code Random} uses a seed with only 48 bits,
     * this algorithm will not return all possible {@code long} values.
     *
     * @return the next pseudorandom, uniformly distributed {@code long}
     * value from this random number generator's sequence
     */
    public long nextLong() {
        // it's okay that the bottom word remains signed.
        return ((long) (next(32)) << 32) + next(32);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed
     * {@code boolean} value from this random number generator's
     * sequence. The general contract of {@code nextBoolean} is that one
     * {@code boolean} value is pseudorandomly generated and returned.  The
     * values {@code true} and {@code false} are produced with
     * (approximately) equal probability.
     *
     * <p>The method {@code nextBoolean} is implemented by class {@code Random}
     * as if by:
     * <pre> {@code
     * public boolean nextBoolean() {
     *   return next(1) != 0;
     * }}</pre>
     *
     * @return the next pseudorandom, uniformly distributed
     * {@code boolean} value from this random number generator's
     * sequence
     * @since 1.2
     */
    public boolean nextBoolean() {
        return next(1) != 0;
    }

    /**
     * Returns the next pseudorandom, uniformly distributed {@code float}
     * value between {@code 0.0} and {@code 1.0} from this random
     * number generator's sequence.
     *
     * <p>The general contract of {@code nextFloat} is that one
     * {@code float} value, chosen (approximately) uniformly from the
     * range {@code 0.0f} (inclusive) to {@code 1.0f} (exclusive), is
     * pseudorandomly generated and returned. All 2<sup>24</sup> possible
     * {@code float} values of the form <i>m&nbsp;x&nbsp;</i>2<sup>-24</sup>,
     * where <i>m</i> is a positive integer less than 2<sup>24</sup>, are
     * produced with (approximately) equal probability.
     *
     * <p>The method {@code nextFloat} is implemented by class {@code Random}
     * as if by:
     * <pre> {@code
     * public float nextFloat() {
     *   return next(24) / ((float)(1 << 24));
     * }}</pre>
     *
     * <p>The hedge "approximately" is used in the foregoing description only
     * because the next method is only approximately an unbiased source of
     * independently chosen bits. If it were a perfect source of randomly
     * chosen bits, then the algorithm shown would choose {@code float}
     * values from the stated range with perfect uniformity.<p>
     * [In early versions of Java, the result was incorrectly calculated as:
     * <pre> {@code
     *   return next(30) / ((float)(1 << 30));}</pre>
     * This might seem to be equivalent, if not better, but in fact it
     * introduced a slight nonuniformity because of the bias in the rounding
     * of floating-point numbers: it was slightly more likely that the
     * low-order bit of the significand would be 0 than that it would be 1.]
     *
     * @return the next pseudorandom, uniformly distributed {@code float}
     * value between {@code 0.0} and {@code 1.0} from this
     * random number generator's sequence
     */
    public float nextFloat() {
        return next(24) / ((float) (1 << 24));
    }

    /**
     * Returns the next pseudorandom, uniformly distributed
     * {@code double} value between {@code 0.0} and
     * {@code 1.0} from this random number generator's sequence.
     *
     * <p>The general contract of {@code nextDouble} is that one
     * {@code double} value, chosen (approximately) uniformly from the
     * range {@code 0.0d} (inclusive) to {@code 1.0d} (exclusive), is
     * pseudorandomly generated and returned.
     *
     * <p>The method {@code nextDouble} is implemented by class {@code Random}
     * as if by:
     * <pre> {@code
     * public double nextDouble() {
     *   return (((long)next(26) << 27) + next(27))
     *     / (double)(1L << 53);
     * }}</pre>
     *
     * <p>The hedge "approximately" is used in the foregoing description only
     * because the {@code next} method is only approximately an unbiased
     * source of independently chosen bits. If it were a perfect source of
     * randomly chosen bits, then the algorithm shown would choose
     * {@code double} values from the stated range with perfect uniformity.
     * <p>[In early versions of Java, the result was incorrectly calculated as:
     * <pre> {@code
     *   return (((long)next(27) << 27) + next(27))
     *     / (double)(1L << 54);}</pre>
     * This might seem to be equivalent, if not better, but in fact it
     * introduced a large nonuniformity because of the bias in the rounding
     * of floating-point numbers: it was three times as likely that the
     * low-order bit of the significand would be 0 than that it would be 1!
     * This nonuniformity probably doesn't matter much in practice, but we
     * strive for perfection.]
     *
     * @return the next pseudorandom, uniformly distributed {@code double}
     * value between {@code 0.0} and {@code 1.0} from this
     * random number generator's sequence
     * @see Math#random
     */
    public double nextDouble() {
        return (((long) (next(26)) << 27) + next(27)) * 0x1.0p-53;
    }

    public final Float3 float3 = new Float3();

    public void nextWUV() {
        float t = (float) Math.sqrt(nextFloat());
        float u = 1 - t;
        float v = t * nextFloat();
        float3.x = t - v;
        float3.y = u;
        float3.z = v;
    }

    public void nextWUV(float a, float b, float c) {
        float s = a + b + c;
        float r = s * nextFloat();
        float t = (float) Math.pow(nextFloat(), 1f / 3);
        float u = 1 - t;
        float v = t * (1 - (float) Math.sqrt(nextFloat()));
        if (r < a) {
            float3.x = 1 - u - v;
            float3.y = u;
            float3.z = v;
        } else if (r < a + b) {
            float3.x = v;
            float3.y = 1 - u - v;
            float3.z = u;
        } else {
            float3.x = u;
            float3.y = v;
            float3.z = 1 - u - v;
        }
    }

    public void nextTarget() {
        float r = nextFloat() * 2 * (float) Math.PI;
        float z = nextFloat() * 2 - 1;
        float x = (float) Math.sin(r);
        float y = (float) Math.cos(r);
        float h = (float) Math.sqrt(1 - z * z);
        float3.x = x * h;
        float3.y = y * h;
        float3.z = z;
    }

    public float nextCosineTarget() {
        float pdf = nextCosineHalfTarget();
        if (nextBoolean()) {
            float3.z = -float3.z;
        }
        return pdf / 2;
    }

    public float nextCosineHalfTarget() {
        float t = nextFloat() * 2 * (float) Math.PI;
        float r2 = nextFloat();
        float r = (float) Math.sqrt(r2);
        float3.x = r * (float) Math.sin(t);
        float3.y = r * (float) Math.cos(t);
        float3.z = (float) Math.sqrt(1 - r2);
        return float3.z / (float) Math.PI;
    }

    public float nextCosineHalfTarget(float normalX, float normalY, float normalZ) {
        float normal = (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        normalX /= normal;
        normalY /= normal;
        normalZ /= normal;
        nextTarget();
        float x = float3.x;
        float y = float3.y;
        float z = float3.z;
        float r = x * normalX + y * normalY + z * normalZ;


        if (false) {
            if (r < 0) {
                x = -x;
                y = -y;
                z = -z;
            }
            float3.x = x;
            float3.y = y;
            float3.z = z;
            return 0.5f / (float) Math.PI;
        }


        r = Math.abs(r);
        x -= r * normalX;
        y -= r * normalY;
        z -= r * normalZ;
        float pR = (float) Math.sqrt(r / (1 - r * r));
        x *= pR;
        y *= pR;
        z *= pR;
        r = (float) Math.sqrt(1 - r);
        x += r * normalX;
        y += r * normalY;
        z += r * normalZ;
        float3.x = x;
        float3.y = y;
        float3.z = z;
        return r / (float) Math.PI;
    }


}