package org.apache.commons.math.exception.util;
/**
 * Class that contains the actual implementation of the functionality mandated
 * by the {@link ExceptionContext} interface.
 * All Commons Math exceptions delegate the interface's methods to this class.
 *
 * @since 3.0
 * @version $Id$
 */
public class ExceptionContext implements java.io.Serializable {
	/**
	 * Serializable version Id.
	 */
	private static final long serialVersionUID = -6024911025449780478L;

	/**
	 * The throwable to which this context refers to.
	 */
	private java.lang.Throwable throwable;

	/**
	 * Various informations that enrich the informative message.
	 */
	private java.util.List<org.apache.commons.math.exception.util.Localizable> msgPatterns;

	/**
	 * Various informations that enrich the informative message.
	 * The arguments will replace the corresponding place-holders in
	 * {@link #msgPatterns}.
	 */
	private java.util.List<java.lang.Object[]> msgArguments;

	/**
	 * Arbitrary context information.
	 */
	private java.util.Map<java.lang.String, java.lang.Object> context;

	/**
	 * Simple constructor.
	 *
	 * @param throwable
	 * 		the exception this context refers too
	 */
	public ExceptionContext(final java.lang.Throwable throwable) {
		this.throwable = throwable;
		msgPatterns = new java.util.ArrayList<org.apache.commons.math.exception.util.Localizable>();
		msgArguments = new java.util.ArrayList<java.lang.Object[]>();
		context = new java.util.HashMap<java.lang.String, java.lang.Object>();
	}

	/**
	 * Get a reference to the exception to which the context relates.
	 *
	 * @return a reference to the exception to which the context relates
	 */
	public java.lang.Throwable getThrowable() {
		return throwable;
	}

	/**
	 * Adds a message.
	 *
	 * @param pattern
	 * 		Message pattern.
	 * @param arguments
	 * 		Values for replacing the placeholders in the message
	 * 		pattern.
	 */
	public void addMessage(org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
		msgPatterns.add(pattern);
		msgArguments.add(org.apache.commons.math.exception.util.ArgUtils.flatten(arguments));
	}

	/**
	 * Sets the context (key, value) pair.
	 * Keys are assumed to be unique within an instance. If the same key is
	 * assigned a new value, the previous one will be lost.
	 *
	 * @param key
	 * 		Context key (not null).
	 * @param value
	 * 		Context value.
	 */
	public void setValue(java.lang.String key, java.lang.Object value) {
		context.put(key, value);
	}

	/**
	 * Gets the value associated to the given context key.
	 *
	 * @param key
	 * 		Context key.
	 * @return the context value or {@code null} if the key does not exist.
	 */
	public java.lang.Object getValue(java.lang.String key) {
		return context.get(key);
	}

	/**
	 * Gets all the keys stored in the exception
	 *
	 * @return the set of keys.
	 */
	public java.util.Set<java.lang.String> getKeys() {
		return context.keySet();
	}

	/**
	 * Gets the default message.
	 *
	 * @return the message.
	 */
	public java.lang.String getMessage() {
		return getMessage(java.util.Locale.US);
	}

	/**
	 * Gets the message in the default locale.
	 *
	 * @return the localized message.
	 */
	public java.lang.String getLocalizedMessage() {
		return getMessage(java.util.Locale.getDefault());
	}

	/**
	 * Gets the message in a specified locale.
	 *
	 * @param locale
	 * 		Locale in which the message should be translated.
	 * @return the localized message.
	 */
	public java.lang.String getMessage(final java.util.Locale locale) {
		return buildMessage(locale, ": ");
	}

	/**
	 * Gets the message in a specified locale.
	 *
	 * @param locale
	 * 		Locale in which the message should be translated.
	 * @param separator
	 * 		Separator inserted between the message parts.
	 * @return the localized message.
	 */
	public java.lang.String getMessage(final java.util.Locale locale, final java.lang.String separator) {
		return buildMessage(locale, separator);
	}

	/**
	 * Builds a message string.
	 *
	 * @param locale
	 * 		Locale in which the message should be translated.
	 * @param separator
	 * 		Message separator.
	 * @return a localized message string.
	 */
	private java.lang.String buildMessage(java.util.Locale locale, java.lang.String separator) {
		final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		int count = 0;
		final int len = msgPatterns.size();
		for (int i = 0; i < len; i++) {
			final org.apache.commons.math.exception.util.Localizable pat = msgPatterns.get(i);
			final java.lang.Object[] args = msgArguments.get(i);
			final java.text.MessageFormat fmt = new java.text.MessageFormat(pat.getLocalizedString(locale), locale);
			sb.append(fmt.format(args));
			if ((++count) < len) {
				// Add a separator if there are other messages.
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * Serialize this object to the given stream.
	 *
	 * @param out
	 * 		Stream.
	 * @throws IOException
	 * 		This should never happen.
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
		out.writeObject(throwable);
		serializeMessages(out);
		serializeContext(out);
	}

	/**
	 * Deserialize this object from the given stream.
	 *
	 * @param in
	 * 		Stream.
	 * @throws IOException
	 * 		This should never happen.
	 * @throws ClassNotFoundException
	 * 		This should never happen.
	 */
	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
		throwable = ((java.lang.Throwable) (in.readObject()));
		deSerializeMessages(in);
		deSerializeContext(in);
	}

	/**
	 * Serialize  {@link #msgPatterns} and {@link #msgArguments}.
	 *
	 * @param out
	 * 		Stream.
	 * @throws IOException
	 * 		This should never happen.
	 */
	private void serializeMessages(java.io.ObjectOutputStream out) throws java.io.IOException {
		// Step 1.
		final int len = msgPatterns.size();
		out.writeInt(len);
		// Step 2.
		for (int i = 0; i < len; i++) {
			final org.apache.commons.math.exception.util.Localizable pat = msgPatterns.get(i);
			// Step 3.
			out.writeObject(pat);
			final java.lang.Object[] args = msgArguments.get(i);
			final int aLen = args.length;
			// Step 4.
			out.writeInt(aLen);
			for (int j = 0; j < aLen; j++) {
				if (args[j] instanceof java.io.Serializable) {
					// Step 5a.
					out.writeObject(args[j]);
				} else {
					// Step 5b.
					out.writeObject(nonSerializableReplacement(args[j]));
				}
			}
		}
	}

	/**
	 * Deserialize {@link #msgPatterns} and {@link #msgArguments}.
	 *
	 * @param in
	 * 		Stream.
	 * @throws IOException
	 * 		This should never happen.
	 * @throws ClassNotFoundException
	 * 		This should never happen.
	 */
	private void deSerializeMessages(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
		// Step 1.
		final int len = in.readInt();
		msgPatterns = new java.util.ArrayList<org.apache.commons.math.exception.util.Localizable>(len);
		msgArguments = new java.util.ArrayList<java.lang.Object[]>(len);
		// Step 2.
		for (int i = 0; i < len; i++) {
			// Step 3.
			final org.apache.commons.math.exception.util.Localizable pat = ((org.apache.commons.math.exception.util.Localizable) (in.readObject()));
			msgPatterns.add(pat);
			// Step 4.
			final int aLen = in.readInt();
			final java.lang.Object[] args = new java.lang.Object[aLen];
			for (int j = 0; j < aLen; j++) {
				// Step 5.
				args[j] = in.readObject();
			}
			msgArguments.add(args);
		}
	}

	/**
	 * Serialize {@link #context}.
	 *
	 * @param out
	 * 		Stream.
	 * @throws IOException
	 * 		This should never happen.
	 */
	private void serializeContext(java.io.ObjectOutputStream out) throws java.io.IOException {
		// Step 1.
		final int len = context.keySet().size();
		out.writeInt(len);
		for (java.lang.String key : context.keySet()) {
			// Step 2.
			out.writeObject(key);
			final java.lang.Object value = context.get(key);
			if (value instanceof java.io.Serializable) {
				// Step 3a.
				out.writeObject(value);
			} else {
				// Step 3b.
				out.writeObject(nonSerializableReplacement(value));
			}
		}
	}

	/**
	 * Deserialize {@link #context}.
	 *
	 * @param in
	 * 		Stream.
	 * @throws IOException
	 * 		This should never happen.
	 * @throws ClassNotFoundException
	 * 		This should never happen.
	 */
	private void deSerializeContext(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
		// Step 1.
		final int len = in.readInt();
		context = new java.util.HashMap<java.lang.String, java.lang.Object>();
		for (int i = 0; i < len; i++) {
			// Step 2.
			final java.lang.String key = ((java.lang.String) (in.readObject()));
			// Step 3.
			final java.lang.Object value = in.readObject();
			context.put(key, value);
		}
	}

	/**
	 * Replaces a non-serializable object with an error message string.
	 *
	 * @param obj
	 * 		Object that does not implement the {@code Serializable}
	 * 		interface.
	 * @return a string that mentions which class could not be serialized.
	 */
	private java.lang.String nonSerializableReplacement(java.lang.Object obj) {
		return ("[Object could not be serialized: " + obj.getClass().getName()) + "]";
	}
}