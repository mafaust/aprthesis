package org.apache.commons.math.fraction;


























/**
 * Common part shared by both {@link FractionFormat} and {@link BigFractionFormat}.
 *
 * @version $Revision$ $Date$
 * @since 2.0
 */
public abstract class AbstractFormat extends java.text.NumberFormat implements java.io.Serializable {
	/**
	 * Serializable version identifier.
	 */ 	private static final long serialVersionUID = -6981118387974191891L;
	/**
	 * The format used for the denominator.
	 */ 	protected java.text.NumberFormat denominatorFormat;
	/**
	 * The format used for the numerator.
	 */ 	protected java.text.NumberFormat numeratorFormat;
	/**
	 * Create an improper formatting instance with the default number format
	 * for the numerator and denominator.
	 */
	protected AbstractFormat() {
		this(org.apache.commons.math.fraction.AbstractFormat.getDefaultNumberFormat());
	}

	/**
	 * Create an improper formatting instance with a custom number format for
	 * both the numerator and denominator.
	 *
	 * @param format
	 * 		the custom format for both the numerator and denominator.
	 */ 	protected AbstractFormat(final java.text.NumberFormat format) { 		this(format, ((java.text.NumberFormat) (format.clone())));
	}

	/**
	 * Create an improper formatting instance with a custom number format for
	 * the numerator and a custom number format for the denominator.
	 *
	 * @param numeratorFormat
	 * 		the custom format for the numerator.
	 * @param denominatorFormat
	 * 		the custom format for the denominator.
	 */ 	protected AbstractFormat(final java.text.NumberFormat numeratorFormat, final java.text.NumberFormat denominatorFormat) { 		this.numeratorFormat = numeratorFormat;
		this.denominatorFormat = denominatorFormat;
	}

	/**
	 * Create a default number format.  The default number format is based on
	 * {@link NumberFormat#getNumberInstance(java.util.Locale)} with the only
	 * customizing is the maximum number of BigFraction digits, which is set to 0.
	 *
	 * @return the default number format.
	 */ 	protected static java.text.NumberFormat getDefaultNumberFormat() {
		return org.apache.commons.math.fraction.AbstractFormat.getDefaultNumberFormat(java.util.Locale.getDefault());
	}

	/**
	 * Create a default number format.  The default number format is based on
	 * {@link NumberFormat#getNumberInstance(java.util.Locale)} with the only
	 * customizing is the maximum number of BigFraction digits, which is set to 0.
	 *
	 * @param locale
	 * 		the specific locale used by the format.
	 * @return the default number format specific to the given locale.
	 */ 	protected static java.text.NumberFormat getDefaultNumberFormat(final java.util.Locale locale) { 		final java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(locale);
		nf.setMaximumFractionDigits(0);
		nf.setParseIntegerOnly(true);
		return nf;
	}

	/**
	 * Access the denominator format.
	 *
	 * @return the denominator format.
	 */ 	public java.text.NumberFormat getDenominatorFormat() {
		return denominatorFormat;
	}

	/**
	 * Access the numerator format.
	 *
	 * @return the numerator format.
	 */ 	public java.text.NumberFormat getNumeratorFormat() {
		return numeratorFormat;
	}

	/**
	 * Modify the denominator format.
	 *
	 * @param format
	 * 		the new denominator format value.
	 * @throws IllegalArgumentException
	 * 		if <code>format</code> is
	 * 		<code>null</code>.
	 */ 	public void setDenominatorFormat(final java.text.NumberFormat format) { 		if (format == null) { 			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(
			"denominator format can not be null");
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
	 */ 	public void setNumeratorFormat(final java.text.NumberFormat format) { 		if (format == null) { 			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(
			"numerator format can not be null");
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
	 */ 	protected static void parseAndIgnoreWhitespace(final java.lang.String source, final java.text.ParsePosition pos) { 		org.apache.commons.math.fraction.AbstractFormat.parseNextCharacter(source, pos);
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
	 */ 	protected static char parseNextCharacter(final java.lang.String source, final java.text.ParsePosition pos) { 		int index = pos.getIndex();
		final int n = source.length();
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

	/**
	 * Formats a double value as a fraction and appends the result to a StringBuffer.
	 *
	 * @param value
	 * 		the double value to format
	 * @param buffer
	 * 		StringBuffer to append to
	 * @param position
	 * 		On input: an alignment field, if desired. On output: the
	 * 		offsets of the alignment field
	 * @return a reference to the appended buffer
	 * @see #format(Object, StringBuffer, FieldPosition)
	 */ 	@java.lang.Override 	public java.lang.StringBuffer format(final double value, final java.lang.StringBuffer buffer, final java.text.FieldPosition position) {
		return format(java.lang.Double.valueOf(value), buffer, position);
	}


	/**
	 * Formats a long value as a fraction and appends the result to a StringBuffer.
	 *
	 * @param value
	 * 		the long value to format
	 * @param buffer
	 * 		StringBuffer to append to
	 * @param position
	 * 		On input: an alignment field, if desired. On output: the
	 * 		offsets of the alignment field
	 * @return a reference to the appended buffer
	 * @see #format(Object, StringBuffer, FieldPosition)
	 */ 	@java.lang.Override 	public java.lang.StringBuffer format(final long value, final java.lang.StringBuffer buffer, final java.text.FieldPosition position) {
		return format(java.lang.Long.valueOf(value), buffer, position);
	}}