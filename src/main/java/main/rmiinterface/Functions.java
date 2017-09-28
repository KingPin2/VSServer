package main.rmiinterface;

import main.exceptions.*;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Functions extends Remote
{
    String test(int testID) throws RemoteException;
    User getUserById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    User getUserByName(String key, String name) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<User> getUsers(String key) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<User> getUsersByLevel(String key, int level) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList <User> getUsersNotInGroup(String key, Group group) throws RemoteException;
    void saveUser(String key, User u) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotSavedException;
    void deleteUser(String key, User u, Functions rmi) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException, DatabaseUserIsModException;
    Group getGroupById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    Group getGroupByName(String key, String name) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<Group> getGroups(String key) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList <Group> getGroupsByUser(String key, User u) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList <Group> getGroupsByModerator(String key, User u) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    void saveGroup(String key, Group g) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotSavedException;
    void deleteGroup(String key, Group g, Functions rmi) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException;
    Message getMessageById(String key, int id, Functions rmi) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<Message> getMessagesByUser(String key, User u, Functions rmi) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<Message> getMessages(String key, Functions rmi) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<Message> getMessagesByGroup(String key, Group g, Functions rmi) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    void saveMessage(String key, Message m, Functions rmi) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotSavedException;
    void deleteMessage(String key, Message m) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException;
    String connect(NotifyUpdate upd) throws RemoteException;
    void disconnect(String key) throws RemoteException;
    User login(String key, String username, String password) throws RemoteException;
    void logout(String key) throws RemoteException;
}
