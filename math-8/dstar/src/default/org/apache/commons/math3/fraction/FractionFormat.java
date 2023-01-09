package org.apache.commons.math3.fraction;



























/**
 * Formats a Fraction number in proper format or improper format.  The number
 * format for each of the whole number, numerator and, denominator can be
 * configured.
 *
 * @since 1.1
 * @version $Id$
 */
public class FractionFormat extends org.apache.commons.math3.fraction.AbstractFormat {

	/**
	 * Serializable version identifier
	 */ 	private static final long serialVersionUID = 3008655719530972611L;
	/**
	 * Create an improper formatting instance with the default number format
	 * for the numerator and denominator.
	 */
	public FractionFormat() {
	}

	/**
	 * Create an improper formatting instance with a custom number format for
	 * both the numerator and denominator.
	 *
	 * @param format
	 * 		the custom format for both the numerator and denominator.
	 */ 	public FractionFormat(final java.text.NumberFormat format) { 		super(format);
	}

	/**
	 * Create an improper formatting instance with a custom number format for
	 * the numerator and a custom number format for the denominator.
	 *
	 * @param numeratorFormat
	 * 		the custom format for the numerator.
	 * @param denominatorFormat
	 * 		the custom format for the denominator.
	 */ 	public FractionFormat(final java.text.NumberFormat numeratorFormat, final java.text.NumberFormat denominatorFormat) { 		super(numeratorFormat, denominatorFormat);
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
	 * This static method calls formatFraction() on a default instance of
	 * FractionFormat.
	 *
	 * @param f
	 * 		Fraction object to format
	 * @return a formatted fraction in proper form.
	 */ 	public static java.lang.String formatFraction(org.apache.commons.math3.fraction.Fraction f) {
		return org.apache.commons.math3.fraction.FractionFormat.getImproperInstance().format(f);
	}

	/**
	 * Returns the default complex format for the current locale.
	 *
	 * @return the default complex format.
	 */ 	public static org.apache.commons.math3.fraction.FractionFormat getImproperInstance() {
		return org.apache.commons.math3.fraction.FractionFormat.getImproperInstance(java.util.Locale.getDefault());
	}

	/**
	 * Returns the default complex format for the given locale.
	 *
	 * @param locale
	 * 		the specific locale used by the format.
	 * @return the complex format specific to the given locale.
	 */ 	public static org.apache.commons.math3.fraction.FractionFormat getImproperInstance(final java.util.Locale locale) { 		return new org.apache.commons.math3.fraction.FractionFormat(org.apache.commons.math3.fraction.AbstractFormat.getDefaultNumberFormat(locale));
	}

	/**
	 * Returns the default complex format for the current locale.
	 *
	 * @return the default complex format.
	 */ 	public static org.apache.commons.math3.fraction.FractionFormat getProperInstance() {
		return org.apache.commons.math3.fraction.FractionFormat.getProperInstance(java.util.Locale.getDefault());
	}

	/**
	 * Returns the default complex format for the given locale.
	 *
	 * @param locale
	 * 		the specific locale used by the format.
	 * @return the complex format specific to the given locale.
	 */ 	public static org.apache.commons.math3.fraction.FractionFormat getProperInstance(final java.util.Locale locale) { 		return new org.apache.commons.math3.fraction.ProperFractionFormat(org.apache.commons.math3.fraction.AbstractFormat.getDefaultNumberFormat(locale));
	}

	/**
	 * Create a default number format.  The default number format is based on
	 * {@link NumberFormat#getNumberInstance(java.util.Locale)} with the only
	 * customizing is the maximum number of fraction digits, which is set to 0.
	 *
	 * @return the default number format.
	 */ 	protected static java.text.NumberFormat getDefaultNumberFormat() {
		return org.apache.commons.math3.fraction.AbstractFormat.getDefaultNumberFormat(java.util.Locale.getDefault());
	}

	/**
	 * Formats a {@link Fraction} object to produce a string.  The fraction is
	 * output in improper format.
	 *
	 * @param fraction
	 * 		the object to format.
	 * @param toAppendTo
	 * 		where the text is to be appended
	 * @param pos
	 * 		On input: an alignment field, if desired. On output: the
	 * 		offsets of the alignment field
	 * @return the value passed in as toAppendTo.
	 */ 	public java.lang.StringBuffer format(final org.apache.commons.math3.fraction.Fraction fraction, final java.lang.StringBuffer toAppendTo, final java.text.FieldPosition pos) {
		pos.setBeginIndex(0);
		pos.setEndIndex(0);

		getNumeratorFormat().format(fraction.getNumerator(), toAppendTo, pos);
		toAppendTo.append(" / ");
		getDenominatorFormat().format(fraction.getDenominator(), toAppendTo, 
		pos);

		return toAppendTo;
	}

	/**
	 * Formats an object and appends the result to a StringBuffer. <code>obj</code> must be either a
	 * {@link Fraction} object or a {@link Number} object.  Any other type of
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
	 * @throws FractionConversionException
	 * 		if the number cannot be converted to a fraction
	 * @throws MathIllegalArgumentException
	 * 		if <code>obj</code> is not a valid type.
	 */ 	@java.lang.Override 	public java.lang.StringBuffer format(final java.lang.Object obj, final java.lang.StringBuffer toAppendTo, final java.text.FieldPosition pos) throws org.apache.commons.math3.fraction.FractionConversionException, org.apache.commons.math3.exception.MathIllegalArgumentException { 		java.lang.StringBuffer ret = null;

		if (obj instanceof org.apache.commons.math3.fraction.Fraction) {
			ret = format(((org.apache.commons.math3.fraction.Fraction) (obj)), toAppendTo, pos);
		} else if (obj instanceof java.lang.Number) {
			ret = format(new org.apache.commons.math3.fraction.Fraction(((java.lang.Number) (obj)).doubleValue()), toAppendTo, pos);
		} else {
			throw new org.apache.commons.math3.exception.MathIllegalArgumentException(org.apache.commons.math3.exception.util.LocalizedFormats.CANNOT_FORMAT_OBJECT_TO_FRACTION);
		}

		return ret;
	}

	/**
	 * Parses a string to produce a {@link Fraction} object.
	 *
	 * @param source
	 * 		the string to parse
	 * @return the parsed {@link Fraction} object.
	 * @exception MathParseException
	 * 		if the beginning of the specified string
	 * 		cannot be parsed.
	 */ 	@java.lang.Override 	public org.apache.commons.math3.fraction.Fraction parse(final java.lang.String source) throws org.apache.commons.math3.exception.MathParseException { 		final java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
		final org.apache.commons.math3.fraction.Fraction result = parse(source, parsePosition);
		if (parsePosition.getIndex() == 0) {
			throw new org.apache.commons.math3.exception.MathParseException(source, parsePosition.getErrorIndex(), org.apache.commons.math3.fraction.Fraction.class);
		}
		return result;
	}

	/**
	 * Parses a string to produce a {@link Fraction} object.  This method
	 * expects the string to be formatted as an improper fraction.
	 *
	 * @param source
	 * 		the string to parse
	 * @param pos
	 * 		input/output parsing parameter.
	 * @return the parsed {@link Fraction} object.
	 */ 	@java.lang.Override 	public org.apache.commons.math3.fraction.Fraction parse(final java.lang.String source, final java.text.ParsePosition pos) { 		final int initialIndex = pos.getIndex();

		// parse whitespace
		org.apache.commons.math3.fraction.AbstractFormat.parseAndIgnoreWhitespace(source, pos);

		// parse numerator
		final java.lang.Number num = getNumeratorFormat().parse(source, pos);
		if (num == null) {
			// invalid integer number
			// set index back to initial, error index should already be set
			// character examined.
			pos.setIndex(initialIndex);
			return null;
		}

		// parse '/'
		final int startIndex = pos.getIndex();
		final char c = org.apache.commons.math3.fraction.AbstractFormat.parseNextCharacter(source, pos);
		switch (c) {
			case 0 :
				// no '/'
				// return num as a fraction
				return new org.apache.commons.math3.fraction.Fraction(num.intValue(), 1);
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
		org.apache.commons.math3.fraction.AbstractFormat.parseAndIgnoreWhitespace(source, pos);

		// parse denominator
		final java.lang.Number den = getDenominatorFormat().parse(source, pos);
		if (den == null) {
			// invalid integer number
			// set index back to initial, error index should already be set
			// character examined.
			pos.setIndex(initialIndex);
			return null;
		}

		return new org.apache.commons.math3.fraction.Fraction(num.intValue(), den.intValue());
	}}