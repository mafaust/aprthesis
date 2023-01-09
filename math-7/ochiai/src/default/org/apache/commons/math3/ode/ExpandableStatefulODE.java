package org.apache.commons.math3.ode;
























/**
 * This class represents a combined set of first order differential equations,
 * with at least a primary set of equations expandable by some sets of secondary
 * equations.
 * <p>
 * One typical use case is the computation of the Jacobian matrix for some ODE.
 * In this case, the primary set of equations corresponds to the raw ODE, and we
 * add to this set another bunch of secondary equations which represent the Jacobian
 * matrix of the primary set.
 * </p>
 * <p>
 * We want the integrator to use <em>only</em> the primary set to estimate the
 * errors and hence the step sizes. It should <em>not</em> use the secondary
 * equations in this computation. The {@link AbstractIntegrator integrator} will
 * be able to know where the primary set ends and so where the secondary sets begin.
 * </p>
 *
 * @see FirstOrderDifferentialEquations
 * @see JacobianMatrices
 * @version $Id$
 * @since 3.0
 */
public class ExpandableStatefulODE {



	/**
	 * Primary differential equation.
	 */ 	private final org.apache.commons.math3.ode.FirstOrderDifferentialEquations primary;
	/**
	 * Mapper for primary equation.
	 */ 	private final org.apache.commons.math3.ode.EquationsMapper primaryMapper;
	/**
	 * Time.
	 */ 	private double time;
	/**
	 * State.
	 */ 	private final double[] primaryState;
	/**
	 * State derivative.
	 */ 	private final double[] primaryStateDot;
	/**
	 * Components of the expandable ODE.
	 */ 	private java.util.List<org.apache.commons.math3.ode.ExpandableStatefulODE.SecondaryComponent> components;
	/**
	 * Build an expandable set from its primary ODE set.
	 *
	 * @param primary
	 * 		the primary set of differential equations to be integrated.
	 */ 	public ExpandableStatefulODE(final org.apache.commons.math3.ode.FirstOrderDifferentialEquations primary) { 		final int n = primary.getDimension(); 		this.primary = primary;
		this.primaryMapper = new org.apache.commons.math3.ode.EquationsMapper(0, n);
		this.time = java.lang.Double.NaN;
		this.primaryState = new double[n];
		this.primaryStateDot = new double[n];
		this.components = new java.util.ArrayList<org.apache.commons.math3.ode.ExpandableStatefulODE.SecondaryComponent>();
	}

	/**
	 * Get the primary set of differential equations.
	 *
	 * @return primary set of differential equations
	 */ 	public org.apache.commons.math3.ode.FirstOrderDifferentialEquations getPrimary() { 		return primary;
	}

	/**
	 * Return the dimension of the complete set of equations.
	 * <p>
	 * The complete set of equations correspond to the primary set plus all secondary sets.
	 * </p>
	 *
	 * @return dimension of the complete set of equations
	 */ 	public int getTotalDimension() { 		if (components.isEmpty()) {
			// there are no secondary equations, the complete set is limited to the primary set
			return primaryMapper.getDimension();
		} else {
			// there are secondary equations, the complete set ends after the last set
			final org.apache.commons.math3.ode.EquationsMapper lastMapper = components.get(components.size() - 1).mapper;
			return lastMapper.getFirstIndex() + lastMapper.getDimension();
		}
	}

	/**
	 * Get the current time derivative of the complete state vector.
	 *
	 * @param t
	 * 		current value of the independent <I>time</I> variable
	 * @param y
	 * 		array containing the current value of the complete state vector
	 * @param yDot
	 * 		placeholder array where to put the time derivative of the complete state vector
	 * @exception MaxCountExceededException
	 * 		if the number of functions evaluations is exceeded
	 * @exception DimensionMismatchException
	 * 		if arrays dimensions do not match equations settings
	 */ 	public void computeDerivatives(final double t, final double[] y, final double[] yDot) throws org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.DimensionMismatchException { 		// compute derivatives of the primary equations 		primaryMapper.extractEquationData(y, primaryState); 		primary.computeDerivatives(t, primaryState, primaryStateDot); 		primaryMapper.insertEquationData(primaryStateDot, yDot);

		// Add contribution for secondary equations
		for (final org.apache.commons.math3.ode.ExpandableStatefulODE.SecondaryComponent component : components) {
			component.mapper.extractEquationData(y, component.state);
			component.equation.computeDerivatives(t, primaryState, primaryStateDot, 
			component.state, component.stateDot);
			component.mapper.insertEquationData(component.stateDot, yDot);
		}

	}

	/**
	 * Add a set of secondary equations to be integrated along with the primary set.
	 *
	 * @param secondary
	 * 		secondary equations set
	 * @return index of the secondary equation in the expanded state
	 */ 	public int addSecondaryEquations(final org.apache.commons.math3.ode.SecondaryEquations secondary) { 		final int firstIndex;
		if (components.isEmpty()) {
			// lazy creation of the components list
			components = new java.util.ArrayList<org.apache.commons.math3.ode.ExpandableStatefulODE.SecondaryComponent>();
			firstIndex = primary.getDimension();
		} else {
			final org.apache.commons.math3.ode.ExpandableStatefulODE.SecondaryComponent last = components.get(components.size() - 1);
			firstIndex = last.mapper.getFirstIndex() + last.mapper.getDimension();
		}

		components.add(new org.apache.commons.math3.ode.ExpandableStatefulODE.SecondaryComponent(secondary, firstIndex));

		return components.size() - 1;

	}

	/**
	 * Get an equations mapper for the primary equations set.
	 *
	 * @return mapper for the primary set
	 * @see #getSecondaryMappers()
	 */ 	public org.apache.commons.math3.ode.EquationsMapper getPrimaryMapper() { 		return primaryMapper;
	}

	/**
	 * Get the equations mappers for the secondary equations sets.
	 *
	 * @return equations mappers for the secondary equations sets
	 * @see #getPrimaryMapper()
	 */ 	public org.apache.commons.math3.ode.EquationsMapper[] getSecondaryMappers() { 		final org.apache.commons.math3.ode.EquationsMapper[] mappers = new org.apache.commons.math3.ode.EquationsMapper[components.size()];
		for (int i = 0; i < mappers.length; ++i) {
			mappers[i] = components.get(i).mapper;
		}
		return mappers;
	}

	/**
	 * Set current time.
	 *
	 * @param time
	 * 		current time
	 */ 	public void setTime(final double time) { 		this.time = time;}

	/**
	 * Get current time.
	 *
	 * @return current time
	 */ 	public double getTime() { 		return time;
	}

	/**
	 * Set primary part of the current state.
	 *
	 * @param primaryState
	 * 		primary part of the current state
	 * @throws DimensionMismatchException
	 * 		if the dimension of the array does not
	 * 		match the primary set
	 */ 	public void setPrimaryState(final double[] primaryState) throws org.apache.commons.math3.exception.DimensionMismatchException { 		// safety checks
		if (primaryState.length != this.primaryState.length) { 			throw new org.apache.commons.math3.exception.DimensionMismatchException(primaryState.length, this.primaryState.length);
		}

		// set the data
		java.lang.System.arraycopy(primaryState, 0, this.primaryState, 0, primaryState.length);

	}

	/**
	 * Get primary part of the current state.
	 *
	 * @return primary part of the current state
	 */ 	public double[] getPrimaryState() { 		return primaryState.clone();
	}

	/**
	 * Get primary part of the current state derivative.
	 *
	 * @return primary part of the current state derivative
	 */ 	public double[] getPrimaryStateDot() { 		return primaryStateDot.clone();
	}

	/**
	 * Set secondary part of the current state.
	 *
	 * @param index
	 * 		index of the part to set as returned by {@link #addSecondaryEquations(SecondaryEquations)}
	 * @param secondaryState
	 * 		secondary part of the current state
	 * @throws DimensionMismatchException
	 * 		if the dimension of the partial state does not
	 * 		match the selected equations set dimension
	 */ 	public void setSecondaryState(final int index, final double[] secondaryState) throws org.apache.commons.math3.exception.DimensionMismatchException { 		// get either the secondary state
		double[] localArray = components.get(index).state;

		// safety checks
		if (secondaryState.length != localArray.length) {
			throw new org.apache.commons.math3.exception.DimensionMismatchException(secondaryState.length, localArray.length);
		}

		// set the data
		java.lang.System.arraycopy(secondaryState, 0, localArray, 0, secondaryState.length);

	}

	/**
	 * Get secondary part of the current state.
	 *
	 * @param index
	 * 		index of the part to set as returned by {@link #addSecondaryEquations(SecondaryEquations)}
	 * @return secondary part of the current state
	 */ 	public double[] getSecondaryState(final int index) { 		return components.get(index).state.clone();
	}

	/**
	 * Get secondary part of the current state derivative.
	 *
	 * @param index
	 * 		index of the part to set as returned by {@link #addSecondaryEquations(SecondaryEquations)}
	 * @return secondary part of the current state derivative
	 */ 	public double[] getSecondaryStateDot(final int index) { 		return components.get(index).stateDot.clone();
	}

	/**
	 * Set the complete current state.
	 *
	 * @param completeState
	 * 		complete current state to copy data from
	 * @throws DimensionMismatchException
	 * 		if the dimension of the complete state does not
	 * 		match the complete equations sets dimension
	 */ 	public void setCompleteState(final double[] completeState) throws org.apache.commons.math3.exception.DimensionMismatchException { 		// safety checks
		if (completeState.length != getTotalDimension()) {
			throw new org.apache.commons.math3.exception.DimensionMismatchException(completeState.length, getTotalDimension());
		}

		// set the data
		primaryMapper.extractEquationData(completeState, primaryState);
		for (final org.apache.commons.math3.ode.ExpandableStatefulODE.SecondaryComponent component : components) {
			component.mapper.extractEquationData(completeState, component.state);
		}

	}

	/**
	 * Get the complete current state.
	 *
	 * @return complete current state
	 * @throws DimensionMismatchException
	 * 		if the dimension of the complete state does not
	 * 		match the complete equations sets dimension
	 */ 	public double[] getCompleteState() throws org.apache.commons.math3.exception.DimensionMismatchException { 		// allocate complete array
		double[] completeState = new double[getTotalDimension()];

		// set the data
		primaryMapper.insertEquationData(primaryState, completeState);
		for (final org.apache.commons.math3.ode.ExpandableStatefulODE.SecondaryComponent component : components) {
			component.mapper.insertEquationData(component.state, completeState);
		}

		return completeState;

	}

	/**
	 * Components of the compound stateful ODE.
	 */ 	private static class SecondaryComponent {
		/**
		 * Secondary differential equation.
		 */ 		private final org.apache.commons.math3.ode.SecondaryEquations equation;
		/**
		 * Mapper between local and complete arrays.
		 */ 		private final org.apache.commons.math3.ode.EquationsMapper mapper;
		/**
		 * State.
		 */ 		private final double[] state;
		/**
		 * State derivative.
		 */ 		private final double[] stateDot;
		/**
		 * Simple constructor.
		 *
		 * @param equation
		 * 		secondary differential equation
		 * @param firstIndex
		 * 		index to use for the first element in the complete arrays
		 */ 		public SecondaryComponent(final org.apache.commons.math3.ode.SecondaryEquations equation, final int firstIndex) { 			final int n = equation.getDimension(); 			this.equation = equation; 			mapper = new org.apache.commons.math3.ode.EquationsMapper(firstIndex, n);
			state = new double[n];
			stateDot = new double[n];
		}

	}}