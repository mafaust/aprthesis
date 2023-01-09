package org.apache.commons.math.fraction;
/**
 * Representation of a rational number without any overflow. This class is
 * immutable.
 *
 * @version $Id$
 * @since 2.0
 */
public class BigFraction extends java.lang.Number implements org.apache.commons.math.FieldElement<org.apache.commons.math.fraction.BigFraction> , java.lang.Comparable<org.apache.commons.math.fraction.BigFraction> , java.io.Serializable {
	/**
	 * A fraction representing "2 / 1".
	 */
	public static final org.apache.commons.math.fraction.BigFraction TWO = new org.apache.commons.math.fraction.BigFraction(2);

	/**
	 * A fraction representing "1".
	 */
	public static final org.apache.commons.math.fraction.BigFraction ONE = new org.apache.commons.math.fraction.BigFraction(1);

	/**
	 * A fraction representing "0".
	 */
	public static final org.apache.commons.math.fraction.BigFraction ZERO = new org.apache.commons.math.fraction.BigFraction(0);

	/**
	 * A fraction representing "-1 / 1".
	 */
	public static final org.apache.commons.math.fraction.BigFraction MINUS_ONE = new org.apache.commons.math.fraction.BigFraction(-1);

	/**
	 * A fraction representing "4/5".
	 */
	public static final org.apache.commons.math.fraction.BigFraction FOUR_FIFTHS = new org.apache.commons.math.fraction.BigFraction(4, 5);

	/**
	 * A fraction representing "1/5".
	 */
	public static final org.apache.commons.math.fraction.BigFraction ONE_FIFTH = new org.apache.commons.math.fraction.BigFraction(1, 5);

	/**
	 * A fraction representing "1/2".
	 */
	public static final org.apache.commons.math.fraction.BigFraction ONE_HALF = new org.apache.commons.math.fraction.BigFraction(1, 2);

	/**
	 * A fraction representing "1/4".
	 */
	public static final org.apache.commons.math.fraction.BigFraction ONE_QUARTER = new org.apache.commons.math.fraction.BigFraction(1, 4);

	/**
	 * A fraction representing "1/3".
	 */
	public static final org.apache.commons.math.fraction.BigFraction ONE_THIRD = new org.apache.commons.math.fraction.BigFraction(1, 3);

	/**
	 * A fraction representing "3/5".
	 */
	public static final org.apache.commons.math.fraction.BigFraction THREE_FIFTHS = new org.apache.commons.math.fraction.BigFraction(3, 5);

	/**
	 * A fraction representing "3/4".
	 */
	public static final org.apache.commons.math.fraction.BigFraction THREE_QUARTERS = new org.apache.commons.math.fraction.BigFraction(3, 4);

	/**
	 * A fraction representing "2/5".
	 */
	public static final org.apache.commons.math.fraction.BigFraction TWO_FIFTHS = new org.apache.commons.math.fraction.BigFraction(2, 5);

	/**
	 * A fraction representing "2/4".
	 */
	public static final org.apache.commons.math.fraction.BigFraction TWO_QUARTERS = new org.apache.commons.math.fraction.BigFraction(2, 4);

	/**
	 * A fraction representing "2/3".
	 */
	public static final org.apache.commons.math.fraction.BigFraction TWO_THIRDS = new org.apache.commons.math.fraction.BigFraction(2, 3);

	/**
	 * Serializable version identifier.
	 */
	private static final long serialVersionUID = -5630213147331578515L;

	/**
	 * <code>BigInteger</code> representation of 100.
	 */
	private static final java.math.BigInteger ONE_HUNDRED = java.math.BigInteger.valueOf(100);

	/**
	 * The numerator.
	 */
	private final java.math.BigInteger numerator;

	/**
	 * The denominator.
	 */
	private final java.math.BigInteger denominator;

	/**
	 * <p>
	 * Create a {@link BigFraction} equivalent to the passed <tt>BigInteger</tt>, ie
	 * "num / 1".
	 * </p>
	 *
	 * @param num
	 * 		the numerator.
	 */
	public BigFraction(final java.math.BigInteger num) {
		this(num, java.math.BigInteger.ONE);
	}

	/**
	 * Create a {@link BigFraction} given the numerator and denominator as
	 * {@code BigInteger}. The {@link BigFraction} is reduced to lowest terms.
	 *
	 * @param num
	 * 		the numerator, must not be {@code null}.
	 * @param den
	 * 		the denominator, must not be {@code null}.
	 * @throws ZeroException
	 * 		if the denominator is zero.
	 * @throws NullArgumentException
	 * 		if either of the arguments is null
	 */
	public BigFraction(java.math.BigInteger num, java.math.BigInteger den) {
		org.apache.commons.math.util.MathUtils.checkNotNull(num, org.apache.commons.math.exception.util.LocalizedFormats.NUMERATOR);
		org.apache.commons.math.util.MathUtils.checkNotNull(den, org.apache.commons.math.exception.util.LocalizedFormats.DENOMINATOR);
		if (java.math.BigInteger.ZERO.equals(den)) {
			throw new org.apache.commons.math.exception.ZeroException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_DENOMINATOR);
		}
		if (java.math.BigInteger.ZERO.equals(num)) {
			numerator = java.math.BigInteger.ZERO;
			denominator = java.math.BigInteger.ONE;
		} else {
			// reduce numerator and denominator by greatest common denominator
			final java.math.BigInteger gcd = num.gcd(den);
			if (java.math.BigInteger.ONE.compareTo(gcd) < 0) {
				num = num.divide(gcd);
				den = den.divide(gcd);
			}
			// move sign to numerator
			if (java.math.BigInteger.ZERO.compareTo(den) > 0) {
				num = num.negate();
				den = den.negate();
			}
			// store the values in the final fields
			numerator = num;
			denominator = den;
		}
	}

	/**
	 * Create a fraction given the double value.
	 * <p>
	 * This constructor behaves <em>differently</em> from
	 * {@link #BigFraction(double, double, int)}. It converts the double value
	 * exactly, considering its internal bits representation. This works for all
	 * values except NaN and infinities and does not requires any loop or
	 * convergence threshold.
	 * </p>
	 * <p>
	 * Since this conversion is exact and since double numbers are sometimes
	 * approximated, the fraction created may seem strange in some cases. For example,
	 * calling <code>new BigFraction(1.0 / 3.0)</code> does <em>not</em> create
	 * the fraction 1/3, but the fraction 6004799503160661 / 18014398509481984
	 * because the double number passed to the constructor is not exactly 1/3
	 * (this number cannot be stored exactly in IEEE754).
	 * </p>
	 *
	 * @see #BigFraction(double, double, int)
	 * @param value
	 * 		the double value to convert to a fraction.
	 * @exception MathIllegalArgumentException
	 * 		if value is NaN or infinite
	 */
	public BigFraction(final double value) throws org.apache.commons.math.exception.MathIllegalArgumentException {
		if (java.lang.Double.isNaN(value)) {
			throw new org.apache.commons.math.exception.MathIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NAN_VALUE_CONVERSION);
		}
		if (java.lang.Double.isInfinite(value)) {
			throw new org.apache.commons.math.exception.MathIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INFINITE_VALUE_CONVERSION);
		}
		// compute m and k such that value = m * 2^k
		final long bits = java.lang.Double.doubleToLongBits(value);
		final long sign = bits & 0x8000000000000000L;
		final long exponent = bits & 0x7ff0000000000000L;
		long m = bits & 0xfffffffffffffL;
		if (exponent != 0) {
			// this was a normalized number, add the implicit most significant bit
			m |= 0x10000000000000L;
		}
		if (sign != 0) {
			m = -m;
		}
		int k = ((int) (exponent >> 52)) - 1075;
		while (((m & 0x1ffffffffffffeL) != 0) && ((m & 0x1) == 0)) {
			m = m >> 1;
			++k;
		} 
		if (k < 0) {
			numerator = java.math.BigInteger.valueOf(m);
			denominator = java.math.BigInteger.ZERO.flipBit(-k);
		} else {
			numerator = java.math.BigInteger.valueOf(m).multiply(java.math.BigInteger.ZERO.flipBit(k));
			denominator = java.math.BigInteger.ONE;
		}
	}

	/**
	 * Create a fraction given the double value and maximum error allowed.
	 * <p>
	 * References:
	 * <ul>
	 * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
	 * Continued Fraction</a> equations (11) and (22)-(26)</li>
	 * </ul>
	 * </p>
	 *
	 * @param value
	 * 		the double value to convert to a fraction.
	 * @param epsilon
	 * 		maximum error allowed. The resulting fraction is within
	 * 		<code>epsilon</code> of <code>value</code>, in absolute terms.
	 * @param maxIterations
	 * 		maximum number of convergents.
	 * @throws FractionConversionException
	 * 		if the continued fraction failed to converge.
	 * @see #BigFraction(double)
	 */
	public BigFraction(final double value, final double epsilon, final int maxIterations) throws org.apache.commons.math.fraction.FractionConversionException {
		this(value, epsilon, java.lang.Integer.MAX_VALUE, maxIterations);
	}

	/**
	 * Create a fraction given the double value and either the maximum error
	 * allowed or the maximum number of denominator digits.
	 * <p>
	 *
	 * NOTE: This constructor is called with EITHER - a valid epsilon value and
	 * the maxDenominator set to Integer.MAX_VALUE (that way the maxDenominator
	 * has no effect). OR - a valid maxDenominator value and the epsilon value
	 * set to zero (that way epsilon only has effect if there is an exact match
	 * before the maxDenominator value is reached).
	 * </p>
	 * <p>
	 *
	 * It has been done this way so that the same code can be (re)used for both
	 * scenarios. However this could be confusing to users if it were part of
	 * the public API and this constructor should therefore remain PRIVATE.
	 * </p>
	 *
	 * See JIRA issue ticket MATH-181 for more details:
	 *
	 * https://issues.apache.org/jira/browse/MATH-181
	 *
	 * @param value
	 * 		the double value to convert to a fraction.
	 * @param epsilon
	 * 		maximum error allowed. The resulting fraction is within
	 * 		<code>epsilon</code> of <code>value</code>, in absolute terms.
	 * @param maxDenominator
	 * 		maximum denominator value allowed.
	 * @param maxIterations
	 * 		maximum number of convergents.
	 * @throws FractionConversionException
	 * 		if the continued fraction failed to converge.
	 */
	private BigFraction(final double value, final double epsilon, final int maxDenominator, int maxIterations) throws org.apache.commons.math.fraction.FractionConversionException {
		long overflow = java.lang.Integer.MAX_VALUE;
		double r0 = value;
		long a0 = ((long) (org.apache.commons.math.util.FastMath.floor(r0)));
		if (a0 > overflow) {
			throw new org.apache.commons.math.fraction.FractionConversionException(value, a0, 1L);
		}
		// check for (almost) integer arguments, which should not go
		// to iterations.
		if (org.apache.commons.math.util.FastMath.abs(a0 - value) < epsilon) {
			numerator = java.math.BigInteger.valueOf(a0);
			denominator = java.math.BigInteger.ONE;
			return;
		}
		long p0 = 1;
		long q0 = 0;
		long p1 = a0;
		long q1 = 1;
		long p2 = 0;
		long q2 = 1;
		int n = 0;
		boolean stop = false;
		do {
			++n;
			final double r1 = 1.0 / (r0 - a0);
			final long a1 = ((long) (org.apache.commons.math.util.FastMath.floor(r1)));
			p2 = (a1 * p1) + p0;
			q2 = (a1 * q1) + q0;
			if ((p2 > overflow) || (q2 > overflow)) {
				throw new org.apache.commons.math.fraction.FractionConversionException(value, p2, q2);
			}
			final double convergent = ((double) (p2)) / ((double) (q2));
			if (((n < maxIterations) && (org.apache.commons.math.util.FastMath.abs(convergent - value) > epsilon)) && (q2 < maxDenominator)) {
				p0 = p1;
				p1 = p2;
				q0 = q1;
				q1 = q2;
				a0 = a1;
				r0 = r1;
			} else {
				stop = true;
			}
		} while (!stop );
		if (n >= maxIterations) {
			throw new org.apache.commons.math.fraction.FractionConversionException(value, maxIterations);
		}
		if (q2 < maxDenominator) {
			numerator = java.math.BigInteger.valueOf(p2);
			denominator = java.math.BigInteger.valueOf(q2);
		} else {
			numerator = java.math.BigInteger.valueOf(p1);
			denominator = java.math.BigInteger.valueOf(q1);
		}
	}

	/**
	 * Create a fraction given the double value and maximum denominator.
	 * <p>
	 * References:
	 * <ul>
	 * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
	 * Continued Fraction</a> equations (11) and (22)-(26)</li>
	 * </ul>
	 * </p>
	 *
	 * @param value
	 * 		the double value to convert to a fraction.
	 * @param maxDenominator
	 * 		The maximum allowed value for denominator.
	 * @throws FractionConversionException
	 * 		if the continued fraction failed to converge.
	 */
	public BigFraction(final double value, final int maxDenominator) throws org.apache.commons.math.fraction.FractionConversionException {
		this(value, 0, maxDenominator, 100);
	}

	/**
	 * <p>
	 * Create a {@link BigFraction} equivalent to the passed <tt>int</tt>, ie
	 * "num / 1".
	 * </p>
	 *
	 * @param num
	 * 		the numerator.
	 */
	public BigFraction(final int num) {
		this(java.math.BigInteger.valueOf(num), java.math.BigInteger.ONE);
	}

	/**
	 * <p>
	 * Create a {@link BigFraction} given the numerator and denominator as simple
	 * <tt>int</tt>. The {@link BigFraction} is reduced to lowest terms.
	 * </p>
	 *
	 * @param num
	 * 		the numerator.
	 * @param den
	 * 		the denominator.
	 */
	public BigFraction(final int num, final int den) {
		this(java.math.BigInteger.valueOf(num), java.math.BigInteger.valueOf(den));
	}

	/**
	 * <p>
	 * Create a {@link BigFraction} equivalent to the passed long, ie "num / 1".
	 * </p>
	 *
	 * @param num
	 * 		the numerator.
	 */
	public BigFraction(final long num) {
		this(java.math.BigInteger.valueOf(num), java.math.BigInteger.ONE);
	}

	/**
	 * <p>
	 * Create a {@link BigFraction} given the numerator and denominator as simple
	 * <tt>long</tt>. The {@link BigFraction} is reduced to lowest terms.
	 * </p>
	 *
	 * @param num
	 * 		the numerator.
	 * @param den
	 * 		the denominator.
	 */
	public BigFraction(final long num, final long den) {
		this(java.math.BigInteger.valueOf(num), java.math.BigInteger.valueOf(den));
	}

	/**
	 * <p>
	 * Creates a <code>BigFraction</code> instance with the 2 parts of a fraction
	 * Y/Z.
	 * </p>
	 *
	 * <p>
	 * Any negative signs are resolved to be on the numerator.
	 * </p>
	 *
	 * @param numerator
	 * 		the numerator, for example the three in 'three sevenths'.
	 * @param denominator
	 * 		the denominator, for example the seven in 'three sevenths'.
	 * @return a new fraction instance, with the numerator and denominator
	reduced.
	 * @throws ArithmeticException
	 * 		if the denominator is <code>zero</code>.
	 */
	public static org.apache.commons.math.fraction.BigFraction getReducedFraction(final int numerator, final int denominator) {
		if (numerator == 0) {
			return org.apache.commons.math.fraction.BigFraction.ZERO;// normalize zero.

		}
		return new org.apache.commons.math.fraction.BigFraction(numerator, denominator);
	}

	/**
	 * <p>
	 * Returns the absolute value of this {@link BigFraction}.
	 * </p>
	 *
	 * @return the absolute value as a {@link BigFraction}.
	 */
	public org.apache.commons.math.fraction.BigFraction abs() {
		return java.math.BigInteger.ZERO.compareTo(numerator) <= 0 ? this : negate();
	}

	/**
	 * <p>
	 * Adds the value of this fraction to the passed {@link BigInteger},
	 * returning the result in reduced form.
	 * </p>
	 *
	 * @param bg
	 * 		the {@link BigInteger} to add, must'nt be <code>null</code>.
	 * @return a <code>BigFraction</code> instance with the resulting values.
	 * @throws NullArgumentException
	 * 		if the {@link BigInteger} is <code>null</code>.
	 */
	public org.apache.commons.math.fraction.BigFraction add(final java.math.BigInteger bg) throws org.apache.commons.math.exception.NullArgumentException {
		org.apache.commons.math.util.MathUtils.checkNotNull(bg);
		return new org.apache.commons.math.fraction.BigFraction(numerator.add(denominator.multiply(bg)), denominator);
	}

	/**
	 * <p>
	 * Adds the value of this fraction to the passed <tt>integer</tt>, returning
	 * the result in reduced form.
	 * </p>
	 *
	 * @param i
	 * 		the <tt>integer</tt> to add.
	 * @return a <code>BigFraction</code> instance with the resulting values.
	 */
	public org.apache.commons.math.fraction.BigFraction add(final int i) {
		return add(java.math.BigInteger.valueOf(i));
	}

	/**
	 * <p>
	 * Adds the value of this fraction to the passed <tt>long</tt>, returning
	 * the result in reduced form.
	 * </p>
	 *
	 * @param l
	 * 		the <tt>long</tt> to add.
	 * @return a <code>BigFraction</code> instance with the resulting values.
	 */
	public org.apache.commons.math.fraction.BigFraction add(final long l) {
		return add(java.math.BigInteger.valueOf(l));
	}

	/**
	 * <p>
	 * Adds the value of this fraction to another, returning the result in
	 * reduced form.
	 * </p>
	 *
	 * @param fraction
	 * 		the {@link BigFraction} to add, must not be <code>null</code>.
	 * @return a {@link BigFraction} instance with the resulting values.
	 * @throws NullArgumentException
	 * 		if the {@link BigFraction} is {@code null}.
	 */
	public org.apache.commons.math.fraction.BigFraction add(final org.apache.commons.math.fraction.BigFraction fraction) {
		if (fraction == null) {
			throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION);
		}
		if (org.apache.commons.math.fraction.BigFraction.ZERO.equals(fraction)) {
			return this;
		}
		java.math.BigInteger num = null;
		java.math.BigInteger den = null;
		if (denominator.equals(fraction.denominator)) {
			num = numerator.add(fraction.numerator);
			den = denominator;
		} else {
			num = numerator.multiply(fraction.denominator).add(fraction.numerator.multiply(denominator));
			den = denominator.multiply(fraction.denominator);
		}
		return new org.apache.commons.math.fraction.BigFraction(num, den);
	}

	/**
	 * <p>
	 * Gets the fraction as a <code>BigDecimal</code>. This calculates the
	 * fraction as the numerator divided by denominator.
	 * </p>
	 *
	 * @return the fraction as a <code>BigDecimal</code>.
	 * @throws ArithmeticException
	 * 		if the exact quotient does not have a terminating decimal
	 * 		expansion.
	 * @see BigDecimal
	 */
	public java.math.BigDecimal bigDecimalValue() {
		return new java.math.BigDecimal(numerator).divide(new java.math.BigDecimal(denominator));
	}

	/**
	 * <p>
	 * Gets the fraction as a <code>BigDecimal</code> following the passed
	 * rounding mode. This calculates the fraction as the numerator divided by
	 * denominator.
	 * </p>
	 *
	 * @param roundingMode
	 * 		rounding mode to apply. see {@link BigDecimal} constants.
	 * @return the fraction as a <code>BigDecimal</code>.
	 * @throws IllegalArgumentException
	 * 		if <tt>roundingMode</tt> does not represent a valid rounding
	 * 		mode.
	 * @see BigDecimal
	 */
	public java.math.BigDecimal bigDecimalValue(final int roundingMode) {
		return new java.math.BigDecimal(numerator).divide(new java.math.BigDecimal(denominator), roundingMode);
	}

	/**
	 * <p>
	 * Gets the fraction as a <code>BigDecimal</code> following the passed scale
	 * and rounding mode. This calculates the fraction as the numerator divided
	 * by denominator.
	 * </p>
	 *
	 * @param scale
	 * 		scale of the <code>BigDecimal</code> quotient to be returned.
	 * 		see {@link BigDecimal} for more information.
	 * @param roundingMode
	 * 		rounding mode to apply. see {@link BigDecimal} constants.
	 * @return the fraction as a <code>BigDecimal</code>.
	 * @see BigDecimal
	 */
	public java.math.BigDecimal bigDecimalValue(final int scale, final int roundingMode) {
		return new java.math.BigDecimal(numerator).divide(new java.math.BigDecimal(denominator), scale, roundingMode);
	}

	/**
	 * <p>
	 * Compares this object to another based on size.
	 * </p>
	 *
	 * @param object
	 * 		the object to compare to, must not be <code>null</code>.
	 * @return -1 if this is less than <tt>object</tt>, +1 if this is greater
	than <tt>object</tt>, 0 if they are equal.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final org.apache.commons.math.fraction.BigFraction object) {
		java.math.BigInteger nOd = numerator.multiply(object.denominator);
		java.math.BigInteger dOn = denominator.multiply(object.numerator);
		return nOd.compareTo(dOn);
	}

	/**
	 * <p>
	 * Divide the value of this fraction by the passed <code>BigInteger</code>,
	 * ie "this * 1 / bg", returning the result in reduced form.
	 * </p>
	 *
	 * @param bg
	 * 		the <code>BigInteger</code> to divide by, must not be
	 * 		<code>null</code>.
	 * @return a {@link BigFraction} instance with the resulting values.
	 * @throws NullArgumentException
	 * 		if the {@code BigInteger} is {@code null}.
	 * @throws ZeroException
	 * 		if the fraction to divide by is zero.
	 */
	public org.apache.commons.math.fraction.BigFraction divide(final java.math.BigInteger bg) {
		if (java.math.BigInteger.ZERO.equals(bg)) {
			throw new org.apache.commons.math.exception.ZeroException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_DENOMINATOR);
		}
		return new org.apache.commons.math.fraction.BigFraction(numerator, denominator.multiply(bg));
	}

	/**
	 * <p>
	 * Divide the value of this fraction by the passed <tt>int</tt>, ie
	 * "this * 1 / i", returning the result in reduced form.
	 * </p>
	 *
	 * @param i
	 * 		the <tt>int</tt> to divide by.
	 * @return a {@link BigFraction} instance with the resulting values.
	 * @throws ArithmeticException
	 * 		if the fraction to divide by is zero.
	 */
	public org.apache.commons.math.fraction.BigFraction divide(final int i) {
		return divide(java.math.BigInteger.valueOf(i));
	}

	/**
	 * <p>
	 * Divide the value of this fraction by the passed <tt>long</tt>, ie
	 * "this * 1 / l", returning the result in reduced form.
	 * </p>
	 *
	 * @param l
	 * 		the <tt>long</tt> to divide by.
	 * @return a {@link BigFraction} instance with the resulting values.
	 * @throws ArithmeticException
	 * 		if the fraction to divide by is zero.
	 */
	public org.apache.commons.math.fraction.BigFraction divide(final long l) {
		return divide(java.math.BigInteger.valueOf(l));
	}

	/**
	 * <p>
	 * Divide the value of this fraction by another, returning the result in
	 * reduced form.
	 * </p>
	 *
	 * @param fraction
	 * 		Fraction to divide by, must not be {@code null}.
	 * @return a {@link BigFraction} instance with the resulting values.
	 * @throws NullArgumentException
	 * 		if the {@code fraction} is {@code null}.
	 * @throws ZeroException
	 * 		if the fraction to divide by is zero.
	 */
	public org.apache.commons.math.fraction.BigFraction divide(final org.apache.commons.math.fraction.BigFraction fraction) {
		if (fraction == null) {
			throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION);
		}
		if (java.math.BigInteger.ZERO.equals(fraction.numerator)) {
			throw new org.apache.commons.math.exception.ZeroException(org.apache.commons.math.exception.util.LocalizedFormats.ZERO_DENOMINATOR);
		}
		return multiply(fraction.reciprocal());
	}

	/**
	 * <p>
	 * Gets the fraction as a <tt>double</tt>. This calculates the fraction as
	 * the numerator divided by denominator.
	 * </p>
	 *
	 * @return the fraction as a <tt>double</tt>
	 * @see java.lang.Number#doubleValue()
	 */
	@java.lang.Override
	public double doubleValue() {
		return numerator.doubleValue() / denominator.doubleValue();
	}

	/**
	 * <p>
	 * Test for the equality of two fractions. If the lowest term numerator and
	 * denominators are the same for both fractions, the two fractions are
	 * considered to be equal.
	 * </p>
	 *
	 * @param other
	 * 		fraction to test for equality to this fraction, can be
	 * 		<code>null</code>.
	 * @return true if two fractions are equal, false if object is
	<code>null</code>, not an instance of {@link BigFraction}, or not
	equal to this fraction instance.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@java.lang.Override
	public boolean equals(final java.lang.Object other) {
		boolean ret = false;
		if (this == other) {
			ret = true;
		} else if (other instanceof org.apache.commons.math.fraction.BigFraction) {
			org.apache.commons.math.fraction.BigFraction rhs = ((org.apache.commons.math.fraction.BigFraction) (other)).reduce();
			org.apache.commons.math.fraction.BigFraction thisOne = this.reduce();
			ret = thisOne.numerator.equals(rhs.numerator) && thisOne.denominator.equals(rhs.denominator);
		}
		return ret;
	}

	/**
	 * <p>
	 * Gets the fraction as a <tt>float</tt>. This calculates the fraction as
	 * the numerator divided by denominator.
	 * </p>
	 *
	 * @return the fraction as a <tt>float</tt>.
	 * @see java.lang.Number#floatValue()
	 */
	@java.lang.Override
	public float floatValue() {
		return numerator.floatValue() / denominator.floatValue();
	}

	/**
	 * <p>
	 * Access the denominator as a <code>BigInteger</code>.
	 * </p>
	 *
	 * @return the denominator as a <code>BigInteger</code>.
	 */
	public java.math.BigInteger getDenominator() {
		return denominator;
	}

	/**
	 * <p>
	 * Access the denominator as a <tt>int</tt>.
	 * </p>
	 *
	 * @return the denominator as a <tt>int</tt>.
	 */
	public int getDenominatorAsInt() {
		return denominator.intValue();
	}

	/**
	 * <p>
	 * Access the denominator as a <tt>long</tt>.
	 * </p>
	 *
	 * @return the denominator as a <tt>long</tt>.
	 */
	public long getDenominatorAsLong() {
		return denominator.longValue();
	}

	/**
	 * <p>
	 * Access the numerator as a <code>BigInteger</code>.
	 * </p>
	 *
	 * @return the numerator as a <code>BigInteger</code>.
	 */
	public java.math.BigInteger getNumerator() {
		return numerator;
	}

	/**
	 * <p>
	 * Access the numerator as a <tt>int</tt>.
	 * </p>
	 *
	 * @return the numerator as a <tt>int</tt>.
	 */
	public int getNumeratorAsInt() {
		return numerator.intValue();
	}

	/**
	 * <p>
	 * Access the numerator as a <tt>long</tt>.
	 * </p>
	 *
	 * @return the numerator as a <tt>long</tt>.
	 */
	public long getNumeratorAsLong() {
		return numerator.longValue();
	}

	/**
	 * <p>
	 * Gets a hashCode for the fraction.
	 * </p>
	 *
	 * @return a hash code value for this object.
	 * @see java.lang.Object#hashCode()
	 */
	@java.lang.Override
	public int hashCode() {
		return (37 * ((37 * 17) + numerator.hashCode())) + denominator.hashCode();
	}

	/**
	 * <p>
	 * Gets the fraction as an <tt>int</tt>. This returns the whole number part
	 * of the fraction.
	 * </p>
	 *
	 * @return the whole number fraction part.
	 * @see java.lang.Number#intValue()
	 */
	@java.lang.Override
	public int intValue() {
		return numerator.divide(denominator).intValue();
	}

	/**
	 * <p>
	 * Gets the fraction as a <tt>long</tt>. This returns the whole number part
	 * of the fraction.
	 * </p>
	 *
	 * @return the whole number fraction part.
	 * @see java.lang.Number#longValue()
	 */
	@java.lang.Override
	public long longValue() {
		return numerator.divide(denominator).longValue();
	}

	/**
	 * <p>
	 * Multiplies the value of this fraction by the passed
	 * <code>BigInteger</code>, returning the result in reduced form.
	 * </p>
	 *
	 * @param bg
	 * 		the {@code BigInteger} to multiply by.
	 * @return a {@code BigFraction} instance with the resulting values.
	 * @throws NullArgumentException
	 * 		if {@code bg} is {@code null}.
	 */
	public org.apache.commons.math.fraction.BigFraction multiply(final java.math.BigInteger bg) {
		if (bg == null) {
			throw new org.apache.commons.math.exception.NullArgumentException();
		}
		return new org.apache.commons.math.fraction.BigFraction(bg.multiply(numerator), denominator);
	}

	/**
	 * <p>
	 * Multiply the value of this fraction by the passed <tt>int</tt>, returning
	 * the result in reduced form.
	 * </p>
	 *
	 * @param i
	 * 		the <tt>int</tt> to multiply by.
	 * @return a {@link BigFraction} instance with the resulting values.
	 */
	public org.apache.commons.math.fraction.BigFraction multiply(final int i) {
		return multiply(java.math.BigInteger.valueOf(i));
	}

	/**
	 * <p>
	 * Multiply the value of this fraction by the passed <tt>long</tt>,
	 * returning the result in reduced form.
	 * </p>
	 *
	 * @param l
	 * 		the <tt>long</tt> to multiply by.
	 * @return a {@link BigFraction} instance with the resulting values.
	 */
	public org.apache.commons.math.fraction.BigFraction multiply(final long l) {
		return multiply(java.math.BigInteger.valueOf(l));
	}

	/**
	 * <p>
	 * Multiplies the value of this fraction by another, returning the result in
	 * reduced form.
	 * </p>
	 *
	 * @param fraction
	 * 		Fraction to multiply by, must not be {@code null}.
	 * @return a {@link BigFraction} instance with the resulting values.
	 * @throws NullArgumentException
	 * 		if {@code fraction} is {@code null}.
	 */
	public org.apache.commons.math.fraction.BigFraction multiply(final org.apache.commons.math.fraction.BigFraction fraction) {
		if (fraction == null) {
			throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION);
		}
		if (numerator.equals(java.math.BigInteger.ZERO) || fraction.numerator.equals(java.math.BigInteger.ZERO)) {
			return org.apache.commons.math.fraction.BigFraction.ZERO;
		}
		return new org.apache.commons.math.fraction.BigFraction(numerator.multiply(fraction.numerator), denominator.multiply(fraction.denominator));
	}

	/**
	 * <p>
	 * Return the additive inverse of this fraction, returning the result in
	 * reduced form.
	 * </p>
	 *
	 * @return the negation of this fraction.
	 */
	public org.apache.commons.math.fraction.BigFraction negate() {
		return new org.apache.commons.math.fraction.BigFraction(numerator.negate(), denominator);
	}

	/**
	 * <p>
	 * Gets the fraction percentage as a <tt>double</tt>. This calculates the
	 * fraction as the numerator divided by denominator multiplied by 100.
	 * </p>
	 *
	 * @return the fraction percentage as a <tt>double</tt>.
	 */
	public double percentageValue() {
		return multiply(org.apache.commons.math.fraction.BigFraction.ONE_HUNDRED).doubleValue();
	}

	/**
	 * <p>
	 * Returns a {@code BigFraction} whose value is
	 * {@code (this<sup>exponent</sup>)}, returning the result in reduced form.
	 * </p>
	 *
	 * @param exponent
	 * 		exponent to which this {@code BigFraction} is to be
	 * 		raised.
	 * @return <tt>this<sup>exponent</sup></tt>.
	 */
	public org.apache.commons.math.fraction.BigFraction pow(final int exponent) {
		if (exponent < 0) {
			return new org.apache.commons.math.fraction.BigFraction(denominator.pow(-exponent), numerator.pow(-exponent));
		}
		return new org.apache.commons.math.fraction.BigFraction(numerator.pow(exponent), denominator.pow(exponent));
	}

	/**
	 * <p>
	 * Returns a <code>BigFraction</code> whose value is
	 * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
	 * </p>
	 *
	 * @param exponent
	 * 		exponent to which this <code>BigFraction</code> is to be raised.
	 * @return <tt>this<sup>exponent</sup></tt> as a <code>BigFraction</code>.
	 */
	public org.apache.commons.math.fraction.BigFraction pow(final long exponent) {
		if (exponent < 0) {
			return new org.apache.commons.math.fraction.BigFraction(org.apache.commons.math.util.ArithmeticUtils.pow(denominator, -exponent), org.apache.commons.math.util.ArithmeticUtils.pow(numerator, -exponent));
		}
		return new org.apache.commons.math.fraction.BigFraction(org.apache.commons.math.util.ArithmeticUtils.pow(numerator, exponent), org.apache.commons.math.util.ArithmeticUtils.pow(denominator, exponent));
	}

	/**
	 * <p>
	 * Returns a <code>BigFraction</code> whose value is
	 * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
	 * </p>
	 *
	 * @param exponent
	 * 		exponent to which this <code>BigFraction</code> is to be raised.
	 * @return <tt>this<sup>exponent</sup></tt> as a <code>BigFraction</code>.
	 */
	public org.apache.commons.math.fraction.BigFraction pow(final java.math.BigInteger exponent) {
		if (exponent.compareTo(java.math.BigInteger.ZERO) < 0) {
			final java.math.BigInteger eNeg = exponent.negate();
			return new org.apache.commons.math.fraction.BigFraction(org.apache.commons.math.util.ArithmeticUtils.pow(denominator, eNeg), org.apache.commons.math.util.ArithmeticUtils.pow(numerator, eNeg));
		}
		return new org.apache.commons.math.fraction.BigFraction(org.apache.commons.math.util.ArithmeticUtils.pow(numerator, exponent), org.apache.commons.math.util.ArithmeticUtils.pow(denominator, exponent));
	}

	/**
	 * <p>
	 * Returns a <code>double</code> whose value is
	 * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
	 * </p>
	 *
	 * @param exponent
	 * 		exponent to which this <code>BigFraction</code> is to be raised.
	 * @return <tt>this<sup>exponent</sup></tt>.
	 */
	public double pow(final double exponent) {
		return org.apache.commons.math.util.FastMath.pow(numerator.doubleValue(), exponent) / org.apache.commons.math.util.FastMath.pow(denominator.doubleValue(), exponent);
	}

	/**
	 * <p>
	 * Return the multiplicative inverse of this fraction.
	 * </p>
	 *
	 * @return the reciprocal fraction.
	 */
	public org.apache.commons.math.fraction.BigFraction reciprocal() {
		return new org.apache.commons.math.fraction.BigFraction(denominator, numerator);
	}

	/**
	 * <p>
	 * Reduce this <code>BigFraction</code> to its lowest terms.
	 * </p>
	 *
	 * @return the reduced <code>BigFraction</code>. It doesn't change anything if
	the fraction can be reduced.
	 */
	public org.apache.commons.math.fraction.BigFraction reduce() {
		final java.math.BigInteger gcd = numerator.gcd(denominator);
		return new org.apache.commons.math.fraction.BigFraction(numerator.divide(gcd), denominator.divide(gcd));
	}

	/**
	 * <p>
	 * Subtracts the value of an {@link BigInteger} from the value of this
	 * {@code BigFraction}, returning the result in reduced form.
	 * </p>
	 *
	 * @param bg
	 * 		the {@link BigInteger} to subtract, cannot be {@code null}.
	 * @return a {@code BigFraction} instance with the resulting values.
	 * @throws NullArgumentException
	 * 		if the {@link BigInteger} is {@code null}.
	 */
	public org.apache.commons.math.fraction.BigFraction subtract(final java.math.BigInteger bg) {
		if (bg == null) {
			throw new org.apache.commons.math.exception.NullArgumentException();
		}
		return new org.apache.commons.math.fraction.BigFraction(numerator.subtract(denominator.multiply(bg)), denominator);
	}

	/**
	 * <p>
	 * Subtracts the value of an {@code integer} from the value of this
	 * {@code BigFraction}, returning the result in reduced form.
	 * </p>
	 *
	 * @param i
	 * 		the {@code integer} to subtract.
	 * @return a {@code BigFraction} instance with the resulting values.
	 */
	public org.apache.commons.math.fraction.BigFraction subtract(final int i) {
		return subtract(java.math.BigInteger.valueOf(i));
	}

	/**
	 * <p>
	 * Subtracts the value of a {@code long} from the value of this
	 * {@code BigFraction}, returning the result in reduced form.
	 * </p>
	 *
	 * @param l
	 * 		the {@code long} to subtract.
	 * @return a {@code BigFraction} instance with the resulting values.
	 */
	public org.apache.commons.math.fraction.BigFraction subtract(final long l) {
		return subtract(java.math.BigInteger.valueOf(l));
	}

	/**
	 * <p>
	 * Subtracts the value of another fraction from the value of this one,
	 * returning the result in reduced form.
	 * </p>
	 *
	 * @param fraction
	 * 		{@link BigFraction} to subtract, must not be {@code null}.
	 * @return a {@link BigFraction} instance with the resulting values
	 * @throws NullArgumentException
	 * 		if the {@code fraction} is {@code null}.
	 */
	public org.apache.commons.math.fraction.BigFraction subtract(final org.apache.commons.math.fraction.BigFraction fraction) {
		if (fraction == null) {
			throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FRACTION);
		}
		if (org.apache.commons.math.fraction.BigFraction.ZERO.equals(fraction)) {
			return this;
		}
		java.math.BigInteger num = null;
		java.math.BigInteger den = null;
		if (denominator.equals(fraction.denominator)) {
			num = numerator.subtract(fraction.numerator);
			den = denominator;
		} else {
			num = numerator.multiply(fraction.denominator).subtract(fraction.numerator.multiply(denominator));
			den = denominator.multiply(fraction.denominator);
		}
		return new org.apache.commons.math.fraction.BigFraction(num, den);
	}

	/**
	 * <p>
	 * Returns the <code>String</code> representing this fraction, ie
	 * "num / dem" or just "num" if the denominator is one.
	 * </p>
	 *
	 * @return a string representation of the fraction.
	 * @see java.lang.Object#toString()
	 */
	@java.lang.Override
	public java.lang.String toString() {
		java.lang.String str = null;
		if (java.math.BigInteger.ONE.equals(denominator)) {
			str = numerator.toString();
		} else if (java.math.BigInteger.ZERO.equals(numerator)) {
			str = "0";
		} else {
			str = (numerator + " / ") + denominator;
		}
		return str;
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.fraction.BigFractionField getField() {
		return org.apache.commons.math.fraction.BigFractionField.getInstance();
	}
}