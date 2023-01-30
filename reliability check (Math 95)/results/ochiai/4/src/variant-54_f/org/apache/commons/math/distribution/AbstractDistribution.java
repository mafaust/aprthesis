package org.apache.commons.math.distribution;
/**
 * Base class for probability distributions.
 *
 * @version $Revision$ $Date$
 */
public abstract class AbstractDistribution implements org.apache.commons.math.distribution.Distribution , java.io.Serializable {
	/**
	 * Serializable version identifier
	 */
	private static final long serialVersionUID = -38038050983108802L;

	/**
	 * Default constructor.
	 */
	protected AbstractDistribution() {
		super();
	}

	/**
	 * For a random variable X whose values are distributed according
	 * to this distribution, this method returns P(x0 &le; X &le; x1).
	 * <p>
	 * The default implementation uses the identity</p>
	 * <p>
	 * P(x0 &le; X &le; x1) = P(X &le; x1) - P(X &le; x0) </p>
	 *
	 * @param x0
	 * 		the (inclusive) lower bound
	 * @param x1
	 * 		the (inclusive) upper bound
	 * @return the probability that a random variable with this distribution
	will take a value between <code>x0</code> and <code>x1</code>,
	including the endpoints.
	 * @throws MathException
	 * 		if the cumulative probability can not be
	 * 		computed due to convergence or other numerical errors.
	 * @throws IllegalArgumentException
	 * 		if <code>x0 > x1</code>
	 */
	public double cumulativeProbability(double x0, double x1) throws org.apache.commons.math.MathException {
		if (x0 > x1) {
			throw new java.lang.IllegalArgumentException("lower endpoint must be less than or equal to upper endpoint");
		}
		return cumulativeProbability(x1) - cumulativeProbability(x0);
	}
}