package main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Dominik on 26.09.2017.
 */
public class Log {

    private FileWriter logWriter;
    private boolean enabled = false;

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
            //enabled = true;
            System.out.println("Log ok!");
        } catch (Exception e) {
            System.out.println("Could not open log!");
            System.out.println("Log disabled!");
        }
        ;
    }

    /**
     * Close log
     */
    public void closeLog() {
        if (enabled) {
            try {
                logWriter.close();
            } catch (Exception e) {
                enabled = false;
                System.out.println("Logging error!");
                System.out.println("Log disabled!");
            }
        }
    }

    /**
     * Add info message to log
     * @param message
     */
    public void addToLog(String message) {
        String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [INFO] " + message + "\n";
        executePost(mes);
        if (enabled) {
            try {
                logWriter.write(mes);
            } catch (Exception e) {
                enabled = false;
                try {
                    logWriter.close();
                } catch (Exception ex) {
                }
                System.out.println("Logging error!");
                System.out.println("Log disabled!");
            }
        }
    }

    /**
     * Add warning message to log
     * @param message
     */
    public void addWarningToLog(String message) {
        String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [WARNING] " + message + "\n";
        executePost(mes);
        if (enabled) {
            try {
                logWriter.write(mes);
            } catch (Exception e) {
                enabled = false;
                try {
                    logWriter.close();
                } catch (Exception ex) {
                }
                System.out.println("Logging error!");
                System.out.println("Log disabled!");
            }
        }
    }

    /**
     * Add error message to log
     * @param message
     */
    public void addErrorToLog(String message) {
        String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [ERROR] " + message + "\n";
        executePost(mes);
        if (enabled) {
            try {
                logWriter.write(mes);
            } catch (Exception e) {
                enabled = false;
                try {
                    logWriter.close();
                } catch (Exception ex) {
                }
                System.out.println("Logging error!");
                System.out.println("Log disabled!");
            }
        }
    }

    /**
     * Execute post to logserver
     * @param urlParameters
     */
    public static void executePost(String urlParameters) {
        LogThread lt = new LogThread(urlParameters);
        lt.run();

    }
}
