package main.rmiinterface;

import main.exceptions.DatabaseConnectionException;
import main.exceptions.DatabaseObjectNotFoundException;
import main.exceptions.UserAuthException;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Cache RMI function calls
 *
 * @author Dominik Bergum, 3603490
 */
public class CachedFunctions implements Serializable {

    private Functions rmi;
    private HashMap<Integer, User> uCache = new HashMap<Integer, User>();
    private HashMap<Integer, Group> gCache = new HashMap<Integer, Group>();
    private HashMap<Integer, Message> mCache = new HashMap<Integer, Message>();

    /**
     * Instantiate
     *
     * @param rmi RMI Interface
     */
    public CachedFunctions(Functions rmi) {
        this.rmi = rmi;
        uCache = new HashMap<Integer, User>();
        gCache = new HashMap<Integer, Group>();
        mCache = new HashMap<Integer, Message>();
    }

    /**
     * Get a user by id (cached)
     *
     * @param key Auth key
     * @param id  userId
     * @return User
     * @throws RemoteException                 RemoteException
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    public User getUserById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        if (!uCache.containsKey(id)) {
            uCache.put(id, rmi.getUserById(key, id));
        }
        return uCache.get(id);
    }


    /**
     * Get a simple user by id (cached)
     *
     * @param key Auth key
     * @param id  userId
     * @return User
     * @throws RemoteException                 RemoteException
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    public User getSimpleUserById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        if (!uCache.containsKey(id)) {
            uCache.put(id, rmi.getSimpleUserById(key, id));
        }
        return uCache.get(id);
    }

    /**
     * Get a group by id (cached)
     *
     * @param key Auth key
     * @param id  groupId
     * @return Group
     * @throws RemoteException                 RemoteException
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    public Group getGroupById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        if (!gCache.containsKey(id)) {
            gCache.put(id, rmi.getGroupById(key, id));
        }
        return gCache.get(id);
    }

    /**
     * Get message by id (cached)
     *
     * @param key  Auth key
     * @param id   messageId
     * @param cRMI RMI functions
     * @return Message
     * @throws RemoteException                 RemoteException
     * @throws DatabaseObjectNotFoundException DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException     DatabaseConnectionException
     * @throws UserAuthException               UserAuthException
     */
    public Message getMessageById(String key, int id, CachedFunctions cRMI) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        if (!mCache.containsKey(id)) {
            mCache.put(id, rmi.getMessageById(key, id, cRMI));
        }
        return mCache.get(id);
    }

    /**
     * Remove from cache, if message is updated
     *
     * @param m Message
     */
    public void onUpdateMessage(Message m, UpdateType type) {
        try {
            if (m != null && mCache.containsKey(m.getID())) {
                mCache.remove(m.getID());
            }
            if (type != UpdateType.DELETE) {
                mCache.put(m.getID(), m);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Remove from cache, if group is updated
     *
     * @param g Group
     */
    public void onUpdateGroup(Group g, UpdateType type) {
        try {
            if (g != null && gCache.containsKey(g.getID())) {
                gCache.remove(g.getID());
            }
            if (type != UpdateType.DELETE) {
                gCache.put(g.getID(), g);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Remove from cache, if user is updated
     *
     * @param u User
     */
    public void onUpdateUser(User u, UpdateType type) {
        try {
            if (u != null && uCache.containsKey(u.getID())) {
                uCache.remove(u.getID());
            }
            if (type != UpdateType.DELETE) {
                uCache.put(u.getID(), u);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
