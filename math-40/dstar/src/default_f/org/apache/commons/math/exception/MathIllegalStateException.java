package org.apache.commons.math.exception;
/**
 * Base class for all exceptions that signal a mismatch between the
 * current state and the user's expectations.
 *
 * @since 2.2
 * @version $Id$
 */
public class MathIllegalStateException extends java.lang.IllegalStateException implements org.apache.commons.math.exception.util.ExceptionContextProvider {
	/**
	 * Serializable version Id.
	 */
	private static final long serialVersionUID = -6024911025449780478L;

	/**
	 * Context.
	 */
	private final org.apache.commons.math.exception.util.ExceptionContext context;

	/**
	 * Simple constructor.
	 *
	 * @param pattern
	 * 		Message pattern explaining the cause of the error.
	 * @param args
	 * 		Arguments.
	 */
	public MathIllegalStateException(org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... args) {
		context = new org.apache.commons.math.exception.util.ExceptionContext(this);
		context.addMessage(pattern, args);
	}

	/**
	 * Simple constructor.
	 *
	 * @param cause
	 * 		Root cause.
	 * @param pattern
	 * 		Message pattern explaining the cause of the error.
	 * @param args
	 * 		Arguments.
	 */
	public MathIllegalStateException(java.lang.Throwable cause, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... args) {
		super(cause);
		context = new org.apache.commons.math.exception.util.ExceptionContext(this);
		context.addMessage(pattern, args);
	}

	/**
	 * Default constructor.
	 */
	public MathIllegalStateException() {
		this(org.apache.commons.math.exception.util.LocalizedFormats.ILLEGAL_STATE);
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.exception.util.ExceptionContext getContext() {
		return context;
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	public java.lang.String getMessage() {
		return context.getMessage();
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	public java.lang.String getLocalizedMessage() {
		return context.getLocalizedMessage();
	}
}