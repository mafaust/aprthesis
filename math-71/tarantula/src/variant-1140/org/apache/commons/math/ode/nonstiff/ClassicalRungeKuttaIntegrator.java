package org.apache.commons.math.ode.nonstiff;



















/**
 * This class implements the classical fourth order Runge-Kutta
 * integrator for Ordinary Differential Equations (it is the most
 * often used Runge-Kutta method).
 *
 * <p>This method is an explicit Runge-Kutta method, its Butcher-array
 * is the following one :
 * <pre>
 *    0  |  0    0    0    0
 *   1/2 | 1/2   0    0    0
 *   1/2 |  0   1/2   0    0
 *    1  |  0    0    1    0
 *       |--------------------
 *       | 1/6  1/3  1/3  1/6
 * </pre>
 * </p>
 *
 * @see EulerIntegrator
 * @see GillIntegrator
 * @see MidpointIntegrator
 * @see ThreeEighthesIntegrator
 * @version $Revision$ $Date$
 * @since 1.2
 */
public class ClassicalRungeKuttaIntegrator extends 
org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator {

	/**
	 * Time steps Butcher array.
	 */ 	private static final double[] STATIC_C = new double[]{ 1.0 / 2.0, 1.0 / 2.0, 1.0 };


	/**
	 * Internal weights Butcher array.
	 */ 	private static final double[][] STATIC_A = new double[][]{ new double[]{ 1.0 / 2.0 }, 
	new double[]{ 0.0, 1.0 / 2.0 }, 
	new double[]{ 0.0, 0.0, 1.0 } };


	/**
	 * Propagation weights Butcher array.
	 */ 	private static final double[] STATIC_B = new double[]{ 1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0 };


	/**
	 * Simple constructor.
	 * Build a fourth-order Runge-Kutta integrator with the given
	 * step.
	 *
	 * @param step
	 * 		integration step
	 */ 	public ClassicalRungeKuttaIntegrator(final double step) { 		super("classical Runge-Kutta", org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaIntegrator.STATIC_C, org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaIntegrator.STATIC_A, org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaIntegrator.STATIC_B, new org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaStepInterpolator(), step);
	}}