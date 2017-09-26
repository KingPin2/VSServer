package main.database.exceptions;

import java.io.Serializable;

/**
 * Created by Dominik on 08.07.2017.
 */
public class DatabaseObjectNotFoundException extends Exception implements Serializable{
    public DatabaseObjectNotFoundException(){
        super();
    }
}
