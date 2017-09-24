package main;

import main.database.Database;
import main.database.exceptions.DatabaseConnectionException;
import main.database.exceptions.DatabaseObjectNotFoundException;
import main.objects.Board;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import main.rmiinterface.Functions;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class Server implements Functions
{

    private Database db;

    public Server()
    {
        try{
            db = new Database();
            try {
                System.out.println(db.getUsers());
            } catch (DatabaseObjectNotFoundException e) {
                e.printStackTrace();
            }
        }catch (DatabaseConnectionException e){
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(int id) throws Exception {
        System.out.println("Get user by id: " + id);
        return this.db.getUserById(id);
    }
    @Override
    public User getUserByName(String username) throws Exception
    {
        System.out.println("Get user by name: " + username);
        return this.db.getUserByName(username);
    }
    @Override
    public ArrayList<User> getUsers() throws Exception {
        System.out.println("Login all user");
        return this.db.getUsers();
    }
    @Override
    public String test(int testID) throws Exception
    {
        System.out.println("Test");
        return "Hallo Welt!";
    }
    @Override
    public ArrayList<User> getUsersByLevel(int level) throws Exception {
        System.out.println("Get user by level: " + level);
        return this.db.getUsersByLevel(level);
    }
    @Override
    public void saveUser(User user) throws Exception {
        System.out.println("Save user: " + user);
        this.db.saveUser(user);
    }
    @Override
    public Board getBoardById(int id) throws Exception {
        System.out.println("Get board by id: " + id);
        return this.db.getBoardById(id);
    }
    @Override
    public ArrayList<Board> getBoards() throws Exception {
        System.out.println("Get all boards");
        return this.db.getBoards();
    }
    @Override
    public void saveBoard(Board board) throws Exception {
        System.out.println("Save board: " + board);
        this.db.saveBoard(board);
    }
    @Override
    public Group getGroupById(int id) throws Exception {
        System.out.println("Get group by id: " + id);
        return this.db.getGroupById(id);
    }
    @Override
    public ArrayList<Group> getGroups() throws Exception {
        System.out.println("Get all groups");
        return this.db.getGroups();
    }
    @Override
    public ArrayList<Group> getGroupsByUser(User u) throws Exception {
        System.out.println("Get groups by user: " + u);
        return this.db.getGroupsByUser(u);
    }
    @Override
    public void saveGroup(Group group) throws Exception {
        System.out.println("Save group: " + group);
        this.db.saveGroup(group);
    }
    @Override
    public ArrayList<User> getUsersNotInGroup(Group group) throws Exception {
        System.out.println("Get user not in group: " + group);
        return this.db.getUsersNotInGroup(group);
    }
    @Override
    public ArrayList<User> getUsersNotInGroup(int groupID) throws Exception {
        System.out.println("Get user not in group: " + groupID);
        return this.db.getUsersNotInGroup(groupID);
    }
    @Override
    public ArrayList<User> getGroupMembers(int groupId) throws Exception {
        System.out.println("Get group members: " + groupId);
        return this.db.getGroupMembers(groupId);
    }
    @Override
    public void deleteGroupMembers(int groupId) throws Exception {
        System.out.println("Delete group members: " + groupId);
        this.db.deleteGroupMembers(groupId);
    }
    @Override
    public void saveGroupMembers(int groupId, ArrayList<User> groupMembers) throws Exception {
        System.out.println("Save group members: " + groupId);
        this.db.saveGroupMembers(groupId, groupMembers);
    }

    @Override
    public Message getMessageById(int id) throws Exception {
        System.out.println("Get message by id: " + id);
        return this.db.getMessageById(id);
    }
    @Override
    public ArrayList<Message> getMessagesByUser(User u) throws Exception {
        System.out.println("Get messages by user: " + u);
        return this.db.getMessagesByUser(u);
    }
    @Override
    public ArrayList<Message> getMessages() throws Exception {
        System.out.println("Get all messages");
        return this.db.getMessages();
    }
    @Override
    public ArrayList<Message> getMessagesByGroup(Group g) throws Exception {
        System.out.println("Get messages by group: " + g);
        return this.db.getMessagesByGroup(g);
    }
    @Override
    public void saveMessage(Message message) throws Exception {
        System.out.println("Save message: " + message);
        this.db.saveMessage(message);
    }
    @Override
    public User loginUser(String username, String password) throws Exception {
        System.out.println("Login user: " + username);
        return this.db.loginUser(username, password);
    }

    @Override
    public Group getGroupByName(String name) throws Exception {
        System.out.println("Get group by name: " + name);
        return getGroupByName(name);
    }

    @Override
    public void deleteMessage(Message m) throws Exception {
        System.out.println("Delete message: " + m);
        this.db.deleteMessage(m);
    }


    public static void main(String args[])
    {
        try
        {
            Server obj = new Server();
            Functions stub = (Functions) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind("Functions", stub);

            System.out.println("Server laeuft");
        } catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
