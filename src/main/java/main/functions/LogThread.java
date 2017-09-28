package main.functions;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dominik on 26.09.2017.
 */
public class LogThread implements Runnable {

    public enum LogType{
        LOCAL, REMOTE
    }

    String mes;
    LogType type;
    FileWriter logWriter;
    LogCallback cb;

    /**
     * Initiate log thread
     * @param mes
     */
    public LogThread(String mes, LogType type, FileWriter logWriter, LogCallback cb){
        this.mes = mes;
        this.type = type;
        this.logWriter = logWriter;
        this.cb = cb;
    }

    /**
     * Run log thread
     */
    @Override
    public void run() {
        if (type == LogType.REMOTE) {
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
                ps.print("logMes=" + mes);

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
            } catch (Exception e) {
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } else {
            try {
                logWriter.write(mes);
            } catch (Exception e) {
                cb.notifyDisabled();
            }
        }
    }
}
