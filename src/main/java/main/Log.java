package main;

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
            logWriter = new FileWriter("log/log_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".txt", true);
            enabled = true;
        } catch (IOException e) {
            System.out.println("Could not open log!");
            System.out.println("Log disabled!");
        }
        ;
    }

    public void closeLog() {
        if (enabled) {
            try {
                logWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addToLog(String message) {
        if (enabled) {
            try {
                logWriter.write(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": " + message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addErrorToLog(String message) {
        if (enabled) {
            try {
                logWriter.write(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [ERROR] " + message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
