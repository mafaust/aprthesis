package org.apache.commons.math.fraction;
/**
 * Formats a Fraction number in proper format.  The number format for each of
 * the whole number, numerator and, denominator can be configured.
 * <p>
 * Minus signs are only allowed in the whole number part - i.e.,
 * "-3 1/2" is legitimate and denotes -7/2, but "-3 -1/2" is invalid and
 * will result in a <code>ParseException</code>.</p>
 *
 * @since 1.1
 * @version $Revision$ $Date$
 */
public class ProperFractionFormat extends org.apache.commons.math.fraction.FractionFormat {
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
	public ProperFractionFormat() {
		this(org.apache.commons.math.fraction.FractionFormat.getDefaultNumberFormat());
	}

	/**
	 * Create a proper formatting instance with a custom number format for the
	 * whole, numerator, and denominator.
	 *
	 * @param format
	 * 		the custom format for the whole, numerator, and
	 * 		denominator.
	 */
	public ProperFractionFormat(java.text.NumberFormat format) {
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
	public ProperFractionFormat(java.text.NumberFormat wholeFormat, java.text.NumberFormat numeratorFormat, java.text.NumberFormat denominatorFormat) {
		super(numeratorFormat, denominatorFormat);
		setWholeFormat(wholeFormat);
	}

	/**
	 * Formats a {@link Fraction} object to produce a string.  The fraction
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
	public java.lang.StringBuffer format(org.apache.commons.math.fraction.Fraction fraction, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
		pos.setBeginIndex(0);
		pos.setEndIndex(0);
		int num = fraction.getNumerator();
		int den = fraction.getDenominator();
		int whole = num / den;
		num = num % den;
		if (whole != 0) {
			getWholeFormat().format(whole, toAppendTo, pos);
			toAppendTo.append(' ');
			num = java.lang.Math.abs(num);
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
	 * Parses a string to produce a {@link Fraction} object.  This method
	 * expects the string to be formatted as a proper fraction.
	 * <p>
	 * Minus signs are only allowed in the whole number part - i.e.,
	 * "-3 1/2" is legitimate and denotes -7/2, but "-3 -1/2" is invalid and
	 * will result in a <code>ParseException</code>.</p>
	 *
	 * @param source
	 * 		the string to parse
	 * @param pos
	 * 		input/ouput parsing parameter.
	 * @return the parsed {@link Fraction} object.
	 */
	public org.apache.commons.math.fraction.Fraction parse(java.lang.String source, java.text.ParsePosition pos) {
		// try to parse improper fraction
		org.apache.commons.math.fraction.Fraction ret = super.parse(source, pos);
		if (ret != null) {
			return ret;
		}
		int initialIndex = pos.getIndex();
		// parse whitespace
		org.apache.commons.math.fraction.FractionFormat.parseAndIgnoreWhitespace(source, pos);
		// parse whole
		java.lang.Number whole = getWholeFormat().parse(source, pos);
		if (whole == null) {
			// invalid integer number
			// set index back to initial, error index should already be set
			// character examined.
			pos.setIndex(initialIndex);
			return null;
		}
		// parse whitespace
		org.apache.commons.math.fraction.FractionFormat.parseAndIgnoreWhitespace(source, pos);
		// parse numerator
		java.lang.Number num = getNumeratorFormat().parse(source, pos);
		if (num == null) {
			// invalid integer number
			// set index back to initial, error index should already be set
			// character examined.
			pos.setIndex(initialIndex);
			return null;
		}
		if (num.intValue() < 0) {
			// minus signs should be leading, invalid expression
			pos.setIndex(initialIndex);
			return null;
		}
		// parse '/'
		int startIndex = pos.getIndex();
		char c = org.apache.commons.math.fraction.FractionFormat.parseNextCharacter(source, pos);
		switch (c) {
			case 0 :
				// no '/'
				// return num as a fraction
				return new org.apache.commons.math.fraction.Fraction(num.intValue(), 1);
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
		org.apache.commons.math.fraction.FractionFormat.parseAndIgnoreWhitespace(source, pos);
		// parse denominator
		java.lang.Number den = getDenominatorFormat().parse(source, pos);
		if (den == null) {
			// invalid integer number
			// set index back to initial, error index should already be set
			// character examined.
			pos.setIndex(initialIndex);
			return null;
		}
		if (den.intValue() < 0) {
			// minus signs must be leading, invalid
			pos.setIndex(initialIndex);
			return null;
		}
		int w = whole.intValue();
		int n = num.intValue();
		int d = den.intValue();
		return new org.apache.commons.math.fraction.Fraction(((java.lang.Math.abs(w) * d) + n) * org.apache.commons.math.util.MathUtils.sign(w), d);
	}

	/**
	 * Modify the whole format.
	 *
	 * @param format
	 * 		The new whole format value.
	 * @throws IllegalArgumentException
	 * 		if <code>format</code> is
	 * 		<code>null</code>.
	 */
	public void setWholeFormat(java.text.NumberFormat format) {
		if (format == null) {
			throw new java.lang.IllegalArgumentException("whole format can not be null.");
		}
		this.wholeFormat = format;
	}
}