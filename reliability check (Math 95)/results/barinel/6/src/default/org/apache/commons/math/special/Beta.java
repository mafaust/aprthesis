package org.apache.commons.math.special;






















/**
 * This is a utility class that provides computation methods related to the
 * Beta family of functions.
 *
 * @version $Revision$ $Date$
 */
public class Beta implements java.io.Serializable {

	/**
	 * Serializable version identifier
	 */ 	private static final long serialVersionUID = -3833485397404128220L;
	/**
	 * Maximum allowed numerical error.
	 */ 	private static final double DEFAULT_EPSILON = 1.0E-14;
	/**
	 * Default constructor.  Prohibit instantiation.
	 */
	private Beta() {
		super();
	}

	/**
	 * Returns the
	 * <a href="http://mathworld.wolfram.com/RegularizedBetaFunction.html">
	 * regularized beta function</a> I(x, a, b).
	 *
	 * @param x
	 * 		the value.
	 * @param a
	 * 		the a parameter.
	 * @param b
	 * 		the b parameter.
	 * @return the regularized beta function I(x, a, b)
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public static double regularizedBeta(double x, double a, double b) throws org.apache.commons.math.MathException { 		return org.apache.commons.math.special.Beta.regularizedBeta(x, a, b, org.apache.commons.math.special.Beta.DEFAULT_EPSILON, java.lang.Integer.MAX_VALUE);
	}

	/**
	 * Returns the
	 * <a href="http://mathworld.wolfram.com/RegularizedBetaFunction.html">
	 * regularized beta function</a> I(x, a, b).
	 *
	 * @param x
	 * 		the value.
	 * @param a
	 * 		the a parameter.
	 * @param b
	 * 		the b parameter.
	 * @param epsilon
	 * 		When the absolute value of the nth item in the
	 * 		series is less than epsilon the approximation ceases
	 * 		to calculate further elements in the series.
	 * @return the regularized beta function I(x, a, b)
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public static double regularizedBeta(double x, double a, double b, double epsilon) throws org.apache.commons.math.MathException { 		return org.apache.commons.math.special.Beta.regularizedBeta(x, a, b, epsilon, java.lang.Integer.MAX_VALUE);}

	/**
	 * Returns the regularized beta function I(x, a, b).
	 *
	 * @param x
	 * 		the value.
	 * @param a
	 * 		the a parameter.
	 * @param b
	 * 		the b parameter.
	 * @param maxIterations
	 * 		Maximum number of "iterations" to complete.
	 * @return the regularized beta function I(x, a, b)
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public static double regularizedBeta(double x, double a, double b, int maxIterations) throws org.apache.commons.math.MathException { 		return org.apache.commons.math.special.Beta.regularizedBeta(x, a, b, org.apache.commons.math.special.Beta.DEFAULT_EPSILON, maxIterations);}

	/**
	 * Returns the regularized beta function I(x, a, b).
	 *
	 * The implementation of this method is based on:
	 * <ul>
	 * <li>
	 * <a href="http://mathworld.wolfram.com/RegularizedBetaFunction.html">
	 * Regularized Beta Function</a>.</li>
	 * <li>
	 * <a href="http://functions.wolfram.com/06.21.10.0001.01">
	 * Regularized Beta Function</a>.</li>
	 * </ul>
	 *
	 * @param x
	 * 		the value.
	 * @param a
	 * 		the a parameter.
	 * @param b
	 * 		the b parameter.
	 * @param epsilon
	 * 		When the absolute value of the nth item in the
	 * 		series is less than epsilon the approximation ceases
	 * 		to calculate further elements in the series.
	 * @param maxIterations
	 * 		Maximum number of "iterations" to complete.
	 * @return the regularized beta function I(x, a, b)
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public static double regularizedBeta(double x, final double a, final double b, double epsilon, int maxIterations) throws org.apache.commons.math.MathException { 		double ret; 		if ((((((java.lang.Double.isNaN(x) || java.lang.Double.isNaN(a)) || java.lang.Double.isNaN(b)) || (x < 0)) || 
		(x > 1)) || (a <= 0.0)) || (b <= 0.0)) 
		{
			ret = java.lang.Double.NaN;
		} else if (x > ((a + 1.0) / ((a + b) + 2.0))) {
			ret = 1.0 - org.apache.commons.math.special.Beta.regularizedBeta(1.0 - x, b, a, epsilon, maxIterations);
		} else {
			org.apache.commons.math.util.ContinuedFraction fraction = new org.apache.commons.math.util.ContinuedFraction() {

				private static final long serialVersionUID = -7658917278956100597L;

				protected double getB(int n, double x) {
					double ret;
					double m;
					if ((n % 2) == 0) { 						// even
						m = n / 2.0;
						ret = ((m * (b - m)) * x) / 
						(((a + (2 * m)) - 1) * (a + (2 * m)));
					} else {
						m = (n - 1.0) / 2.0;
						ret = (-(((a + m) * ((a + b) + m)) * x)) / 
						((a + (2 * m)) * ((a + (2 * m)) + 1.0));
					}
					return ret;
				}

				protected double getA(int n, double x) {
					return 1.0;
				}
			};
			ret = (java.lang.Math.exp((((a * java.lang.Math.log(x)) + (b * java.lang.Math.log(1.0 - x))) - 
			java.lang.Math.log(a)) - org.apache.commons.math.special.Beta.logBeta(a, b, epsilon, maxIterations)) * 
			1.0) / fraction.evaluate(x, epsilon, maxIterations);
		}

		return ret;
	}

	/**
	 * Returns the natural logarithm of the beta function B(a, b).
	 *
	 * @param a
	 * 		the a parameter.
	 * @param b
	 * 		the b parameter.
	 * @return log(B(a, b))
	 */ 	public static double logBeta(double a, double b) { 		return org.apache.commons.math.special.Beta.logBeta(a, b, org.apache.commons.math.special.Beta.DEFAULT_EPSILON, java.lang.Integer.MAX_VALUE);
	}

	/**
	 * Returns the natural logarithm of the beta function B(a, b).
	 *
	 * The implementation of this method is based on:
	 * <ul>
	 * <li><a href="http://mathworld.wolfram.com/BetaFunction.html">
	 * Beta Function</a>, equation (1).</li>
	 * </ul>
	 *
	 * @param a
	 * 		the a parameter.
	 * @param b
	 * 		the b parameter.
	 * @param epsilon
	 * 		When the absolute value of the nth item in the
	 * 		series is less than epsilon the approximation ceases
	 * 		to calculate further elements in the series.
	 * @param maxIterations
	 * 		Maximum number of "iterations" to complete.
	 * @return log(B(a, b))
	 */ 	public static double logBeta(double a, double b, double epsilon, int maxIterations) { 		double ret;

		if (((java.lang.Double.isNaN(a) || java.lang.Double.isNaN(b)) || (a <= 0.0)) || (b <= 0.0)) {
			ret = java.lang.Double.NaN;
		} else {
			ret = (org.apache.commons.math.special.Gamma.logGamma(a) + org.apache.commons.math.special.Gamma.logGamma(b)) - 
			org.apache.commons.math.special.Gamma.logGamma(a + b);
		}

		return ret;
	}}