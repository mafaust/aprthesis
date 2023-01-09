package org.apache.commons.math.optimization.linear;



























/**
 * An objective function for a linear optimization problem.
 * <p>
 * A linear objective function has one the form:
 * <pre>
 * c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> + d
 * </pre>
 * The c<sub>i</sub> and d are the coefficients of the equation,
 * the x<sub>i</sub> are the coordinates of the current point.
 * </p>
 *
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class LinearObjectiveFunction implements java.io.Serializable {
	/**
	 * Serializable version identifier.
	 */ 	private static final long serialVersionUID = -4531815507568396090L;
	/**
	 * Coefficients of the constraint (c<sub>i</sub>).
	 */ 	private transient final org.apache.commons.math.linear.RealVector coefficients;
	/**
	 * Constant term of the linear equation.
	 */ 	private final double constantTerm;
	/**
	 *
	 *
	 * @param coefficients
	 * 		The coefficients for the linear equation being optimized
	 * @param constantTerm
	 * 		The constant term of the linear equation
	 */ 	public LinearObjectiveFunction(double[] coefficients, double constantTerm) { 		this(new org.apache.commons.math.linear.ArrayRealVector(coefficients), constantTerm);}
	/**
	 *
	 *
	 * @param coefficients
	 * 		The coefficients for the linear equation being optimized
	 * @param constantTerm
	 * 		The constant term of the linear equation
	 */ 	public LinearObjectiveFunction(org.apache.commons.math.linear.RealVector coefficients, double constantTerm) { 		this.coefficients = coefficients; 		this.constantTerm = constantTerm;}

	/**
	 * Get the coefficients of the linear equation being optimized.
	 *
	 * @return coefficients of the linear equation being optimized
	 */ 	public org.apache.commons.math.linear.RealVector getCoefficients() {
		return coefficients;
	}

	/**
	 * Get the constant of the linear equation being optimized.
	 *
	 * @return constant of the linear equation being optimized
	 */ 	public double getConstantTerm() {
		return constantTerm;
	}

	/**
	 * Compute the value of the linear equation at the current point
	 *
	 * @param point
	 * 		point at which linear equation must be evaluated
	 * @return value of the linear equation at the current point
	 */ 	public double getValue(final double[] point) { 		return coefficients.dotProduct(point) + constantTerm;
	}

	/**
	 * Compute the value of the linear equation at the current point
	 *
	 * @param point
	 * 		point at which linear equation must be evaluated
	 * @return value of the linear equation at the current point
	 */ 	public double getValue(final org.apache.commons.math.linear.RealVector point) { 		return coefficients.dotProduct(point) + constantTerm;
	}

	/**
	 * {@inheritDoc }
	 */ 	@java.lang.Override 	public boolean equals(java.lang.Object other) {

		if (this == other) {
			return true;
		}

		if (other == null) {
			return false;
		}

		try {

			org.apache.commons.math.optimization.linear.LinearObjectiveFunction rhs = ((org.apache.commons.math.optimization.linear.LinearObjectiveFunction) (other));
			return (constantTerm == rhs.constantTerm) && coefficients.equals(rhs.coefficients);

		} catch (java.lang.ClassCastException ex) {
			// ignore exception
			return false;
		}

	}

	/**
	 * {@inheritDoc }
	 */ 	@java.lang.Override 	public int hashCode() {
		return java.lang.Double.valueOf(constantTerm).hashCode() ^ coefficients.hashCode();
	}

	/**
	 * Serialize the instance.
	 *
	 * @param oos
	 * 		stream where object should be written
	 * @throws IOException
	 * 		if object cannot be written to stream
	 */ 	private void writeObject(java.io.ObjectOutputStream oos) throws java.io.IOException { 		oos.defaultWriteObject(); 		org.apache.commons.math.linear.MatrixUtils.serializeRealVector(coefficients, oos);
	}

	/**
	 * Deserialize the instance.
	 *
	 * @param ois
	 * 		stream from which the object should be read
	 * @throws ClassNotFoundException
	 * 		if a class in the stream cannot be found
	 * @throws IOException
	 * 		if object cannot be read from the stream
	 */ 	private void readObject(java.io.ObjectInputStream ois) throws java.lang.ClassNotFoundException, java.io.IOException { 		ois.defaultReadObject(); 		org.apache.commons.math.linear.MatrixUtils.deserializeRealVector(this, "coefficients", ois);}}