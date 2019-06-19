package caffeinatedpinkie.tmel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple class to conveniently log messages for this mod.
 *
 * @author CaffeinatedPinkie
 */
public class LoggerTMEL {
    public static Logger logger = LogManager.getLogger(TMEL.MODID);

    /**
     * Runs a specified {@link Runnable} and then logs the time it took to complete,
     * using the given {@link String}.
     *
     * @param runnable    an arbitrary series of commands
     * @param processName the name of the process being run
     */
    public static void log(Runnable runnable, String processName) {
	long startTime = System.nanoTime();
	runnable.run();
	log(processName + " in " + (System.nanoTime() - startTime) / 1000000.0 + " ms!");
    }

    /**
     * Logs a message with {@link #logger}, the mod {@link Logger}, if
     * {@link ConfigTMEL#verbose} is true.
     *
     * @param message the message to be logged
     */
    public static void log(String message) {
	if (ConfigTMEL.verbose)
	    logger.info(message);
    }

    /**
     * Logs a warning message with {@link #logger}, the mod {@link Logger}, if
     * {@link ConfigTMEL#verbose} is true.
     *
     * @param message the message to be logged
     * @param e       the {@link Exception} that led to this warning
     */
    public static void warn(String message, Exception e) {
	if (ConfigTMEL.verbose)
	    logger.warn(message + "Caused by: " + e.getLocalizedMessage());
    }
}
