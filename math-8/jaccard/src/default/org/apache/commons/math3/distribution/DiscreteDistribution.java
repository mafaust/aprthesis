package org.apache.commons.math3.distribution;






























/**
 * Generic implementation of the discrete distribution.
 *
 * @param <T>
 * 		type of the random variable.
 * @see <a href="http://en.wikipedia.org/wiki/Probability_distribution#Discrete_probability_distribution">Discrete probability distribution (Wikipedia)</a>
 * @see <a href="http://mathworld.wolfram.com/DiscreteDistribution.html">Discrete Distribution (MathWorld)</a>
 * @version $Id: DiscreteDistribution.java 169 2013-03-08 09:02:38Z wydrych $
 */
public class DiscreteDistribution<T> {
	/**
	 * RNG instance used to generate samples from the distribution.
	 */
	protected final org.apache.commons.math3.random.RandomGenerator random;
	/**
	 * List of random variable values.
	 */
	private final java.util.List<T> singletons;
	/**
	 * Normalized array of probabilities of respective random variable values.
	 */
	private final double[] probabilities;

	/**
	 * Create a discrete distribution using the given probability mass function
	 * definition.
	 *
	 * @param samples
	 * 		definition of probability mass function in the format of
	 * 		list of pairs.
	 * @throws NotPositiveException
	 * 		if probability of at least one value is
	 * 		negative.
	 * @throws MathArithmeticException
	 * 		if the probabilities sum to zero.
	 * @throws MathIllegalArgumentException
	 * 		if probability of at least one value
	 * 		is infinite.
	 */ 	public DiscreteDistribution(final java.util.List<org.apache.commons.math3.util.Pair<T, java.lang.Double>> samples) throws org.apache.commons.math3.exception.NotPositiveException, org.apache.commons.math3.exception.MathArithmeticException, org.apache.commons.math3.exception.MathIllegalArgumentException { 		this(new org.apache.commons.math3.random.Well19937c(), samples);}

	/**
	 * Create a discrete distribution using the given random number generator
	 * and probability mass function definition.
	 *
	 * @param rng
	 * 		random number generator.
	 * @param samples
	 * 		definition of probability mass function in the format of
	 * 		list of pairs.
	 * @throws NotPositiveException
	 * 		if probability of at least one value is
	 * 		negative.
	 * @throws MathArithmeticException
	 * 		if the probabilities sum to zero.
	 * @throws MathIllegalArgumentException
	 * 		if probability of at least one value
	 * 		is infinite.
	 */ 	public DiscreteDistribution(final org.apache.commons.math3.random.RandomGenerator rng, final java.util.List<org.apache.commons.math3.util.Pair<T, java.lang.Double>> samples) throws org.apache.commons.math3.exception.NotPositiveException, org.apache.commons.math3.exception.MathArithmeticException, org.apache.commons.math3.exception.MathIllegalArgumentException { 		random = rng; 		singletons = new java.util.ArrayList<T>(samples.size());
		final double[] probs = new double[samples.size()];

		for (int i = 0; i < samples.size(); i++) {
			final org.apache.commons.math3.util.Pair<T, java.lang.Double> sample = samples.get(i);
			singletons.add(sample.getKey());
			if (sample.getValue() < 0) {
				throw new org.apache.commons.math3.exception.NotPositiveException(sample.getValue());
			}
			probs[i] = sample.getValue();
		}

		probabilities = org.apache.commons.math3.util.MathArrays.normalizeArray(probs, 1.0);
	}

	/**
	 * Reseed the random generator used to generate samples.
	 *
	 * @param seed
	 * 		the new seed
	 */ 	public void reseedRandomGenerator(long seed) {
		random.setSeed(seed);
	}

	/**
	 * For a random variable {@code X} whose values are distributed according to
	 * this distribution, this method returns {@code P(X = x)}. In other words,
	 * this method represents the probability mass function (PMF) for the
	 * distribution.
	 *
	 * @param x
	 * 		the point at which the PMF is evaluated
	 * @return the value of the probability mass function at {@code x}
	 */ 	double probability(final T x) {
		double probability = 0;

		for (int i = 0; i < probabilities.length; i++) {
			if (((x == null) && (singletons.get(i) == null)) || 
			((x != null) && x.equals(singletons.get(i)))) {
				probability += probabilities[i];
			}
		}

		return probability;
	}

	/**
	 * Return the definition of probability mass function in the format of list
	 * of pairs.
	 *
	 * @return definition of probability mass function.
	 */
	public java.util.List<org.apache.commons.math3.util.Pair<T, java.lang.Double>> getSamples() {
		final java.util.List<org.apache.commons.math3.util.Pair<T, java.lang.Double>> samples = new java.util.ArrayList<org.apache.commons.math3.util.Pair<T, java.lang.Double>>(probabilities.length);

		for (int i = 0; i < probabilities.length; i++) {
			samples.add(new org.apache.commons.math3.util.Pair<T, java.lang.Double>(singletons.get(i), probabilities[i]));
		}

		return samples;
	}

	/**
	 * Generate a random value sampled from this distribution.
	 *
	 * @return a random value.
	 */
	public T sample() {
		final double randomValue = random.nextDouble();
		double sum = 0;

		for (int i = 0; i < probabilities.length; i++) {
			sum += probabilities[i];
			if (randomValue < sum) {
				return singletons.get(i);
			}
		}

		/* This should never happen, but it ensures we will return a correct
		object in case the loop above has some floating point inequality
		problem on the final iteration.
		 */
		return singletons.get(singletons.size() - 1);}

	/**
	 * Generate a random sample from the distribution.
	 *
	 * @param sampleSize
	 * 		the number of random values to generate.
	 * @return an array representing the random sample.
	 * @throws NotStrictlyPositiveException
	 * 		if {@code sampleSize} is not
	 * 		positive.
	 */ 	public T[] sample(int sampleSize) throws org.apache.commons.math3.exception.NotStrictlyPositiveException { 		if (sampleSize <= 0) {
			throw new org.apache.commons.math3.exception.NotStrictlyPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.NUMBER_OF_SAMPLES, 
			sampleSize);
		}

		final T[] out = ((T[]) (java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize)));

		for (int i = 0; i < sampleSize; i++) {
			out[i] = sample();
		}

		return out;

	}}