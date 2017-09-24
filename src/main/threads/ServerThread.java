package main.threads;

import main.database.Database;

/**
 * @author D.Bergum
 * Manage requests and delegate to UserThreads
 */
public class ServerThread implements Runnable{

    Database database = null;

    public ServerThread(Database database){
        this.database = database;
    }

    @Override
    public void run() {

    }
}
