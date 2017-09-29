package main.exceptions;

import java.io.Serializable;

/**
 * Created by Dominik on 08.07.2017.
 */
public class DatabaseCreateException extends Exception implements Serializable{
    public DatabaseCreateException(){
        super();
    }
}
