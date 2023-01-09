package org.apache.commons.math.stat;
/**
 * Maintains a frequency distribution.
 * <p>
 * Accepts int, long, char or Comparable values.  New values added must be
 * comparable to those that have been added, otherwise the add method will
 * throw an IllegalArgumentException.</p>
 * <p>
 * Integer values (int, long, Integer, Long) are not distinguished by type --
 * i.e. <code>addValue(Long.valueOf(2)), addValue(2), addValue(2l)</code> all have
 * the same effect (similarly for arguments to <code>getCount,</code> etc.).</p>
 * <p>
 * char values are converted by <code>addValue</code> to Character instances.
 * As such, these values are not comparable to integral values, so attempts
 * to combine integral types with chars in a frequency distribution will fail.
 * </p>
 * <p>
 * The values are ordered using the default (natural order), unless a
 * <code>Comparator</code> is supplied in the constructor.</p>
 *
 * @version $Revision$ $Date$
 */
public class Frequency implements java.io.Serializable {
	/**
	 * Serializable version identifier
	 */
	private static final long serialVersionUID = -3845586908418844111L;

	/**
	 * underlying collection
	 */
	private final java.util.TreeMap<java.lang.Comparable<?>, java.lang.Long> freqTable;

	/**
	 * Default constructor.
	 */
	public Frequency() {
		freqTable = new java.util.TreeMap<java.lang.Comparable<?>, java.lang.Long>();
	}

	/**
	 * Constructor allowing values Comparator to be specified.
	 *
	 * @param comparator
	 * 		Comparator used to order values
	 */
	// TODO is the cast OK?
	@java.lang.SuppressWarnings("unchecked")
	public Frequency(java.util.Comparator<?> comparator) {
		freqTable = new java.util.TreeMap<java.lang.Comparable<?>, java.lang.Long>(((java.util.Comparator<? super java.lang.Comparable<?>>) (comparator)));
	}

	/**
	 * Return a string representation of this frequency
	 * distribution.
	 *
	 * @return a string representation.
	 */
	@java.lang.Override
	public java.lang.String toString() {
		java.text.NumberFormat nf = java.text.NumberFormat.getPercentInstance();
		java.lang.StringBuffer outBuffer = new java.lang.StringBuffer();
		outBuffer.append("Value \t Freq. \t Pct. \t Cum Pct. \n");
		java.util.Iterator<java.lang.Comparable<?>> iter = freqTable.keySet().iterator();
		while (iter.hasNext()) {
			java.lang.Comparable<?> value = iter.next();
			outBuffer.append(value);
			outBuffer.append('\t');
			outBuffer.append(getCount(value));
			outBuffer.append('\t');
			outBuffer.append(nf.format(getPct(value)));
			outBuffer.append('\t');
			outBuffer.append(nf.format(getCumPct(value)));
			outBuffer.append('\n');
		} 
		return outBuffer.toString();
	}

	/**
	 * Adds 1 to the frequency count for v.
	 * <p>
	 * If other objects have already been added to this Frequency, v must
	 * be comparable to those that have already been added.
	 * </p>
	 *
	 * @param v
	 * 		the value to add.
	 * @throws IllegalArgumentException
	 * 		if <code>v</code> is not Comparable,
	 * 		or is not comparable with previous entries
	 * @deprecated use {@link #addValue(Comparable)} instead
	 */
	@java.lang.Deprecated
	public void addValue(java.lang.Object v) {
		if (v instanceof java.lang.Comparable<?>) {
			addValue(((java.lang.Comparable<?>) (v)));
		} else {
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("class ({0}) does not implement Comparable", v.getClass().getName());
		}
	}

	/**
	 * Adds 1 to the frequency count for v.
	 * <p>
	 * If other objects have already been added to this Frequency, v must
	 * be comparable to those that have already been added.
	 * </p>
	 *
	 * @param v
	 * 		the value to add.
	 * @throws IllegalArgumentException
	 * 		if <code>v</code> is not comparable with previous entries
	 */
	public void addValue(java.lang.Comparable<?> v) {
		java.lang.Comparable<?> obj = v;
		if (v instanceof java.lang.Integer) {
			obj = java.lang.Long.valueOf(((java.lang.Integer) (v)).longValue());
		}
		try {
			java.lang.Long count = freqTable.get(obj);
			if (count == null) {
				freqTable.put(obj, java.lang.Long.valueOf(1));
			} else {
				freqTable.put(obj, java.lang.Long.valueOf(count.longValue() + 1));
			}
		} catch (java.lang.ClassCastException ex) {
			// TreeMap will throw ClassCastException if v is not comparable
			throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException("instance of class {0} not comparable to existing values", v.getClass().getName());
		}
	}

	/**
	 * Adds 1 to the frequency count for v.
	 *
	 * @param v
	 * 		the value to add.
	 */
	public void addValue(int v) {
		addValue(java.lang.Long.valueOf(v));
	}

	/**
	 * Adds 1 to the frequency count for v.
	 *
	 * @param v
	 * 		the value to add.
	 * @deprecated to be removed in math 3.0
	 */
	@java.lang.Deprecated
	public void addValue(java.lang.Integer v) {
		addValue(java.lang.Long.valueOf(v.longValue()));
	}

	/**
	 * Adds 1 to the frequency count for v.
	 *
	 * @param v
	 * 		the value to add.
	 */
	public void addValue(long v) {
		addValue(java.lang.Long.valueOf(v));
	}

	/**
	 * Adds 1 to the frequency count for v.
	 *
	 * @param v
	 * 		the value to add.
	 */
	public void addValue(char v) {
		addValue(java.lang.Character.valueOf(v));
	}

	/**
	 * Clears the frequency table
	 */
	public void clear() {
		freqTable.clear();
	}

	/**
	 * Returns an Iterator over the set of values that have been added.
	 * <p>
	 * If added values are integral (i.e., integers, longs, Integers, or Longs),
	 * they are converted to Longs when they are added, so the objects returned
	 * by the Iterator will in this case be Longs.</p>
	 *
	 * @return values Iterator
	 */
	public java.util.Iterator<java.lang.Comparable<?>> valuesIterator() {
		return freqTable.keySet().iterator();
	}

	// -------------------------------------------------------------------------
	/**
	 * Returns the sum of all frequencies.
	 *
	 * @return the total frequency count.
	 */
	public long getSumFreq() {
		long result = 0;
		java.util.Iterator<java.lang.Long> iterator = freqTable.values().iterator();
		while (iterator.hasNext()) {
			result += iterator.next().longValue();
		} 
		return result;
	}

	/**
	 * Returns the number of values = v.
	 * Returns 0 if the value is not comparable.
	 *
	 * @param v
	 * 		the value to lookup.
	 * @return the frequency of v.
	 * @deprecated replaced by {@link #getCount(Comparable)} as of 2.0
	 */
	@java.lang.Deprecated
	public long getCount(java.lang.Object v) {
		return getCount(((java.lang.Comparable<?>) (v)));
	}

	/**
	 * Returns the number of values = v.
	 * Returns 0 if the value is not comparable.
	 *
	 * @param v
	 * 		the value to lookup.
	 * @return the frequency of v.
	 */
	public long getCount(java.lang.Comparable<?> v) {
		if (v instanceof java.lang.Integer) {
			return getCount(((java.lang.Integer) (v)).longValue());
		}
		long result = 0;
		try {
			java.lang.Long count = freqTable.get(v);
			if (count != null) {
				result = count.longValue();
			}
		} catch (java.lang.ClassCastException ex) {
			// ignore and return 0 -- ClassCastException will be thrown if value is not comparable
		}
		return result;
	}

	/**
	 * Returns the number of values = v.
	 *
	 * @param v
	 * 		the value to lookup.
	 * @return the frequency of v.
	 */
	public long getCount(int v) {
		return getCount(java.lang.Long.valueOf(v));
	}

	/**
	 * Returns the number of values = v.
	 *
	 * @param v
	 * 		the value to lookup.
	 * @return the frequency of v.
	 */
	public long getCount(long v) {
		return getCount(java.lang.Long.valueOf(v));
	}

	/**
	 * Returns the number of values = v.
	 *
	 * @param v
	 * 		the value to lookup.
	 * @return the frequency of v.
	 */
	public long getCount(char v) {
		return getCount(java.lang.Character.valueOf(v));
	}

	// -------------------------------------------------------------
	/**
	 * Returns the percentage of values that are equal to v
	 * (as a proportion between 0 and 1).
	 * <p>
	 * Returns <code>Double.NaN</code> if no values have been added.</p>
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values equal to v
	 * @deprecated replaced by {@link #getPct(Comparable)} as of 2.0
	 */
	@java.lang.Deprecated
	public double getPct(java.lang.Object v) {
		return getPct(((java.lang.Comparable<?>) (v)));
	}

	/**
	 * Returns the percentage of values that are equal to v
	 * (as a proportion between 0 and 1).
	 * <p>
	 * Returns <code>Double.NaN</code> if no values have been added.</p>
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values equal to v
	 */
	public double getPct(java.lang.Comparable<?> v) {
		final long sumFreq = getSumFreq();
		if (sumFreq == 0) {
			return java.lang.Double.NaN;
		}
		return ((double) (getCount(v))) / ((double) (sumFreq));
	}

	/**
	 * Returns the percentage of values that are equal to v
	 * (as a proportion between 0 and 1).
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values equal to v
	 */
	public double getPct(int v) {
		return getPct(java.lang.Long.valueOf(v));
	}

	/**
	 * Returns the percentage of values that are equal to v
	 * (as a proportion between 0 and 1).
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values equal to v
	 */
	public double getPct(long v) {
		return getPct(java.lang.Long.valueOf(v));
	}

	/**
	 * Returns the percentage of values that are equal to v
	 * (as a proportion between 0 and 1).
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values equal to v
	 */
	public double getPct(char v) {
		return getPct(java.lang.Character.valueOf(v));
	}

	// -----------------------------------------------------------------------------------------
	/**
	 * Returns the cumulative frequency of values less than or equal to v.
	 * <p>
	 * Returns 0 if v is not comparable to the values set.</p>
	 *
	 * @param v
	 * 		the value to lookup.
	 * @return the proportion of values equal to v
	 * @deprecated replaced by {@link #getCumFreq(Comparable)} as of 2.0
	 */
	@java.lang.Deprecated
	public long getCumFreq(java.lang.Object v) {
		return getCumFreq(((java.lang.Comparable<?>) (v)));
	}

	/**
	 * Returns the cumulative frequency of values less than or equal to v.
	 * <p>
	 * Returns 0 if v is not comparable to the values set.</p>
	 *
	 * @param v
	 * 		the value to lookup.
	 * @return the proportion of values equal to v
	 */
	public long getCumFreq(java.lang.Comparable<?> v) {
		if (getSumFreq() == 0) {
			return 0;
		}
		if (v instanceof java.lang.Integer) {
			return getCumFreq(((java.lang.Integer) (v)).longValue());
		}
		// OK, freqTable is Comparable<?>
		@java.lang.SuppressWarnings("unchecked")
		java.util.Comparator<java.lang.Comparable<?>> c = ((java.util.Comparator<java.lang.Comparable<?>>) (freqTable.comparator()));
		if (c == null) {
			c = new org.apache.commons.math.stat.Frequency.NaturalComparator();
		}
		long result = 0;
		try {
			java.lang.Long value = freqTable.get(v);
			if (value != null) {
				result = value.longValue();
			}
		} catch (java.lang.ClassCastException ex) {
			return result;// v is not comparable

		}
		if (c.compare(v, freqTable.firstKey()) < 0) {
			return 0;// v is comparable, but less than first value

		}
		if (c.compare(v, freqTable.lastKey()) >= 0) {
			return getSumFreq();// v is comparable, but greater than the last value

		}
		java.util.Iterator<java.lang.Comparable<?>> values = valuesIterator();
		while (values.hasNext()) {
			java.lang.Comparable<?> nextValue = values.next();
			if (c.compare(v, nextValue) > 0) {
				result += getCount(nextValue);
			} else {
				return result;
			}
		} 
		return result;
	}

	/**
	 * Returns the cumulative frequency of values less than or equal to v.
	 * <p>
	 * Returns 0 if v is not comparable to the values set.</p>
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values equal to v
	 */
	public long getCumFreq(int v) {
		return getCumFreq(java.lang.Long.valueOf(v));
	}

	/**
	 * Returns the cumulative frequency of values less than or equal to v.
	 * <p>
	 * Returns 0 if v is not comparable to the values set.</p>
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values equal to v
	 */
	public long getCumFreq(long v) {
		return getCumFreq(java.lang.Long.valueOf(v));
	}

	/**
	 * Returns the cumulative frequency of values less than or equal to v.
	 * <p>
	 * Returns 0 if v is not comparable to the values set.</p>
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values equal to v
	 */
	public long getCumFreq(char v) {
		return getCumFreq(java.lang.Character.valueOf(v));
	}

	// ----------------------------------------------------------------------------------------------
	/**
	 * Returns the cumulative percentage of values less than or equal to v
	 * (as a proportion between 0 and 1).
	 * <p>
	 * Returns <code>Double.NaN</code> if no values have been added.
	 * Returns 0 if at least one value has been added, but v is not comparable
	 * to the values set.</p>
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values less than or equal to v
	 * @deprecated replaced by {@link #getCumPct(Comparable)} as of 2.0
	 */
	@java.lang.Deprecated
	public double getCumPct(java.lang.Object v) {
		return getCumPct(((java.lang.Comparable<?>) (v)));
	}

	/**
	 * Returns the cumulative percentage of values less than or equal to v
	 * (as a proportion between 0 and 1).
	 * <p>
	 * Returns <code>Double.NaN</code> if no values have been added.
	 * Returns 0 if at least one value has been added, but v is not comparable
	 * to the values set.</p>
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values less than or equal to v
	 */
	public double getCumPct(java.lang.Comparable<?> v) {
		final long sumFreq = getSumFreq();
		if (sumFreq == 0) {
			return java.lang.Double.NaN;
		}
		return ((double) (getCumFreq(v))) / ((double) (sumFreq));
	}

	/**
	 * Returns the cumulative percentage of values less than or equal to v
	 * (as a proportion between 0 and 1).
	 * <p>
	 * Returns 0 if v is not comparable to the values set.</p>
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values less than or equal to v
	 */
	public double getCumPct(int v) {
		return getCumPct(java.lang.Long.valueOf(v));
	}

	/**
	 * Returns the cumulative percentage of values less than or equal to v
	 * (as a proportion between 0 and 1).
	 * <p>
	 * Returns 0 if v is not comparable to the values set.</p>
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values less than or equal to v
	 */
	public double getCumPct(long v) {
		return getCumPct(java.lang.Long.valueOf(v));
	}

	/**
	 * Returns the cumulative percentage of values less than or equal to v
	 * (as a proportion between 0 and 1).
	 * <p>
	 * Returns 0 if v is not comparable to the values set.</p>
	 *
	 * @param v
	 * 		the value to lookup
	 * @return the proportion of values less than or equal to v
	 */
	public double getCumPct(char v) {
		return getCumPct(java.lang.Character.valueOf(v));
	}

	/**
	 * A Comparator that compares comparable objects using the
	 * natural order.  Copied from Commons Collections ComparableComparator.
	 */
	private static class NaturalComparator<T extends java.lang.Comparable<T>> implements java.util.Comparator<java.lang.Comparable<T>> , java.io.Serializable {
		/**
		 * Serializable version identifier
		 */
		private static final long serialVersionUID = -3852193713161395148L;

		/**
		 * Compare the two {@link Comparable Comparable} arguments.
		 * This method is equivalent to:
		 * <pre>(({@link Comparable Comparable})o1).{@link Comparable#compareTo compareTo}(o2)</pre>
		 *
		 * @param o1
		 * 		the first object
		 * @param o2
		 * 		the second object
		 * @return result of comparison
		 * @throws NullPointerException
		 * 		when <i>o1</i> is <code>null</code>,
		 * 		or when <code>((Comparable)o1).compareTo(o2)</code> does
		 * @throws ClassCastException
		 * 		when <i>o1</i> is not a {@link Comparable Comparable},
		 * 		or when <code>((Comparable)o1).compareTo(o2)</code> does
		 */
		// cast to (T) may throw ClassCastException, see Javadoc
		@java.lang.SuppressWarnings("unchecked")
		public int compare(java.lang.Comparable<T> o1, java.lang.Comparable<T> o2) {
			return o1.compareTo(((T) (o2)));
		}
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (freqTable == null ? 0 : freqTable.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	public boolean equals(java.lang.Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof org.apache.commons.math.stat.Frequency))
			return false;

		org.apache.commons.math.stat.Frequency other = ((org.apache.commons.math.stat.Frequency) (obj));
		if (freqTable == null) {
			if (other.freqTable != null)
				return false;

		} else if (!freqTable.equals(other.freqTable))
			return false;

		return true;
	}
}