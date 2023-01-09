package org.apache.commons.math.fraction;
/**
 * Formats a BigFraction number in proper format.  The number format for each of
 * the whole number, numerator and, denominator can be configured.
 * <p>
 * Minus signs are only allowed in the whole number part - i.e.,
 * "-3 1/2" is legitimate and denotes -7/2, but "-3 -1/2" is invalid and
 * will result in a <code>ParseException</code>.</p>
 *
 * @since 1.1
 * @version $Id$
 */
public class ProperBigFractionFormat extends org.apache.commons.math.fraction.BigFractionFormat {
	/**
	 * Serializable version identifier
	 */
	private static final long serialVersionUID = -6337346779577272307L;

	/**
	 * The format used for the whole number.
	 */
	private java.text.NumberFormat wholeFormat;

	/**
	 * Create a proper formatting instance with the default number format for
	 * the whole, numerator, and denominator.
	 */
	public ProperBigFractionFormat() {
		this(org.apache.commons.math.fraction.AbstractFormat.getDefaultNumberFormat());
	}

	/**
	 * Create a proper formatting instance with a custom number format for the
	 * whole, numerator, and denominator.
	 *
	 * @param format
	 * 		the custom format for the whole, numerator, and
	 * 		denominator.
	 */
	public ProperBigFractionFormat(final java.text.NumberFormat format) {
		this(format, ((java.text.NumberFormat) (format.clone())), ((java.text.NumberFormat) (format.clone())));
	}

	/**
	 * Create a proper formatting instance with a custom number format for each
	 * of the whole, numerator, and denominator.
	 *
	 * @param wholeFormat
	 * 		the custom format for the whole.
	 * @param numeratorFormat
	 * 		the custom format for the numerator.
	 * @param denominatorFormat
	 * 		the custom format for the denominator.
	 */
	public ProperBigFractionFormat(final java.text.NumberFormat wholeFormat, final java.text.NumberFormat numeratorFormat, final java.text.NumberFormat denominatorFormat) {
		super(numeratorFormat, denominatorFormat);
		setWholeFormat(wholeFormat);
	}

	/**
	 * Formats a {@link BigFraction} object to produce a string.  The BigFraction
	 * is output in proper format.
	 *
	 * @param fraction
	 * 		the object to format.
	 * @param toAppendTo
	 * 		where the text is to be appended
	 * @param pos
	 * 		On input: an alignment field, if desired. On output: the
	 * 		offsets of the alignment field
	 * @return the value passed in as toAppendTo.
	 */
	@java.lang.Override
	public java.lang.StringBuffer format(final org.apache.commons.math.fraction.BigFraction fraction, final java.lang.StringBuffer toAppendTo, final java.text.FieldPosition pos) {
		pos.setBeginIndex(0);
		pos.setEndIndex(0);
		java.math.BigInteger num = fraction.getNumerator();
		java.math.BigInteger den = fraction.getDenominator();
		java.math.BigInteger whole = num.divide(den);
		num = num.remainder(den);
		if (!java.math.BigInteger.ZERO.equals(whole)) {
			getWholeFormat().format(whole, toAppendTo, pos);
			toAppendTo.append(' ');
			if (num.compareTo(java.math.BigInteger.ZERO) < 0) {
				num = num.negate();
			}
		}
		getNumeratorFormat().format(num, toAppendTo, pos);
		toAppendTo.append(" / ");
		getDenominatorFormat().format(den, toAppendTo, pos);
		return toAppendTo;
	}

	/**
	 * Access the whole format.
	 *
	 * @return the whole format.
	 */
	public java.text.NumberFormat getWholeFormat() {
		return wholeFormat;
	}

	/**
	 * Parses a string to produce a {@link BigFraction} object.  This method
	 * expects the string to be formatted as a proper BigFraction.
	 * <p>
	 * Minus signs are only allowed in the whole number part - i.e.,
	 * "-3 1/2" is legitimate and denotes -7/2, but "-3 -1/2" is invalid and
	 * will result in a <code>ParseException</code>.</p>
	 *
	 * @param source
	 * 		the string to parse
	 * @param pos
	 * 		input/ouput parsing parameter.
	 * @return the parsed {@link BigFraction} object.
	 */
	@java.lang.Override
	public org.apache.commons.math.fraction.BigFraction parse(final java.lang.String source, final java.text.ParsePosition pos) {
		// try to parse improper BigFraction
		org.apache.commons.math.fraction.BigFraction ret = super.parse(source, pos);
		if (ret != null) {
			return ret;
		}
		final int initialIndex = pos.getIndex();
		// parse whitespace
		org.apache.commons.math.fraction.AbstractFormat.parseAndIgnoreWhitespace(source, pos);
		// parse whole
		java.math.BigInteger whole = parseNextBigInteger(source, pos);
		if (whole == null) {
			// invalid integer number
			// set index back to initial, error index should already be set
			// character examined.
			pos.setIndex(initialIndex);
			return null;
		}
		// parse whitespace
		org.apache.commons.math.fraction.AbstractFormat.parseAndIgnoreWhitespace(source, pos);
		// parse numerator
		java.math.BigInteger num = parseNextBigInteger(source, pos);
		if (num == null) {
			// invalid integer number
			// set index back to initial, error index should already be set
			// character examined.
			pos.setIndex(initialIndex);
			return null;
		}
		if (num.compareTo(java.math.BigInteger.ZERO) < 0) {
			// minus signs should be leading, invalid expression
			pos.setIndex(initialIndex);
			return null;
		}
		// parse '/'
		final int startIndex = pos.getIndex();
		final char c = org.apache.commons.math.fraction.AbstractFormat.parseNextCharacter(source, pos);
		switch (c) {
			case 0 :
				// no '/'
				// return num as a BigFraction
				return new org.apache.commons.math.fraction.BigFraction(num);
			case '/' :
				// found '/', continue parsing denominator
				break;
			default :
				// invalid '/'
				// set index back to initial, error index should be the last
				// character examined.
				pos.setIndex(initialIndex);
				pos.setErrorIndex(startIndex);
				return null;
		}
		// parse whitespace
		org.apache.commons.math.fraction.AbstractFormat.parseAndIgnoreWhitespace(source, pos);
		// parse denominator
		final java.math.BigInteger den = parseNextBigInteger(source, pos);
		if (den == null) {
			// invalid integer number
			// set index back to initial, error index should already be set
			// character examined.
			pos.setIndex(initialIndex);
			return null;
		}
		if (den.compareTo(java.math.BigInteger.ZERO) < 0) {
			// minus signs must be leading, invalid
			pos.setIndex(initialIndex);
			return null;
		}
		boolean wholeIsNeg = whole.compareTo(java.math.BigInteger.ZERO) < 0;
		if (wholeIsNeg) {
			whole = whole.negate();
		}
		num = whole.multiply(den).add(num);
		if (wholeIsNeg) {
			num = num.negate();
		}
		return new org.apache.commons.math.fraction.BigFraction(num, den);
	}

	/**
	 * Modify the whole format.
	 *
	 * @param format
	 * 		The new whole format value.
	 * @throws NullArgumentException
	 * 		if {@code format} is {@code null}.
	 */
	public void setWholeFormat(final java.text.NumberFormat format) {
		if (format == null) {
			throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.WHOLE_FORMAT);
		}
		this.wholeFormat = format;
	}
}