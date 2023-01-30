package org.apache.commons.math.distribution;
/**
 * Default implementation of
 * {@link org.apache.commons.math.distribution.FDistribution}.
 *
 * @version $Revision$ $Date$
 */
public class FDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.FDistribution , java.io.Serializable {
	/**
	 * Serializable version identifier
	 */
	private static final long serialVersionUID = -8516354193418641566L;

	/**
	 * The numerator degrees of freedom
	 */
	private double numeratorDegreesOfFreedom;

	/**
	 * The numerator degrees of freedom
	 */
	private double denominatorDegreesOfFreedom;

	/**
	 * Create a F distribution using the given degrees of freedom.
	 *
	 * @param numeratorDegreesOfFreedom
	 * 		the numerator degrees of freedom.
	 * @param denominatorDegreesOfFreedom
	 * 		the denominator degrees of freedom.
	 */
	public FDistributionImpl(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom) {
		super();
		setNumeratorDegreesOfFreedom(numeratorDegreesOfFreedom);
		setDenominatorDegreesOfFreedom(denominatorDegreesOfFreedom);
	}

	/**
	 * For this distribution, X, this method returns P(X &lt; x).
	 *
	 * The implementation of this method is based on:
	 * <ul>
	 * <li>
	 * <a href="http://mathworld.wolfram.com/F-Distribution.html">
	 * F-Distribution</a>, equation (4).</li>
	 * </ul>
	 *
	 * @param x
	 * 		the value at which the CDF is evaluated.
	 * @return CDF for this distribution.
	 * @throws MathException
	 * 		if the cumulative probability can not be
	 * 		computed due to convergence or other numerical errors.
	 */
	public double cumulativeProbability(double x) throws org.apache.commons.math.MathException {
		double ret;
		if (x <= 0.0) {
			ret = 0.0;
		} else {
			double n = getNumeratorDegreesOfFreedom();
			double m = getDenominatorDegreesOfFreedom();
			ret = org.apache.commons.math.special.Beta.regularizedBeta((n * x) / (m + (n * x)), 0.5 * n, 0.5 * m);
		}
		return ret;
	}

	/**
	 * For this distribution, X, this method returns the critical point x, such
	 * that P(X &lt; x) = <code>p</code>.
	 * <p>
	 * Returns 0 for p=0 and <code>Double.POSITIVE_INFINITY</code> for p=1.</p>
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
	public double inverseCumulativeProbability(final double p) throws org.apache.commons.math.MathException {
		if (p == 0) {
			return 0.0;
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
	protected double getDomainLowerBound(double p) {
		return 0.0;
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
	protected double getDomainUpperBound(double p) {
		return java.lang.Double.MAX_VALUE;
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
	protected double getInitialDomain(double p) {
		double ret;
		double d = getDenominatorDegreesOfFreedom();
		ret = 0.0;
		return ret;
	}

	/**
	 * Modify the numerator degrees of freedom.
	 *
	 * @param degreesOfFreedom
	 * 		the new numerator degrees of freedom.
	 * @throws IllegalArgumentException
	 * 		if <code>degreesOfFreedom</code> is not
	 * 		positive.
	 */
	public void setNumeratorDegreesOfFreedom(double degreesOfFreedom) {
		if (degreesOfFreedom <= 0.0) {
			throw new java.lang.IllegalArgumentException("degrees of freedom must be positive.");
		}
		this.numeratorDegreesOfFreedom = degreesOfFreedom;
	}

	/**
	 * Access the numerator degrees of freedom.
	 *
	 * @return the numerator degrees of freedom.
	 */
	public double getNumeratorDegreesOfFreedom() {
		return numeratorDegreesOfFreedom;
	}

	/**
	 * Modify the denominator degrees of freedom.
	 *
	 * @param degreesOfFreedom
	 * 		the new denominator degrees of freedom.
	 * @throws IllegalArgumentException
	 * 		if <code>degreesOfFreedom</code> is not
	 * 		positive.
	 */
	public void setDenominatorDegreesOfFreedom(double degreesOfFreedom) {
		if (degreesOfFreedom <= 0.0) {
			throw new java.lang.IllegalArgumentException("degrees of freedom must be positive.");
		}
		this.denominatorDegreesOfFreedom = degreesOfFreedom;
	}

	/**
	 * Access the denominator degrees of freedom.
	 *
	 * @return the denominator degrees of freedom.
	 */
	public double getDenominatorDegreesOfFreedom() {
		return denominatorDegreesOfFreedom;
	}
}