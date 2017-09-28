package main.functions;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Dominik on 26.09.2017.
 */
public class Log {

    private FileWriter logWriter;

    private boolean consoleEnabled = true;
    private boolean localEnabled = false;
    private boolean remoteEnabled = true;


    /**
     * Instantiate log
     */
    public Log() {
        try {
            File directory = new File("log");
            if (!directory.exists()) {
                directory.mkdir();
            }
            logWriter = new FileWriter("log/log_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".txt", true);
            System.out.println("Log ok!");
        } catch (Exception e) {
            localEnabled = false;
            System.err.println("Could not open log!");
            System.err.println("Local log disabled!");
        }
        ;
    }

    /**
     * Close log
     */
    private synchronized void closeLog() {
        if (localEnabled) {
            try {
                logWriter.close();
            } catch (Exception ex) {
                System.err.println(ex.toString());
            }
            localEnabled = false;
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
        if (consoleEnabled) {
            System.out.println(mes);
        }

        mes = mes + "\n";

        if (remoteEnabled) {
            executePost(mes);
        }
        if (localEnabled) {
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
        if (consoleEnabled) {
            System.out.println(mes);
        }

        mes = mes + "\n";

        if (remoteEnabled) {
            executePost(mes);
        }
        if (localEnabled) {
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
        if (consoleEnabled) {
            System.out.println(mes);
        }

        mes = mes + "\n";
        
        if (remoteEnabled) {
            executePost(mes);
        }
        if (localEnabled) {
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
