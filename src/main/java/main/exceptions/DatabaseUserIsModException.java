package main.exceptions;

import java.io.Serializable;

/**
 * @author Dominik Bergum, 3603490
 */
public class DatabaseUserIsModException extends Exception implements Serializable {
    public DatabaseUserIsModException() {
        super();
    }
}
