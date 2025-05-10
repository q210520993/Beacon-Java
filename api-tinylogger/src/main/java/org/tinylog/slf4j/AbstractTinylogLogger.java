package org.tinylog.slf4j;

import com.redstone.beacon.core.logger.LoggerColor;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;
import org.tinylog.Level;
import org.tinylog.format.LegacyMessageFormatter;
import org.tinylog.format.MessageFormatter;
import org.tinylog.provider.LoggingProvider;
import org.tinylog.provider.ProviderRegistry;

/**
 * tinylog 的 {@link LoggingProvider} 抽象定位感知日志器。
 */
public abstract class AbstractTinylogLogger implements LocationAwareLogger {

	protected static final int STACKTRACE_DEPTH = 2;

	protected static final MessageFormatter formatter = new LegacyMessageFormatter();
	protected static final LoggingProvider provider = ProviderRegistry.getLoggingProvider();

	protected static final boolean MINIMUM_GLOBAL_LEVEL_COVERS_TRACE = isCoveredByGlobalMinimumLevel(Level.TRACE);
	protected static final boolean MINIMUM_GLOBAL_LEVEL_COVERS_DEBUG = isCoveredByGlobalMinimumLevel(Level.DEBUG);
	protected static final boolean MINIMUM_GLOBAL_LEVEL_COVERS_INFO  = isCoveredByGlobalMinimumLevel(Level.INFO);
	protected static final boolean MINIMUM_GLOBAL_LEVEL_COVERS_WARN  = isCoveredByGlobalMinimumLevel(Level.WARN);
	protected static final boolean MINIMUM_GLOBAL_LEVEL_COVERS_ERROR = isCoveredByGlobalMinimumLevel(Level.ERROR);

	protected static final boolean MINIMUM_DEFAULT_LEVEL_COVERS_TRACE = isCoveredByDefaultMinimumLevel(Level.TRACE);
	protected static final boolean MINIMUM_DEFAULT_LEVEL_COVERS_DEBUG = isCoveredByDefaultMinimumLevel(Level.DEBUG);
	protected static final boolean MINIMUM_DEFAULT_LEVEL_COVERS_INFO  = isCoveredByDefaultMinimumLevel(Level.INFO);
	protected static final boolean MINIMUM_DEFAULT_LEVEL_COVERS_WARN  = isCoveredByDefaultMinimumLevel(Level.WARN);
	protected static final boolean MINIMUM_DEFAULT_LEVEL_COVERS_ERROR = isCoveredByDefaultMinimumLevel(Level.ERROR);

	private final String name;

	/**
	 * @param name 日志器的名称
	 */
	AbstractTinylogLogger(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	private String colorMessage(Level level, String message) {
        return switch (level) {
            case TRACE -> LoggerColor.CYAN + message + LoggerColor.RESET;
            case DEBUG -> LoggerColor.GREEN + message + LoggerColor.RESET;
            case INFO -> LoggerColor.RESET + message + LoggerColor.RESET;
            case WARN -> LoggerColor.YELLOW + message + LoggerColor.RESET;
            case ERROR -> LoggerColor.RED + message + LoggerColor.RESET;
            default -> message;
        };
	}

	// 实现所有日志方法
	@Override
	public boolean isTraceEnabled() {
		return MINIMUM_DEFAULT_LEVEL_COVERS_TRACE && provider.isEnabled(STACKTRACE_DEPTH, null, Level.TRACE);
	}

	@Override
	public void trace(final String message) {
		if (isTraceEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.TRACE, null, null, colorMessage(Level.TRACE, message), (Object[]) null);
		}
	}

	@Override
	public void trace(final String format, final Object arg) {
		if (isTraceEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.TRACE, extractThrowable(arg), formatter, format, arg);
		}
	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {
		if (isTraceEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.TRACE, extractThrowable(arg2), formatter, format, arg1, arg2);
		}
	}

	@Override
	public void trace(final String format, final Object... arguments) {
		if (isTraceEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.TRACE, extractThrowable(arguments), formatter, format, arguments);
		}
	}

	@Override
	public void trace(final String message, final Throwable exception) {
		if (isTraceEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.TRACE, exception, null, colorMessage(Level.TRACE, message), (Object[]) null);
		}
	}

	@Override
	public boolean isTraceEnabled(final Marker marker) {
		String tag = marker == null ? null : marker.getName();
		return MINIMUM_GLOBAL_LEVEL_COVERS_TRACE && provider.isEnabled(STACKTRACE_DEPTH, tag, Level.TRACE);
	}

	@Override
	public void trace(final Marker marker, final String message) {
		if (isTraceEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.TRACE, null, null, colorMessage(Level.TRACE, message), (Object[]) null);
		}
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg) {
		if (isTraceEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.TRACE, extractThrowable(arg), formatter, format, arg);
		}
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
		if (isTraceEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.TRACE, extractThrowable(arg2), formatter, format, arg1, arg2);
		}
	}

	@Override
	public void trace(final Marker marker, final String format, final Object... arguments) {
		if (isTraceEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.TRACE, extractThrowable(arguments), formatter, format, arguments);
		}
	}

	@Override
	public void trace(final Marker marker, final String message, final Throwable exception) {
		if (isTraceEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.TRACE, exception, null, colorMessage(Level.TRACE, message), (Object[]) null);
		}
	}

	@Override
	public boolean isDebugEnabled() {
		return MINIMUM_DEFAULT_LEVEL_COVERS_DEBUG && provider.isEnabled(STACKTRACE_DEPTH, null, Level.DEBUG);
	}

	@Override
	public void debug(final String message) {
		if (isDebugEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.DEBUG, null, null, colorMessage(Level.DEBUG, message), (Object[]) null);
		}
	}

	@Override
	public void debug(final String format, final Object arg) {
		if (isDebugEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.DEBUG, extractThrowable(arg), formatter, format, arg);
		}
	}

	@Override
	public void debug(final String format, final Object arg1, final Object arg2) {
		if (isDebugEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.DEBUG, extractThrowable(arg2), formatter, format, arg1, arg2);
		}
	}

	@Override
	public void debug(final String format, final Object... arguments) {
		if (isDebugEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.DEBUG, extractThrowable(arguments), formatter, format, arguments);
		}
	}

	@Override
	public void debug(final String message, final Throwable exception) {
		if (isDebugEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.DEBUG, exception, null, colorMessage(Level.DEBUG, message), (Object[]) null);
		}
	}

	@Override
	public boolean isDebugEnabled(final Marker marker) {
		String tag = marker == null ? null : marker.getName();
		return MINIMUM_GLOBAL_LEVEL_COVERS_DEBUG && provider.isEnabled(STACKTRACE_DEPTH, tag, Level.DEBUG);
	}

	@Override
	public void debug(final Marker marker, final String message) {
		if (isDebugEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.DEBUG, null, null, colorMessage(Level.DEBUG, message), (Object[]) null);
		}
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg) {
		if (isDebugEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.DEBUG, extractThrowable(arg), formatter, format, arg);
		}
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
		if (isDebugEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.DEBUG, extractThrowable(arg2), formatter, format, arg1, arg2);
		}
	}

	@Override
	public void debug(final Marker marker, final String format, final Object... arguments) {
		if (isDebugEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.DEBUG, extractThrowable(arguments), formatter, format, arguments);
		}
	}

	@Override
	public void debug(final Marker marker, final String message, final Throwable exception) {
		if (isDebugEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.DEBUG, exception, null, colorMessage(Level.DEBUG, message), (Object[]) null);
		}
	}

	@Override
	public boolean isInfoEnabled() {
		return MINIMUM_DEFAULT_LEVEL_COVERS_INFO && provider.isEnabled(STACKTRACE_DEPTH, null, Level.INFO);
	}

	@Override
	public void info(final String message) {
		if (isInfoEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.INFO, null, null, colorMessage(Level.INFO, message), (Object[]) null);
		}
	}

	@Override
	public void info(final String format, final Object arg) {
		if (isInfoEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.INFO, extractThrowable(arg), formatter, format, arg);
		}
	}

	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
		if (isInfoEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.INFO, extractThrowable(arg2), formatter, format, arg1, arg2);
		}
	}

	@Override
	public void info(final String format, final Object... arguments) {
		if (isInfoEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.INFO, extractThrowable(arguments), formatter, format, arguments);
		}
	}

	@Override
	public void info(final String message, final Throwable exception) {
		if (isInfoEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.INFO, exception, null, colorMessage(Level.INFO, message), (Object[]) null);
		}
	}

	@Override
	public boolean isInfoEnabled(final Marker marker) {
		String tag = marker == null ? null : marker.getName();
		return MINIMUM_GLOBAL_LEVEL_COVERS_INFO && provider.isEnabled(STACKTRACE_DEPTH, tag, Level.INFO);
	}

	@Override
	public void info(final Marker marker, final String message) {
		if (isInfoEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.INFO, null, null, colorMessage(Level.INFO, message), (Object[]) null);
		}
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg) {
		if (isInfoEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.INFO, extractThrowable(arg), formatter, format, arg);
		}
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
		if (isInfoEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.INFO, extractThrowable(arg2), formatter, format, arg1, arg2);
		}
	}

	@Override
	public void info(final Marker marker, final String format, final Object... arguments) {
		if (isInfoEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.INFO, extractThrowable(arguments), formatter, format, arguments);
		}
	}

	@Override
	public void info(final Marker marker, final String message, final Throwable exception) {
		if (isInfoEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.INFO, exception, null, colorMessage(Level.INFO, message), (Object[]) null);
		}
	}

	@Override
	public boolean isWarnEnabled() {
		return MINIMUM_DEFAULT_LEVEL_COVERS_WARN && provider.isEnabled(STACKTRACE_DEPTH, null, Level.WARN);
	}

	@Override
	public void warn(final String message) {
		if (isWarnEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.WARN, null, null, colorMessage(Level.WARN, message), (Object[]) null);
		}
	}

	@Override
	public void warn(final String format, final Object arg) {
		if (isWarnEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.WARN, extractThrowable(arg), formatter, format, arg);
		}
	}

	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {
		if (isWarnEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.WARN, extractThrowable(arg2), formatter, format, arg1, arg2);
		}
	}

	@Override
	public void warn(final String format, final Object... arguments) {
		if (isWarnEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.WARN, extractThrowable(arguments), formatter, format, arguments);
		}
	}

	@Override
	public void warn(final String message, final Throwable exception) {
		if (isWarnEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.WARN, exception, null, colorMessage(Level.WARN, message), (Object[]) null);
		}
	}

	@Override
	public boolean isWarnEnabled(final Marker marker) {
		String tag = marker == null ? null : marker.getName();
		return MINIMUM_GLOBAL_LEVEL_COVERS_WARN && provider.isEnabled(STACKTRACE_DEPTH, tag, Level.WARN);
	}

	@Override
	public void warn(final Marker marker, final String message) {
		if (isWarnEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.WARN, null, null, colorMessage(Level.WARN, message), (Object[]) null);
		}
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg) {
		if (isWarnEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.WARN, extractThrowable(arg), formatter, format, arg);
		}
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
		if (isWarnEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.WARN, extractThrowable(arg2), formatter, format, arg1, arg2);
		}
	}

	@Override
	public void warn(final Marker marker, final String format, final Object... arguments) {
		if (isWarnEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.WARN, extractThrowable(arguments), formatter, format, arguments);
		}
	}

	@Override
	public void warn(final Marker marker, final String message, final Throwable exception) {
		if (isWarnEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.WARN, exception, null, colorMessage(Level.WARN, message), (Object[]) null);
		}
	}

	@Override
	public boolean isErrorEnabled() {
		return MINIMUM_DEFAULT_LEVEL_COVERS_ERROR && provider.isEnabled(STACKTRACE_DEPTH, null, Level.ERROR);
	}

	@Override
	public void error(final String message) {
		if (isErrorEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.ERROR, null, null, colorMessage(Level.ERROR, message), (Object[]) null);
		}
	}

	@Override
	public void error(final String format, final Object arg) {
		if (isErrorEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.ERROR, extractThrowable(arg), formatter, format, arg);
		}
	}

	@Override
	public void error(final String format, final Object arg1, final Object arg2) {
		if (isErrorEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.ERROR, extractThrowable(arg2), formatter, format, arg1, arg2);
		}
	}

	@Override
	public void error(final String format, final Object... arguments) {
		if (isErrorEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.ERROR, extractThrowable(arguments), formatter, format, arguments);
		}
	}

	@Override
	public void error(final String message, final Throwable exception) {
		if (isErrorEnabled()) {
			provider.log(STACKTRACE_DEPTH, null, Level.ERROR, exception, null, colorMessage(Level.ERROR, message), (Object[]) null);
		}
	}

	@Override
	public boolean isErrorEnabled(final Marker marker) {
		String tag = marker == null ? null : marker.getName();
		return MINIMUM_GLOBAL_LEVEL_COVERS_ERROR && provider.isEnabled(STACKTRACE_DEPTH, tag, Level.ERROR);
	}

	@Override
	public void error(final Marker marker, final String message) {
		if (isErrorEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.ERROR, null, null, colorMessage(Level.ERROR, message), (Object[]) null);
		}
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg) {
		if (isErrorEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.ERROR, extractThrowable(arg), formatter, format, arg);
		}
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
		if (isErrorEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.ERROR, extractThrowable(arg2), formatter, format, arg1, arg2);
		}
	}

	@Override
	public void error(final Marker marker, final String format, final Object... arguments) {
		if (isErrorEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.ERROR, extractThrowable(arguments), formatter, format, arguments);
		}
	}

	@Override
	public void error(final Marker marker, final String message, final Throwable exception) {
		if (isErrorEnabled(marker)) {
			String tag = marker == null ? null : marker.getName();
			provider.log(STACKTRACE_DEPTH, tag, Level.ERROR, exception, null, colorMessage(Level.ERROR, message), (Object[]) null);
		}
	}

	@Override
	public void log(final Marker marker, final String fqcn, final int level, final String message, final Object[] arguments,
					final Throwable exception) {
		Level severityLevel = translateLevel(level);
		String tag = marker == null ? null : marker.getName();
		if (provider.getMinimumLevel(tag).ordinal() <= severityLevel.ordinal()) {
			provider.log(fqcn, tag, severityLevel, exception, formatter, colorMessage(severityLevel, message), arguments);
		}
	}

	protected static Level translateLevel(final int level) {
		if (level <= TRACE_INT) {
			return Level.TRACE;
		} else if (level <= DEBUG_INT) {
			return Level.DEBUG;
		} else if (level <= INFO_INT) {
			return Level.INFO;
		} else if (level <= WARN_INT) {
			return Level.WARN;
		} else {
			return Level.ERROR;
		}
	}

	private static boolean isCoveredByGlobalMinimumLevel(final Level level) {
		return provider.getMinimumLevel().ordinal() <= level.ordinal();
	}

	private static boolean isCoveredByDefaultMinimumLevel(final Level level) {
		return provider.getMinimumLevel(null).ordinal() <= level.ordinal();
	}

	private static Throwable extractThrowable(final Object[] arguments) {
		return arguments.length == 0 ? null : extractThrowable(arguments[arguments.length - 1]);
	}

	private static Throwable extractThrowable(final Object argument) {
		return argument instanceof Throwable ? (Throwable) argument : null;
	}
}