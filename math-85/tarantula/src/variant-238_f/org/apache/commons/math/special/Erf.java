package org.apache.commons.math.special;
/**
 * This is a utility class that provides computation methods related to the
 * error functions.
 *
 * @version $Revision$ $Date$
 */
public class Erf {
	/**
	 * Default constructor.  Prohibit instantiation.
	 */
	private Erf() {
		super();
	}

	/**
	 * Returns the error function erf(x).
	 *
	 * The implementation of this method is based on:
	 * <ul>
	 * <li>
	 * <a href="http://mathworld.wolfram.com/Erf.html">
	 * Erf</a>, equation (3).</li>
	 * </ul>
	 *
	 * @param x
	 * 		the value.
	 * @return the error function erf(x)
	 * @throws MathException
	 * 		if the algorithm fails to converge.
	 */
	public static double erf(double x) throws org.apache.commons.math.MathException {
		double ret = org.apache.commons.math.special.Gamma.regularizedGammaP(0.5, x * x, 1.0E-15, 10000);
		if (x < 0) {
			ret = -ret;
		}
		return ret;
	}
}