package main.functions;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Log functions
 *
 * @author Dominik Bergum, 3603490
 */
public class Log {

    private FileWriter logWriter;

    //Enable log to console
    private boolean consoleEnabled = true;

    //Enable log to local file
    private boolean localEnabled = false;

    //Enable log to webserver
    private boolean remoteEnabled = false;

    public Log() {
        this(false, false);
    }

    /**
     * Instantiate log
     */
    public Log(boolean localEnabled, boolean remoteEnabled) {
        this.localEnabled = localEnabled;
        this.remoteEnabled = remoteEnabled;

        if (this.localEnabled) {
            try {
                File directory = new File("log");
                if (!directory.exists()) {
                    directory.mkdir();
                }
                logWriter = new FileWriter("log/log_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".txt", true);
                System.out.println("Log ok!");
            } catch (Exception e) {
                this.localEnabled = false;
                System.err.println("Could not open log!");
                System.err.println("Local log disabled!");
            }
        }
    }

    /**
     * Close log
     */
    private synchronized void closeLog() {
        if (this.localEnabled) {
            try {
                logWriter.close();
            } catch (Exception ex) {
                System.err.println(ex.toString());
            }
            this.localEnabled = false;
            System.err.println("Logging error!");
            System.err.println("Local log disabled!");
        }
    }

    /**
     * Add info message to log
     *
     * @param message Message
     */
    public void addToLog(String message) {
        String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [INFO] " + message;
        if (this.consoleEnabled) {
            System.out.println(mes);
        }

        mes = mes + "\n";

        if (this.remoteEnabled) {
            executePost(mes);
        }
        if (this.localEnabled) {
            LogThread lt = new LogThread(mes, LogThread.LogType.LOCAL, logWriter, new LogCallback() {
                @Override
                public void notifyDisabled() {
                    closeLog();
                }
            });
            lt.run();
        }
    }

    /**
     * Add warning message to log
     *
     * @param message Message
     */
    public void addWarningToLog(String message) {
        String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [WARNING] " + message;
        if (this.consoleEnabled) {
            System.out.println(mes);
        }

        mes = mes + "\n";

        if (this.remoteEnabled) {
            executePost(mes);
        }
        if (this.localEnabled) {
            LogThread lt = new LogThread(mes, LogThread.LogType.LOCAL, logWriter, new LogCallback() {
                @Override
                public void notifyDisabled() {
                    closeLog();
                }
            });
            lt.run();
        }
    }

    /**
     * Add error message to log
     *
     * @param message Message
     */
    public void addErrorToLog(String message) {
        String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [ERROR] " + message;
        if (this.consoleEnabled) {
            System.out.println(mes);
        }

        mes = mes + "\n";

        if (this.remoteEnabled) {
            executePost(mes);
        }
        if (this.localEnabled) {
            LogThread lt = new LogThread(mes, LogThread.LogType.LOCAL, logWriter, new LogCallback() {
                @Override
                public void notifyDisabled() {
                    closeLog();
                }
            });
            lt.run();
        }
    }

    /**
     * Execute post to logserver
     *
     * @param message Message
     */
    private void executePost(String message) {
        LogThread lt = new LogThread(message, LogThread.LogType.REMOTE, null, null);
        lt.run();
    }
}
