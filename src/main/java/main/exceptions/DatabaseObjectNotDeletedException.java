package main.exceptions;

import java.io.Serializable;

/**
 * Created by Dominik on 08.07.2017.
 */
public class DatabaseObjectNotDeletedException extends Exception implements Serializable{
    public DatabaseObjectNotDeletedException(){
        super();
    }
}
