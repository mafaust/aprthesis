package org.apache.commons.math3.ode.nonstiff;



























/**
 * This abstract class holds the common part of all adaptive
 * stepsize integrators for Ordinary Differential Equations.
 *
 * <p>These algorithms perform integration with stepsize control, which
 * means the user does not specify the integration step but rather a
 * tolerance on error. The error threshold is computed as
 * <pre>
 * threshold_i = absTol_i + relTol_i * max (abs (ym), abs (ym+1))
 * </pre>
 * where absTol_i is the absolute tolerance for component i of the
 * state vector and relTol_i is the relative tolerance for the same
 * component. The user can also use only two scalar values absTol and
 * relTol which will be used for all components.
 * </p>
 * <p>
 * If the Ordinary Differential Equations is an {@link ExpandableStatefulODE
 * extended ODE} rather than a {@link org.apache.commons.math3.ode.FirstOrderDifferentialEquations basic ODE}, then
 * <em>only</em> the {@link ExpandableStatefulODE#getPrimaryState() primary part}
 * of the state vector is used for stepsize control, not the complete state vector.
 * </p>
 *
 * <p>If the estimated error for ym+1 is such that
 * <pre>
 * sqrt((sum (errEst_i / threshold_i)^2 ) / n) < 1
 * </pre>
 *
 * (where n is the main set dimension) then the step is accepted,
 * otherwise the step is rejected and a new attempt is made with a new
 * stepsize.</p>
 *
 * @version $Id$
 * @since 1.2
 */
public abstract class AdaptiveStepsizeIntegrator extends 



org.apache.commons.math3.ode.AbstractIntegrator {

	/**
	 * Allowed absolute scalar error.
	 */ 	protected double scalAbsoluteTolerance;
	/**
	 * Allowed relative scalar error.
	 */ 	protected double scalRelativeTolerance;
	/**
	 * Allowed absolute vectorial error.
	 */ 	protected double[] vecAbsoluteTolerance;
	/**
	 * Allowed relative vectorial error.
	 */ 	protected double[] vecRelativeTolerance;
	/**
	 * Main set dimension.
	 */ 	protected int mainSetDimension;
	/**
	 * User supplied initial step.
	 */ 	private double initialStep;
	/**
	 * Minimal step.
	 */ 	private double minStep;
	/**
	 * Maximal step.
	 */ 	private double maxStep;
	/**
	 * Build an integrator with the given stepsize bounds.
	 * The default step handler does nothing.
	 *
	 * @param name
	 * 		name of the method
	 * @param minStep
	 * 		minimal step (sign is irrelevant, regardless of
	 * 		integration direction, forward or backward), the last step can
	 * 		be smaller than this
	 * @param maxStep
	 * 		maximal step (sign is irrelevant, regardless of
	 * 		integration direction, forward or backward), the last step can
	 * 		be smaller than this
	 * @param scalAbsoluteTolerance
	 * 		allowed absolute error
	 * @param scalRelativeTolerance
	 * 		allowed relative error
	 */ 	public AdaptiveStepsizeIntegrator(final java.lang.String name, final double minStep, final double maxStep, final double scalAbsoluteTolerance, final double scalRelativeTolerance) { 		super(name); 		setStepSizeControl(minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
		resetInternalState();

	}

	/**
	 * Build an integrator with the given stepsize bounds.
	 * The default step handler does nothing.
	 *
	 * @param name
	 * 		name of the method
	 * @param minStep
	 * 		minimal step (sign is irrelevant, regardless of
	 * 		integration direction, forward or backward), the last step can
	 * 		be smaller than this
	 * @param maxStep
	 * 		maximal step (sign is irrelevant, regardless of
	 * 		integration direction, forward or backward), the last step can
	 * 		be smaller than this
	 * @param vecAbsoluteTolerance
	 * 		allowed absolute error
	 * @param vecRelativeTolerance
	 * 		allowed relative error
	 */ 	public AdaptiveStepsizeIntegrator(final java.lang.String name, final double minStep, final double maxStep, final double[] vecAbsoluteTolerance, final double[] vecRelativeTolerance) { 		super(name); 		setStepSizeControl(minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
		resetInternalState();

	}

	/**
	 * Set the adaptive step size control parameters.
	 * <p>
	 * A side effect of this method is to also reset the initial
	 * step so it will be automatically computed by the integrator
	 * if {@link #setInitialStepSize(double) setInitialStepSize}
	 * is not called by the user.
	 * </p>
	 *
	 * @param minimalStep
	 * 		minimal step (must be positive even for backward
	 * 		integration), the last step can be smaller than this
	 * @param maximalStep
	 * 		maximal step (must be positive even for backward
	 * 		integration)
	 * @param absoluteTolerance
	 * 		allowed absolute error
	 * @param relativeTolerance
	 * 		allowed relative error
	 */ 	public void setStepSizeControl(final double minimalStep, final double maximalStep, final double absoluteTolerance, final double relativeTolerance) { 		minStep = org.apache.commons.math3.util.FastMath.abs(minimalStep); 		maxStep = org.apache.commons.math3.util.FastMath.abs(maximalStep);
		initialStep = -1;

		scalAbsoluteTolerance = absoluteTolerance;
		scalRelativeTolerance = relativeTolerance;
		vecAbsoluteTolerance = null;
		vecRelativeTolerance = null;

	}

	/**
	 * Set the adaptive step size control parameters.
	 * <p>
	 * A side effect of this method is to also reset the initial
	 * step so it will be automatically computed by the integrator
	 * if {@link #setInitialStepSize(double) setInitialStepSize}
	 * is not called by the user.
	 * </p>
	 *
	 * @param minimalStep
	 * 		minimal step (must be positive even for backward
	 * 		integration), the last step can be smaller than this
	 * @param maximalStep
	 * 		maximal step (must be positive even for backward
	 * 		integration)
	 * @param absoluteTolerance
	 * 		allowed absolute error
	 * @param relativeTolerance
	 * 		allowed relative error
	 */ 	public void setStepSizeControl(final double minimalStep, final double maximalStep, final double[] absoluteTolerance, final double[] relativeTolerance) { 		minStep = org.apache.commons.math3.util.FastMath.abs(minimalStep); 		maxStep = org.apache.commons.math3.util.FastMath.abs(maximalStep);
		initialStep = -1;

		scalAbsoluteTolerance = 0;
		scalRelativeTolerance = 0;
		vecAbsoluteTolerance = absoluteTolerance.clone();
		vecRelativeTolerance = relativeTolerance.clone();

	}

	/**
	 * Set the initial step size.
	 * <p>This method allows the user to specify an initial positive
	 * step size instead of letting the integrator guess it by
	 * itself. If this method is not called before integration is
	 * started, the initial step size will be estimated by the
	 * integrator.</p>
	 *
	 * @param initialStepSize
	 * 		initial step size to use (must be positive even
	 * 		for backward integration ; providing a negative value or a value
	 * 		outside of the min/max step interval will lead the integrator to
	 * 		ignore the value and compute the initial step size by itself)
	 */ 	public void setInitialStepSize(final double initialStepSize) { 		if ((initialStepSize < minStep) || (initialStepSize > maxStep)) { 			initialStep = -1.0;
		} else {
			initialStep = initialStepSize;
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	@java.lang.Override 	protected void sanityChecks(final org.apache.commons.math3.ode.ExpandableStatefulODE equations, final double t) throws 
	org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.NumberIsTooSmallException {

		super.sanityChecks(equations, t);

		mainSetDimension = equations.getPrimaryMapper().getDimension();

		if ((vecAbsoluteTolerance != null) && (vecAbsoluteTolerance.length != mainSetDimension)) {
			throw new org.apache.commons.math3.exception.DimensionMismatchException(mainSetDimension, vecAbsoluteTolerance.length);
		}

		if ((vecRelativeTolerance != null) && (vecRelativeTolerance.length != mainSetDimension)) {
			throw new org.apache.commons.math3.exception.DimensionMismatchException(mainSetDimension, vecRelativeTolerance.length);
		}

	}

	/**
	 * Initialize the integration step.
	 *
	 * @param forward
	 * 		forward integration indicator
	 * @param order
	 * 		order of the method
	 * @param scale
	 * 		scaling vector for the state vector (can be shorter than state vector)
	 * @param t0
	 * 		start time
	 * @param y0
	 * 		state vector at t0
	 * @param yDot0
	 * 		first time derivative of y0
	 * @param y1
	 * 		work array for a state vector
	 * @param yDot1
	 * 		work array for the first time derivative of y1
	 * @return first integration step
	 * @exception MaxCountExceededException
	 * 		if the number of functions evaluations is exceeded
	 * @exception DimensionMismatchException
	 * 		if arrays dimensions do not match equations settings
	 */ 	public double initializeStep(final boolean forward, final int order, final double[] scale, final double t0, final double[] y0, final double[] yDot0, final double[] y1, final double[] yDot1) throws org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.DimensionMismatchException { 		if (initialStep > 0) { 			// use the user provided value
			return forward ? initialStep : -initialStep;} 		// very rough first guess : h = 0.01 * ||y/scale|| / ||y'/scale|| 		// this guess will be used to perform an Euler step 		double ratio;
		double yOnScale2 = 0;
		double yDotOnScale2 = 0;
		for (int j = 0; j < scale.length; ++j) {
			ratio = y0[j] / scale[j];
			yOnScale2 += ratio * ratio;
			ratio = yDot0[j] / scale[j];
			yDotOnScale2 += ratio * ratio;
		}

		double h = ((yOnScale2 < 1.0E-10) || (yDotOnScale2 < 1.0E-10)) ? 
		1.0E-6 : 0.01 * org.apache.commons.math3.util.FastMath.sqrt(yOnScale2 / yDotOnScale2);
		if (!forward) {
			h = -h;
		}

		// perform an Euler step using the preceding rough guess
		for (int j = 0; j < y0.length; ++j) {
			y1[j] = y0[j] + (h * yDot0[j]);
		}
		computeDerivatives(t0 + h, y1, yDot1);

		// estimate the second derivative of the solution
		double yDDotOnScale = 0;
		for (int j = 0; j < scale.length; ++j) {
			ratio = (yDot1[j] - yDot0[j]) / scale[j];
			yDDotOnScale += ratio * ratio;
		}
		yDDotOnScale = org.apache.commons.math3.util.FastMath.sqrt(yDDotOnScale) / h;

		// step size is computed such that
		// h^order * max (||y'/tol||, ||y''/tol||) = 0.01
		final double maxInv2 = org.apache.commons.math3.util.FastMath.max(org.apache.commons.math3.util.FastMath.sqrt(yDotOnScale2), yDDotOnScale);
		final double h1 = (maxInv2 < 1.0E-15) ? 
		org.apache.commons.math3.util.FastMath.max(1.0E-6, 0.001 * org.apache.commons.math3.util.FastMath.abs(h)) : 
		org.apache.commons.math3.util.FastMath.pow(0.01 / maxInv2, 1.0 / order);
		h = org.apache.commons.math3.util.FastMath.min(100.0 * org.apache.commons.math3.util.FastMath.abs(h), h1);
		h = org.apache.commons.math3.util.FastMath.max(h, 1.0E-12 * org.apache.commons.math3.util.FastMath.abs(t0));// avoids cancellation when computing t1 - t0
		if (h < getMinStep()) {
			h = getMinStep();
		}
		if (h > getMaxStep()) {
			h = getMaxStep();
		}
		if (!forward) {
			h = -h;
		}

		return h;

	}

	/**
	 * Filter the integration step.
	 *
	 * @param h
	 * 		signed step
	 * @param forward
	 * 		forward integration indicator
	 * @param acceptSmall
	 * 		if true, steps smaller than the minimal value
	 * 		are silently increased up to this value, if false such small
	 * 		steps generate an exception
	 * @return a bounded integration step (h if no bound is reach, or a bounded value)
	 * @exception NumberIsTooSmallException
	 * 		if the step is too small and acceptSmall is false
	 */ 	protected double filterStep(final double h, final boolean forward, final boolean acceptSmall) throws org.apache.commons.math3.exception.NumberIsTooSmallException { 		double filteredH = h; 		if (org.apache.commons.math3.util.FastMath.abs(h) < minStep) { 			if (acceptSmall) {
				filteredH = (forward) ? minStep : -minStep;
			} else {
				throw new org.apache.commons.math3.exception.NumberIsTooSmallException(org.apache.commons.math3.exception.util.LocalizedFormats.MINIMAL_STEPSIZE_REACHED_DURING_INTEGRATION, 
				org.apache.commons.math3.util.FastMath.abs(h), minStep, true);
			}
		}

		if (filteredH > maxStep) {
			filteredH = maxStep;
		} else if (filteredH < (-maxStep)) {
			filteredH = -maxStep;
		}

		return filteredH;

	}

	/**
	 * {@inheritDoc }
	 */ 	@java.lang.Override 	public abstract void integrate(org.apache.commons.math3.ode.ExpandableStatefulODE equations, double t) throws 
	org.apache.commons.math3.exception.NumberIsTooSmallException, org.apache.commons.math3.exception.DimensionMismatchException, 
	org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.NoBracketingException;

	/**
	 * {@inheritDoc }
	 */ 	@java.lang.Override 	public double getCurrentStepStart() {
		return stepStart;
	}

	/**
	 * Reset internal state to dummy values.
	 */ 	protected void resetInternalState() { 		stepStart = java.lang.Double.NaN;
		stepSize = org.apache.commons.math3.util.FastMath.sqrt(minStep * maxStep);
	}

	/**
	 * Get the minimal step.
	 *
	 * @return minimal step
	 */ 	public double getMinStep() { 		return minStep;
	}

	/**
	 * Get the maximal step.
	 *
	 * @return maximal step
	 */ 	public double getMaxStep() { 		return maxStep;
	}}