package org.apache.commons.math.analysis.solvers;
/**
 * Base class for solvers.
 *
 * @version $Id$
 * @since 3.0
 */
public abstract class AbstractUnivariateRealSolver extends org.apache.commons.math.analysis.solvers.BaseAbstractUnivariateRealSolver<org.apache.commons.math.analysis.UnivariateFunction> implements org.apache.commons.math.analysis.solvers.UnivariateRealSolver {
	/**
	 * Construct a solver with given absolute accuracy.
	 *
	 * @param absoluteAccuracy
	 * 		Maximum absolute error.
	 */
	protected AbstractUnivariateRealSolver(final double absoluteAccuracy) {
		super(absoluteAccuracy);
	}

	/**
	 * Construct a solver with given accuracies.
	 *
	 * @param relativeAccuracy
	 * 		Maximum relative error.
	 * @param absoluteAccuracy
	 * 		Maximum absolute error.
	 */
	protected AbstractUnivariateRealSolver(final double relativeAccuracy, final double absoluteAccuracy) {
		super(relativeAccuracy, absoluteAccuracy);
	}

	/**
	 * Construct a solver with given accuracies.
	 *
	 * @param relativeAccuracy
	 * 		Maximum relative error.
	 * @param absoluteAccuracy
	 * 		Maximum absolute error.
	 * @param functionValueAccuracy
	 * 		Maximum function value error.
	 */
	protected AbstractUnivariateRealSolver(final double relativeAccuracy, final double absoluteAccuracy, final double functionValueAccuracy) {
		super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
	}
}