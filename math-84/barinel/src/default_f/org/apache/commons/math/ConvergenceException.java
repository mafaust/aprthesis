package org.apache.commons.math;
/**
 * Error thrown when a numerical computation can not be performed because the
 * numerical result failed to converge to a finite value.
 *
 * @version $Revision$ $Date$
 */
public class ConvergenceException extends org.apache.commons.math.MathException {
	/**
	 * Serializable version identifier
	 */
	private static final long serialVersionUID = 4883703247677159141L;

	/**
	 * Default constructor.
	 */
	public ConvergenceException() {
		super("Convergence failed");
	}

	/**
	 * Constructs an exception with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @since 1.2
	 */
	public ConvergenceException(java.lang.String pattern, java.lang.Object... arguments) {
		super(pattern, arguments);
	}

	/**
	 * Create an exception with a given root cause.
	 *
	 * @param cause
	 * 		the exception or error that caused this exception to be thrown
	 */
	public ConvergenceException(java.lang.Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs an exception with specified formatted detail message and root cause.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param cause
	 * 		the exception or error that caused this exception to be thrown
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @since 1.2
	 */
	public ConvergenceException(java.lang.Throwable cause, java.lang.String pattern, java.lang.Object... arguments) {
		super(cause, pattern, arguments);
	}
}