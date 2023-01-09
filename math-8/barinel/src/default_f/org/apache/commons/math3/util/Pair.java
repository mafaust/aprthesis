package org.apache.commons.math3.util;
/**
 * Generic pair.
 * <br/>
 * Although the instances of this class are immutable, it is impossible
 * to ensure that the references passed to the constructor will not be
 * modified by the caller.
 *
 * @param <K>
 * 		Key type.
 * @param <V>
 * 		Value type.
 * @since 3.0
 * @version $Id$
 */
public class Pair<K, V> {
	/**
	 * Key.
	 */
	private final K key;

	/**
	 * Value.
	 */
	private final V value;

	/**
	 * Create an entry representing a mapping from the specified key to the
	 * specified value.
	 *
	 * @param k
	 * 		Key (first element of the pair).
	 * @param v
	 * 		Value (second element of the pair).
	 */
	public Pair(K k, V v) {
		key = k;
		value = v;
	}

	/**
	 * Create an entry representing the same mapping as the specified entry.
	 *
	 * @param entry
	 * 		Entry to copy.
	 */
	public Pair(org.apache.commons.math3.util.Pair<? extends K, ? extends V> entry) {
		this(entry.getKey(), entry.getValue());
	}

	/**
	 * Get the key.
	 *
	 * @return the key (first element of the pair).
	 */
	public K getKey() {
		return key;
	}

	/**
	 * Get the value.
	 *
	 * @return the value (second element of the pair).
	 */
	public V getValue() {
		return value;
	}

	/**
	 * Get the first element of the pair.
	 *
	 * @return the first element of the pair.
	 * @since 3.1
	 */
	public K getFirst() {
		return key;
	}

	/**
	 * Get the second element of the pair.
	 *
	 * @return the second element of the pair.
	 * @since 3.1
	 */
	public V getSecond() {
		return value;
	}

	/**
	 * Compare the specified object with this entry for equality.
	 *
	 * @param o
	 * 		Object.
	 * @return {@code true} if the given object is also a map entry and
	the two entries represent the same mapping.
	 */
	@java.lang.Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof org.apache.commons.math3.util.Pair)) {
			return false;
		} else {
			org.apache.commons.math3.util.Pair<?, ?> oP = ((org.apache.commons.math3.util.Pair<?, ?>) (o));
			return (key == null ? oP.key == null : key.equals(oP.key)) && (value == null ? oP.value == null : value.equals(oP.value));
		}
	}

	/**
	 * Compute a hash code.
	 *
	 * @return the hash code value.
	 */
	@java.lang.Override
	public int hashCode() {
		int result = (key == null) ? 0 : key.hashCode();
		final int h = (value == null) ? 0 : value.hashCode();
		result = ((37 * result) + h) ^ (h >>> 16);
		return result;
	}
}