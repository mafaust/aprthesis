package org.apache.commons.math.linear;































/**
 * A collection of static methods that operate on or return matrices.
 *
 * @version $Revision$ $Date$
 */
public class MatrixUtils {

	/**
	 * Private constructor.
	 */
	private MatrixUtils() {
		super();
	}

	/**
	 * Returns a {@link RealMatrix} with specified dimensions.
	 * <p>The type of matrix returned depends on the dimension. Below
	 * 2<sup>12</sup> elements (i.e. 4096 elements or 64&times;64 for a
	 * square matrix) which can be stored in a 32kB array, a {@link Array2DRowRealMatrix} instance is built. Above this threshold a {@link BlockRealMatrix} instance is built.</p>
	 * <p>The matrix elements are all set to 0.0.</p>
	 *
	 * @param rows
	 * 		number of rows of the matrix
	 * @param columns
	 * 		number of columns of the matrix
	 * @return RealMatrix with specified dimensions
	 * @see #createRealMatrix(double[][])
	 */ 	public static org.apache.commons.math.linear.RealMatrix createRealMatrix(final int rows, final int columns) {
		return (rows * columns) <= 4096 ? 
		new org.apache.commons.math.linear.Array2DRowRealMatrix(rows, columns) : new org.apache.commons.math.linear.BlockRealMatrix(rows, columns);
	}

	/**
	 * Returns a {@link FieldMatrix} with specified dimensions.
	 * <p>The type of matrix returned depends on the dimension. Below
	 * 2<sup>12</sup> elements (i.e. 4096 elements or 64&times;64 for a
	 * square matrix), a {@link FieldMatrix} instance is built. Above
	 * this threshold a {@link BlockFieldMatrix} instance is built.</p>
	 * <p>The matrix elements are all set to field.getZero().</p>
	 *
	 * @param <T>
	 * 		the type of the field elements
	 * @param field
	 * 		field to which the matrix elements belong
	 * @param rows
	 * 		number of rows of the matrix
	 * @param columns
	 * 		number of columns of the matrix
	 * @return FieldMatrix with specified dimensions
	 * @see #createFieldMatrix(FieldElement[][])
	 * @since 2.0
	 */ 	public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createFieldMatrix(final org.apache.commons.math.Field<T> field, final int rows, final int columns) { 		return (rows * columns) <= 4096 ? new org.apache.commons.math.linear.Array2DRowFieldMatrix<T>(field, rows, columns) : new org.apache.commons.math.linear.BlockFieldMatrix<T>(field, rows, columns);
	}

	/**
	 * Returns a {@link RealMatrix} whose entries are the the values in the
	 * the input array.
	 * <p>The type of matrix returned depends on the dimension. Below
	 * 2<sup>12</sup> elements (i.e. 4096 elements or 64&times;64 for a
	 * square matrix) which can be stored in a 32kB array, a {@link Array2DRowRealMatrix} instance is built. Above this threshold a {@link BlockRealMatrix} instance is built.</p>
	 * <p>The input array is copied, not referenced.</p>
	 *
	 * @param data
	 * 		input array
	 * @return RealMatrix containing the values of the array
	 * @throws IllegalArgumentException
	 * 		if <code>data</code> is not rectangular
	 * 		(not all rows have the same length) or empty
	 * @throws NullPointerException
	 * 		if either <code>data</code> or
	 * 		<code>data[0]</code> is null
	 * @see #createRealMatrix(int, int)
	 */ 	public static org.apache.commons.math.linear.RealMatrix createRealMatrix(double[][] data) {
		return (data.length * data[0].length) <= 4096 ? 
		new org.apache.commons.math.linear.Array2DRowRealMatrix(data) : new org.apache.commons.math.linear.BlockRealMatrix(data);
	}

	/**
	 * Returns a {@link FieldMatrix} whose entries are the the values in the
	 * the input array.
	 * <p>The type of matrix returned depends on the dimension. Below
	 * 2<sup>12</sup> elements (i.e. 4096 elements or 64&times;64 for a
	 * square matrix), a {@link FieldMatrix} instance is built. Above
	 * this threshold a {@link BlockFieldMatrix} instance is built.</p>
	 * <p>The input array is copied, not referenced.</p>
	 *
	 * @param <T>
	 * 		the type of the field elements
	 * @param data
	 * 		input array
	 * @return RealMatrix containing the values of the array
	 * @throws IllegalArgumentException
	 * 		if <code>data</code> is not rectangular
	 * 		(not all rows have the same length) or empty
	 * @throws NullPointerException
	 * 		if either <code>data</code> or
	 * 		<code>data[0]</code> is null
	 * @see #createFieldMatrix(Field, int, int)
	 * @since 2.0
	 */ 	public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createFieldMatrix(T[][] data) { 		return (data.length * data[0].length) <= 4096 ? new org.apache.commons.math.linear.Array2DRowFieldMatrix<T>(data) : new org.apache.commons.math.linear.BlockFieldMatrix<T>(data);}
	/**
	 * Returns <code>dimension x dimension</code> identity matrix.
	 *
	 * @param dimension
	 * 		dimension of identity matrix to generate
	 * @return identity matrix
	 * @throws IllegalArgumentException
	 * 		if dimension is not positive
	 * @since 1.1
	 */ 	public static org.apache.commons.math.linear.RealMatrix createRealIdentityMatrix(int dimension) { 		final org.apache.commons.math.linear.RealMatrix m = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(dimension, dimension);
		for (int i = 0; i < dimension; ++i) {
			m.setEntry(i, i, 1.0);
		}
		return m;
	}

	/**
	 * Returns <code>dimension x dimension</code> identity matrix.
	 *
	 * @param <T>
	 * 		the type of the field elements
	 * @param field
	 * 		field to which the elements belong
	 * @param dimension
	 * 		dimension of identity matrix to generate
	 * @return identity matrix
	 * @throws IllegalArgumentException
	 * 		if dimension is not positive
	 * @since 2.0
	 */ 	@java.lang.SuppressWarnings("unchecked") 	public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createFieldIdentityMatrix(final org.apache.commons.math.Field<T> field, final int dimension) { 		final T zero = field.getZero();
		final T one = field.getOne();
		final T[][] d = ((T[][]) (java.lang.reflect.Array.newInstance(zero.getClass(), new int[]{ dimension, dimension })));
		for (int row = 0; row < dimension; row++) {
			final T[] dRow = d[row];
			java.util.Arrays.fill(dRow, zero);
			dRow[row] = one;
		}
		return new org.apache.commons.math.linear.Array2DRowFieldMatrix<T>(d, false);
	}

	/**
	 * Returns <code>dimension x dimension</code> identity matrix.
	 *
	 * @param dimension
	 * 		dimension of identity matrix to generate
	 * @return identity matrix
	 * @throws IllegalArgumentException
	 * 		if dimension is not positive
	 * @since 1.1
	 * @deprecated since 2.0, replaced by {@link #createFieldIdentityMatrix(Field, int)}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createBigIdentityMatrix(int dimension) {
		final java.math.BigDecimal[][] d = new java.math.BigDecimal[dimension][dimension];
		for (int row = 0; row < dimension; row++) {
			final java.math.BigDecimal[] dRow = d[row];
			java.util.Arrays.fill(dRow, org.apache.commons.math.linear.BigMatrixImpl.ZERO);
			dRow[row] = org.apache.commons.math.linear.BigMatrixImpl.ONE;
		}
		return new org.apache.commons.math.linear.BigMatrixImpl(d, false);
	}

	/**
	 * Returns a diagonal matrix with specified elements.
	 *
	 * @param diagonal
	 * 		diagonal elements of the matrix (the array elements
	 * 		will be copied)
	 * @return diagonal matrix
	 * @since 2.0
	 */ 	public static org.apache.commons.math.linear.RealMatrix createRealDiagonalMatrix(final double[] diagonal) {
		final org.apache.commons.math.linear.RealMatrix m = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(diagonal.length, diagonal.length);
		for (int i = 0; i < diagonal.length; ++i) {
			m.setEntry(i, i, diagonal[i]);
		}
		return m;
	}

	/**
	 * Returns a diagonal matrix with specified elements.
	 *
	 * @param <T>
	 * 		the type of the field elements
	 * @param diagonal
	 * 		diagonal elements of the matrix (the array elements
	 * 		will be copied)
	 * @return diagonal matrix
	 * @since 2.0
	 */ 	public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createFieldDiagonalMatrix(final T[] diagonal) {
		final org.apache.commons.math.linear.FieldMatrix<T> m = 
		org.apache.commons.math.linear.MatrixUtils.createFieldMatrix(diagonal[0].getField(), diagonal.length, diagonal.length);
		for (int i = 0; i < diagonal.length; ++i) {
			m.setEntry(i, i, diagonal[i]);
		}
		return m;
	}

	/**
	 * Returns a {@link BigMatrix} whose entries are the the values in the
	 * the input array.  The input array is copied, not referenced.
	 *
	 * @param data
	 * 		input array
	 * @return RealMatrix containing the values of the array
	 * @throws IllegalArgumentException
	 * 		if <code>data</code> is not rectangular
	 * 		(not all rows have the same length) or empty
	 * @throws NullPointerException
	 * 		if data is null
	 * @deprecated since 2.0 replaced by {@link #createFieldMatrix(FieldElement[][])}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createBigMatrix(double[][] data) { 		return new org.apache.commons.math.linear.BigMatrixImpl(data);
	}

	/**
	 * Returns a {@link BigMatrix} whose entries are the the values in the
	 * the input array.  The input array is copied, not referenced.
	 *
	 * @param data
	 * 		input array
	 * @return RealMatrix containing the values of the array
	 * @throws IllegalArgumentException
	 * 		if <code>data</code> is not rectangular
	 * 		(not all rows have the same length) or empty
	 * @throws NullPointerException
	 * 		if data is null
	 * @deprecated since 2.0 replaced by {@link #createFieldMatrix(FieldElement[][])}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createBigMatrix(java.math.BigDecimal[][] data) { 		return new org.apache.commons.math.linear.BigMatrixImpl(data);
	}

	/**
	 * Returns a {@link BigMatrix} whose entries are the the values in the
	 * the input array.
	 * <p>If an array is built specially in order to be embedded in a
	 * BigMatrix and not used directly, the <code>copyArray</code> may be
	 * set to <code>false</code. This will prevent the copying and improve
	 * performance as no new array will be built and no data will be copied.</p>
	 *
	 * @param data
	 * 		data for new matrix
	 * @param copyArray
	 * 		if true, the input array will be copied, otherwise
	 * 		it will be referenced
	 * @return BigMatrix containing the values of the array
	 * @throws IllegalArgumentException
	 * 		if <code>data</code> is not rectangular
	 * 		(not all rows have the same length) or empty
	 * @throws NullPointerException
	 * 		if <code>data</code> is null
	 * @see #createRealMatrix(double[][])
	 * @deprecated since 2.0 replaced by {@link #createFieldMatrix(FieldElement[][])}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createBigMatrix(java.math.BigDecimal[][] data, boolean copyArray) { 		return new org.apache.commons.math.linear.BigMatrixImpl(data, copyArray);}
	/**
	 * Returns a {@link BigMatrix} whose entries are the the values in the
	 * the input array.  The input array is copied, not referenced.
	 *
	 * @param data
	 * 		input array
	 * @return RealMatrix containing the values of the array
	 * @throws IllegalArgumentException
	 * 		if <code>data</code> is not rectangular
	 * 		(not all rows have the same length) or empty
	 * @throws NullPointerException
	 * 		if data is null
	 * @deprecated since 2.0 replaced by {@link #createFieldMatrix(FieldElement[][])}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createBigMatrix(java.lang.String[][] data) { 		return new org.apache.commons.math.linear.BigMatrixImpl(data);
	}

	/**
	 * Creates a {@link RealVector} using the data from the input array.
	 *
	 * @param data
	 * 		the input data
	 * @return a data.length RealVector
	 * @throws IllegalArgumentException
	 * 		if <code>data</code> is empty
	 * @throws NullPointerException
	 * 		if <code>data</code>is null
	 */ 	public static org.apache.commons.math.linear.RealVector createRealVector(double[] data) { 		return new org.apache.commons.math.linear.ArrayRealVector(data, true);}

	/**
	 * Creates a {@link FieldVector} using the data from the input array.
	 *
	 * @param <T>
	 * 		the type of the field elements
	 * @param data
	 * 		the input data
	 * @return a data.length FieldVector
	 * @throws IllegalArgumentException
	 * 		if <code>data</code> is empty
	 * @throws NullPointerException
	 * 		if <code>data</code>is null
	 */ 	public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldVector<T> createFieldVector(final T[] data) { 		return new org.apache.commons.math.linear.ArrayFieldVector<T>(data, true);}
	/**
	 * Creates a row {@link RealMatrix} using the data from the input
	 * array.
	 *
	 * @param rowData
	 * 		the input row data
	 * @return a 1 x rowData.length RealMatrix
	 * @throws IllegalArgumentException
	 * 		if <code>rowData</code> is empty
	 * @throws NullPointerException
	 * 		if <code>rowData</code>is null
	 */ 	public static org.apache.commons.math.linear.RealMatrix createRowRealMatrix(double[] rowData) { 		final int nCols = rowData.length; 		final org.apache.commons.math.linear.RealMatrix m = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(1, nCols);
		for (int i = 0; i < nCols; ++i) {
			m.setEntry(0, i, rowData[i]);
		}
		return m;
	}

	/**
	 * Creates a row {@link FieldMatrix} using the data from the input
	 * array.
	 *
	 * @param <T>
	 * 		the type of the field elements
	 * @param rowData
	 * 		the input row data
	 * @return a 1 x rowData.length FieldMatrix
	 * @throws IllegalArgumentException
	 * 		if <code>rowData</code> is empty
	 * @throws NullPointerException
	 * 		if <code>rowData</code>is null
	 */ 	public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createRowFieldMatrix(final T[] rowData) { 		final int nCols = rowData.length; 		if (nCols == 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("matrix must have at least one column");
		}
		final org.apache.commons.math.linear.FieldMatrix<T> m = org.apache.commons.math.linear.MatrixUtils.createFieldMatrix(rowData[0].getField(), 1, nCols);
		for (int i = 0; i < nCols; ++i) {
			m.setEntry(0, i, rowData[i]);
		}
		return m;
	}

	/**
	 * Creates a row {@link BigMatrix} using the data from the input
	 * array.
	 *
	 * @param rowData
	 * 		the input row data
	 * @return a 1 x rowData.length BigMatrix
	 * @throws IllegalArgumentException
	 * 		if <code>rowData</code> is empty
	 * @throws NullPointerException
	 * 		if <code>rowData</code>is null
	 * @deprecated since 2.0 replaced by {@link #createRowFieldMatrix(FieldElement[])}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createRowBigMatrix(double[] rowData) { 		final int nCols = rowData.length;
		final java.math.BigDecimal[][] data = new java.math.BigDecimal[1][nCols];
		for (int i = 0; i < nCols; ++i) {
			data[0][i] = new java.math.BigDecimal(rowData[i]);
		}
		return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
	}

	/**
	 * Creates a row {@link BigMatrix} using the data from the input
	 * array.
	 *
	 * @param rowData
	 * 		the input row data
	 * @return a 1 x rowData.length BigMatrix
	 * @throws IllegalArgumentException
	 * 		if <code>rowData</code> is empty
	 * @throws NullPointerException
	 * 		if <code>rowData</code>is null
	 * @deprecated since 2.0 replaced by {@link #createRowFieldMatrix(FieldElement[])}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createRowBigMatrix(java.math.BigDecimal[] rowData) { 		final int nCols = rowData.length;
		final java.math.BigDecimal[][] data = new java.math.BigDecimal[1][nCols];
		java.lang.System.arraycopy(rowData, 0, data[0], 0, nCols);
		return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
	}

	/**
	 * Creates a row {@link BigMatrix} using the data from the input
	 * array.
	 *
	 * @param rowData
	 * 		the input row data
	 * @return a 1 x rowData.length BigMatrix
	 * @throws IllegalArgumentException
	 * 		if <code>rowData</code> is empty
	 * @throws NullPointerException
	 * 		if <code>rowData</code>is null
	 * @deprecated since 2.0 replaced by {@link #createRowFieldMatrix(FieldElement[])}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createRowBigMatrix(java.lang.String[] rowData) { 		final int nCols = rowData.length;
		final java.math.BigDecimal[][] data = new java.math.BigDecimal[1][nCols];
		for (int i = 0; i < nCols; ++i) {
			data[0][i] = new java.math.BigDecimal(rowData[i]);
		}
		return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
	}

	/**
	 * Creates a column {@link RealMatrix} using the data from the input
	 * array.
	 *
	 * @param columnData
	 * 		the input column data
	 * @return a columnData x 1 RealMatrix
	 * @throws IllegalArgumentException
	 * 		if <code>columnData</code> is empty
	 * @throws NullPointerException
	 * 		if <code>columnData</code>is null
	 */ 	public static org.apache.commons.math.linear.RealMatrix createColumnRealMatrix(double[] columnData) { 		final int nRows = columnData.length; 		final org.apache.commons.math.linear.RealMatrix m = org.apache.commons.math.linear.MatrixUtils.createRealMatrix(nRows, 1);
		for (int i = 0; i < nRows; ++i) {
			m.setEntry(i, 0, columnData[i]);
		}
		return m;
	}

	/**
	 * Creates a column {@link FieldMatrix} using the data from the input
	 * array.
	 *
	 * @param <T>
	 * 		the type of the field elements
	 * @param columnData
	 * 		the input column data
	 * @return a columnData x 1 FieldMatrix
	 * @throws IllegalArgumentException
	 * 		if <code>columnData</code> is empty
	 * @throws NullPointerException
	 * 		if <code>columnData</code>is null
	 */ 	public static <T extends org.apache.commons.math.FieldElement<T>> org.apache.commons.math.linear.FieldMatrix<T> createColumnFieldMatrix(final T[] columnData) { 		final int nRows = columnData.length; 		if (nRows == 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("matrix must have at least one row");
		}
		final org.apache.commons.math.linear.FieldMatrix<T> m = org.apache.commons.math.linear.MatrixUtils.createFieldMatrix(columnData[0].getField(), nRows, 1);
		for (int i = 0; i < nRows; ++i) {
			m.setEntry(i, 0, columnData[i]);
		}
		return m;
	}

	/**
	 * Creates a column {@link BigMatrix} using the data from the input
	 * array.
	 *
	 * @param columnData
	 * 		the input column data
	 * @return a columnData x 1 BigMatrix
	 * @throws IllegalArgumentException
	 * 		if <code>columnData</code> is empty
	 * @throws NullPointerException
	 * 		if <code>columnData</code>is null
	 * @deprecated since 2.0 replaced by {@link #createColumnFieldMatrix(FieldElement[])}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createColumnBigMatrix(double[] columnData) { 		final int nRows = columnData.length;
		final java.math.BigDecimal[][] data = new java.math.BigDecimal[nRows][1];
		for (int row = 0; row < nRows; row++) {
			data[row][0] = new java.math.BigDecimal(columnData[row]);
		}
		return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
	}

	/**
	 * Creates a column {@link BigMatrix} using the data from the input
	 * array.
	 *
	 * @param columnData
	 * 		the input column data
	 * @return a columnData x 1 BigMatrix
	 * @throws IllegalArgumentException
	 * 		if <code>columnData</code> is empty
	 * @throws NullPointerException
	 * 		if <code>columnData</code>is null
	 * @deprecated since 2.0 replaced by {@link #createColumnFieldMatrix(FieldElement[])}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createColumnBigMatrix(java.math.BigDecimal[] columnData) { 		final int nRows = columnData.length;
		final java.math.BigDecimal[][] data = new java.math.BigDecimal[nRows][1];
		for (int row = 0; row < nRows; row++) {
			data[row][0] = columnData[row];
		}
		return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
	}

	/**
	 * Creates a column {@link BigMatrix} using the data from the input
	 * array.
	 *
	 * @param columnData
	 * 		the input column data
	 * @return a columnData x 1 BigMatrix
	 * @throws IllegalArgumentException
	 * 		if <code>columnData</code> is empty
	 * @throws NullPointerException
	 * 		if <code>columnData</code>is null
	 * @deprecated since 2.0 replaced by {@link #createColumnFieldMatrix(FieldElement[])}
	 */ 	@java.lang.Deprecated 	public static org.apache.commons.math.linear.BigMatrix createColumnBigMatrix(java.lang.String[] columnData) { 		int nRows = columnData.length;
		final java.math.BigDecimal[][] data = new java.math.BigDecimal[nRows][1];
		for (int row = 0; row < nRows; row++) {
			data[row][0] = new java.math.BigDecimal(columnData[row]);
		}
		return new org.apache.commons.math.linear.BigMatrixImpl(data, false);
	}

	/**
	 * Check if a row index is valid.
	 *
	 * @param m
	 * 		matrix containing the submatrix
	 * @param row
	 * 		row index to check
	 * @exception MatrixIndexException
	 * 		if index is not valid
	 */ 	public static void checkRowIndex(final org.apache.commons.math.linear.AnyMatrix m, final int row) { 		if ((row < 0) || (row >= m.getRowDimension())) { 			throw new org.apache.commons.math.linear.MatrixIndexException("row index {0} out of allowed range [{1}, {2}]", row, 0, m.getRowDimension() - 1);
		}
	}

	/**
	 * Check if a column index is valid.
	 *
	 * @param m
	 * 		matrix containing the submatrix
	 * @param column
	 * 		column index to check
	 * @exception MatrixIndexException
	 * 		if index is not valid
	 */ 	public static void checkColumnIndex(final org.apache.commons.math.linear.AnyMatrix m, final int column) throws org.apache.commons.math.linear.MatrixIndexException { 		if ((column < 0) || (column >= m.getColumnDimension())) { 			throw new org.apache.commons.math.linear.MatrixIndexException("column index {0} out of allowed range [{1}, {2}]", 
			column, 0, m.getColumnDimension() - 1);
		}
	}

	/**
	 * Check if submatrix ranges indices are valid.
	 * Rows and columns are indicated counting from 0 to n-1.
	 *
	 * @param m
	 * 		matrix containing the submatrix
	 * @param startRow
	 * 		Initial row index
	 * @param endRow
	 * 		Final row index
	 * @param startColumn
	 * 		Initial column index
	 * @param endColumn
	 * 		Final column index
	 * @exception MatrixIndexException
	 * 		if the indices are not valid
	 */ 	public static void checkSubMatrixIndex(final org.apache.commons.math.linear.AnyMatrix m, final int startRow, final int endRow, final int startColumn, final int endColumn) { 		org.apache.commons.math.linear.MatrixUtils.checkRowIndex(m, startRow); 		org.apache.commons.math.linear.MatrixUtils.checkRowIndex(m, endRow); 		if (startRow > endRow) {
			throw new org.apache.commons.math.linear.MatrixIndexException("initial row {0} after final row {1}", 
			startRow, endRow);
		}

		org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(m, startColumn);
		org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(m, endColumn);
		if (startColumn > endColumn) {
			throw new org.apache.commons.math.linear.MatrixIndexException("initial column {0} after final column {1}", 
			startColumn, endColumn);
		}


	}

	/**
	 * Check if submatrix ranges indices are valid.
	 * Rows and columns are indicated counting from 0 to n-1.
	 *
	 * @param m
	 * 		matrix containing the submatrix
	 * @param selectedRows
	 * 		Array of row indices.
	 * @param selectedColumns
	 * 		Array of column indices.
	 * @exception MatrixIndexException
	 * 		if row or column selections are not valid
	 */ 	public static void checkSubMatrixIndex(final org.apache.commons.math.linear.AnyMatrix m, final int[] selectedRows, final int[] selectedColumns) throws org.apache.commons.math.linear.MatrixIndexException { 		if ((selectedRows.length * selectedColumns.length) == 0) {
			if (selectedRows.length == 0) {
				throw new org.apache.commons.math.linear.MatrixIndexException("empty selected row index array");
			}
			throw new org.apache.commons.math.linear.MatrixIndexException("empty selected column index array");
		}

		for (final int row : selectedRows) {
			org.apache.commons.math.linear.MatrixUtils.checkRowIndex(m, row);
		}
		for (final int column : selectedColumns) {
			org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(m, column);
		}
	}

	/**
	 * Check if matrices are addition compatible
	 *
	 * @param left
	 * 		left hand side matrix
	 * @param right
	 * 		right hand side matrix
	 * @exception IllegalArgumentException
	 * 		if matrices are not addition compatible
	 */ 	public static void checkAdditionCompatible(final org.apache.commons.math.linear.AnyMatrix left, final org.apache.commons.math.linear.AnyMatrix right) throws java.lang.IllegalArgumentException { 		if ((left.getRowDimension() != right.getRowDimension()) || (left.getColumnDimension() != right.getColumnDimension())) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(
			"{0}x{1} and {2}x{3} matrices are not addition compatible", 
			left.getRowDimension(), left.getColumnDimension(), 
			right.getRowDimension(), right.getColumnDimension());
		}
	}

	/**
	 * Check if matrices are subtraction compatible
	 *
	 * @param left
	 * 		left hand side matrix
	 * @param right
	 * 		right hand side matrix
	 * @exception IllegalArgumentException
	 * 		if matrices are not subtraction compatible
	 */ 	public static void checkSubtractionCompatible(final org.apache.commons.math.linear.AnyMatrix left, final org.apache.commons.math.linear.AnyMatrix right) throws java.lang.IllegalArgumentException { 		if ((left.getRowDimension() != right.getRowDimension()) || (left.getColumnDimension() != right.getColumnDimension())) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(
			"{0}x{1} and {2}x{3} matrices are not subtraction compatible", 
			left.getRowDimension(), left.getColumnDimension(), 
			right.getRowDimension(), right.getColumnDimension());
		}
	}

	/**
	 * Check if matrices are multiplication compatible
	 *
	 * @param left
	 * 		left hand side matrix
	 * @param right
	 * 		right hand side matrix
	 * @exception IllegalArgumentException
	 * 		if matrices are not multiplication compatible
	 */ 	public static void checkMultiplicationCompatible(final org.apache.commons.math.linear.AnyMatrix left, final org.apache.commons.math.linear.AnyMatrix right) throws java.lang.IllegalArgumentException { 		if (left.getColumnDimension() != right.getRowDimension()) { 			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(
			"{0}x{1} and {2}x{3} matrices are not multiplication compatible", 
			left.getRowDimension(), left.getColumnDimension(), 
			right.getRowDimension(), right.getColumnDimension());
		}
	}

	/**
	 * Convert a {@link FieldMatrix}/{@link Fraction} matrix to a {@link RealMatrix}.
	 *
	 * @param m
	 * 		matrix to convert
	 * @return converted matrix
	 */ 	public static org.apache.commons.math.linear.Array2DRowRealMatrix fractionMatrixToRealMatrix(final org.apache.commons.math.linear.FieldMatrix<org.apache.commons.math.fraction.Fraction> m) { 		final org.apache.commons.math.linear.MatrixUtils.FractionMatrixConverter converter = new org.apache.commons.math.linear.MatrixUtils.FractionMatrixConverter();
		m.walkInOptimizedOrder(converter);
		return converter.getConvertedMatrix();
	}

	/**
	 * Converter for {@link FieldMatrix}/{@link Fraction}.
	 */ 	private static class FractionMatrixConverter extends org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor<org.apache.commons.math.fraction.Fraction> {
		/**
		 * Converted array.
		 */ 		private double[][] data;
		/**
		 * Simple constructor.
		 */ 		public FractionMatrixConverter() { 			super(org.apache.commons.math.fraction.Fraction.ZERO);
		}

		/**
		 * {@inheritDoc }
		 */ 		@java.lang.Override 		public void start(int rows, int columns, 
		int startRow, int endRow, int startColumn, int endColumn) {
			data = new double[rows][columns];
		}

		/**
		 * {@inheritDoc }
		 */ 		@java.lang.Override 		public void visit(int row, int column, org.apache.commons.math.fraction.Fraction value) {
			data[row][column] = value.doubleValue();
		}

		/**
		 * Get the converted matrix.
		 *
		 * @return converted matrix
		 */ 		org.apache.commons.math.linear.Array2DRowRealMatrix getConvertedMatrix() { 			return new org.apache.commons.math.linear.Array2DRowRealMatrix(data, false);
		}

	}

	/**
	 * Convert a {@link FieldMatrix}/{@link BigFraction} matrix to a {@link RealMatrix}.
	 *
	 * @param m
	 * 		matrix to convert
	 * @return converted matrix
	 */ 	public static org.apache.commons.math.linear.Array2DRowRealMatrix bigFractionMatrixToRealMatrix(final org.apache.commons.math.linear.FieldMatrix<org.apache.commons.math.fraction.BigFraction> m) { 		final org.apache.commons.math.linear.MatrixUtils.BigFractionMatrixConverter converter = new org.apache.commons.math.linear.MatrixUtils.BigFractionMatrixConverter();
		m.walkInOptimizedOrder(converter);
		return converter.getConvertedMatrix();
	}

	/**
	 * Converter for {@link FieldMatrix}/{@link BigFraction}.
	 */ 	private static class BigFractionMatrixConverter extends org.apache.commons.math.linear.DefaultFieldMatrixPreservingVisitor<org.apache.commons.math.fraction.BigFraction> {
		/**
		 * Converted array.
		 */ 		private double[][] data;
		/**
		 * Simple constructor.
		 */ 		public BigFractionMatrixConverter() { 			super(org.apache.commons.math.fraction.BigFraction.ZERO);
		}

		/**
		 * {@inheritDoc }
		 */ 		@java.lang.Override 		public void start(int rows, int columns, 
		int startRow, int endRow, int startColumn, int endColumn) {
			data = new double[rows][columns];
		}

		/**
		 * {@inheritDoc }
		 */ 		@java.lang.Override 		public void visit(int row, int column, org.apache.commons.math.fraction.BigFraction value) {
			data[row][column] = value.doubleValue();
		}

		/**
		 * Get the converted matrix.
		 *
		 * @return converted matrix
		 */ 		org.apache.commons.math.linear.Array2DRowRealMatrix getConvertedMatrix() { 			return new org.apache.commons.math.linear.Array2DRowRealMatrix(data, false);
		}

	}

	/**
	 * Serialize a {@link RealVector}.
	 * <p>
	 * This method is intended to be called from within a private
	 * <code>writeObject</code> method (after a call to
	 * <code>oos.defaultWriteObject()</code>) in a class that has a
	 * {@link RealVector} field, which should be declared <code>transient</code>.
	 * This way, the default handling does not serialize the vector (the {@link RealVector} interface is not serializable by default) but this method does
	 * serialize it specifically.
	 * </p>
	 * <p>
	 * The following example shows how a simple class with a name and a real vector
	 * should be written:
	 * <pre><code>
	 * public class NamedVector implements Serializable {
	 *
	 *     private final String name;
	 *     private final transient RealVector coefficients;
	 *
	 *     // omitted constructors, getters ...
	 *
	 *     private void writeObject(ObjectOutputStream oos) throws IOException {
	 *         oos.defaultWriteObject();  // takes care of name field
	 *         MatrixUtils.serializeRealVector(coefficients, oos);
	 *     }
	 *
	 *     private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	 *         ois.defaultReadObject();  // takes care of name field
	 *         MatrixUtils.deserializeRealVector(this, "coefficients", ois);
	 *     }
	 *
	 * }
	 * </code></pre>
	 * </p>
	 *
	 * @param vector
	 * 		real vector to serialize
	 * @param oos
	 * 		stream where the real vector should be written
	 * @exception IOException
	 * 		if object cannot be written to stream
	 * @see #deserializeRealVector(Object, String, ObjectInputStream)
	 */ 	public static void serializeRealVector(final org.apache.commons.math.linear.RealVector vector, final java.io.ObjectOutputStream oos) throws java.io.IOException {
		final int n = vector.getDimension();
		oos.writeInt(n);
		for (int i = 0; i < n; ++i) {
			oos.writeDouble(vector.getEntry(i));
		}
	}

	/**
	 * Deserialize  a {@link RealVector} field in a class.
	 * <p>
	 * This method is intended to be called from within a private
	 * <code>readObject</code> method (after a call to
	 * <code>ois.defaultReadObject()</code>) in a class that has a
	 * {@link RealVector} field, which should be declared <code>transient</code>.
	 * This way, the default handling does not deserialize the vector (the {@link RealVector} interface is not serializable by default) but this method does
	 * deserialize it specifically.
	 * </p>
	 *
	 * @param instance
	 * 		instance in which the field must be set up
	 * @param fieldName
	 * 		name of the field within the class (may be private and final)
	 * @param ois
	 * 		stream from which the real vector should be read
	 * @exception ClassNotFoundException
	 * 		if a class in the stream cannot be found
	 * @exception IOException
	 * 		if object cannot be read from the stream
	 * @see #serializeRealVector(RealVector, ObjectOutputStream)
	 */ 	public static void deserializeRealVector(final java.lang.Object instance, final java.lang.String fieldName, final java.io.ObjectInputStream ois) throws java.lang.ClassNotFoundException, java.io.IOException { 		try {
			// read the vector data
			final int n = ois.readInt();
			final double[] data = new double[n];
			for (int i = 0; i < n; ++i) {
				data[i] = ois.readDouble();
			}

			// create the instance
			final org.apache.commons.math.linear.RealVector vector = new org.apache.commons.math.linear.ArrayRealVector(data, false);

			// set up the field
			final java.lang.reflect.Field f = 
			instance.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(instance, vector);

		} catch (java.lang.NoSuchFieldException nsfe) {
			java.io.IOException ioe = new java.io.IOException();
			ioe.initCause(nsfe);
			throw ioe;
		} catch (java.lang.IllegalAccessException iae) {
			java.io.IOException ioe = new java.io.IOException();
			ioe.initCause(iae);
			throw ioe;
		}

	}

	/**
	 * Serialize a {@link RealMatrix}.
	 * <p>
	 * This method is intended to be called from within a private
	 * <code>writeObject</code> method (after a call to
	 * <code>oos.defaultWriteObject()</code>) in a class that has a
	 * {@link RealMatrix} field, which should be declared <code>transient</code>.
	 * This way, the default handling does not serialize the matrix (the {@link RealMatrix} interface is not serializable by default) but this method does
	 * serialize it specifically.
	 * </p>
	 * <p>
	 * The following example shows how a simple class with a name and a real matrix
	 * should be written:
	 * <pre><code>
	 * public class NamedMatrix implements Serializable {
	 *
	 *     private final String name;
	 *     private final transient RealMatrix coefficients;
	 *
	 *     // omitted constructors, getters ...
	 *
	 *     private void writeObject(ObjectOutputStream oos) throws IOException {
	 *         oos.defaultWriteObject();  // takes care of name field
	 *         MatrixUtils.serializeRealMatrix(coefficients, oos);
	 *     }
	 *
	 *     private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	 *         ois.defaultReadObject();  // takes care of name field
	 *         MatrixUtils.deserializeRealMatrix(this, "coefficients", ois);
	 *     }
	 *
	 * }
	 * </code></pre>
	 * </p>
	 *
	 * @param matrix
	 * 		real matrix to serialize
	 * @param oos
	 * 		stream where the real matrix should be written
	 * @exception IOException
	 * 		if object cannot be written to stream
	 * @see #deserializeRealMatrix(Object, String, ObjectInputStream)
	 */ 	public static void serializeRealMatrix(final org.apache.commons.math.linear.RealMatrix matrix, final java.io.ObjectOutputStream oos) throws java.io.IOException {
		final int n = matrix.getRowDimension();
		final int m = matrix.getColumnDimension();
		oos.writeInt(n);
		oos.writeInt(m);
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < m; ++j) {
				oos.writeDouble(matrix.getEntry(i, j));
			}
		}
	}

	/**
	 * Deserialize  a {@link RealMatrix} field in a class.
	 * <p>
	 * This method is intended to be called from within a private
	 * <code>readObject</code> method (after a call to
	 * <code>ois.defaultReadObject()</code>) in a class that has a
	 * {@link RealMatrix} field, which should be declared <code>transient</code>.
	 * This way, the default handling does not deserialize the matrix (the {@link RealMatrix} interface is not serializable by default) but this method does
	 * deserialize it specifically.
	 * </p>
	 *
	 * @param instance
	 * 		instance in which the field must be set up
	 * @param fieldName
	 * 		name of the field within the class (may be private and final)
	 * @param ois
	 * 		stream from which the real matrix should be read
	 * @exception ClassNotFoundException
	 * 		if a class in the stream cannot be found
	 * @exception IOException
	 * 		if object cannot be read from the stream
	 * @see #serializeRealMatrix(RealMatrix, ObjectOutputStream)
	 */ 	public static void deserializeRealMatrix(final java.lang.Object instance, final java.lang.String fieldName, final java.io.ObjectInputStream ois) throws java.lang.ClassNotFoundException, java.io.IOException { 		try {
			// read the matrix data
			final int n = ois.readInt();
			final int m = ois.readInt();
			final double[][] data = new double[n][m];
			for (int i = 0; i < n; ++i) {
				final double[] dataI = data[i];
				for (int j = 0; j < m; ++j) {
					dataI[j] = ois.readDouble();
				}
			}

			// create the instance
			final org.apache.commons.math.linear.RealMatrix matrix = new org.apache.commons.math.linear.Array2DRowRealMatrix(data, false);

			// set up the field
			final java.lang.reflect.Field f = 
			instance.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(instance, matrix);

		} catch (java.lang.NoSuchFieldException nsfe) {
			java.io.IOException ioe = new java.io.IOException();
			ioe.initCause(nsfe);
			throw ioe;
		} catch (java.lang.IllegalAccessException iae) {
			java.io.IOException ioe = new java.io.IOException();
			ioe.initCause(iae);
			throw ioe;
		}

	}}