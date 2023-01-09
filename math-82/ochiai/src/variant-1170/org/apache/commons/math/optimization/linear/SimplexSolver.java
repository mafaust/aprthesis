package org.apache.commons.math.optimization.linear;























/**
 * Solves a linear problem using the Two-Phase Simplex Method.
 *
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class SimplexSolver extends org.apache.commons.math.optimization.linear.AbstractLinearOptimizer {
	/**
	 * Default amount of error to accept in floating point comparisons.
	 */ 	private static final double DEFAULT_EPSILON = 1.0E-6;
	/**
	 * Amount of error to accept in floating point comparisons.
	 */ 	protected final double epsilon;
	/**
	 * Build a simplex solver with default settings.
	 */
	public SimplexSolver() {
		this(org.apache.commons.math.optimization.linear.SimplexSolver.DEFAULT_EPSILON);
	}

	/**
	 * Build a simplex solver with a specified accepted amount of error
	 *
	 * @param epsilon
	 * 		the amount of error to accept in floating point comparisons
	 */ 	public SimplexSolver(final double epsilon) { 		this.epsilon = epsilon;
	}

	/**
	 * Returns the column with the most negative coefficient in the objective function row.
	 *
	 * @param tableau
	 * 		simple tableau for the problem
	 * @return column with the most negative coefficient
	 */ 	private java.lang.Integer getPivotColumn(org.apache.commons.math.optimization.linear.SimplexTableau tableau) { 		double minValue = 0;
		java.lang.Integer minPos = null;
		for (int i = tableau.getNumObjectiveFunctions(); i < (tableau.getWidth() - 1); i++) {
			if (org.apache.commons.math.util.MathUtils.compareTo(tableau.getEntry(0, i), minValue, epsilon) < 0)

				minPos = i;

		}
		return minPos;
	}

	/**
	 * Returns the row with the minimum ratio as given by the minimum ratio test (MRT).
	 *
	 * @param tableau
	 * 		simple tableau for the problem
	 * @param col
	 * 		the column to test the ratio of.  See {@link #getPivotColumn(SimplexTableau)}
	 * @return row with the minimum ratio
	 */ 	private java.lang.Integer getPivotRow(final int col, final org.apache.commons.math.optimization.linear.SimplexTableau tableau) { 		double minRatio = java.lang.Double.MAX_VALUE; 		java.lang.Integer minRatioPos = null;
		for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
			final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
			final double entry = tableau.getEntry(i, col);
			if (org.apache.commons.math.util.MathUtils.compareTo(entry, 0, epsilon) >= 0) {
				final double ratio = rhs / entry;
				if (ratio < minRatio) {
					minRatio = ratio;
					minRatioPos = i;
				}
			}
		}
		return minRatioPos;
	}


	/**
	 * Runs one iteration of the Simplex method on the given model.
	 *
	 * @param tableau
	 * 		simple tableau for the problem
	 * @throws OptimizationException
	 * 		if the maximal iteration count has been
	 * 		exceeded or if the model is found not to have a bounded solution
	 */ 	protected void doIteration(final org.apache.commons.math.optimization.linear.SimplexTableau tableau) throws org.apache.commons.math.optimization.OptimizationException {
		incrementIterationsCounter();

		java.lang.Integer pivotCol = getPivotColumn(tableau);
		java.lang.Integer pivotRow = getPivotRow(pivotCol, tableau);
		if (pivotRow == null) {
			throw new org.apache.commons.math.optimization.linear.UnboundedSolutionException();
		}

		// set the pivot element to 1
		double pivotVal = tableau.getEntry(pivotRow, pivotCol);
		tableau.divideRow(pivotRow, pivotVal);

		// set the rest of the pivot column to 0
		for (int i = 0; i < tableau.getHeight(); i++) {
			if (i != pivotRow) {
				double multiplier = tableau.getEntry(i, pivotCol);
				tableau.subtractRow(i, pivotRow, multiplier);
			}
		}
	}

	/**
	 * Checks whether Phase 1 is solved.
	 *
	 * @param tableau
	 * 		simple tableau for the problem
	 * @return whether Phase 1 is solved
	 */ 	private boolean isPhase1Solved(final org.apache.commons.math.optimization.linear.SimplexTableau tableau) { 		if (tableau.getNumArtificialVariables() == 0) {
			return true;
		}
		for (int i = tableau.getNumObjectiveFunctions(); i < (tableau.getWidth() - 1); i++) {
			if (org.apache.commons.math.util.MathUtils.compareTo(tableau.getEntry(0, i), 0, epsilon) < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns whether the problem is at an optimal state.
	 *
	 * @param tableau
	 * 		simple tableau for the problem
	 * @return whether the model has been solved
	 */ 	public boolean isOptimal(final org.apache.commons.math.optimization.linear.SimplexTableau tableau) { 		if (tableau.getNumArtificialVariables() > 0) {
			return false;
		}
		for (int i = tableau.getNumObjectiveFunctions(); i < (tableau.getWidth() - 1); i++) {
			if (org.apache.commons.math.util.MathUtils.compareTo(tableau.getEntry(0, i), 0, epsilon) < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Solves Phase 1 of the Simplex method.
	 *
	 * @param tableau
	 * 		simple tableau for the problem
	 * @exception OptimizationException
	 * 		if the maximal number of iterations is
	 * 		exceeded, or if the problem is found not to have a bounded solution, or
	 * 		if there is no feasible solution
	 */ 	protected void solvePhase1(final org.apache.commons.math.optimization.linear.SimplexTableau tableau) throws org.apache.commons.math.optimization.OptimizationException { 		// make sure we're in Phase 1
		if (tableau.getNumArtificialVariables() == 0) {
			return;
		}

		while (!isPhase1Solved(tableau)) {
			doIteration(tableau);
		} 

		// if W is not zero then we have no feasible solution
		if (!org.apache.commons.math.util.MathUtils.equals(tableau.getEntry(0, tableau.getRhsOffset()), 0, epsilon)) {
			throw new org.apache.commons.math.optimization.linear.NoFeasibleSolutionException();
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	@java.lang.Override 	public org.apache.commons.math.optimization.RealPointValuePair doOptimize() throws 
	org.apache.commons.math.optimization.OptimizationException {
		final org.apache.commons.math.optimization.linear.SimplexTableau tableau = 
		new org.apache.commons.math.optimization.linear.SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
		solvePhase1(tableau);
		tableau.discardArtificialVariables();
		while (!isOptimal(tableau)) {
			doIteration(tableau);
		} 
		return tableau.getSolution();
	}}