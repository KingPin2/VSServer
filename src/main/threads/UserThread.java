package main.threads;

import main.database.Database;

/**
 * Created by Dominik on 08.07.2017.
 */
public class UserThread implements Runnable{

    Database database = null;
    ServerThread server = null;

    public UserThread(Database database, ServerThread server){
        this.database = database;
        this.server = server;
    }

    @Override
    public void run() {

    }
}
