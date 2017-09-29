package main.exceptions;

import java.io.Serializable;

/**
 * @author Dominik Bergum, 3603490
 */
public class DatabaseException extends Exception implements Serializable {
    public DatabaseException(String message) {
        super(message);
    }
}
