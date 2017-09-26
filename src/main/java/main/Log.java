package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Dominik on 26.09.2017.
 */
public class Log {

    private FileWriter logWriter;
    private boolean enabled = false;

    public Log() {
        try {
            File directory = new File("log");
            if (! directory.exists()){
                directory.mkdir();
            }
            logWriter = new FileWriter("log/log_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".txt", true);
            enabled = true;
        } catch (Exception e) {
            System.out.println("Could not open log!");
            System.out.println("Log disabled!");
        }
        ;
    }

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

    public void addToLog(String message) {
        if (enabled) {
            try {
                logWriter.write(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": " + message + "\n");
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

    public void addErrorToLog(String message) {
        if (enabled) {
            try {
                logWriter.write(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [ERROR] " + message + "\n");
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
}
