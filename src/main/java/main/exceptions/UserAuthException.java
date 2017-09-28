package main.exceptions;

import java.io.Serializable;

/**
 * Created by Dominik on 08.07.2017.
 */
public class UserAuthException extends Exception implements Serializable {
    public UserAuthException(){
        super();
    }
}
