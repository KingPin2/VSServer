package main;

import main.database.Database;
import main.database.exceptions.DatabaseConnectionException;
import main.database.exceptions.DatabaseObjectNotDeletedException;
import main.database.exceptions.DatabaseObjectNotFoundException;
import main.database.exceptions.DatabaseObjectNotSavedException;
import main.objects.Board;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import main.rmiinterface.Functions;
import main.rmiinterface.NotifyUpdate;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Server extends UnicastRemoteObject implements Functions
{

    private Database db;
    private Log log;

    private RandomString rs = new RandomString(15);
    private HashMap<String, NotifyUpdate> clients = new HashMap<String, NotifyUpdate>();

    /**
     * Server instance
     * @param log Logging object
     * @throws DatabaseConnectionException
     * @throws RemoteException
     */
    public Server(Log log) throws DatabaseConnectionException, RemoteException {
        super();
        this.log = log;
        db = new Database(this);
    }

    /**
     * Returns the logging object
     * @return Log
     */
    public Log getLogger(){
        return this.log;
    }

    /**
     * Get a user by id
     * @param id userId
     * @return User
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    @Override
    public User getUserById(int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get user by id: " + id);
        log.addToLog("Get user by id: " + id);
        return this.db.getUserById(id);
    }

    /**
     * Get a user by name
     * @param username
     * @return User
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    @Override
    public User getUserByName(String username) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get user by name: " + username);
        log.addToLog("Get user by name: " + username);
        return this.db.getUserByName(username);
    }

    /**
     * Get all user
     * @return Userlist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    @Override
    public ArrayList<User> getUsers() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get all user");
        log.addToLog("Get all user");
        return this.db.getUsers();
    }

    /**
     * Test
     * @param testID
     * @return Hallo Welt
     */
    @Override
    public String test(int testID) 
    {
        System.out.println("Test");
        log.addToLog("Test");
        return "Hallo Welt!";
    }

    /**
     * Get all user with specified level
     * @param level
     * @return Userlist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    @Override
    public ArrayList<User> getUsersByLevel(int level) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get user by level: " + level);
        log.addToLog("Get user by level: " + level);
        return this.db.getUsersByLevel(level);
    }
    @Override
    public void saveUser(User user) throws DatabaseConnectionException, DatabaseObjectNotSavedException {
        System.out.println("Save user: " + user);
        log.addToLog("Save user: " + user);
        this.db.saveUser(user);
    }
    @Override
    public Board getBoardById(int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get board by id: " + id);
        log.addToLog("Get board by id: " + id);
        return this.db.getBoardById(id);
    }
    @Override
    public ArrayList<Board> getBoards() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get all boards");
        log.addToLog("Get all boards");
        return this.db.getBoards();
    }
    @Override
    public void saveBoard(Board board) throws DatabaseConnectionException, DatabaseObjectNotSavedException {
        System.out.println("Save board: " + board);
        log.addToLog("Save board: " + board);
        this.db.saveBoard(board);
    }
    @Override
    public Group getGroupById(int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get group by id: " + id);
        log.addToLog("Get group by id: " + id);
        return this.db.getGroupById(id);
    }
    @Override
    public ArrayList<Group> getGroups() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get all groups");
        log.addToLog("Get all groups");
        return this.db.getGroups();
    }
    @Override
    public ArrayList<Group> getGroupsByUser(User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get groups by user: " + u);
        log.addToLog("Get groups by user: " + u);
        return this.db.getGroupsByUser(u);
    }

    @Override
    public ArrayList<Group> getGroupsByModerator(User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get groups by moderator: " + u);
        log.addToLog("Get groups by moderator: " + u);
        return this.db.getGroupsByModerator(u);
    }

    @Override
    public void saveGroup(Group group) throws DatabaseConnectionException, DatabaseObjectNotSavedException {
        System.out.println("Save group: " + group);
        log.addToLog("Save group: " + group);
        this.db.saveGroup(group);
    }
    @Override
    public ArrayList<User> getUsersNotInGroup(Group group)  {
        System.out.println("Get user not in group: " + group);
        log.addToLog("Get user not in group: " + group);
        return this.db.getUsersNotInGroup(group);
    }
    @Override
    public Message getMessageById(int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get message by id: " + id);
        log.addToLog("Get message by id: " + id);
        return this.db.getMessageById(id);
    }
    @Override
    public ArrayList<Message> getMessagesByUser(User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get messages by user: " + u);
        log.addToLog("Get messages by user: " + u);
        return this.db.getMessagesByUser(u);
    }
    @Override
    public ArrayList<Message> getMessages() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get all messages");
        log.addToLog("Get all messages");
        return this.db.getMessages();
    }
    @Override
    public ArrayList<Message> getMessagesByGroup(Group g) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get messages by group: " + g);
        log.addToLog("Get messages by group: " + g);
        return this.db.getMessagesByGroup(g);
    }
    @Override
    public void saveMessage(Message message) throws DatabaseConnectionException, DatabaseObjectNotSavedException {
        System.out.println("Save message: " + message);
        log.addToLog("Save message: " + message);
        this.db.saveMessage(message);
    }
    @Override
    public User loginUser(String username, String password)  {
        System.out.println("Login user: " + username);
        log.addToLog("Login user: " + username);
        return this.db.loginUser(username, password);
    }

    @Override
    public Group getGroupByName(String name) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get group by name: " + name);
        log.addToLog("Get group by name: " + name);
        return this.db.getGroupByName(name);
    }

    @Override
    public void deleteMessage(Message m) throws DatabaseConnectionException, DatabaseObjectNotDeletedException {
        System.out.println("Delete message: " + m);
        log.addToLog("Delete message: " + m);
        this.db.deleteMessage(m);
    }

    @Override
    public void deleteUser(User u) throws DatabaseConnectionException, DatabaseObjectNotDeletedException {
        System.out.println("Delete user: " + u);
        log.addToLog("Delete user: " + u);
        this.db.deleteUser(u);
    }

    @Override
    public void deleteBoard(Board b) throws DatabaseConnectionException, DatabaseObjectNotDeletedException {
        System.out.println("Delete board: " + b);
        log.addToLog("Delete board: " + b);
        this.db.deleteBoard(b);
    }

    @Override
    public void deleteGroup(Group g) throws DatabaseConnectionException, DatabaseObjectNotDeletedException {
        System.out.println("Delete group: " + g);
        log.addToLog("Delete group: " + g);
        this.db.deleteGroup(g);
    }

    @Override
    public String connect(NotifyUpdate upd) throws RemoteException {
        String random = rs.nextString();

        while (clients.containsKey(random)){
            random = rs.nextString();
        }

        System.out.println("Connect: " + random);
        clients.put(random,upd);
        log.addToLog("Connect: " + random);
        return random;
    }

    @Override
    public void disconnect(String id) throws RemoteException {
        try {
            System.out.println("Disconnect: " + id);
            clients.remove(id);
            log.addToLog("Disconnect: " + id);
        } catch (Exception e){
            log.addErrorToLog(e.toString());
        }
    }


    public static void main(String args[])
    {
        Log log = null;
        try
        {
            System.out.println("-------------------------------------------------");
            System.out.println("");
            log = new Log();
            System.out.println("");
            System.out.println("Starting server...");
            log.addToLog("Starting server");
            Server obj = new Server(log);
            Functions stub;
            try {
                stub = (Functions) UnicastRemoteObject.exportObject(obj, 0);
            } catch (Exception e) {
                stub = (Functions) UnicastRemoteObject.toStub(obj);
            }

            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind("Functions", stub);

            System.out.println("");
            System.out.println("-------------------------------------------------");
            System.out.println("");
            System.out.println("Server running...");
        } catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            log.addErrorToLog(e.toString());
            e.printStackTrace();
        } finally {
            try {
                log.closeLog();
            } catch (Exception e){

            }
        }
    }

    /**
     * Notify all connected clients, that messages are updated
     */
    public void notifyMessageUpdated() {
        for (Map.Entry<String,NotifyUpdate> ent : clients.entrySet()){
            try {
                ent.getValue().onUpdateMessage();
            } catch (Exception e) {
                log.addErrorToLog(e.toString());
                clients.remove(ent.getKey());
            }
        }
    }

    /**
     * Notify all connected clients, that groups are updated
     */
    public void notifyGroupUpdated() {
        for (Map.Entry<String,NotifyUpdate> ent : clients.entrySet()){
            try {
                ent.getValue().onUpdateGroup();
            } catch (Exception e) {
                log.addErrorToLog(e.toString());
                clients.remove(ent.getKey());
            }
        }
    }

    /**
     * Notify all connected clients, that user are updated
     */
    public void notifyUserUpdated() {
        for (Map.Entry<String,NotifyUpdate> ent : clients.entrySet()){
            try {
                ent.getValue().onUpdateUser();
            } catch (Exception e) {
                log.addErrorToLog(e.toString());
                clients.remove(ent.getKey());
            }
        }
    }
}
