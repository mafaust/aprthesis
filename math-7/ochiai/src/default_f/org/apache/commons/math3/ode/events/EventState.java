package org.apache.commons.math3.ode.events;
/**
 * This class handles the state for one {@link EventHandler
 * event handler} during integration steps.
 *
 * <p>Each time the integrator proposes a step, the event handler
 * switching function should be checked. This class handles the state
 * of one handler during one integration step, with references to the
 * state at the end of the preceding step. This information is used to
 * decide if the handler should trigger an event or not during the
 * proposed step.</p>
 *
 * @version $Id$
 * @since 1.2
 */
public class EventState {
	/**
	 * Event handler.
	 */
	private final org.apache.commons.math3.ode.events.EventHandler handler;

	/**
	 * Maximal time interval between events handler checks.
	 */
	private final double maxCheckInterval;

	/**
	 * Convergence threshold for event localization.
	 */
	private final double convergence;

	/**
	 * Upper limit in the iteration count for event localization.
	 */
	private final int maxIterationCount;

	/**
	 * Time at the beginning of the step.
	 */
	private double t0;

	/**
	 * Value of the events handler at the beginning of the step.
	 */
	private double g0;

	/**
	 * Simulated sign of g0 (we cheat when crossing events).
	 */
	private boolean g0Positive;

	/**
	 * Indicator of event expected during the step.
	 */
	private boolean pendingEvent;

	/**
	 * Occurrence time of the pending event.
	 */
	private double pendingEventTime;

	/**
	 * Occurrence time of the previous event.
	 */
	private double previousEventTime;

	/**
	 * Integration direction.
	 */
	private boolean forward;

	/**
	 * Variation direction around pending event.
	 *  (this is considered with respect to the integration direction)
	 */
	private boolean increasing;

	/**
	 * Next action indicator.
	 */
	private org.apache.commons.math3.ode.events.EventHandler.Action nextAction;

	/**
	 * Root-finding algorithm to use to detect state events.
	 */
	private final org.apache.commons.math3.analysis.solvers.UnivariateSolver solver;

	/**
	 * Simple constructor.
	 *
	 * @param handler
	 * 		event handler
	 * @param maxCheckInterval
	 * 		maximal time interval between switching
	 * 		function checks (this interval prevents missing sign changes in
	 * 		case the integration steps becomes very large)
	 * @param convergence
	 * 		convergence threshold in the event time search
	 * @param maxIterationCount
	 * 		upper limit of the iteration count in
	 * 		the event time search
	 * @param solver
	 * 		Root-finding algorithm to use to detect state events
	 */
	public EventState(final org.apache.commons.math3.ode.events.EventHandler handler, final double maxCheckInterval, final double convergence, final int maxIterationCount, final org.apache.commons.math3.analysis.solvers.UnivariateSolver solver) {
		this.handler = handler;
		this.maxCheckInterval = maxCheckInterval;
		this.convergence = org.apache.commons.math3.util.FastMath.abs(convergence);
		this.maxIterationCount = maxIterationCount;
		this.solver = solver;
		// some dummy values ...
		t0 = java.lang.Double.NaN;
		g0 = java.lang.Double.NaN;
		g0Positive = true;
		pendingEvent = false;
		pendingEventTime = java.lang.Double.NaN;
		previousEventTime = java.lang.Double.NaN;
		increasing = true;
		nextAction = org.apache.commons.math3.ode.events.EventHandler.Action.CONTINUE;
	}

	/**
	 * Get the underlying event handler.
	 *
	 * @return underlying event handler
	 */
	public org.apache.commons.math3.ode.events.EventHandler getEventHandler() {
		return handler;
	}

	/**
	 * Get the maximal time interval between events handler checks.
	 *
	 * @return maximal time interval between events handler checks
	 */
	public double getMaxCheckInterval() {
		return maxCheckInterval;
	}

	/**
	 * Get the convergence threshold for event localization.
	 *
	 * @return convergence threshold for event localization
	 */
	public double getConvergence() {
		return convergence;
	}

	/**
	 * Get the upper limit in the iteration count for event localization.
	 *
	 * @return upper limit in the iteration count for event localization
	 */
	public int getMaxIterationCount() {
		return maxIterationCount;
	}

	/**
	 * Reinitialize the beginning of the step.
	 *
	 * @param interpolator
	 * 		valid for the current step
	 * @exception MaxCountExceededException
	 * 		if the interpolator throws one because
	 * 		the number of functions evaluations is exceeded
	 */
	public void reinitializeBegin(final org.apache.commons.math3.ode.sampling.StepInterpolator interpolator) throws org.apache.commons.math3.exception.MaxCountExceededException {
		t0 = interpolator.getPreviousTime();
		interpolator.setInterpolatedTime(t0);
		g0 = handler.g(t0, interpolator.getInterpolatedState());
		if (g0 == 0) {
			// excerpt from MATH-421 issue:
			// If an ODE solver is setup with an EventHandler that return STOP
			// when the even is triggered, the integrator stops (which is exactly
			// the expected behavior). If however the user wants to restart the
			// solver from the final state reached at the event with the same
			// configuration (expecting the event to be triggered again at a
			// later time), then the integrator may fail to start. It can get stuck
			// at the previous event. The use case for the bug MATH-421 is fairly
			// general, so events occurring exactly at start in the first step should
			// be ignored.
			// extremely rare case: there is a zero EXACTLY at interval start
			// we will use the sign slightly after step beginning to force ignoring this zero
			final double epsilon = org.apache.commons.math3.util.FastMath.max(solver.getAbsoluteAccuracy(), org.apache.commons.math3.util.FastMath.abs(solver.getRelativeAccuracy() * t0));
			final double tStart = t0 + (0.5 * epsilon);
			interpolator.setInterpolatedTime(tStart);
			g0 = handler.g(tStart, interpolator.getInterpolatedState());
		}
		g0Positive = g0 >= 0;
	}

	/**
	 * Evaluate the impact of the proposed step on the event handler.
	 *
	 * @param interpolator
	 * 		step interpolator for the proposed step
	 * @return true if the event handler triggers an event before
	the end of the proposed step
	 * @exception MaxCountExceededException
	 * 		if the interpolator throws one because
	 * 		the number of functions evaluations is exceeded
	 * @exception NoBracketingException
	 * 		if the event cannot be bracketed
	 */
	public boolean evaluateStep(final org.apache.commons.math3.ode.sampling.StepInterpolator interpolator) throws org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.NoBracketingException {
		try {
			forward = interpolator.isForward();
			final double t1 = interpolator.getCurrentTime();
			final double dt = t1 - t0;
			if (org.apache.commons.math3.util.FastMath.abs(dt) < convergence) {
				// we cannot do anything on such a small step, don't trigger any events
				return false;
			}
			final int n = org.apache.commons.math3.util.FastMath.max(1, ((int) (org.apache.commons.math3.util.FastMath.ceil(org.apache.commons.math3.util.FastMath.abs(dt) / maxCheckInterval))));
			final double h = dt / n;
			final org.apache.commons.math3.analysis.UnivariateFunction f = new org.apache.commons.math3.analysis.UnivariateFunction() {
				public double value(final double t) throws org.apache.commons.math3.ode.events.EventState.LocalMaxCountExceededException {
					try {
						interpolator.setInterpolatedTime(t);
						return handler.g(t, interpolator.getInterpolatedState());
					} catch (org.apache.commons.math3.exception.MaxCountExceededException mcee) {
						throw new org.apache.commons.math3.ode.events.EventState.LocalMaxCountExceededException(mcee);
					}
				}
			};
			double ta = t0;
			double ga = g0;
			for (int i = 0; i < n; ++i) {
				// evaluate handler value at the end of the substep
				final double tb = t0 + ((i + 1) * h);
				interpolator.setInterpolatedTime(tb);
				final double gb = handler.g(tb, interpolator.getInterpolatedState());
				// check events occurrence
				if (g0Positive ^ (gb >= 0)) {
					// there is a sign change: an event is expected during this step
					// variation direction, with respect to the integration direction
					increasing = gb >= ga;
					// find the event time making sure we select a solution just at or past the exact root
					final double root;
					if (solver instanceof org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver<?>) {
						@java.lang.SuppressWarnings("unchecked")
						org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver<org.apache.commons.math3.analysis.UnivariateFunction> bracketing = ((org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver<org.apache.commons.math3.analysis.UnivariateFunction>) (solver));
						root = (forward) ? bracketing.solve(maxIterationCount, f, ta, tb, org.apache.commons.math3.analysis.solvers.AllowedSolution.RIGHT_SIDE) : bracketing.solve(maxIterationCount, f, tb, ta, org.apache.commons.math3.analysis.solvers.AllowedSolution.LEFT_SIDE);
					} else {
						final double baseRoot = (forward) ? solver.solve(maxIterationCount, f, ta, tb) : solver.solve(maxIterationCount, f, tb, ta);
						final int remainingEval = maxIterationCount - solver.getEvaluations();
						org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver<org.apache.commons.math3.analysis.UnivariateFunction> bracketing = new org.apache.commons.math3.analysis.solvers.PegasusSolver(solver.getRelativeAccuracy(), solver.getAbsoluteAccuracy());
						root = (forward) ? org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils.forceSide(remainingEval, f, bracketing, baseRoot, ta, tb, org.apache.commons.math3.analysis.solvers.AllowedSolution.RIGHT_SIDE) : org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils.forceSide(remainingEval, f, bracketing, baseRoot, tb, ta, org.apache.commons.math3.analysis.solvers.AllowedSolution.LEFT_SIDE);
					}
					if (((!java.lang.Double.isNaN(previousEventTime)) && (org.apache.commons.math3.util.FastMath.abs(root - ta) <= convergence)) && (org.apache.commons.math3.util.FastMath.abs(root - previousEventTime) <= convergence)) {
						// we have either found nothing or found (again ?) a past event,
						// retry the substep excluding this value
						ta = (forward) ? ta + convergence : ta - convergence;
						ga = f.value(ta);
						--i;
					} else if (java.lang.Double.isNaN(previousEventTime) || (org.apache.commons.math3.util.FastMath.abs(previousEventTime - root) > convergence)) {
						pendingEventTime = root;
						pendingEvent = true;
						return true;
					} else {
						// no sign change: there is no event for now
						ta = tb;
						ga = gb;
					}
				} else {
					// no sign change: there is no event for now
					ta = tb;
					ga = gb;
				}
			}
			// no event during the whole step
			pendingEvent = false;
			pendingEventTime = java.lang.Double.NaN;
			return false;
		} catch (org.apache.commons.math3.ode.events.EventState.LocalMaxCountExceededException lmcee) {
			throw lmcee.getException();
		}
	}

	/**
	 * Get the occurrence time of the event triggered in the current step.
	 *
	 * @return occurrence time of the event triggered in the current
	step or infinity if no events are triggered
	 */
	public double getEventTime() {
		return pendingEvent ? pendingEventTime : forward ? java.lang.Double.POSITIVE_INFINITY : java.lang.Double.NEGATIVE_INFINITY;
	}

	/**
	 * Acknowledge the fact the step has been accepted by the integrator.
	 *
	 * @param t
	 * 		value of the independent <i>time</i> variable at the
	 * 		end of the step
	 * @param y
	 * 		array containing the current value of the state vector
	 * 		at the end of the step
	 */
	public void stepAccepted(final double t, final double[] y) {
		t0 = t;
		g0 = handler.g(t, y);
		if (pendingEvent && (org.apache.commons.math3.util.FastMath.abs(pendingEventTime - t) <= convergence)) {
			// force the sign to its value "just after the event"
			previousEventTime = t;
			g0Positive = increasing;
			nextAction = handler.eventOccurred(t, y, !(increasing ^ forward));
		} else {
			g0Positive = g0 >= 0;
			nextAction = org.apache.commons.math3.ode.events.EventHandler.Action.CONTINUE;
		}
	}

	/**
	 * Check if the integration should be stopped at the end of the
	 * current step.
	 *
	 * @return true if the integration should be stopped
	 */
	public boolean stop() {
		return nextAction == org.apache.commons.math3.ode.events.EventHandler.Action.STOP;
	}

	/**
	 * Let the event handler reset the state if it wants.
	 *
	 * @param t
	 * 		value of the independent <i>time</i> variable at the
	 * 		beginning of the next step
	 * @param y
	 * 		array were to put the desired state vector at the beginning
	 * 		of the next step
	 * @return true if the integrator should reset the derivatives too
	 */
	public boolean reset(final double t, final double[] y) {
		if (!(pendingEvent && (org.apache.commons.math3.util.FastMath.abs(pendingEventTime - t) <= convergence))) {
			return false;
		}
		if (nextAction == org.apache.commons.math3.ode.events.EventHandler.Action.RESET_STATE) {
			handler.resetState(t, y);
		}
		pendingEvent = false;
		pendingEventTime = java.lang.Double.NaN;
		return (nextAction == org.apache.commons.math3.ode.events.EventHandler.Action.RESET_STATE) || (nextAction == org.apache.commons.math3.ode.events.EventHandler.Action.RESET_DERIVATIVES);
	}

	/**
	 * Local wrapper to propagate exceptions.
	 */
	private static class LocalMaxCountExceededException extends java.lang.RuntimeException {
		/**
		 * Serializable UID.
		 */
		private static final long serialVersionUID = 20120901L;

		/**
		 * Wrapped exception.
		 */
		private final org.apache.commons.math3.exception.MaxCountExceededException wrapped;

		/**
		 * Simple constructor.
		 *
		 * @param exception
		 * 		exception to wrap
		 */
		public LocalMaxCountExceededException(final org.apache.commons.math3.exception.MaxCountExceededException exception) {
			wrapped = exception;
		}

		/**
		 * Get the wrapped exception.
		 *
		 * @return wrapped exception
		 */
		public org.apache.commons.math3.exception.MaxCountExceededException getException() {
			return wrapped;
		}
	}
}