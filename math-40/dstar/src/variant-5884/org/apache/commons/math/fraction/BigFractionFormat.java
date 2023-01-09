package org.apache.commons.math.fraction;





























/**
 * Formats a BigFraction number in proper format or improper format.
 * <p>
 * The number format for each of the whole number, numerator and,
 * denominator can be configured.
 * </p>
 *
 * @since 2.0
 * @version $Id$
 */
public class BigFractionFormat extends org.apache.commons.math.fraction.AbstractFormat implements java.io.Serializable {

	/**
	 * Serializable version identifier
	 */ 	private static final long serialVersionUID = -2932167925527338976L;
	/**
	 * Create an improper formatting instance with the default number format
	 * for the numerator and denominator.
	 */
	public BigFractionFormat() {
	}

	/**
	 * Create an improper formatting instance with a custom number format for
	 * both the numerator and denominator.
	 *
	 * @param format
	 * 		the custom format for both the numerator and denominator.
	 */ 	public BigFractionFormat(final java.text.NumberFormat format) { 		super(format);
	}

	/**
	 * Create an improper formatting instance with a custom number format for
	 * the numerator and a custom number format for the denominator.
	 *
	 * @param numeratorFormat
	 * 		the custom format for the numerator.
	 * @param denominatorFormat
	 * 		the custom format for the denominator.
	 */ 	public BigFractionFormat(final java.text.NumberFormat numeratorFormat, final java.text.NumberFormat denominatorFormat) { 		super(numeratorFormat, denominatorFormat);
	}

	/**
	 * Get the set of locales for which complex formats are available.  This
	 * is the same set as the {@link NumberFormat} set.
	 *
	 * @return available complex format locales.
	 */ 	public static java.util.Locale[] getAvailableLocales() {
		return java.text.NumberFormat.getAvailableLocales();
	}

	/**
	 * This static method calls formatBigFraction() on a default instance of
	 * BigFractionFormat.
	 *
	 * @param f
	 * 		BigFraction object to format
	 * @return A formatted BigFraction in proper form.
	 */ 	public static java.lang.String formatBigFraction(final org.apache.commons.math.fraction.BigFraction f) {
		return org.apache.commons.math.fraction.BigFractionFormat.getImproperInstance().format(f);
	}

	/**
	 * Returns the default complex format for the current locale.
	 *
	 * @return the default complex format.
	 */ 	public static org.apache.commons.math.fraction.BigFractionFormat getImproperInstance() {
		return org.apache.commons.math.fraction.BigFractionFormat.getImproperInstance(java.util.Locale.getDefault());
	}

	/**
	 * Returns the default complex format for the given locale.
	 *
	 * @param locale
	 * 		the specific locale used by the format.
	 * @return the complex format specific to the given locale.
	 */ 	public static org.apache.commons.math.fraction.BigFractionFormat getImproperInstance(final java.util.Locale locale) { 		return new org.apache.commons.math.fraction.BigFractionFormat(org.apache.commons.math.fraction.AbstractFormat.getDefaultNumberFormat(locale));
	}

	/**
	 * Returns the default complex format for the current locale.
	 *
	 * @return the default complex format.
	 */ 	public static org.apache.commons.math.fraction.BigFractionFormat getProperInstance() {
		return org.apache.commons.math.fraction.BigFractionFormat.getProperInstance(java.util.Locale.getDefault());
	}

	/**
	 * Returns the default complex format for the given locale.
	 *
	 * @param locale
	 * 		the specific locale used by the format.
	 * @return the complex format specific to the given locale.
	 */ 	public static org.apache.commons.math.fraction.BigFractionFormat getProperInstance(final java.util.Locale locale) { 		return new org.apache.commons.math.fraction.ProperBigFractionFormat(org.apache.commons.math.fraction.AbstractFormat.getDefaultNumberFormat(locale));
	}

	/**
	 * Formats a {@link BigFraction} object to produce a string.  The BigFraction is
	 * output in improper format.
	 *
	 * @param BigFraction
	 * 		the object to format.
	 * @param toAppendTo
	 * 		where the text is to be appended
	 * @param pos
	 * 		On input: an alignment field, if desired. On output: the
	 * 		offsets of the alignment field
	 * @return the value passed in as toAppendTo.
	 */ 	public java.lang.StringBuffer format(final org.apache.commons.math.fraction.BigFraction BigFraction, final java.lang.StringBuffer toAppendTo, final java.text.FieldPosition pos) {
		pos.setBeginIndex(0);
		pos.setEndIndex(0);

		getNumeratorFormat().format(BigFraction.getNumerator(), toAppendTo, pos);
		toAppendTo.append(" / ");
		getDenominatorFormat().format(BigFraction.getDenominator(), toAppendTo, pos);

		return toAppendTo;
	}

	/**
	 * Formats an object and appends the result to a StringBuffer.
	 * <code>obj</code> must be either a  {@link BigFraction} object or a
	 * {@link BigInteger} object or a {@link Number} object. Any other type of
	 * object will result in an {@link IllegalArgumentException} being thrown.
	 *
	 * @param obj
	 * 		the object to format.
	 * @param toAppendTo
	 * 		where the text is to be appended
	 * @param pos
	 * 		On input: an alignment field, if desired. On output: the
	 * 		offsets of the alignment field
	 * @return the value passed in as toAppendTo.
	 * @see java.text.Format#format(java.lang.Object, java.lang.StringBuffer, java.text.FieldPosition)
	 * @throws MathIllegalArgumentException
	 * 		if <code>obj</code> is not a valid type.
	 */ 	@java.lang.Override 	public java.lang.StringBuffer format(final java.lang.Object obj, final java.lang.StringBuffer toAppendTo, final java.text.FieldPosition pos) {
		final java.lang.StringBuffer ret;
		if (obj instanceof org.apache.commons.math.fraction.BigFraction) {
			ret = format(((org.apache.commons.math.fraction.BigFraction) (obj)), toAppendTo, pos);
		} else if (obj instanceof java.math.BigInteger) {
			ret = format(new org.apache.commons.math.fraction.BigFraction(((java.math.BigInteger) (obj))), toAppendTo, pos);
		} else if (obj instanceof java.lang.Number) {
			ret = format(new org.apache.commons.math.fraction.BigFraction(((java.lang.Number) (obj)).doubleValue()), 
			toAppendTo, pos);
		} else {
			throw new org.apache.commons.math.exception.MathIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_FORMAT_OBJECT_TO_FRACTION);
		}

		return ret;
	}

	/**
	 * Parses a string to produce a {@link BigFraction} object.
	 *
	 * @param source
	 * 		the string to parse
	 * @return the parsed {@link BigFraction} object.
	 * @exception MathParseException
	 * 		if the beginning of the specified string
	 * 		cannot be parsed.
	 */ 	@java.lang.Override 	public org.apache.commons.math.fraction.BigFraction parse(final java.lang.String source) throws org.apache.commons.math.exception.MathParseException { 		final java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
		final org.apache.commons.math.fraction.BigFraction result = parse(source, parsePosition);
		if (parsePosition.getIndex() == 0) {
			throw new org.apache.commons.math.exception.MathParseException(source, parsePosition.getErrorIndex(), org.apache.commons.math.fraction.BigFraction.class);
		}
		return result;
	}

	/**
	 * Parses a string to produce a {@link BigFraction} object.
	 * This method expects the string to be formatted as an improper BigFraction.
	 *
	 * @param source
	 * 		the string to parse
	 * @param pos
	 * 		input/output parsing parameter.
	 * @return the parsed {@link BigFraction} object.
	 */ 	@java.lang.Override 	public org.apache.commons.math.fraction.BigFraction parse(final java.lang.String source, final java.text.ParsePosition pos) { 		final int initialIndex = pos.getIndex();

		// parse whitespace
		org.apache.commons.math.fraction.AbstractFormat.parseAndIgnoreWhitespace(source, pos);

		// parse numerator
		final java.math.BigInteger num = parseNextBigInteger(source, pos);
		if (num == null) {
			// invalid integer number
			// set index back to initial, error index should already be set
			// character examined.
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
				return null;}


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

		return new org.apache.commons.math.fraction.BigFraction(num, den);
	}

	/**
	 * Parses a string to produce a <code>BigInteger</code>.
	 *
	 * @param source
	 * 		the string to parse
	 * @param pos
	 * 		input/output parsing parameter.
	 * @return a parsed <code>BigInteger</code> or null if string does not
	contain a BigInteger at the specified position
	 */ 	protected java.math.BigInteger parseNextBigInteger(final java.lang.String source, final java.text.ParsePosition pos) {
		final int start = pos.getIndex();
		int end = (source.charAt(start) == '-') ? start + 1 : start;
		while ((end < source.length()) && 
		java.lang.Character.isDigit(source.charAt(end))) {
			++end;
		} 

		try {
			java.math.BigInteger n = new java.math.BigInteger(source.substring(start, end));
			pos.setIndex(end);
			return n;
		} catch (java.lang.NumberFormatException nfe) {
			pos.setErrorIndex(start);
			return null;
		}

	}}