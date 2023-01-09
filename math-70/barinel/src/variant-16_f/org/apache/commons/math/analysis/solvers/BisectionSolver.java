package org.apache.commons.math.analysis.solvers;
/**
 * Implements the <a href="http://mathworld.wolfram.com/Bisection.html">
 * bisection algorithm</a> for finding zeros of univariate real functions.
 * <p>
 * The function should be continuous but not necessarily smooth.</p>
 *
 * @version $Revision$ $Date$
 */
public class BisectionSolver extends org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl {
	/**
	 * Construct a solver for the given function.
	 *
	 * @param f
	 * 		function to solve.
	 * @deprecated as of 2.0 the function to solve is passed as an argument
	to the {@link #solve(UnivariateRealFunction, double, double)} or
	{@link UnivariateRealSolverImpl#solve(UnivariateRealFunction, double, double, double)}
	method.
	 */
	@java.lang.Deprecated
	public BisectionSolver(org.apache.commons.math.analysis.UnivariateRealFunction f) {
		super(f, 100, 1.0E-6);
	}

	/**
	 * Construct a solver.
	 */
	public BisectionSolver() {
		super(100, 1.0E-6);
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Deprecated
	public double solve(double min, double max, double initial) throws org.apache.commons.math.MaxIterationsExceededException, org.apache.commons.math.FunctionEvaluationException {
		return solve(f, min, max);
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Deprecated
	public double solve(double min, double max) throws org.apache.commons.math.MaxIterationsExceededException, org.apache.commons.math.FunctionEvaluationException {
		return solve(f, min, max);
	}

	/**
	 * {@inheritDoc }
	 */
	public double solve(final org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double initial) throws org.apache.commons.math.MaxIterationsExceededException, org.apache.commons.math.FunctionEvaluationException {
		return solve(f, min, max);
	}

	/**
	 * {@inheritDoc }
	 */
	public double solve(final org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.MaxIterationsExceededException, org.apache.commons.math.FunctionEvaluationException {
		clearResult();
		verifyInterval(min, max);
		double m;
		double fm;
		double fmin;
		int i = 0;
		while (i < maximalIterationCount) {
			m = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.midpoint(min, max);
			fmin = f.value(min);
			fm = f.value(m);
			if ((fm * fmin) > 0.0) {
				// max and m bracket the root.
				min = m;
			} else {
				// min and m bracket the root.
				max = m;
			}
			if (java.lang.Math.abs(max - min) <= absoluteAccuracy) {
				m = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.midpoint(min, max);
				setResult(m, i);
				return m;
			}
			++i;
		} 
		throw new org.apache.commons.math.MaxIterationsExceededException(maximalIterationCount);
	}
}