package main.exceptions;

import java.io.Serializable;

/**
 * Created by Dominik on 08.07.2017.
 */
public class DatabaseObjectNotSavedException extends Exception implements Serializable{
    public DatabaseObjectNotSavedException(){
        super();
    }
}
