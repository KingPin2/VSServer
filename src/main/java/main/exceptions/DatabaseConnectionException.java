package main.exceptions;

import java.io.Serializable;

/**
 * @author Dominik Bergum, 3603490
 */
public class DatabaseConnectionException extends Exception implements Serializable {
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
