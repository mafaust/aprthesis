package org.apache.commons.math.linear;























/**
 * This class implements the {@link RealVector} interface with a double array.
 *
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class ArrayRealVector implements org.apache.commons.math.linear.RealVector , java.io.Serializable {
	/**
	 * Serializable version identifier.
	 */ 	private static final long serialVersionUID = -1097961340710804027L;
	/**
	 * Default format.
	 */ 	private static final org.apache.commons.math.linear.RealVectorFormat DEFAULT_FORMAT = org.apache.commons.math.linear.RealVectorFormat.getInstance();

	/**
	 * Entries of the vector.
	 */ 	protected double[] data;
	/**
	 * Build a 0-length vector.
	 * <p>Zero-length vectors may be used to initialized construction of vectors
	 * by data gathering. We start with zero-length and use either the {@link #ArrayRealVector(ArrayRealVector, ArrayRealVector)} constructor
	 * or one of the <code>append</code> method ({@link #append(double)}, {@link #append(double[])}, {@link #append(ArrayRealVector)}) to gather data
	 * into this vector.</p>
	 */


	public ArrayRealVector() {
		data = new double[0];
	}

	/**
	 * Construct a (size)-length vector of zeros.
	 *
	 * @param size
	 * 		size of the vector
	 */ 	public ArrayRealVector(int size) { 		data = new double[size];
	}

	/**
	 * Construct an (size)-length vector with preset values.
	 *
	 * @param size
	 * 		size of the vector
	 * @param preset
	 * 		fill the vector with this scalar value
	 */ 	public ArrayRealVector(int size, double preset) { 		data = new double[size]; 		java.util.Arrays.fill(data, preset);
	}

	/**
	 * Construct a vector from an array, copying the input array.
	 *
	 * @param d
	 * 		array of doubles.
	 */ 	public ArrayRealVector(double[] d) { 		data = d.clone();
	}

	/**
	 * Create a new ArrayRealVector using the input array as the underlying
	 * data array.
	 * <p>If an array is built specially in order to be embedded in a
	 * ArrayRealVector and not used directly, the <code>copyArray</code> may be
	 * set to <code>false</code. This will prevent the copying and improve
	 * performance as no new array will be built and no data will be copied.</p>
	 *
	 * @param d
	 * 		data for new vector
	 * @param copyArray
	 * 		if true, the input array will be copied, otherwise
	 * 		it will be referenced
	 * @throws IllegalArgumentException
	 * 		if <code>d</code> is empty
	 * @throws NullPointerException
	 * 		if <code>d</code> is null
	 * @see #ArrayRealVector(double[])
	 */ 	public ArrayRealVector(double[] d, boolean copyArray) throws java.lang.NullPointerException, java.lang.IllegalArgumentException { 		if (d == null) { 			throw new java.lang.NullPointerException();}
		if (d.length == 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("vector must have at least one element");
		}
		data = (copyArray) ? d.clone() : d;
	}

	/**
	 * Construct a vector from part of a array.
	 *
	 * @param d
	 * 		array of doubles.
	 * @param pos
	 * 		position of first entry
	 * @param size
	 * 		number of entries to copy
	 */ 	public ArrayRealVector(double[] d, int pos, int size) { 		if (d.length < (pos + size)) { 			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("position {0} and size {1} don't fit to the size of the input array {2}", 
			pos, size, d.length);
		}
		data = new double[size];
		java.lang.System.arraycopy(d, pos, data, 0, size);
	}

	/**
	 * Construct a vector from an array.
	 *
	 * @param d
	 * 		array of Doubles.
	 */ 	public ArrayRealVector(java.lang.Double[] d) { 		data = new double[d.length];
		for (int i = 0; i < d.length; i++) {
			data[i] = d[i].doubleValue();
		}
	}

	/**
	 * Construct a vector from part of a Double array
	 *
	 * @param d
	 * 		array of Doubles.
	 * @param pos
	 * 		position of first entry
	 * @param size
	 * 		number of entries to copy
	 */ 	public ArrayRealVector(java.lang.Double[] d, int pos, int size) { 		if (d.length < (pos + size)) { 			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("position {0} and size {1} don't fit to the size of the input array {2}", 
			pos, size, d.length);
		}
		data = new double[size];
		for (int i = pos; i < (pos + size); i++) {
			data[i - pos] = d[i].doubleValue();
		}
	}

	/**
	 * Construct a vector from another vector, using a deep copy.
	 *
	 * @param v
	 * 		vector to copy
	 */ 	public ArrayRealVector(org.apache.commons.math.linear.RealVector v) { 		data = new double[v.getDimension()];
		for (int i = 0; i < data.length; ++i) {
			data[i] = v.getEntry(i);
		}
	}

	/**
	 * Construct a vector from another vector, using a deep copy.
	 *
	 * @param v
	 * 		vector to copy
	 */ 	public ArrayRealVector(org.apache.commons.math.linear.ArrayRealVector v) { 		data = v.data.clone();
	}

	/**
	 * Construct a vector from another vector.
	 *
	 * @param v
	 * 		vector to copy
	 * @param deep
	 * 		if true perform a deep copy otherwise perform a shallow copy
	 */ 	public ArrayRealVector(org.apache.commons.math.linear.ArrayRealVector v, boolean deep) { 		data = (deep) ? v.data.clone() : v.data;}

	/**
	 * Construct a vector by appending one vector to another vector.
	 *
	 * @param v1
	 * 		first vector (will be put in front of the new vector)
	 * @param v2
	 * 		second vector (will be put at back of the new vector)
	 */ 	public ArrayRealVector(org.apache.commons.math.linear.ArrayRealVector v1, org.apache.commons.math.linear.ArrayRealVector v2) { 		data = new double[v1.data.length + v2.data.length]; 		java.lang.System.arraycopy(v1.data, 0, data, 0, v1.data.length);
		java.lang.System.arraycopy(v2.data, 0, data, v1.data.length, v2.data.length);
	}

	/**
	 * Construct a vector by appending one vector to another vector.
	 *
	 * @param v1
	 * 		first vector (will be put in front of the new vector)
	 * @param v2
	 * 		second vector (will be put at back of the new vector)
	 */ 	public ArrayRealVector(org.apache.commons.math.linear.ArrayRealVector v1, double[] v2) { 		data = new double[v1.data.length + v2.length]; 		java.lang.System.arraycopy(v1.data, 0, data, 0, v1.data.length);
		java.lang.System.arraycopy(v2, 0, data, v1.data.length, v2.length);
	}

	/**
	 * Construct a vector by appending one vector to another vector.
	 *
	 * @param v1
	 * 		first vector (will be put in front of the new vector)
	 * @param v2
	 * 		second vector (will be put at back of the new vector)
	 */ 	public ArrayRealVector(double[] v1, org.apache.commons.math.linear.ArrayRealVector v2) { 		data = new double[v1.length + v2.data.length]; 		java.lang.System.arraycopy(v1, 0, data, 0, v1.length);
		java.lang.System.arraycopy(v2.data, 0, data, v1.length, v2.data.length);
	}

	/**
	 * Construct a vector by appending one vector to another vector.
	 *
	 * @param v1
	 * 		first vector (will be put in front of the new vector)
	 * @param v2
	 * 		second vector (will be put at back of the new vector)
	 */ 	public ArrayRealVector(double[] v1, double[] v2) { 		data = new double[v1.length + v2.length]; 		java.lang.System.arraycopy(v1, 0, data, 0, v1.length);
		java.lang.System.arraycopy(v2, 0, data, v1.length, v2.length);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector copy() { 		return new org.apache.commons.math.linear.ArrayRealVector(this, true);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector add(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return add(((org.apache.commons.math.linear.ArrayRealVector) (v)));
		} catch (java.lang.ClassCastException cce) {
			checkVectorDimensions(v);
			double[] out = new double[data.length];
			for (int i = 0; i < data.length; i++) {
				out[i] = data[i] + v.getEntry(i);
			}
			return new org.apache.commons.math.linear.ArrayRealVector(out);
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector add(double[] v) throws java.lang.IllegalArgumentException {
		checkVectorDimensions(v.length);
		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = data[i] + v[i];
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * Compute the sum of this and v.
	 *
	 * @param v
	 * 		vector to be added
	 * @return this + v
	 * @throws IllegalArgumentException
	 * 		if v is not the same size as this
	 */ 	public org.apache.commons.math.linear.ArrayRealVector add(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException { 		return ((org.apache.commons.math.linear.ArrayRealVector) (add(v.data)));
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector subtract(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return subtract(((org.apache.commons.math.linear.ArrayRealVector) (v)));
		} catch (java.lang.ClassCastException cce) {
			checkVectorDimensions(v);
			double[] out = new double[data.length];
			for (int i = 0; i < data.length; i++) {
				out[i] = data[i] - v.getEntry(i);
			}
			return new org.apache.commons.math.linear.ArrayRealVector(out);
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector subtract(double[] v) throws java.lang.IllegalArgumentException {
		checkVectorDimensions(v.length);
		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = data[i] - v[i];
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * Compute this minus v.
	 *
	 * @param v
	 * 		vector to be subtracted
	 * @return this + v
	 * @throws IllegalArgumentException
	 * 		if v is not the same size as this
	 */ 	public org.apache.commons.math.linear.ArrayRealVector subtract(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException { 		return ((org.apache.commons.math.linear.ArrayRealVector) (subtract(v.data)));
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapAdd(double d) { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = data[i] + d;
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapAddToSelf(double d) { 		for (int i = 0; i < data.length; i++) {
			data[i] = data[i] + d;
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapSubtract(double d) { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = data[i] - d;
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapSubtractToSelf(double d) { 		for (int i = 0; i < data.length; i++) {
			data[i] = data[i] - d;
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapMultiply(double d) { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = data[i] * d;
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapMultiplyToSelf(double d) { 		for (int i = 0; i < data.length; i++) {
			data[i] = data[i] * d;
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapDivide(double d) { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = data[i] / d;
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapDivideToSelf(double d) { 		for (int i = 0; i < data.length; i++) {
			data[i] = data[i] / d;
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapPow(double d) { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.pow(data[i], d);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapPowToSelf(double d) { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.pow(data[i], d);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapExp() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.exp(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapExpToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.exp(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapExpm1() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.expm1(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapExpm1ToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.expm1(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapLog() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.log(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapLogToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.log(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapLog10() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.log10(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapLog10ToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.log10(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapLog1p() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.log1p(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapLog1pToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.log1p(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapCosh() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.cosh(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapCoshToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.cosh(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapSinh() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.sinh(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapSinhToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.sinh(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapTanh() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.tanh(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapTanhToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.tanh(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapCos() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.cos(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapCosToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.cos(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapSin() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.sin(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapSinToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.sin(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapTan() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.tan(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapTanToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.tan(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapAcos() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.acos(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapAcosToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.acos(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapAsin() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.asin(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapAsinToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.asin(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapAtan() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.atan(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapAtanToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.atan(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapInv() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = 1.0 / data[i];
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapInvToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = 1.0 / data[i];
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapAbs() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.abs(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapAbsToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.abs(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapSqrt() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.sqrt(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapSqrtToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.sqrt(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapCbrt() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.cbrt(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapCbrtToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.cbrt(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapCeil() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.ceil(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapCeilToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.ceil(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapFloor() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.floor(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapFloorToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.floor(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapRint() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.rint(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapRintToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.rint(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapSignum() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.signum(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapSignumToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.signum(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapUlp() { 		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = java.lang.Math.ulp(data[i]);
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector mapUlpToSelf() { 		for (int i = 0; i < data.length; i++) {
			data[i] = java.lang.Math.ulp(data[i]);
		}
		return this;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector ebeMultiply(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return ebeMultiply(((org.apache.commons.math.linear.ArrayRealVector) (v)));
		} catch (java.lang.ClassCastException cce) {
			checkVectorDimensions(v);
			double[] out = new double[data.length];
			for (int i = 0; i < data.length; i++) {
				out[i] = data[i] * v.getEntry(i);
			}
			return new org.apache.commons.math.linear.ArrayRealVector(out);
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector ebeMultiply(double[] v) throws java.lang.IllegalArgumentException {
		checkVectorDimensions(v.length);
		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = data[i] * v[i];
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * Element-by-element multiplication.
	 *
	 * @param v
	 * 		vector by which instance elements must be multiplied
	 * @return a vector containing this[i] * v[i] for all i
	 * @exception IllegalArgumentException
	 * 		if v is not the same size as this
	 */ 	public org.apache.commons.math.linear.ArrayRealVector ebeMultiply(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException { 		return ((org.apache.commons.math.linear.ArrayRealVector) (ebeMultiply(v.data)));
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector ebeDivide(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return ebeDivide(((org.apache.commons.math.linear.ArrayRealVector) (v)));
		} catch (java.lang.ClassCastException cce) {
			checkVectorDimensions(v);
			double[] out = new double[data.length];
			for (int i = 0; i < data.length; i++) {
				out[i] = data[i] / v.getEntry(i);
			}
			return new org.apache.commons.math.linear.ArrayRealVector(out);
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector ebeDivide(double[] v) throws java.lang.IllegalArgumentException {
		checkVectorDimensions(v.length);
		double[] out = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = data[i] / v[i];
		}
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * Element-by-element division.
	 *
	 * @param v
	 * 		vector by which instance elements must be divided
	 * @return a vector containing this[i] / v[i] for all i
	 * @throws IllegalArgumentException
	 * 		if v is not the same size as this
	 */ 	public org.apache.commons.math.linear.ArrayRealVector ebeDivide(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException { 		return ((org.apache.commons.math.linear.ArrayRealVector) (ebeDivide(v.data)));
	}

	/**
	 * {@inheritDoc }
	 */ 	public double[] getData() { 		return data.clone();
	}

	/**
	 * Returns a reference to the underlying data array.
	 * <p>Does not make a fresh copy of the underlying data.</p>
	 *
	 * @return array of entries
	 */ 	public double[] getDataRef() {
		return data;
	}

	/**
	 * {@inheritDoc }
	 */ 	public double dotProduct(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return dotProduct(((org.apache.commons.math.linear.ArrayRealVector) (v)));
		} catch (java.lang.ClassCastException cce) {
			checkVectorDimensions(v);
			double dot = 0;
			for (int i = 0; i < data.length; i++) {
				dot += data[i] * v.getEntry(i);
			}
			return dot;
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public double dotProduct(double[] v) throws java.lang.IllegalArgumentException {
		checkVectorDimensions(v.length);
		double dot = 0;
		for (int i = 0; i < data.length; i++) {
			dot += data[i] * v[i];
		}
		return dot;
	}

	/**
	 * Compute the dot product.
	 *
	 * @param v
	 * 		vector with which dot product should be computed
	 * @return the scalar dot product between instance and v
	 * @exception IllegalArgumentException
	 * 		if v is not the same size as this
	 */ 	public double dotProduct(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException { 		return dotProduct(v.data);
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getNorm() { 		double sum = 0;
		for (double a : data) {
			sum += a * a;
		}
		return java.lang.Math.sqrt(sum);
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getL1Norm() { 		double sum = 0;
		for (double a : data) {
			sum += java.lang.Math.abs(a);
		}
		return sum;
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getLInfNorm() { 		double max = 0;
		for (double a : data) {
			max += java.lang.Math.max(max, java.lang.Math.abs(a));
		}
		return max;
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getDistance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return getDistance(((org.apache.commons.math.linear.ArrayRealVector) (v)));
		} catch (java.lang.ClassCastException cce) {
			checkVectorDimensions(v);
			double sum = 0;
			for (int i = 0; i < data.length; ++i) {
				final double delta = data[i] - v.getEntry(i);
				sum += delta * delta;
			}
			return java.lang.Math.sqrt(sum);
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getDistance(double[] v) throws java.lang.IllegalArgumentException {
		checkVectorDimensions(v.length);
		double sum = 0;
		for (int i = 0; i < data.length; ++i) {
			final double delta = data[i] - v[i];
			sum += delta * delta;
		}
		return java.lang.Math.sqrt(sum);
	}

	/**
	 * Distance between two vectors.
	 * <p>This method computes the distance consistent with the
	 * L<sub>2</sub> norm, i.e. the square root of the sum of
	 * elements differences, or euclidian distance.</p>
	 *
	 * @param v
	 * 		vector to which distance is requested
	 * @return distance between two vectors.
	 * @exception IllegalArgumentException
	 * 		if v is not the same size as this
	 * @see #getDistance(RealVector)
	 * @see #getL1Distance(ArrayRealVector)
	 * @see #getLInfDistance(ArrayRealVector)
	 * @see #getNorm()
	 */ 	public double getDistance(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException { 		return getDistance(v.data);
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getL1Distance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return getL1Distance(((org.apache.commons.math.linear.ArrayRealVector) (v)));
		} catch (java.lang.ClassCastException cce) {
			checkVectorDimensions(v);
			double sum = 0;
			for (int i = 0; i < data.length; ++i) {
				final double delta = data[i] - v.getEntry(i);
				sum += java.lang.Math.abs(delta);
			}
			return sum;
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getL1Distance(double[] v) throws java.lang.IllegalArgumentException {
		checkVectorDimensions(v.length);
		double sum = 0;
		for (int i = 0; i < data.length; ++i) {
			final double delta = data[i] - v[i];
			sum += java.lang.Math.abs(delta);
		}
		return sum;
	}

	/**
	 * Distance between two vectors.
	 * <p>This method computes the distance consistent with
	 * L<sub>1</sub> norm, i.e. the sum of the absolute values of
	 * elements differences.</p>
	 *
	 * @param v
	 * 		vector to which distance is requested
	 * @return distance between two vectors.
	 * @exception IllegalArgumentException
	 * 		if v is not the same size as this
	 * @see #getDistance(RealVector)
	 * @see #getL1Distance(ArrayRealVector)
	 * @see #getLInfDistance(ArrayRealVector)
	 * @see #getNorm()
	 */ 	public double getL1Distance(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException { 		return getL1Distance(v.data);
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getLInfDistance(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return getLInfDistance(((org.apache.commons.math.linear.ArrayRealVector) (v)));
		} catch (java.lang.ClassCastException cce) {
			checkVectorDimensions(v);
			double max = 0;
			for (int i = 0; i < data.length; ++i) {
				final double delta = data[i] - v.getEntry(i);
				max = java.lang.Math.max(max, java.lang.Math.abs(delta));
			}
			return max;
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getLInfDistance(double[] v) throws java.lang.IllegalArgumentException {
		checkVectorDimensions(v.length);
		double max = 0;
		for (int i = 0; i < data.length; ++i) {
			final double delta = data[i] - v[i];
			max = java.lang.Math.max(max, java.lang.Math.abs(delta));
		}
		return max;
	}

	/**
	 * Distance between two vectors.
	 * <p>This method computes the distance consistent with
	 * L<sub>&infin;</sub> norm, i.e. the max of the absolute values of
	 * elements differences.</p>
	 *
	 * @param v
	 * 		vector to which distance is requested
	 * @return distance between two vectors.
	 * @exception IllegalArgumentException
	 * 		if v is not the same size as this
	 * @see #getDistance(RealVector)
	 * @see #getL1Distance(ArrayRealVector)
	 * @see #getLInfDistance(ArrayRealVector)
	 * @see #getNorm()
	 */ 	public double getLInfDistance(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException { 		return getLInfDistance(v.data);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector unitVector() throws java.lang.ArithmeticException { 		final double norm = getNorm();
		if (norm == 0) {
			throw org.apache.commons.math.MathRuntimeException.createArithmeticException("zero norm");
		}
		return mapDivide(getNorm());
	}

	/**
	 * {@inheritDoc }
	 */ 	public void unitize() throws java.lang.ArithmeticException { 		final double norm = getNorm();
		if (norm == 0) {
			throw org.apache.commons.math.MathRuntimeException.createArithmeticException("cannot normalize a zero norm vector");
		}
		for (int i = 0; i < data.length; i++) {
			data[i] /= norm;
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector projection(org.apache.commons.math.linear.RealVector v) { 		return v.mapMultiply(dotProduct(v) / v.dotProduct(v));
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector projection(double[] v) { 		return projection(new org.apache.commons.math.linear.ArrayRealVector(v, false));
	}

	/**
	 * Find the orthogonal projection of this vector onto another vector.
	 *
	 * @param v
	 * 		vector onto which instance must be projected
	 * @return projection of the instance onto v
	 * @throws IllegalArgumentException
	 * 		if v is not the same size as this
	 */ 	public org.apache.commons.math.linear.ArrayRealVector projection(org.apache.commons.math.linear.ArrayRealVector v) { 		return ((org.apache.commons.math.linear.ArrayRealVector) (v.mapMultiply(dotProduct(v) / v.dotProduct(v))));}
	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealMatrix outerProduct(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return outerProduct(((org.apache.commons.math.linear.ArrayRealVector) (v)));
		} catch (java.lang.ClassCastException cce) {
			checkVectorDimensions(v);
			final int m = data.length;
			final org.apache.commons.math.linear.RealMatrix out = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data.length; j++) {
					out.setEntry(i, j, data[i] * v.getEntry(j));
				}
			}
			return out;
		}
	}

	/**
	 * Compute the outer product.
	 *
	 * @param v
	 * 		vector with which outer product should be computed
	 * @return the square matrix outer product between instance and v
	 * @exception IllegalArgumentException
	 * 		if v is not the same size as this
	 */ 	public org.apache.commons.math.linear.RealMatrix outerProduct(org.apache.commons.math.linear.ArrayRealVector v) throws java.lang.IllegalArgumentException { 		return outerProduct(v.data);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealMatrix outerProduct(double[] v) throws java.lang.IllegalArgumentException {
		checkVectorDimensions(v.length);
		final int m = data.length;
		final org.apache.commons.math.linear.RealMatrix out = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(m, m);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data.length; j++) {
				out.setEntry(i, j, data[i] * v[j]);
			}
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */ 	public double getEntry(int index) throws org.apache.commons.math.linear.MatrixIndexException { 		return data[index];
	}

	/**
	 * {@inheritDoc }
	 */ 	public int getDimension() { 		return data.length;
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector append(org.apache.commons.math.linear.RealVector v) { 		try {
			return append(((org.apache.commons.math.linear.ArrayRealVector) (v)));
		} catch (java.lang.ClassCastException cce) {
			return new org.apache.commons.math.linear.ArrayRealVector(this, new org.apache.commons.math.linear.ArrayRealVector(v));
		}
	}

	/**
	 * Construct a vector by appending a vector to this vector.
	 *
	 * @param v
	 * 		vector to append to this one.
	 * @return a new vector
	 */ 	public org.apache.commons.math.linear.ArrayRealVector append(org.apache.commons.math.linear.ArrayRealVector v) { 		return new org.apache.commons.math.linear.ArrayRealVector(this, v);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector append(double in) { 		final double[] out = new double[data.length + 1];
		java.lang.System.arraycopy(data, 0, out, 0, data.length);
		out[data.length] = in;
		return new org.apache.commons.math.linear.ArrayRealVector(out);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector append(double[] in) { 		return new org.apache.commons.math.linear.ArrayRealVector(this, in);
	}

	/**
	 * {@inheritDoc }
	 */ 	public org.apache.commons.math.linear.RealVector getSubVector(int index, int n) { 		org.apache.commons.math.linear.ArrayRealVector out = new org.apache.commons.math.linear.ArrayRealVector(n);
		try {
			java.lang.System.arraycopy(data, index, out.data, 0, n);
		} catch (java.lang.IndexOutOfBoundsException e) {
			checkIndex(index);
			checkIndex((index + n) - 1);
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */ 	public void setEntry(int index, double value) { 		try {
			data[index] = value;
		} catch (java.lang.IndexOutOfBoundsException e) {
			checkIndex(index);
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public void setSubVector(int index, org.apache.commons.math.linear.RealVector v) { 		try {
			try {
				set(index, ((org.apache.commons.math.linear.ArrayRealVector) (v)));
			} catch (java.lang.ClassCastException cce) {
				for (int i = index; i < (index + v.getDimension()); ++i) {
					data[i] = v.getEntry(i - index);
				}
			}
		} catch (java.lang.IndexOutOfBoundsException e) {
			checkIndex(index);
			checkIndex((index + v.getDimension()) - 1);
		}
	}

	/**
	 * {@inheritDoc }
	 */ 	public void setSubVector(int index, double[] v) { 		try {
			java.lang.System.arraycopy(v, 0, data, index, v.length);
		} catch (java.lang.IndexOutOfBoundsException e) {
			checkIndex(index);
			checkIndex((index + v.length) - 1);
		}
	}

	/**
	 * Set a set of consecutive elements.
	 *
	 * @param index
	 * 		index of first element to be set.
	 * @param v
	 * 		vector containing the values to set.
	 * @exception MatrixIndexException
	 * 		if the index is
	 * 		inconsistent with vector size
	 */ 	public void set(int index, org.apache.commons.math.linear.ArrayRealVector v) throws org.apache.commons.math.linear.MatrixIndexException { 		setSubVector(index, v.data);
	}

	/**
	 * {@inheritDoc }
	 */ 	public void set(double value) { 		java.util.Arrays.fill(data, value);
	}

	/**
	 * {@inheritDoc }
	 */ 	public double[] toArray() { 		return data.clone();
	}

	/**
	 * {@inheritDoc }
	 */ 	@java.lang.Override 	public java.lang.String toString() {
		return org.apache.commons.math.linear.ArrayRealVector.DEFAULT_FORMAT.format(this);
	}

	/**
	 * Check if instance and specified vectors have the same dimension.
	 *
	 * @param v
	 * 		vector to compare instance with
	 * @exception IllegalArgumentException
	 * 		if the vectors do not
	 * 		have the same dimension
	 */ 	protected void checkVectorDimensions(org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException { 		checkVectorDimensions(v.getDimension());
	}

	/**
	 * Check if instance dimension is equal to some expected value.
	 *
	 * @param n
	 * 		expected dimension.
	 * @exception IllegalArgumentException
	 * 		if the dimension is
	 * 		inconsistent with vector size
	 */ 	protected void checkVectorDimensions(int n) throws java.lang.IllegalArgumentException {
		if (data.length != n) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(
			"vector length mismatch: got {0} but expected {1}", 
			data.length, n);
		}
	}

	/**
	 * Returns true if any coordinate of this vector is NaN; false otherwise
	 *
	 * @return true if any coordinate of this vector is NaN; false otherwise
	 */ 	public boolean isNaN() {
		for (double v : data) {
			if (java.lang.Double.isNaN(v)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if any coordinate of this vector is infinite and none are NaN;
	 * false otherwise
	 *
	 * @return true if any coordinate of this vector is infinite and none are NaN;
	false otherwise
	 */ 	public boolean isInfinite() {

		if (isNaN()) {
			return false;
		}

		for (double v : data) {
			if (java.lang.Double.isInfinite(v)) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Test for the equality of two real vectors.
	 * <p>
	 * If all coordinates of two real vectors are exactly the same, and none are
	 * <code>Double.NaN</code>, the two real vectors are considered to be equal.
	 * </p>
	 * <p>
	 * <code>NaN</code> coordinates are considered to affect globally the vector
	 * and be equals to each other - i.e, if either (or all) coordinates of the
	 * real vector are equal to <code>Double.NaN</code>, the real vector is equal to
	 * a vector with all <code>Double.NaN</code> coordinates.
	 * </p>
	 *
	 * @param other
	 * 		Object to test for equality to this
	 * @return true if two vector objects are equal, false if
	object is null, not an instance of RealVector, or
	not equal to this RealVector instance
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

			org.apache.commons.math.linear.RealVector rhs = ((org.apache.commons.math.linear.RealVector) (other));
			if (data.length != rhs.getDimension()) {
				return false;
			}

			if (rhs.isNaN()) {
				return this.isNaN();
			}

			for (int i = 0; i < data.length; ++i) {
				if (data[i] != rhs.getEntry(i)) {
					return false;
				}
			}
			return true;

		} catch (java.lang.ClassCastException ex) {
			// ignore exception
			return false;
		}

	}

	/**
	 * Get a hashCode for the real vector.
	 * <p>All NaN values have the same hash code.</p>
	 *
	 * @return a hash code value for this object
	 */ 	@java.lang.Override
	public int hashCode() {
		if (isNaN()) {
			return 9;
		}
		return org.apache.commons.math.util.MathUtils.hash(data);
	}

	/**
	 * Check if an index is valid.
	 *
	 * @param index
	 * 		index to check
	 * @exception MatrixIndexException
	 * 		if index is not valid
	 */ 	private void checkIndex(final int index) throws org.apache.commons.math.linear.MatrixIndexException { 		if ((index < 0) || (index >= getDimension())) {
			throw new org.apache.commons.math.linear.MatrixIndexException(
			"index {0} out of allowed range [{1}, {2}]", 
			index, 0, getDimension() - 1);
		}
	}}