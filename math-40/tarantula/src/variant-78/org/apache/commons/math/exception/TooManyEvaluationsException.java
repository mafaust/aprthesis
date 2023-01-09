package org.apache.commons.math.exception;



















/**
 * Exception to be thrown when the maximal number of evaluations is exceeded.
 *
 * @since 3.0
 * @version $Id$
 */
public class TooManyEvaluationsException extends org.apache.commons.math.exception.MaxCountExceededException {
	/**
	 * Serializable version Id.
	 */ 	private static final long serialVersionUID = 4330003017885151975L;
	/**
	 * Construct the exception.
	 *
	 * @param max
	 * 		Maximum number of evaluations.
	 */ 	public TooManyEvaluationsException(java.lang.Number max) {
		super(max);
		getContext().addMessage(org.apache.commons.math.exception.util.LocalizedFormats.EVALUATIONS);
	}}