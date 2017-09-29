package main.rmiinterface;

import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Notify update interface
 *
 * @author Dominik Bergum, 3603490
 */
public interface NotifyUpdate extends Remote {
    void onUpdateGroup(Group g, UpdateType type) throws RemoteException;

    void onUpdateUser(User u, UpdateType type) throws RemoteException;

    void onUpdateMessage(Message m, UpdateType type) throws RemoteException;
}
