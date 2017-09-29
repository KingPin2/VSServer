package main.exceptions;

import java.io.Serializable;

/**
 * @author Dominik Bergum, 3603490
 */
public class DatabaseDropException extends Exception implements Serializable {
    public DatabaseDropException() {
        super();
    }
}
