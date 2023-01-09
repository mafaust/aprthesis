package org.apache.commons.math.optimization.linear;

























/**
 * Base class for implementing linear optimizers.
 * <p>This base class handles the boilerplate methods associated to thresholds
 * settings and iterations counters.</p>
 *
 * @version $Revision$ $Date$
 * @since 2.0
 */
public abstract class AbstractLinearOptimizer implements org.apache.commons.math.optimization.linear.LinearOptimizer {

	/**
	 * Default maximal number of iterations allowed.
	 */ 	public static final int DEFAULT_MAX_ITERATIONS = 100;
	/**
	 * Maximal number of iterations allowed.
	 */ 	private int maxIterations;
	/**
	 * Number of iterations already performed.
	 */ 	private int iterations;
	/**
	 * Linear objective function.
	 */ 	protected org.apache.commons.math.optimization.linear.LinearObjectiveFunction f;
	/**
	 * Linear constraints.
	 */ 	protected java.util.Collection<org.apache.commons.math.optimization.linear.LinearConstraint> constraints;
	/**
	 * Type of optimization goal: either {@link GoalType#MAXIMIZE} or {@link GoalType#MINIMIZE}.
	 */ 	protected org.apache.commons.math.optimization.GoalType goalType;
	/**
	 * Whether to restrict the variables to non-negative values.
	 */ 	protected boolean restrictToNonNegative;
	/**
	 * Simple constructor with default settings.
	 * <p>The maximal number of evaluation is set to its default value.</p>
	 */ 	protected AbstractLinearOptimizer() {
		setMaxIterations(org.apache.commons.math.optimization.linear.AbstractLinearOptimizer.DEFAULT_MAX_ITERATIONS);
	}

	/**
	 * {@inheritDoc }
	 */ 	public void setMaxIterations(int maxIterations) { 		this.maxIterations = maxIterations;
	}

	/**
	 * {@inheritDoc }
	 */ 	public int getMaxIterations() { 		return maxIterations;
	}

	/**
	 * {@inheritDoc }
	 */ 	public int getIterations() { 		return iterations;
	}

	/**
	 * Increment the iterations counter by 1.
	 *
	 * @exception OptimizationException
	 * 		if the maximal number
	 * 		of iterations is exceeded
	 */ 	protected void incrementIterationsCounter() throws org.apache.commons.math.optimization.OptimizationException { 		if ((++iterations) > maxIterations) {
			throw new org.apache.commons.math.optimization.OptimizationException(new org.apache.commons.math.MaxIterationsExceededException(maxIterations));
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.optimization.RealPointValuePair optimize(final org.apache.commons.math.optimization.linear.LinearObjectiveFunction f, final java.util.Collection<org.apache.commons.math.optimization.linear.LinearConstraint> constraints, final 
	org.apache.commons.math.optimization.GoalType goalType, final boolean restrictToNonNegative) throws 
	org.apache.commons.math.optimization.OptimizationException {

		// store linear problem characteristics
		this.f = f;
		this.constraints = constraints;
		this.goalType = goalType;
		this.restrictToNonNegative = restrictToNonNegative;

		iterations = 0;

		// solve the problem
		return doOptimize();

	}

	/**
	 * Perform the bulk of optimization algorithm.
	 *
	 * @return the point/value pair giving the optimal value for objective function
	 * @exception OptimizationException
	 * 		if no solution fulfilling the constraints
	 * 		can be found in the allowed number of iterations
	 */ 	protected abstract org.apache.commons.math.optimization.RealPointValuePair doOptimize() throws org.apache.commons.math.optimization.OptimizationException;}