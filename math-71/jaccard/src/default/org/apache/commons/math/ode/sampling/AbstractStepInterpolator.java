package org.apache.commons.math.ode.sampling;

























/**
 * This abstract class represents an interpolator over the last step
 * during an ODE integration.
 *
 * <p>The various ODE integrators provide objects extending this class
 * to the step handlers. The handlers can use these objects to
 * retrieve the state vector at intermediate times between the
 * previous and the current grid points (dense output).</p>
 *
 * @see org.apache.commons.math.ode.FirstOrderIntegrator
 * @see org.apache.commons.math.ode.SecondOrderIntegrator
 * @see StepHandler
 * @version $Revision$ $Date$
 * @since 1.2
 */
public abstract class AbstractStepInterpolator implements 


org.apache.commons.math.ode.sampling.StepInterpolator {

	/**
	 * previous time
	 */ 	protected double previousTime;
	/**
	 * current time
	 */ 	protected double currentTime;
	/**
	 * current time step
	 */ 	protected double h;
	/**
	 * current state
	 */ 	protected double[] currentState;
	/**
	 * interpolated time
	 */ 	protected double interpolatedTime;
	/**
	 * interpolated state
	 */ 	protected double[] interpolatedState;
	/**
	 * interpolated derivatives
	 */ 	protected double[] interpolatedDerivatives;
	/**
	 * indicate if the step has been finalized or not.
	 */ 	private boolean finalized;
	/**
	 * integration direction.
	 */ 	private boolean forward;
	/**
	 * indicator for dirty state.
	 */ 	private boolean dirtyState;

	/**
	 * Simple constructor.
	 * This constructor builds an instance that is not usable yet, the
	 * {@link #reinitialize} method should be called before using the
	 * instance in order to initialize the internal arrays. This
	 * constructor is used only in order to delay the initialization in
	 * some cases. As an example, the {@link org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator}
	 * class uses the prototyping design pattern to create the step
	 * interpolators by cloning an uninitialized model and latter
	 * initializing the copy.
	 */
	protected AbstractStepInterpolator() {
		previousTime = java.lang.Double.NaN;
		currentTime = java.lang.Double.NaN;
		h = java.lang.Double.NaN;
		interpolatedTime = java.lang.Double.NaN;
		currentState = null;
		interpolatedState = null;
		interpolatedDerivatives = null;
		finalized = false;
		this.forward = true;
		this.dirtyState = true;
	}

	/**
	 * Simple constructor.
	 *
	 * @param y
	 * 		reference to the integrator array holding the state at
	 * 		the end of the step
	 * @param forward
	 * 		integration direction indicator
	 */ 	protected AbstractStepInterpolator(final double[] y, final boolean forward) { 		previousTime = java.lang.Double.NaN; 		currentTime = java.lang.Double.NaN;
		h = java.lang.Double.NaN;
		interpolatedTime = java.lang.Double.NaN;

		currentState = y;
		interpolatedState = new double[y.length];
		interpolatedDerivatives = new double[y.length];

		finalized = false;
		this.forward = forward;
		this.dirtyState = true;

	}

	/**
	 * Copy constructor.
	 *
	 * <p>The copied interpolator should have been finalized before the
	 * copy, otherwise the copy will not be able to perform correctly
	 * any derivative computation and will throw a {@link NullPointerException} later. Since we don't want this constructor
	 * to throw the exceptions finalization may involve and since we
	 * don't want this method to modify the state of the copied
	 * interpolator, finalization is <strong>not</strong> done
	 * automatically, it remains under user control.</p>
	 *
	 * <p>The copy is a deep copy: its arrays are separated from the
	 * original arrays of the instance.</p>
	 *
	 * @param interpolator
	 * 		interpolator to copy from.
	 */
	protected AbstractStepInterpolator(final org.apache.commons.math.ode.sampling.AbstractStepInterpolator interpolator) {

		previousTime = interpolator.previousTime;
		currentTime = interpolator.currentTime;
		h = interpolator.h;
		interpolatedTime = interpolator.interpolatedTime;

		if (interpolator.currentState != null) {
			currentState = interpolator.currentState.clone();
			interpolatedState = interpolator.interpolatedState.clone();
			interpolatedDerivatives = interpolator.interpolatedDerivatives.clone();
		} else {
			currentState = null;
			interpolatedState = null;
			interpolatedDerivatives = null;
		}

		finalized = interpolator.finalized;
		forward = interpolator.forward;
		dirtyState = interpolator.dirtyState;

	}

	/**
	 * Reinitialize the instance
	 *
	 * @param y
	 * 		reference to the integrator array holding the state at
	 * 		the end of the step
	 * @param isForward
	 * 		integration direction indicator
	 */ 	protected void reinitialize(final double[] y, final boolean isForward) { 		previousTime = java.lang.Double.NaN; 		currentTime = java.lang.Double.NaN;
		h = java.lang.Double.NaN;
		interpolatedTime = java.lang.Double.NaN;

		currentState = y;
		interpolatedState = new double[y.length];
		interpolatedDerivatives = new double[y.length];

		finalized = false;
		this.forward = isForward;
		this.dirtyState = true;

	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.ode.sampling.StepInterpolator copy() throws org.apache.commons.math.ode.DerivativeException {
		// finalize the step before performing copy
		finalizeStep();

		// create the new independent instance
		return doCopy();

	}

	/**
	 * Really copy the finalized instance.
	 * <p>This method is called by {@link #copy()} after the
	 * step has been finalized. It must perform a deep copy
	 * to have an new instance completely independent for the
	 * original instance.
	 *
	 * @return a copy of the finalized instance
	 */ 	protected abstract org.apache.commons.math.ode.sampling.StepInterpolator doCopy();
	/**
	 * Shift one step forward.
	 * Copy the current time into the previous time, hence preparing the
	 * interpolator for future calls to {@link #storeTime storeTime}
	 */ 	public void shift() {
		previousTime = currentTime;
	}

	/**
	 * Store the current step time.
	 *
	 * @param t
	 * 		current time
	 */ 	public void storeTime(final double t) { 		currentTime = t;
		h = currentTime - previousTime;
		setInterpolatedTime(t);

		// the step is not finalized anymore
		finalized = false;

	}

	/**
	 * {@inheritDoc }
	 */ 	public double getPreviousTime() { 		return previousTime;
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getCurrentTime() { 		return currentTime;
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getInterpolatedTime() { 		return interpolatedTime;
	}

	/**
	 * {@inheritDoc }
	 */ 	public void setInterpolatedTime(final double time) { 		interpolatedTime = time;
		dirtyState = true;
	}

	/**
	 * {@inheritDoc }
	 */ 	public boolean isForward() { 		return forward;
	}

	/**
	 * Compute the state and derivatives at the interpolated time.
	 * This is the main processing method that should be implemented by
	 * the derived classes to perform the interpolation.
	 *
	 * @param theta
	 * 		normalized interpolation abscissa within the step
	 * 		(theta is zero at the previous time step and one at the current time step)
	 * @param oneMinusThetaH
	 * 		time gap between the interpolated time and
	 * 		the current time
	 * @throws DerivativeException
	 * 		this exception is propagated to the caller if the
	 * 		underlying user function triggers one
	 */ 	protected abstract void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) throws org.apache.commons.math.ode.DerivativeException; 	/**
	 * {@inheritDoc }
	 */ 	public double[] getInterpolatedState() throws org.apache.commons.math.ode.DerivativeException {
		// lazy evaluation of the state
		if (dirtyState) {
			final double oneMinusThetaH = currentTime - interpolatedTime;
			final double theta = (h == 0) ? 0 : (h - oneMinusThetaH) / h;
			computeInterpolatedStateAndDerivatives(theta, oneMinusThetaH);
			dirtyState = false;
		}

		return interpolatedState;

	}

	/**
	 * {@inheritDoc }
	 */ 	public double[] getInterpolatedDerivatives() throws org.apache.commons.math.ode.DerivativeException {
		// lazy evaluation of the state
		if (dirtyState) {
			final double oneMinusThetaH = currentTime - interpolatedTime;
			final double theta = (h == 0) ? 0 : (h - oneMinusThetaH) / h;
			computeInterpolatedStateAndDerivatives(theta, oneMinusThetaH);
			dirtyState = false;
		}

		return interpolatedDerivatives;

	}

	/**
	 * Finalize the step.
	 *
	 * <p>Some embedded Runge-Kutta integrators need fewer functions
	 * evaluations than their counterpart step interpolators. These
	 * interpolators should perform the last evaluations they need by
	 * themselves only if they need them. This method triggers these
	 * extra evaluations. It can be called directly by the user step
	 * handler and it is called automatically if {@link #setInterpolatedTime} is called.</p>
	 *
	 * <p>Once this method has been called, <strong>no</strong> other
	 * evaluation will be performed on this step. If there is a need to
	 * have some side effects between the step handler and the
	 * differential equations (for example update some data in the
	 * equations once the step has been done), it is advised to call
	 * this method explicitly from the step handler before these side
	 * effects are set up. If the step handler induces no side effect,
	 * then this method can safely be ignored, it will be called
	 * transparently as needed.</p>
	 *
	 * <p><strong>Warning</strong>: since the step interpolator provided
	 * to the step handler as a parameter of the {@link StepHandler#handleStep handleStep} is valid only for the duration
	 * of the {@link StepHandler#handleStep handleStep} call, one cannot
	 * simply store a reference and reuse it later. One should first
	 * finalize the instance, then copy this finalized instance into a
	 * new object that can be kept.</p>
	 *
	 * <p>This method calls the protected <code>doFinalize</code> method
	 * if it has never been called during this step and set a flag
	 * indicating that it has been called once. It is the <code>
	 * doFinalize</code> method which should perform the evaluations.
	 * This wrapping prevents from calling <code>doFinalize</code> several
	 * times and hence evaluating the differential equations too often.
	 * Therefore, subclasses are not allowed not reimplement it, they
	 * should rather reimplement <code>doFinalize</code>.</p>
	 *
	 * @throws DerivativeException
	 * 		this exception is propagated to the
	 * 		caller if the underlying user function triggers one
	 */

	public final void finalizeStep() throws 
	org.apache.commons.math.ode.DerivativeException {
		if (!finalized) {
			doFinalize();
			finalized = true;
		}
	}

	/**
	 * Really finalize the step.
	 * The default implementation of this method does nothing.
	 *
	 * @throws DerivativeException
	 * 		this exception is propagated to the
	 * 		caller if the underlying user function triggers one
	 */ 	protected void doFinalize() throws org.apache.commons.math.ode.DerivativeException {
	}

	/**
	 * {@inheritDoc }
	 */ 	public abstract void writeExternal(java.io.ObjectOutput out) throws java.io.IOException;

	/**
	 * {@inheritDoc }
	 */ 	public abstract void readExternal(java.io.ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException;

	/**
	 * Save the base state of the instance.
	 * This method performs step finalization if it has not been done
	 * before.
	 *
	 * @param out
	 * 		stream where to save the state
	 * @exception IOException
	 * 		in case of write error
	 */ 	protected void writeBaseExternal(final java.io.ObjectOutput out) throws java.io.IOException { 		if (currentState == null) {
			out.writeInt(-1);
		} else {
			out.writeInt(currentState.length);
		}
		out.writeDouble(previousTime);
		out.writeDouble(currentTime);
		out.writeDouble(h);
		out.writeBoolean(forward);

		if (currentState != null) {
			for (int i = 0; i < currentState.length; ++i) {
				out.writeDouble(currentState[i]);
			}
		}

		out.writeDouble(interpolatedTime);

		// we do not store the interpolated state,
		// it will be recomputed as needed after reading

		// finalize the step (and don't bother saving the now true flag)
		try {
			finalizeStep();
		} catch (org.apache.commons.math.ode.DerivativeException e) {
			throw org.apache.commons.math.MathRuntimeException.createIOException(e);
		}

	}

	/**
	 * Read the base state of the instance.
	 * This method does <strong>neither</strong> set the interpolated
	 * time nor state. It is up to the derived class to reset it
	 * properly calling the {@link #setInterpolatedTime} method later,
	 * once all rest of the object state has been set up properly.
	 *
	 * @param in
	 * 		stream where to read the state from
	 * @return interpolated time be set later by the caller
	 * @exception IOException
	 * 		in case of read error
	 */ 	protected double readBaseExternal(final java.io.ObjectInput in) throws java.io.IOException { 		final int dimension = in.readInt();
		previousTime = in.readDouble();
		currentTime = in.readDouble();
		h = in.readDouble();
		forward = in.readBoolean();
		dirtyState = true;

		if (dimension < 0) {
			currentState = null;
		} else {
			currentState = new double[dimension];
			for (int i = 0; i < currentState.length; ++i) {
				currentState[i] = in.readDouble();
			}
		}

		// we do NOT handle the interpolated time and state here
		interpolatedTime = java.lang.Double.NaN;
		interpolatedState = (dimension < 0) ? null : new double[dimension];
		interpolatedDerivatives = (dimension < 0) ? null : new double[dimension];

		finalized = true;

		return in.readDouble();

	}}