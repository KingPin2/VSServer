package main.functions;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dominik on 26.09.2017.
 */
public class LogThread implements Runnable {

    public enum LogType {
        LOCAL, REMOTE
    }

    private String mes;
    private LogType type;
    private FileWriter logWriter;
    private LogCallback cb;

    /**
     * Initiate log thread
     *
     * @param mes       Message
     * @param cb        LogCallback
     * @param logWriter FileWriter
     * @param type      LogType
     */
    public LogThread(String mes, LogType type, FileWriter logWriter, LogCallback cb) {
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
                rd.close();
            } catch (Exception e) {
                System.err.println(e.toString());
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
