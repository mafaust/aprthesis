package org.apache.commons.math.fraction;
/**
 * Formats a Fraction number in proper format or improper format.  The number
 * format for each of the whole number, numerator and, denominator can be
 * configured.
 *
 * @since 1.1
 * @version $Revision$ $Date$
 */
public class FractionFormat extends java.text.Format implements java.io.Serializable {
	/**
	 * Serializable version identifier
	 */
	private static final long serialVersionUID = -6337346779577272306L;

	/**
	 * The format used for the denominator.
	 */
	private java.text.NumberFormat denominatorFormat;

	/**
	 * The format used for the numerator.
	 */
	private java.text.NumberFormat numeratorFormat;

	/**
	 * Create an improper formatting instance with the default number format
	 * for the numerator and denominator.
	 */
	public FractionFormat() {
		this(org.apache.commons.math.fraction.FractionFormat.getDefaultNumberFormat());
	}

	/**
	 * Create an improper formatting instance with a custom number format for
	 * both the numerator and denominator.
	 *
	 * @param format
	 * 		the custom format for both the numerator and denominator.
	 */
	public FractionFormat(java.text.NumberFormat format) {
		this(format, ((java.text.NumberFormat) (format.clone())));
	}

	/**
	 * Create an improper formatting instance with a custom number format for
	 * the numerator and a custom number format for the denominator.
	 *
	 * @param numeratorFormat
	 * 		the custom format for the numerator.
	 * @param denominatorFormat
	 * 		the custom format for the denominator.
	 */
	public FractionFormat(java.text.NumberFormat numeratorFormat, java.text.NumberFormat denominatorFormat) {
		super();
		this.numeratorFormat = numeratorFormat;
		this.denominatorFormat = denominatorFormat;
	}

	/**
	 * This static method calls formatFraction() on a default instance of
	 * FractionFormat.
	 *
	 * @param f
	 * 		Fraction object to format
	 * @return A formatted fraction in proper form.
	 */
	public static java.lang.String formatFraction(org.apache.commons.math.fraction.Fraction f) {
		return org.apache.commons.math.fraction.FractionFormat.getImproperInstance().format(f);
	}

	/**
	 * Get the set of locales for which complex formats are available.  This
	 * is the same set as the {@link NumberFormat} set.
	 *
	 * @return available complex format locales.
	 */
	public static java.util.Locale[] getAvailableLocales() {
		return java.text.NumberFormat.getAvailableLocales();
	}

	/**
	 * Returns the default complex format for the current locale.
	 *
	 * @return the default complex format.
	 */
	public static org.apache.commons.math.fraction.FractionFormat getImproperInstance() {
		return org.apache.commons.math.fraction.FractionFormat.getImproperInstance(java.util.Locale.getDefault());
	}

	/**
	 * Returns the default complex format for the given locale.
	 *
	 * @param locale
	 * 		the specific locale used by the format.
	 * @return the complex format specific to the given locale.
	 */
	public static org.apache.commons.math.fraction.FractionFormat getImproperInstance(java.util.Locale locale) {
		java.text.NumberFormat f = org.apache.commons.math.fraction.FractionFormat.getDefaultNumberFormat(locale);
		return new org.apache.commons.math.fraction.FractionFormat(f);
	}

	/**
	 * Returns the default complex format for the current locale.
	 *
	 * @return the default complex format.
	 */
	public static org.apache.commons.math.fraction.FractionFormat getProperInstance() {
		return org.apache.commons.math.fraction.FractionFormat.getProperInstance(java.util.Locale.getDefault());
	}

	/**
	 * Returns the default complex format for the given locale.
	 *
	 * @param locale
	 * 		the specific locale used by the format.
	 * @return the complex format specific to the given locale.
	 */
	public static org.apache.commons.math.fraction.FractionFormat getProperInstance(java.util.Locale locale) {
		java.text.NumberFormat f = org.apache.commons.math.fraction.FractionFormat.getDefaultNumberFormat(locale);
		return new org.apache.commons.math.fraction.ProperFractionFormat(f);
	}

	/**
	 * Create a default number format.  The default number format is based on
	 * {@link NumberFormat#getNumberInstance(java.util.Locale)} with the only
	 * customizing is the maximum number of fraction digits, which is set to 0.
	 *
	 * @return the default number format.
	 */
	protected static java.text.NumberFormat getDefaultNumberFormat() {
		return org.apache.commons.math.fraction.FractionFormat.getDefaultNumberFormat(java.util.Locale.getDefault());
	}

	/**
	 * Create a default number format.  The default number format is based on
	 * {@link NumberFormat#getNumberInstance(java.util.Locale)} with the only
	 * customizing is the maximum number of fraction digits, which is set to 0.
	 *
	 * @param locale
	 * 		the specific locale used by the format.
	 * @return the default number format specific to the given locale.
	 */
	private static java.text.NumberFormat getDefaultNumberFormat(java.util.Locale locale) {
		java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(locale);
		nf.setMaximumFractionDigits(0);
		nf.setParseIntegerOnly(true);
		return nf;
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
	 */
	public java.lang.StringBuffer format(org.apache.commons.math.fraction.Fraction fraction, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
		pos.setBeginIndex(0);
		pos.setEndIndex(0);
		getNumeratorFormat().format(fraction.getNumerator(), toAppendTo, pos);
		toAppendTo.append(" / ");
		getDenominatorFormat().format(fraction.getDenominator(), toAppendTo, pos);
		return toAppendTo;
	}

	/**
	 * Formats a object to produce a string.  <code>obj</code> must be either a
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
	 * @throws IllegalArgumentException
	 * 		is <code>obj</code> is not a valid type.
	 */
	public java.lang.StringBuffer format(java.lang.Object obj, java.lang.StringBuffer toAppendTo, java.text.FieldPosition pos) {
		java.lang.StringBuffer ret = null;
		if (obj instanceof org.apache.commons.math.fraction.Fraction) {
			ret = format(((org.apache.commons.math.fraction.Fraction) (obj)), toAppendTo, pos);
		} else if (obj instanceof java.lang.Number) {
			try {
				ret = format(new org.apache.commons.math.fraction.Fraction(((java.lang.Number) (obj)).doubleValue()), toAppendTo, pos);
			} catch (org.apache.commons.math.ConvergenceException ex) {
				throw new java.lang.IllegalArgumentException("Cannot convert given object to a fraction.");
			}
		} else {
			throw new java.lang.IllegalArgumentException("Cannot format given object as a fraction");
		}
		return ret;
	}

	/**
	 * Access the denominator format.
	 *
	 * @return the denominator format.
	 */
	public java.text.NumberFormat getDenominatorFormat() {
		return denominatorFormat;
	}

	/**
	 * Access the numerator format.
	 *
	 * @return the numerator format.
	 */
	public java.text.NumberFormat getNumeratorFormat() {
		return numeratorFormat;
	}

	/**
	 * Parses a string to produce a {@link Fraction} object.
	 *
	 * @param source
	 * 		the string to parse
	 * @return the parsed {@link Fraction} object.
	 * @exception ParseException
	 * 		if the beginning of the specified string
	 * 		cannot be parsed.
	 */
	public org.apache.commons.math.fraction.Fraction parse(java.lang.String source) throws java.text.ParseException {
		java.text.ParsePosition parsePosition = new java.text.ParsePosition(0);
		org.apache.commons.math.fraction.Fraction result = parse(source, parsePosition);
		if (parsePosition.getIndex() == 0) {
			throw new java.text.ParseException(("Unparseable fraction number: \"" + source) + "\"", parsePosition.getErrorIndex());
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
	 * 		input/ouput parsing parameter.
	 * @return the parsed {@link Fraction} object.
	 */
	public org.apache.commons.math.fraction.Fraction parse(java.lang.String source, java.text.ParsePosition pos) {
		int initialIndex = pos.getIndex();
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
		return new org.apache.commons.math.fraction.Fraction(num.intValue(), den.intValue());
	}

	/**
	 * Parses a string to produce a object.
	 *
	 * @param source
	 * 		the string to parse
	 * @param pos
	 * 		input/ouput parsing parameter.
	 * @return the parsed object.
	 * @see java.text.Format#parseObject(java.lang.String, java.text.ParsePosition)
	 */
	public java.lang.Object parseObject(java.lang.String source, java.text.ParsePosition pos) {
		return parse(source, pos);
	}

	/**
	 * Modify the denominator format.
	 *
	 * @param format
	 * 		the new denominator format value.
	 * @throws IllegalArgumentException
	 * 		if <code>format</code> is
	 * 		<code>null</code>.
	 */
	public void setDenominatorFormat(java.text.NumberFormat format) {
		if (format == null) {
			throw new java.lang.IllegalArgumentException("denominator format can not be null.");
		}
		this.denominatorFormat = format;
	}

	/**
	 * Modify the numerator format.
	 *
	 * @param format
	 * 		the new numerator format value.
	 * @throws IllegalArgumentException
	 * 		if <code>format</code> is
	 * 		<code>null</code>.
	 */
	public void setNumeratorFormat(java.text.NumberFormat format) {
		if (format == null) {
			throw new java.lang.IllegalArgumentException("numerator format can not be null.");
		}
		this.numeratorFormat = format;
	}

	/**
	 * Parses <code>source</code> until a non-whitespace character is found.
	 *
	 * @param source
	 * 		the string to parse
	 * @param pos
	 * 		input/ouput parsing parameter.  On output, <code>pos</code>
	 * 		holds the index of the next non-whitespace character.
	 */
	protected static void parseAndIgnoreWhitespace(java.lang.String source, java.text.ParsePosition pos) {
		org.apache.commons.math.fraction.FractionFormat.parseNextCharacter(source, pos);
		pos.setIndex(pos.getIndex() - 1);
	}

	/**
	 * Parses <code>source</code> until a non-whitespace character is found.
	 *
	 * @param source
	 * 		the string to parse
	 * @param pos
	 * 		input/ouput parsing parameter.
	 * @return the first non-whitespace character.
	 */
	protected static char parseNextCharacter(java.lang.String source, java.text.ParsePosition pos) {
		int index = pos.getIndex();
		int n = source.length();
		char ret = 0;
		if (index < n) {
			char c;
			do {
				c = source.charAt(index++);
			} while (java.lang.Character.isWhitespace(c) && (index < n) );
			pos.setIndex(index);
			if (index < n) {
				ret = c;
			}
		}
		return ret;
	}
}