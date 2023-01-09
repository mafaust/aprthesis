package org.apache.commons.math.ode;
/**
 * Base class managing common boilerplate for all integrators.
 *
 * @version $Revision$ $Date$
 * @since 2.0
 */
public abstract class AbstractIntegrator implements org.apache.commons.math.ode.FirstOrderIntegrator {
	/**
	 * Step handler.
	 */
	protected java.util.Collection<org.apache.commons.math.ode.sampling.StepHandler> stepHandlers;

	/**
	 * Current step start time.
	 */
	protected double stepStart;

	/**
	 * Current stepsize.
	 */
	protected double stepSize;

	/**
	 * Events handlers manager.
	 */
	protected org.apache.commons.math.ode.events.CombinedEventsManager eventsHandlersManager;

	/**
	 * Name of the method.
	 */
	private final java.lang.String name;

	/**
	 * Maximal number of evaluations allowed.
	 */
	private int maxEvaluations;

	/**
	 * Number of evaluations already performed.
	 */
	private int evaluations;

	/**
	 * Differential equations to integrate.
	 */
	private transient org.apache.commons.math.ode.FirstOrderDifferentialEquations equations;

	/**
	 * Build an instance.
	 *
	 * @param name
	 * 		name of the method
	 */
	public AbstractIntegrator(final java.lang.String name) {
		this.name = name;
		stepHandlers = new java.util.ArrayList<org.apache.commons.math.ode.sampling.StepHandler>();
		stepStart = java.lang.Double.NaN;
		stepSize = java.lang.Double.NaN;
		eventsHandlersManager = new org.apache.commons.math.ode.events.CombinedEventsManager();
		setMaxEvaluations(-1);
		resetEvaluations();
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
	public void addStepHandler(final org.apache.commons.math.ode.sampling.StepHandler handler) {
		stepHandlers.add(handler);
	}

	/**
	 * {@inheritDoc }
	 */
	public java.util.Collection<org.apache.commons.math.ode.sampling.StepHandler> getStepHandlers() {
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
	public void addEventHandler(final org.apache.commons.math.ode.events.EventHandler function, final double maxCheckInterval, final double convergence, final int maxIterationCount) {
		eventsHandlersManager.addEventHandler(function, maxCheckInterval, convergence, maxIterationCount);
	}

	/**
	 * {@inheritDoc }
	 */
	public java.util.Collection<org.apache.commons.math.ode.events.EventHandler> getEventHandlers() {
		return eventsHandlersManager.getEventsHandlers();
	}

	/**
	 * {@inheritDoc }
	 */
	public void clearEventHandlers() {
		eventsHandlersManager.clearEventsHandlers();
	}

	/**
	 * Check if one of the step handlers requires dense output.
	 *
	 * @return true if one of the step handlers requires dense output
	 */
	protected boolean requiresDenseOutput() {
		for (org.apache.commons.math.ode.sampling.StepHandler handler : stepHandlers) {
			if (handler.requiresDenseOutput()) {
				return true;
			}
		}
		return false;
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
		this.maxEvaluations = (maxEvaluations < 0) ? java.lang.Integer.MAX_VALUE : maxEvaluations;
	}

	/**
	 * {@inheritDoc }
	 */
	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	/**
	 * {@inheritDoc }
	 */
	public int getEvaluations() {
		return evaluations;
	}

	/**
	 * Reset the number of evaluations to zero.
	 */
	protected void resetEvaluations() {
		evaluations = 0;
	}

	/**
	 * Set the differential equations.
	 *
	 * @param equations
	 * 		differential equations to integrate
	 * @see #computeDerivatives(double, double[], double[])
	 */
	protected void setEquations(final org.apache.commons.math.ode.FirstOrderDifferentialEquations equations) {
		this.equations = equations;
	}

	/**
	 * Compute the derivatives and check the number of evaluations.
	 *
	 * @param t
	 * 		current value of the independent <I>time</I> variable
	 * @param y
	 * 		array containing the current value of the state vector
	 * @param yDot
	 * 		placeholder array where to put the time derivative of the state vector
	 * @throws DerivativeException
	 * 		this exception is propagated to the caller if the
	 * 		underlying user function triggers one
	 */
	public void computeDerivatives(final double t, final double[] y, final double[] yDot) throws org.apache.commons.math.ode.DerivativeException {
		if ((++evaluations) > maxEvaluations) {
			throw new org.apache.commons.math.ode.DerivativeException(new org.apache.commons.math.MaxEvaluationsExceededException(maxEvaluations));
		}
		equations.computeDerivatives(t, y, yDot);
	}

	/**
	 * Perform some sanity checks on the integration parameters.
	 *
	 * @param ode
	 * 		differential equations set
	 * @param t0
	 * 		start time
	 * @param y0
	 * 		state vector at t0
	 * @param t
	 * 		target time for the integration
	 * @param y
	 * 		placeholder where to put the state vector
	 * @exception IntegratorException
	 * 		if some inconsistency is detected
	 */
	protected void sanityChecks(final org.apache.commons.math.ode.FirstOrderDifferentialEquations ode, final double t0, final double[] y0, final double t, final double[] y) throws org.apache.commons.math.ode.IntegratorException {
		if (ode.getDimension() != y0.length) {
			throw new org.apache.commons.math.ode.IntegratorException("dimensions mismatch: ODE problem has dimension {0}," + " initial state vector has dimension {1}", ode.getDimension(), y0.length);
		}
		if (ode.getDimension() != y.length) {
			throw new org.apache.commons.math.ode.IntegratorException("dimensions mismatch: ODE problem has dimension {0}," + " final state vector has dimension {1}", ode.getDimension(), y.length);
		}
		if (java.lang.Math.abs(t - t0) <= (1.0E-12 * java.lang.Math.max(java.lang.Math.abs(t0), java.lang.Math.abs(t)))) {
			throw new org.apache.commons.math.ode.IntegratorException("too small integration interval: length = {0}", java.lang.Math.abs(t - t0));
		}
	}

	/**
	 * Add an event handler for end time checking.
	 * <p>This method can be used to simplify handling of integration end time.
	 * It leverages the nominal stop condition with the exceptional stop
	 * conditions.</p>
	 *
	 * @param startTime
	 * 		integration start time
	 * @param endTime
	 * 		desired end time
	 * @param manager
	 * 		manager containing the user-defined handlers
	 * @return a new manager containing all the user-defined handlers plus a
	dedicated manager triggering a stop event at entTime
	 */
	protected org.apache.commons.math.ode.events.CombinedEventsManager addEndTimeChecker(final double startTime, final double endTime, final org.apache.commons.math.ode.events.CombinedEventsManager manager) {
		org.apache.commons.math.ode.events.CombinedEventsManager newManager = new org.apache.commons.math.ode.events.CombinedEventsManager();
		for (final org.apache.commons.math.ode.events.EventState state : manager.getEventsStates()) {
			newManager.addEventHandler(state.getEventHandler(), state.getMaxCheckInterval(), state.getConvergence(), state.getMaxIterationCount());
		}
		newManager.addEventHandler(new org.apache.commons.math.ode.AbstractIntegrator.EndTimeChecker(endTime), java.lang.Double.POSITIVE_INFINITY, java.lang.Math.ulp(java.lang.Math.max(java.lang.Math.abs(startTime), java.lang.Math.abs(endTime))), 100);
		return newManager;
	}

	/**
	 * Specialized event handler to stop integration.
	 */
	private static class EndTimeChecker implements org.apache.commons.math.ode.events.EventHandler {
		/**
		 * Desired end time.
		 */
		private final double endTime;

		/**
		 * Build an instance.
		 *
		 * @param endTime
		 * 		desired time
		 */
		public EndTimeChecker(final double endTime) {
			this.endTime = endTime;
		}

		/**
		 * {@inheritDoc }
		 */
		public int eventOccurred(double t, double[] y, boolean increasing) {
			return org.apache.commons.math.ode.events.EventHandler.STOP;
		}

		/**
		 * {@inheritDoc }
		 */
		public double g(double t, double[] y) {
			return t - endTime;
		}

		/**
		 * {@inheritDoc }
		 */
		public void resetState(double t, double[] y) {
		}
	}
}