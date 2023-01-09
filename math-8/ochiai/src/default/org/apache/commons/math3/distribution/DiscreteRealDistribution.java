package org.apache.commons.math3.distribution;



























/**
 * Implementation of the discrete distribution on the reals.
 *
 * Note: values with zero-probability are allowed but they do not extend the
 * support.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Probability_distribution#Discrete_probability_distribution">Discrete probability distribution (Wikipedia)</a>
 * @see <a href="http://mathworld.wolfram.com/DiscreteDistribution.html">Discrete Distribution (MathWorld)</a>
 * @version $Id: DiscreteRealDistribution.java 169 2013-03-08 09:02:38Z wydrych $
 */
public class DiscreteRealDistribution extends org.apache.commons.math3.distribution.AbstractRealDistribution {

	/**
	 * Serializable UID.
	 */ 	private static final long serialVersionUID = 20130308L;
	/**
	 * {@link DiscreteDistribution} instance (using the {@link Double} wrapper)
	 * used to generate samples.
	 */
	protected final org.apache.commons.math3.distribution.DiscreteDistribution<java.lang.Double> innerDistribution;

	/**
	 * Create a discrete distribution using the given probability mass function
	 * definition.
	 *
	 * @param singletons
	 * 		array of random variable values.
	 * @param probabilities
	 * 		array of probabilities.
	 * @throws DimensionMismatchException
	 * 		if
	 * 		{@code singletons.length != probabilities.length}
	 * @throws NotPositiveException
	 * 		if probability of at least one value is
	 * 		negative.
	 * @throws MathArithmeticException
	 * 		if the probabilities sum to zero.
	 * @throws MathIllegalArgumentException
	 * 		if probability of at least one value
	 * 		is infinite.
	 */ 	public DiscreteRealDistribution(final double[] singletons, final double[] probabilities) throws org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.NotPositiveException, org.apache.commons.math3.exception.MathArithmeticException, org.apache.commons.math3.exception.MathIllegalArgumentException { 		this(new org.apache.commons.math3.random.Well19937c(), singletons, probabilities);} 	/**
	 * Create a discrete distribution using the given random number generator
	 * and probability mass function definition.
	 *
	 * @param rng
	 * 		random number generator.
	 * @param singletons
	 * 		array of random variable values.
	 * @param probabilities
	 * 		array of probabilities.
	 * @throws DimensionMismatchException
	 * 		if
	 * 		{@code singletons.length != probabilities.length}
	 * @throws NotPositiveException
	 * 		if probability of at least one value is
	 * 		negative.
	 * @throws MathArithmeticException
	 * 		if the probabilities sum to zero.
	 * @throws MathIllegalArgumentException
	 * 		if probability of at least one value
	 * 		is infinite.
	 */ 	public DiscreteRealDistribution(final org.apache.commons.math3.random.RandomGenerator rng, final double[] singletons, final double[] probabilities) throws org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.NotPositiveException, org.apache.commons.math3.exception.MathArithmeticException, org.apache.commons.math3.exception.MathIllegalArgumentException { 		super(rng); 		if (singletons.length != probabilities.length) { 			throw new org.apache.commons.math3.exception.DimensionMismatchException(probabilities.length, singletons.length);}

		java.util.List<org.apache.commons.math3.util.Pair<java.lang.Double, java.lang.Double>> samples = new java.util.ArrayList<org.apache.commons.math3.util.Pair<java.lang.Double, java.lang.Double>>(singletons.length);

		for (int i = 0; i < singletons.length; i++) {
			samples.add(new org.apache.commons.math3.util.Pair<java.lang.Double, java.lang.Double>(singletons[i], probabilities[i]));
		}

		innerDistribution = new org.apache.commons.math3.distribution.DiscreteDistribution<java.lang.Double>(rng, samples);
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	public double probability(final double x) {
		return innerDistribution.probability(x);
	}

	/**
	 * For a random variable {@code X} whose values are distributed according to
	 * this distribution, this method returns {@code P(X = x)}. In other words,
	 * this method represents the probability mass function (PMF) for the
	 * distribution.
	 *
	 * @param x
	 * 		the point at which the PMF is evaluated
	 * @return the value of the probability mass function at point {@code x}
	 */ 	public double density(final double x) {
		return probability(x);
	}

	/**
	 * {@inheritDoc }
	 */
	public double cumulativeProbability(final double x) {
		double probability = 0;

		for (final org.apache.commons.math3.util.Pair<java.lang.Double, java.lang.Double> sample : innerDistribution.getSamples()) {
			if (sample.getKey() <= x) {
				probability += sample.getValue();
			}
		}

		return probability;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return {@code sum(singletons[i] * probabilities[i])}
	 */
	public double getNumericalMean() {
		double mean = 0;

		for (final org.apache.commons.math3.util.Pair<java.lang.Double, java.lang.Double> sample : innerDistribution.getSamples()) {
			mean += sample.getValue() * sample.getKey();
		}

		return mean;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return {@code sum((singletons[i] - mean) ^ 2 * probabilities[i])}
	 */
	public double getNumericalVariance() {
		double mean = 0;
		double meanOfSquares = 0;

		for (final org.apache.commons.math3.util.Pair<java.lang.Double, java.lang.Double> sample : innerDistribution.getSamples()) {
			mean += sample.getValue() * sample.getKey();
			meanOfSquares += (sample.getValue() * sample.getKey()) * sample.getKey();
		}

		return meanOfSquares - (mean * mean);
	}

	/**
	 * {@inheritDoc }
	 *
	 * Returns the lowest value with non-zero probability.
	 *
	 * @return the lowest value with non-zero probability.
	 */
	public double getSupportLowerBound() {
		double min = java.lang.Double.POSITIVE_INFINITY;
		for (final org.apache.commons.math3.util.Pair<java.lang.Double, java.lang.Double> sample : innerDistribution.getSamples()) {
			if ((sample.getKey() < min) && (sample.getValue() > 0)) {
				min = sample.getKey();
			}
		}

		return min;
	}

	/**
	 * {@inheritDoc }
	 *
	 * Returns the highest value with non-zero probability.
	 *
	 * @return the highest value with non-zero probability.
	 */
	public double getSupportUpperBound() {
		double max = java.lang.Double.NEGATIVE_INFINITY;
		for (final org.apache.commons.math3.util.Pair<java.lang.Double, java.lang.Double> sample : innerDistribution.getSamples()) {
			if ((sample.getKey() > max) && (sample.getValue() > 0)) {
				max = sample.getKey();
			}
		}

		return max;
	}

	/**
	 * {@inheritDoc }
	 *
	 * The support of this distribution includes the lower bound.
	 *
	 * @return {@code true}
	 */
	public boolean isSupportLowerBoundInclusive() {
		return true;
	}

	/**
	 * {@inheritDoc }
	 *
	 * The support of this distribution includes the upper bound.
	 *
	 * @return {@code true}
	 */
	public boolean isSupportUpperBoundInclusive() {
		return true;
	}

	/**
	 * {@inheritDoc }
	 *
	 * The support of this distribution is connected.
	 *
	 * @return {@code true}
	 */
	public boolean isSupportConnected() {
		return true;
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	public double sample() {
		return innerDistribution.sample();
	}}