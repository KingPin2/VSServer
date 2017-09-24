package main.exceptions;

/**
 * Created by Dominik on 08.07.2017.
 */
public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException(String message){
        super(message);
    }
}
