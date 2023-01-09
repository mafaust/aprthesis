package org.apache.commons.math3.distribution;




























/**
 * Base class for integer-valued discrete distributions.  Default
 * implementations are provided for some of the methods that do not vary
 * from distribution to distribution.
 *
 * @version $Id$
 */
public abstract class AbstractIntegerDistribution implements org.apache.commons.math3.distribution.IntegerDistribution , java.io.Serializable {

	/**
	 * Serializable version identifier
	 */ 	private static final long serialVersionUID = -1146319659338487221L;
	/**
	 * RandomData instance used to generate samples from the distribution.
	 *
	 * @deprecated As of 3.1, to be removed in 4.0. Please use the
	{@link #random} instance variable instead.
	 */ 	@java.lang.Deprecated
	protected final org.apache.commons.math3.random.RandomDataImpl randomData = new org.apache.commons.math3.random.RandomDataImpl();

	/**
	 * RNG instance used to generate samples from the distribution.
	 *
	 * @since 3.1
	 */ 	protected final org.apache.commons.math3.random.RandomGenerator random;

	/**
	 *
	 *
	 * @deprecated As of 3.1, to be removed in 4.0. Please use
	{@link #AbstractIntegerDistribution(RandomGenerator)} instead.
	 */ 	@java.lang.Deprecated 	protected AbstractIntegerDistribution() {
		// Legacy users are only allowed to access the deprecated "randomData".
		// New users are forbidden to use this constructor.
		random = null;
	}

	/**
	 *
	 *
	 * @param rng
	 * 		Random number generator.
	 * @since 3.1
	 */ 	protected AbstractIntegerDistribution(org.apache.commons.math3.random.RandomGenerator rng) { 		random = rng;}

	/**
	 * {@inheritDoc }
	 *
	 * The default implementation uses the identity
	 * <p>{@code P(x0 < X <= x1) = P(X <= x1) - P(X <= x0)}</p>
	 */
	public double cumulativeProbability(int x0, int x1) throws org.apache.commons.math3.exception.NumberIsTooLargeException {
		if (x1 < x0) {
			throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, 
			x0, x1, true);
		}
		return cumulativeProbability(x1) - cumulativeProbability(x0);
	}

	/**
	 * {@inheritDoc }
	 *
	 * The default implementation returns
	 * <ul>
	 * <li>{@link #getSupportLowerBound()} for {@code p = 0},</li>
	 * <li>{@link #getSupportUpperBound()} for {@code p = 1}, and</li>
	 * <li>{@link #solveInverseCumulativeProbability(double, int, int)} for
	 *     {@code 0 < p < 1}.</li>
	 * </ul>
	 */
	public int inverseCumulativeProbability(final double p) throws org.apache.commons.math3.exception.OutOfRangeException {
		if ((p < 0.0) || (p > 1.0)) {
			throw new org.apache.commons.math3.exception.OutOfRangeException(p, 0, 1);
		}

		int lower = getSupportLowerBound();
		if (p == 0.0) {
			return lower;
		}
		if (lower == java.lang.Integer.MIN_VALUE) {
			if (checkedCumulativeProbability(lower) >= p) {
				return lower;
			}
		} else {
			lower -= 1;// this ensures cumulativeProbability(lower) < p, which
			// is important for the solving step
		}

		int upper = getSupportUpperBound();
		if (p == 1.0) {
			return upper;
		}

		// use the one-sided Chebyshev inequality to narrow the bracket
		// cf. AbstractRealDistribution.inverseCumulativeProbability(double)
		final double mu = getNumericalMean();
		final double sigma = org.apache.commons.math3.util.FastMath.sqrt(getNumericalVariance());
		final boolean chebyshevApplies = !((((java.lang.Double.isInfinite(mu) || java.lang.Double.isNaN(mu)) || 
		java.lang.Double.isInfinite(sigma)) || java.lang.Double.isNaN(sigma)) || (sigma == 0.0));
		if (chebyshevApplies) {
			double k = org.apache.commons.math3.util.FastMath.sqrt((1.0 - p) / p);
			double tmp = mu - (k * sigma);
			if (tmp > lower) {
				lower = ((int) (java.lang.Math.ceil(tmp))) - 1;
			}
			k = 1.0 / k;
			tmp = mu + (k * sigma);
			if (tmp < upper) {
				upper = ((int) (java.lang.Math.ceil(tmp))) - 1;
			}
		}

		return solveInverseCumulativeProbability(p, lower, upper);
	}

	/**
	 * This is a utility function used by {@link #inverseCumulativeProbability(double)}. It assumes {@code 0 < p < 1} and
	 * that the inverse cumulative probability lies in the bracket {@code (lower, upper]}. The implementation does simple bisection to find the
	 * smallest {@code p}-quantile <code>inf{x in Z | P(X<=x) >= p}</code>.
	 *
	 * @param p
	 * 		the cumulative probability
	 * @param lower
	 * 		a value satisfying {@code cumulativeProbability(lower) < p}
	 * @param upper
	 * 		a value satisfying {@code p <= cumulativeProbability(upper)}
	 * @return the smallest {@code p}-quantile of this distribution
	 */ 	protected int solveInverseCumulativeProbability(final double p, int lower, int upper) {
		while ((lower + 1) < upper) {
			int xm = (lower + upper) / 2;
			if ((xm < lower) || (xm > upper)) {
				/* Overflow.
				There will never be an overflow in both calculation methods
				for xm at the same time
				 */

				xm = lower + ((upper - lower) / 2);
			}

			double pm = checkedCumulativeProbability(xm);
			if (pm >= p) {
				upper = xm;
			} else {
				lower = xm;
			}
		} 
		return upper;
	}

	/**
	 * {@inheritDoc }
	 */ 	public void reseedRandomGenerator(long seed) { 		random.setSeed(seed);
		randomData.reSeed(seed);
	}

	/**
	 * {@inheritDoc }
	 *
	 * The default implementation uses the
	 * <a href="http://en.wikipedia.org/wiki/Inverse_transform_sampling">
	 * inversion method</a>.
	 */
	public int sample() {
		return inverseCumulativeProbability(random.nextDouble());
	}

	/**
	 * {@inheritDoc }
	 *
	 * The default implementation generates the sample by calling
	 * {@link #sample()} in a loop.
	 */
	public int[] sample(int sampleSize) {
		if (sampleSize <= 0) {
			throw new org.apache.commons.math3.exception.NotStrictlyPositiveException(
			org.apache.commons.math3.exception.util.LocalizedFormats.NUMBER_OF_SAMPLES, sampleSize);
		}
		int[] out = new int[sampleSize];
		for (int i = 0; i < sampleSize; i++) {
			out[i] = sample();
		}
		return out;
	}

	/**
	 * Computes the cumulative probability function and checks for {@code NaN}
	 * values returned. Throws {@code MathInternalError} if the value is
	 * {@code NaN}. Rethrows any exception encountered evaluating the cumulative
	 * probability function. Throws {@code MathInternalError} if the cumulative
	 * probability function returns {@code NaN}.
	 *
	 * @param argument
	 * 		input value
	 * @return the cumulative probability
	 * @throws MathInternalError
	 * 		if the cumulative probability is {@code NaN}
	 */ 	private double checkedCumulativeProbability(int argument) throws org.apache.commons.math3.exception.MathInternalError {
		double result = java.lang.Double.NaN;
		result = cumulativeProbability(argument);
		if (java.lang.Double.isNaN(result)) {
			throw new org.apache.commons.math3.exception.MathInternalError(org.apache.commons.math3.exception.util.LocalizedFormats.
			DISCRETE_CUMULATIVE_PROBABILITY_RETURNED_NAN, argument);
		}
		return result;
	}}