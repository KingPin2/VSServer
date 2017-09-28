package main;

import main.database.Database;
import main.database.exceptions.*;
import main.functions.Log;
import main.functions.NotifyCallback;
import main.functions.NotifyThread;
import main.functions.RandomString;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import main.rmiinterface.Functions;
import main.rmiinterface.NotifyUpdate;
import main.rmiinterface.UpdateType;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class Server extends UnicastRemoteObject implements Functions {

    public static Server server;
    public static Log log;

    private Database db;

    private RandomString rs = new RandomString(15);
    private HashMap<String, NotifyUpdate> clients = new HashMap<String, NotifyUpdate>();

    /**
     * Server instance
     *
     * @throws DatabaseConnectionException
     * @throws RemoteException
     */
    public Server() throws DatabaseConnectionException, RemoteException {
        super();
        db = new Database(this);
    }

    /**
     * Get a user by id
     *
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
     *
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
     *
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
     *
     * @param testID
     * @return Hallo Welt
     */
    @Override
    public String test(int testID) {
        System.out.println("Test");
        log.addToLog("Test");
        return "Hallo Welt!";
    }

    /**
     * Get all user with specified level
     *
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
    public ArrayList<User> getUsersNotInGroup(Group group) {
        System.out.println("Get user not in group: " + group);
        log.addToLog("Get user not in group: " + group);
        return this.db.getUsersNotInGroup(group);
    }

    @Override
    public Message getMessageById(int id, Functions rmi) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get message by id: " + id);
        log.addToLog("Get message by id: " + id);
        return this.db.getMessageById(id, rmi);
    }

    @Override
    public ArrayList<Message> getMessagesByUser(User u, Functions rmi) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get messages by user: " + u);
        log.addToLog("Get messages by user: " + u);
        return this.db.getMessagesByUser(u, rmi);
    }

    @Override
    public ArrayList<Message> getMessages(Functions rmi) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get all messages");
        log.addToLog("Get all messages");
        return this.db.getMessages(rmi);
    }

    @Override
    public ArrayList<Message> getMessagesByGroup(Group g, Functions rmi) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get messages by group: " + g);
        log.addToLog("Get messages by group: " + g);
        return this.db.getMessagesByGroup(g, rmi);
    }

    @Override
    public void saveMessage(Message message, Functions rmi) throws DatabaseConnectionException, DatabaseObjectNotSavedException {
        System.out.println("Save message: " + message);
        log.addToLog("Save message: " + message);
        this.db.saveMessage(message, rmi);
    }

    @Override
    public User loginUser(String username, String password) {
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
    public void deleteUser(User u, Functions rmi) throws DatabaseConnectionException, DatabaseObjectNotDeletedException, DatabaseUserIsModException {
        System.out.println("Delete user: " + u);
        log.addToLog("Delete user: " + u);
        this.db.deleteUser(u, rmi);
    }

    @Override
    public void deleteGroup(Group g, Functions rmi) throws DatabaseConnectionException, DatabaseObjectNotDeletedException {
        System.out.println("Delete group: " + g);
        log.addToLog("Delete group: " + g);
        this.db.deleteGroup(g, rmi);
    }

    @Override
    public String connect(NotifyUpdate upd) throws RemoteException {
        String random = rs.nextString();

        while (clients.containsKey(random)) {
            random = rs.nextString();
        }

        System.out.println("Connect: " + random);
        clients.put(random, upd);
        log.addToLog("Connect: " + random);
        return random;
    }

    @Override
    public void disconnect(String id) throws RemoteException {
        try {
            System.out.println("Disconnect: " + id);
            clients.remove(id);
            log.addToLog("Disconnect: " + id);
        } catch (Exception e) {
            log.addErrorToLog("Disconnect: " + e.toString());
        }
    }


    public static void main(String args[]) {
        try {
            System.out.println("-------------------------------------------------");
            System.out.println("");
            log = new Log();
            System.out.println("");
            System.out.println("Starting server...");
            log.addToLog("Starting server");
            server = new Server();
            Functions stub;
            try {
                stub = (Functions) UnicastRemoteObject.exportObject(server, 0);
            } catch (Exception e) {
                stub = (Functions) UnicastRemoteObject.toStub(server);
            }

            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind("Functions", stub);

            System.out.println("");
            System.out.println("-------------------------------------------------");
            System.out.println("");
            System.out.println("Server running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            log.addErrorToLog("Main: " + e.toString());
        }
    }

    /**
     * Notify all connected clients, that messages are updated
     */
    public void notifyMessageUpdated(Message m, UpdateType type) {
        try {
            Iterator<Map.Entry<String, NotifyUpdate>> iter = clients.entrySet().iterator();

            while (iter.hasNext()) {
                NotifyThread nt = new NotifyThread(log, NotifyThread.NotifyType.MESSAGE, iter.next(), new NotifyCallback() {
                    @Override
                    public void notifyRemoved(String key) {
                        iter.remove();
                    }
                }, type, m);
                nt.run();
            }
        } catch (Exception e) {
            log.addErrorToLog("notifyMessageUpdated: " + e.toString());
        }
    }

    /**
     * Notify all connected clients, that groups are updated
     */
    public void notifyGroupUpdated(Group g, UpdateType type) {
        try {
            Iterator<Map.Entry<String, NotifyUpdate>> iter = clients.entrySet().iterator();

            while (iter.hasNext()) {
                NotifyThread nt = new NotifyThread(log, NotifyThread.NotifyType.GROUP, iter.next(), new NotifyCallback() {
                    @Override
                    public void notifyRemoved(String key) {
                        iter.remove();
                    }
                }, type, g);
                nt.run();
            }
        } catch (Exception e) {
            log.addErrorToLog("notifyGroupUpdated: " + e.toString());
        }
    }

    /**
     * Notify all connected clients, that user are updated
     */
    public void notifyUserUpdated(User u, UpdateType type) {
        try {
            Iterator<Map.Entry<String, NotifyUpdate>> iter = clients.entrySet().iterator();

            while (iter.hasNext()) {
                NotifyThread nt = new NotifyThread(log, NotifyThread.NotifyType.USER, iter.next(), new NotifyCallback() {
                    @Override
                    public void notifyRemoved(String key) {
                        iter.remove();
                    }
                }, type, u);
                nt.run();
            }
        } catch (Exception e) {
            log.addErrorToLog("notifyUserUpdated: " + e.toString());
        }
    }
}
