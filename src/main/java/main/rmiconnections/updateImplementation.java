package main.rmiconnections;

import main.rmiinterface.NotifyUpdate;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class updateImplementation extends UnicastRemoteObject implements NotifyUpdate , Serializable{

    protected updateImplementation() throws RemoteException {
    }

    @Override
    public void onUserUpdated() {
    }

    @Override
    public void onMessageUpdated() {

    }

    @Override
    public void onGroupUpdated() {

    }
}
