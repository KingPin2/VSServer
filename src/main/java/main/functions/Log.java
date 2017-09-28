package main.functions;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Dominik on 26.09.2017.
 */
public class Log {

    private FileWriter logWriter;
    private boolean localEnabled = false;
    private boolean remoteEnabled = true;


    /**
     * Instantiate log
     */
    public Log() {
        try {
            File directory = new File("log");
            if (! directory.exists()){
                directory.mkdir();
            }
            logWriter = new FileWriter("log/log_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".txt", true);
            System.out.println("Log ok!");
        } catch (Exception e) {
            localEnabled = false;
            System.out.println("Could not open log!");
            System.out.println("Local log disabled!");
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
            }
            localEnabled = false;
            System.out.println("Logging error!");
            System.out.println("Local log disabled!");
        }
    }

    /**
     * Add info message to log
     * @param message
     */
    public void addToLog(String message) {
        String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [INFO] " + message + "\n";
        executePost(mes);
        if (localEnabled) {
            LogThread lt = new LogThread(mes, LogThread.LogType.LOCAL, logWriter, new LogCallback() {
                @Override
                public void notifyDisabled() {
                    closeLog();
                }
            });
        }
    }

    /**
     * Add warning message to log
     * @param message
     */
    public void addWarningToLog(String message) {
        String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [WARNING] " + message + "\n";
        executePost(mes);
        if (localEnabled) {
            LogThread lt = new LogThread(mes, LogThread.LogType.LOCAL, logWriter, new LogCallback() {
                @Override
                public void notifyDisabled() {
                    closeLog();
                }
            });
        }
    }

    /**
     * Add error message to log
     * @param message
     */
    public void addErrorToLog(String message) {
        String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [ERROR] " + message + "\n";
        executePost(mes);
        if (localEnabled) {
            LogThread lt = new LogThread(mes, LogThread.LogType.LOCAL, logWriter, new LogCallback() {
                @Override
                public void notifyDisabled() {
                    closeLog();
                }
            });
        }
    }

    /**
     * Execute post to logserver
     * @param urlParameters
     */
    private void executePost(String urlParameters) {
        if (remoteEnabled) {
            LogThread lt = new LogThread(urlParameters, LogThread.LogType.REMOTE, null, null);
            lt.run();
        }

    }
}
