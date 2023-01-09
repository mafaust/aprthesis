package org.apache.commons.math.optimization;




















/**
 * Simple implementation of the {@link RealConvergenceChecker} interface using
 * only objective function values.
 * <p>
 * Convergence is considered to have been reached if either the relative
 * difference between the objective function values is smaller than a
 * threshold or if either the absolute difference between the objective
 * function values is smaller than another threshold.
 * </p>
 *
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class SimpleScalarValueChecker implements org.apache.commons.math.optimization.RealConvergenceChecker {
	/**
	 * Default relative threshold.
	 */ 	private static final double DEFAULT_RELATIVE_THRESHOLD = 100 * org.apache.commons.math.util.MathUtils.EPSILON;
	/**
	 * Default absolute threshold.
	 */ 	private static final double DEFAULT_ABSOLUTE_THRESHOLD = 100 * org.apache.commons.math.util.MathUtils.SAFE_MIN;
	/**
	 * Relative tolerance threshold.
	 */ 	private final double relativeThreshold;
	/**
	 * Absolute tolerance threshold.
	 */ 	private final double absoluteThreshold;
	/**
	 * Build an instance with default threshold.
	 */ 	public SimpleScalarValueChecker() {
		this.relativeThreshold = org.apache.commons.math.optimization.SimpleScalarValueChecker.DEFAULT_RELATIVE_THRESHOLD;
		this.absoluteThreshold = org.apache.commons.math.optimization.SimpleScalarValueChecker.DEFAULT_ABSOLUTE_THRESHOLD;
	}

	/**
	 * Build an instance with a specified threshold.
	 * <p>
	 * In order to perform only relative checks, the absolute tolerance
	 * must be set to a negative value. In order to perform only absolute
	 * checks, the relative tolerance must be set to a negative value.
	 * </p>
	 *
	 * @param relativeThreshold
	 * 		relative tolerance threshold
	 * @param absoluteThreshold
	 * 		absolute tolerance threshold
	 */ 	public SimpleScalarValueChecker(final double relativeThreshold, final double absoluteThreshold) { 		this.relativeThreshold = relativeThreshold; 		this.absoluteThreshold = absoluteThreshold;
	}

	/**
	 * {@inheritDoc }
	 */ 	public boolean converged(final int iteration, final org.apache.commons.math.optimization.RealPointValuePair previous, final 
	org.apache.commons.math.optimization.RealPointValuePair current) {
		final double p = previous.getValue();
		final double c = current.getValue();
		final double difference = java.lang.Math.abs(p - c);
		final double size = java.lang.Math.max(java.lang.Math.abs(p), java.lang.Math.abs(c));
		return (difference <= (size * relativeThreshold)) || (difference <= absoluteThreshold);
	}}