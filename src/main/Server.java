package main;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import main.database.Database;
import main.exceptions.DatabaseConnectionException;

import main.objects.Board;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import main.rmiinterface.Functions;
import main.threads.ServerThread;


public class Server implements Functions
{

    private Database db;

    public Server()
    {
//        try{
//            db = new Database();
//        }catch (DatabaseConnectionException e){}
    }


    @Override
    public User getUserById(int id) throws Exception {
        return this.db.getUserById(id);
    }
    @Override
    public User getUserByName(String username) throws Exception
    {
        return this.db.getUserByName(username);
    }
    @Override
    public ArrayList<User> getUsers() throws Exception {
        return this.db.getUsers();
    }
    @Override
    public String test(int testID) throws Exception
    {
        return "Hallo Welt!";
    }
    @Override
    public ArrayList<User> getUsersByLevel(int level) throws Exception {
        return this.db.getUsersByLevel(level);
    }
    @Override
    public void saveUser(User user) throws Exception {
        this.db.saveUser(user);
    }
    @Override
    public Board getBoardById(int id) throws Exception {
        return this.db.getBoardById(id);
    }
    @Override
    public ArrayList<Board> getBoards() throws Exception {
        return this.db.getBoards();
    }
    @Override
    public void saveBoard(Board board) throws Exception {
        this.db.saveBoard(board);
    }
    @Override
    public Group getGroupById(int id) throws Exception {
        return this.db.getGroupById(id);
    }
    @Override
    public ArrayList<Group> getGroups() throws Exception {
        return this.db.getGroups();
    }
    @Override
    public ArrayList<Group> getGroupsByUser(User u) throws Exception {
        return this.db.getGroupsByUser(u);
    }
    @Override
    public void saveGroup(Group group) throws Exception {
        this.db.saveGroup(group);
    }
    @Override
    public ArrayList<User> getUsersNotInGroup(Group group) throws Exception {
        return this.db.getUsersNotInGroup(group);
    }
    @Override
    public ArrayList<User> getUsersNotInGroup(int groupID) throws Exception {
        return this.db.getUsersNotInGroup(groupID);
    }
    @Override
    public ArrayList<User> getGroupMembers(int groupId) throws Exception {
        return this.db.getGroupMembers(groupId);
    }
    @Override
    public void deleteGroupMembers(int groupId) throws Exception {
        this.db.deleteGroupMembers(groupId);
    }
    @Override
    public void saveGroupMembers(int groupId, ArrayList<User> groupMembers) throws Exception {
        this.db.saveGroupMembers(groupId, groupMembers);
    }

    @Override
    public Message getMessageById(int id) throws Exception {
        return this.db.getMessageById(id);
    }
    @Override
    public ArrayList<Message> getMessagesByUser(User u) throws Exception {
        return this.db.getMessagesByUser(u);
    }
    @Override
    public ArrayList<Message> getMessages() throws Exception {
        return this.db.getMessages();
    }
    @Override
    public ArrayList<Message> getMessagesByGroup(Group g) throws Exception {
        return this.db.getMessagesByGroup(g);
    }
    @Override
    public void saveMessage(Message message) throws Exception {
        this.db.saveMessage(message);
    }

    public User loginUser(String username, String password) throws Exception {
        return this.db.loginUser(username, password);
    }




    public static void main(String args[])
    {

//        Database database = null;
//        try {
//            database = new Database();
//            try {
//                ServerThread server = new ServerThread(database);
//                server.run();
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                database.closeDB();
//            }
//
//        } catch (DatabaseConnectionException e) {
//            e.printStackTrace();
//        }


        try
        {
            Server obj = new Server();
            Functions stub = (Functions) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind("Functions", stub);

            System.err.println("main.Server laeuft");
        } catch (Exception e)
        {
            System.err.println("main.Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
