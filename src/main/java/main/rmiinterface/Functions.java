package main.rmiinterface;

import main.database.exceptions.DatabaseConnectionException;
import main.database.exceptions.DatabaseObjectNotDeletedException;
import main.database.exceptions.DatabaseObjectNotFoundException;
import main.database.exceptions.DatabaseObjectNotSavedException;
import main.objects.Board;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Functions extends Remote
{
    String test(int testID) throws RemoteException;
    User getUserById(int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    User getUserByName(String username) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<User> getUsers() throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<User> getUsersByLevel(int level) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    void saveUser(User user) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotSavedException;
    Board getBoardById(int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<Board> getBoards() throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    void saveBoard(Board board) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotSavedException;
    Group getGroupById(int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<Group> getGroups() throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList <Group> getGroupsByUser(User u) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList <Group> getGroupsByModerator(User u) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    void saveGroup(Group group) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotSavedException;
    ArrayList <User> getUsersNotInGroup(Group group) throws RemoteException;
    Message getMessageById(int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<Message> getMessagesByUser(User u) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<Message> getMessages() throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    ArrayList<Message> getMessagesByGroup(Group g) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    void saveMessage(Message message) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotSavedException;
    User loginUser(String username, String password) throws RemoteException;
    Group getGroupByName(String name) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException;
    void deleteMessage(Message m) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException;
    void deleteUser(User u) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException;
    void deleteBoard(Board b) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException;
    void deleteGroup(Group g) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException;
    void registerClient(NotifyUpdate updateListener) throws RemoteException;

}
