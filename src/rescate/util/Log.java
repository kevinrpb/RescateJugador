package rescate.util;

import java.util.logging.*;

public class Log {

  private static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

  private static Log l = new Log();

  private Log() {
    ConsoleHandler h = new ConsoleHandler();
    h.setFormatter(new SimpleFormatter());
    LOGGER.addHandler(h);
  }

  private void logInfo(String log) {
    LOGGER.info(log);
  }

  private void logDebug(String log) {
    LOGGER.fine(log);
  }

  private void logWarning(String log) {
    LOGGER.warning(log);
  }

  private void logError(String log) {
    LOGGER.severe(log);
  }

  public static void info(String log) {
    l.logInfo(log);
  }

  public static void debug(String log) {
    l.logDebug(log);
  }

  public static void warning(String log) {
    l.logWarning(log);
  }

  public static void error(String log) {
    l.logError(log);
  }

}
