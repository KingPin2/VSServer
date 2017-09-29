package main.exceptions;

import java.io.Serializable;

/**
 * @author Dominik Bergum, 3603490
 */
public class DatabaseObjectNotSavedException extends Exception implements Serializable {
    public DatabaseObjectNotSavedException() {
        super();
    }
}
