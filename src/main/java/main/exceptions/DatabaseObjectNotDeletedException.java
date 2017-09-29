package main.exceptions;

import java.io.Serializable;

/**
 * @author Dominik Bergum, 3603490
 */
public class DatabaseObjectNotDeletedException extends Exception implements Serializable {
    public DatabaseObjectNotDeletedException() {
        super();
    }
}
