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
    User getUserById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    User getUserByName(String key, String name) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    ArrayList<User> getUsers(String key) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    ArrayList<User> getUsersByLevel(String key, int level) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    ArrayList <User> getUsersNotInGroup(String key, Group group) throws RemoteException, UserAuthException;
    void saveUser(String key, User u) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotSavedException, UserAuthException;
    void deleteUser(String key, User u, Functions rmi) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException, DatabaseUserIsModException, UserAuthException;
    Group getGroupById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    Group getGroupByName(String key, String name) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    ArrayList<Group> getGroups(String key) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    ArrayList <Group> getGroupsByUser(String key, User u) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    ArrayList <Group> getGroupsByModerator(String key, User u) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    void saveGroup(String key, Group g) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotSavedException, UserAuthException;
    void deleteGroup(String key, Group g, Functions rmi) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException, UserAuthException;
    Message getMessageById(String key, int id, Functions rmi) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    ArrayList<Message> getMessagesByUser(String key, User u, Functions rmi) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    ArrayList<Message> getMessages(String key, Functions rmi) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    ArrayList<Message> getMessagesByGroup(String key, Group g, Functions rmi) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException;
    void saveMessage(String key, Message m, Functions rmi) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotSavedException, UserAuthException;
    void deleteMessage(String key, Message m) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException, UserAuthException;
    String connect(NotifyUpdate upd) throws RemoteException;
    void disconnect(String key) throws RemoteException;
    User login(String key, String username, String password) throws RemoteException;
    void logout(String key) throws RemoteException;
}
