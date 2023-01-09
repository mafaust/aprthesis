package org.apache.commons.math.linear;
/**
 * Basic implementation of RealMatrix methods regardless of the underlying storage.
 * <p>All the methods implemented here use {@link #getEntry(int, int)} to access
 * matrix elements. Derived class can provide faster implementations. </p>
 *
 * @version $Revision$ $Date$
 * @since 2.0
 */
public abstract class AbstractRealMatrix implements org.apache.commons.math.linear.RealMatrix {
	/**
	 * Cached LU solver.
	 *
	 * @deprecated as of release 2.0, since all methods using this are deprecated
	 */
	@java.lang.Deprecated
	private org.apache.commons.math.linear.DecompositionSolver lu;

	/**
	 * Creates a matrix with no data
	 */
	protected AbstractRealMatrix() {
		lu = null;
	}

	/**
	 * Create a new RealMatrix with the supplied row and column dimensions.
	 *
	 * @param rowDimension
	 * 		the number of rows in the new matrix
	 * @param columnDimension
	 * 		the number of columns in the new matrix
	 * @throws IllegalArgumentException
	 * 		if row or column dimension is not positive
	 */
	protected AbstractRealMatrix(final int rowDimension, final int columnDimension) throws java.lang.IllegalArgumentException {
		if (rowDimension <= 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("invalid row dimension {0} (must be positive)", rowDimension);
		}
		if (columnDimension <= 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("invalid column dimension {0} (must be positive)", columnDimension);
		}
		lu = null;
	}

	/**
	 * {@inheritDoc }
	 */
	public abstract org.apache.commons.math.linear.RealMatrix createMatrix(final int rowDimension, final int columnDimension) throws java.lang.IllegalArgumentException;

	/**
	 * {@inheritDoc }
	 */
	public abstract org.apache.commons.math.linear.RealMatrix copy();

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix add(org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
		// safety check
		org.apache.commons.math.linear.MatrixUtils.checkAdditionCompatible(this, m);
		final int rowCount = getRowDimension();
		final int columnCount = getColumnDimension();
		final org.apache.commons.math.linear.RealMatrix out = createMatrix(rowCount, columnCount);
		for (int row = 0; row < rowCount; ++row) {
			for (int col = 0; col < columnCount; ++col) {
				out.setEntry(row, col, getEntry(row, col) + m.getEntry(row, col));
			}
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix subtract(final org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
		// safety check
		org.apache.commons.math.linear.MatrixUtils.checkSubtractionCompatible(this, m);
		final int rowCount = getRowDimension();
		final int columnCount = getColumnDimension();
		final org.apache.commons.math.linear.RealMatrix out = createMatrix(rowCount, columnCount);
		for (int row = 0; row < rowCount; ++row) {
			for (int col = 0; col < columnCount; ++col) {
				out.setEntry(row, col, getEntry(row, col) - m.getEntry(row, col));
			}
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix scalarAdd(final double d) {
		final int rowCount = getRowDimension();
		final int columnCount = getColumnDimension();
		final org.apache.commons.math.linear.RealMatrix out = createMatrix(rowCount, columnCount);
		for (int row = 0; row < rowCount; ++row) {
			for (int col = 0; col < columnCount; ++col) {
				out.setEntry(row, col, getEntry(row, col) + d);
			}
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix scalarMultiply(final double d) {
		final int rowCount = getRowDimension();
		final int columnCount = getColumnDimension();
		final org.apache.commons.math.linear.RealMatrix out = createMatrix(rowCount, columnCount);
		for (int row = 0; row < rowCount; ++row) {
			for (int col = 0; col < columnCount; ++col) {
				out.setEntry(row, col, getEntry(row, col) * d);
			}
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix multiply(final org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
		// safety check
		org.apache.commons.math.linear.MatrixUtils.checkMultiplicationCompatible(this, m);
		final int nRows = getRowDimension();
		final int nCols = m.getColumnDimension();
		final int nSum = getColumnDimension();
		final org.apache.commons.math.linear.RealMatrix out = createMatrix(nRows, nCols);
		for (int row = 0; row < nRows; ++row) {
			for (int col = 0; col < nCols; ++col) {
				double sum = 0;
				for (int i = 0; i < nSum; ++i) {
					sum += getEntry(row, i) * m.getEntry(i, col);
				}
				out.setEntry(row, col, sum);
			}
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix preMultiply(final org.apache.commons.math.linear.RealMatrix m) throws java.lang.IllegalArgumentException {
		return m.multiply(this);
	}

	/**
	 * {@inheritDoc }
	 */
	public double[][] getData() {
		final double[][] data = new double[getRowDimension()][getColumnDimension()];
		for (int i = 0; i < data.length; ++i) {
			final double[] dataI = data[i];
			for (int j = 0; j < dataI.length; ++j) {
				dataI[j] = getEntry(i, j);
			}
		}
		return data;
	}

	/**
	 * {@inheritDoc }
	 */
	public double getNorm() {
		return walkInColumnOrder(new org.apache.commons.math.linear.RealMatrixPreservingVisitor() {
			/**
			 * Last row index.
			 */
			private double endRow;

			/**
			 * Sum of absolute values on one column.
			 */
			private double columnSum;

			/**
			 * Maximal sum across all columns.
			 */
			private double maxColSum;

			/**
			 * {@inheritDoc }
			 */
			public void start(final int rows, final int columns, final int startRow, final int endRow, final int startColumn, final int endColumn) {
				this.endRow = endRow;
				columnSum = 0;
				maxColSum = 0;
			}

			/**
			 * {@inheritDoc }
			 */
			public void visit(final int row, final int column, final double value) {
				columnSum += java.lang.Math.abs(value);
				if (row == endRow) {
					maxColSum = java.lang.Math.max(maxColSum, columnSum);
					columnSum = 0;
				}
			}

			/**
			 * {@inheritDoc }
			 */
			public double end() {
				return maxColSum;
			}
		});
	}

	/**
	 * {@inheritDoc }
	 */
	public double getFrobeniusNorm() {
		return walkInOptimizedOrder(new org.apache.commons.math.linear.RealMatrixPreservingVisitor() {
			/**
			 * Sum of squared entries.
			 */
			private double sum;

			/**
			 * {@inheritDoc }
			 */
			public void start(final int rows, final int columns, final int startRow, final int endRow, final int startColumn, final int endColumn) {
				sum = 0;
			}

			/**
			 * {@inheritDoc }
			 */
			public void visit(final int row, final int column, final double value) {
				sum += value * value;
			}

			/**
			 * {@inheritDoc }
			 */
			public double end() {
				return java.lang.Math.sqrt(sum);
			}
		});
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix getSubMatrix(final int startRow, final int endRow, final int startColumn, final int endColumn) throws org.apache.commons.math.linear.MatrixIndexException {
		org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
		final org.apache.commons.math.linear.RealMatrix subMatrix = createMatrix((endRow - startRow) + 1, (endColumn - startColumn) + 1);
		for (int i = startRow; i <= endRow; ++i) {
			for (int j = startColumn; j <= endColumn; ++j) {
				subMatrix.setEntry(i - startRow, j - startColumn, getEntry(i, j));
			}
		}
		return subMatrix;
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix getSubMatrix(final int[] selectedRows, final int[] selectedColumns) throws org.apache.commons.math.linear.MatrixIndexException {
		// safety checks
		org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, selectedRows, selectedColumns);
		// copy entries
		final org.apache.commons.math.linear.RealMatrix subMatrix = createMatrix(selectedRows.length, selectedColumns.length);
		subMatrix.walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultRealMatrixChangingVisitor() {
			/**
			 * {@inheritDoc }
			 */
			@java.lang.Override
			public double visit(final int row, final int column, final double value) {
				return getEntry(selectedRows[row], selectedColumns[column]);
			}
		});
		return subMatrix;
	}

	/**
	 * {@inheritDoc }
	 */
	public void copySubMatrix(final int startRow, final int endRow, final int startColumn, final int endColumn, final double[][] destination) throws org.apache.commons.math.linear.MatrixIndexException, java.lang.IllegalArgumentException {
		// safety checks
		org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
		final int rowsCount = (endRow + 1) - startRow;
		final int columnsCount = (endColumn + 1) - startColumn;
		if ((destination.length < rowsCount) || (destination[0].length < columnsCount)) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("dimensions mismatch: got {0}x{1} but expected {2}x{3}", destination.length, destination[0].length, rowsCount, columnsCount);
		}
		// copy entries
		walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultRealMatrixPreservingVisitor() {
			/**
			 * Initial row index.
			 */
			private int startRow;

			/**
			 * Initial column index.
			 */
			private int startColumn;

			/**
			 * {@inheritDoc }
			 */
			@java.lang.Override
			public void start(final int rows, final int columns, final int startRow, final int endRow, final int startColumn, final int endColumn) {
				this.startRow = startRow;
				this.startColumn = startColumn;
			}

			/**
			 * {@inheritDoc }
			 */
			@java.lang.Override
			public void visit(final int row, final int column, final double value) {
				destination[row - startRow][column - startColumn] = value;
			}
		}, startRow, endRow, startColumn, endColumn);
	}

	/**
	 * {@inheritDoc }
	 */
	public void copySubMatrix(int[] selectedRows, int[] selectedColumns, double[][] destination) throws org.apache.commons.math.linear.MatrixIndexException, java.lang.IllegalArgumentException {
		// safety checks
		org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, selectedRows, selectedColumns);
		if ((destination.length < selectedRows.length) || (destination[0].length < selectedColumns.length)) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("dimensions mismatch: got {0}x{1} but expected {2}x{3}", destination.length, destination[0].length, selectedRows.length, selectedColumns.length);
		}
		// copy entries
		for (int i = 0; i < selectedRows.length; i++) {
			final double[] destinationI = destination[i];
			for (int j = 0; j < selectedColumns.length; j++) {
				destinationI[j] = getEntry(selectedRows[i], selectedColumns[j]);
			}
		}
	}

	/**
	 * {@inheritDoc }
	 */
	public void setSubMatrix(final double[][] subMatrix, final int row, final int column) throws org.apache.commons.math.linear.MatrixIndexException {
		final int nRows = subMatrix.length;
		if (nRows == 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("matrix must have at least one row");
		}
		final int nCols = subMatrix[0].length;
		if (nCols == 0) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("matrix must have at least one column");
		}
		for (int r = 1; r < nRows; ++r) {
			if (subMatrix[r].length != nCols) {
				throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("some rows have length {0} while others have length {1}", nCols, subMatrix[r].length);
			}
		}
		org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
		org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
		org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, (nRows + row) - 1);
		org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, (nCols + column) - 1);
		for (int i = 0; i < nRows; ++i) {
			for (int j = 0; j < nCols; ++j) {
				setEntry(row + i, column + j, subMatrix[i][j]);
			}
		}
		lu = null;
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix getRowMatrix(final int row) throws org.apache.commons.math.linear.MatrixIndexException {
		org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
		final int nCols = getColumnDimension();
		final org.apache.commons.math.linear.RealMatrix out = createMatrix(1, nCols);
		for (int i = 0; i < nCols; ++i) {
			out.setEntry(0, i, getEntry(row, i));
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public void setRowMatrix(final int row, final org.apache.commons.math.linear.RealMatrix matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
		org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
		final int nCols = getColumnDimension();
		if ((matrix.getRowDimension() != 1) || (matrix.getColumnDimension() != nCols)) {
			throw new org.apache.commons.math.linear.InvalidMatrixException("dimensions mismatch: got {0}x{1} but expected {2}x{3}", matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
		}
		for (int i = 0; i < nCols; ++i) {
			setEntry(row, i, matrix.getEntry(0, i));
		}
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix getColumnMatrix(final int column) throws org.apache.commons.math.linear.MatrixIndexException {
		org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
		final int nRows = getRowDimension();
		final org.apache.commons.math.linear.RealMatrix out = createMatrix(nRows, 1);
		for (int i = 0; i < nRows; ++i) {
			out.setEntry(i, 0, getEntry(i, column));
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public void setColumnMatrix(final int column, final org.apache.commons.math.linear.RealMatrix matrix) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
		org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
		final int nRows = getRowDimension();
		if ((matrix.getRowDimension() != nRows) || (matrix.getColumnDimension() != 1)) {
			throw new org.apache.commons.math.linear.InvalidMatrixException("dimensions mismatch: got {0}x{1} but expected {2}x{3}", matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
		}
		for (int i = 0; i < nRows; ++i) {
			setEntry(i, column, matrix.getEntry(i, 0));
		}
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealVector getRowVector(final int row) throws org.apache.commons.math.linear.MatrixIndexException {
		return new org.apache.commons.math.linear.ArrayRealVector(getRow(row), false);
	}

	/**
	 * {@inheritDoc }
	 */
	public void setRowVector(final int row, final org.apache.commons.math.linear.RealVector vector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
		org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
		final int nCols = getColumnDimension();
		if (vector.getDimension() != nCols) {
			throw new org.apache.commons.math.linear.InvalidMatrixException("dimensions mismatch: got {0}x{1} but expected {2}x{3}", 1, vector.getDimension(), 1, nCols);
		}
		for (int i = 0; i < nCols; ++i) {
			setEntry(row, i, vector.getEntry(i));
		}
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealVector getColumnVector(final int column) throws org.apache.commons.math.linear.MatrixIndexException {
		return new org.apache.commons.math.linear.ArrayRealVector(getColumn(column), false);
	}

	/**
	 * {@inheritDoc }
	 */
	public void setColumnVector(final int column, final org.apache.commons.math.linear.RealVector vector) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
		org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
		final int nRows = getRowDimension();
		if (vector.getDimension() != nRows) {
			throw new org.apache.commons.math.linear.InvalidMatrixException("dimensions mismatch: got {0}x{1} but expected {2}x{3}", vector.getDimension(), 1, nRows, 1);
		}
		for (int i = 0; i < nRows; ++i) {
			setEntry(i, column, vector.getEntry(i));
		}
	}

	/**
	 * {@inheritDoc }
	 */
	public double[] getRow(final int row) throws org.apache.commons.math.linear.MatrixIndexException {
		org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
		final int nCols = getColumnDimension();
		final double[] out = new double[nCols];
		for (int i = 0; i < nCols; ++i) {
			out[i] = getEntry(row, i);
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public void setRow(final int row, final double[] array) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
		org.apache.commons.math.linear.MatrixUtils.checkRowIndex(this, row);
		final int nCols = getColumnDimension();
		if (array.length != nCols) {
			throw new org.apache.commons.math.linear.InvalidMatrixException("dimensions mismatch: got {0}x{1} but expected {2}x{3}", 1, array.length, 1, nCols);
		}
		for (int i = 0; i < nCols; ++i) {
			setEntry(row, i, array[i]);
		}
	}

	/**
	 * {@inheritDoc }
	 */
	public double[] getColumn(final int column) throws org.apache.commons.math.linear.MatrixIndexException {
		org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
		final int nRows = getRowDimension();
		final double[] out = new double[nRows];
		for (int i = 0; i < nRows; ++i) {
			out[i] = getEntry(i, column);
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public void setColumn(final int column, final double[] array) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.InvalidMatrixException {
		org.apache.commons.math.linear.MatrixUtils.checkColumnIndex(this, column);
		final int nRows = getRowDimension();
		if (array.length != nRows) {
			throw new org.apache.commons.math.linear.InvalidMatrixException("dimensions mismatch: got {0}x{1} but expected {2}x{3}", array.length, 1, nRows, 1);
		}
		for (int i = 0; i < nRows; ++i) {
			setEntry(i, column, array[i]);
		}
	}

	/**
	 * {@inheritDoc }
	 */
	public abstract double getEntry(int row, int column) throws org.apache.commons.math.linear.MatrixIndexException;

	/**
	 * {@inheritDoc }
	 */
	public abstract void setEntry(int row, int column, double value) throws org.apache.commons.math.linear.MatrixIndexException;

	/**
	 * {@inheritDoc }
	 */
	public abstract void addToEntry(int row, int column, double increment) throws org.apache.commons.math.linear.MatrixIndexException;

	/**
	 * {@inheritDoc }
	 */
	public abstract void multiplyEntry(int row, int column, double factor) throws org.apache.commons.math.linear.MatrixIndexException;

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealMatrix transpose() {
		final int nRows = getRowDimension();
		final int nCols = getColumnDimension();
		final org.apache.commons.math.linear.RealMatrix out = createMatrix(nCols, nRows);
		walkInOptimizedOrder(new org.apache.commons.math.linear.DefaultRealMatrixPreservingVisitor() {
			/**
			 * {@inheritDoc }
			 */
			@java.lang.Override
			public void visit(final int row, final int column, final double value) {
				out.setEntry(column, row, value);
			}
		});
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Deprecated
	public org.apache.commons.math.linear.RealMatrix inverse() throws org.apache.commons.math.linear.InvalidMatrixException {
		if (lu == null) {
			lu = new org.apache.commons.math.linear.LUDecompositionImpl(this, org.apache.commons.math.util.MathUtils.SAFE_MIN).getSolver();
		}
		return lu.getInverse();
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Deprecated
	public double getDeterminant() throws org.apache.commons.math.linear.InvalidMatrixException {
		return new org.apache.commons.math.linear.LUDecompositionImpl(this, org.apache.commons.math.util.MathUtils.SAFE_MIN).getDeterminant();
	}

	/**
	 * {@inheritDoc }
	 */
	public boolean isSquare() {
		return getColumnDimension() == getRowDimension();
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Deprecated
	public boolean isSingular() {
		if (lu == null) {
			lu = new org.apache.commons.math.linear.LUDecompositionImpl(this, org.apache.commons.math.util.MathUtils.SAFE_MIN).getSolver();
		}
		return !lu.isNonSingular();
	}

	/**
	 * {@inheritDoc }
	 */
	public abstract int getRowDimension();

	/**
	 * {@inheritDoc }
	 */
	public abstract int getColumnDimension();

	/**
	 * {@inheritDoc }
	 */
	public double getTrace() throws org.apache.commons.math.linear.NonSquareMatrixException {
		final int nRows = getRowDimension();
		final int nCols = getColumnDimension();
		if (nRows != nCols) {
			throw new org.apache.commons.math.linear.NonSquareMatrixException(nRows, nCols);
		}
		double trace = 0;
		for (int i = 0; i < nRows; ++i) {
			trace += getEntry(i, i);
		}
		return trace;
	}

	/**
	 * {@inheritDoc }
	 */
	public double[] operate(final double[] v) throws java.lang.IllegalArgumentException {
		final int nRows = getRowDimension();
		final int nCols = getColumnDimension();
		if (v.length != nCols) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("vector length mismatch: got {0} but expected {1}", v.length, nCols);
		}
		final double[] out = new double[nRows];
		for (int row = 0; row < nRows; ++row) {
			double sum = 0;
			for (int i = 0; i < nCols; ++i) {
				sum += getEntry(row, i) * v[i];
			}
			out[row] = sum;
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealVector operate(final org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return new org.apache.commons.math.linear.ArrayRealVector(operate(((org.apache.commons.math.linear.ArrayRealVector) (v)).getDataRef()), false);
		} catch (java.lang.ClassCastException cce) {
			final int nRows = getRowDimension();
			final int nCols = getColumnDimension();
			if (v.getDimension() != nCols) {
				throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("vector length mismatch: got {0} but expected {1}", v.getDimension(), nCols);
			}
			final double[] out = new double[nRows];
			for (int row = 0; row < nRows; ++row) {
				double sum = 0;
				for (int i = 0; i < nCols; ++i) {
					sum += getEntry(row, i) * v.getEntry(i);
				}
				out[row] = sum;
			}
			return new org.apache.commons.math.linear.ArrayRealVector(out, false);
		}
	}

	/**
	 * {@inheritDoc }
	 */
	public double[] preMultiply(final double[] v) throws java.lang.IllegalArgumentException {
		final int nRows = getRowDimension();
		final int nCols = getColumnDimension();
		if (v.length != nRows) {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("vector length mismatch: got {0} but expected {1}", v.length, nRows);
		}
		final double[] out = new double[nCols];
		for (int col = 0; col < nCols; ++col) {
			double sum = 0;
			for (int i = 0; i < nRows; ++i) {
				sum += getEntry(i, col) * v[i];
			}
			out[col] = sum;
		}
		return out;
	}

	/**
	 * {@inheritDoc }
	 */
	public org.apache.commons.math.linear.RealVector preMultiply(final org.apache.commons.math.linear.RealVector v) throws java.lang.IllegalArgumentException {
		try {
			return new org.apache.commons.math.linear.ArrayRealVector(preMultiply(((org.apache.commons.math.linear.ArrayRealVector) (v)).getDataRef()), false);
		} catch (java.lang.ClassCastException cce) {
			final int nRows = getRowDimension();
			final int nCols = getColumnDimension();
			if (v.getDimension() != nRows) {
				throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("vector length mismatch: got {0} but expected {1}", v.getDimension(), nRows);
			}
			final double[] out = new double[nCols];
			for (int col = 0; col < nCols; ++col) {
				double sum = 0;
				for (int i = 0; i < nRows; ++i) {
					sum += getEntry(i, col) * v.getEntry(i);
				}
				out[col] = sum;
			}
			return new org.apache.commons.math.linear.ArrayRealVector(out);
		}
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInRowOrder(final org.apache.commons.math.linear.RealMatrixChangingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
		final int rows = getRowDimension();
		final int columns = getColumnDimension();
		visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				final double oldValue = getEntry(row, column);
				final double newValue = visitor.visit(row, column, oldValue);
				setEntry(row, column, newValue);
			}
		}
		lu = null;
		return visitor.end();
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInRowOrder(final org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
		final int rows = getRowDimension();
		final int columns = getColumnDimension();
		visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				visitor.visit(row, column, getEntry(row, column));
			}
		}
		return visitor.end();
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInRowOrder(final org.apache.commons.math.linear.RealMatrixChangingVisitor visitor, final int startRow, final int endRow, final int startColumn, final int endColumn) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.MatrixVisitorException {
		org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
		visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
		for (int row = startRow; row <= endRow; ++row) {
			for (int column = startColumn; column <= endColumn; ++column) {
				final double oldValue = getEntry(row, column);
				final double newValue = visitor.visit(row, column, oldValue);
				setEntry(row, column, newValue);
			}
		}
		lu = null;
		return visitor.end();
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInRowOrder(final org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor, final int startRow, final int endRow, final int startColumn, final int endColumn) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.MatrixVisitorException {
		org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
		visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
		for (int row = startRow; row <= endRow; ++row) {
			for (int column = startColumn; column <= endColumn; ++column) {
				visitor.visit(row, column, getEntry(row, column));
			}
		}
		return visitor.end();
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInColumnOrder(final org.apache.commons.math.linear.RealMatrixChangingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
		final int rows = getRowDimension();
		final int columns = getColumnDimension();
		visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
		for (int column = 0; column < columns; ++column) {
			for (int row = 0; row < rows; ++row) {
				final double oldValue = getEntry(row, column);
				final double newValue = visitor.visit(row, column, oldValue);
				setEntry(row, column, newValue);
			}
		}
		lu = null;
		return visitor.end();
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInColumnOrder(final org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
		final int rows = getRowDimension();
		final int columns = getColumnDimension();
		visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
		for (int column = 0; column < columns; ++column) {
			for (int row = 0; row < rows; ++row) {
				visitor.visit(row, column, getEntry(row, column));
			}
		}
		return visitor.end();
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInColumnOrder(final org.apache.commons.math.linear.RealMatrixChangingVisitor visitor, final int startRow, final int endRow, final int startColumn, final int endColumn) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.MatrixVisitorException {
		org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
		visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
		for (int column = startColumn; column <= endColumn; ++column) {
			for (int row = startRow; row <= endRow; ++row) {
				final double oldValue = getEntry(row, column);
				final double newValue = visitor.visit(row, column, oldValue);
				setEntry(row, column, newValue);
			}
		}
		lu = null;
		return visitor.end();
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInColumnOrder(final org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor, final int startRow, final int endRow, final int startColumn, final int endColumn) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.MatrixVisitorException {
		org.apache.commons.math.linear.MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
		visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
		for (int column = startColumn; column <= endColumn; ++column) {
			for (int row = startRow; row <= endRow; ++row) {
				visitor.visit(row, column, getEntry(row, column));
			}
		}
		return visitor.end();
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInOptimizedOrder(final org.apache.commons.math.linear.RealMatrixChangingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
		return walkInRowOrder(visitor);
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInOptimizedOrder(final org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor) throws org.apache.commons.math.linear.MatrixVisitorException {
		return walkInRowOrder(visitor);
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInOptimizedOrder(final org.apache.commons.math.linear.RealMatrixChangingVisitor visitor, final int startRow, final int endRow, final int startColumn, final int endColumn) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.MatrixVisitorException {
		return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
	}

	/**
	 * {@inheritDoc }
	 */
	public double walkInOptimizedOrder(final org.apache.commons.math.linear.RealMatrixPreservingVisitor visitor, final int startRow, final int endRow, final int startColumn, final int endColumn) throws org.apache.commons.math.linear.MatrixIndexException, org.apache.commons.math.linear.MatrixVisitorException {
		return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Deprecated
	public double[] solve(final double[] b) throws java.lang.IllegalArgumentException, org.apache.commons.math.linear.InvalidMatrixException {
		if (lu == null) {
			lu = new org.apache.commons.math.linear.LUDecompositionImpl(this, org.apache.commons.math.util.MathUtils.SAFE_MIN).getSolver();
		}
		return lu.solve(b);
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Deprecated
	public org.apache.commons.math.linear.RealMatrix solve(final org.apache.commons.math.linear.RealMatrix b) throws java.lang.IllegalArgumentException, org.apache.commons.math.linear.InvalidMatrixException {
		if (lu == null) {
			lu = new org.apache.commons.math.linear.LUDecompositionImpl(this, org.apache.commons.math.util.MathUtils.SAFE_MIN).getSolver();
		}
		return lu.solve(b);
	}

	/**
	 * Computes a new
	 * <a href="http://www.math.gatech.edu/~bourbaki/math2601/Web-notes/2num.pdf">
	 * LU decomposition</a> for this matrix, storing the result for use by other methods.
	 * <p>
	 * <strong>Implementation Note</strong>:<br>
	 * Uses <a href="http://www.damtp.cam.ac.uk/user/fdl/people/sd/lectures/nummeth98/linear.htm">
	 * Crout's algorithm</a>, with partial pivoting.</p>
	 * <p>
	 * <strong>Usage Note</strong>:<br>
	 * This method should rarely be invoked directly. Its only use is
	 * to force recomputation of the LU decomposition when changes have been
	 * made to the underlying data using direct array references. Changes
	 * made using setXxx methods will trigger recomputation when needed
	 * automatically.</p>
	 *
	 * @throws InvalidMatrixException
	 * 		if the matrix is non-square or singular.
	 * @deprecated as of release 2.0, replaced by {@link LUDecomposition}
	 */
	@java.lang.Deprecated
	public void luDecompose() throws org.apache.commons.math.linear.InvalidMatrixException {
		if (lu == null) {
			lu = new org.apache.commons.math.linear.LUDecompositionImpl(this, org.apache.commons.math.util.MathUtils.SAFE_MIN).getSolver();
		}
	}

	/**
	 * Get a string representation for this matrix.
	 *
	 * @return a string representation for this matrix
	 */
	@java.lang.Override
	public java.lang.String toString() {
		final int nRows = getRowDimension();
		final int nCols = getColumnDimension();
		final java.lang.StringBuffer res = new java.lang.StringBuffer();
		java.lang.String fullClassName = getClass().getName();
		java.lang.String shortClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
		res.append(shortClassName).append("{");
		for (int i = 0; i < nRows; ++i) {
			if (i > 0) {
				res.append(",");
			}
			res.append("{");
			for (int j = 0; j < nCols; ++j) {
				if (j > 0) {
					res.append(",");
				}
				res.append(getEntry(i, j));
			}
			res.append("}");
		}
		res.append("}");
		return res.toString();
	}

	/**
	 * Returns true iff <code>object</code> is a
	 * <code>RealMatrix</code> instance with the same dimensions as this
	 * and all corresponding matrix entries are equal.
	 *
	 * @param object
	 * 		the object to test equality against.
	 * @return true if object equals this
	 */
	@java.lang.Override
	public boolean equals(final java.lang.Object object) {
		if (object == this) {
			return true;
		}
		if ((object instanceof org.apache.commons.math.linear.RealMatrix) == false) {
			return false;
		}
		org.apache.commons.math.linear.RealMatrix m = ((org.apache.commons.math.linear.RealMatrix) (object));
		final int nRows = getRowDimension();
		final int nCols = getColumnDimension();
		if ((m.getColumnDimension() != nCols) || (m.getRowDimension() != nRows)) {
			return false;
		}
		for (int row = 0; row < nRows; ++row) {
			for (int col = 0; col < nCols; ++col) {
				if (getEntry(row, col) != m.getEntry(row, col)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Computes a hashcode for the matrix.
	 *
	 * @return hashcode for matrix
	 */
	@java.lang.Override
	public int hashCode() {
		int ret = 7;
		final int nRows = getRowDimension();
		final int nCols = getColumnDimension();
		ret = (ret * 31) + nRows;
		ret = (ret * 31) + nCols;
		for (int row = 0; row < nRows; ++row) {
			for (int col = 0; col < nCols; ++col) {
				ret = (ret * 31) + (((11 * (row + 1)) + (17 * (col + 1))) * org.apache.commons.math.util.MathUtils.hash(getEntry(row, col)));
			}
		}
		return ret;
	}
}