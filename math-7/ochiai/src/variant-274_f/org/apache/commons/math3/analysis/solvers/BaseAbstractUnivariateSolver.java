package org.apache.commons.math3.analysis.solvers;
/**
 * Provide a default implementation for several functions useful to generic
 * solvers.
 *
 * @param <FUNC>
 * 		Type of function to solve.
 * @since 2.0
 * @version $Id$
 */
public abstract class BaseAbstractUnivariateSolver<FUNC extends org.apache.commons.math3.analysis.UnivariateFunction> implements org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver<FUNC> {
	/**
	 * Default relative accuracy.
	 */
	private static final double DEFAULT_RELATIVE_ACCURACY = 1.0E-14;

	/**
	 * Default function value accuracy.
	 */
	private static final double DEFAULT_FUNCTION_VALUE_ACCURACY = 1.0E-15;

	/**
	 * Function value accuracy.
	 */
	private final double functionValueAccuracy;

	/**
	 * Absolute accuracy.
	 */
	private final double absoluteAccuracy;

	/**
	 * Relative accuracy.
	 */
	private final double relativeAccuracy;

	/**
	 * Evaluations counter.
	 */
	private final org.apache.commons.math3.util.Incrementor evaluations = new org.apache.commons.math3.util.Incrementor();

	/**
	 * Lower end of search interval.
	 */
	private double searchMin;

	/**
	 * Higher end of search interval.
	 */
	private double searchMax;

	/**
	 * Initial guess.
	 */
	private double searchStart;

	/**
	 * Function to solve.
	 */
	private FUNC function;

	/**
	 * Construct a solver with given absolute accuracy.
	 *
	 * @param absoluteAccuracy
	 * 		Maximum absolute error.
	 */
	protected BaseAbstractUnivariateSolver(final double absoluteAccuracy) {
		this(org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver.DEFAULT_RELATIVE_ACCURACY, absoluteAccuracy, org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver.DEFAULT_FUNCTION_VALUE_ACCURACY);
	}

	/**
	 * Construct a solver with given accuracies.
	 *
	 * @param relativeAccuracy
	 * 		Maximum relative error.
	 * @param absoluteAccuracy
	 * 		Maximum absolute error.
	 */
	protected BaseAbstractUnivariateSolver(final double relativeAccuracy, final double absoluteAccuracy) {
		this(relativeAccuracy, absoluteAccuracy, org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver.DEFAULT_FUNCTION_VALUE_ACCURACY);
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
	protected BaseAbstractUnivariateSolver(final double relativeAccuracy, final double absoluteAccuracy, final double functionValueAccuracy) {
		this.absoluteAccuracy = absoluteAccuracy;
		this.relativeAccuracy = relativeAccuracy;
		this.functionValueAccuracy = functionValueAccuracy;
	}

	/**
	 * {@inheritDoc }
	 */
	public int getMaxEvaluations() {
		return evaluations.getMaximalCount();
	}

	/**
	 * {@inheritDoc }
	 */
	public int getEvaluations() {
		return evaluations.getCount();
	}

	/**
	 *
	 *
	 * @return the lower end of the search interval.
	 */
	public double getMin() {
		return searchMin;
	}

	/**
	 *
	 *
	 * @return the higher end of the search interval.
	 */
	public double getMax() {
		return searchMax;
	}

	/**
	 *
	 *
	 * @return the initial guess.
	 */
	public double getStartValue() {
		return searchStart;
	}

	/**
	 * {@inheritDoc }
	 */
	public double getAbsoluteAccuracy() {
		return absoluteAccuracy;
	}

	/**
	 * {@inheritDoc }
	 */
	public double getRelativeAccuracy() {
		return relativeAccuracy;
	}

	/**
	 * {@inheritDoc }
	 */
	public double getFunctionValueAccuracy() {
		return functionValueAccuracy;
	}

	/**
	 * Compute the objective function value.
	 *
	 * @param point
	 * 		Point at which the objective function must be evaluated.
	 * @return the objective function value at specified point.
	 * @throws TooManyEvaluationsException
	 * 		if the maximal number of evaluations
	 * 		is exceeded.
	 */
	protected double computeObjectiveValue(double point) throws org.apache.commons.math3.exception.TooManyEvaluationsException {
		incrementEvaluationCount();
		return function.value(point);
	}

	/**
	 * Prepare for computation.
	 * Subclasses must call this method if they override any of the
	 * {@code solve} methods.
	 *
	 * @param f
	 * 		Function to solve.
	 * @param min
	 * 		Lower bound for the interval.
	 * @param max
	 * 		Upper bound for the interval.
	 * @param startValue
	 * 		Start value to use.
	 * @param maxEval
	 * 		Maximum number of evaluations.
	 * @exception NullArgumentException
	 * 		if f is null
	 */
	protected void setup(int maxEval, FUNC f, double min, double max, double startValue) throws org.apache.commons.math3.exception.NullArgumentException {
		// Checks.
		org.apache.commons.math3.util.MathUtils.checkNotNull(f);
		// Reset.
		searchMin = min;
		searchMax = max;
		searchStart = startValue;
		function = f;
		evaluations.setMaximalCount(maxEval);
		evaluations.resetCount();
	}

	/**
	 * {@inheritDoc }
	 */
	public double solve(int maxEval, FUNC f, double min, double max, double startValue) throws org.apache.commons.math3.exception.TooManyEvaluationsException, org.apache.commons.math3.exception.NoBracketingException {
		// Initialization.
		setup(maxEval, f, min, max, startValue);
		// Perform computation.
		return doSolve();
	}

	/**
	 * {@inheritDoc }
	 */
	public double solve(int maxEval, FUNC f, double min, double max) {
		return solve(maxEval, f, min, max, min + (0.5 * (max - min)));
	}

	/**
	 * {@inheritDoc }
	 */
	public double solve(int maxEval, FUNC f, double startValue) throws org.apache.commons.math3.exception.TooManyEvaluationsException, org.apache.commons.math3.exception.NoBracketingException {
		return solve(maxEval, f, java.lang.Double.NaN, java.lang.Double.NaN, startValue);
	}

	/**
	 * Method for implementing actual optimization algorithms in derived
	 * classes.
	 *
	 * @return the root.
	 * @throws TooManyEvaluationsException
	 * 		if the maximal number of evaluations
	 * 		is exceeded.
	 * @throws NoBracketingException
	 * 		if the initial search interval does not bracket
	 * 		a root and the solver requires it.
	 */
	protected abstract double doSolve() throws org.apache.commons.math3.exception.TooManyEvaluationsException, org.apache.commons.math3.exception.NoBracketingException;

	/**
	 * Check whether the function takes opposite signs at the endpoints.
	 *
	 * @param lower
	 * 		Lower endpoint.
	 * @param upper
	 * 		Upper endpoint.
	 * @return {@code true} if the function values have opposite signs at the
	given points.
	 */
	protected boolean isBracketing(final double lower, final double upper) {
		return org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils.isBracketing(function, lower, upper);
	}

	/**
	 * Check whether the arguments form a (strictly) increasing sequence.
	 *
	 * @param start
	 * 		First number.
	 * @param mid
	 * 		Second number.
	 * @param end
	 * 		Third number.
	 * @return {@code true} if the arguments form an increasing sequence.
	 */
	protected boolean isSequence(final double start, final double mid, final double end) {
		return org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils.isSequence(start, mid, end);
	}

	/**
	 * Check that the endpoints specify an interval.
	 *
	 * @param lower
	 * 		Lower endpoint.
	 * @param upper
	 * 		Upper endpoint.
	 * @throws NumberIsTooLargeException
	 * 		if {@code lower >= upper}.
	 */
	protected void verifyInterval(final double lower, final double upper) throws org.apache.commons.math3.exception.NumberIsTooLargeException {
		org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils.verifyInterval(lower, upper);
	}

	/**
	 * Check that {@code lower < initial < upper}.
	 *
	 * @param lower
	 * 		Lower endpoint.
	 * @param initial
	 * 		Initial value.
	 * @param upper
	 * 		Upper endpoint.
	 * @throws NumberIsTooLargeException
	 * 		if {@code lower >= initial} or
	 * 		{@code initial >= upper}.
	 */
	protected void verifySequence(final double lower, final double initial, final double upper) throws org.apache.commons.math3.exception.NumberIsTooLargeException {
		org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils.verifySequence(lower, initial, upper);
	}

	/**
	 * Check that the endpoints specify an interval and the function takes
	 * opposite signs at the endpoints.
	 *
	 * @param lower
	 * 		Lower endpoint.
	 * @param upper
	 * 		Upper endpoint.
	 * @throws NullArgumentException
	 * 		if the function has not been set.
	 * @throws NoBracketingException
	 * 		if the function has the same sign at
	 * 		the endpoints.
	 */
	protected void verifyBracketing(final double lower, final double upper) throws org.apache.commons.math3.exception.NullArgumentException, org.apache.commons.math3.exception.NoBracketingException {
		org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils.verifyBracketing(function, lower, upper);
	}

	/**
	 * Increment the evaluation count by one.
	 * Method {@link #computeObjectiveValue(double)} calls this method internally.
	 * It is provided for subclasses that do not exclusively use
	 * {@code computeObjectiveValue} to solve the function.
	 * See e.g. {@link AbstractUnivariateDifferentiableSolver}.
	 *
	 * @throws TooManyEvaluationsException
	 * 		when the allowed number of function
	 * 		evaluations has been exhausted.
	 */
	protected void incrementEvaluationCount() throws org.apache.commons.math3.exception.TooManyEvaluationsException {
		try {
			evaluations.incrementCount();
		} catch (org.apache.commons.math3.exception.MaxCountExceededException e) {
			throw new org.apache.commons.math3.exception.TooManyEvaluationsException(e.getMax());
		}
	}
}