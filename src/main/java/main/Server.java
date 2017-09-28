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
import main.rmiinterface.CachedFunctions;
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

    public static final int ADMIN = 2;

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
    public User getUserById(String key, int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get user by id: " + key + ";" + id);
        User u = this.db.getUserById(id);
        checkAuthReadSingleUser(key, u);
        return u;
    }

    /**
     * Get a simple user by id
     * @param key Client key
     * @param id userID
     * @return User
     * @throws RemoteException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     * @throws UserAuthException
     */
    @Override
    public User getSimpleUserById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get simple user by id: " + key + ";" + id);
        return this.db.getSimpleUserById(id);
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
    public User getUserByName(String key, String username) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get user by name: " + key + ";" + username);
        User u = this.db.getUserByName(username);
        checkAuthReadSingleUser(key, u);
        return u;
    }

    /**
     * Get a simple user by name
     * @param key Client key
     * @param name username
     * @return User
     * @throws RemoteException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     * @throws UserAuthException
     */
    @Override
    public User getSimpleUserByName(String key, String name) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get simple user by id: " + key + ";" + name);
        return this.db.getSimpleUserByName(name);
    }

    /**
     * Get all user
     *
     * @return Userlist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    @Override
    public ArrayList<User> getUsers(String key) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get all user: " + key);
        checkAuthReadMoreUser(key);
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
        log.addToLog("Test");
        return "Hallo Welt!";
    }

    /**
     * Get all user with specified level
     *
     * @param key
     * @param level
     * @return Userlist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    @Override
    public ArrayList<User> getUsersByLevel(String key, int level) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get user by level: " + key + ";" + level);
        checkAuthReadMoreUser(key);
        return this.db.getUsersByLevel(level);
    }

    /**
     * Save a user
     * @param key
     * @param u
     * @throws DatabaseConnectionException
     * @throws DatabaseObjectNotSavedException
     * @throws UserAuthException
     */
    @Override
    public void saveUser(String key, User u) throws DatabaseConnectionException, DatabaseObjectNotSavedException, UserAuthException {
        log.addToLog("Save user: " + key + ";" + u);
        checkAuthEditUser(key,u);
        this.db.saveUser(u);
    }

    @Override
    public Group getGroupById(String key, int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get group by id: " + key + ";" + id);
        checkAuth(key);
        return this.db.getGroupById(id);
    }

    @Override
    public ArrayList<Group> getGroups(String key) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get all groups: " + key);
        checkAuth(key);
        return this.db.getGroups();
    }

    @Override
    public ArrayList<Group> getGroupsByUser(String key, User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get groups by user: " + key + ";" + u);
        checkAuth(key);
        return this.db.getGroupsByUser(u);
    }

    @Override
    public ArrayList<Group> getGroupsByModerator(String key, User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get groups by moderator: " + key + ";" + u);
        checkAuth(key);
        return this.db.getGroupsByModerator(u);
    }

    @Override
    public void saveGroup(String key, Group g) throws DatabaseConnectionException, DatabaseObjectNotSavedException, UserAuthException {
        log.addToLog("Save group: " + key + ";" + g);
        checkAuthEditGroup(key, g);
        this.db.saveGroup(g);
    }

    @Override
    public ArrayList<User> getUsersNotInGroup(String key, Group group) throws UserAuthException {
        log.addToLog("Get user not in group: " + key + ";" + group);
        checkAuth(key);
        return this.db.getUsersNotInGroup(group);
    }

    @Override
    public Message getMessageById(String key, int id, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get message by id: " + key + ";" + id);
        checkAuth(key);
        Message m = this.db.getMessageById(id, cRMI);
        m.setKey(key);
        return m;
    }

    @Override
    public ArrayList<Message> getMessagesByUser(String key, User u, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get messages by user: " + key + ";" + u);
        checkAuth(key);
        ArrayList<Message> messages = new ArrayList<Message>();
        for (Message m : this.db.getMessagesByUser(u,cRMI)){
            m.setKey(key);
            messages.add(m);
        }
        return messages;
    }

    @Override
    public ArrayList<Message> getMessages(String key, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get all messages: " + key);
        checkAuth(key);
        ArrayList<Message> messages = new ArrayList<Message>();
        for (Message m : this.db.getMessages(cRMI)){
            m.setKey(key);
            messages.add(m);
        }
        return messages;
    }

    @Override
    public ArrayList<Message> getMessagesByGroup(String key, Group g, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get messages by group: " + key + ";" + g);
        checkAuth(key);
        ArrayList<Message> messages = new ArrayList<Message>();
        for (Message m : this.db.getMessagesByGroup(g, cRMI)){
            m.setKey(key);
            messages.add(m);
        }
        return messages;
    }

    @Override
    public void saveMessage(String key, Message m, CachedFunctions cRMI) throws DatabaseConnectionException, DatabaseObjectNotSavedException, UserAuthException {
        log.addToLog("Save message: " + key + ";" + m);
        checkAuthEditMessage(key,m);
        this.db.saveMessage(key, m, cRMI);
    }

    @Override
    public Group getGroupByName(String key, String name) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get group by name: " + key + ";" + name);
        checkAuth(key);
        return this.db.getGroupByName(name);
    }

    @Override
    public void deleteMessage(String key, Message m) throws DatabaseConnectionException, DatabaseObjectNotDeletedException, UserAuthException {
        log.addToLog("Delete message: " + key + ";" + m);
        checkAuthEditMessage(key,m);
        this.db.deleteMessage(key, m);
    }

    @Override
    public void deleteUser(String key, User u, CachedFunctions cRMI) throws DatabaseConnectionException, DatabaseObjectNotDeletedException, DatabaseUserIsModException, UserAuthException {
        log.addToLog("Delete user: " + key + ";" + u);
        checkAuthEditUser(key,u);
        this.db.deleteUser(key, u, cRMI);
    }

    @Override
    public void deleteGroup(String key, Group g, CachedFunctions cRMI) throws DatabaseConnectionException, DatabaseObjectNotDeletedException, UserAuthException {
        log.addToLog("Delete group: " + key + ";" + g);
        checkAuthEditGroup(key, g);
        this.db.deleteGroup(key, g, cRMI);
    }

    @Override
    public String connect(NotifyUpdate upd) throws RemoteException {
        String random = rs.nextString();

        while (clients.containsKey(random)) {
            random = rs.nextString();
        }

        clients.put(random, upd);
        log.addToLog("Connect: " + random);
        return random;
    }

    @Override
    public void disconnect(String key) throws RemoteException {
        try {
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
            log.addToLog("Logout user: " + key + ";" + user.get(key));
            user.remove(key);
        }
    }

    private void checkAuthReadMoreUser(String key) throws UserAuthException {
        checkAuth(key);
        User auth = user.get(key);
        if (auth.getLevel() != ADMIN){
            throw new UserAuthException();
        }
    }

    private void checkAuthReadSingleUser(String key, User u) throws UserAuthException {
        checkAuth(key);
        User auth = user.get(key);
        if (!auth.equals(u) && auth.getLevel() != ADMIN){
            throw new UserAuthException();
        }
    }

    private void checkAuthEditUser(String key, User u) throws UserAuthException {
        checkAuth(key);
        User auth = user.get(key);
        if (auth.getLevel() != ADMIN){
            throw new UserAuthException();
        }
    }

    private void checkAuthEditGroup(String key, Group g) throws UserAuthException {
        checkAuth(key);
        User auth = user.get(key);
        if (auth.getLevel() != ADMIN){
            throw new UserAuthException();
        }
    }

    private void checkAuthEditMessage(String key, Message m) throws UserAuthException {
        checkAuth(key);
        User auth = user.get(key);
        if (auth.getLevel() != ADMIN && m.getAuthorId() != auth.getID()){
            throw new UserAuthException();
        }
    }

    private void checkAuth(String key) throws UserAuthException {
        if (!user.containsKey(key)) {
            throw new UserAuthException();
        }
    }


    public static void main(String args[]) {
        try {
            log = new Log();
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
            log.addToLog("Server running");
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
            log.addToLog("Notify message updated: " + type + "; " + m);
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
            log.addToLog("Notify group updated: " + type + "; " + g);
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
            log.addToLog("Notify user updated: " + type + "; " + u);
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
