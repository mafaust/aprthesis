package org.apache.commons.math3.random;






















/**
 * Base class for random number generators that generates bits streams.
 *
 * @version $Id$
 * @since 2.0
 */
public abstract class BitsStreamGenerator implements org.apache.commons.math3.random.RandomGenerator , 
java.io.Serializable {
	/**
	 * Serializable version identifier
	 */ 	private static final long serialVersionUID = 20130104L; 	/**
	 * Next gaussian.
	 */ 	private double nextGaussian;
	/**
	 * Creates a new random number generator.
	 */
	public BitsStreamGenerator() {
		nextGaussian = java.lang.Double.NaN;
	}

	/**
	 * {@inheritDoc }
	 */ 	public abstract void setSeed(int seed);
	/**
	 * {@inheritDoc }
	 */ 	public abstract void setSeed(int[] seed);
	/**
	 * {@inheritDoc }
	 */ 	public abstract void setSeed(long seed);
	/**
	 * Generate next pseudorandom number.
	 * <p>This method is the core generation algorithm. It is used by all the
	 * public generation methods for the various primitive types {@link #nextBoolean()}, {@link #nextBytes(byte[])}, {@link #nextDouble()},
	 * {@link #nextFloat()}, {@link #nextGaussian()}, {@link #nextInt()},
	 * {@link #next(int)} and {@link #nextLong()}.</p>
	 *
	 * @param bits
	 * 		number of random bits to produce
	 * @return random bits generated
	 */ 	protected abstract int next(int bits);
	/**
	 * {@inheritDoc }
	 */ 	public boolean nextBoolean() { 		return next(1) != 0;
	}

	/**
	 * {@inheritDoc }
	 */ 	public void nextBytes(byte[] bytes) { 		int i = 0;
		final int iEnd = bytes.length - 3;
		while (i < iEnd) {
			final int random = next(32);
			bytes[i] = ((byte) (random & 0xff));
			bytes[i + 1] = ((byte) ((random >> 8) & 0xff));
			bytes[i + 2] = ((byte) ((random >> 16) & 0xff));
			bytes[i + 3] = ((byte) ((random >> 24) & 0xff));
			i += 4;
		} 
		int random = next(32);
		while (i < bytes.length) {
			bytes[i++] = ((byte) (random & 0xff));
			random = random >> 8;
		} 
	}

	/**
	 * {@inheritDoc }
	 */ 	public double nextDouble() { 		final long high = ((long) (next(26))) << 26;
		final int low = next(26);
		return (high | low) * 0x1.0p-52;
	}

	/**
	 * {@inheritDoc }
	 */ 	public float nextFloat() { 		return next(23) * 0x1.0p-23F;
	}

	/**
	 * {@inheritDoc }
	 */ 	public double nextGaussian() {
		final double random;
		if (java.lang.Double.isNaN(nextGaussian)) {
			// generate a new pair of gaussian numbers
			final double x = nextDouble();
			final double y = nextDouble();
			final double alpha = (2 * org.apache.commons.math3.util.FastMath.PI) * x;
			final double r = org.apache.commons.math3.util.FastMath.sqrt((-2) * org.apache.commons.math3.util.FastMath.log(y));
			random = r * org.apache.commons.math3.util.FastMath.cos(alpha);
			nextGaussian = r * org.apache.commons.math3.util.FastMath.sin(alpha);
		} else {
			// use the second element of the pair already generated
			random = nextGaussian;
			nextGaussian = java.lang.Double.NaN;
		}

		return random;

	}

	/**
	 * {@inheritDoc }
	 */ 	public int nextInt() { 		return next(32);
	}

	/**
	 * {@inheritDoc }
	 * <p>This default implementation is copied from Apache Harmony
	 * java.util.Random (r929253).</p>
	 *
	 * <p>Implementation notes: <ul>
	 * <li>If n is a power of 2, this method returns
	 * {@code (int) ((n * (long) next(31)) >> 31)}.</li>
	 *
	 * <li>If n is not a power of 2, what is returned is {@code next(31) % n}
	 * with {@code next(31)} values rejected (i.e. regenerated) until a
	 * value that is larger than the remainder of {@code Integer.MAX_VALUE / n}
	 * is generated. Rejection of this initial segment is necessary to ensure
	 * a uniform distribution.</li></ul></p>
	 */
	public int nextInt(int n) throws java.lang.IllegalArgumentException {
		if (n > 0) {
			if ((n & (-n)) == n) {
				return ((int) ((n * ((long) (next(31)))) >> 31));
			}
			int bits;
			int val;
			do {
				bits = next(31);
				val = bits % n;
			} while (((bits - val) + (n - 1)) < 0 );
			return val;
		}
		throw new org.apache.commons.math3.exception.NotStrictlyPositiveException(n);
	}

	/**
	 * {@inheritDoc }
	 */ 	public long nextLong() { 		final long high = ((long) (next(32))) << 32;
		final long low = ((long) (next(32))) & 0xffffffffL;
		return high | low;
	}

	/**
	 * Returns a pseudorandom, uniformly distributed <tt>long</tt> value
	 * between 0 (inclusive) and the specified value (exclusive), drawn from
	 * this random number generator's sequence.
	 *
	 * @param n
	 * 		the bound on the random number to be returned.  Must be
	 * 		positive.
	 * @return a pseudorandom, uniformly distributed <tt>long</tt>
	value between 0 (inclusive) and n (exclusive).
	 * @throws IllegalArgumentException
	 * 		if n is not positive.
	 */ 	public long nextLong(long n) throws java.lang.IllegalArgumentException { 		if (n > 0) {
			long bits;
			long val;
			do {
				bits = ((long) (next(31))) << 32;
				bits = bits | (((long) (next(32))) & 0xffffffffL);
				val = bits % n;
			} while (((bits - val) + (n - 1)) < 0 );
			return val;
		}
		throw new org.apache.commons.math3.exception.NotStrictlyPositiveException(n);
	}

	/**
	 * Clears the cache used by the default implementation of
	 * {@link #nextGaussian}.
	 */
	public void clear() {
		nextGaussian = java.lang.Double.NaN;
	}}