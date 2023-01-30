package org.apache.commons.math.util;























/**
 * Provides a generic means to evaluate continued fractions.  Subclasses simply
 * provided the a and b coefficients to evaluate the continued fraction.
 *
 * <p>
 * References:
 * <ul>
 * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
 * Continued Fraction</a></li>
 * </ul>
 * </p>
 *
 * @version $Revision$ $Date$
 */
public abstract class ContinuedFraction implements java.io.Serializable {

	/**
	 * Serialization UID
	 */ 	private static final long serialVersionUID = 1768555336266158242L;
	/**
	 * Maximum allowed numerical error.
	 */ 	private static final double DEFAULT_EPSILON = 1.0E-8;
	/**
	 * Default constructor.
	 */
	protected ContinuedFraction() {
		super();
	}

	/**
	 * Access the n-th a coefficient of the continued fraction.  Since a can be
	 * a function of the evaluation point, x, that is passed in as well.
	 *
	 * @param n
	 * 		the coefficient index to retrieve.
	 * @param x
	 * 		the evaluation point.
	 * @return the n-th a coefficient.
	 */ 	protected abstract double getA(int n, double x); 	/**
	 * Access the n-th b coefficient of the continued fraction.  Since b can be
	 * a function of the evaluation point, x, that is passed in as well.
	 *
	 * @param n
	 * 		the coefficient index to retrieve.
	 * @param x
	 * 		the evaluation point.
	 * @return the n-th b coefficient.
	 */ 	protected abstract double getB(int n, double x); 	/**
	 * Evaluates the continued fraction at the value x.
	 *
	 * @param x
	 * 		the evaluation point.
	 * @return the value of the continued fraction evaluated at x.
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public double evaluate(double x) throws org.apache.commons.math.MathException { 		return evaluate(x, org.apache.commons.math.util.ContinuedFraction.DEFAULT_EPSILON, java.lang.Integer.MAX_VALUE);}

	/**
	 * Evaluates the continued fraction at the value x.
	 *
	 * @param x
	 * 		the evaluation point.
	 * @param epsilon
	 * 		maximum error allowed.
	 * @return the value of the continued fraction evaluated at x.
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public double evaluate(double x, double epsilon) throws org.apache.commons.math.MathException { 		return evaluate(x, epsilon, java.lang.Integer.MAX_VALUE);}
	/**
	 * Evaluates the continued fraction at the value x.
	 *
	 * @param x
	 * 		the evaluation point.
	 * @param maxIterations
	 * 		maximum number of convergents
	 * @return the value of the continued fraction evaluated at x.
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public double evaluate(double x, int maxIterations) throws org.apache.commons.math.MathException { 		return evaluate(x, org.apache.commons.math.util.ContinuedFraction.DEFAULT_EPSILON, maxIterations);}
	/**
	 * <p>
	 * Evaluates the continued fraction at the value x.
	 * </p>
	 *
	 * <p>
	 * The implementation of this method is based on equations 14-17 of:
	 * <ul>
	 * <li>
	 *   Eric W. Weisstein. "Continued Fraction." From MathWorld--A Wolfram Web
	 *   Resource. <a target="_blank"
	 *   href="http://mathworld.wolfram.com/ContinuedFraction.html">
	 *   http://mathworld.wolfram.com/ContinuedFraction.html</a>
	 * </li>
	 * </ul>
	 * The recurrence relationship defined in those equations can result in
	 * very large intermediate results which can result in numerical overflow.
	 * As a means to combat these overflow conditions, the intermediate results
	 * are scaled whenever they threaten to become numerically unstable.</p>
	 *
	 * @param x
	 * 		the evaluation point.
	 * @param epsilon
	 * 		maximum error allowed.
	 * @param maxIterations
	 * 		maximum number of convergents
	 * @return the value of the continued fraction evaluated at x.
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public double evaluate(double x, double epsilon, int maxIterations) throws org.apache.commons.math.MathException { 		double p0 = 1.0;
		double p1 = getA(0, x);
		double q0 = 0.0;
		double q1 = 1.0;
		double c = p1 / q1;
		int n = 0;
		double relativeError = java.lang.Double.MAX_VALUE;
		while ((n < maxIterations) && (relativeError > epsilon)) {
			++n;
			double a = getA(n, x);
			double b = getB(n, x);
			double p2 = (a * p1) + (b * p0);
			double q2 = (a * q1) + (b * q0);
			if (java.lang.Double.isInfinite(p2) || java.lang.Double.isInfinite(q2)) {
				// need to scale
				if (a != 0.0) {
					p2 = p1 + ((b / a) * p0);
					q2 = q1 + ((b / a) * q0);
				} else if (b != 0) {
					p2 = ((a / b) * p1) + p0;
					q2 = ((a / b) * q1) + q0;
				} else {
					// can not scale an convergent is unbounded.
					throw new org.apache.commons.math.ConvergenceException(
					"Continued fraction convergents diverged to +/- infinity for value {0}", 
					new java.lang.Object[]{ java.lang.Double.valueOf(x) });
				}
			}
			double r = p2 / q2;
			relativeError = java.lang.Math.abs((r / c) - 1.0);

			// prepare for next iteration
			c = p2 / q2;
			p0 = p1;
			p1 = p2;
			q0 = q1;
			q1 = q2;
		} 

		if (n >= maxIterations) {
			throw new org.apache.commons.math.MaxIterationsExceededException(maxIterations, 
			"Continued fraction convergents failed to converge for value {0}", 
			new java.lang.Object[]{ java.lang.Double.valueOf(x) });
		}

		return c;
	}}