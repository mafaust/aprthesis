package org.apache.commons.math3.random;
/**
 * Implements the {@link RandomData} interface using a {@link RandomGenerator}
 * instance to generate non-secure data and a {@link java.security.SecureRandom}
 * instance to provide data for the <code>nextSecureXxx</code> methods. If no
 * <code>RandomGenerator</code> is provided in the constructor, the default is
 * to use a {@link Well19937c} generator. To plug in a different
 * implementation, either implement <code>RandomGenerator</code> directly or
 * extend {@link AbstractRandomGenerator}.
 * <p>
 * Supports reseeding the underlying pseudo-random number generator (PRNG). The
 * <code>SecurityProvider</code> and <code>Algorithm</code> used by the
 * <code>SecureRandom</code> instance can also be reset.
 * </p>
 * <p>
 * For details on the default PRNGs, see {@link java.util.Random} and
 * {@link java.security.SecureRandom}.
 * </p>
 * <p>
 * <strong>Usage Notes</strong>:
 * <ul>
 * <li>
 * Instance variables are used to maintain <code>RandomGenerator</code> and
 * <code>SecureRandom</code> instances used in data generation. Therefore, to
 * generate a random sequence of values or strings, you should use just
 * <strong>one</strong> <code>RandomDataImpl</code> instance repeatedly.</li>
 * <li>
 * The "secure" methods are *much* slower. These should be used only when a
 * cryptographically secure random sequence is required. A secure random
 * sequence is a sequence of pseudo-random values which, in addition to being
 * well-dispersed (so no subsequence of values is an any more likely than other
 * subsequence of the the same length), also has the additional property that
 * knowledge of values generated up to any point in the sequence does not make
 * it any easier to predict subsequent values.</li>
 * <li>
 * When a new <code>RandomDataImpl</code> is created, the underlying random
 * number generators are <strong>not</strong> initialized. If you do not
 * explicitly seed the default non-secure generator, it is seeded with the
 * current time in milliseconds plus the system identity hash code on first use.
 * The same holds for the secure generator. If you provide a <code>RandomGenerator</code>
 * to the constructor, however, this generator is not reseeded by the constructor
 * nor is it reseeded on first use.</li>
 * <li>
 * The <code>reSeed</code> and <code>reSeedSecure</code> methods delegate to the
 * corresponding methods on the underlying <code>RandomGenerator</code> and
 * <code>SecureRandom</code> instances. Therefore, <code>reSeed(long)</code>
 * fully resets the initial state of the non-secure random number generator (so
 * that reseeding with a specific value always results in the same subsequent
 * random sequence); whereas reSeedSecure(long) does <strong>not</strong>
 * reinitialize the secure random number generator (so secure sequences started
 * with calls to reseedSecure(long) won't be identical).</li>
 * <li>
 * This implementation is not synchronized. The underlying <code>RandomGenerator</code>
 * or <code>SecureRandom</code> instances are not protected by synchronization and
 * are not guaranteed to be thread-safe.  Therefore, if an instance of this class
 * is concurrently utilized by multiple threads, it is the responsibility of
 * client code to synchronize access to seeding and data generation methods.
 * </li>
 * </ul>
 * </p>
 *
 * @since 3.1
 * @version $Id$
 */
public class RandomDataGenerator implements org.apache.commons.math3.random.RandomData , java.io.Serializable {
	/**
	 * Serializable version identifier
	 */
	private static final long serialVersionUID = -626730818244969716L;

	/**
	 * underlying random number generator
	 */
	private org.apache.commons.math3.random.RandomGenerator rand = null;

	/**
	 * underlying secure random number generator
	 */
	private org.apache.commons.math3.random.RandomGenerator secRand = null;

	/**
	 * Construct a RandomDataGenerator, using a default random generator as the source
	 * of randomness.
	 *
	 * <p>The default generator is a {@link Well19937c} seeded
	 * with {@code System.currentTimeMillis() + System.identityHashCode(this))}.
	 * The generator is initialized and seeded on first use.</p>
	 */
	public RandomDataGenerator() {
	}

	/**
	 * Construct a RandomDataGenerator using the supplied {@link RandomGenerator} as
	 * the source of (non-secure) random data.
	 *
	 * @param rand
	 * 		the source of (non-secure) random data
	 * 		(may be null, resulting in the default generator)
	 */
	public RandomDataGenerator(org.apache.commons.math3.random.RandomGenerator rand) {
		this.rand = rand;
	}

	/**
	 * {@inheritDoc }
	 * <p>
	 * <strong>Algorithm Description:</strong> hex strings are generated using a
	 * 2-step process.
	 * <ol>
	 * <li>{@code len / 2 + 1} binary bytes are generated using the underlying
	 * Random</li>
	 * <li>Each binary byte is translated into 2 hex digits</li>
	 * </ol>
	 * </p>
	 *
	 * @param len
	 * 		the desired string length.
	 * @return the random string.
	 * @throws NotStrictlyPositiveException
	 * 		if {@code len <= 0}.
	 */
	public java.lang.String nextHexString(int len) throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
		if (len <= 0) {
			throw new org.apache.commons.math3.exception.NotStrictlyPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.LENGTH, len);
		}
		// Get a random number generator
		org.apache.commons.math3.random.RandomGenerator ran = getRandomGenerator();
		// Initialize output buffer
		java.lang.StringBuilder outBuffer = new java.lang.StringBuilder();
		// Get int(len/2)+1 random bytes
		byte[] randomBytes = new byte[(len / 2) + 1];
		ran.nextBytes(randomBytes);
		// Convert each byte to 2 hex digits
		for (int i = 0; i < randomBytes.length; i++) {
			java.lang.Integer c = java.lang.Integer.valueOf(randomBytes[i]);
			/* Add 128 to byte value to make interval 0-255 before doing hex
			conversion. This guarantees <= 2 hex digits from toHexString()
			toHexString would otherwise add 2^32 to negative arguments.
			 */
			java.lang.String hex = java.lang.Integer.toHexString(c.intValue() + 128);
			// Make sure we add 2 hex digits for each byte
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			outBuffer.append(hex);
		}
		return outBuffer.toString().substring(0, len);
	}

	/**
	 * {@inheritDoc }
	 */
	public int nextInt(final int lower, final int upper) throws org.apache.commons.math3.exception.NumberIsTooLargeException {
		return new org.apache.commons.math3.distribution.UniformIntegerDistribution(getRandomGenerator(), lower, upper).sample();
	}

	/**
	 * {@inheritDoc }
	 */
	public long nextLong(final long lower, final long upper) throws org.apache.commons.math3.exception.NumberIsTooLargeException {
		if (lower >= upper) {
			throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, lower, upper, false);
		}
		final long max = (upper - lower) + 1;
		if (max <= 0) {
			// the range is too wide to fit in a positive long (larger than 2^63); as it covers
			// more than half the long range, we use directly a simple rejection method
			final org.apache.commons.math3.random.RandomGenerator rng = getRandomGenerator();
			while (true) {
				final long r = rng.nextLong();
				if ((r >= lower) && (r <= upper)) {
					return r;
				}
			} 
		} else if (max < java.lang.Integer.MAX_VALUE) {
			// we can shift the range and generate directly a positive int
			return lower + getRandomGenerator().nextInt(((int) (max)));
		} else {
			// we can shift the range and generate directly a positive long
			return lower + org.apache.commons.math3.random.RandomDataGenerator.nextLong(getRandomGenerator(), max);
		}
	}

	/**
	 * Returns a pseudorandom, uniformly distributed <tt>long</tt> value
	 * between 0 (inclusive) and the specified value (exclusive), drawn from
	 * this random number generator's sequence.
	 *
	 * @param rng
	 * 		random generator to use
	 * @param n
	 * 		the bound on the random number to be returned.  Must be
	 * 		positive.
	 * @return a pseudorandom, uniformly distributed <tt>long</tt>
	value between 0 (inclusive) and n (exclusive).
	 * @throws IllegalArgumentException
	 * 		if n is not positive.
	 */
	private static long nextLong(final org.apache.commons.math3.random.RandomGenerator rng, final long n) throws java.lang.IllegalArgumentException {
		if (n > 0) {
			final byte[] byteArray = new byte[8];
			long bits;
			long val;
			do {
				rng.nextBytes(byteArray);
				bits = 0;
				for (final byte b : byteArray) {
					bits = (bits << 8) | (((long) (b)) & 0xffL);
				}
				bits = bits & 0x7fffffffffffffffL;
				val = bits % n;
			} while (((bits - val) + (n - 1)) < 0 );
			return val;
		}
		throw new org.apache.commons.math3.exception.NotStrictlyPositiveException(n);
	}

	/**
	 * {@inheritDoc }
	 * <p>
	 * <strong>Algorithm Description:</strong> hex strings are generated in
	 * 40-byte segments using a 3-step process.
	 * <ol>
	 * <li>
	 * 20 random bytes are generated using the underlying
	 * <code>SecureRandom</code>.</li>
	 * <li>
	 * SHA-1 hash is applied to yield a 20-byte binary digest.</li>
	 * <li>
	 * Each byte of the binary digest is converted to 2 hex digits.</li>
	 * </ol>
	 * </p>
	 *
	 * @throws NotStrictlyPositiveException
	 * 		if {@code len <= 0}
	 */
	public java.lang.String nextSecureHexString(int len) throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
		if (len <= 0) {
			throw new org.apache.commons.math3.exception.NotStrictlyPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.LENGTH, len);
		}
		// Get SecureRandom and setup Digest provider
		final org.apache.commons.math3.random.RandomGenerator secRan = getSecRan();
		java.security.MessageDigest alg = null;
		try {
			alg = java.security.MessageDigest.getInstance("SHA-1");
		} catch (java.security.NoSuchAlgorithmException ex) {
			// this should never happen
			throw new org.apache.commons.math3.exception.MathInternalError(ex);
		}
		alg.reset();
		// Compute number of iterations required (40 bytes each)
		int numIter = (len / 40) + 1;
		java.lang.StringBuilder outBuffer = new java.lang.StringBuilder();
		for (int iter = 1; iter < (numIter + 1); iter++) {
			byte[] randomBytes = new byte[40];
			secRan.nextBytes(randomBytes);
			alg.update(randomBytes);
			// Compute hash -- will create 20-byte binary hash
			byte[] hash = alg.digest();
			// Loop over the hash, converting each byte to 2 hex digits
			for (int i = 0; i < hash.length; i++) {
				java.lang.Integer c = java.lang.Integer.valueOf(hash[i]);
				/* Add 128 to byte value to make interval 0-255 This guarantees
				<= 2 hex digits from toHexString() toHexString would
				otherwise add 2^32 to negative arguments
				 */
				java.lang.String hex = java.lang.Integer.toHexString(c.intValue() + 128);
				// Keep strings uniform length -- guarantees 40 bytes
				if (hex.length() == 1) {
					hex = "0" + hex;
				}
				outBuffer.append(hex);
			}
		}
		return outBuffer.toString().substring(0, len);
	}

	/**
	 * {@inheritDoc }
	 */
	public int nextSecureInt(final int lower, final int upper) throws org.apache.commons.math3.exception.NumberIsTooLargeException {
		return new org.apache.commons.math3.distribution.UniformIntegerDistribution(getSecRan(), lower, upper).sample();
	}

	/**
	 * {@inheritDoc }
	 */
	public long nextSecureLong(final long lower, final long upper) throws org.apache.commons.math3.exception.NumberIsTooLargeException {
		if (lower >= upper) {
			throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, lower, upper, false);
		}
		final org.apache.commons.math3.random.RandomGenerator rng = getSecRan();
		final long max = (upper - lower) + 1;
		if (max <= 0) {
			// the range is too wide to fit in a positive long (larger than 2^63); as it covers
			// more than half the long range, we use directly a simple rejection method
			while (true) {
				final long r = rng.nextLong();
				if ((r >= lower) && (r <= upper)) {
					return r;
				}
			} 
		} else if (max < java.lang.Integer.MAX_VALUE) {
			// we can shift the range and generate directly a positive int
			return lower + rng.nextInt(((int) (max)));
		} else {
			// we can shift the range and generate directly a positive long
			return lower + org.apache.commons.math3.random.RandomDataGenerator.nextLong(rng, max);
		}
	}

	/**
	 * {@inheritDoc }
	 * <p>
	 * <strong>Algorithm Description</strong>:
	 * <ul><li> For small means, uses simulation of a Poisson process
	 * using Uniform deviates, as described
	 * <a href="http://irmi.epfl.ch/cmos/Pmmi/interactive/rng7.htm"> here.</a>
	 * The Poisson process (and hence value returned) is bounded by 1000 * mean.</li>
	 *
	 * <li> For large means, uses the rejection algorithm described in <br/>
	 * Devroye, Luc. (1981).<i>The Computer Generation of Poisson Random Variables</i>
	 * <strong>Computing</strong> vol. 26 pp. 197-207.</li></ul></p>
	 *
	 * @throws NotStrictlyPositiveException
	 * 		if {@code len <= 0}
	 */
	public long nextPoisson(double mean) throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
		return new org.apache.commons.math3.distribution.PoissonDistribution(getRandomGenerator(), mean, org.apache.commons.math3.distribution.PoissonDistribution.DEFAULT_EPSILON, org.apache.commons.math3.distribution.PoissonDistribution.DEFAULT_MAX_ITERATIONS).sample();
	}

	/**
	 * {@inheritDoc }
	 */
	public double nextGaussian(double mu, double sigma) throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
		if (sigma <= 0) {
			throw new org.apache.commons.math3.exception.NotStrictlyPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.STANDARD_DEVIATION, sigma);
		}
		return (sigma * getRandomGenerator().nextGaussian()) + mu;
	}

	/**
	 * {@inheritDoc }
	 *
	 * <p>
	 * <strong>Algorithm Description</strong>: Uses the Algorithm SA (Ahrens)
	 * from p. 876 in:
	 * [1]: Ahrens, J. H. and Dieter, U. (1972). Computer methods for
	 * sampling from the exponential and normal distributions.
	 * Communications of the ACM, 15, 873-882.
	 * </p>
	 */
	public double nextExponential(double mean) throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
		return new org.apache.commons.math3.distribution.ExponentialDistribution(getRandomGenerator(), mean, org.apache.commons.math3.distribution.ExponentialDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
	}

	/**
	 * <p>Generates a random value from the
	 * {@link org.apache.commons.math3.distribution.GammaDistribution Gamma Distribution}.</p>
	 *
	 * <p>This implementation uses the following algorithms: </p>
	 *
	 * <p>For 0 < shape < 1: <br/>
	 * Ahrens, J. H. and Dieter, U., <i>Computer methods for
	 * sampling from gamma, beta, Poisson and binomial distributions.</i>
	 * Computing, 12, 223-246, 1974.</p>
	 *
	 * <p>For shape >= 1: <br/>
	 * Marsaglia and Tsang, <i>A Simple Method for Generating
	 * Gamma Variables.</i> ACM Transactions on Mathematical Software,
	 * Volume 26 Issue 3, September, 2000.</p>
	 *
	 * @param shape
	 * 		the median of the Gamma distribution
	 * @param scale
	 * 		the scale parameter of the Gamma distribution
	 * @return random value sampled from the Gamma(shape, scale) distribution
	 * @throws NotStrictlyPositiveException
	 * 		if {@code shape <= 0} or
	 * 		{@code scale <= 0}.
	 */
	public double nextGamma(double shape, double scale) throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
		return new org.apache.commons.math3.distribution.GammaDistribution(getRandomGenerator(), shape, scale, org.apache.commons.math3.distribution.GammaDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
	}

	/**
	 * Generates a random value from the {@link HypergeometricDistribution Hypergeometric Distribution}.
	 *
	 * @param populationSize
	 * 		the population size of the Hypergeometric distribution
	 * @param numberOfSuccesses
	 * 		number of successes in the population of the Hypergeometric distribution
	 * @param sampleSize
	 * 		the sample size of the Hypergeometric distribution
	 * @return random value sampled from the Hypergeometric(numberOfSuccesses, sampleSize) distribution
	 * @throws NumberIsTooLargeException
	 * 		if {@code numberOfSuccesses > populationSize},
	 * 		or {@code sampleSize > populationSize}.
	 * @throws NotStrictlyPositiveException
	 * 		if {@code populationSize <= 0}.
	 * @throws NotPositiveException
	 * 		if {@code numberOfSuccesses < 0}.
	 */
	public int nextHypergeometric(int populationSize, int numberOfSuccesses, int sampleSize) throws org.apache.commons.math3.exception.NotPositiveException, org.apache.commons.math3.exception.NotStrictlyPositiveException, org.apache.commons.math3.exception.NumberIsTooLargeException {
		return new org.apache.commons.math3.distribution.HypergeometricDistribution(getRandomGenerator(), populationSize, numberOfSuccesses, sampleSize).sample();
	}

	/**
	 * Generates a random value from the {@link PascalDistribution Pascal Distribution}.
	 *
	 * @param r
	 * 		the number of successes of the Pascal distribution
	 * @param p
	 * 		the probability of success of the Pascal distribution
	 * @return random value sampled from the Pascal(r, p) distribution
	 * @throws NotStrictlyPositiveException
	 * 		if the number of successes is not positive
	 * @throws OutOfRangeException
	 * 		if the probability of success is not in the
	 * 		range {@code [0, 1]}.
	 */
	public int nextPascal(int r, double p) throws org.apache.commons.math3.exception.NotStrictlyPositiveException, org.apache.commons.math3.exception.OutOfRangeException {
		return new org.apache.commons.math3.distribution.PascalDistribution(getRandomGenerator(), r, p).sample();
	}

	/**
	 * Generates a random value from the {@link TDistribution T Distribution}.
	 *
	 * @param df
	 * 		the degrees of freedom of the T distribution
	 * @return random value from the T(df) distribution
	 * @throws NotStrictlyPositiveException
	 * 		if {@code df <= 0}
	 */
	public double nextT(double df) throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
		return new org.apache.commons.math3.distribution.TDistribution(getRandomGenerator(), df, org.apache.commons.math3.distribution.TDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
	}

	/**
	 * Generates a random value from the {@link WeibullDistribution Weibull Distribution}.
	 *
	 * @param shape
	 * 		the shape parameter of the Weibull distribution
	 * @param scale
	 * 		the scale parameter of the Weibull distribution
	 * @return random value sampled from the Weibull(shape, size) distribution
	 * @throws NotStrictlyPositiveException
	 * 		if {@code shape <= 0} or
	 * 		{@code scale <= 0}.
	 */
	public double nextWeibull(double shape, double scale) throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
		return new org.apache.commons.math3.distribution.WeibullDistribution(getRandomGenerator(), shape, scale, org.apache.commons.math3.distribution.WeibullDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
	}

	/**
	 * Generates a random value from the {@link ZipfDistribution Zipf Distribution}.
	 *
	 * @param numberOfElements
	 * 		the number of elements of the ZipfDistribution
	 * @param exponent
	 * 		the exponent of the ZipfDistribution
	 * @return random value sampled from the Zipf(numberOfElements, exponent) distribution
	 * @exception NotStrictlyPositiveException
	 * 		if {@code numberOfElements <= 0}
	 * 		or {@code exponent <= 0}.
	 */
	public int nextZipf(int numberOfElements, double exponent) throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
		return new org.apache.commons.math3.distribution.ZipfDistribution(getRandomGenerator(), numberOfElements, exponent).sample();
	}

	/**
	 * Generates a random value from the {@link BetaDistribution Beta Distribution}.
	 *
	 * @param alpha
	 * 		first distribution shape parameter
	 * @param beta
	 * 		second distribution shape parameter
	 * @return random value sampled from the beta(alpha, beta) distribution
	 */
	public double nextBeta(double alpha, double beta) {
		return new org.apache.commons.math3.distribution.BetaDistribution(getRandomGenerator(), alpha, beta, org.apache.commons.math3.distribution.BetaDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
	}

	/**
	 * Generates a random value from the {@link BinomialDistribution Binomial Distribution}.
	 *
	 * @param numberOfTrials
	 * 		number of trials of the Binomial distribution
	 * @param probabilityOfSuccess
	 * 		probability of success of the Binomial distribution
	 * @return random value sampled from the Binomial(numberOfTrials, probabilityOfSuccess) distribution
	 */
	public int nextBinomial(int numberOfTrials, double probabilityOfSuccess) {
		return new org.apache.commons.math3.distribution.BinomialDistribution(getRandomGenerator(), numberOfTrials, probabilityOfSuccess).sample();
	}

	/**
	 * Generates a random value from the {@link CauchyDistribution Cauchy Distribution}.
	 *
	 * @param median
	 * 		the median of the Cauchy distribution
	 * @param scale
	 * 		the scale parameter of the Cauchy distribution
	 * @return random value sampled from the Cauchy(median, scale) distribution
	 */
	public double nextCauchy(double median, double scale) {
		return new org.apache.commons.math3.distribution.CauchyDistribution(getRandomGenerator(), median, scale, org.apache.commons.math3.distribution.CauchyDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
	}

	/**
	 * Generates a random value from the {@link ChiSquaredDistribution ChiSquare Distribution}.
	 *
	 * @param df
	 * 		the degrees of freedom of the ChiSquare distribution
	 * @return random value sampled from the ChiSquare(df) distribution
	 */
	public double nextChiSquare(double df) {
		return new org.apache.commons.math3.distribution.ChiSquaredDistribution(getRandomGenerator(), df, org.apache.commons.math3.distribution.ChiSquaredDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
	}

	/**
	 * Generates a random value from the {@link FDistribution F Distribution}.
	 *
	 * @param numeratorDf
	 * 		the numerator degrees of freedom of the F distribution
	 * @param denominatorDf
	 * 		the denominator degrees of freedom of the F distribution
	 * @return random value sampled from the F(numeratorDf, denominatorDf) distribution
	 * @throws NotStrictlyPositiveException
	 * 		if
	 * 		{@code numeratorDf <= 0} or {@code denominatorDf <= 0}.
	 */
	public double nextF(double numeratorDf, double denominatorDf) throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
		return new org.apache.commons.math3.distribution.FDistribution(getRandomGenerator(), numeratorDf, denominatorDf, org.apache.commons.math3.distribution.FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
	}

	/**
	 * {@inheritDoc }
	 *
	 * <p>
	 * <strong>Algorithm Description</strong>: scales the output of
	 * Random.nextDouble(), but rejects 0 values (i.e., will generate another
	 * random double if Random.nextDouble() returns 0). This is necessary to
	 * provide a symmetric output interval (both endpoints excluded).
	 * </p>
	 *
	 * @throws NumberIsTooLargeException
	 * 		if {@code lower >= upper}
	 * @throws NotFiniteNumberException
	 * 		if one of the bounds is infinite
	 * @throws NotANumberException
	 * 		if one of the bounds is NaN
	 */
	public double nextUniform(double lower, double upper) throws org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.NotFiniteNumberException, org.apache.commons.math3.exception.NotANumberException {
		return nextUniform(lower, upper, false);
	}

	/**
	 * {@inheritDoc }
	 *
	 * <p>
	 * <strong>Algorithm Description</strong>: if the lower bound is excluded,
	 * scales the output of Random.nextDouble(), but rejects 0 values (i.e.,
	 * will generate another random double if Random.nextDouble() returns 0).
	 * This is necessary to provide a symmetric output interval (both
	 * endpoints excluded).
	 * </p>
	 *
	 * @throws NumberIsTooLargeException
	 * 		if {@code lower >= upper}
	 * @throws NotFiniteNumberException
	 * 		if one of the bounds is infinite
	 * @throws NotANumberException
	 * 		if one of the bounds is NaN
	 */
	public double nextUniform(double lower, double upper, boolean lowerInclusive) throws org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.NotFiniteNumberException, org.apache.commons.math3.exception.NotANumberException {
		if (lower >= upper) {
			throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, lower, upper, false);
		}
		if (java.lang.Double.isInfinite(lower)) {
			throw new org.apache.commons.math3.exception.NotFiniteNumberException(org.apache.commons.math3.exception.util.LocalizedFormats.INFINITE_BOUND, lower);
		}
		if (java.lang.Double.isInfinite(upper)) {
			throw new org.apache.commons.math3.exception.NotFiniteNumberException(org.apache.commons.math3.exception.util.LocalizedFormats.INFINITE_BOUND, upper);
		}
		if (java.lang.Double.isNaN(lower) || java.lang.Double.isNaN(upper)) {
			throw new org.apache.commons.math3.exception.NotANumberException();
		}
		final org.apache.commons.math3.random.RandomGenerator generator = getRandomGenerator();
		// ensure nextDouble() isn't 0.0
		double u = generator.nextDouble();
		while ((!lowerInclusive) && (u <= 0.0)) {
			u = generator.nextDouble();
		} 
		return (u * upper) + ((1.0 - u) * lower);
	}

	/**
	 * {@inheritDoc }
	 *
	 * <p>
	 * Uses a 2-cycle permutation shuffle. The shuffling process is described <a
	 * href="http://www.maths.abdn.ac.uk/~igc/tch/mx4002/notes/node83.html">
	 * here</a>.
	 * </p>
	 *
	 * @throws NumberIsTooLargeException
	 * 		if {@code k > n}.
	 * @throws NotStrictlyPositiveException
	 * 		if {@code k <= 0}.
	 */
	public int[] nextPermutation(int n, int k) throws org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.NotStrictlyPositiveException {
		if (k > n) {
			throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.PERMUTATION_EXCEEDS_N, k, n, true);
		}
		if (k <= 0) {
			throw new org.apache.commons.math3.exception.NotStrictlyPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.PERMUTATION_SIZE, k);
		}
		int[] index = getNatural(n);
		org.apache.commons.math3.util.MathArrays.shuffle(index, getRandomGenerator());
		// Return a new array containing the first "k" entries of "index".
		return org.apache.commons.math3.util.MathArrays.copyOf(index, k);
	}

	/**
	 * {@inheritDoc }
	 *
	 * <p>
	 * <strong>Algorithm Description</strong>: Uses a 2-cycle permutation
	 * shuffle to generate a random permutation of <code>c.size()</code> and
	 * then returns the elements whose indexes correspond to the elements of the
	 * generated permutation. This technique is described, and proven to
	 * generate random samples <a
	 * href="http://www.maths.abdn.ac.uk/~igc/tch/mx4002/notes/node83.html">
	 * here</a>
	 * </p>
	 */
	public java.lang.Object[] nextSample(java.util.Collection<?> c, int k) throws org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.NotStrictlyPositiveException {
		int len = c.size();
		if (k > len) {
			throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.SAMPLE_SIZE_EXCEEDS_COLLECTION_SIZE, k, len, true);
		}
		if (k <= 0) {
			throw new org.apache.commons.math3.exception.NotStrictlyPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.NUMBER_OF_SAMPLES, k);
		}
		java.lang.Object[] objects = c.toArray();
		int[] index = nextPermutation(len, k);
		java.lang.Object[] result = new java.lang.Object[k];
		for (int i = 0; i < k; i++) {
			result[i] = objects[index[i]];
		}
		return result;
	}

	/**
	 * Reseeds the random number generator with the supplied seed.
	 * <p>
	 * Will create and initialize if null.
	 * </p>
	 *
	 * @param seed
	 * 		the seed value to use
	 */
	public void reSeed(long seed) {
		getRandomGenerator().setSeed(seed);
	}

	/**
	 * Reseeds the secure random number generator with the current time in
	 * milliseconds.
	 * <p>
	 * Will create and initialize if null.
	 * </p>
	 */
	public void reSeedSecure() {
		getSecRan().setSeed(java.lang.System.currentTimeMillis());
	}

	/**
	 * Reseeds the secure random number generator with the supplied seed.
	 * <p>
	 * Will create and initialize if null.
	 * </p>
	 *
	 * @param seed
	 * 		the seed value to use
	 */
	public void reSeedSecure(long seed) {
		getSecRan().setSeed(seed);
	}

	/**
	 * Reseeds the random number generator with
	 * {@code System.currentTimeMillis() + System.identityHashCode(this))}.
	 */
	public void reSeed() {
		getRandomGenerator().setSeed(java.lang.System.currentTimeMillis() + java.lang.System.identityHashCode(this));
	}

	/**
	 * Sets the PRNG algorithm for the underlying SecureRandom instance using
	 * the Security Provider API. The Security Provider API is defined in <a
	 * href =
	 * "http://java.sun.com/j2se/1.3/docs/guide/security/CryptoSpec.html#AppA">
	 * Java Cryptography Architecture API Specification & Reference.</a>
	 * <p>
	 * <strong>USAGE NOTE:</strong> This method carries <i>significant</i>
	 * overhead and may take several seconds to execute.
	 * </p>
	 *
	 * @param algorithm
	 * 		the name of the PRNG algorithm
	 * @param provider
	 * 		the name of the provider
	 * @throws NoSuchAlgorithmException
	 * 		if the specified algorithm is not available
	 * @throws NoSuchProviderException
	 * 		if the specified provider is not installed
	 */
	public void setSecureAlgorithm(java.lang.String algorithm, java.lang.String provider) throws java.security.NoSuchAlgorithmException, java.security.NoSuchProviderException {
		secRand = org.apache.commons.math3.random.RandomGeneratorFactory.createRandomGenerator(java.security.SecureRandom.getInstance(algorithm, provider));
	}

	/**
	 * Returns the RandomGenerator used to generate non-secure random data.
	 * <p>
	 * Creates and initializes a default generator if null. Uses a {@link Well19937c}
	 * generator with {@code System.currentTimeMillis() + System.identityHashCode(this))}
	 * as the default seed.
	 * </p>
	 *
	 * @return the Random used to generate random data
	 * @since 3.2
	 */
	public org.apache.commons.math3.random.RandomGenerator getRandomGenerator() {
		if (rand == null) {
			initRan();
		}
		return rand;
	}

	/**
	 * Sets the default generator to a {@link Well19937c} generator seeded with
	 * {@code System.currentTimeMillis() + System.identityHashCode(this))}.
	 */
	private void initRan() {
		rand = new org.apache.commons.math3.random.Well19937c(java.lang.System.currentTimeMillis() + java.lang.System.identityHashCode(this));
	}

	/**
	 * Returns the SecureRandom used to generate secure random data.
	 * <p>
	 * Creates and initializes if null.  Uses
	 * {@code System.currentTimeMillis() + System.identityHashCode(this)} as the default seed.
	 * </p>
	 *
	 * @return the SecureRandom used to generate secure random data, wrapped in a
	{@link RandomGenerator}.
	 */
	private org.apache.commons.math3.random.RandomGenerator getSecRan() {
		if (secRand == null) {
			secRand = org.apache.commons.math3.random.RandomGeneratorFactory.createRandomGenerator(new java.security.SecureRandom());
			secRand.setSeed(java.lang.System.currentTimeMillis() + java.lang.System.identityHashCode(this));
		}
		return secRand;
	}

	/**
	 * Uses a 2-cycle permutation shuffle to randomly re-order the last elements
	 * of list.
	 *
	 * @param list
	 * 		list to be shuffled
	 * @param end
	 * 		element past which shuffling begins
	 */
	private void shuffle(int[] list, int end) {
		int target = 0;
		for (int i = list.length - 1; i >= end; i--) {
			if (i == 0) {
				target = 0;
			} else {
				// NumberIsTooLargeException cannot occur
				target = nextInt(0, i);
			}
			int temp = list[target];
			list[target] = list[i];
			list[i] = temp;
		}
	}

	/**
	 * Returns an array representing n.
	 *
	 * @param n
	 * 		the natural number to represent
	 * @return array with entries = elements of n
	 */
	private int[] getNatural(int n) {
		int[] natural = new int[n];
		for (int i = 0; i < n; i++) {
			natural[i] = i;
		}
		return natural;
	}
}