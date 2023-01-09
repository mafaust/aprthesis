package org.apache.commons.math.distribution;
/**
 * Base class for continuous distributions.  Default implementations are
 * provided for some of the methods that do not vary from distribution to
 * distribution.
 *
 * @version $Revision$ $Date$
 */
public abstract class AbstractContinuousDistribution extends org.apache.commons.math.distribution.AbstractDistribution implements org.apache.commons.math.distribution.ContinuousDistribution , java.io.Serializable {
	/**
	 * Serializable version identifier
	 */
	private static final long serialVersionUID = -38038050983108802L;

	/**
	 * Solver absolute accuracy for inverse cum computation
	 *
	 * @since 2.1
	 */
	private double solverAbsoluteAccuracy = org.apache.commons.math.analysis.solvers.BrentSolver.DEFAULT_ABSOLUTE_ACCURACY;

	/**
	 * Default constructor.
	 */
	protected AbstractContinuousDistribution() {
		super();
	}

	/**
	 * Return the probability density for a particular point.
	 *
	 * @param x
	 * 		The point at which the density should be computed.
	 * @return The pdf at point x.
	 * @throws MathRuntimeException
	 * 		if the specialized class hasn't implemented this function
	 * @since 2.1
	 */
	public double density(double x) throws org.apache.commons.math.MathRuntimeException {
		throw new org.apache.commons.math.MathRuntimeException(new java.lang.UnsupportedOperationException(), "This distribution does not have a density function implemented");
	}

	/**
	 * For this distribution, X, this method returns the critical point x, such
	 * that P(X &lt; x) = <code>p</code>.
	 *
	 * @param p
	 * 		the desired probability
	 * @return x, such that P(X &lt; x) = <code>p</code>
	 * @throws MathException
	 * 		if the inverse cumulative probability can not be
	 * 		computed due to convergence or other numerical errors.
	 * @throws IllegalArgumentException
	 * 		if <code>p</code> is not a valid
	 * 		probability.
	 */
	public double inverseCumulativeProbability(final double p) throws org.apache.commons.math.MathException {
		if ((p < 0.0) || (p > 1.0)) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("{0} out of [{1}, {2}] range", p, 0.0, 1.0);
		}
		// by default, do simple root finding using bracketing and default solver.
		// subclasses can override if there is a better method.
		org.apache.commons.math.analysis.UnivariateRealFunction rootFindingFunction = new org.apache.commons.math.analysis.UnivariateRealFunction() {
			public double value(double x) throws org.apache.commons.math.FunctionEvaluationException {
				double ret = java.lang.Double.NaN;
				try {
					ret = cumulativeProbability(x) - p;
				} catch (org.apache.commons.math.MathException ex) {
					throw new org.apache.commons.math.FunctionEvaluationException(ex, x, ex.getPattern(), ex.getArguments());
				}
				if (java.lang.Double.isNaN(ret)) {
					throw new org.apache.commons.math.FunctionEvaluationException(x, "Cumulative probability function returned NaN for argument {0} p = {1}", x, p);
				}
				return ret;
			}
		};
		// Try to bracket root, test domain endoints if this fails
		double lowerBound = getDomainLowerBound(p);
		double upperBound = getDomainUpperBound(p);
		double[] bracket = null;
		try {
			bracket = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.bracket(rootFindingFunction, getInitialDomain(p), lowerBound, upperBound);
		} catch (org.apache.commons.math.ConvergenceException ex) {
			/* Check domain endpoints to see if one gives value that is within
			the default solver's defaultAbsoluteAccuracy of 0 (will be the
			case if density has bounded support and p is 0 or 1).
			 */
			if (java.lang.Math.abs(rootFindingFunction.value(lowerBound)) < getSolverAbsoluteAccuracy()) {
				return lowerBound;
			}
			if (java.lang.Math.abs(rootFindingFunction.value(upperBound)) < getSolverAbsoluteAccuracy()) {
				return upperBound;
			}
			// Failed bracket convergence was not because of corner solution
			throw new org.apache.commons.math.MathException(ex);
		}
		// find root
		double root = // override getSolverAbsoluteAccuracy() to use a Brent solver with
		// absolute accuracy different from BrentSolver default
		org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.solve(rootFindingFunction, bracket[0], bracket[1], getSolverAbsoluteAccuracy());
		return root;
	}

	/**
	 * Access the initial domain value, based on <code>p</code>, used to
	 * bracket a CDF root.  This method is used by
	 * {@link #inverseCumulativeProbability(double)} to find critical values.
	 *
	 * @param p
	 * 		the desired probability for the critical value
	 * @return initial domain value
	 */
	protected abstract double getInitialDomain(double p);

	/**
	 * Access the domain value lower bound, based on <code>p</code>, used to
	 * bracket a CDF root.  This method is used by
	 * {@link #inverseCumulativeProbability(double)} to find critical values.
	 *
	 * @param p
	 * 		the desired probability for the critical value
	 * @return domain value lower bound, i.e.
	P(X &lt; <i>lower bound</i>) &lt; <code>p</code>
	 */
	protected abstract double getDomainLowerBound(double p);

	/**
	 * Access the domain value upper bound, based on <code>p</code>, used to
	 * bracket a CDF root.  This method is used by
	 * {@link #inverseCumulativeProbability(double)} to find critical values.
	 *
	 * @param p
	 * 		the desired probability for the critical value
	 * @return domain value upper bound, i.e.
	P(X &lt; <i>upper bound</i>) &gt; <code>p</code>
	 */
	protected abstract double getDomainUpperBound(double p);

	/**
	 * Returns the solver absolute accuracy for inverse cum computation.
	 *
	 * @return the maximum absolute error in inverse cumulative probability estimates
	 * @since 2.1
	 */
	protected double getSolverAbsoluteAccuracy() {
		return solverAbsoluteAccuracy;
	}
}