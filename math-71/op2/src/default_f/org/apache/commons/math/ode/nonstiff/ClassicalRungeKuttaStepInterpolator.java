package org.apache.commons.math.ode.nonstiff;
/**
 * This class implements a step interpolator for the classical fourth
 * order Runge-Kutta integrator.
 *
 * <p>This interpolator allows to compute dense output inside the last
 * step computed. The interpolation equation is consistent with the
 * integration scheme :
 *
 * <pre>
 *   y(t_n + theta h) = y (t_n + h)
 *                    + (1 - theta) (h/6) [ (-4 theta^2 + 5 theta - 1) y'_1
 *                                          +(4 theta^2 - 2 theta - 2) (y'_2 + y'_3)
 *                                          -(4 theta^2 +   theta + 1) y'_4
 *                                        ]
 * </pre>
 *
 * where theta belongs to [0 ; 1] and where y'_1 to y'_4 are the four
 * evaluations of the derivatives already computed during the
 * step.</p>
 *
 * @see ClassicalRungeKuttaIntegrator
 * @version $Revision$ $Date$
 * @since 1.2
 */
class ClassicalRungeKuttaStepInterpolator extends org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator {
	/**
	 * Serializable version identifier
	 */
	private static final long serialVersionUID = -6576285612589783992L;

	/**
	 * Simple constructor.
	 * This constructor builds an instance that is not usable yet, the
	 * {@link RungeKuttaStepInterpolator#reinitialize} method should be
	 * called before using the instance in order to initialize the
	 * internal arrays. This constructor is used only in order to delay
	 * the initialization in some cases. The {@link RungeKuttaIntegrator}
	 * class uses the prototyping design pattern to create the step
	 * interpolators by cloning an uninitialized model and latter initializing
	 * the copy.
	 */
	public ClassicalRungeKuttaStepInterpolator() {
	}

	/**
	 * Copy constructor.
	 *
	 * @param interpolator
	 * 		interpolator to copy from. The copy is a deep
	 * 		copy: its arrays are separated from the original arrays of the
	 * 		instance
	 */
	public ClassicalRungeKuttaStepInterpolator(final org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaStepInterpolator interpolator) {
		super(interpolator);
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	protected org.apache.commons.math.ode.sampling.StepInterpolator doCopy() {
		return new org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaStepInterpolator(this);
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	protected void computeInterpolatedStateAndDerivatives(final double theta, final double oneMinusThetaH) throws org.apache.commons.math.ode.DerivativeException {
		final double fourTheta = 4 * theta;
		final double oneMinusTheta = 1 - theta;
		final double oneMinus2Theta = 1 - (2 * theta);
		final double s = oneMinusThetaH / 6.0;
		final double coeff1 = s * ((((-fourTheta) + 5) * theta) - 1);
		final double coeff23 = s * (((fourTheta - 2) * theta) - 2);
		final double coeff4 = s * ((((-fourTheta) - 1) * theta) - 1);
		final double coeffDot1 = oneMinusTheta * oneMinus2Theta;
		final double coeffDot23 = (2 * theta) * oneMinusTheta;
		final double coeffDot4 = (-theta) * oneMinus2Theta;
		for (int i = 0; i < interpolatedState.length; ++i) {
			final double yDot1 = yDotK[0][i];
			final double yDot23 = yDotK[1][i] + yDotK[2][i];
			final double yDot4 = yDotK[3][i];
			interpolatedState[i] = ((currentState[i] + (coeff1 * yDot1)) + (coeff23 * yDot23)) + (coeff4 * yDot4);
			interpolatedDerivatives[i] = ((coeffDot1 * yDot1) + (coeffDot23 * yDot23)) + (coeffDot4 * yDot4);
		}
	}
}