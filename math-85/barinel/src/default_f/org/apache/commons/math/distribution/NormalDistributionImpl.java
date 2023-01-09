package org.apache.commons.math.distribution;
/**
 * Default implementation of
 * {@link org.apache.commons.math.distribution.NormalDistribution}.
 *
 * @version $Revision$ $Date$
 */
public class NormalDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.NormalDistribution , java.io.Serializable {
	/**
	 * Serializable version identifier
	 */
	private static final long serialVersionUID = 8589540077390120676L;

	/**
	 * &sqrt;(2 &pi;)
	 */
	private static final double SQRT2PI = java.lang.Math.sqrt(2 * java.lang.Math.PI);

	/**
	 * The mean of this distribution.
	 */
	private double mean = 0;

	/**
	 * The standard deviation of this distribution.
	 */
	private double standardDeviation = 1;

	/**
	 * Create a normal distribution using the given mean and standard deviation.
	 *
	 * @param mean
	 * 		mean for this distribution
	 * @param sd
	 * 		standard deviation for this distribution
	 */
	public NormalDistributionImpl(double mean, double sd) {
		super();
		setMean(mean);
		setStandardDeviation(sd);
	}

	/**
	 * Creates normal distribution with the mean equal to zero and standard
	 * deviation equal to one.
	 */
	public NormalDistributionImpl() {
		this(0.0, 1.0);
	}

	/**
	 * Access the mean.
	 *
	 * @return mean for this distribution
	 */
	public double getMean() {
		return mean;
	}

	/**
	 * Modify the mean.
	 *
	 * @param mean
	 * 		for this distribution
	 */
	public void setMean(double mean) {
		this.mean = mean;
	}

	/**
	 * Access the standard deviation.
	 *
	 * @return standard deviation for this distribution
	 */
	public double getStandardDeviation() {
		return standardDeviation;
	}

	/**
	 * Modify the standard deviation.
	 *
	 * @param sd
	 * 		standard deviation for this distribution
	 * @throws IllegalArgumentException
	 * 		if <code>sd</code> is not positive.
	 */
	public void setStandardDeviation(double sd) {
		if (sd <= 0.0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("standard deviation must be positive ({0})", sd);
		}
		standardDeviation = sd;
	}

	/**
	 * Return the probability density for a particular point.
	 *
	 * @param x
	 * 		The point at which the density should be computed.
	 * @return The pdf at point x.
	 */
	public double density(java.lang.Double x) {
		double x0 = x - getMean();
		return java.lang.Math.exp(((-x0) * x0) / ((2 * getStandardDeviation()) * getStandardDeviation())) / (getStandardDeviation() * org.apache.commons.math.distribution.NormalDistributionImpl.SQRT2PI);
	}

	/**
	 * For this distribution, X, this method returns P(X &lt; <code>x</code>).
	 *
	 * @param x
	 * 		the value at which the CDF is evaluated.
	 * @return CDF evaluted at <code>x</code>.
	 * @throws MathException
	 * 		if the algorithm fails to converge; unless
	 * 		x is more than 20 standard deviations from the mean, in which case the
	 * 		convergence exception is caught and 0 or 1 is returned.
	 */
	public double cumulativeProbability(double x) throws org.apache.commons.math.MathException {
		try {
			return 0.5 * (1.0 + org.apache.commons.math.special.Erf.erf((x - mean) / (standardDeviation * java.lang.Math.sqrt(2.0))));
		} catch (org.apache.commons.math.MaxIterationsExceededException ex) {
			if (x < (mean - (20 * standardDeviation))) {
				// JDK 1.5 blows at 38
				return 0.0;
			} else if (x > (mean + (20 * standardDeviation))) {
				return 1.0;
			} else {
				throw ex;
			}
		}
	}

	/**
	 * For this distribution, X, this method returns the critical point x, such
	 * that P(X &lt; x) = <code>p</code>.
	 * <p>
	 * Returns <code>Double.NEGATIVE_INFINITY</code> for p=0 and
	 * <code>Double.POSITIVE_INFINITY</code> for p=1.</p>
	 *
	 * @param p
	 * 		the desired probability
	 * @return x, such that P(X &lt; x) = <code>p</code>
	 * @throws MathException
	 * 		if the inverse cumulative probability can not be
	 * 		computed due to convergence or other numerical errors.
	 * @throws IllegalArgumentException
	 * 		if <code>p</code> is not a valid
	 * 		probability.
	 */
	@java.lang.Override
	public double inverseCumulativeProbability(final double p) throws org.apache.commons.math.MathException {
		if (p == 0) {
			return java.lang.Double.NEGATIVE_INFINITY;
		}
		if (p == 1) {
			return java.lang.Double.POSITIVE_INFINITY;
		}
		return super.inverseCumulativeProbability(p);
	}

	/**
	 * Access the domain value lower bound, based on <code>p</code>, used to
	 * bracket a CDF root.  This method is used by
	 * {@link #inverseCumulativeProbability(double)} to find critical values.
	 *
	 * @param p
	 * 		the desired probability for the critical value
	 * @return domain value lower bound, i.e.
	P(X &lt; <i>lower bound</i>) &lt; <code>p</code>
	 */
	@java.lang.Override
	protected double getDomainLowerBound(double p) {
		double ret;
		if (p < 0.5) {
			ret = -java.lang.Double.MAX_VALUE;
		} else {
			ret = getMean();
		}
		return ret;
	}

	/**
	 * Access the domain value upper bound, based on <code>p</code>, used to
	 * bracket a CDF root.  This method is used by
	 * {@link #inverseCumulativeProbability(double)} to find critical values.
	 *
	 * @param p
	 * 		the desired probability for the critical value
	 * @return domain value upper bound, i.e.
	P(X &lt; <i>upper bound</i>) &gt; <code>p</code>
	 */
	@java.lang.Override
	protected double getDomainUpperBound(double p) {
		double ret;
		if (p < 0.5) {
			ret = getMean();
		} else {
			ret = java.lang.Double.MAX_VALUE;
		}
		return ret;
	}

	/**
	 * Access the initial domain value, based on <code>p</code>, used to
	 * bracket a CDF root.  This method is used by
	 * {@link #inverseCumulativeProbability(double)} to find critical values.
	 *
	 * @param p
	 * 		the desired probability for the critical value
	 * @return initial domain value
	 */
	@java.lang.Override
	protected double getInitialDomain(double p) {
		double ret;
		if (p < 0.5) {
			ret = getMean() - getStandardDeviation();
		} else if (p > 0.5) {
			ret = getMean() + getStandardDeviation();
		} else {
			ret = getMean();
		}
		return ret;
	}
}