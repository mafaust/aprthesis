package org.apache.commons.math.util;
/**
 * Some useful additions to the built-in functions in {@link Math}.
 *
 * @version $Revision$ $Date$
 */
public final class MathUtils {
	/**
	 * Smallest positive number such that 1 - EPSILON is not numerically equal to 1.
	 */
	public static final double EPSILON = 0x1.0p-53;

	/**
	 * Safe minimum, such that 1 / SAFE_MIN does not overflow.
	 * <p>In IEEE 754 arithmetic, this is also the smallest normalized
	 * number 2<sup>-1022</sup>.</p>
	 */
	public static final double SAFE_MIN = 0x1.0p-1022;

	/**
	 * 2 &pi;.
	 *
	 * @since 2.1
	 */
	public static final double TWO_PI = 2 * java.lang.Math.PI;

	/**
	 * -1.0 cast as a byte.
	 */
	private static final byte NB = ((byte) (-1));

	/**
	 * -1.0 cast as a short.
	 */
	private static final short NS = ((short) (-1));

	/**
	 * 1.0 cast as a byte.
	 */
	private static final byte PB = ((byte) (1));

	/**
	 * 1.0 cast as a short.
	 */
	private static final short PS = ((short) (1));

	/**
	 * 0.0 cast as a byte.
	 */
	private static final byte ZB = ((byte) (0));

	/**
	 * 0.0 cast as a short.
	 */
	private static final short ZS = ((short) (0));

	/**
	 * Gap between NaN and regular numbers.
	 */
	private static final int NAN_GAP = (4 * 1024) * 1024;

	/**
	 * Offset to order signed double numbers lexicographically.
	 */
	private static final long SGN_MASK = 0x8000000000000000L;

	/**
	 * All long-representable factorials
	 */
	private static final long[] FACTORIALS = new long[]{ 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };

	/**
	 * Private Constructor
	 */
	private MathUtils() {
		super();
	}

	/**
	 * Add two integers, checking for overflow.
	 *
	 * @param x
	 * 		an addend
	 * @param y
	 * 		an addend
	 * @return the sum <code>x+y</code>
	 * @throws ArithmeticException
	 * 		if the result can not be represented as an
	 * 		int
	 * @since 1.1
	 */
	public static int addAndCheck(int x, int y) {
		long s = ((long) (x)) + ((long) (y));
		if ((s < java.lang.Integer.MIN_VALUE) || (s > java.lang.Integer.MAX_VALUE)) {
			throw new java.lang.ArithmeticException("overflow: add");
		}
		return ((int) (s));
	}

	/**
	 * Add two long integers, checking for overflow.
	 *
	 * @param a
	 * 		an addend
	 * @param b
	 * 		an addend
	 * @return the sum <code>a+b</code>
	 * @throws ArithmeticException
	 * 		if the result can not be represented as an
	 * 		long
	 * @since 1.2
	 */
	public static long addAndCheck(long a, long b) {
		return org.apache.commons.math.util.MathUtils.addAndCheck(a, b, "overflow: add");
	}

	/**
	 * Add two long integers, checking for overflow.
	 *
	 * @param a
	 * 		an addend
	 * @param b
	 * 		an addend
	 * @param msg
	 * 		the message to use for any thrown exception.
	 * @return the sum <code>a+b</code>
	 * @throws ArithmeticException
	 * 		if the result can not be represented as an
	 * 		long
	 * @since 1.2
	 */
	private static long addAndCheck(long a, long b, java.lang.String msg) {
		long ret;
		if (a > b) {
			// use symmetry to reduce boundary cases
			ret = org.apache.commons.math.util.MathUtils.addAndCheck(b, a, msg);
		} else // assert a <= b
		if (a < 0) {
			if (b < 0) {
				// check for negative overflow
				if ((java.lang.Long.MIN_VALUE - b) <= a) {
					ret = a + b;
				} else {
					throw new java.lang.ArithmeticException(msg);
				}
			} else {
				// opposite sign addition is always safe
				ret = a + b;
			}
		} else // assert a >= 0
		// assert b >= 0
		// check for positive overflow
		if (a <= (java.lang.Long.MAX_VALUE - b)) {
			ret = a + b;
		} else {
			throw new java.lang.ArithmeticException(msg);
		}
		return ret;
	}

	/**
	 * Returns an exact representation of the <a
	 * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
	 * Coefficient</a>, "<code>n choose k</code>", the number of
	 * <code>k</code>-element subsets that can be selected from an
	 * <code>n</code>-element set.
	 * <p>
	 * <Strong>Preconditions</strong>:
	 * <ul>
	 * <li> <code>0 <= k <= n </code> (otherwise
	 * <code>IllegalArgumentException</code> is thrown)</li>
	 * <li> The result is small enough to fit into a <code>long</code>. The
	 * largest value of <code>n</code> for which all coefficients are
	 * <code> < Long.MAX_VALUE</code> is 66. If the computed value exceeds
	 * <code>Long.MAX_VALUE</code> an <code>ArithMeticException</code> is
	 * thrown.</li>
	 * </ul></p>
	 *
	 * @param n
	 * 		the size of the set
	 * @param k
	 * 		the size of the subsets to be counted
	 * @return <code>n choose k</code>
	 * @throws IllegalArgumentException
	 * 		if preconditions are not met.
	 * @throws ArithmeticException
	 * 		if the result is too large to be represented
	 * 		by a long integer.
	 */
	public static long binomialCoefficient(final int n, final int k) {
		org.apache.commons.math.util.MathUtils.checkBinomial(n, k);
		if ((n == k) || (k == 0)) {
			return 1;
		}
		if ((k == 1) || (k == (n - 1))) {
			return n;
		}
		// Use symmetry for large k
		if (k > (n / 2))
			return org.apache.commons.math.util.MathUtils.binomialCoefficient(n, n - k);

		// We use the formula
		// (n choose k) = n! / (n-k)! / k!
		// (n choose k) == ((n-k+1)*...*n) / (1*...*k)
		// which could be written
		// (n choose k) == (n-1 choose k-1) * n / k
		long result = 1;
		if (n <= 61) {
			// For n <= 61, the naive implementation cannot overflow.
			int i = (n - k) + 1;
			for (int j = 1; j <= k; j++) {
				result = (result * i) / j;
				i++;
			}
		} else if (n <= 66) {
			// For n > 61 but n <= 66, the result cannot overflow,
			// but we must take care not to overflow intermediate values.
			int i = (n - k) + 1;
			for (int j = 1; j <= k; j++) {
				// We know that (result * i) is divisible by j,
				// but (result * i) may overflow, so we split j:
				// Filter out the gcd, d, so j/d and i/d are integer.
				// result is divisible by (j/d) because (j/d)
				// is relative prime to (i/d) and is a divisor of
				// result * (i/d).
				final long d = org.apache.commons.math.util.MathUtils.gcd(i, j);
				result = (result / (j / d)) * (i / d);
				i++;
			}
		} else {
			// For n > 66, a result overflow might occur, so we check
			// the multiplication, taking care to not overflow
			// unnecessary.
			int i = (n - k) + 1;
			for (int j = 1; j <= k; j++) {
				final long d = org.apache.commons.math.util.MathUtils.gcd(i, j);
				result = org.apache.commons.math.util.MathUtils.mulAndCheck(result / (j / d), i / d);
				i++;
			}
		}
		return result;
	}

	/**
	 * Returns a <code>double</code> representation of the <a
	 * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
	 * Coefficient</a>, "<code>n choose k</code>", the number of
	 * <code>k</code>-element subsets that can be selected from an
	 * <code>n</code>-element set.
	 * <p>
	 * <Strong>Preconditions</strong>:
	 * <ul>
	 * <li> <code>0 <= k <= n </code> (otherwise
	 * <code>IllegalArgumentException</code> is thrown)</li>
	 * <li> The result is small enough to fit into a <code>double</code>. The
	 * largest value of <code>n</code> for which all coefficients are <
	 * Double.MAX_VALUE is 1029. If the computed value exceeds Double.MAX_VALUE,
	 * Double.POSITIVE_INFINITY is returned</li>
	 * </ul></p>
	 *
	 * @param n
	 * 		the size of the set
	 * @param k
	 * 		the size of the subsets to be counted
	 * @return <code>n choose k</code>
	 * @throws IllegalArgumentException
	 * 		if preconditions are not met.
	 */
	public static double binomialCoefficientDouble(final int n, final int k) {
		org.apache.commons.math.util.MathUtils.checkBinomial(n, k);
		if ((n == k) || (k == 0)) {
			return 1.0;
		}
		if ((k == 1) || (k == (n - 1))) {
			return n;
		}
		if (k > (n / 2)) {
			return org.apache.commons.math.util.MathUtils.binomialCoefficientDouble(n, n - k);
		}
		if (n < 67) {
			return org.apache.commons.math.util.MathUtils.binomialCoefficient(n, k);
		}
		double result = 1.0;
		for (int i = 1; i <= k; i++) {
			result *= ((double) ((n - k) + i)) / ((double) (i));
		}
		return java.lang.Math.floor(result + 0.5);
	}

	/**
	 * Returns the natural <code>log</code> of the <a
	 * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
	 * Coefficient</a>, "<code>n choose k</code>", the number of
	 * <code>k</code>-element subsets that can be selected from an
	 * <code>n</code>-element set.
	 * <p>
	 * <Strong>Preconditions</strong>:
	 * <ul>
	 * <li> <code>0 <= k <= n </code> (otherwise
	 * <code>IllegalArgumentException</code> is thrown)</li>
	 * </ul></p>
	 *
	 * @param n
	 * 		the size of the set
	 * @param k
	 * 		the size of the subsets to be counted
	 * @return <code>n choose k</code>
	 * @throws IllegalArgumentException
	 * 		if preconditions are not met.
	 */
	public static double binomialCoefficientLog(final int n, final int k) {
		org.apache.commons.math.util.MathUtils.checkBinomial(n, k);
		if ((n == k) || (k == 0)) {
			return 0;
		}
		if ((k == 1) || (k == (n - 1))) {
			return java.lang.Math.log(n);
		}
		/* For values small enough to do exact integer computation,
		return the log of the exact value
		 */
		if (n < 67) {
			return java.lang.Math.log(org.apache.commons.math.util.MathUtils.binomialCoefficient(n, k));
		}
		/* Return the log of binomialCoefficientDouble for values that will not
		overflow binomialCoefficientDouble
		 */
		if (n < 1030) {
			return java.lang.Math.log(org.apache.commons.math.util.MathUtils.binomialCoefficientDouble(n, k));
		}
		if (k > (n / 2)) {
			return org.apache.commons.math.util.MathUtils.binomialCoefficientLog(n, n - k);
		}
		/* Sum logs for values that could overflow */
		double logSum = 0;
		// n!/(n-k)!
		for (int i = (n - k) + 1; i <= n; i++) {
			logSum += java.lang.Math.log(i);
		}
		// divide by k!
		for (int i = 2; i <= k; i++) {
			logSum -= java.lang.Math.log(i);
		}
		return logSum;
	}

	/**
	 * Check binomial preconditions.
	 *
	 * @param n
	 * 		the size of the set
	 * @param k
	 * 		the size of the subsets to be counted
	 * @exception IllegalArgumentException
	 * 		if preconditions are not met.
	 */
	private static void checkBinomial(final int n, final int k) throws java.lang.IllegalArgumentException {
		if (n < k) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("must have n >= k for binomial coefficient (n,k), got n = {0}, k = {1}", n, k);
		}
		if (n < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("must have n >= 0 for binomial coefficient (n,k), got n = {0}", n);
		}
	}

	/**
	 * Compares two numbers given some amount of allowed error.
	 *
	 * @param x
	 * 		the first number
	 * @param y
	 * 		the second number
	 * @param eps
	 * 		the amount of error to allow when checking for equality
	 * @return <ul><li>0 if  {@link #equals(double, double, double) equals(x, y, eps)}</li>
	<li>&lt; 0 if !{@link #equals(double, double, double) equals(x, y, eps)} &amp;&amp; x &lt; y</li>
	<li>> 0 if !{@link #equals(double, double, double) equals(x, y, eps)} &amp;&amp; x > y</li></ul>
	 */
	public static int compareTo(double x, double y, double eps) {
		if (org.apache.commons.math.util.MathUtils.equals(x, y, eps)) {
			return 0;
		} else if (x < y) {
			return -1;
		}
		return 1;
	}

	/**
	 * Returns the <a href="http://mathworld.wolfram.com/HyperbolicCosine.html">
	 * hyperbolic cosine</a> of x.
	 *
	 * @param x
	 * 		double value for which to find the hyperbolic cosine
	 * @return hyperbolic cosine of x
	 */
	public static double cosh(double x) {
		return (java.lang.Math.exp(x) + java.lang.Math.exp(-x)) / 2.0;
	}

	/**
	 * Returns true iff both arguments are NaN or neither is NaN and they are
	 * equal
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @return true if the values are equal or both are NaN
	 */
	public static boolean equals(double x, double y) {
		return (java.lang.Double.isNaN(x) && java.lang.Double.isNaN(y)) || (x == y);
	}

	/**
	 * Returns true iff both arguments are equal or within the range of allowed
	 * error (inclusive).
	 * <p>
	 * Two NaNs are considered equals, as are two infinities with same sign.
	 * </p>
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @param eps
	 * 		the amount of absolute error to allow
	 * @return true if the values are equal or within range of each other
	 */
	public static boolean equals(double x, double y, double eps) {
		return org.apache.commons.math.util.MathUtils.equals(x, y) || (java.lang.Math.abs(y - x) <= eps);
	}

	/**
	 * Returns true iff both arguments are equal or within the range of allowed
	 * error (inclusive).
	 * Adapted from <a
	 * href="http://www.cygnus-software.com/papers/comparingfloats/comparingfloats.htm">
	 * Bruce Dawson</a>
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @param maxUlps
	 * 		{@code (maxUlps - 1)} is the number of floating point
	 * 		values between {@code x} and {@code y}.
	 * @return {@code true} if there are less than {@code maxUlps} floating
	point values between {@code x} and {@code y}
	 */
	public static boolean equals(double x, double y, int maxUlps) {
		// Check that "maxUlps" is non-negative and small enough so that the
		// default NAN won't compare as equal to anything.
		assert (maxUlps > 0) && (maxUlps < org.apache.commons.math.util.MathUtils.NAN_GAP);
		long xInt = java.lang.Double.doubleToLongBits(x);
		long yInt = java.lang.Double.doubleToLongBits(y);
		// Make lexicographically ordered as a two's-complement integer.
		if (xInt < 0) {
			xInt = org.apache.commons.math.util.MathUtils.SGN_MASK - xInt;
		}
		if (yInt < 0) {
			yInt = org.apache.commons.math.util.MathUtils.SGN_MASK - yInt;
		}
		return java.lang.Math.abs(xInt - yInt) <= maxUlps;
	}

	/**
	 * Returns true iff both arguments are null or have same dimensions
	 * and all their elements are {@link #equals(double,double) equals}
	 *
	 * @param x
	 * 		first array
	 * @param y
	 * 		second array
	 * @return true if the values are both null or have same dimension
	and equal elements
	 * @since 1.2
	 */
	public static boolean equals(double[] x, double[] y) {
		if ((x == null) || (y == null)) {
			return !((x == null) ^ (y == null));
		}
		if (x.length != y.length) {
			return false;
		}
		for (int i = 0; i < x.length; ++i) {
			if (!org.apache.commons.math.util.MathUtils.equals(x[i], y[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns n!. Shorthand for <code>n</code> <a
	 * href="http://mathworld.wolfram.com/Factorial.html"> Factorial</a>, the
	 * product of the numbers <code>1,...,n</code>.
	 * <p>
	 * <Strong>Preconditions</strong>:
	 * <ul>
	 * <li> <code>n >= 0</code> (otherwise
	 * <code>IllegalArgumentException</code> is thrown)</li>
	 * <li> The result is small enough to fit into a <code>long</code>. The
	 * largest value of <code>n</code> for which <code>n!</code> <
	 * Long.MAX_VALUE</code> is 20. If the computed value exceeds <code>Long.MAX_VALUE</code>
	 * an <code>ArithMeticException </code> is thrown.</li>
	 * </ul>
	 * </p>
	 *
	 * @param n
	 * 		argument
	 * @return <code>n!</code>
	 * @throws ArithmeticException
	 * 		if the result is too large to be represented
	 * 		by a long integer.
	 * @throws IllegalArgumentException
	 * 		if n < 0
	 */
	public static long factorial(final int n) {
		if (n < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("must have n >= 0 for n!, got n = {0}", n);
		}
		if (n > 20) {
			throw new java.lang.ArithmeticException("factorial value is too large to fit in a long");
		}
		return org.apache.commons.math.util.MathUtils.FACTORIALS[n];
	}

	/**
	 * Returns n!. Shorthand for <code>n</code> <a
	 * href="http://mathworld.wolfram.com/Factorial.html"> Factorial</a>, the
	 * product of the numbers <code>1,...,n</code> as a <code>double</code>.
	 * <p>
	 * <Strong>Preconditions</strong>:
	 * <ul>
	 * <li> <code>n >= 0</code> (otherwise
	 * <code>IllegalArgumentException</code> is thrown)</li>
	 * <li> The result is small enough to fit into a <code>double</code>. The
	 * largest value of <code>n</code> for which <code>n!</code> <
	 * Double.MAX_VALUE</code> is 170. If the computed value exceeds
	 * Double.MAX_VALUE, Double.POSITIVE_INFINITY is returned</li>
	 * </ul>
	 * </p>
	 *
	 * @param n
	 * 		argument
	 * @return <code>n!</code>
	 * @throws IllegalArgumentException
	 * 		if n < 0
	 */
	public static double factorialDouble(final int n) {
		if (n < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("must have n >= 0 for n!, got n = {0}", n);
		}
		if (n < 21) {
			return org.apache.commons.math.util.MathUtils.factorial(n);
		}
		return java.lang.Math.floor(java.lang.Math.exp(org.apache.commons.math.util.MathUtils.factorialLog(n)) + 0.5);
	}

	/**
	 * Returns the natural logarithm of n!.
	 * <p>
	 * <Strong>Preconditions</strong>:
	 * <ul>
	 * <li> <code>n >= 0</code> (otherwise
	 * <code>IllegalArgumentException</code> is thrown)</li>
	 * </ul></p>
	 *
	 * @param n
	 * 		argument
	 * @return <code>n!</code>
	 * @throws IllegalArgumentException
	 * 		if preconditions are not met.
	 */
	public static double factorialLog(final int n) {
		if (n < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("must have n >= 0 for n!, got n = {0}", n);
		}
		if (n < 21) {
			return java.lang.Math.log(org.apache.commons.math.util.MathUtils.factorial(n));
		}
		double logSum = 0;
		for (int i = 2; i <= n; i++) {
			logSum += java.lang.Math.log(i);
		}
		return logSum;
	}

	/**
	 * <p>
	 * Gets the greatest common divisor of the absolute value of two numbers,
	 * using the "binary gcd" method which avoids division and modulo
	 * operations. See Knuth 4.5.2 algorithm B. This algorithm is due to Josef
	 * Stein (1961).
	 * </p>
	 * Special cases:
	 * <ul>
	 * <li>The invocations
	 * <code>gcd(Integer.MIN_VALUE, Integer.MIN_VALUE)</code>,
	 * <code>gcd(Integer.MIN_VALUE, 0)</code> and
	 * <code>gcd(0, Integer.MIN_VALUE)</code> throw an
	 * <code>ArithmeticException</code>, because the result would be 2^31, which
	 * is too large for an int value.</li>
	 * <li>The result of <code>gcd(x, x)</code>, <code>gcd(0, x)</code> and
	 * <code>gcd(x, 0)</code> is the absolute value of <code>x</code>, except
	 * for the special cases above.
	 * <li>The invocation <code>gcd(0, 0)</code> is the only one which returns
	 * <code>0</code>.</li>
	 * </ul>
	 *
	 * @param p
	 * 		any number
	 * @param q
	 * 		any number
	 * @return the greatest common divisor, never negative
	 * @throws ArithmeticException
	 * 		if the result cannot be represented as a
	 * 		nonnegative int value
	 * @since 1.1
	 */
	public static int gcd(final int p, final int q) {
		int u = p;
		int v = q;
		if ((u == 0) || (v == 0)) {
			if ((u == java.lang.Integer.MIN_VALUE) || (v == java.lang.Integer.MIN_VALUE)) {
				throw org.apache.commons.math.MathRuntimeException.createArithmeticException("overflow: gcd({0}, {1}) is 2^31", p, q);
			}
			return java.lang.Math.abs(u) + java.lang.Math.abs(v);
		}
		// keep u and v negative, as negative integers range down to
		// -2^31, while positive numbers can only be as large as 2^31-1
		// (i.e. we can't necessarily negate a negative number without
		// overflow)
		/* assert u!=0 && v!=0; */
		if (u > 0) {
			u = -u;
		}// make u negative

		if (v > 0) {
			v = -v;
		}// make v negative

		// B1. [Find power of 2]
		int k = 0;
		while ((((u & 1) == 0) && ((v & 1) == 0)) && (k < 31)) {
			// while u and v are
			// both even...
			u /= 2;
			v /= 2;
			k++;// cast out twos.

		} 
		if (k == 31) {
			throw org.apache.commons.math.MathRuntimeException.createArithmeticException("overflow: gcd({0}, {1}) is 2^31", p, q);
		}
		// B2. Initialize: u and v have been divided by 2^k and at least
		// one is odd.
		/* B3 */
		int t = ((u & 1) == 1) ? v : -(u / 2);
		// t negative: u was odd, v may be even (t replaces v)
		// t positive: u was even, v is odd (t replaces u)
		do {
			/* assert u<0 && v<0; */
			// B4/B3: cast out twos from t.
			while ((t & 1) == 0) {
				// while t is even..
				t /= 2;// cast out twos

			} 
			// B5 [reset max(u,v)]
			if (t > 0) {
				u = -t;
			} else {
				v = t;
			}
			// B6/B3. at this point both u and v should be odd.
			t = (v - u) / 2;
			// |u| larger: t positive (replace u)
			// |v| larger: t negative (replace v)
		} while (t != 0 );
		return (-u) * (1 << k);// gcd is u*2^k

	}

	/**
	 * <p>
	 * Gets the greatest common divisor of the absolute value of two numbers,
	 * using the "binary gcd" method which avoids division and modulo
	 * operations. See Knuth 4.5.2 algorithm B. This algorithm is due to Josef
	 * Stein (1961).
	 * </p>
	 * Special cases:
	 * <ul>
	 * <li>The invocations
	 * <code>gcd(Long.MIN_VALUE, Long.MIN_VALUE)</code>,
	 * <code>gcd(Long.MIN_VALUE, 0L)</code> and
	 * <code>gcd(0L, Long.MIN_VALUE)</code> throw an
	 * <code>ArithmeticException</code>, because the result would be 2^63, which
	 * is too large for a long value.</li>
	 * <li>The result of <code>gcd(x, x)</code>, <code>gcd(0L, x)</code> and
	 * <code>gcd(x, 0L)</code> is the absolute value of <code>x</code>, except
	 * for the special cases above.
	 * <li>The invocation <code>gcd(0L, 0L)</code> is the only one which returns
	 * <code>0L</code>.</li>
	 * </ul>
	 *
	 * @param p
	 * 		any number
	 * @param q
	 * 		any number
	 * @return the greatest common divisor, never negative
	 * @throws ArithmeticException
	 * 		if the result cannot be represented as a nonnegative long
	 * 		value
	 * @since 2.1
	 */
	public static long gcd(final long p, final long q) {
		long u = p;
		long v = q;
		if ((u == 0) || (v == 0)) {
			if ((u == java.lang.Long.MIN_VALUE) || (v == java.lang.Long.MIN_VALUE)) {
				throw org.apache.commons.math.MathRuntimeException.createArithmeticException("overflow: gcd({0}, {1}) is 2^63", p, q);
			}
			return java.lang.Math.abs(u) + java.lang.Math.abs(v);
		}
		// keep u and v negative, as negative integers range down to
		// -2^63, while positive numbers can only be as large as 2^63-1
		// (i.e. we can't necessarily negate a negative number without
		// overflow)
		/* assert u!=0 && v!=0; */
		if (u > 0) {
			u = -u;
		}// make u negative

		if (v > 0) {
			v = -v;
		}// make v negative

		// B1. [Find power of 2]
		int k = 0;
		while ((((u & 1) == 0) && ((v & 1) == 0)) && (k < 63)) {
			// while u and v are
			// both even...
			u /= 2;
			v /= 2;
			k++;// cast out twos.

		} 
		if (k == 63) {
			throw org.apache.commons.math.MathRuntimeException.createArithmeticException("overflow: gcd({0}, {1}) is 2^63", p, q);
		}
		// B2. Initialize: u and v have been divided by 2^k and at least
		// one is odd.
		/* B3 */
		long t = ((u & 1) == 1) ? v : -(u / 2);
		// t negative: u was odd, v may be even (t replaces v)
		// t positive: u was even, v is odd (t replaces u)
		do {
			/* assert u<0 && v<0; */
			// B4/B3: cast out twos from t.
			while ((t & 1) == 0) {
				// while t is even..
				t /= 2;// cast out twos

			} 
			// B5 [reset max(u,v)]
			if (t > 0) {
				u = -t;
			} else {
				v = t;
			}
			// B6/B3. at this point both u and v should be odd.
			t = (v - u) / 2;
			// |u| larger: t positive (replace u)
			// |v| larger: t negative (replace v)
		} while (t != 0 );
		return (-u) * (1L << k);// gcd is u*2^k

	}

	/**
	 * Returns an integer hash code representing the given double value.
	 *
	 * @param value
	 * 		the value to be hashed
	 * @return the hash code
	 */
	public static int hash(double value) {
		return new java.lang.Double(value).hashCode();
	}

	/**
	 * Returns an integer hash code representing the given double array.
	 *
	 * @param value
	 * 		the value to be hashed (may be null)
	 * @return the hash code
	 * @since 1.2
	 */
	public static int hash(double[] value) {
		return java.util.Arrays.hashCode(value);
	}

	/**
	 * For a byte value x, this method returns (byte)(+1) if x >= 0 and
	 * (byte)(-1) if x < 0.
	 *
	 * @param x
	 * 		the value, a byte
	 * @return (byte)(+1) or (byte)(-1), depending on the sign of x
	 */
	public static byte indicator(final byte x) {
		return x >= org.apache.commons.math.util.MathUtils.ZB ? org.apache.commons.math.util.MathUtils.PB : org.apache.commons.math.util.MathUtils.NB;
	}

	/**
	 * For a double precision value x, this method returns +1.0 if x >= 0 and
	 * -1.0 if x < 0. Returns <code>NaN</code> if <code>x</code> is
	 * <code>NaN</code>.
	 *
	 * @param x
	 * 		the value, a double
	 * @return +1.0 or -1.0, depending on the sign of x
	 */
	public static double indicator(final double x) {
		if (java.lang.Double.isNaN(x)) {
			return java.lang.Double.NaN;
		}
		return x >= 0.0 ? 1.0 : -1.0;
	}

	/**
	 * For a float value x, this method returns +1.0F if x >= 0 and -1.0F if x <
	 * 0. Returns <code>NaN</code> if <code>x</code> is <code>NaN</code>.
	 *
	 * @param x
	 * 		the value, a float
	 * @return +1.0F or -1.0F, depending on the sign of x
	 */
	public static float indicator(final float x) {
		if (java.lang.Float.isNaN(x)) {
			return java.lang.Float.NaN;
		}
		return x >= 0.0F ? 1.0F : -1.0F;
	}

	/**
	 * For an int value x, this method returns +1 if x >= 0 and -1 if x < 0.
	 *
	 * @param x
	 * 		the value, an int
	 * @return +1 or -1, depending on the sign of x
	 */
	public static int indicator(final int x) {
		return x >= 0 ? 1 : -1;
	}

	/**
	 * For a long value x, this method returns +1L if x >= 0 and -1L if x < 0.
	 *
	 * @param x
	 * 		the value, a long
	 * @return +1L or -1L, depending on the sign of x
	 */
	public static long indicator(final long x) {
		return x >= 0L ? 1L : -1L;
	}

	/**
	 * For a short value x, this method returns (short)(+1) if x >= 0 and
	 * (short)(-1) if x < 0.
	 *
	 * @param x
	 * 		the value, a short
	 * @return (short)(+1) or (short)(-1), depending on the sign of x
	 */
	public static short indicator(final short x) {
		return x >= org.apache.commons.math.util.MathUtils.ZS ? org.apache.commons.math.util.MathUtils.PS : org.apache.commons.math.util.MathUtils.NS;
	}

	/**
	 * <p>
	 * Returns the least common multiple of the absolute value of two numbers,
	 * using the formula <code>lcm(a,b) = (a / gcd(a,b)) * b</code>.
	 * </p>
	 * Special cases:
	 * <ul>
	 * <li>The invocations <code>lcm(Integer.MIN_VALUE, n)</code> and
	 * <code>lcm(n, Integer.MIN_VALUE)</code>, where <code>abs(n)</code> is a
	 * power of 2, throw an <code>ArithmeticException</code>, because the result
	 * would be 2^31, which is too large for an int value.</li>
	 * <li>The result of <code>lcm(0, x)</code> and <code>lcm(x, 0)</code> is
	 * <code>0</code> for any <code>x</code>.
	 * </ul>
	 *
	 * @param a
	 * 		any number
	 * @param b
	 * 		any number
	 * @return the least common multiple, never negative
	 * @throws ArithmeticException
	 * 		if the result cannot be represented as a nonnegative int
	 * 		value
	 * @since 1.1
	 */
	public static int lcm(int a, int b) {
		if ((a == 0) || (b == 0)) {
			return 0;
		}
		int lcm = java.lang.Math.abs(org.apache.commons.math.util.MathUtils.mulAndCheck(a / org.apache.commons.math.util.MathUtils.gcd(a, b), b));
		if (lcm == java.lang.Integer.MIN_VALUE) {
			throw org.apache.commons.math.MathRuntimeException.createArithmeticException("overflow: lcm({0}, {1}) is 2^31", a, b);
		}
		return lcm;
	}

	/**
	 * <p>
	 * Returns the least common multiple of the absolute value of two numbers,
	 * using the formula <code>lcm(a,b) = (a / gcd(a,b)) * b</code>.
	 * </p>
	 * Special cases:
	 * <ul>
	 * <li>The invocations <code>lcm(Long.MIN_VALUE, n)</code> and
	 * <code>lcm(n, Long.MIN_VALUE)</code>, where <code>abs(n)</code> is a
	 * power of 2, throw an <code>ArithmeticException</code>, because the result
	 * would be 2^63, which is too large for an int value.</li>
	 * <li>The result of <code>lcm(0L, x)</code> and <code>lcm(x, 0L)</code> is
	 * <code>0L</code> for any <code>x</code>.
	 * </ul>
	 *
	 * @param a
	 * 		any number
	 * @param b
	 * 		any number
	 * @return the least common multiple, never negative
	 * @throws ArithmeticException
	 * 		if the result cannot be represented as a nonnegative long
	 * 		value
	 * @since 2.1
	 */
	public static long lcm(long a, long b) {
		if ((a == 0) || (b == 0)) {
			return 0;
		}
		long lcm = java.lang.Math.abs(org.apache.commons.math.util.MathUtils.mulAndCheck(a / org.apache.commons.math.util.MathUtils.gcd(a, b), b));
		if (lcm == java.lang.Long.MIN_VALUE) {
			throw org.apache.commons.math.MathRuntimeException.createArithmeticException("overflow: lcm({0}, {1}) is 2^63", a, b);
		}
		return lcm;
	}

	/**
	 * <p>Returns the
	 * <a href="http://mathworld.wolfram.com/Logarithm.html">logarithm</a>
	 * for base <code>b</code> of <code>x</code>.
	 * </p>
	 * <p>Returns <code>NaN<code> if either argument is negative.  If
	 * <code>base</code> is 0 and <code>x</code> is positive, 0 is returned.
	 * If <code>base</code> is positive and <code>x</code> is 0,
	 * <code>Double.NEGATIVE_INFINITY</code> is returned.  If both arguments
	 * are 0, the result is <code>NaN</code>.</p>
	 *
	 * @param base
	 * 		the base of the logarithm, must be greater than 0
	 * @param x
	 * 		argument, must be greater than 0
	 * @return the value of the logarithm - the number y such that base^y = x.
	 * @since 1.2
	 */
	public static double log(double base, double x) {
		return java.lang.Math.log(x) / java.lang.Math.log(base);
	}

	/**
	 * Multiply two integers, checking for overflow.
	 *
	 * @param x
	 * 		a factor
	 * @param y
	 * 		a factor
	 * @return the product <code>x*y</code>
	 * @throws ArithmeticException
	 * 		if the result can not be represented as an
	 * 		int
	 * @since 1.1
	 */
	public static int mulAndCheck(int x, int y) {
		long m = ((long) (x)) * ((long) (y));
		if ((m < java.lang.Integer.MIN_VALUE) || (m > java.lang.Integer.MAX_VALUE)) {
			throw new java.lang.ArithmeticException("overflow: mul");
		}
		return ((int) (m));
	}

	/**
	 * Multiply two long integers, checking for overflow.
	 *
	 * @param a
	 * 		first value
	 * @param b
	 * 		second value
	 * @return the product <code>a * b</code>
	 * @throws ArithmeticException
	 * 		if the result can not be represented as an
	 * 		long
	 * @since 1.2
	 */
	public static long mulAndCheck(long a, long b) {
		long ret;
		java.lang.String msg = "overflow: multiply";
		if (a > b) {
			// use symmetry to reduce boundary cases
			ret = org.apache.commons.math.util.MathUtils.mulAndCheck(b, a);
		} else if (a < 0) {
			if (b < 0) {
				// check for positive overflow with negative a, negative b
				if (a >= (java.lang.Long.MAX_VALUE / b)) {
					ret = a * b;
				} else {
					throw new java.lang.ArithmeticException(msg);
				}
			} else if (b > 0) {
				// check for negative overflow with negative a, positive b
				if ((java.lang.Long.MIN_VALUE / b) <= a) {
					ret = a * b;
				} else {
					throw new java.lang.ArithmeticException(msg);
				}
			} else {
				// assert b == 0
				ret = 0;
			}
		} else if (a > 0) {
			// assert a > 0
			// assert b > 0
			// check for positive overflow with positive a, positive b
			if (a <= (java.lang.Long.MAX_VALUE / b)) {
				ret = a * b;
			} else {
				throw new java.lang.ArithmeticException(msg);
			}
		} else {
			// assert a == 0
			ret = 0;
		}
		return ret;
	}

	/**
	 * Get the next machine representable number after a number, moving
	 * in the direction of another number.
	 * <p>
	 * If <code>direction</code> is greater than or equal to<code>d</code>,
	 * the smallest machine representable number strictly greater than
	 * <code>d</code> is returned; otherwise the largest representable number
	 * strictly less than <code>d</code> is returned.</p>
	 * <p>
	 * If <code>d</code> is NaN or Infinite, it is returned unchanged.</p>
	 *
	 * @param d
	 * 		base number
	 * @param direction
	 * 		(the only important thing is whether
	 * 		direction is greater or smaller than d)
	 * @return the next machine representable number in the specified direction
	 * @since 1.2
	 */
	public static double nextAfter(double d, double direction) {
		// handling of some important special cases
		if (java.lang.Double.isNaN(d) || java.lang.Double.isInfinite(d)) {
			return d;
		} else if (d == 0) {
			return direction < 0 ? -java.lang.Double.MIN_VALUE : java.lang.Double.MIN_VALUE;
		}
		// special cases MAX_VALUE to infinity and  MIN_VALUE to 0
		// are handled just as normal numbers
		// split the double in raw components
		long bits = java.lang.Double.doubleToLongBits(d);
		long sign = bits & 0x8000000000000000L;
		long exponent = bits & 0x7ff0000000000000L;
		long mantissa = bits & 0xfffffffffffffL;
		if ((d * (direction - d)) >= 0) {
			// we should increase the mantissa
			if (mantissa == 0xfffffffffffffL) {
				return java.lang.Double.longBitsToDouble(sign | (exponent + 0x10000000000000L));
			} else {
				return java.lang.Double.longBitsToDouble((sign | exponent) | (mantissa + 1));
			}
		} else // we should decrease the mantissa
		if (mantissa == 0L) {
			return java.lang.Double.longBitsToDouble((sign | (exponent - 0x10000000000000L)) | 0xfffffffffffffL);
		} else {
			return java.lang.Double.longBitsToDouble((sign | exponent) | (mantissa - 1));
		}
	}

	/**
	 * Scale a number by 2<sup>scaleFactor</sup>.
	 * <p>If <code>d</code> is 0 or NaN or Infinite, it is returned unchanged.</p>
	 *
	 * @param d
	 * 		base number
	 * @param scaleFactor
	 * 		power of two by which d sould be multiplied
	 * @return d &times; 2<sup>scaleFactor</sup>
	 * @since 2.0
	 */
	public static double scalb(final double d, final int scaleFactor) {
		// handling of some important special cases
		if (((d == 0) || java.lang.Double.isNaN(d)) || java.lang.Double.isInfinite(d)) {
			return d;
		}
		// split the double in raw components
		final long bits = java.lang.Double.doubleToLongBits(d);
		final long exponent = bits & 0x7ff0000000000000L;
		final long rest = bits & 0x800fffffffffffffL;
		// shift the exponent
		final long newBits = rest | (exponent + (((long) (scaleFactor)) << 52));
		return java.lang.Double.longBitsToDouble(newBits);
	}

	/**
	 * Normalize an angle in a 2&pi wide interval around a center value.
	 * <p>This method has three main uses:</p>
	 * <ul>
	 *   <li>normalize an angle between 0 and 2&pi;:<br/>
	 *       <code>a = MathUtils.normalizeAngle(a, Math.PI);</code></li>
	 *   <li>normalize an angle between -&pi; and +&pi;<br/>
	 *       <code>a = MathUtils.normalizeAngle(a, 0.0);</code></li>
	 *   <li>compute the angle between two defining angular positions:<br>
	 *       <code>angle = MathUtils.normalizeAngle(end, start) - start;</code></li>
	 * </ul>
	 * <p>Note that due to numerical accuracy and since &pi; cannot be represented
	 * exactly, the result interval is <em>closed</em>, it cannot be half-closed
	 * as would be more satisfactory in a purely mathematical view.</p>
	 *
	 * @param a
	 * 		angle to normalize
	 * @param center
	 * 		center of the desired 2&pi; interval for the result
	 * @return a-2k&pi; with integer k and center-&pi; &lt;= a-2k&pi; &lt;= center+&pi;
	 * @since 1.2
	 */
	public static double normalizeAngle(double a, double center) {
		return a - (org.apache.commons.math.util.MathUtils.TWO_PI * java.lang.Math.floor(((a + java.lang.Math.PI) - center) / org.apache.commons.math.util.MathUtils.TWO_PI));
	}

	/**
	 * <p>Normalizes an array to make it sum to a specified value.
	 * Returns the result of the transformation <pre>
	 *    x |-> x * normalizedSum / sum
	 * </pre>
	 * applied to each non-NaN element x of the input array, where sum is the
	 * sum of the non-NaN entries in the input array.</p>
	 *
	 * <p>Throws IllegalArgumentException if <code>normalizedSum</code> is infinite
	 * or NaN and ArithmeticException if the input array contains any infinite elements
	 * or sums to 0</p>
	 *
	 * <p>Ignores (i.e., copies unchanged to the output array) NaNs in the input array.</p>
	 *
	 * @param values
	 * 		input array to be normalized
	 * @param normalizedSum
	 * 		target sum for the normalized array
	 * @return normalized array
	 * @throws ArithmeticException
	 * 		if the input array contains infinite elements or sums to zero
	 * @throws IllegalArgumentException
	 * 		if the target sum is infinite or NaN
	 * @since 2.1
	 */
	public static double[] normalizeArray(double[] values, double normalizedSum) throws java.lang.ArithmeticException, java.lang.IllegalArgumentException {
		if (java.lang.Double.isInfinite(normalizedSum)) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("Cannot normalize to an infinite value");
		}
		if (java.lang.Double.isNaN(normalizedSum)) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("Cannot normalize to NaN");
		}
		double sum = 0.0;
		final int len = values.length;
		double[] out = new double[len];
		for (int i = 0; i < len; i++) {
			if (java.lang.Double.isInfinite(values[i])) {
				throw org.apache.commons.math.MathRuntimeException.createArithmeticException("Array contains an infinite element, {0} at index {1}", values[i], i);
			}
			if (!java.lang.Double.isNaN(values[i])) {
				sum += values[i];
			}
		}
		if (sum == 0) {
			throw org.apache.commons.math.MathRuntimeException.createArithmeticException("Array sums to zero");
		}
		for (int i = 0; i < len; i++) {
			if (java.lang.Double.isNaN(values[i])) {
				out[i] = java.lang.Double.NaN;
			} else {
				out[i] = (values[i] * normalizedSum) / sum;
			}
		}
		return out;
	}

	/**
	 * Round the given value to the specified number of decimal places. The
	 * value is rounded using the {@link BigDecimal#ROUND_HALF_UP} method.
	 *
	 * @param x
	 * 		the value to round.
	 * @param scale
	 * 		the number of digits to the right of the decimal point.
	 * @return the rounded value.
	 * @since 1.1
	 */
	public static double round(double x, int scale) {
		return org.apache.commons.math.util.MathUtils.round(x, scale, java.math.BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Round the given value to the specified number of decimal places. The
	 * value is rounded using the given method which is any method defined in
	 * {@link BigDecimal}.
	 *
	 * @param x
	 * 		the value to round.
	 * @param scale
	 * 		the number of digits to the right of the decimal point.
	 * @param roundingMethod
	 * 		the rounding method as defined in
	 * 		{@link BigDecimal}.
	 * @return the rounded value.
	 * @since 1.1
	 */
	public static double round(double x, int scale, int roundingMethod) {
		try {
			return new java.math.BigDecimal(java.lang.Double.toString(x)).setScale(scale, roundingMethod).doubleValue();
		} catch (java.lang.NumberFormatException ex) {
			if (java.lang.Double.isInfinite(x)) {
				return x;
			} else {
				return java.lang.Double.NaN;
			}
		}
	}

	/**
	 * Round the given value to the specified number of decimal places. The
	 * value is rounding using the {@link BigDecimal#ROUND_HALF_UP} method.
	 *
	 * @param x
	 * 		the value to round.
	 * @param scale
	 * 		the number of digits to the right of the decimal point.
	 * @return the rounded value.
	 * @since 1.1
	 */
	public static float round(float x, int scale) {
		return org.apache.commons.math.util.MathUtils.round(x, scale, java.math.BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Round the given value to the specified number of decimal places. The
	 * value is rounded using the given method which is any method defined in
	 * {@link BigDecimal}.
	 *
	 * @param x
	 * 		the value to round.
	 * @param scale
	 * 		the number of digits to the right of the decimal point.
	 * @param roundingMethod
	 * 		the rounding method as defined in
	 * 		{@link BigDecimal}.
	 * @return the rounded value.
	 * @since 1.1
	 */
	public static float round(float x, int scale, int roundingMethod) {
		float sign = org.apache.commons.math.util.MathUtils.indicator(x);
		float factor = ((float) (java.lang.Math.pow(10.0F, scale))) * sign;
		return ((float) (org.apache.commons.math.util.MathUtils.roundUnscaled(x * factor, sign, roundingMethod))) / factor;
	}

	/**
	 * Round the given non-negative, value to the "nearest" integer. Nearest is
	 * determined by the rounding method specified. Rounding methods are defined
	 * in {@link BigDecimal}.
	 *
	 * @param unscaled
	 * 		the value to round.
	 * @param sign
	 * 		the sign of the original, scaled value.
	 * @param roundingMethod
	 * 		the rounding method as defined in
	 * 		{@link BigDecimal}.
	 * @return the rounded value.
	 * @since 1.1
	 */
	private static double roundUnscaled(double unscaled, double sign, int roundingMethod) {
		switch (roundingMethod) {
			case java.math.BigDecimal.ROUND_CEILING :
				if (sign == (-1)) {
					unscaled = java.lang.Math.floor(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY));
				} else {
					unscaled = java.lang.Math.ceil(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY));
				}
				break;
			case java.math.BigDecimal.ROUND_DOWN :
				unscaled = java.lang.Math.floor(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY));
				break;
			case java.math.BigDecimal.ROUND_FLOOR :
				if (sign == (-1)) {
					unscaled = java.lang.Math.ceil(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY));
				} else {
					unscaled = java.lang.Math.floor(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY));
				}
				break;
			case java.math.BigDecimal.ROUND_HALF_DOWN :
				{
					unscaled = org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY);
					double fraction = unscaled - java.lang.Math.floor(unscaled);
					if (fraction > 0.5) {
						unscaled = java.lang.Math.ceil(unscaled);
					} else {
						unscaled = java.lang.Math.floor(unscaled);
					}
					break;
				}
			case java.math.BigDecimal.ROUND_HALF_EVEN :
				{
					double fraction = unscaled - java.lang.Math.floor(unscaled);
					if (fraction > 0.5) {
						unscaled = java.lang.Math.ceil(unscaled);
					} else if (fraction < 0.5) {
						unscaled = java.lang.Math.floor(unscaled);
					} else // The following equality test is intentional and needed for rounding purposes
					if ((java.lang.Math.floor(unscaled) / 2.0) == java.lang.Math.floor(java.lang.Math.floor(unscaled) / 2.0)) {
						// even
						unscaled = java.lang.Math.floor(unscaled);
					} else {
						// odd
						unscaled = java.lang.Math.ceil(unscaled);
					}
					break;
				}
			case java.math.BigDecimal.ROUND_HALF_UP :
				{
					unscaled = org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY);
					double fraction = unscaled - java.lang.Math.floor(unscaled);
					if (fraction >= 0.5) {
						unscaled = java.lang.Math.ceil(unscaled);
					} else {
						unscaled = java.lang.Math.floor(unscaled);
					}
					break;
				}
			case java.math.BigDecimal.ROUND_UNNECESSARY :
				if (unscaled != java.lang.Math.floor(unscaled)) {
					throw new java.lang.ArithmeticException("Inexact result from rounding");
				}
				break;
			case java.math.BigDecimal.ROUND_UP :
				unscaled = java.lang.Math.ceil(org.apache.commons.math.util.MathUtils.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY));
				break;
			default :
				throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("invalid rounding method {0}, valid methods: {1} ({2}), {3} ({4})," + " {5} ({6}), {7} ({8}), {9} ({10}), {11} ({12}), {13} ({14}), {15} ({16})", roundingMethod, "ROUND_CEILING", java.math.BigDecimal.ROUND_CEILING, "ROUND_DOWN", java.math.BigDecimal.ROUND_DOWN, "ROUND_FLOOR", java.math.BigDecimal.ROUND_FLOOR, "ROUND_HALF_DOWN", java.math.BigDecimal.ROUND_HALF_DOWN, "ROUND_HALF_EVEN", java.math.BigDecimal.ROUND_HALF_EVEN, "ROUND_HALF_UP", java.math.BigDecimal.ROUND_HALF_UP, "ROUND_UNNECESSARY", java.math.BigDecimal.ROUND_UNNECESSARY, "ROUND_UP", java.math.BigDecimal.ROUND_UP);
		}
		return unscaled;
	}

	/**
	 * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
	 * for byte value <code>x</code>.
	 * <p>
	 * For a byte value x, this method returns (byte)(+1) if x > 0, (byte)(0) if
	 * x = 0, and (byte)(-1) if x < 0.</p>
	 *
	 * @param x
	 * 		the value, a byte
	 * @return (byte)(+1), (byte)(0), or (byte)(-1), depending on the sign of x
	 */
	public static byte sign(final byte x) {
		return x == org.apache.commons.math.util.MathUtils.ZB ? org.apache.commons.math.util.MathUtils.ZB : x > org.apache.commons.math.util.MathUtils.ZB ? org.apache.commons.math.util.MathUtils.PB : org.apache.commons.math.util.MathUtils.NB;
	}

	/**
	 * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
	 * for double precision <code>x</code>.
	 * <p>
	 * For a double value <code>x</code>, this method returns
	 * <code>+1.0</code> if <code>x > 0</code>, <code>0.0</code> if
	 * <code>x = 0.0</code>, and <code>-1.0</code> if <code>x < 0</code>.
	 * Returns <code>NaN</code> if <code>x</code> is <code>NaN</code>.</p>
	 *
	 * @param x
	 * 		the value, a double
	 * @return +1.0, 0.0, or -1.0, depending on the sign of x
	 */
	public static double sign(final double x) {
		if (java.lang.Double.isNaN(x)) {
			return java.lang.Double.NaN;
		}
		return x == 0.0 ? 0.0 : x > 0.0 ? 1.0 : -1.0;
	}

	/**
	 * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
	 * for float value <code>x</code>.
	 * <p>
	 * For a float value x, this method returns +1.0F if x > 0, 0.0F if x =
	 * 0.0F, and -1.0F if x < 0. Returns <code>NaN</code> if <code>x</code>
	 * is <code>NaN</code>.</p>
	 *
	 * @param x
	 * 		the value, a float
	 * @return +1.0F, 0.0F, or -1.0F, depending on the sign of x
	 */
	public static float sign(final float x) {
		if (java.lang.Float.isNaN(x)) {
			return java.lang.Float.NaN;
		}
		return x == 0.0F ? 0.0F : x > 0.0F ? 1.0F : -1.0F;
	}

	/**
	 * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
	 * for int value <code>x</code>.
	 * <p>
	 * For an int value x, this method returns +1 if x > 0, 0 if x = 0, and -1
	 * if x < 0.</p>
	 *
	 * @param x
	 * 		the value, an int
	 * @return +1, 0, or -1, depending on the sign of x
	 */
	public static int sign(final int x) {
		return x == 0 ? 0 : x > 0 ? 1 : -1;
	}

	/**
	 * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
	 * for long value <code>x</code>.
	 * <p>
	 * For a long value x, this method returns +1L if x > 0, 0L if x = 0, and
	 * -1L if x < 0.</p>
	 *
	 * @param x
	 * 		the value, a long
	 * @return +1L, 0L, or -1L, depending on the sign of x
	 */
	public static long sign(final long x) {
		return x == 0L ? 0L : x > 0L ? 1L : -1L;
	}

	/**
	 * Returns the <a href="http://mathworld.wolfram.com/Sign.html"> sign</a>
	 * for short value <code>x</code>.
	 * <p>
	 * For a short value x, this method returns (short)(+1) if x > 0, (short)(0)
	 * if x = 0, and (short)(-1) if x < 0.</p>
	 *
	 * @param x
	 * 		the value, a short
	 * @return (short)(+1), (short)(0), or (short)(-1), depending on the sign of
	x
	 */
	public static short sign(final short x) {
		return x == org.apache.commons.math.util.MathUtils.ZS ? org.apache.commons.math.util.MathUtils.ZS : x > org.apache.commons.math.util.MathUtils.ZS ? org.apache.commons.math.util.MathUtils.PS : org.apache.commons.math.util.MathUtils.NS;
	}

	/**
	 * Returns the <a href="http://mathworld.wolfram.com/HyperbolicSine.html">
	 * hyperbolic sine</a> of x.
	 *
	 * @param x
	 * 		double value for which to find the hyperbolic sine
	 * @return hyperbolic sine of x
	 */
	public static double sinh(double x) {
		return (java.lang.Math.exp(x) - java.lang.Math.exp(-x)) / 2.0;
	}

	/**
	 * Subtract two integers, checking for overflow.
	 *
	 * @param x
	 * 		the minuend
	 * @param y
	 * 		the subtrahend
	 * @return the difference <code>x-y</code>
	 * @throws ArithmeticException
	 * 		if the result can not be represented as an
	 * 		int
	 * @since 1.1
	 */
	public static int subAndCheck(int x, int y) {
		long s = ((long) (x)) - ((long) (y));
		if ((s < java.lang.Integer.MIN_VALUE) || (s > java.lang.Integer.MAX_VALUE)) {
			throw new java.lang.ArithmeticException("overflow: subtract");
		}
		return ((int) (s));
	}

	/**
	 * Subtract two long integers, checking for overflow.
	 *
	 * @param a
	 * 		first value
	 * @param b
	 * 		second value
	 * @return the difference <code>a-b</code>
	 * @throws ArithmeticException
	 * 		if the result can not be represented as an
	 * 		long
	 * @since 1.2
	 */
	public static long subAndCheck(long a, long b) {
		long ret;
		java.lang.String msg = "overflow: subtract";
		if (b == java.lang.Long.MIN_VALUE) {
			if (a < 0) {
				ret = a - b;
			} else {
				throw new java.lang.ArithmeticException(msg);
			}
		} else {
			// use additive inverse
			ret = org.apache.commons.math.util.MathUtils.addAndCheck(a, -b, msg);
		}
		return ret;
	}

	/**
	 * Raise an int to an int power.
	 *
	 * @param k
	 * 		number to raise
	 * @param e
	 * 		exponent (must be positive or null)
	 * @return k<sup>e</sup>
	 * @exception IllegalArgumentException
	 * 		if e is negative
	 */
	public static int pow(final int k, int e) throws java.lang.IllegalArgumentException {
		if (e < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("cannot raise an integral value to a negative power ({0}^{1})", k, e);
		}
		int result = 1;
		int k2p = k;
		while (e != 0) {
			if ((e & 0x1) != 0) {
				result *= k2p;
			}
			k2p *= k2p;
			e = e >> 1;
		} 
		return result;
	}

	/**
	 * Raise an int to a long power.
	 *
	 * @param k
	 * 		number to raise
	 * @param e
	 * 		exponent (must be positive or null)
	 * @return k<sup>e</sup>
	 * @exception IllegalArgumentException
	 * 		if e is negative
	 */
	public static int pow(final int k, long e) throws java.lang.IllegalArgumentException {
		if (e < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("cannot raise an integral value to a negative power ({0}^{1})", k, e);
		}
		int result = 1;
		int k2p = k;
		while (e != 0) {
			if ((e & 0x1) != 0) {
				result *= k2p;
			}
			k2p *= k2p;
			e = e >> 1;
		} 
		return result;
	}

	/**
	 * Raise a long to an int power.
	 *
	 * @param k
	 * 		number to raise
	 * @param e
	 * 		exponent (must be positive or null)
	 * @return k<sup>e</sup>
	 * @exception IllegalArgumentException
	 * 		if e is negative
	 */
	public static long pow(final long k, int e) throws java.lang.IllegalArgumentException {
		if (e < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("cannot raise an integral value to a negative power ({0}^{1})", k, e);
		}
		long result = 1L;
		long k2p = k;
		while (e != 0) {
			if ((e & 0x1) != 0) {
				result *= k2p;
			}
			k2p *= k2p;
			e = e >> 1;
		} 
		return result;
	}

	/**
	 * Raise a long to a long power.
	 *
	 * @param k
	 * 		number to raise
	 * @param e
	 * 		exponent (must be positive or null)
	 * @return k<sup>e</sup>
	 * @exception IllegalArgumentException
	 * 		if e is negative
	 */
	public static long pow(final long k, long e) throws java.lang.IllegalArgumentException {
		if (e < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("cannot raise an integral value to a negative power ({0}^{1})", k, e);
		}
		long result = 1L;
		long k2p = k;
		while (e != 0) {
			if ((e & 0x1) != 0) {
				result *= k2p;
			}
			k2p *= k2p;
			e = e >> 1;
		} 
		return result;
	}

	/**
	 * Raise a BigInteger to an int power.
	 *
	 * @param k
	 * 		number to raise
	 * @param e
	 * 		exponent (must be positive or null)
	 * @return k<sup>e</sup>
	 * @exception IllegalArgumentException
	 * 		if e is negative
	 */
	public static java.math.BigInteger pow(final java.math.BigInteger k, int e) throws java.lang.IllegalArgumentException {
		if (e < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("cannot raise an integral value to a negative power ({0}^{1})", k, e);
		}
		return k.pow(e);
	}

	/**
	 * Raise a BigInteger to a long power.
	 *
	 * @param k
	 * 		number to raise
	 * @param e
	 * 		exponent (must be positive or null)
	 * @return k<sup>e</sup>
	 * @exception IllegalArgumentException
	 * 		if e is negative
	 */
	public static java.math.BigInteger pow(final java.math.BigInteger k, long e) throws java.lang.IllegalArgumentException {
		if (e < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("cannot raise an integral value to a negative power ({0}^{1})", k, e);
		}
		java.math.BigInteger result = java.math.BigInteger.ONE;
		java.math.BigInteger k2p = k;
		while (e != 0) {
			if ((e & 0x1) != 0) {
				result = result.multiply(k2p);
			}
			k2p = k2p.multiply(k2p);
			e = e >> 1;
		} 
		return result;
	}

	/**
	 * Raise a BigInteger to a BigInteger power.
	 *
	 * @param k
	 * 		number to raise
	 * @param e
	 * 		exponent (must be positive or null)
	 * @return k<sup>e</sup>
	 * @exception IllegalArgumentException
	 * 		if e is negative
	 */
	public static java.math.BigInteger pow(final java.math.BigInteger k, java.math.BigInteger e) throws java.lang.IllegalArgumentException {
		if (e.compareTo(java.math.BigInteger.ZERO) < 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("cannot raise an integral value to a negative power ({0}^{1})", k, e);
		}
		java.math.BigInteger result = java.math.BigInteger.ONE;
		java.math.BigInteger k2p = k;
		while (!java.math.BigInteger.ZERO.equals(e)) {
			if (e.testBit(0)) {
				result = result.multiply(k2p);
			}
			k2p = k2p.multiply(k2p);
			e = e.shiftRight(1);
		} 
		return result;
	}

	/**
	 * Calculates the L<sub>1</sub> (sum of abs) distance between two points.
	 *
	 * @param p1
	 * 		the first point
	 * @param p2
	 * 		the second point
	 * @return the L<sub>1</sub> distance between the two points
	 */
	public static double distance1(double[] p1, double[] p2) {
		double sum = 0;
		for (int i = 0; i < p1.length; i++) {
			sum += java.lang.Math.abs(p1[i] - p2[i]);
		}
		return sum;
	}

	/**
	 * Calculates the L<sub>1</sub> (sum of abs) distance between two points.
	 *
	 * @param p1
	 * 		the first point
	 * @param p2
	 * 		the second point
	 * @return the L<sub>1</sub> distance between the two points
	 */
	public static int distance1(int[] p1, int[] p2) {
		int sum = 0;
		for (int i = 0; i < p1.length; i++) {
			sum += java.lang.Math.abs(p1[i] - p2[i]);
		}
		return sum;
	}

	/**
	 * Calculates the L<sub>2</sub> (Euclidean) distance between two points.
	 *
	 * @param p1
	 * 		the first point
	 * @param p2
	 * 		the second point
	 * @return the L<sub>2</sub> distance between the two points
	 */
	public static double distance(double[] p1, double[] p2) {
		double sum = 0;
		for (int i = 0; i < p1.length; i++) {
			final double dp = p1[i] - p2[i];
			sum += dp * dp;
		}
		return java.lang.Math.sqrt(sum);
	}

	/**
	 * Calculates the L<sub>2</sub> (Euclidean) distance between two points.
	 *
	 * @param p1
	 * 		the first point
	 * @param p2
	 * 		the second point
	 * @return the L<sub>2</sub> distance between the two points
	 */
	public static double distance(int[] p1, int[] p2) {
		double sum = 0;
		for (int i = 0; i < p1.length; i++) {
			final double dp = p1[i] - p2[i];
			sum += dp * dp;
		}
		return java.lang.Math.sqrt(sum);
	}

	/**
	 * Calculates the L<sub>&infin;</sub> (max of abs) distance between two points.
	 *
	 * @param p1
	 * 		the first point
	 * @param p2
	 * 		the second point
	 * @return the L<sub>&infin;</sub> distance between the two points
	 */
	public static double distanceInf(double[] p1, double[] p2) {
		double max = 0;
		for (int i = 0; i < p1.length; i++) {
			max = java.lang.Math.max(max, java.lang.Math.abs(p1[i] - p2[i]));
		}
		return max;
	}

	/**
	 * Calculates the L<sub>&infin;</sub> (max of abs) distance between two points.
	 *
	 * @param p1
	 * 		the first point
	 * @param p2
	 * 		the second point
	 * @return the L<sub>&infin;</sub> distance between the two points
	 */
	public static int distanceInf(int[] p1, int[] p2) {
		int max = 0;
		for (int i = 0; i < p1.length; i++) {
			max = java.lang.Math.max(max, java.lang.Math.abs(p1[i] - p2[i]));
		}
		return max;
	}

	/**
	 * Checks that the given array is sorted.
	 *
	 * @param val
	 * 		Values
	 * @param dir
	 * 		Order direction (-1 for decreasing, 1 for increasing)
	 * @param strict
	 * 		Whether the order should be strict
	 * @throws IllegalArgumentException
	 * 		if the array is not sorted.
	 */
	public static void checkOrder(double[] val, int dir, boolean strict) {
		double previous = val[0];
		int max = val.length;
		for (int i = 1; i < max; i++) {
			if (dir > 0) {
				if (strict) {
					if (val[i] <= previous) {
						throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("points {0} and {1} are not strictly increasing ({2} >= {3})", i - 1, i, previous, val[i]);
					}
				} else if (val[i] < previous) {
					throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("points {0} and {1} are not increasing ({2} > {3})", i - 1, i, previous, val[i]);
				}
			} else if (strict) {
				if (val[i] >= previous) {
					throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("points {0} and {1} are not strictly decreasing ({2} <= {3})", i - 1, i, previous, val[i]);
				}
			} else if (val[i] > previous) {
				throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("points {0} and {1} are not decreasing ({2} < {3})", i - 1, i, previous, val[i]);
			}
			previous = val[i];
		}
	}
}