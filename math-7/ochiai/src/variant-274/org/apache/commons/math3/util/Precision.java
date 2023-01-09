package org.apache.commons.math3.util;
























/**
 * Utilities for comparing numbers.
 *
 * @since 3.0
 * @version $Id$
 */
public class Precision {
	/**
	 * <p>
	 * Largest double-precision floating-point number such that
	 * {@code 1 + EPSILON} is numerically equal to 1. This value is an upper
	 * bound on the relative error due to rounding real numbers to double
	 * precision floating-point numbers.
	 * </p>
	 * <p>
	 * In IEEE 754 arithmetic, this is 2<sup>-53</sup>.
	 * </p>
	 *
	 * @see <a href="http://en.wikipedia.org/wiki/Machine_epsilon">Machine epsilon</a>
	 */
	public static final double EPSILON;

	/**
	 * Safe minimum, such that {@code 1 / SAFE_MIN} does not overflow.
	 * <br/>
	 * In IEEE 754 arithmetic, this is also the smallest normalized
	 * number 2<sup>-1022</sup>.
	 */
	public static final double SAFE_MIN;

	/**
	 * Exponent offset in IEEE754 representation.
	 */ 	private static final long EXPONENT_OFFSET = 1023L;
	/**
	 * Offset to order signed double numbers lexicographically.
	 */ 	private static final long SGN_MASK = 0x8000000000000000L; 	/**
	 * Offset to order signed double numbers lexicographically.
	 */ 	private static final int SGN_MASK_FLOAT = 0x80000000;
	static {
		/* This was previously expressed as = 0x1.0p-53;
		 However, OpenJDK (Sparc Solaris) cannot handle such small
		 constants: MATH-721
		 */

		org.apache.commons.math3.util.Precision.EPSILON = java.lang.Double.longBitsToDouble((org.apache.commons.math3.util.Precision.EXPONENT_OFFSET - 53L) << 52);

		/* This was previously expressed as = 0x1.0p-1022;
		However, OpenJDK (Sparc Solaris) cannot handle such small
		constants: MATH-721
		 */

		org.apache.commons.math3.util.Precision.SAFE_MIN = java.lang.Double.longBitsToDouble((org.apache.commons.math3.util.Precision.EXPONENT_OFFSET - 1022L) << 52);
	}

	/**
	 * Private constructor.
	 */
	private Precision() {}

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
	 */ 	public static int compareTo(double x, double y, double eps) { 		if (org.apache.commons.math3.util.Precision.equals(x, y, eps)) { 			return 0;
		} else if (x < y) {
			return -1;
		}
		return 1;
	}

	/**
	 * Compares two numbers given some amount of allowed error.
	 * Two float numbers are considered equal if there are {@code (maxUlps - 1)}
	 * (or fewer) floating point numbers between them, i.e. two adjacent floating
	 * point numbers are considered equal.
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
	 * @return <ul><li>0 if  {@link #equals(double, double, int) equals(x, y, maxUlps)}</li>
	<li>&lt; 0 if !{@link #equals(double, double, int) equals(x, y, maxUlps)} &amp;&amp; x &lt; y</li>
	<li>> 0 if !{@link #equals(double, double, int) equals(x, y, maxUlps)} &amp;&amp; x > y</li></ul>
	 */ 	public static int compareTo(final double x, final double y, final int maxUlps) { 		if (org.apache.commons.math3.util.Precision.equals(x, y, maxUlps)) { 			return 0;
		} else if (x < y) {
			return -1;
		}
		return 1;
	}

	/**
	 * Returns true iff they are equal as defined by
	 * {@link #equals(float,float,int) equals(x, y, 1)}.
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @return {@code true} if the values are equal.
	 */ 	public static boolean equals(float x, float y) { 		return org.apache.commons.math3.util.Precision.equals(x, y, 1);
	}

	/**
	 * Returns true if both arguments are NaN or neither is NaN and they are
	 * equal as defined by {@link #equals(float,float) equals(x, y, 1)}.
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @return {@code true} if the values are equal or both are NaN.
	 * @since 2.2
	 */ 	public static boolean equalsIncludingNaN(float x, float y) { 		return (java.lang.Float.isNaN(x) && java.lang.Float.isNaN(y)) || org.apache.commons.math3.util.Precision.equals(x, y, 1);
	}

	/**
	 * Returns true if both arguments are equal or within the range of allowed
	 * error (inclusive).
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @param eps
	 * 		the amount of absolute error to allow.
	 * @return {@code true} if the values are equal or within range of each other.
	 * @since 2.2
	 */ 	public static boolean equals(float x, float y, float eps) { 		return org.apache.commons.math3.util.Precision.equals(x, y, 1) || (org.apache.commons.math3.util.FastMath.abs(y - x) <= eps);}

	/**
	 * Returns true if both arguments are NaN or are equal or within the range
	 * of allowed error (inclusive).
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @param eps
	 * 		the amount of absolute error to allow.
	 * @return {@code true} if the values are equal or within range of each other,
	or both are NaN.
	 * @since 2.2
	 */ 	public static boolean equalsIncludingNaN(float x, float y, float eps) { 		return org.apache.commons.math3.util.Precision.equalsIncludingNaN(x, y) || (org.apache.commons.math3.util.FastMath.abs(y - x) <= eps);}

	/**
	 * Returns true if both arguments are equal or within the range of allowed
	 * error (inclusive).
	 * Two float numbers are considered equal if there are {@code (maxUlps - 1)}
	 * (or fewer) floating point numbers between them, i.e. two adjacent floating
	 * point numbers are considered equal.
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
	 * @return {@code true} if there are fewer than {@code maxUlps} floating
	point values between {@code x} and {@code y}.
	 * @since 2.2
	 */ 	public static boolean equals(float x, float y, int maxUlps) { 		int xInt = java.lang.Float.floatToIntBits(x); 		int yInt = java.lang.Float.floatToIntBits(y);

		// Make lexicographically ordered as a two's-complement integer.
		if (xInt < 0) {
			xInt = org.apache.commons.math3.util.Precision.SGN_MASK_FLOAT - xInt;
		}
		if (yInt < 0) {
			yInt = org.apache.commons.math3.util.Precision.SGN_MASK_FLOAT - yInt;
		}

		final boolean isEqual = org.apache.commons.math3.util.FastMath.abs(xInt - yInt) <= maxUlps;

		return (isEqual && (!java.lang.Float.isNaN(x))) && (!java.lang.Float.isNaN(y));
	}

	/**
	 * Returns true if both arguments are NaN or if they are equal as defined
	 * by {@link #equals(float,float,int) equals(x, y, maxUlps)}.
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @param maxUlps
	 * 		{@code (maxUlps - 1)} is the number of floating point
	 * 		values between {@code x} and {@code y}.
	 * @return {@code true} if both arguments are NaN or if there are less than
	{@code maxUlps} floating point values between {@code x} and {@code y}.
	 * @since 2.2
	 */ 	public static boolean equalsIncludingNaN(float x, float y, int maxUlps) { 		return (java.lang.Float.isNaN(x) && java.lang.Float.isNaN(y)) || org.apache.commons.math3.util.Precision.equals(x, y, maxUlps);}

	/**
	 * Returns true iff they are equal as defined by
	 * {@link #equals(double,double,int) equals(x, y, 1)}.
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @return {@code true} if the values are equal.
	 */ 	public static boolean equals(double x, double y) { 		return org.apache.commons.math3.util.Precision.equals(x, y, 1);
	}

	/**
	 * Returns true if both arguments are NaN or neither is NaN and they are
	 * equal as defined by {@link #equals(double,double) equals(x, y, 1)}.
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @return {@code true} if the values are equal or both are NaN.
	 * @since 2.2
	 */ 	public static boolean equalsIncludingNaN(double x, double y) { 		return (java.lang.Double.isNaN(x) && java.lang.Double.isNaN(y)) || org.apache.commons.math3.util.Precision.equals(x, y, 1);
	}

	/**
	 * Returns {@code true} if there is no double value strictly between the
	 * arguments or the difference between them is within the range of allowed
	 * error (inclusive).
	 *
	 * @param x
	 * 		First value.
	 * @param y
	 * 		Second value.
	 * @param eps
	 * 		Amount of allowed absolute error.
	 * @return {@code true} if the values are two adjacent floating point
	numbers or they are within range of each other.
	 */ 	public static boolean equals(double x, double y, double eps) { 		return org.apache.commons.math3.util.Precision.equals(x, y, 1) || (org.apache.commons.math3.util.FastMath.abs(y - x) <= eps);}

	/**
	 * Returns {@code true} if there is no double value strictly between the
	 * arguments or the reltaive difference between them is smaller or equal
	 * to the given tolerance.
	 *
	 * @param x
	 * 		First value.
	 * @param y
	 * 		Second value.
	 * @param eps
	 * 		Amount of allowed relative error.
	 * @return {@code true} if the values are two adjacent floating point
	numbers or they are within range of each other.
	 * @since 3.1
	 */ 	public static boolean equalsWithRelativeTolerance(double x, double y, double eps) { 		if (org.apache.commons.math3.util.Precision.equals(x, y, 1)) { 			return true;
		}

		final double absoluteMax = org.apache.commons.math3.util.FastMath.max(org.apache.commons.math3.util.FastMath.abs(x), org.apache.commons.math3.util.FastMath.abs(y));
		final double relativeDifference = org.apache.commons.math3.util.FastMath.abs((x - y) / absoluteMax);

		return relativeDifference <= eps;
	}

	/**
	 * Returns true if both arguments are NaN or are equal or within the range
	 * of allowed error (inclusive).
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @param eps
	 * 		the amount of absolute error to allow.
	 * @return {@code true} if the values are equal or within range of each other,
	or both are NaN.
	 * @since 2.2
	 */ 	public static boolean equalsIncludingNaN(double x, double y, double eps) { 		return org.apache.commons.math3.util.Precision.equalsIncludingNaN(x, y) || (org.apache.commons.math3.util.FastMath.abs(y - x) <= eps);}

	/**
	 * Returns true if both arguments are equal or within the range of allowed
	 * error (inclusive).
	 * Two float numbers are considered equal if there are {@code (maxUlps - 1)}
	 * (or fewer) floating point numbers between them, i.e. two adjacent floating
	 * point numbers are considered equal.
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
	 * @return {@code true} if there are fewer than {@code maxUlps} floating
	point values between {@code x} and {@code y}.
	 */ 	public static boolean equals(double x, double y, int maxUlps) { 		long xInt = java.lang.Double.doubleToLongBits(x); 		long yInt = java.lang.Double.doubleToLongBits(y);

		// Make lexicographically ordered as a two's-complement integer.
		if (xInt < 0) {
			xInt = org.apache.commons.math3.util.Precision.SGN_MASK - xInt;
		}
		if (yInt < 0) {
			yInt = org.apache.commons.math3.util.Precision.SGN_MASK - yInt;
		}

		final boolean isEqual = org.apache.commons.math3.util.FastMath.abs(xInt - yInt) <= maxUlps;

		return (isEqual && (!java.lang.Double.isNaN(x))) && (!java.lang.Double.isNaN(y));
	}

	/**
	 * Returns true if both arguments are NaN or if they are equal as defined
	 * by {@link #equals(double,double,int) equals(x, y, maxUlps)}.
	 *
	 * @param x
	 * 		first value
	 * @param y
	 * 		second value
	 * @param maxUlps
	 * 		{@code (maxUlps - 1)} is the number of floating point
	 * 		values between {@code x} and {@code y}.
	 * @return {@code true} if both arguments are NaN or if there are less than
	{@code maxUlps} floating point values between {@code x} and {@code y}.
	 * @since 2.2
	 */ 	public static boolean equalsIncludingNaN(double x, double y, int maxUlps) { 		return (java.lang.Double.isNaN(x) && java.lang.Double.isNaN(y)) || org.apache.commons.math3.util.Precision.equals(x, y, maxUlps);}

	/**
	 * Rounds the given value to the specified number of decimal places.
	 * The value is rounded using the {@link BigDecimal#ROUND_HALF_UP} method.
	 *
	 * @param x
	 * 		Value to round.
	 * @param scale
	 * 		Number of digits to the right of the decimal point.
	 * @return the rounded value.
	 * @since 1.1 (previously in {@code MathUtils}, moved as of version 3.0)
	 */ 	public static double round(double x, int scale) { 		return org.apache.commons.math3.util.Precision.round(x, scale, java.math.BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Rounds the given value to the specified number of decimal places.
	 * The value is rounded using the given method which is any method defined
	 * in {@link BigDecimal}.
	 * If {@code x} is infinite or {@code NaN}, then the value of {@code x} is
	 * returned unchanged, regardless of the other parameters.
	 *
	 * @param x
	 * 		Value to round.
	 * @param scale
	 * 		Number of digits to the right of the decimal point.
	 * @param roundingMethod
	 * 		Rounding method as defined in {@link BigDecimal}.
	 * @return the rounded value.
	 * @throws ArithmeticException
	 * 		if {@code roundingMethod == ROUND_UNNECESSARY}
	 * 		and the specified scaling operation would require rounding.
	 * @throws IllegalArgumentException
	 * 		if {@code roundingMethod} does not
	 * 		represent a valid rounding mode.
	 * @since 1.1 (previously in {@code MathUtils}, moved as of version 3.0)
	 */ 	public static double round(double x, int scale, int roundingMethod) { 		try { 			return new java.math.BigDecimal(java.lang.Double.toString(x)).setScale(scale, roundingMethod).doubleValue();

		} catch (java.lang.NumberFormatException ex) {
			if (java.lang.Double.isInfinite(x)) {
				return x;
			} else {
				return java.lang.Double.NaN;
			}
		}
	}

	/**
	 * Rounds the given value to the specified number of decimal places.
	 * The value is rounded using the {@link BigDecimal#ROUND_HALF_UP} method.
	 *
	 * @param x
	 * 		Value to round.
	 * @param scale
	 * 		Number of digits to the right of the decimal point.
	 * @return the rounded value.
	 * @since 1.1 (previously in {@code MathUtils}, moved as of version 3.0)
	 */ 	public static float round(float x, int scale) { 		return org.apache.commons.math3.util.Precision.round(x, scale, java.math.BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Rounds the given value to the specified number of decimal places.
	 * The value is rounded using the given method which is any method defined
	 * in {@link BigDecimal}.
	 *
	 * @param x
	 * 		Value to round.
	 * @param scale
	 * 		Number of digits to the right of the decimal point.
	 * @param roundingMethod
	 * 		Rounding method as defined in {@link BigDecimal}.
	 * @return the rounded value.
	 * @since 1.1 (previously in {@code MathUtils}, moved as of version 3.0)
	 * @throws MathArithmeticException
	 * 		if an exact operation is required but result is not exact
	 * @throws MathIllegalArgumentException
	 * 		if {@code roundingMethod} is not a valid rounding method.
	 */ 	public static float round(float x, int scale, int roundingMethod) throws org.apache.commons.math3.exception.MathArithmeticException, org.apache.commons.math3.exception.MathIllegalArgumentException { 		final float sign = org.apache.commons.math3.util.FastMath.copySign(1.0F, x); 		final float factor = ((float) (org.apache.commons.math3.util.FastMath.pow(10.0F, scale))) * sign; 		return ((float) (org.apache.commons.math3.util.Precision.roundUnscaled(x * factor, sign, roundingMethod))) / factor;
	}

	/**
	 * Rounds the given non-negative value to the "nearest" integer. Nearest is
	 * determined by the rounding method specified. Rounding methods are defined
	 * in {@link BigDecimal}.
	 *
	 * @param unscaled
	 * 		Value to round.
	 * @param sign
	 * 		Sign of the original, scaled value.
	 * @param roundingMethod
	 * 		Rounding method, as defined in {@link BigDecimal}.
	 * @return the rounded value.
	 * @throws MathArithmeticException
	 * 		if an exact operation is required but result is not exact
	 * @throws MathIllegalArgumentException
	 * 		if {@code roundingMethod} is not a valid rounding method.
	 * @since 1.1 (previously in {@code MathUtils}, moved as of version 3.0)
	 */ 	private static double roundUnscaled(double unscaled, double sign, int roundingMethod) throws org.apache.commons.math3.exception.MathArithmeticException, org.apache.commons.math3.exception.MathIllegalArgumentException { 		switch (roundingMethod) {
			case java.math.BigDecimal.ROUND_CEILING :
				if (sign == (-1)) {
					unscaled = org.apache.commons.math3.util.FastMath.floor(org.apache.commons.math3.util.FastMath.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY));
				} else {
					unscaled = org.apache.commons.math3.util.FastMath.ceil(org.apache.commons.math3.util.FastMath.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY));
				}
				break;
			case java.math.BigDecimal.ROUND_DOWN :
				unscaled = org.apache.commons.math3.util.FastMath.floor(org.apache.commons.math3.util.FastMath.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY));
				break;
			case java.math.BigDecimal.ROUND_FLOOR :
				if (sign == (-1)) {
					unscaled = org.apache.commons.math3.util.FastMath.ceil(org.apache.commons.math3.util.FastMath.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY));
				} else {
					unscaled = org.apache.commons.math3.util.FastMath.floor(org.apache.commons.math3.util.FastMath.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY));
				}
				break;
			case java.math.BigDecimal.ROUND_HALF_DOWN : 				{
					unscaled = org.apache.commons.math3.util.FastMath.nextAfter(unscaled, java.lang.Double.NEGATIVE_INFINITY);
					double fraction = unscaled - org.apache.commons.math3.util.FastMath.floor(unscaled);
					if (fraction > 0.5) {
						unscaled = org.apache.commons.math3.util.FastMath.ceil(unscaled);
					} else {
						unscaled = org.apache.commons.math3.util.FastMath.floor(unscaled);
					}
					break;
				}
			case java.math.BigDecimal.ROUND_HALF_EVEN : 				{
					double fraction = unscaled - org.apache.commons.math3.util.FastMath.floor(unscaled);
					if (fraction > 0.5) {
						unscaled = org.apache.commons.math3.util.FastMath.ceil(unscaled);
					} else if (fraction < 0.5) {
						unscaled = org.apache.commons.math3.util.FastMath.floor(unscaled);
					} else 
					// The following equality test is intentional and needed for rounding purposes
					if ((org.apache.commons.math3.util.FastMath.floor(unscaled) / 2.0) == org.apache.commons.math3.util.FastMath.floor(java.lang.Math.floor(
					unscaled) / 2.0)) { 						// even
						unscaled = org.apache.commons.math3.util.FastMath.floor(unscaled);
					} else { 						// odd
						unscaled = org.apache.commons.math3.util.FastMath.ceil(unscaled);
					}

					break;
				}
			case java.math.BigDecimal.ROUND_HALF_UP : 				{
					unscaled = org.apache.commons.math3.util.FastMath.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY);
					double fraction = unscaled - org.apache.commons.math3.util.FastMath.floor(unscaled);
					if (fraction >= 0.5) {
						unscaled = org.apache.commons.math3.util.FastMath.ceil(unscaled);
					} else {
						unscaled = org.apache.commons.math3.util.FastMath.floor(unscaled);
					}
					break;
				}
			case java.math.BigDecimal.ROUND_UNNECESSARY :
				if (unscaled != org.apache.commons.math3.util.FastMath.floor(unscaled)) {
					throw new org.apache.commons.math3.exception.MathArithmeticException();
				}
				break;
			case java.math.BigDecimal.ROUND_UP :
				unscaled = org.apache.commons.math3.util.FastMath.ceil(org.apache.commons.math3.util.FastMath.nextAfter(unscaled, java.lang.Double.POSITIVE_INFINITY));
				break;
			default :
				throw new org.apache.commons.math3.exception.MathIllegalArgumentException(org.apache.commons.math3.exception.util.LocalizedFormats.INVALID_ROUNDING_METHOD, 
				roundingMethod, 
				"ROUND_CEILING", java.math.BigDecimal.ROUND_CEILING, 
				"ROUND_DOWN", java.math.BigDecimal.ROUND_DOWN, 
				"ROUND_FLOOR", java.math.BigDecimal.ROUND_FLOOR, 
				"ROUND_HALF_DOWN", java.math.BigDecimal.ROUND_HALF_DOWN, 
				"ROUND_HALF_EVEN", java.math.BigDecimal.ROUND_HALF_EVEN, 
				"ROUND_HALF_UP", java.math.BigDecimal.ROUND_HALF_UP, 
				"ROUND_UNNECESSARY", java.math.BigDecimal.ROUND_UNNECESSARY, 
				"ROUND_UP", java.math.BigDecimal.ROUND_UP);}

		return unscaled;
	}


	/**
	 * Computes a number {@code delta} close to {@code originalDelta} with
	 * the property that <pre><code>
	 *   x + delta - x
	 * </code></pre>
	 * is exactly machine-representable.
	 * This is useful when computing numerical derivatives, in order to reduce
	 * roundoff errors.
	 *
	 * @param x
	 * 		Value.
	 * @param originalDelta
	 * 		Offset value.
	 * @return a number {@code delta} so that {@code x + delta} and {@code x}
	differ by a representable floating number.
	 */ 	public static double representableDelta(double x, double originalDelta) {
		return (x + originalDelta) - x;
	}}