package main.rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Dominik on 26.09.2017.
 */
public interface NotifyUpdate extends Remote{
    void onUpdateGroup() throws RemoteException;
    void onUpdateUser() throws RemoteException;
    void onUpdateMessage() throws RemoteException;
}
