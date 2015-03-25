package org.nextime.ion.framework.logger;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.nextime.ion.framework.config.Config;

/**
 * Tenue d'un journal de log
 *
 * @author gbort
 * @version 0.9
 */
public class Logger {

    private static Logger instance;
    private boolean _disableLog;

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    private Logger() {
        BasicConfigurator.configure();
        _disableLog = Config.getInstance().getDisableLog();
    }

    public void log(String message, Object source) {
        if (!_disableLog) {
            doLog(message, source.getClass(), Priority.INFO);
        }
    }

    public void error(String message, Object source, Throwable e) {
        if (!_disableLog) {
            doLog(message, source.getClass(), Priority.ERROR);
            e.printStackTrace();
        }
    }

    public void fatal(String message, Object source, Throwable e) {
        if (!_disableLog) {
            doLog(message, source.getClass(), Priority.FATAL);
        }
        e.printStackTrace();
    }

    public void log(String message, Class source) {
        if (!_disableLog) {
            doLog(message, source, Priority.INFO);
        }
    }

    public void error(String message, Class source, Throwable e) {
        if (!_disableLog) {
            doLog(message, source, Priority.ERROR);
            e.printStackTrace();
        }
    }

    public void fatal(String message, Class source, Throwable e) {
        if (!_disableLog) {
            doLog(message, source, Priority.FATAL);
            e.printStackTrace();
        }
    }

    private void doLog(String message, Class source, Priority p) {
        Category cat = Category.getInstance(source);
        cat.log(p, message);
    }
}
