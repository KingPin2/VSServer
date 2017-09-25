package main.rmiinterface;

import main.objects.Board;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.rmi.Remote;
import java.util.ArrayList;

public interface Functions extends Remote
{
    String test(int testID) throws Exception;
    User getUserById(int id) throws Exception;
    User getUserByName(String username) throws Exception;
    ArrayList<User> getUsers() throws Exception;
    ArrayList<User> getUsersByLevel(int level) throws Exception;
    void saveUser(User user) throws Exception;
    Board getBoardById(int id) throws Exception;
    ArrayList<Board> getBoards() throws Exception;
    void saveBoard(Board board) throws Exception;
    Group getGroupById(int id) throws Exception;
    ArrayList<Group> getGroups() throws Exception;
    ArrayList <Group> getGroupsByUser(User u) throws Exception;
    ArrayList <Group> getGroupsByModerator(User u) throws Exception;
    void saveGroup(Group group) throws Exception;
    ArrayList <User> getUsersNotInGroup(Group group) throws Exception;
    Message getMessageById(int id) throws Exception;
    ArrayList<Message> getMessagesByUser(User u) throws Exception;
    ArrayList<Message> getMessages() throws Exception;
    ArrayList<Message> getMessagesByGroup(Group g) throws Exception;
    void saveMessage(Message message) throws Exception;
    User loginUser(String username, String password) throws Exception;
    Group getGroupByName(String name) throws Exception;
    void deleteMessage(Message m) throws Exception;
    void deleteUser(User u) throws Exception;
    void deleteBoard(Board b) throws Exception;
    void deleteGroup(Group g) throws Exception;

}
