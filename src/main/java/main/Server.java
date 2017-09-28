package main;

import main.database.Database;
import main.exceptions.*;
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

    private RandomString rs = new RandomString(10);
    private HashMap<String, NotifyUpdate> clients = new HashMap<String, NotifyUpdate>();
    private HashMap<String, User> user = new HashMap<String, User>();

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
    public User getUserById(String key, int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get user by id: " + key + ";" + id);
        log.addToLog("Get user by id: " + key + ";" + id);
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
    public User getUserByName(String key, String username) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get user by name: " + key + ";" + username);
        log.addToLog("Get user by name: " + key + ";" + username);
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
    public ArrayList<User> getUsers(String key) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get all user: " + key);
        log.addToLog("Get all user: " + key);
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
    public ArrayList<User> getUsersByLevel(String key, int level) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get user by level: " + key + ";" + level);
        log.addToLog("Get user by level: " + key + ";" + level);
        return this.db.getUsersByLevel(level);
    }

    @Override
    public void saveUser(String key, User user) throws DatabaseConnectionException, DatabaseObjectNotSavedException {
        System.out.println("Save user: " + key + ";" + user);
        log.addToLog("Save user: " + key + ";" + user);
        this.db.saveUser(user);
    }

    @Override
    public Group getGroupById(String key, int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get group by id: " + key + ";" + id);
        log.addToLog("Get group by id: " + key + ";" + id);
        return this.db.getGroupById(id);
    }

    @Override
    public ArrayList<Group> getGroups(String key) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get all groups: " + key);
        log.addToLog("Get all groups: " + key);
        return this.db.getGroups();
    }

    @Override
    public ArrayList<Group> getGroupsByUser(String key, User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get groups by user: " + key + ";" + u);
        log.addToLog("Get groups by user: " + key + ";" + u);
        return this.db.getGroupsByUser(u);
    }

    @Override
    public ArrayList<Group> getGroupsByModerator(String key, User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get groups by moderator: " + key + ";" + u);
        log.addToLog("Get groups by moderator: " + key + ";" + u);
        return this.db.getGroupsByModerator(u);
    }

    @Override
    public void saveGroup(String key, Group group) throws DatabaseConnectionException, DatabaseObjectNotSavedException {
        System.out.println("Save group: " + key + ";" + group);
        log.addToLog("Save group: " + key + ";" + group);
        this.db.saveGroup(group);
    }

    @Override
    public ArrayList<User> getUsersNotInGroup(String key, Group group) {
        System.out.println("Get user not in group: " + key + ";" + group);
        log.addToLog("Get user not in group: " + key + ";" + group);
        return this.db.getUsersNotInGroup(group);
    }

    @Override
    public Message getMessageById(String key, int id, Functions rmi) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get message by id: " + key + ";" + id);
        log.addToLog("Get message by id: " + key + ";" + id);
        Message m = this.db.getMessageById(id, rmi);
        m.setKey(key);
        return m;
    }

    @Override
    public ArrayList<Message> getMessagesByUser(String key, User u, Functions rmi) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get messages by user: " + key + ";" + u);
        log.addToLog("Get messages by user: " + key + ";" + u);
        ArrayList<Message> messages = new ArrayList<Message>();
        for (Message m : this.db.getMessagesByUser(u,rmi)){
            m.setKey(key);
            messages.add(m);
        }
        return messages;
    }

    @Override
    public ArrayList<Message> getMessages(String key, Functions rmi) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get all messages: " + key);
        log.addToLog("Get all messages: " + key);
        ArrayList<Message> messages = new ArrayList<Message>();
        for (Message m : this.db.getMessages(rmi)){
            m.setKey(key);
            messages.add(m);
        }
        return messages;
    }

    @Override
    public ArrayList<Message> getMessagesByGroup(String key, Group g, Functions rmi) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get messages by group: " + key + ";" + g);
        log.addToLog("Get messages by group: " + key + ";" + g);
        ArrayList<Message> messages = new ArrayList<Message>();
        for (Message m : this.db.getMessagesByGroup(g, rmi)){
            m.setKey(key);
            messages.add(m);
        }
        return messages;
    }

    @Override
    public void saveMessage(String key, Message message, Functions rmi) throws DatabaseConnectionException, DatabaseObjectNotSavedException {
        System.out.println("Save message: " + key + ";" + message);
        log.addToLog("Save message: " + key + ";" + message);
        this.db.saveMessage(message, rmi);
    }

    @Override
    public Group getGroupByName(String key, String name) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        System.out.println("Get group by name: " + key + ";" + name);
        log.addToLog("Get group by name: " + key + ";" + name);
        return this.db.getGroupByName(name);
    }

    @Override
    public void deleteMessage(String key, Message m) throws DatabaseConnectionException, DatabaseObjectNotDeletedException {
        System.out.println("Delete message: " + key + ";" + m);
        log.addToLog("Delete message: " + key + ";" + m);
        this.db.deleteMessage(m);
    }

    @Override
    public void deleteUser(String key, User u, Functions rmi) throws DatabaseConnectionException, DatabaseObjectNotDeletedException, DatabaseUserIsModException {
        System.out.println("Delete user: " + key + ";" + u);
        log.addToLog("Delete user: " + key + ";" + u);
        this.db.deleteUser(u, rmi);
    }

    @Override
    public void deleteGroup(String key, Group g, Functions rmi) throws DatabaseConnectionException, DatabaseObjectNotDeletedException {
        System.out.println("Delete group: " + key + ";" + g);
        log.addToLog("Delete group: " + key + ";" + g);
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
    public void disconnect(String key) throws RemoteException {
        try {
            System.out.println("Disconnect: " + key + ";" + key);
            clients.remove(key);
            logout(key);
            log.addToLog("Disconnect: " + key + ";" + key);
        } catch (Exception e) {
            log.addErrorToLog("Disconnect: " + key + ";" + e.toString());
        }
    }

    @Override
    public User login(String key, String username, String password) {
        User erg = null;
        if (!user.containsKey(key)) {
            System.out.println("Login user: " + key + ";" + username);
            log.addToLog("Login user: " + key + ";" + username);
            erg = this.db.loginUser(username, password);
            if (erg != null) {
                user.put(key, erg);
            }
        }
        return erg;
    }

    @Override
    public void logout(String key) {
        if (user.containsKey(key)) {
            System.out.println("Logout user: " + key + ";" + user.get(key));
            log.addToLog("Logout user: " + key + ";" + user.get(key));
            user.remove(key);
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
