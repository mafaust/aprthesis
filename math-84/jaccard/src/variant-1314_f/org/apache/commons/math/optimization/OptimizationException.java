package org.apache.commons.math.optimization;
/**
 * This class represents exceptions thrown by optimizers.
 *
 * @version $Revision$ $Date$
 * @since 1.2
 */
public class OptimizationException extends org.apache.commons.math.ConvergenceException {
	/**
	 * Serializable version identifier.
	 */
	private static final long serialVersionUID = -357696069587075016L;

	/**
	 * Simple constructor.
	 * Build an exception by translating and formating a message
	 *
	 * @param specifier
	 * 		format specifier (to be translated)
	 * @param parts
	 * 		to insert in the format (no translation)
	 */
	public OptimizationException(java.lang.String specifier, java.lang.Object... parts) {
		super(specifier, parts);
	}

	/**
	 * Create an exception with a given root cause.
	 *
	 * @param cause
	 * 		the exception or error that caused this exception to be thrown
	 */
	public OptimizationException(java.lang.Throwable cause) {
		super(cause);
	}
}