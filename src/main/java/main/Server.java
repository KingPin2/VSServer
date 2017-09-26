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

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class Server implements Functions
{

    private Database db;
    private Log log;

    private boolean working = false;
    private ArrayList<NotifyUpdate> updateListener = new ArrayList<NotifyUpdate>();

    public Server(Log log) throws DatabaseConnectionException {
        this.log = log;
        db = new Database(this);
    }

    public void notifyUserUpdated(){
        Thread notifyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (working) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e){
                    }
                }
                working = true;
                for (NotifyUpdate nu : updateListener) {
                    try{
                        nu.onUserUpdated();
                    } catch (Exception e) {

                    }
                }
                working = false;
            }
        });
    }

    public void notifyGroupUpdated(){
        Thread notifyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (working) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e){
                    }
                }
                working = true;
                for (NotifyUpdate nu : updateListener) {
                    try{
                        nu.onGroupUpdated();
                    } catch (Exception e) {

                    }
                }
                working = false;
            }
        });
    }

    public void notifyMessageUpdated(){
        Thread notifyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (working) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e){
                    }
                }
                working = true;
                for (NotifyUpdate nu : updateListener) {
                    try{
                        nu.onMessageUpdated();
                    } catch (Exception e) {

                    }
                }
                working = false;
            }
        });
    }

    public void addUpdateListener(NotifyUpdate nu){
        while (working) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e){
            }
        }
        working = true;
        this.updateListener.add(nu);
        working = false;
    }

    @Override
    public User getUserById(int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get user by id: " + id);
        log.addToLog("Get user by id: " + id);
        return this.db.getUserById(id);
    }
    @Override
    public User getUserByName(String username) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get user by name: " + username);
        log.addToLog("Get user by name: " + username);
        return this.db.getUserByName(username);
    }
    @Override
    public ArrayList<User> getUsers() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Login all user");
        log.addToLog("Login all user");
        return this.db.getUsers();
    }
    @Override
    public String test(int testID) 
    {
        System.out.println("Test");
        log.addToLog("Test");
        return "Hallo Welt!";
    }
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
    public void registerClient(NotifyUpdate updateListener)  {
        System.out.println("Registered update listener");
        log.addToLog("Registered update listener");
        addUpdateListener(updateListener);
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
            Functions stub = (Functions) UnicastRemoteObject.exportObject(obj, 0);

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

}
