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

    public Log() {
        try {
            File directory = new File("log");
            if (! directory.exists()){
                directory.mkdir();
            }
            logWriter = new FileWriter("log/log_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".txt", true);
            enabled = true;
            System.out.println("Log ok!");
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
                String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": " + message + "\n";
                logWriter.write(mes);
                executePost(mes);
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
                String mes = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": [ERROR] " + message + "\n";
                logWriter.write(mes);
                executePost(mes);
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

    public static String executePost(String urlParameters) {
        HttpURLConnection connection = null;
        String targetURL = "http://log.bergum.de/serverLog.php";

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);

            //Send request
            PrintStream ps = new PrintStream(connection.getOutputStream());
            // send your parameters to your site
            ps.print("logMes=" + urlParameters);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            System.out.println(response.toString());
            return response.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
