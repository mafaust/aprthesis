package org.apache.commons.math;
/**
 * Base class for commons-math checked exceptions.
 * <p>
 * Supports nesting, emulating JDK 1.4 behavior if necessary.</p>
 * <p>
 * Adapted from <a href="http://commons.apache.org/collections/api-release/org/apache/commons/collections/FunctorException.html"/>.</p>
 *
 * @version $Revision$ $Date$
 */
public class MathException extends java.lang.Exception {
	/**
	 * Serializable version identifier.
	 */
	private static final long serialVersionUID = -9004610152740737812L;

	/**
	 * Pattern used to build the message.
	 */
	private final java.lang.String pattern;

	/**
	 * Arguments used to build the message.
	 */
	private final java.lang.Object[] arguments;

	/**
	 * Translate a string to a given locale.
	 *
	 * @param s
	 * 		string to translate
	 * @param locale
	 * 		locale into which to translate the string
	 * @return translated string or original string
	for unsupported locales or unknown strings
	 */
	private static java.lang.String translate(java.lang.String s, java.util.Locale locale) {
		try {
			java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org.apache.commons.math.MessagesResources", locale);
			if (bundle.getLocale().getLanguage().equals(locale.getLanguage())) {
				// the value of the resource is the translated string
				return bundle.getString(s);
			}
		} catch (java.util.MissingResourceException mre) {
			// do nothing here
		}
		// the locale is not supported or the resource is unknown
		// don't translate and fall back to using the string as is
		return s;
	}

	/**
	 * Builds a message string by from a pattern and its arguments.
	 *
	 * @param locale
	 * 		Locale in which the message should be translated
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @return a message string
	 */
	private static java.lang.String buildMessage(java.util.Locale locale, java.lang.String pattern, java.lang.Object... arguments) {
		return pattern == null ? "" : new java.text.MessageFormat(org.apache.commons.math.MathException.translate(pattern, locale), locale).format(arguments);
	}

	/**
	 * Constructs a new <code>MathException</code> with no
	 * detail message.
	 */
	public MathException() {
		super();
		this.pattern = null;
		this.arguments = new java.lang.Object[0];
	}

	/**
	 * Constructs a new <code>MathException</code> with specified
	 * formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 */
	public MathException(java.lang.String pattern, java.lang.Object... arguments) {
		super(org.apache.commons.math.MathException.buildMessage(java.util.Locale.US, pattern, arguments));
		this.pattern = pattern;
		this.arguments = (arguments == null) ? new java.lang.Object[0] : arguments.clone();
	}

	/**
	 * Constructs a new <code>MathException</code> with specified
	 * nested <code>Throwable</code> root cause.
	 *
	 * @param rootCause
	 * 		the exception or error that caused this exception
	 * 		to be thrown.
	 */
	public MathException(java.lang.Throwable rootCause) {
		super(rootCause);
		this.pattern = getMessage();
		this.arguments = new java.lang.Object[0];
	}

	/**
	 * Constructs a new <code>MathException</code> with specified
	 * formatted detail message and nested <code>Throwable</code> root cause.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param rootCause
	 * 		the exception or error that caused this exception
	 * 		to be thrown.
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @since 1.2
	 */
	public MathException(java.lang.Throwable rootCause, java.lang.String pattern, java.lang.Object... arguments) {
		super(org.apache.commons.math.MathException.buildMessage(java.util.Locale.US, pattern, arguments), rootCause);
		this.pattern = pattern;
		this.arguments = (arguments == null) ? new java.lang.Object[0] : arguments.clone();
	}

	/**
	 * Gets the pattern used to build the message of this throwable.
	 *
	 * @return the pattern used to build the message of this throwable
	 * @since 1.2
	 */
	public java.lang.String getPattern() {
		return pattern;
	}

	/**
	 * Gets the arguments used to build the message of this throwable.
	 *
	 * @return the arguments used to build the message of this throwable
	 * @since 1.2
	 */
	public java.lang.Object[] getArguments() {
		return arguments.clone();
	}

	/**
	 * Gets the message in a specified locale.
	 *
	 * @param locale
	 * 		Locale in which the message should be translated
	 * @return localized message
	 * @since 1.2
	 */
	public java.lang.String getMessage(java.util.Locale locale) {
		return org.apache.commons.math.MathException.buildMessage(locale, pattern, arguments);
	}

	/**
	 * {@inheritDoc }
	 */
	@java.lang.Override
	public java.lang.String getLocalizedMessage() {
		return getMessage(java.util.Locale.getDefault());
	}

	/**
	 * Prints the stack trace of this exception to the standard error stream.
	 */
	@java.lang.Override
	public void printStackTrace() {
		printStackTrace(java.lang.System.err);
	}

	/**
	 * Prints the stack trace of this exception to the specified stream.
	 *
	 * @param out
	 * 		the <code>PrintStream</code> to use for output
	 */
	@java.lang.Override
	public void printStackTrace(java.io.PrintStream out) {
		synchronized(out) {
			java.io.PrintWriter pw = new java.io.PrintWriter(out, false);
			printStackTrace(pw);
			// Flush the PrintWriter before it's GC'ed.
			pw.flush();
		}
	}
}