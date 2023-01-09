package org.apache.commons.math;




















/**
 * Error thrown when a numerical computation exceeds its allowed
 * number of iterations.
 *
 * @version $Revision$ $Date$
 * @since 1.2
 */
public class MaxIterationsExceededException extends org.apache.commons.math.ConvergenceException {

	/**
	 * Serializable version identifier.
	 */ 	private static final long serialVersionUID = -7821226672760574694L;
	/**
	 * Maximal number of iterations allowed.
	 */ 	private final int maxIterations;
	/**
	 * Constructs an exception with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param maxIterations
	 * 		maximal number of iterations allowed
	 */ 	public MaxIterationsExceededException(final int maxIterations) { 		super("Maximal number of iterations ({0}) exceeded", maxIterations);
		this.maxIterations = maxIterations;
	}

	/**
	 * Constructs an exception with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param maxIterations
	 * 		the exceeded maximal number of iterations
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 */ 	public MaxIterationsExceededException(final int maxIterations, final java.lang.String pattern, final java.lang.Object... arguments) { 		super(pattern, arguments); 		this.maxIterations = maxIterations;
	}

	/**
	 * Get the maximal number of iterations allowed.
	 *
	 * @return maximal number of iterations allowed
	 */ 	public int getMaxIterations() { 		return maxIterations;
	}}