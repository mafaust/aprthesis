package org.apache.commons.math.optimization.general;


































/**
 * Base class for implementing least squares optimizers.
 * <p>This base class handles the boilerplate methods associated to thresholds
 * settings, jacobian and error estimation.</p>
 *
 * @version $Revision$ $Date$
 * @since 1.2
 */
public abstract class AbstractLeastSquaresOptimizer implements org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer {

	/**
	 * Default maximal number of iterations allowed.
	 */ 	public static final int DEFAULT_MAX_ITERATIONS = 100;
	/**
	 * Convergence checker.
	 */ 	protected org.apache.commons.math.optimization.VectorialConvergenceChecker checker;
	/**
	 * Jacobian matrix.
	 * <p>This matrix is in canonical form just after the calls to
	 * {@link #updateJacobian()}, but may be modified by the solver
	 * in the derived class (the {@link LevenbergMarquardtOptimizer
	 * Levenberg-Marquardt optimizer} does this).</p>
	 */
	protected double[][] jacobian;

	/**
	 * Number of columns of the jacobian matrix.
	 */ 	protected int cols;
	/**
	 * Number of rows of the jacobian matrix.
	 */ 	protected int rows;
	/**
	 * Target value for the objective functions at optimum.
	 *
	 * @since 2.1
	 */ 	protected double[] targetValues;

	/**
	 * Weight for the least squares cost computation.
	 *
	 * @since 2.1
	 */ 	protected double[] residualsWeights;

	/**
	 * Current point.
	 */ 	protected double[] point;
	/**
	 * Current objective function value.
	 */ 	protected double[] objective;
	/**
	 * Current residuals.
	 */ 	protected double[] residuals;
	/**
	 * Cost value (square root of the sum of the residuals).
	 */ 	protected double cost;
	/**
	 * Maximal number of iterations allowed.
	 */ 	private int maxIterations;
	/**
	 * Number of iterations already performed.
	 */ 	private int iterations;
	/**
	 * Maximal number of evaluations allowed.
	 */ 	private int maxEvaluations;
	/**
	 * Number of evaluations already performed.
	 */ 	private int objectiveEvaluations;
	/**
	 * Number of jacobian evaluations.
	 */ 	private int jacobianEvaluations;
	/**
	 * Objective function.
	 */ 	private org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction function;
	/**
	 * Objective function derivatives.
	 */ 	private org.apache.commons.math.analysis.MultivariateMatrixFunction jF;
	/**
	 * Simple constructor with default settings.
	 * <p>The convergence check is set to a {@link SimpleVectorialValueChecker}
	 * and the maximal number of evaluation is set to its default value.</p>
	 */ 	protected AbstractLeastSquaresOptimizer() {
		setConvergenceChecker(new org.apache.commons.math.optimization.SimpleVectorialValueChecker());
		setMaxIterations(org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS);
		setMaxEvaluations(java.lang.Integer.MAX_VALUE);
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
	 * {@inheritDoc }
	 */ 	public void setMaxEvaluations(int maxEvaluations) { 		this.maxEvaluations = maxEvaluations;
	}

	/**
	 * {@inheritDoc }
	 */ 	public int getMaxEvaluations() { 		return maxEvaluations;
	}

	/**
	 * {@inheritDoc }
	 */ 	public int getEvaluations() { 		return objectiveEvaluations;
	}

	/**
	 * {@inheritDoc }
	 */ 	public int getJacobianEvaluations() { 		return jacobianEvaluations;
	}

	/**
	 * {@inheritDoc }
	 */ 	public void setConvergenceChecker(org.apache.commons.math.optimization.VectorialConvergenceChecker convergenceChecker) { 		this.checker = convergenceChecker;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.optimization.VectorialConvergenceChecker getConvergenceChecker() { 		return checker;
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
	 * Update the jacobian matrix.
	 *
	 * @exception FunctionEvaluationException
	 * 		if the function jacobian
	 * 		cannot be evaluated or its dimension doesn't match problem dimension
	 */ 	protected void updateJacobian() throws org.apache.commons.math.FunctionEvaluationException { 		++jacobianEvaluations;
		jacobian = jF.value(point);
		if (jacobian.length != rows) {
			throw new org.apache.commons.math.FunctionEvaluationException(point, org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, 
			jacobian.length, rows);
		}
		for (int i = 0; i < rows; i++) {
			final double[] ji = jacobian[i];
			final double factor = -java.lang.Math.sqrt(residualsWeights[i]);
			for (int j = 0; j < cols; ++j) {
				ji[j] *= factor;
			}
		}
	}

	/**
	 * Update the residuals array and cost function value.
	 *
	 * @exception FunctionEvaluationException
	 * 		if the function cannot be evaluated
	 * 		or its dimension doesn't match problem dimension or maximal number of
	 * 		of evaluations is exceeded
	 */ 	protected void updateResidualsAndCost() throws org.apache.commons.math.FunctionEvaluationException {

		if ((++objectiveEvaluations) > maxEvaluations) {
			throw new org.apache.commons.math.FunctionEvaluationException(new org.apache.commons.math.MaxEvaluationsExceededException(maxEvaluations), 
			point);
		}
		objective = function.value(point);
		if (objective.length != rows) {
			throw new org.apache.commons.math.FunctionEvaluationException(point, org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, 
			objective.length, rows);
		}
		cost = 0;
		int index = 0;
		for (int i = 0; i < rows; i++) {
			final double residual = targetValues[i] - objective[i];
			residuals[i] = residual;
			cost += (residualsWeights[i] * residual) * residual;
			index += cols;
		}
		cost = java.lang.Math.sqrt(cost);

	}

	/**
	 * Get the Root Mean Square value.
	 * Get the Root Mean Square value, i.e. the root of the arithmetic
	 * mean of the square of all weighted residuals. This is related to the
	 * criterion that is minimized by the optimizer as follows: if
	 * <em>c</em> if the criterion, and <em>n</em> is the number of
	 * measurements, then the RMS is <em>sqrt (c/n)</em>.
	 *
	 * @return RMS value
	 */
	public double getRMS() {
		return java.lang.Math.sqrt(getChiSquare() / rows);
	}

	/**
	 * Get a Chi-Square-like value assuming the N residuals follow N
	 * distinct normal distributions centered on 0 and whose variances are
	 * the reciprocal of the weights.
	 *
	 * @return chi-square value
	 */ 	public double getChiSquare() {
		return cost * cost;
	}

	/**
	 * Get the covariance matrix of optimized parameters.
	 *
	 * @return covariance matrix
	 * @exception FunctionEvaluationException
	 * 		if the function jacobian cannot
	 * 		be evaluated
	 * @exception OptimizationException
	 * 		if the covariance matrix
	 * 		cannot be computed (singular problem)
	 */ 	public double[][] getCovariances() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException {
		// set up the jacobian
		updateJacobian();

		// compute transpose(J).J, avoiding building big intermediate matrices
		double[][] jTj = new double[cols][cols];
		for (int i = 0; i < cols; ++i) {
			for (int j = i; j < cols; ++j) {
				double sum = 0;
				for (int k = 0; k < rows; ++k) {
					sum += jacobian[k][i] * jacobian[k][j];
				}
				jTj[i][j] = sum;
				jTj[j][i] = sum;
			}
		}

		try {
			// compute the covariances matrix
			org.apache.commons.math.linear.RealMatrix inverse = 
			new org.apache.commons.math.linear.LUDecompositionImpl(org.apache.commons.math.linear.MatrixUtils.createRealMatrix(jTj)).getSolver().getInverse();
			return inverse.getData();
		} catch (org.apache.commons.math.linear.InvalidMatrixException ime) {
			throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.UNABLE_TO_COMPUTE_COVARIANCE_SINGULAR_PROBLEM);
		}

	}

	/**
	 * Guess the errors in optimized parameters.
	 * <p>Guessing is covariance-based, it only gives rough order of magnitude.</p>
	 *
	 * @return errors in optimized parameters
	 * @exception FunctionEvaluationException
	 * 		if the function jacobian cannot b evaluated
	 * @exception OptimizationException
	 * 		if the covariances matrix cannot be computed
	 * 		or the number of degrees of freedom is not positive (number of measurements
	 * 		lesser or equal to number of parameters)
	 */ 	public double[] guessParametersErrors() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException { 		if (rows <= cols) {
			throw new org.apache.commons.math.optimization.OptimizationException(
			org.apache.commons.math.exception.util.LocalizedFormats.NO_DEGREES_OF_FREEDOM, 
			rows, cols);
		}
		double[] errors = new double[cols];
		final double c = java.lang.Math.sqrt(getChiSquare() / (rows - cols));
		double[][] covar = getCovariances();
		for (int i = 0; i < errors.length; ++i) {
			errors[i] = java.lang.Math.sqrt(covar[i][i]) * c;
		}
		return errors;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.optimization.VectorialPointValuePair optimize(final org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction f, final double[] target, final double[] weights, final 
	double[] startPoint) throws 
	org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException {

		if (target.length != weights.length) {
			throw new org.apache.commons.math.optimization.OptimizationException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, 
			target.length, weights.length);
		}

		// reset counters
		iterations = 0;
		objectiveEvaluations = 0;
		jacobianEvaluations = 0;

		// store least squares problem characteristics
		function = f;
		jF = f.jacobian();
		targetValues = target.clone();
		residualsWeights = weights.clone();
		this.point = startPoint.clone();
		this.residuals = new double[target.length];

		// arrays shared with the other private methods
		rows = target.length;
		cols = point.length;
		jacobian = new double[rows][cols];

		cost = java.lang.Double.POSITIVE_INFINITY;

		return doOptimize();

	}

	/**
	 * Perform the bulk of optimization algorithm.
	 *
	 * @return the point/value pair giving the optimal value for objective function
	 * @exception FunctionEvaluationException
	 * 		if the objective function throws one during
	 * 		the search
	 * @exception OptimizationException
	 * 		if the algorithm failed to converge
	 * @exception IllegalArgumentException
	 * 		if the start point dimension is wrong
	 */ 	protected abstract org.apache.commons.math.optimization.VectorialPointValuePair doOptimize() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException;}