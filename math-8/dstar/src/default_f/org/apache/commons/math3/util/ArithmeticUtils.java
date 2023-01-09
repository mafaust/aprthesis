package org.apache.commons.math3.util;
/**
 * Some useful, arithmetics related, additions to the built-in functions in
 * {@link Math}.
 *
 * @version $Id$
 */
public final class ArithmeticUtils {
	/**
	 * All long-representable factorials
	 */
	static final long[] FACTORIALS = new long[]{ 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };

	/**
	 * Stirling numbers of the second kind.
	 */
	static final java.util.concurrent.atomic.AtomicReference<long[][]> STIRLING_S2 = new java.util.concurrent.atomic.AtomicReference<long[][]>(null);

	/**
	 * Private constructor.
	 */
	private ArithmeticUtils() {
		super();
	}

	/**
	 * Add two integers, checking for overflow.
	 *
	 * @param x
	 * 		an addend
	 * @param y
	 * 		an addend
	 * @return the sum {@code x+y}
	 * @throws MathArithmeticException
	 * 		if the result can not be represented
	 * 		as an {@code int}.
	 * @since 1.1
	 */
	public static int addAndCheck(int x, int y) throws org.apache.commons.math3.exception.MathArithmeticException {
		long s = ((long) (x)) + ((long) (y));
		if ((s < java.lang.Integer.MIN_VALUE) || (s > java.lang.Integer.MAX_VALUE)) {
			throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.OVERFLOW_IN_ADDITION, x, y);
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
	 * @return the sum {@code a+b}
	 * @throws MathArithmeticException
	 * 		if the result can not be represented as an
	 * 		long
	 * @since 1.2
	 */
	public static long addAndCheck(long a, long b) throws org.apache.commons.math3.exception.MathArithmeticException {
		return org.apache.commons.math3.util.ArithmeticUtils.addAndCheck(a, b, org.apache.commons.math3.exception.util.LocalizedFormats.OVERFLOW_IN_ADDITION);
	}

	/**
	 * Returns an exact representation of the <a
	 * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
	 * Coefficient</a>, "{@code n choose k}", the number of
	 * {@code k}-element subsets that can be selected from an
	 * {@code n}-element set.
	 * <p>
	 * <Strong>Preconditions</strong>:
	 * <ul>
	 * <li> {@code 0 <= k <= n} (otherwise
	 * {@code IllegalArgumentException} is thrown)</li>
	 * <li> The result is small enough to fit into a {@code long}. The
	 * largest value of {@code n} for which all coefficients are
	 * {@code < Long.MAX_VALUE} is 66. If the computed value exceeds
	 * {@code Long.MAX_VALUE} an {@code ArithMeticException} is
	 * thrown.</li>
	 * </ul></p>
	 *
	 * @param n
	 * 		the size of the set
	 * @param k
	 * 		the size of the subsets to be counted
	 * @return {@code n choose k}
	 * @throws NotPositiveException
	 * 		if {@code n < 0}.
	 * @throws NumberIsTooLargeException
	 * 		if {@code k > n}.
	 * @throws MathArithmeticException
	 * 		if the result is too large to be
	 * 		represented by a long integer.
	 */
	public static long binomialCoefficient(final int n, final int k) throws org.apache.commons.math3.exception.NotPositiveException, org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.MathArithmeticException {
		org.apache.commons.math3.util.ArithmeticUtils.checkBinomial(n, k);
		if ((n == k) || (k == 0)) {
			return 1;
		}
		if ((k == 1) || (k == (n - 1))) {
			return n;
		}
		// Use symmetry for large k
		if (k > (n / 2)) {
			return org.apache.commons.math3.util.ArithmeticUtils.binomialCoefficient(n, n - k);
		}
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
				final long d = org.apache.commons.math3.util.ArithmeticUtils.gcd(i, j);
				result = (result / (j / d)) * (i / d);
				i++;
			}
		} else {
			// For n > 66, a result overflow might occur, so we check
			// the multiplication, taking care to not overflow
			// unnecessary.
			int i = (n - k) + 1;
			for (int j = 1; j <= k; j++) {
				final long d = org.apache.commons.math3.util.ArithmeticUtils.gcd(i, j);
				result = org.apache.commons.math3.util.ArithmeticUtils.mulAndCheck(result / (j / d), i / d);
				i++;
			}
		}
		return result;
	}

	/**
	 * Returns a {@code double} representation of the <a
	 * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
	 * Coefficient</a>, "{@code n choose k}", the number of
	 * {@code k}-element subsets that can be selected from an
	 * {@code n}-element set.
	 * <p>
	 * <Strong>Preconditions</strong>:
	 * <ul>
	 * <li> {@code 0 <= k <= n} (otherwise
	 * {@code IllegalArgumentException} is thrown)</li>
	 * <li> The result is small enough to fit into a {@code double}. The
	 * largest value of {@code n} for which all coefficients are <
	 * Double.MAX_VALUE is 1029. If the computed value exceeds Double.MAX_VALUE,
	 * Double.POSITIVE_INFINITY is returned</li>
	 * </ul></p>
	 *
	 * @param n
	 * 		the size of the set
	 * @param k
	 * 		the size of the subsets to be counted
	 * @return {@code n choose k}
	 * @throws NotPositiveException
	 * 		if {@code n < 0}.
	 * @throws NumberIsTooLargeException
	 * 		if {@code k > n}.
	 * @throws MathArithmeticException
	 * 		if the result is too large to be
	 * 		represented by a long integer.
	 */
	public static double binomialCoefficientDouble(final int n, final int k) throws org.apache.commons.math3.exception.NotPositiveException, org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.MathArithmeticException {
		org.apache.commons.math3.util.ArithmeticUtils.checkBinomial(n, k);
		if ((n == k) || (k == 0)) {
			return 1.0;
		}
		if ((k == 1) || (k == (n - 1))) {
			return n;
		}
		if (k > (n / 2)) {
			return org.apache.commons.math3.util.ArithmeticUtils.binomialCoefficientDouble(n, n - k);
		}
		if (n < 67) {
			return org.apache.commons.math3.util.ArithmeticUtils.binomialCoefficient(n, k);
		}
		double result = 1.0;
		for (int i = 1; i <= k; i++) {
			result *= ((double) ((n - k) + i)) / ((double) (i));
		}
		return org.apache.commons.math3.util.FastMath.floor(result + 0.5);
	}

	/**
	 * Returns the natural {@code log} of the <a
	 * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
	 * Coefficient</a>, "{@code n choose k}", the number of
	 * {@code k}-element subsets that can be selected from an
	 * {@code n}-element set.
	 * <p>
	 * <Strong>Preconditions</strong>:
	 * <ul>
	 * <li> {@code 0 <= k <= n} (otherwise
	 * {@code IllegalArgumentException} is thrown)</li>
	 * </ul></p>
	 *
	 * @param n
	 * 		the size of the set
	 * @param k
	 * 		the size of the subsets to be counted
	 * @return {@code n choose k}
	 * @throws NotPositiveException
	 * 		if {@code n < 0}.
	 * @throws NumberIsTooLargeException
	 * 		if {@code k > n}.
	 * @throws MathArithmeticException
	 * 		if the result is too large to be
	 * 		represented by a long integer.
	 */
	public static double binomialCoefficientLog(final int n, final int k) throws org.apache.commons.math3.exception.NotPositiveException, org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.MathArithmeticException {
		org.apache.commons.math3.util.ArithmeticUtils.checkBinomial(n, k);
		if ((n == k) || (k == 0)) {
			return 0;
		}
		if ((k == 1) || (k == (n - 1))) {
			return org.apache.commons.math3.util.FastMath.log(n);
		}
		/* For values small enough to do exact integer computation,
		return the log of the exact value
		 */
		if (n < 67) {
			return org.apache.commons.math3.util.FastMath.log(org.apache.commons.math3.util.ArithmeticUtils.binomialCoefficient(n, k));
		}
		/* Return the log of binomialCoefficientDouble for values that will not
		overflow binomialCoefficientDouble
		 */
		if (n < 1030) {
			return org.apache.commons.math3.util.FastMath.log(org.apache.commons.math3.util.ArithmeticUtils.binomialCoefficientDouble(n, k));
		}
		if (k > (n / 2)) {
			return org.apache.commons.math3.util.ArithmeticUtils.binomialCoefficientLog(n, n - k);
		}
		/* Sum logs for values that could overflow */
		double logSum = 0;
		// n!/(n-k)!
		for (int i = (n - k) + 1; i <= n; i++) {
			logSum += org.apache.commons.math3.util.FastMath.log(i);
		}
		// divide by k!
		for (int i = 2; i <= k; i++) {
			logSum -= org.apache.commons.math3.util.FastMath.log(i);
		}
		return logSum;
	}

	/**
	 * Returns n!. Shorthand for {@code n} <a
	 * href="http://mathworld.wolfram.com/Factorial.html"> Factorial</a>, the
	 * product of the numbers {@code 1,...,n}.
	 * <p>
	 * <Strong>Preconditions</strong>:
	 * <ul>
	 * <li> {@code n >= 0} (otherwise
	 * {@code IllegalArgumentException} is thrown)</li>
	 * <li> The result is small enough to fit into a {@code long}. The
	 * largest value of {@code n} for which {@code n!} <
	 * Long.MAX_VALUE} is 20. If the computed value exceeds {@code Long.MAX_VALUE}
	 * an {@code ArithMeticException} is thrown.</li>
	 * </ul>
	 * </p>
	 *
	 * @param n
	 * 		argument
	 * @return {@code n!}
	 * @throws MathArithmeticException
	 * 		if the result is too large to be represented
	 * 		by a {@code long}.
	 * @throws NotPositiveException
	 * 		if {@code n < 0}.
	 * @throws MathArithmeticException
	 * 		if {@code n > 20}: The factorial value is too
	 * 		large to fit in a {@code long}.
	 */
	public static long factorial(final int n) throws org.apache.commons.math3.exception.NotPositiveException, org.apache.commons.math3.exception.MathArithmeticException {
		if (n < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, n);
		}
		if (n > 20) {
			throw new org.apache.commons.math3.exception.MathArithmeticException();
		}
		return org.apache.commons.math3.util.ArithmeticUtils.FACTORIALS[n];
	}

	/**
	 * Compute n!, the<a href="http://mathworld.wolfram.com/Factorial.html">
	 * factorial</a> of {@code n} (the product of the numbers 1 to n), as a
	 * {@code double}.
	 * The result should be small enough to fit into a {@code double}: The
	 * largest {@code n} for which {@code n! < Double.MAX_VALUE} is 170.
	 * If the computed value exceeds {@code Double.MAX_VALUE},
	 * {@code Double.POSITIVE_INFINITY} is returned.
	 *
	 * @param n
	 * 		Argument.
	 * @return {@code n!}
	 * @throws NotPositiveException
	 * 		if {@code n < 0}.
	 */
	public static double factorialDouble(final int n) throws org.apache.commons.math3.exception.NotPositiveException {
		if (n < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, n);
		}
		if (n < 21) {
			return org.apache.commons.math3.util.ArithmeticUtils.FACTORIALS[n];
		}
		return org.apache.commons.math3.util.FastMath.floor(org.apache.commons.math3.util.FastMath.exp(org.apache.commons.math3.util.ArithmeticUtils.factorialLog(n)) + 0.5);
	}

	/**
	 * Compute the natural logarithm of the factorial of {@code n}.
	 *
	 * @param n
	 * 		Argument.
	 * @return {@code n!}
	 * @throws NotPositiveException
	 * 		if {@code n < 0}.
	 */
	public static double factorialLog(final int n) throws org.apache.commons.math3.exception.NotPositiveException {
		if (n < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, n);
		}
		if (n < 21) {
			return org.apache.commons.math3.util.FastMath.log(org.apache.commons.math3.util.ArithmeticUtils.FACTORIALS[n]);
		}
		double logSum = 0;
		for (int i = 2; i <= n; i++) {
			logSum += org.apache.commons.math3.util.FastMath.log(i);
		}
		return logSum;
	}

	/**
	 * Computes the greatest common divisor of the absolute value of two
	 * numbers, using a modified version of the "binary gcd" method.
	 * See Knuth 4.5.2 algorithm B.
	 * The algorithm is due to Josef Stein (1961).
	 * <br/>
	 * Special cases:
	 * <ul>
	 *  <li>The invocations
	 *   {@code gcd(Integer.MIN_VALUE, Integer.MIN_VALUE)},
	 *   {@code gcd(Integer.MIN_VALUE, 0)} and
	 *   {@code gcd(0, Integer.MIN_VALUE)} throw an
	 *   {@code ArithmeticException}, because the result would be 2^31, which
	 *   is too large for an int value.</li>
	 *  <li>The result of {@code gcd(x, x)}, {@code gcd(0, x)} and
	 *   {@code gcd(x, 0)} is the absolute value of {@code x}, except
	 *   for the special cases above.</li>
	 *  <li>The invocation {@code gcd(0, 0)} is the only one which returns
	 *   {@code 0}.</li>
	 * </ul>
	 *
	 * @param p
	 * 		Number.
	 * @param q
	 * 		Number.
	 * @return the greatest common divisor (never negative).
	 * @throws MathArithmeticException
	 * 		if the result cannot be represented as
	 * 		a non-negative {@code int} value.
	 * @since 1.1
	 */
	public static int gcd(int p, int q) throws org.apache.commons.math3.exception.MathArithmeticException {
		int a = p;
		int b = q;
		if ((a == 0) || (b == 0)) {
			if ((a == java.lang.Integer.MIN_VALUE) || (b == java.lang.Integer.MIN_VALUE)) {
				throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.GCD_OVERFLOW_32_BITS, p, q);
			}
			return org.apache.commons.math3.util.FastMath.abs(a + b);
		}
		long al = a;
		long bl = b;
		boolean useLong = false;
		if (a < 0) {
			if (java.lang.Integer.MIN_VALUE == a) {
				useLong = true;
			} else {
				a = -a;
			}
			al = -al;
		}
		if (b < 0) {
			if (java.lang.Integer.MIN_VALUE == b) {
				useLong = true;
			} else {
				b = -b;
			}
			bl = -bl;
		}
		if (useLong) {
			if (al == bl) {
				throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.GCD_OVERFLOW_32_BITS, p, q);
			}
			long blbu = bl;
			bl = al;
			al = blbu % al;
			if (al == 0) {
				if (bl > java.lang.Integer.MAX_VALUE) {
					throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.GCD_OVERFLOW_32_BITS, p, q);
				}
				return ((int) (bl));
			}
			blbu = bl;
			// Now "al" and "bl" fit in an "int".
			b = ((int) (al));
			a = ((int) (blbu % al));
		}
		return org.apache.commons.math3.util.ArithmeticUtils.gcdPositive(a, b);
	}

	/**
	 * Computes the greatest common divisor of two <em>positive</em> numbers
	 * (this precondition is <em>not</em> checked and the result is undefined
	 * if not fulfilled) using the "binary gcd" method which avoids division
	 * and modulo operations.
	 * See Knuth 4.5.2 algorithm B.
	 * The algorithm is due to Josef Stein (1961).
	 * <br/>
	 * Special cases:
	 * <ul>
	 *  <li>The result of {@code gcd(x, x)}, {@code gcd(0, x)} and
	 *   {@code gcd(x, 0)} is the value of {@code x}.</li>
	 *  <li>The invocation {@code gcd(0, 0)} is the only one which returns
	 *   {@code 0}.</li>
	 * </ul>
	 *
	 * @param a
	 * 		Positive number.
	 * @param b
	 * 		Positive number.
	 * @return the greatest common divisor.
	 */
	private static int gcdPositive(int a, int b) {
		if (a == 0) {
			return b;
		} else if (b == 0) {
			return a;
		}
		// Make "a" and "b" odd, keeping track of common power of 2.
		final int aTwos = java.lang.Integer.numberOfTrailingZeros(a);
		a >>= aTwos;
		final int bTwos = java.lang.Integer.numberOfTrailingZeros(b);
		b >>= bTwos;
		final int shift = java.lang.Math.min(aTwos, bTwos);
		// "a" and "b" are positive.
		// If a > b then "gdc(a, b)" is equal to "gcd(a - b, b)".
		// If a < b then "gcd(a, b)" is equal to "gcd(b - a, a)".
		// Hence, in the successive iterations:
		// "a" becomes the absolute difference of the current values,
		// "b" becomes the minimum of the current values.
		while (a != b) {
			final int delta = a - b;
			b = java.lang.Math.min(a, b);
			a = java.lang.Math.abs(delta);
			// Remove any power of 2 in "a" ("b" is guaranteed to be odd).
			a >>= java.lang.Integer.numberOfTrailingZeros(a);
		} 
		// Recover the common power of 2.
		return a << shift;
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
	 * {@code gcd(Long.MIN_VALUE, Long.MIN_VALUE)},
	 * {@code gcd(Long.MIN_VALUE, 0L)} and
	 * {@code gcd(0L, Long.MIN_VALUE)} throw an
	 * {@code ArithmeticException}, because the result would be 2^63, which
	 * is too large for a long value.</li>
	 * <li>The result of {@code gcd(x, x)}, {@code gcd(0L, x)} and
	 * {@code gcd(x, 0L)} is the absolute value of {@code x}, except
	 * for the special cases above.
	 * <li>The invocation {@code gcd(0L, 0L)} is the only one which returns
	 * {@code 0L}.</li>
	 * </ul>
	 *
	 * @param p
	 * 		Number.
	 * @param q
	 * 		Number.
	 * @return the greatest common divisor, never negative.
	 * @throws MathArithmeticException
	 * 		if the result cannot be represented as
	 * 		a non-negative {@code long} value.
	 * @since 2.1
	 */
	public static long gcd(final long p, final long q) throws org.apache.commons.math3.exception.MathArithmeticException {
		long u = p;
		long v = q;
		if ((u == 0) || (v == 0)) {
			if ((u == java.lang.Long.MIN_VALUE) || (v == java.lang.Long.MIN_VALUE)) {
				throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.GCD_OVERFLOW_64_BITS, p, q);
			}
			return org.apache.commons.math3.util.FastMath.abs(u) + org.apache.commons.math3.util.FastMath.abs(v);
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
			throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.GCD_OVERFLOW_64_BITS, p, q);
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
	 * <p>
	 * Returns the least common multiple of the absolute value of two numbers,
	 * using the formula {@code lcm(a,b) = (a / gcd(a,b)) * b}.
	 * </p>
	 * Special cases:
	 * <ul>
	 * <li>The invocations {@code lcm(Integer.MIN_VALUE, n)} and
	 * {@code lcm(n, Integer.MIN_VALUE)}, where {@code abs(n)} is a
	 * power of 2, throw an {@code ArithmeticException}, because the result
	 * would be 2^31, which is too large for an int value.</li>
	 * <li>The result of {@code lcm(0, x)} and {@code lcm(x, 0)} is
	 * {@code 0} for any {@code x}.
	 * </ul>
	 *
	 * @param a
	 * 		Number.
	 * @param b
	 * 		Number.
	 * @return the least common multiple, never negative.
	 * @throws MathArithmeticException
	 * 		if the result cannot be represented as
	 * 		a non-negative {@code int} value.
	 * @since 1.1
	 */
	public static int lcm(int a, int b) throws org.apache.commons.math3.exception.MathArithmeticException {
		if ((a == 0) || (b == 0)) {
			return 0;
		}
		int lcm = org.apache.commons.math3.util.FastMath.abs(org.apache.commons.math3.util.ArithmeticUtils.mulAndCheck(a / org.apache.commons.math3.util.ArithmeticUtils.gcd(a, b), b));
		if (lcm == java.lang.Integer.MIN_VALUE) {
			throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.LCM_OVERFLOW_32_BITS, a, b);
		}
		return lcm;
	}

	/**
	 * <p>
	 * Returns the least common multiple of the absolute value of two numbers,
	 * using the formula {@code lcm(a,b) = (a / gcd(a,b)) * b}.
	 * </p>
	 * Special cases:
	 * <ul>
	 * <li>The invocations {@code lcm(Long.MIN_VALUE, n)} and
	 * {@code lcm(n, Long.MIN_VALUE)}, where {@code abs(n)} is a
	 * power of 2, throw an {@code ArithmeticException}, because the result
	 * would be 2^63, which is too large for an int value.</li>
	 * <li>The result of {@code lcm(0L, x)} and {@code lcm(x, 0L)} is
	 * {@code 0L} for any {@code x}.
	 * </ul>
	 *
	 * @param a
	 * 		Number.
	 * @param b
	 * 		Number.
	 * @return the least common multiple, never negative.
	 * @throws MathArithmeticException
	 * 		if the result cannot be represented
	 * 		as a non-negative {@code long} value.
	 * @since 2.1
	 */
	public static long lcm(long a, long b) throws org.apache.commons.math3.exception.MathArithmeticException {
		if ((a == 0) || (b == 0)) {
			return 0;
		}
		long lcm = org.apache.commons.math3.util.FastMath.abs(org.apache.commons.math3.util.ArithmeticUtils.mulAndCheck(a / org.apache.commons.math3.util.ArithmeticUtils.gcd(a, b), b));
		if (lcm == java.lang.Long.MIN_VALUE) {
			throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.LCM_OVERFLOW_64_BITS, a, b);
		}
		return lcm;
	}

	/**
	 * Multiply two integers, checking for overflow.
	 *
	 * @param x
	 * 		Factor.
	 * @param y
	 * 		Factor.
	 * @return the product {@code x * y}.
	 * @throws MathArithmeticException
	 * 		if the result can not be
	 * 		represented as an {@code int}.
	 * @since 1.1
	 */
	public static int mulAndCheck(int x, int y) throws org.apache.commons.math3.exception.MathArithmeticException {
		long m = ((long) (x)) * ((long) (y));
		if ((m < java.lang.Integer.MIN_VALUE) || (m > java.lang.Integer.MAX_VALUE)) {
			throw new org.apache.commons.math3.exception.MathArithmeticException();
		}
		return ((int) (m));
	}

	/**
	 * Multiply two long integers, checking for overflow.
	 *
	 * @param a
	 * 		Factor.
	 * @param b
	 * 		Factor.
	 * @return the product {@code a * b}.
	 * @throws MathArithmeticException
	 * 		if the result can not be represented
	 * 		as a {@code long}.
	 * @since 1.2
	 */
	public static long mulAndCheck(long a, long b) throws org.apache.commons.math3.exception.MathArithmeticException {
		long ret;
		if (a > b) {
			// use symmetry to reduce boundary cases
			ret = org.apache.commons.math3.util.ArithmeticUtils.mulAndCheck(b, a);
		} else if (a < 0) {
			if (b < 0) {
				// check for positive overflow with negative a, negative b
				if (a >= (java.lang.Long.MAX_VALUE / b)) {
					ret = a * b;
				} else {
					throw new org.apache.commons.math3.exception.MathArithmeticException();
				}
			} else if (b > 0) {
				// check for negative overflow with negative a, positive b
				if ((java.lang.Long.MIN_VALUE / b) <= a) {
					ret = a * b;
				} else {
					throw new org.apache.commons.math3.exception.MathArithmeticException();
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
				throw new org.apache.commons.math3.exception.MathArithmeticException();
			}
		} else {
			// assert a == 0
			ret = 0;
		}
		return ret;
	}

	/**
	 * Subtract two integers, checking for overflow.
	 *
	 * @param x
	 * 		Minuend.
	 * @param y
	 * 		Subtrahend.
	 * @return the difference {@code x - y}.
	 * @throws MathArithmeticException
	 * 		if the result can not be represented
	 * 		as an {@code int}.
	 * @since 1.1
	 */
	public static int subAndCheck(int x, int y) throws org.apache.commons.math3.exception.MathArithmeticException {
		long s = ((long) (x)) - ((long) (y));
		if ((s < java.lang.Integer.MIN_VALUE) || (s > java.lang.Integer.MAX_VALUE)) {
			throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.OVERFLOW_IN_SUBTRACTION, x, y);
		}
		return ((int) (s));
	}

	/**
	 * Subtract two long integers, checking for overflow.
	 *
	 * @param a
	 * 		Value.
	 * @param b
	 * 		Value.
	 * @return the difference {@code a - b}.
	 * @throws MathArithmeticException
	 * 		if the result can not be represented as a
	 * 		{@code long}.
	 * @since 1.2
	 */
	public static long subAndCheck(long a, long b) throws org.apache.commons.math3.exception.MathArithmeticException {
		long ret;
		if (b == java.lang.Long.MIN_VALUE) {
			if (a < 0) {
				ret = a - b;
			} else {
				throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.OVERFLOW_IN_ADDITION, a, -b);
			}
		} else {
			// use additive inverse
			ret = org.apache.commons.math3.util.ArithmeticUtils.addAndCheck(a, -b, org.apache.commons.math3.exception.util.LocalizedFormats.OVERFLOW_IN_ADDITION);
		}
		return ret;
	}

	/**
	 * Raise an int to an int power.
	 *
	 * @param k
	 * 		Number to raise.
	 * @param e
	 * 		Exponent (must be positive or zero).
	 * @return k<sup>e</sup>
	 * @throws NotPositiveException
	 * 		if {@code e < 0}.
	 */
	public static int pow(final int k, int e) throws org.apache.commons.math3.exception.NotPositiveException {
		if (e < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.EXPONENT, e);
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
	 * 		Number to raise.
	 * @param e
	 * 		Exponent (must be positive or zero).
	 * @return k<sup>e</sup>
	 * @throws NotPositiveException
	 * 		if {@code e < 0}.
	 */
	public static int pow(final int k, long e) throws org.apache.commons.math3.exception.NotPositiveException {
		if (e < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.EXPONENT, e);
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
	 * 		Number to raise.
	 * @param e
	 * 		Exponent (must be positive or zero).
	 * @return k<sup>e</sup>
	 * @throws NotPositiveException
	 * 		if {@code e < 0}.
	 */
	public static long pow(final long k, int e) throws org.apache.commons.math3.exception.NotPositiveException {
		if (e < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.EXPONENT, e);
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
	 * 		Number to raise.
	 * @param e
	 * 		Exponent (must be positive or zero).
	 * @return k<sup>e</sup>
	 * @throws NotPositiveException
	 * 		if {@code e < 0}.
	 */
	public static long pow(final long k, long e) throws org.apache.commons.math3.exception.NotPositiveException {
		if (e < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.EXPONENT, e);
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
	 * 		Number to raise.
	 * @param e
	 * 		Exponent (must be positive or zero).
	 * @return k<sup>e</sup>
	 * @throws NotPositiveException
	 * 		if {@code e < 0}.
	 */
	public static java.math.BigInteger pow(final java.math.BigInteger k, int e) throws org.apache.commons.math3.exception.NotPositiveException {
		if (e < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.EXPONENT, e);
		}
		return k.pow(e);
	}

	/**
	 * Raise a BigInteger to a long power.
	 *
	 * @param k
	 * 		Number to raise.
	 * @param e
	 * 		Exponent (must be positive or zero).
	 * @return k<sup>e</sup>
	 * @throws NotPositiveException
	 * 		if {@code e < 0}.
	 */
	public static java.math.BigInteger pow(final java.math.BigInteger k, long e) throws org.apache.commons.math3.exception.NotPositiveException {
		if (e < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.EXPONENT, e);
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
	 * 		Number to raise.
	 * @param e
	 * 		Exponent (must be positive or zero).
	 * @return k<sup>e</sup>
	 * @throws NotPositiveException
	 * 		if {@code e < 0}.
	 */
	public static java.math.BigInteger pow(final java.math.BigInteger k, java.math.BigInteger e) throws org.apache.commons.math3.exception.NotPositiveException {
		if (e.compareTo(java.math.BigInteger.ZERO) < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.EXPONENT, e);
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
	 * Returns the <a
	 * href="http://mathworld.wolfram.com/StirlingNumberoftheSecondKind.html">
	 * Stirling number of the second kind</a>, "{@code S(n,k)}", the number of
	 * ways of partitioning an {@code n}-element set into {@code k} non-empty
	 * subsets.
	 * <p>
	 * The preconditions are {@code 0 <= k <= n} (otherwise
	 * {@code NotPositiveException} is thrown)
	 * </p>
	 *
	 * @param n
	 * 		the size of the set
	 * @param k
	 * 		the number of non-empty subsets
	 * @return {@code S(n,k)}
	 * @throws NotPositiveException
	 * 		if {@code k < 0}.
	 * @throws NumberIsTooLargeException
	 * 		if {@code k > n}.
	 * @throws MathArithmeticException
	 * 		if some overflow happens, typically for n exceeding 25 and
	 * 		k between 20 and n-2 (S(n,n-1) is handled specifically and does not overflow)
	 * @since 3.1
	 */
	public static long stirlingS2(final int n, final int k) throws org.apache.commons.math3.exception.NotPositiveException, org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.MathArithmeticException {
		if (k < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(k);
		}
		if (k > n) {
			throw new org.apache.commons.math3.exception.NumberIsTooLargeException(k, n, true);
		}
		long[][] stirlingS2 = org.apache.commons.math3.util.ArithmeticUtils.STIRLING_S2.get();
		if (stirlingS2 == null) {
			// the cache has never been initialized, compute the first numbers
			// by direct recurrence relation
			// as S(26,9) = 11201516780955125625 is larger than Long.MAX_VALUE
			// we must stop computation at row 26
			final int maxIndex = 26;
			stirlingS2 = new long[maxIndex][];
			stirlingS2[0] = new long[]{ 1L };
			for (int i = 1; i < stirlingS2.length; ++i) {
				stirlingS2[i] = new long[i + 1];
				stirlingS2[i][0] = 0;
				stirlingS2[i][1] = 1;
				stirlingS2[i][i] = 1;
				for (int j = 2; j < i; ++j) {
					stirlingS2[i][j] = (j * stirlingS2[i - 1][j]) + stirlingS2[i - 1][j - 1];
				}
			}
			// atomically save the cache
			org.apache.commons.math3.util.ArithmeticUtils.STIRLING_S2.compareAndSet(null, stirlingS2);
		}
		if (n < stirlingS2.length) {
			// the number is in the small cache
			return stirlingS2[n][k];
		} else // use explicit formula to compute the number without caching it
		if (k == 0) {
			return 0;
		} else if ((k == 1) || (k == n)) {
			return 1;
		} else if (k == 2) {
			return (1L << (n - 1)) - 1L;
		} else if (k == (n - 1)) {
			return org.apache.commons.math3.util.ArithmeticUtils.binomialCoefficient(n, 2);
		} else {
			// definition formula: note that this may trigger some overflow
			long sum = 0;
			long sign = ((k & 0x1) == 0) ? 1 : -1;
			for (int j = 1; j <= k; ++j) {
				sign = -sign;
				sum += (sign * org.apache.commons.math3.util.ArithmeticUtils.binomialCoefficient(k, j)) * org.apache.commons.math3.util.ArithmeticUtils.pow(j, n);
				if (sum < 0) {
					// there was an overflow somewhere
					throw new org.apache.commons.math3.exception.MathArithmeticException(org.apache.commons.math3.exception.util.LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN, n, 0, stirlingS2.length - 1);
				}
			}
			return sum / org.apache.commons.math3.util.ArithmeticUtils.factorial(k);
		}
	}

	/**
	 * Add two long integers, checking for overflow.
	 *
	 * @param a
	 * 		Addend.
	 * @param b
	 * 		Addend.
	 * @param pattern
	 * 		Pattern to use for any thrown exception.
	 * @return the sum {@code a + b}.
	 * @throws MathArithmeticException
	 * 		if the result cannot be represented
	 * 		as a {@code long}.
	 * @since 1.2
	 */
	private static long addAndCheck(long a, long b, org.apache.commons.math3.exception.util.Localizable pattern) throws org.apache.commons.math3.exception.MathArithmeticException {
		long ret;
		if (a > b) {
			// use symmetry to reduce boundary cases
			ret = org.apache.commons.math3.util.ArithmeticUtils.addAndCheck(b, a, pattern);
		} else // assert a <= b
		if (a < 0) {
			if (b < 0) {
				// check for negative overflow
				if ((java.lang.Long.MIN_VALUE - b) <= a) {
					ret = a + b;
				} else {
					throw new org.apache.commons.math3.exception.MathArithmeticException(pattern, a, b);
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
			throw new org.apache.commons.math3.exception.MathArithmeticException(pattern, a, b);
		}
		return ret;
	}

	/**
	 * Check binomial preconditions.
	 *
	 * @param n
	 * 		Size of the set.
	 * @param k
	 * 		Size of the subsets to be counted.
	 * @throws NotPositiveException
	 * 		if {@code n < 0}.
	 * @throws NumberIsTooLargeException
	 * 		if {@code k > n}.
	 */
	private static void checkBinomial(final int n, final int k) throws org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.NotPositiveException {
		if (n < k) {
			throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.BINOMIAL_INVALID_PARAMETERS_ORDER, k, n, true);
		}
		if (n < 0) {
			throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.BINOMIAL_NEGATIVE_PARAMETER, n);
		}
	}

	/**
	 * Returns true if the argument is a power of two.
	 *
	 * @param n
	 * 		the number to test
	 * @return true if the argument is a power of two
	 */
	public static boolean isPowerOfTwo(long n) {
		return (n > 0) && ((n & (n - 1)) == 0);
	}
}