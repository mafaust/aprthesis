package org.apache.commons.math;





























/**
 * Base class for commons-math unchecked exceptions.
 *
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class MathRuntimeException extends java.lang.RuntimeException {

	/**
	 * Serializable version identifier.
	 */ 	private static final long serialVersionUID = -5128983364075381060L;
	/**
	 * Pattern used to build the message.
	 */
	private final java.lang.String pattern;

	/**
	 * Arguments used to build the message.
	 */
	private final java.lang.Object[] arguments;

	/**
	 * Constructs a new <code>MathRuntimeException</code> with specified
	 * formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 */ 	public MathRuntimeException(final java.lang.String pattern, final java.lang.Object... arguments) { 		this.pattern = pattern; 		this.arguments = (arguments == null) ? new java.lang.Object[0] : arguments.clone();
	}

	/**
	 * Constructs a new <code>MathRuntimeException</code> with specified
	 * nested <code>Throwable</code> root cause.
	 *
	 * @param rootCause
	 * 		the exception or error that caused this exception
	 * 		to be thrown.
	 */ 	public MathRuntimeException(final java.lang.Throwable rootCause) {
		super(rootCause);
		this.pattern = getMessage();
		this.arguments = new java.lang.Object[0];
	}

	/**
	 * Constructs a new <code>MathRuntimeException</code> with specified
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
	 */ 	public MathRuntimeException(final java.lang.Throwable rootCause, final java.lang.String pattern, final java.lang.Object... arguments) { 		super(rootCause); 		this.pattern = pattern;
		this.arguments = (arguments == null) ? new java.lang.Object[0] : arguments.clone();
	}

	/**
	 * Translate a string to a given locale.
	 *
	 * @param s
	 * 		string to translate
	 * @param locale
	 * 		locale into which to translate the string
	 * @return translated string or original string
	for unsupported locales or unknown strings
	 */ 	private static java.lang.String translate(final java.lang.String s, final java.util.Locale locale) { 		try { 			java.util.ResourceBundle bundle = 
			java.util.ResourceBundle.getBundle("org.apache.commons.math.MessagesResources", locale);
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
	 */ 	private static java.lang.String buildMessage(final java.util.Locale locale, final java.lang.String pattern, final java.lang.Object... arguments) { 		return pattern == null ? "" : new java.text.MessageFormat(org.apache.commons.math.MathRuntimeException.translate(pattern, locale), locale).format(arguments);}

	/**
	 * Gets the pattern used to build the message of this throwable.
	 *
	 * @return the pattern used to build the message of this throwable
	 */ 	public java.lang.String getPattern() {
		return pattern;
	}

	/**
	 * Gets the arguments used to build the message of this throwable.
	 *
	 * @return the arguments used to build the message of this throwable
	 */ 	public java.lang.Object[] getArguments() {
		return arguments.clone();
	}

	/**
	 * Gets the message in a specified locale.
	 *
	 * @param locale
	 * 		Locale in which the message should be translated
	 * @return localized message
	 */ 	public java.lang.String getMessage(final java.util.Locale locale) {
		return org.apache.commons.math.MathRuntimeException.buildMessage(locale, pattern, arguments);
	}

	/**
	 * {@inheritDoc }
	 */ 	@java.lang.Override 	public java.lang.String getMessage() {
		return getMessage(java.util.Locale.US);
	}

	/**
	 * {@inheritDoc }
	 */ 	@java.lang.Override 	public java.lang.String getLocalizedMessage() {
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
	 */ 	@java.lang.Override
	public void printStackTrace(final java.io.PrintStream out) {
		synchronized(out) {
			java.io.PrintWriter pw = new java.io.PrintWriter(out, false);
			printStackTrace(pw);
			// Flush the PrintWriter before it's GC'ed.
			pw.flush();
		}
	}

	/**
	 * Constructs a new <code>ArithmeticException</code> with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @return built exception
	 */ 	public static java.lang.ArithmeticException createArithmeticException(final java.lang.String pattern, final java.lang.Object... arguments) { 		return new java.lang.ArithmeticException() {

			/**
			 * Serializable version identifier.
			 */ 			private static final long serialVersionUID = 7705628723242533939L;
			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
			}

			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getLocalizedMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
			}

		};
	}

	/**
	 * Constructs a new <code>ArrayIndexOutOfBoundsException</code> with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @return built exception
	 */ 	public static java.lang.ArrayIndexOutOfBoundsException createArrayIndexOutOfBoundsException(final java.lang.String pattern, final java.lang.Object... arguments) { 		return new java.lang.ArrayIndexOutOfBoundsException() {

			/**
			 * Serializable version identifier.
			 */ 			private static final long serialVersionUID = -3394748305449283486L;
			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
			}

			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getLocalizedMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
			}

		};
	}

	/**
	 * Constructs a new <code>EOFException</code> with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @return built exception
	 */ 	public static java.io.EOFException createEOFException(final java.lang.String pattern, final java.lang.Object... arguments) { 		return new java.io.EOFException() {

			/**
			 * Serializable version identifier.
			 */ 			private static final long serialVersionUID = 279461544586092584L;
			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
			}

			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getLocalizedMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
			}

		};
	}

	/**
	 * Constructs a new <code>IOException</code> with specified nested
	 * <code>Throwable</code> root cause.
	 * <p>This factory method allows chaining of other exceptions within an
	 * <code>IOException</code> even for Java 5. The constructor for
	 * <code>IOException</code> with a cause parameter was introduced only
	 * with Java 6.</p>
	 *
	 * @param rootCause
	 * 		the exception or error that caused this exception
	 * 		to be thrown.
	 * @return built exception
	 */ 	public static java.io.IOException createIOException(final java.lang.Throwable rootCause) { 		java.io.IOException ioe = new java.io.IOException(rootCause.getLocalizedMessage());
		ioe.initCause(rootCause);
		return ioe;
	}

	/**
	 * Constructs a new <code>IllegalArgumentException</code> with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @return built exception
	 */ 	public static java.lang.IllegalArgumentException createIllegalArgumentException(final java.lang.String pattern, final java.lang.Object... arguments) { 		return new java.lang.IllegalArgumentException() {

			/**
			 * Serializable version identifier.
			 */ 			private static final long serialVersionUID = -6555453980658317913L;
			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
			}

			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getLocalizedMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
			}

		};
	}

	/**
	 * Constructs a new <code>IllegalArgumentException</code> with specified nested
	 * <code>Throwable</code> root cause.
	 *
	 * @param rootCause
	 * 		the exception or error that caused this exception
	 * 		to be thrown.
	 * @return built exception
	 */ 	public static java.lang.IllegalArgumentException createIllegalArgumentException(final java.lang.Throwable rootCause) { 		java.lang.IllegalArgumentException iae = new java.lang.IllegalArgumentException(rootCause.getLocalizedMessage());
		iae.initCause(rootCause);
		return iae;
	}

	/**
	 * Constructs a new <code>IllegalStateException</code> with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @return built exception
	 */ 	public static java.lang.IllegalStateException createIllegalStateException(final java.lang.String pattern, final java.lang.Object... arguments) { 		return new java.lang.IllegalStateException() {

			/**
			 * Serializable version identifier.
			 */ 			private static final long serialVersionUID = -95247648156277208L;
			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
			}

			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getLocalizedMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
			}

		};
	}

	/**
	 * Constructs a new <code>ConcurrentModificationException</code> with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @return built exception
	 */ 	public static java.util.ConcurrentModificationException createConcurrentModificationException(final java.lang.String pattern, final java.lang.Object... arguments) { 		return new java.util.ConcurrentModificationException() {

			/**
			 * Serializable version identifier.
			 */ 			private static final long serialVersionUID = 6134247282754009421L;
			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
			}

			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getLocalizedMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
			}

		};
	}

	/**
	 * Constructs a new <code>NoSuchElementException</code> with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @return built exception
	 */ 	public static java.util.NoSuchElementException createNoSuchElementException(final java.lang.String pattern, final java.lang.Object... arguments) { 		return new java.util.NoSuchElementException() {

			/**
			 * Serializable version identifier.
			 */ 			private static final long serialVersionUID = 7304273322489425799L;
			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
			}

			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getLocalizedMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
			}

		};
	}

	/**
	 * Constructs a new <code>NullPointerException</code> with specified formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @return built exception
	 */ 	public static java.lang.NullPointerException createNullPointerException(final java.lang.String pattern, final java.lang.Object... arguments) { 		return new java.lang.NullPointerException() {

			/**
			 * Serializable version identifier.
			 */ 			private static final long serialVersionUID = -3075660477939965216L;
			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
			}

			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getLocalizedMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
			}

		};
	}

	/**
	 * Constructs a new <code>ParseException</code> with specified
	 * formatted detail message.
	 * Message formatting is delegated to {@link java.text.MessageFormat}.
	 *
	 * @param offset
	 * 		offset at which error occurred
	 * @param pattern
	 * 		format specifier
	 * @param arguments
	 * 		format arguments
	 * @return built exception
	 */ 	public static java.text.ParseException createParseException(final int offset, final java.lang.String pattern, final java.lang.Object... arguments) { 		return new java.text.ParseException(null, offset) {

			/**
			 * Serializable version identifier.
			 */ 			private static final long serialVersionUID = -1103502177342465975L;
			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, arguments);
			}

			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getLocalizedMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, arguments);
			}

		};
	}

	/**
	 * Create an {@link java.lang.RuntimeException} for an internal error.
	 *
	 * @param cause
	 * 		underlying cause
	 * @return an {@link java.lang.RuntimeException} for an internal error
	 */ 	public static java.lang.RuntimeException createInternalError(final java.lang.Throwable cause) { 		final java.lang.String pattern = "internal error, please fill a bug report at {0}";
		final java.lang.String argument = "https://issues.apache.org/jira/browse/MATH";

		return new java.lang.RuntimeException() {

			/**
			 * Serializable version identifier.
			 */ 			private static final long serialVersionUID = -201865440834027016L;
			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.US, pattern, argument);
			}

			/**
			 * {@inheritDoc }
			 */ 			@java.lang.Override 			public java.lang.String getLocalizedMessage() {
				return org.apache.commons.math.MathRuntimeException.buildMessage(java.util.Locale.getDefault(), pattern, argument);
			}

		};

	}}