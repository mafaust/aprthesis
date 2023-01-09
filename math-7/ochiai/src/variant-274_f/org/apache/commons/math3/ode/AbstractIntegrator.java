package org.apache.commons.math3.ode;
/**
 * Base class managing common boilerplate for all integrators.
 *
 * @version $Id$
 * @since 2.0
 */
public abstract class AbstractIntegrator implements org.apache.commons.math3.ode.FirstOrderIntegrator {
	/**
	 * Step handler.
	 */
	protected java.util.Collection<org.apache.commons.math3.ode.sampling.StepHandler> stepHandlers;

	/**
	 * Current step start time.
	 */
	protected double stepStart;

	/**
	 * Current stepsize.
	 */
	protected double stepSize;

	/**
	 * Indicator for last step.
	 */
	protected boolean isLastStep;

	/**
	 * Indicator that a state or derivative reset was triggered by some event.
	 */
	protected boolean resetOccurred;

	/**
	 * Events states.
	 */
	private java.util.Collection<org.apache.commons.math3.ode.events.EventState> eventsStates;

	/**
	 * Initialization indicator of events states.
	 */
	private boolean statesInitialized;

	/**
	 * Name of the method.
	 */
	private final java.lang.String name;

	/**
	 * Counter for number of evaluations.
	 */
	private org.apache.commons.math3.util.Incrementor evaluations;

	/**
	 * Differential equations to integrate.
	 */
	private transient org.apache.commons.math3.ode.ExpandableStatefulODE expandable;

	/**
	 * Build an instance.
	 *
	 * @param name
	 * 		name of the method
	 */
	public AbstractIntegrator(final java.lang.String name) {
		this.name = name;
		stepHandlers = new java.util.ArrayList<org.apache.commons.math3.ode.sampling.StepHandler>();
		stepStart = java.lang.Double.NaN;
		stepSize = java.lang.Double.NaN;
		eventsStates = new java.util.ArrayList<org.apache.commons.math3.ode.events.EventState>();
		statesInitialized = false;
		evaluations = new org.apache.commons.math3.util.Incrementor();
		setMaxEvaluations(-1);
		evaluations.resetCount();
	}

	/**
	 * Build an instance with a null name.
	 */
	protected AbstractIntegrator() {
		this(null);
	}

	/**
	 * {@inheritDoc }
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * {@inheritDoc }
	 */
	public void addStepHandler(final org.apache.commons.math3.ode.sampling.StepHandler handler) {
		eventsStates.clear();
		stepHandlers.add(handler);
	}

	/**
	 * {@inheritDoc }
	 */
	public java.util.Collection<org.apache.commons.math3.ode.sampling.StepHandler> getStepHandlers() {
		return java.util.Collections.unmodifiableCollection(stepHandlers);
	}

	/**
	 * {@inheritDoc }
	 */
	public void clearStepHandlers() {
		stepHandlers.clear();
	}

	/**
	 * {@inheritDoc }
	 */
	public void addEventHandler(final org.apache.commons.math3.ode.events.EventHandler handler, final double maxCheckInterval, final double convergence, final int maxIterationCount) {
		addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount, new org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver(convergence, 5));
	}

	/**
	 * {@inheritDoc }
	 */
	public void addEventHandler(final org.apache.commons.math3.ode.events.EventHandler handler, final double maxCheckInterval, final double convergence, final int maxIterationCount, final org.apache.commons.math3.analysis.solvers.UnivariateSolver solver) {
		eventsStates.add(new org.apache.commons.math3.ode.events.EventState(handler, maxCheckInterval, convergence, maxIterationCount, solver));
	}

	/**
	 * {@inheritDoc }
	 */
	public java.util.Collection<org.apache.commons.math3.ode.events.EventHandler> getEventHandlers() {
		final java.util.List<org.apache.commons.math3.ode.events.EventHandler> list = new java.util.ArrayList<org.apache.commons.math3.ode.events.EventHandler>();
		for (org.apache.commons.math3.ode.events.EventState state : eventsStates) {
			list.add(state.getEventHandler());
		}
		return java.util.Collections.unmodifiableCollection(list);
	}

	/**
	 * {@inheritDoc }
	 */
	public void clearEventHandlers() {
		eventsStates.clear();
	}

	/**
	 * {@inheritDoc }
	 */
	public double getCurrentStepStart() {
		return stepStart;
	}

	/**
	 * {@inheritDoc }
	 */
	public double getCurrentSignedStepsize() {
		return stepSize;
	}

	/**
	 * {@inheritDoc }
	 */
	public void setMaxEvaluations(int maxEvaluations) {
		evaluations.setMaximalCount(maxEvaluations < 0 ? java.lang.Integer.MAX_VALUE : maxEvaluations);
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
	 * Prepare the start of an integration.
	 *
	 * @param t0
	 * 		start value of the independent <i>time</i> variable
	 * @param y0
	 * 		array containing the start value of the state vector
	 * @param t
	 * 		target time for the integration
	 */
	protected void initIntegration(final double t0, final double[] y0, final double t) {
		evaluations.resetCount();
		for (final org.apache.commons.math3.ode.events.EventState state : eventsStates) {
			state.getEventHandler().init(t0, y0, t);
		}
		for (org.apache.commons.math3.ode.sampling.StepHandler handler : stepHandlers) {
			handler.init(t0, y0, t);
		}
		setStateInitialized(false);
	}

	/**
	 * Set the equations.
	 *
	 * @param equations
	 * 		equations to set
	 */
	protected void setEquations(final org.apache.commons.math3.ode.ExpandableStatefulODE equations) {
		this.expandable = equations;
	}

	/**
	 * {@inheritDoc }
	 */
	public double integrate(final org.apache.commons.math3.ode.FirstOrderDifferentialEquations equations, final double t0, final double[] y0, final double t, final double[] y) throws org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.NumberIsTooSmallException, org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.NoBracketingException {
		if (y0.length != equations.getDimension()) {
			throw new org.apache.commons.math3.exception.DimensionMismatchException(y0.length, equations.getDimension());
		}
		if (y.length != equations.getDimension()) {
			throw new org.apache.commons.math3.exception.DimensionMismatchException(y.length, equations.getDimension());
		}
		// prepare expandable stateful equations
		final org.apache.commons.math3.ode.ExpandableStatefulODE expandableODE = new org.apache.commons.math3.ode.ExpandableStatefulODE(equations);
		expandableODE.setTime(t0);
		expandableODE.setPrimaryState(y0);
		// perform integration
		integrate(expandableODE, t);
		// extract results back from the stateful equations
		java.lang.System.arraycopy(expandableODE.getPrimaryState(), 0, y, 0, y.length);
		return expandableODE.getTime();
	}

	/**
	 * Integrate a set of differential equations up to the given time.
	 * <p>This method solves an Initial Value Problem (IVP).</p>
	 * <p>The set of differential equations is composed of a main set, which
	 * can be extended by some sets of secondary equations. The set of
	 * equations must be already set up with initial time and partial states.
	 * At integration completion, the final time and partial states will be
	 * available in the same object.</p>
	 * <p>Since this method stores some internal state variables made
	 * available in its public interface during integration ({@link #getCurrentSignedStepsize()}), it is <em>not</em> thread-safe.</p>
	 *
	 * @param equations
	 * 		complete set of differential equations to integrate
	 * @param t
	 * 		target time for the integration
	 * 		(can be set to a value smaller than <code>t0</code> for backward integration)
	 * @exception NumberIsTooSmallException
	 * 		if integration step is too small
	 * @throws DimensionMismatchException
	 * 		if the dimension of the complete state does not
	 * 		match the complete equations sets dimension
	 * @exception MaxCountExceededException
	 * 		if the number of functions evaluations is exceeded
	 * @exception NoBracketingException
	 * 		if the location of an event cannot be bracketed
	 */
	public abstract void integrate(org.apache.commons.math3.ode.ExpandableStatefulODE equations, double t) throws org.apache.commons.math3.exception.NumberIsTooSmallException, org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.NoBracketingException;

	/**
	 * Compute the derivatives and check the number of evaluations.
	 *
	 * @param t
	 * 		current value of the independent <I>time</I> variable
	 * @param y
	 * 		array containing the current value of the state vector
	 * @param yDot
	 * 		placeholder array where to put the time derivative of the state vector
	 * @exception MaxCountExceededException
	 * 		if the number of functions evaluations is exceeded
	 * @exception DimensionMismatchException
	 * 		if arrays dimensions do not match equations settings
	 */
	public void computeDerivatives(final double t, final double[] y, final double[] yDot) throws org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.DimensionMismatchException {
		evaluations.incrementCount();
		expandable.computeDerivatives(t, y, yDot);
	}

	/**
	 * Set the stateInitialized flag.
	 * <p>This method must be called by integrators with the value
	 * {@code false} before they start integration, so a proper lazy
	 * initialization is done automatically on the first step.</p>
	 *
	 * @param stateInitialized
	 * 		new value for the flag
	 * @since 2.2
	 */
	protected void setStateInitialized(final boolean stateInitialized) {
		this.statesInitialized = stateInitialized;
	}

	/**
	 * Accept a step, triggering events and step handlers.
	 *
	 * @param interpolator
	 * 		step interpolator
	 * @param y
	 * 		state vector at step end time, must be reset if an event
	 * 		asks for resetting or if an events stops integration during the step
	 * @param yDot
	 * 		placeholder array where to put the time derivative of the state vector
	 * @param tEnd
	 * 		final integration time
	 * @return time at end of step
	 * @exception MaxCountExceededException
	 * 		if the interpolator throws one because
	 * 		the number of functions evaluations is exceeded
	 * @exception NoBracketingException
	 * 		if the location of an event cannot be bracketed
	 * @exception DimensionMismatchException
	 * 		if arrays dimensions do not match equations settings
	 * @since 2.2
	 */
	protected double acceptStep(final org.apache.commons.math3.ode.sampling.AbstractStepInterpolator interpolator, final double[] y, final double[] yDot, final double tEnd) throws org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.NoBracketingException {
		double previousT = interpolator.getGlobalPreviousTime();
		final double currentT = interpolator.getGlobalCurrentTime();
		// initialize the events states if needed
		if (!statesInitialized) {
			for (org.apache.commons.math3.ode.events.EventState state : eventsStates) {
				state.reinitializeBegin(interpolator);
			}
			statesInitialized = true;
		}
		// search for next events that may occur during the step
		final int orderingSign = (interpolator.isForward()) ? +1 : -1;
		java.util.SortedSet<org.apache.commons.math3.ode.events.EventState> occuringEvents = new java.util.TreeSet<org.apache.commons.math3.ode.events.EventState>(new java.util.Comparator<org.apache.commons.math3.ode.events.EventState>() {
			/**
			 * {@inheritDoc }
			 */
			public int compare(org.apache.commons.math3.ode.events.EventState es0, org.apache.commons.math3.ode.events.EventState es1) {
				return orderingSign * java.lang.Double.compare(es0.getEventTime(), es1.getEventTime());
			}
		});
		for (final org.apache.commons.math3.ode.events.EventState state : eventsStates) {
			if (state.evaluateStep(interpolator)) {
				// the event occurs during the current step
				occuringEvents.add(state);
			}
		}
		while (!occuringEvents.isEmpty()) {
			// handle the chronologically first event
			final java.util.Iterator<org.apache.commons.math3.ode.events.EventState> iterator = occuringEvents.iterator();
			final org.apache.commons.math3.ode.events.EventState currentEvent = iterator.next();
			iterator.remove();
			// restrict the interpolator to the first part of the step, up to the event
			final double eventT = currentEvent.getEventTime();
			interpolator.setSoftPreviousTime(previousT);
			interpolator.setSoftCurrentTime(eventT);
			// get state at event time
			interpolator.setInterpolatedTime(eventT);
			final double[] eventY = interpolator.getInterpolatedState().clone();
			// advance all event states to current time
			currentEvent.stepAccepted(eventT, eventY);
			isLastStep = currentEvent.stop();
			// handle the first part of the step, up to the event
			for (final org.apache.commons.math3.ode.sampling.StepHandler handler : stepHandlers) {
				handler.handleStep(interpolator, isLastStep);
			}
			if (isLastStep) {
				// the event asked to stop integration
				java.lang.System.arraycopy(eventY, 0, y, 0, y.length);
				for (final org.apache.commons.math3.ode.events.EventState remaining : occuringEvents) {
					remaining.stepAccepted(eventT, eventY);
				}
				return eventT;
			}
			boolean needReset = currentEvent.reset(eventT, eventY);
			if (needReset) {
				// some event handler has triggered changes that
				// invalidate the derivatives, we need to recompute them
				java.lang.System.arraycopy(eventY, 0, y, 0, y.length);
				computeDerivatives(eventT, y, yDot);
				resetOccurred = true;
				for (final org.apache.commons.math3.ode.events.EventState remaining : occuringEvents) {
					remaining.stepAccepted(eventT, eventY);
				}
				return eventT;
			}
			// prepare handling of the remaining part of the step
			previousT = eventT;
			interpolator.setSoftPreviousTime(eventT);
			interpolator.setSoftCurrentTime(currentT);
			// check if the same event occurs again in the remaining part of the step
			if (currentEvent.evaluateStep(interpolator)) {
				// the event occurs during the current step
				occuringEvents.add(currentEvent);
			}
		} 
		// last part of the step, after the last event
		interpolator.setInterpolatedTime(currentT);
		final double[] currentY = interpolator.getInterpolatedState();
		for (final org.apache.commons.math3.ode.events.EventState state : eventsStates) {
			state.stepAccepted(currentT, currentY);
			isLastStep = isLastStep || state.stop();
		}
		isLastStep = isLastStep || org.apache.commons.math3.util.Precision.equals(currentT, tEnd, 1);
		// handle the remaining part of the step, after all events if any
		for (org.apache.commons.math3.ode.sampling.StepHandler handler : stepHandlers) {
			handler.handleStep(interpolator, isLastStep);
		}
		return currentT;
	}

	/**
	 * Check the integration span.
	 *
	 * @param equations
	 * 		set of differential equations
	 * @param t
	 * 		target time for the integration
	 * @exception NumberIsTooSmallException
	 * 		if integration span is too small
	 * @exception DimensionMismatchException
	 * 		if adaptive step size integrators
	 * 		tolerance arrays dimensions are not compatible with equations settings
	 */
	protected void sanityChecks(final org.apache.commons.math3.ode.ExpandableStatefulODE equations, final double t) throws org.apache.commons.math3.exception.NumberIsTooSmallException, org.apache.commons.math3.exception.DimensionMismatchException {
		final double threshold = 1000 * org.apache.commons.math3.util.FastMath.ulp(org.apache.commons.math3.util.FastMath.max(org.apache.commons.math3.util.FastMath.abs(equations.getTime()), org.apache.commons.math3.util.FastMath.abs(t)));
		final double dt = org.apache.commons.math3.util.FastMath.abs(equations.getTime() - t);
		if (dt <= threshold) {
			throw new org.apache.commons.math3.exception.NumberIsTooSmallException(org.apache.commons.math3.exception.util.LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, dt, threshold, false);
		}
	}
}