package main.exceptions;

import java.io.Serializable;

/**
 * @author Dominik Bergum, 3603490
 */
public class UserAuthException extends Exception implements Serializable {
    public UserAuthException() {
        super();
    }
}
