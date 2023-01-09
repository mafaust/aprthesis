package org.apache.commons.math3.exception;




















/**
 * Class to signal parse failures.
 *
 * @since 2.2
 * @version $Id$
 */
public class MathParseException extends org.apache.commons.math3.exception.MathIllegalStateException implements 
org.apache.commons.math3.exception.util.ExceptionContextProvider {
	/**
	 * Serializable version Id.
	 */ 	private static final long serialVersionUID = -6024911025449780478L;
	/**
	 *
	 *
	 * @param wrong
	 * 		Bad string representation of the object.
	 * @param position
	 * 		Index, in the {@code wrong} string, that caused the
	 * 		parsing to fail.
	 * @param type
	 * 		Class of the object supposedly represented by the
	 * 		{@code wrong} string.
	 */ 	public MathParseException(java.lang.String wrong, int position, java.lang.Class<?> type) { 		getContext().addMessage(org.apache.commons.math3.exception.util.LocalizedFormats.CANNOT_PARSE_AS_TYPE, wrong, java.lang.Integer.valueOf(position), type.getName());
	}

	/**
	 *
	 *
	 * @param wrong
	 * 		Bad string representation of the object.
	 * @param position
	 * 		Index, in the {@code wrong} string, that caused the
	 * 		parsing to fail.
	 */ 	public MathParseException(java.lang.String wrong, int position) { 		getContext().addMessage(org.apache.commons.math3.exception.util.LocalizedFormats.CANNOT_PARSE, wrong, java.lang.Integer.valueOf(position));
	}}