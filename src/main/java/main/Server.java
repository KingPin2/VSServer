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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Server extends UnicastRemoteObject implements Functions {

    private static Server server;
    public static Log log;

    private static final int ADMIN = 2;

    private Database db;

    private RandomString rs = new RandomString(10);
    private HashMap<String, NotifyUpdate> clients = new HashMap<String, NotifyUpdate>();
    private HashMap<String, User> user = new HashMap<String, User>();

    /**
     * Server instance
     *
     * @throws DatabaseConnectionException DatabaseConnectionException
     * @throws RemoteException             RemoteException
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
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
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
     *
     * @param key Client key
     * @param id  userID
     * @return User
     * @throws RemoteException                 RemoteException
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
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
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
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
     *
     * @param key  Client key
     * @param name username
     * @return User
     * @throws RemoteException                 RemoteException
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public User getSimpleUserByName(String key, String name) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get simple user by id: " + key + ";" + name);
        return this.db.getSimpleUserByName(name);
    }

    /**
     * Get all user
     *
     * @return Users
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     */
    @Override
    public ArrayList<User> getUsers(String key) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get all user: " + key);
        checkAuthReadMoreUser(key);
        return this.db.getUsers();
    }

    /**
     * Get all user with specified level
     *
     * @param key   Key
     * @param level Levek
     * @return Users
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     */
    @Override
    public ArrayList<User> getUsersByLevel(String key, int level) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get user by level: " + key + ";" + level);
        checkAuthReadMoreUser(key);
        return this.db.getUsersByLevel(level);
    }

    /**
     * Save a user
     *
     * @param key Key
     * @param u   User
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws DatabaseObjectNotSavedException DatabaseObjectNotSavedException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public void saveUser(String key, User u) throws DatabaseConnectionException, DatabaseObjectNotSavedException, UserAuthException {
        log.addToLog("Save user: " + key + ";" + u);
        checkAuthEditUser(key, u);
        this.db.saveUser(u);
    }

    /**
     * Get group by id
     *
     * @param key Key
     * @param id  Id
     * @return Group
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public Group getGroupById(String key, int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get group by id: " + key + ";" + id);
        checkAuth(key);
        return this.db.getGroupById(id);
    }

    /**
     * Get groups
     *
     * @param key Key
     * @return Groups
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public ArrayList<Group> getGroups(String key) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get all groups: " + key);
        checkAuth(key);
        return this.db.getGroups();
    }

    /**
     * Get groups by user
     *
     * @param key Key
     * @param u   User
     * @return Groups
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public ArrayList<Group> getGroupsByUser(String key, User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get groups by user: " + key + ";" + u);
        checkAuth(key);
        return this.db.getGroupsByUser(u);
    }

    /**
     * Get groups by moderator
     *
     * @param key Key
     * @param u   User
     * @return Groups
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public ArrayList<Group> getGroupsByModerator(String key, User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get groups by moderator: " + key + ";" + u);
        checkAuth(key);
        return this.db.getGroupsByModerator(u);
    }

    /**
     * Save group
     *
     * @param key Key
     * @param g   Group
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws DatabaseObjectNotSavedException DatabaseObjectNotSavedException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public void saveGroup(String key, Group g) throws DatabaseConnectionException, DatabaseObjectNotSavedException, UserAuthException {
        log.addToLog("Save group: " + key + ";" + g);
        checkAuthEditGroup(key, g);
        this.db.saveGroup(g);
    }

    /**
     * Get users not in group
     *
     * @param key   Key
     * @param group Group
     * @return Users
     * @throws UserAuthException UserAuthException
     */
    @Override
    public ArrayList<User> getUsersNotInGroup(String key, Group group) throws UserAuthException {
        log.addToLog("Get user not in group: " + key + ";" + group);
        checkAuth(key);
        return this.db.getUsersNotInGroup(group);
    }

    /**
     * Get message by id
     *
     * @param key  Key
     * @param id   Id
     * @param cRMI CachedFunctions
     * @return Message
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public Message getMessageById(String key, int id, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get message by id: " + key + ";" + id);
        checkAuth(key);
        Message m = this.db.getMessageById(id, cRMI);
        m.setKey(key);
        return m;
    }

    /**
     * Get messages by user
     *
     * @param key  Key
     * @param u    User
     * @param cRMI CachedFunctions
     * @return Messages
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public ArrayList<Message> getMessagesByUser(String key, User u, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get messages by user: " + key + ";" + u);
        checkAuth(key);
        ArrayList<Message> messages = new ArrayList<Message>();
        for (Message m : this.db.getMessagesByUser(u, cRMI)) {
            m.setKey(key);
            messages.add(m);
        }
        return messages;
    }

    /**
     * Get messages
     *
     * @param key  Key
     * @param cRMI CachedFunctions
     * @return Messages
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public ArrayList<Message> getMessages(String key, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get all messages: " + key);
        checkAuth(key);
        ArrayList<Message> messages = new ArrayList<Message>();
        for (Message m : this.db.getMessages(cRMI)) {
            m.setKey(key);
            messages.add(m);
        }
        return messages;
    }

    /**
     * Get messages by group
     *
     * @param key  Key
     * @param g    Group
     * @param cRMI CachedFunctions
     * @return Messages
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public ArrayList<Message> getMessagesByGroup(String key, Group g, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get messages by group: " + key + ";" + g);
        checkAuth(key);
        ArrayList<Message> messages = new ArrayList<Message>();
        for (Message m : this.db.getMessagesByGroup(g, cRMI)) {
            m.setKey(key);
            messages.add(m);
        }
        return messages;
    }

    /**
     * Save message
     *
     * @param key  Key
     * @param m    Message
     * @param cRMI CachedFunctions
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws DatabaseObjectNotSavedException DatabaseObjectNotSavedException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public void saveMessage(String key, Message m, CachedFunctions cRMI) throws DatabaseConnectionException, DatabaseObjectNotSavedException, UserAuthException {
        log.addToLog("Save message: " + key + ";" + m);
        checkAuthEditMessage(key, m);
        this.db.saveMessage(key, m, cRMI);
    }

    /**
     * Get group by name
     *
     * @param key  Key
     * @param name Name
     * @return Grouop
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    @Override
    public Group getGroupByName(String key, String name) throws DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        log.addToLog("Get group by name: " + key + ";" + name);
        checkAuth(key);
        return this.db.getGroupByName(name);
    }

    /**
     * Delete message
     *
     * @param key Key
     * @param m   Message
     * @throws DatabaseConnectionException       DatabaseConnectionException
     * @throws DatabaseObjectNotDeletedException DatabaseObjectNotDeletedException
     * @throws UserAuthException                 UserAuthException
     */
    @Override
    public void deleteMessage(String key, Message m) throws DatabaseConnectionException, DatabaseObjectNotDeletedException, UserAuthException {
        log.addToLog("Delete message: " + key + ";" + m);
        checkAuthEditMessage(key, m);
        this.db.deleteMessage(key, m);
    }

    /**
     * Delete user
     *
     * @param key  Key
     * @param u    User
     * @param cRMI CachedFunctions
     * @throws DatabaseConnectionException       DatabaseConnectionException
     * @throws DatabaseObjectNotDeletedException DatabaseObjectNotDeletedException
     * @throws DatabaseUserIsModException        DatabaseUserIsModException
     * @throws UserAuthException                 UserAuthException
     */
    @Override
    public void deleteUser(String key, User u, CachedFunctions cRMI) throws DatabaseConnectionException, DatabaseObjectNotDeletedException, DatabaseUserIsModException, UserAuthException {
        log.addToLog("Delete user: " + key + ";" + u);
        checkAuthEditUser(key, u);
        this.db.deleteUser(key, u, cRMI);
    }

    /**
     * Delete group
     *
     * @param key  Key
     * @param g    Group
     * @param cRMI CachedFunctions
     * @throws DatabaseConnectionException       DatabaseConnectionException
     * @throws DatabaseObjectNotDeletedException DatabaseObjectNotDeletedException
     * @throws UserAuthException                 UserAuthException
     */
    @Override
    public void deleteGroup(String key, Group g, CachedFunctions cRMI) throws DatabaseConnectionException, DatabaseObjectNotDeletedException, UserAuthException {
        log.addToLog("Delete group: " + key + ";" + g);
        checkAuthEditGroup(key, g);
        this.db.deleteGroup(key, g, cRMI);
    }

    /**
     * Connect a client
     *
     * @param upd NotifyUpdate
     * @return Key
     * @throws RemoteException RemoteException
     */
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

    /**
     * Disconnect a client
     *
     * @param key Key
     * @throws RemoteException RemoteException
     */
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

    /**
     * Login
     *
     * @param key      Key
     * @param username Username
     * @param password Password
     * @return User (or null)
     */
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

    /**
     * Logout
     *
     * @param key Key
     */
    @Override
    public void logout(String key) {
        if (user.containsKey(key)) {
            log.addToLog("Logout user: " + key + ";" + user.get(key));
            user.remove(key);
        }
    }

    /**
     * Check auth
     *
     * @param key Key
     * @throws UserAuthException Not allowed
     */
    private void checkAuthReadMoreUser(String key) throws UserAuthException {
        checkAuth(key);
        User auth = user.get(key);
        if (auth.getLevel() != ADMIN) {
            throw new UserAuthException();
        }
    }

    /**
     * Check auth
     *
     * @param key Key
     * @param u   User to read
     * @throws UserAuthException Not allowed
     */
    private void checkAuthReadSingleUser(String key, User u) throws UserAuthException {
        checkAuth(key);
        User auth = user.get(key);
        if (!auth.equals(u) && auth.getLevel() != ADMIN) {
            throw new UserAuthException();
        }
    }

    /**
     * Check auth
     *
     * @param key Key
     * @param u   User to edit
     * @throws UserAuthException Not allowed
     */
    private void checkAuthEditUser(String key, User u) throws UserAuthException {
        checkAuth(key);
        User auth = user.get(key);
        if (auth.getLevel() != ADMIN) {
            throw new UserAuthException();
        }
    }

    /**
     * Check auth
     *
     * @param key Key
     * @param g   Group to edit
     * @throws UserAuthException Not allowed
     */
    private void checkAuthEditGroup(String key, Group g) throws UserAuthException {
        checkAuth(key);
        User auth = user.get(key);
        if (auth.getLevel() != ADMIN) {
            throw new UserAuthException();
        }
    }

    /**
     * Check auth
     *
     * @param key Key
     * @param m   Message to edit
     * @throws UserAuthException Not allowed
     */
    private void checkAuthEditMessage(String key, Message m) throws UserAuthException {
        checkAuth(key);
        User auth = user.get(key);
        if (auth.getLevel() != ADMIN && m.getAuthorId() != auth.getID()) {
            throw new UserAuthException();
        }
    }

    /**
     * Check auth
     *
     * @param key Key
     * @throws UserAuthException Not allowed
     */
    private void checkAuth(String key) throws UserAuthException {
        if (!user.containsKey(key)) {
            throw new UserAuthException();
        }
    }


    /**
     * Main
     * -localLog Enable local log
     * -remoteLog Enable remote log
     *
     * @param args Arguments
     */
    public static void main(String args[]) {
        try {
            boolean local = false;
            boolean remote = false;
            try {
                for (String arg : args) {
                    if (arg.contains("-localLog")) {
                        local = true;
                    }
                    if (arg.contains("-remoteLog")) {
                        remote = true;
                    }
                }
            } catch (Exception e) {
                //Nothing to do here
            }
            log = new Log(local, remote);
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
     *
     * @param m    Message
     * @param type UpdateType
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
     *
     * @param g    Group
     * @param type UpdateType
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
     *
     * @param u    User
     * @param type UpdateType
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
