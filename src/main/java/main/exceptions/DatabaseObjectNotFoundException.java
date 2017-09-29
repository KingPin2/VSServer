package main.exceptions;

import java.io.Serializable;

/**
 * @author Dominik Bergum, 3603490
 */
public class DatabaseObjectNotFoundException extends Exception implements Serializable {
    public DatabaseObjectNotFoundException() {
        super();
    }
}
