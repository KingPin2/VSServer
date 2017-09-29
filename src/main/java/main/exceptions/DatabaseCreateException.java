package main.exceptions;

import java.io.Serializable;

/**
 * @author Dominik Bergum, 3603490
 */
public class DatabaseCreateException extends Exception implements Serializable {
    public DatabaseCreateException() {
        super();
    }
}
