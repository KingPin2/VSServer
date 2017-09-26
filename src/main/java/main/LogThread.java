package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dominik on 26.09.2017.
 */
public class LogThread implements Runnable {
    String mes;

    public LogThread(String mes){
        this.mes = mes;
    }

    @Override
    public void run() {
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
    }
}
