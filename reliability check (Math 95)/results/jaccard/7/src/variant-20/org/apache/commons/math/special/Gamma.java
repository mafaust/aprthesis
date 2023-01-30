package org.apache.commons.math.special;























/**
 * This is a utility class that provides computation methods related to the
 * Gamma family of functions.
 *
 * @version $Revision$ $Date$
 */
public class Gamma implements java.io.Serializable {

	/**
	 * Serializable version identifier
	 */ 	private static final long serialVersionUID = -6587513359895466954L;
	/**
	 * Maximum allowed numerical error.
	 */ 	private static final double DEFAULT_EPSILON = 1.0E-14;
	/**
	 * Lanczos coefficients
	 */ 	private static double[] lanczos = new double[]{ 
	0.9999999999999971, 
	57.15623566586292, 
	-59.59796035547549, 
	14.136097974741746, 
	-0.4919138160976202, 
	3.399464998481189E-5, 
	4.652362892704858E-5, 
	-9.837447530487956E-5, 
	1.580887032249125E-4, 
	-2.1026444172410488E-4, 
	2.1743961811521265E-4, 
	-1.643181065367639E-4, 
	8.441822398385275E-5, 
	-2.6190838401581408E-5, 
	3.6899182659531625E-6 };


	/**
	 * Avoid repeated computation of log of 2 PI in logGamma
	 */ 	private static final double HALF_LOG_2_PI = 0.5 * java.lang.Math.log(2.0 * java.lang.Math.PI);

	/**
	 * Default constructor.  Prohibit instantiation.
	 */
	private Gamma() {
		super();
	}

	/**
	 * Returns the natural logarithm of the gamma function &#915;(x).
	 *
	 * The implementation of this method is based on:
	 * <ul>
	 * <li><a href="http://mathworld.wolfram.com/GammaFunction.html">
	 * Gamma Function</a>, equation (28).</li>
	 * <li><a href="http://mathworld.wolfram.com/LanczosApproximation.html">
	 * Lanczos Approximation</a>, equations (1) through (5).</li>
	 * <li><a href="http://my.fit.edu/~gabdo/gamma.txt">Paul Godfrey, A note on
	 * the computation of the convergent Lanczos complex Gamma approximation
	 * </a></li>
	 * </ul>
	 *
	 * @param x
	 * 		the value.
	 * @return log(&#915;(x))
	 */ 	public static double logGamma(double x) {
		double ret;

		if (java.lang.Double.isNaN(x) || (x <= 0.0)) {
			ret = java.lang.Double.NaN;
		} else {
			double g = 607.0 / 128.0;

			double sum = 0.0;
			for (int i = org.apache.commons.math.special.Gamma.lanczos.length - 1; i > 0; --i) {
				sum = sum + (org.apache.commons.math.special.Gamma.lanczos[i] / (x + i));
			}
			sum = sum + org.apache.commons.math.special.Gamma.lanczos[0];

			double tmp = (x + g) + 0.5;
			ret = ((((x + 0.5) * java.lang.Math.log(tmp)) - tmp) + 
			org.apache.commons.math.special.Gamma.HALF_LOG_2_PI) + java.lang.Math.log(sum / x);
		}

		return ret;
	}

	/**
	 * Returns the regularized gamma function P(a, x).
	 *
	 * @param a
	 * 		the a parameter.
	 * @param x
	 * 		the value.
	 * @return the regularized gamma function P(a, x)
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public static double regularizedGammaP(double a, double x) throws org.apache.commons.math.MathException {
		return org.apache.commons.math.special.Gamma.regularizedGammaP(a, x, org.apache.commons.math.special.Gamma.DEFAULT_EPSILON, java.lang.Integer.MAX_VALUE);
	}


	/**
	 * Returns the regularized gamma function P(a, x).
	 *
	 * The implementation of this method is based on:
	 * <ul>
	 * <li>
	 * <a href="http://mathworld.wolfram.com/RegularizedGammaFunction.html">
	 * Regularized Gamma Function</a>, equation (1).</li>
	 * <li>
	 * <a href="http://mathworld.wolfram.com/IncompleteGammaFunction.html">
	 * Incomplete Gamma Function</a>, equation (4).</li>
	 * <li>
	 * <a href="http://mathworld.wolfram.com/ConfluentHypergeometricFunctionoftheFirstKind.html">
	 * Confluent Hypergeometric Function of the First Kind</a>, equation (1).
	 * </li>
	 * </ul>
	 *
	 * @param a
	 * 		the a parameter.
	 * @param x
	 * 		the value.
	 * @param epsilon
	 * 		When the absolute value of the nth item in the
	 * 		series is less than epsilon the approximation ceases
	 * 		to calculate further elements in the series.
	 * @param maxIterations
	 * 		Maximum number of "iterations" to complete.
	 * @return the regularized gamma function P(a, x)
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public static double regularizedGammaP(double a, double x, double epsilon, int maxIterations) throws org.apache.commons.math.MathException 
	{
		double ret;

		if (((java.lang.Double.isNaN(a) || java.lang.Double.isNaN(x)) || (a <= 0.0)) || (x < 0.0)) {
			ret = java.lang.Double.NaN;
		} else if (x == 0.0) {
			ret = 0.0;
		} else if ((a >= 1.0) && (x > a)) {
			// use regularizedGammaQ because it should converge faster in this
			// case.
			ret = 1.0 - org.apache.commons.math.special.Gamma.regularizedGammaQ(a, x, epsilon, maxIterations);
		} else {
			// calculate series
			double n = 0.0;// current element index
			double an = 1.0 / a;// n-th element in the series
			double sum = an;// partial sum
			while ((java.lang.Math.abs(an) > epsilon) && (n < maxIterations)) {
				// compute next element in the series
				n = n + 1.0;
				an = an * (x / (a + n));

				// update partial sum
				sum = sum + an;
			} 
			if (n >= maxIterations) {
				throw new org.apache.commons.math.MaxIterationsExceededException(maxIterations);
			} else {
				ret = java.lang.Math.exp(((-x) + (a * java.lang.Math.log(x))) - org.apache.commons.math.special.Gamma.logGamma(a)) * sum;
			}
		}

		return ret;
	}

	/**
	 * Returns the regularized gamma function Q(a, x) = 1 - P(a, x).
	 *
	 * @param a
	 * 		the a parameter.
	 * @param x
	 * 		the value.
	 * @return the regularized gamma function Q(a, x)
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public static double regularizedGammaQ(double a, double x) throws org.apache.commons.math.MathException {
		return org.apache.commons.math.special.Gamma.regularizedGammaQ(a, x, org.apache.commons.math.special.Gamma.DEFAULT_EPSILON, java.lang.Integer.MAX_VALUE);
	}

	/**
	 * Returns the regularized gamma function Q(a, x) = 1 - P(a, x).
	 *
	 * The implementation of this method is based on:
	 * <ul>
	 * <li>
	 * <a href="http://mathworld.wolfram.com/RegularizedGammaFunction.html">
	 * Regularized Gamma Function</a>, equation (1).</li>
	 * <li>
	 * <a href="    http://functions.wolfram.com/GammaBetaErf/GammaRegularized/10/0003/">
	 * Regularized incomplete gamma function: Continued fraction representations  (formula 06.08.10.0003)</a></li>
	 * </ul>
	 *
	 * @param a
	 * 		the a parameter.
	 * @param x
	 * 		the value.
	 * @param epsilon
	 * 		When the absolute value of the nth item in the
	 * 		series is less than epsilon the approximation ceases
	 * 		to calculate further elements in the series.
	 * @param maxIterations
	 * 		Maximum number of "iterations" to complete.
	 * @return the regularized gamma function P(a, x)
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */ 	public static double regularizedGammaQ(final double a, double x, double epsilon, int maxIterations) throws org.apache.commons.math.MathException 
	{
		double ret;

		if (((java.lang.Double.isNaN(a) || java.lang.Double.isNaN(x)) || (a <= 0.0)) || (x < 0.0)) {
			ret = java.lang.Double.NaN;
		} else if (x == 0.0) {
			ret = 1.0;
		} else if ((x < a) || (a < 1.0)) {
			// use regularizedGammaP because it should converge faster in this
			// case.
			ret = 1.0 - org.apache.commons.math.special.Gamma.regularizedGammaP(a, x, epsilon, maxIterations);
		} else {
			// create continued fraction
			org.apache.commons.math.util.ContinuedFraction cf = new org.apache.commons.math.util.ContinuedFraction() {

				private static final long serialVersionUID = 5378525034886164398L;

				protected double getA(int n, double x) {
					return (((2.0 * n) + 1.0) - a) + x;
				}

				protected double getB(int n, double x) {
					return n * (a - n);
				}
			};

			ret = 1.0 / cf.evaluate(x, epsilon, maxIterations);
			ret = java.lang.Math.exp(((-x) + (a * java.lang.Math.log(x))) - org.apache.commons.math.special.Gamma.logGamma(a)) * ret;
		}

		return ret;
	}}