package org.apache.commons.math.exception;




















/**
 * Exception to be thrown when some counter maximum value is exceeded.
 *
 * @since 3.0
 * @version $Id$
 */
public class MaxCountExceededException extends org.apache.commons.math.exception.MathIllegalStateException {
	/**
	 * Serializable version Id.
	 */ 	private static final long serialVersionUID = 4330003017885151975L; 	/**
	 * Maximum number of evaluations.
	 */
	private final java.lang.Number max;

	/**
	 * Construct the exception.
	 *
	 * @param max
	 * 		Maximum.
	 */ 	public MaxCountExceededException(java.lang.Number max) {
		this(org.apache.commons.math.exception.util.LocalizedFormats.MAX_COUNT_EXCEEDED, max);
	}
	/**
	 * Construct the exception with a specific context.
	 *
	 * @param specific
	 * 		Specific context pattern.
	 * @param max
	 * 		Maximum.
	 * @param args
	 * 		Additional arguments.
	 */ 	public MaxCountExceededException(org.apache.commons.math.exception.util.Localizable specific, java.lang.Number max, java.lang.Object... args) {
		getContext().addMessage(specific, max, args);
		this.max = max;
	}

	/**
	 *
	 *
	 * @return the maximum number of evaluations.
	 */ 	public java.lang.Number getMax() { 		return max;
	}}