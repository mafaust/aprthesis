package org.apache.commons.math.optimization.linear;
/**
 * A linear constraint for a linear optimization problem.
 * <p>
 * A linear constraint has one of the forms:
 * <ul>
 *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> = v</li>
 *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> &lt;= v</li>
 *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> >= v</li>
 *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> =
 *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
 *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> &lt;=
 *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
 *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> >=
 *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
 * </ul>
 * The c<sub>i</sub>, l<sub>i</sub> or r<sub>i</sub> are the coefficients of the constraints, the x<sub>i</sub>
 * are the coordinates of the current point and v is the value of the constraint.
 * </p>
 *
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class LinearConstraint implements java.io.Serializable {
	/**
	 * Serializable version identifier.
	 */
	private static final long serialVersionUID = -764632794033034092L;

	/**
	 * Coefficients of the constraint (left hand side).
	 */
	private transient final org.apache.commons.math.linear.RealVector coefficients;

	/**
	 * Relationship between left and right hand sides (=, &lt;=, >=).
	 */
	private final org.apache.commons.math.optimization.linear.Relationship relationship;

	/**
	 * Value of the constraint (right hand side).
	 */
	private final double value;

	/**
	 * Build a constraint involving a single linear equation.
	 * <p>
	 * A linear constraint with a single linear equation has one of the forms:
	 * <ul>
	 *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> = v</li>
	 *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> &lt;= v</li>
	 *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> >= v</li>
	 * </ul>
	 * </p>
	 *
	 * @param coefficients
	 * 		The coefficients of the constraint (left hand side)
	 * @param relationship
	 * 		The type of (in)equality used in the constraint
	 * @param value
	 * 		The value of the constraint (right hand side)
	 */
	public LinearConstraint(final double[] coefficients, final org.apache.commons.math.optimization.linear.Relationship relationship, final double value) {
		this(new org.apache.commons.math.linear.ArrayRealVector(coefficients), relationship, value);
	}

	/**
	 * Build a constraint involving a single linear equation.
	 * <p>
	 * A linear constraint with a single linear equation has one of the forms:
	 * <ul>
	 *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> = v</li>
	 *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> &lt;= v</li>
	 *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> >= v</li>
	 * </ul>
	 * </p>
	 *
	 * @param coefficients
	 * 		The coefficients of the constraint (left hand side)
	 * @param relationship
	 * 		The type of (in)equality used in the constraint
	 * @param value
	 * 		The value of the constraint (right hand side)
	 */
	public LinearConstraint(final org.apache.commons.math.linear.RealVector coefficients, final org.apache.commons.math.optimization.linear.Relationship relationship, final double value) {
		this.coefficients = coefficients;
		this.relationship = relationship;
		this.value = value;
	}

	/**
	 * Build a constraint involving two linear equations.
	 * <p>
	 * A linear constraint with two linear equation has one of the forms:
	 * <ul>
	 *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> =
	 *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
	 *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> &lt;=
	 *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
	 *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> >=
	 *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
	 * </ul>
	 * </p>
	 *
	 * @param lhsCoefficients
	 * 		The coefficients of the linear expression on the left hand side of the constraint
	 * @param lhsConstant
	 * 		The constant term of the linear expression on the left hand side of the constraint
	 * @param relationship
	 * 		The type of (in)equality used in the constraint
	 * @param rhsCoefficients
	 * 		The coefficients of the linear expression on the right hand side of the constraint
	 * @param rhsConstant
	 * 		The constant term of the linear expression on the right hand side of the constraint
	 */
	public LinearConstraint(final double[] lhsCoefficients, final double lhsConstant, final org.apache.commons.math.optimization.linear.Relationship relationship, final double[] rhsCoefficients, final double rhsConstant) {
		double[] sub = new double[lhsCoefficients.length];
		for (int i = 0; i < sub.length; ++i) {
			sub[i] = lhsCoefficients[i] - rhsCoefficients[i];
		}
		this.coefficients = new org.apache.commons.math.linear.ArrayRealVector(sub, false);
		this.relationship = relationship;
		this.value = rhsConstant - lhsConstant;
	}

	/**
	 * Build a constraint involving two linear equations.
	 * <p>
	 * A linear constraint with two linear equation has one of the forms:
	 * <ul>
	 *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> =
	 *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
	 *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> &lt;=
	 *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
	 *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> >=
	 *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
	 * </ul>
	 * </p>
	 *
	 * @param lhsCoefficients
	 * 		The coefficients of the linear expression on the left hand side of the constraint
	 * @param lhsConstant
	 * 		The constant term of the linear expression on the left hand side of the constraint
	 * @param relationship
	 * 		The type of (in)equality used in the constraint
	 * @param rhsCoefficients
	 * 		The coefficients of the linear expression on the right hand side of the constraint
	 * @param rhsConstant
	 * 		The constant term of the linear expression on the right hand side of the constraint
	 */
	public LinearConstraint(final org.apache.commons.math.linear.RealVector lhsCoefficients, final double lhsConstant, final org.apache.commons.math.optimization.linear.Relationship relationship, final org.apache.commons.math.linear.RealVector rhsCoefficients, final double rhsConstant) {
		this.coefficients = lhsCoefficients.subtract(rhsCoefficients);
		this.relationship = relationship;
		this.value = rhsConstant - lhsConstant;
	}

	/**
	 * Get the coefficients of the constraint (left hand side).
	 *
	 * @return coefficients of the constraint (left hand side)
	 */
	public org.apache.commons.math.linear.RealVector getCoefficients() {
		return coefficients;
	}

	/**
	 * Get the relationship between left and right hand sides.
	 *
	 * @return relationship between left and right hand sides
	 */
	public org.apache.commons.math.optimization.linear.Relationship getRelationship() {
		return relationship;
	}

	/**
	 * Get the value of the constraint (right hand side).
	 *
	 * @return value of the constraint (right hand side)
	 */
	public double getValue() {
		return value;
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	public boolean equals(java.lang.Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		try {
			org.apache.commons.math.optimization.linear.LinearConstraint rhs = ((org.apache.commons.math.optimization.linear.LinearConstraint) (other));
			return ((relationship == rhs.relationship) && (value == rhs.value)) && coefficients.equals(rhs.coefficients);
		} catch (java.lang.ClassCastException ex) {
			// ignore exception
			return false;
		}
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	public int hashCode() {
		return (relationship.hashCode() ^ java.lang.Double.valueOf(value).hashCode()) ^ coefficients.hashCode();
	}

	/**
	 * Serialize the instance.
	 *
	 * @param oos
	 * 		stream where object should be written
	 * @throws IOException
	 * 		if object cannot be written to stream
	 */
	private void writeObject(java.io.ObjectOutputStream oos) throws java.io.IOException {
		oos.defaultWriteObject();
		org.apache.commons.math.linear.MatrixUtils.serializeRealVector(coefficients, oos);
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
	 */
	private void readObject(java.io.ObjectInputStream ois) throws java.lang.ClassNotFoundException, java.io.IOException {
		ois.defaultReadObject();
		org.apache.commons.math.linear.MatrixUtils.deserializeRealVector(this, "coefficients", ois);
	}
}