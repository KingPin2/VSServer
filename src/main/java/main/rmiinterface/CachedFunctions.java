package main.rmiinterface;

import main.exceptions.*;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by Dominik on 29.09.2017.
 */
public class CachedFunctions{

    Functions rmi;
    HashMap<Integer, User> uCache = new HashMap<Integer, User>();
    HashMap<Integer, Group> gCache = new HashMap<Integer, Group>();
    HashMap<Integer, Message> mCache = new HashMap<Integer, Message>();

    /**
     * Instantiate
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
     * @param key Auth key
     * @param id userId
     * @return User
     * @throws RemoteException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     * @throws UserAuthException
     */
    public User getUserById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        if (!uCache.containsKey(id)){
            uCache.put(id,rmi.getUserById(key,id));
        }
        return uCache.get(id);
    }


    /**
     * Get a simple user by id (cached)
     * @param key Auth key
     * @param id userId
     * @return User
     * @throws RemoteException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     * @throws UserAuthException
     */
    public User getSimpleUserById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        if (!uCache.containsKey(id)){
            uCache.put(id,rmi.getSimpleUserById(key,id));
        }
        return uCache.get(id);
    }

    /**
     * Get a group by id (cached)
     * @param key Auth key
     * @param id groupId
     * @return Group
     * @throws RemoteException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     * @throws UserAuthException
     */
    public Group getGroupById(String key, int id) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        if (!gCache.containsKey(id)){
            gCache.put(id,rmi.getGroupById(key,id));
        }
        return gCache.get(id);
    }

    /**
     * Get message by id (cached)
     * @param key Auth key
     * @param id messageId
     * @param cRMI RMI functions
     * @return Message
     * @throws RemoteException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     * @throws UserAuthException
     */
    public Message getMessageById(String key, int id, CachedFunctions cRMI) throws RemoteException, DatabaseObjectNotFoundException, DatabaseConnectionException, UserAuthException {
        if (!mCache.containsKey(id)){
            mCache.put(id,rmi.getMessageById(key,id, cRMI));
        }
        return mCache.get(id);
    }

    /**
     * Remove from cache, if message is updated
     * @param m Message
     */
    public void onUpdateMessage(Message m){
        if (m != null && mCache.containsKey(m.getID())){
            mCache.remove(m.getID());
        }
    }

    /**
     * Remove from cache, if group is updated
     * @param g Group
     */
    public void onUpdateGroup(Group g){
        if (g != null && gCache.containsKey(g.getID())){
            gCache.remove(g.getID());
        }
    }

    /**
     * Remove from cache, if user is updated
     * @param u User
     */
    public void onUpdateUser(User u){
        if (u != null && uCache.containsKey(u.getID())){
            uCache.remove(u.getID());
        }
    }
}
