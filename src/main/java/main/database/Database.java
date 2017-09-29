package main.database;

import main.Server;
import main.exceptions.*;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import main.rmiinterface.CachedFunctions;
import main.rmiinterface.UpdateType;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Object <-> Database Mapping
 *
 * @author Dominik Bergum, 3603490
 */
public class Database {

    private DBConnection dbcon = null;
    private Server server;

    /**
     * Connect to database
     *
     * @throws DatabaseConnectionException DatabaseConnectionException
     */
    public Database(Server server) throws DatabaseConnectionException {
        this.server = server;
        dbcon = new DBConnection();
        this.openDB();
    }

    /**
     * Connect to database
     */
    private synchronized void openDB() {
        try {
            if (!dbcon.isOpen()) {
                dbcon.openDB();
                initDB();
            }
        } catch (DatabaseConnectionException e) {
            Server.log.addErrorToLog("openDB: " + e.toString());
        }
    }

    /**
     * Test if database is valid
     *
     * @return Database valid
     */
    private boolean testDatabase() {
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'User';");
            rs.close();
            dbcon.free();
            rs = dbcon.execute("SELECT * FROM 'Group';");
            rs.close();
            dbcon.free();
            rs = dbcon.execute("SELECT * FROM 'Message';");
            rs.close();
            dbcon.free();
            rs = dbcon.execute("SELECT * FROM 'Group_User';");
            rs.close();
            dbcon.free();
        } catch (Exception e) {
            Server.log.addWarningToLog("Database test failed.");
            return false;
        }
        Server.log.addToLog("Database test succeeded.");
        return true;
    }

    /**
     * Initialize database if not valid
     */
    private synchronized void initDB() {
        try {
            if (!testDatabase()) {
                Server.log.addToLog("Initialize database.");
                try {
                    dbcon.execute("DROP TABLE `Group`;");
                } catch (Exception e) {
                    //Nothing to do here
                }
                try {
                    dbcon.execute("DROP TABLE `Group_User`;");
                } catch (Exception e) {
                    //Nothing to do here
                }
                try {
                    dbcon.execute("DROP TABLE `Message`;");
                } catch (Exception e) {
                    //Nothing to do here
                }
                try {
                    dbcon.execute("DROP TABLE `User`;");
                } catch (Exception e) {
                    //Nothing to do here
                }
                dbcon.execute("CREATE TABLE IF NOT EXISTS `Board` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL UNIQUE, `groupId` INTEGER NOT NULL, `userId` INTEGER NOT NULL );");
                dbcon.execute("CREATE TABLE IF NOT EXISTS `Group` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL UNIQUE, `modId` INTEGER NOT NULL );");
                dbcon.execute("CREATE TABLE IF NOT EXISTS `Group_User` ( `groupId` INTEGER NOT NULL, `userId` INTEGER NOT NULL );");
                dbcon.execute("CREATE TABLE IF NOT EXISTS `Message` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `message` TEXT NOT NULL, `authorId` INTEGER NOT NULL, `groupId` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL );");
                dbcon.execute("CREATE TABLE IF NOT EXISTS `User` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL UNIQUE, `password` TEXT NOT NULL, `level` INTEGER NOT NULL );");
                saveUser(ObjectFactory.createUser("Admin", "admin", 2));
                saveGroup(ObjectFactory.createEmptyGroup("Broadcast", getUserByName("Admin")));
                Server.log.addToLog("-----------------");
                Server.log.addToLog("Default User:");
                Server.log.addToLog("Username: Admin");
                Server.log.addToLog("Passwort: admin");
                Server.log.addToLog("-----------------");
            }
        } catch (Exception e) {
            Server.log.addErrorToLog("initDB: " + e.toString());
        }
    }

    /**
     * Get a user from database by id
     *
     * @param id UserId
     * @return User
     * @throws DatabaseObjectNotFoundException User does not exist
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized User getUserById(int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'User' WHERE id = '" + id + "';");
            if (rs.next()) {
                User u = new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"), rs.getInt("level"));
                rs.close();
                dbcon.free();
                return u;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getUserById: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get a simple user from database by id
     *
     * @param id UserId
     * @return User
     * @throws DatabaseObjectNotFoundException User does not exist
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized User getSimpleUserById(int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'User' WHERE id = '" + id + "';");
            if (rs.next()) {
                User u = new User(rs.getInt("id"), rs.getString("name"), "NONE", 42);
                rs.close();
                dbcon.free();
                return u;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getSimpleUserById: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get a user from database by username
     *
     * @param username User Name
     * @return User
     * @throws DatabaseObjectNotFoundException User does not exist
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized User getUserByName(String username) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'User' WHERE Name = '" + username + "' COLLATE NOCASE;");
            if (rs.next()) {
                User u = new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"), rs.getInt("level"));
                rs.close();
                dbcon.free();
                return u;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getUserByName: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get a simple user from database by username
     *
     * @param username User Name
     * @return User
     * @throws DatabaseObjectNotFoundException User does not exist
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized User getSimpleUserByName(String username) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'User' WHERE Name = '" + username + "' COLLATE NOCASE;");
            if (rs.next()) {
                User u = new User(rs.getInt("id"), rs.getString("name"), "NONE", 42);
                rs.close();
                dbcon.free();
                return u;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getSimpleUserByName: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all users from database
     *
     * @return Userlist
     * @throws DatabaseObjectNotFoundException No user found
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized ArrayList<User> getUsers() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<User> users = new ArrayList<>();
            ResultSet rs = dbcon.execute("SELECT * FROM 'User' WHERE Level != '0';");
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"), rs.getInt("level")));
            }
            rs.close();
            dbcon.free();
            if (users.size() > 0) {
                return users;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getUsers: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all simple users from database
     *
     * @return Userlist
     * @throws DatabaseObjectNotFoundException No user found
     * @throws DatabaseConnectionException     Not connected to the database
     */
    private synchronized ArrayList<User> getSimpleUsers() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<User> users = new ArrayList<>();
            ResultSet rs = dbcon.execute("SELECT * FROM 'User' WHERE Level != '0';");
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("name"), "NONE", 42));
            }
            rs.close();
            dbcon.free();
            if (users.size() > 0) {
                return users;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getSimpleUsers: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get a user by level from database
     *
     * @param level UserLevel
     * @return User
     * @throws DatabaseObjectNotFoundException No user found
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized ArrayList<User> getUsersByLevel(int level) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<User> users = new ArrayList<>();
            ResultSet rs = dbcon.execute("SELECT * FROM 'User' WHERE level = '" + level + "';");
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"), rs.getInt("level")));
            }
            rs.close();
            dbcon.free();
            if (users.size() > 0) {
                return users;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getUserByLevel: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Save a user in the database
     *
     * @param user User
     * @throws DatabaseObjectNotSavedException User could not be saved
     * @throws IllegalArgumentException        User null
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized void saveUser(User user) throws DatabaseObjectNotSavedException, IllegalArgumentException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (user != null) {
            if (user.getPassword().equalsIgnoreCase("NONE") && user.getLevel() == 42) {
                throw new IllegalArgumentException("Don't save simple user!");
            }
            try {
                UpdateType type;
                if (user.getID() == -1) {
                    dbcon.execute("INSERT INTO 'User' (name, password, level) VALUES ('" + escapeSQLString(user.getName()) + "','" + escapeSQLString(user.getPassword()) + "','" + user.getLevel() + "');");
                    type = UpdateType.SAVE;
                } else {
                    dbcon.execute("UPDATE 'User' SET name = '" + escapeSQLString(user.getName()) + "', password = '" + escapeSQLString(user.getPassword()) + "', level = '" + user.getLevel() + "' WHERE id = '" + user.getID() + "';");
                    type = UpdateType.UPDATE;
                }
                if (user.getLevel() != 0) {
                    try {
                        Group broadcast = getGroupByName("Broadcast");
                        broadcast.addMember(getUserByName(user.getName()));
                        saveGroup(broadcast);
                    } catch (Exception e) {
                        //Nothing to do here
                    }
                    server.notifyUserUpdated(getUserByName(user.getName()), type);
                }
            } catch (Exception e) {
                Server.log.addErrorToLog("saveUser: " + e.toString());
                throw new DatabaseObjectNotSavedException();
            }
        } else {
            throw new IllegalArgumentException("User null.");
        }
    }

    /**
     * Delete a user in the database
     *
     * @param user User
     * @throws DatabaseUserIsModException        User is moderator in a group (and could not be deleted)
     * @throws DatabaseObjectNotDeletedException User could not be deleted
     * @throws IllegalArgumentException          User null
     * @throws DatabaseConnectionException       Not connected to the database
     */
    public synchronized void deleteUser(String key, User user, CachedFunctions cRMI) throws IllegalArgumentException, DatabaseConnectionException, DatabaseObjectNotDeletedException, DatabaseUserIsModException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (user != null) {
            try {
                if (user.getID() == -1) {
                    throw new DatabaseObjectNotDeletedException();
                } else {
                    try {
                        ArrayList<Group> modGroups = getGroupsByModerator(user);
                        if (modGroups.size() > 0) {
                            throw new DatabaseUserIsModException();
                        }
                    } catch (DatabaseUserIsModException uim) {
                        throw uim;
                    } catch (Exception e) {
                        //Nothing to do here
                    }

                    try {
                        ArrayList<Group> groups = getGroupsByUser(user);
                        for (Group g : groups) {
                            g.removeMember(user);
                            saveGroup(g);
                        }
                    } catch (Exception e) {
                        //Nothing to do here
                    }

                    try {
                        ArrayList<Message> messages = getMessagesByUser(user, cRMI);
                        for (Message m : messages) {
                            deleteMessage(key, m);
                        }
                    } catch (Exception e) {
                        //Nothing to do here
                    }
                    dbcon.execute("DELETE FROM 'User' WHERE id = '" + user.getID() + "';");
                    if (user.getLevel() != 0) {
                        server.notifyUserUpdated(user, UpdateType.DELETE);
                    }
                }
            } catch (DatabaseUserIsModException uim) {
                throw uim;
            } catch (Exception e) {
                Server.log.addErrorToLog("deleteUser: " + e.toString());
                throw new DatabaseObjectNotDeletedException();
            }
        } else {
            throw new IllegalArgumentException("User null.");
        }
    }

    /**
     * Get a group from database by id
     *
     * @param id GroupId
     * @return Group
     * @throws DatabaseObjectNotFoundException Group does not exist
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized Group getGroupById(int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'Group' WHERE id = '" + id + "';");
            if (rs.next()) {
                int mId = rs.getInt("modId");
                Group g = new Group(rs.getInt("id"), rs.getString("name"), null, null);
                rs.close();
                dbcon.free();
                if (mId != -1) {
                    try {
                        g.setModerator(getSimpleUserById(mId));
                    } catch (Exception e) {
                        //Nothing to do here
                    }
                }
                g.setMembers(getGroupMembers(g.getID()));
                return g;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getGroupById: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get a group from database by name
     *
     * @param name Groupname
     * @return Group
     * @throws DatabaseObjectNotFoundException Group does not exist
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized Group getGroupByName(String name) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'Group' WHERE name = '" + name + "' COLLATE NOCASE;");
            if (rs.next()) {
                int mId = rs.getInt("modId");
                Group g = new Group(rs.getInt("id"), rs.getString("name"), null, null);
                rs.close();
                dbcon.free();
                if (mId != -1) {
                    try {
                        g.setModerator(getSimpleUserById(mId));
                    } catch (Exception e) {
                        //Nothing to do here
                    }
                }
                g.setMembers(getGroupMembers(g.getID()));
                return g;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getGroupByName: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all groups for one user from database
     *
     * @return Grouplist
     * @throws DatabaseObjectNotFoundException No groups found
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized ArrayList<Group> getGroupsByUser(User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Group> groups = new ArrayList<>();
            ArrayList<Integer> gIDs = new ArrayList<>();
            ResultSet rs = dbcon.execute("SELECT id FROM 'Group' WHERE modId = '" + u.getID() + "';");
            while (rs.next()) {
                gIDs.add(rs.getInt("id"));
            }
            rs.close();
            dbcon.free();
            ResultSet rs2 = dbcon.execute("SELECT groupId FROM 'Group_User' WHERE userId = '" + u.getID() + "';");
            while (rs2.next()) {
                gIDs.add(rs.getInt("groupId"));
            }
            rs2.close();
            dbcon.free();
            for (Integer i : gIDs) {
                try {
                    groups.add(getGroupById(i));
                } catch (Exception e) {
                    //Nothing to do here
                }
            }
            if (groups.size() > 0) {
                return groups;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getGroupsByUser: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all groups for one moderator from database
     *
     * @return Grouplist
     * @throws DatabaseObjectNotFoundException No groups found
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized ArrayList<Group> getGroupsByModerator(User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Group> groups = new ArrayList<>();
            ArrayList<Integer> gIDs = new ArrayList<>();
            ResultSet rs = dbcon.execute("SELECT id FROM 'Group' WHERE modId = '" + u.getID() + "';");
            while (rs.next()) {
                gIDs.add(rs.getInt("id"));
            }
            rs.close();
            dbcon.free();
            for (Integer i : gIDs) {
                try {
                    groups.add(getGroupById(i));
                } catch (Exception e) {
                    //Nothing to do here
                }
            }
            if (groups.size() > 0) {
                return groups;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getGroupsByModerator: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all groups from database
     *
     * @return Grouplist
     * @throws DatabaseObjectNotFoundException No groups found
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized ArrayList<Group> getGroups() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Group> groups = new ArrayList<>();
            ArrayList<Integer> mIds = new ArrayList<>();
            ResultSet rs = dbcon.execute("SELECT * FROM 'Group';");
            while (rs.next()) {
                groups.add(new Group(rs.getInt("id"), rs.getString("name"), null, null));
                mIds.add(rs.getInt("modId"));
            }
            rs.close();
            dbcon.free();
            if (groups.size() > 0) {
                for (int i = 0; i < groups.size(); i++) {
                    if (mIds.get(i) != -1) {
                        try {
                            groups.get(i).setModerator(getSimpleUserById(mIds.get(i)));
                        } catch (Exception e) {
                            //Nothing to do here
                        }
                    }
                    groups.get(i).setMembers(getGroupMembers(groups.get(i).getID()));
                }
                return groups;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getGroups: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Save a group in the database
     *
     * @param group Group
     * @throws DatabaseObjectNotSavedException Group could not be saved
     * @throws IllegalArgumentException        Group null
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized void saveGroup(Group group) throws DatabaseObjectNotSavedException, IllegalArgumentException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (group != null) {
            try {
                try {
                    getSimpleUserByName(group.getName());
                } catch (DatabaseObjectNotFoundException dbonfe) {
                    saveUser(ObjectFactory.createUser(group.getName(), group.getName(), 0));
                    group.addMember(getUserByName(group.getName()));
                }
                UpdateType type;
                if (group.getModerator() != null) {
                    group.addMember(group.getModerator());
                }
                int groupId = -1;
                if (group.getID() == -1) {
                    ResultSet rs = dbcon.execute("INSERT INTO 'Group' (name, modId) VALUES ('" + escapeSQLString(group.getName()) + "','" + group.getModerator().getID() + "');");
                    if (rs != null) {
                        try {
                            groupId = rs.getInt(1);
                        } catch (Exception e) {
                            groupId = -1;
                        }
                    }
                    type = UpdateType.SAVE;
                } else {
                    dbcon.execute("UPDATE 'Group' SET name = '" + escapeSQLString(group.getName()) + "', modId = '" + group.getModerator().getID() + "' WHERE id = '" + group.getID() + "';");
                    groupId = group.getID();
                    deleteGroupMembers(group.getID());
                    type = UpdateType.UPDATE;
                }
                if (groupId != -1) {
                    saveGroupMembers(groupId, group.getMembers());
                }
                server.notifyGroupUpdated(getGroupByName(group.getName()), type);
            } catch (Exception e) {
                Server.log.addErrorToLog("saveGroup: " + e.toString());
                throw new DatabaseObjectNotSavedException();
            }
        } else {
            throw new IllegalArgumentException("Group null.");
        }
    }

    /**
     * Delete a user in the database
     *
     * @param group Group
     * @throws DatabaseObjectNotDeletedException Group could not be deleted
     * @throws IllegalArgumentException          Group null
     * @throws DatabaseConnectionException       Not connected to the database
     */
    public synchronized void deleteGroup(String key, Group group, CachedFunctions cRMI) throws IllegalArgumentException, DatabaseConnectionException, DatabaseObjectNotDeletedException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (group != null) {
            try {
                if (group.getID() == -1) {
                    throw new DatabaseObjectNotDeletedException();
                } else {
                    try {
                        ArrayList<Message> messages = getMessagesByGroup(group, cRMI);
                        for (Message m : messages) {
                            deleteMessage(key, m);
                        }
                    } catch (Exception e) {
                        //Nothing to do here
                    }

                    dbcon.execute("DELETE FROM 'Group' WHERE id = '" + group.getID() + "';");
                    deleteGroupMembers(group.getID());
                    server.notifyGroupUpdated(group, UpdateType.DELETE);
                    try {
                        deleteUser(key, getUserByName(group.getName()), cRMI);
                    } catch (DatabaseObjectNotDeletedException e) {
                        Server.log.addErrorToLog("deleteGroup: Group user not deleted.");
                    }
                }
            } catch (Exception e) {
                Server.log.addErrorToLog("deleteGroup: " + e.toString());
                throw new DatabaseObjectNotDeletedException();
            }
        } else {
            throw new IllegalArgumentException("Group null.");
        }
    }

    /**
     * Get all group members
     *
     * @param groupId groupId
     * @return groupMembers
     * @throws DatabaseConnectionException Not connected to the database
     */
    private synchronized ArrayList<User> getGroupMembers(int groupId) throws DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Integer> uIds = new ArrayList<>();
            ResultSet rs = dbcon.execute("SELECT * FROM 'Group_User' WHERE groupId = '" + groupId + "';");
            while (rs.next()) {
                uIds.add(rs.getInt("userId"));
            }
            rs.close();
            dbcon.free();
            if (uIds.size() > 0) {
                ArrayList<User> members = new ArrayList<>();

                for (int i : uIds) {
                    try {
                        User u = getSimpleUserById(i);
                        if (members.contains(u)) {
                            members.remove(u);
                        }
                        members.add(u);
                    } catch (Exception e) {
                        //Nothing to do here
                    }
                }

                return members;
            }
            return null;
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getGroupMembers: " + e.toString());
            return null;
        }
    }

    /**
     * Get all not group members
     *
     * @param group Group
     * @return not group members (null, if found nothing)
     */
    public synchronized ArrayList<User> getUsersNotInGroup(Group group) {
        if (group == null) {
            return null;
        }
        return getUsersNotInGroup(group.getID());
    }

    /**
     * Get all not group members
     *
     * @param groupId Group id
     * @return not group members (null, if found nothing)
     */
    private synchronized ArrayList<User> getUsersNotInGroup(int groupId) {
        try {
            ArrayList<User> members = getGroupMembers(groupId);
            ArrayList<User> users = getSimpleUsers();
            ArrayList<User> notInGroup = new ArrayList<>();
            for (User u : users) {
                boolean found = false;
                for (User m : members) {
                    if (u.getName().equalsIgnoreCase(m.getName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    notInGroup.add(u);
                }
            }
            if (notInGroup.size() > 0) {
                return notInGroup;
            }
            return null;
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getUsersNotInGroup: " + e.toString());
            return null;
        }
    }

    /**
     * Delete all group members
     *
     * @param groupId groupId
     * @throws DatabaseConnectionException Not connected to the database
     */
    private synchronized void deleteGroupMembers(int groupId) throws DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            dbcon.execute("DELETE FROM 'Group_User' WHERE groupId = '" + groupId + "';");
        } catch (Exception e) {
            Server.log.addErrorToLog("deleteGroupMembers: " + e.toString());
            dbcon.free();
        }
    }

    /**
     * Save all group members
     *
     * @param groupId      groupId
     * @param groupMembers groupMembers
     * @throws DatabaseConnectionException     Not connected to the database
     * @throws DatabaseObjectNotSavedException Group members could not be saved
     */
    private synchronized void saveGroupMembers(int groupId, ArrayList<User> groupMembers) throws DatabaseConnectionException, DatabaseObjectNotSavedException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (groupMembers != null && groupMembers.size() > 0) {
            try {
                for (User gM : groupMembers) {
                    dbcon.execute("INSERT INTO 'Group_User' (groupId, userId) VALUES ('" + groupId + "','" + gM.getID() + "');");
                }
            } catch (Exception e) {
                Server.log.addErrorToLog("saveGroupMembers: " + e.toString());
                throw new DatabaseObjectNotSavedException();
            }
        }
    }

    /**
     * Get a message from database by id
     *
     * @param id MessageId
     * @return Message
     * @throws DatabaseObjectNotFoundException Message does not exist
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized Message getMessageById(int id, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'Message' WHERE id = '" + id + "';");
            if (rs.next()) {
                int gId = rs.getInt("groupId");
                int aId = rs.getInt("authorId");
                Message m = new Message(rs.getInt("id"), rs.getString("message"), null, null, rs.getLong("timestamp"), cRMI);
                rs.close();
                dbcon.free();
                if (gId != -1) {
                    try {
                        m.setGroupId(gId);
                    } catch (Exception e) {
                        //Nothing to do here
                    }
                }
                if (aId != -1) {
                    try {
                        m.setAuthorId(aId);
                    } catch (Exception e) {
                        //Nothing to do here
                    }
                }
                return m;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getMessageById: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all messages for a user from database
     *
     * @param u User
     * @return Boardlist
     * @throws DatabaseObjectNotFoundException Message does not exist
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized ArrayList<Message> getMessagesByUser(User u, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Message> messages = new ArrayList<>();
            ArrayList<Integer> mIds = new ArrayList<>();
            ResultSet rs = dbcon.execute("SELECT id FROM 'Message' WHERE authorId='" + u.getID() + "' ORDER BY timestamp DESC;");
            while (rs.next()) {
                mIds.add(rs.getInt("id"));
            }
            rs.close();
            dbcon.free();
            for (Integer i : mIds) {
                try {
                    messages.add(getMessageById(i, cRMI));
                } catch (Exception e) {
                    //Nothing to do here
                }
            }
            if (messages.size() > 0) {
                return messages;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getMessagesByUser: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all messages for a group from database
     *
     * @param g Group
     * @return Boardlist
     * @throws DatabaseObjectNotFoundException Message does not exist
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized ArrayList<Message> getMessagesByGroup(Group g, CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Message> messages = new ArrayList<>();
            ArrayList<Integer> mIds = new ArrayList<>();
            ResultSet rs = dbcon.execute("SELECT id FROM 'Message' WHERE groupId='" + g.getID() + "' ORDER BY timestamp DESC;");
            while (rs.next()) {
                mIds.add(rs.getInt("id"));
            }
            rs.close();
            dbcon.free();
            for (Integer i : mIds) {
                try {
                    messages.add(getMessageById(i, cRMI));
                } catch (Exception e) {
                    //Nothing to do here
                }
            }
            if (messages.size() > 0) {
                return messages;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getMessagesByGroup: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all messages from database
     *
     * @return Boardlist
     * @throws DatabaseObjectNotFoundException Message does not exist
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized ArrayList<Message> getMessages(CachedFunctions cRMI) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Message> messages = new ArrayList<>();
            ArrayList<Integer> gIds = new ArrayList<>();
            ArrayList<Integer> aIds = new ArrayList<>();
            ResultSet rs = dbcon.execute("SELECT * FROM 'Message';");
            while (rs.next()) {
                messages.add(new Message(rs.getInt("id"), rs.getString("message"), null, null, rs.getLong("timestamp"), cRMI));
                gIds.add(rs.getInt("groupId"));
                aIds.add(rs.getInt("authorId"));
            }
            rs.close();
            dbcon.free();
            if (messages.size() > 0) {
                for (int i = 0; i < messages.size(); i++) {
                    if (gIds.get(i) != -1) {
                        try {
                            messages.get(i).setGroupId(gIds.get(i));
                        } catch (Exception e) {
                            //Nothing to do here
                        }
                    }
                    if (aIds.get(i) != -1) {
                        try {
                            messages.get(i).setAuthorId(aIds.get(i));
                        } catch (Exception e) {
                            //Nothing to do here
                        }
                    }
                }
                return messages;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (DatabaseObjectNotFoundException e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            Server.log.addErrorToLog("getMessages: " + e.toString());
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Save a message in the database
     *
     * @param message Message
     * @throws DatabaseObjectNotSavedException Message could not be safed
     * @throws IllegalArgumentException        Message null
     * @throws DatabaseConnectionException     Not connected to the database
     */
    public synchronized void saveMessage(String key, Message message, CachedFunctions cRMI) throws DatabaseObjectNotSavedException, IllegalArgumentException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (message != null) {
            try {
                Message m = null;
                UpdateType type = UpdateType.UNDEF;
                if (message.getID() == -1) {
                    ResultSet rs = dbcon.execute("INSERT INTO 'Message' (message, groupId, authorId, timestamp) VALUES ('" + escapeSQLString(message.getMessage()) + "','" + message.getGroupId() + "','" + message.getAuthorId() + "','" + message.getTimestamp() + "');");
                    if (rs != null) {
                        try {
                            m = getMessageById(rs.getInt(1), cRMI);
                            type = UpdateType.SAVE;
                        } catch (Exception e) {
                            //Nothing to do here
                        }
                    }
                } else {
                    dbcon.execute("UPDATE 'Message' SET message = '" + escapeSQLString(message.getMessage()) + "', authorId = '" + message.getAuthorId() + "', groupId = '" + message.getGroupId() + "', timestamp = '" + message.getTimestamp() + "' WHERE id = '" + message.getID() + "';");
                    m = message;
                    type = UpdateType.UPDATE;
                }
                m.setKey(key);
                server.notifyMessageUpdated(m, type);
            } catch (Exception e) {
                Server.log.addErrorToLog("saveMessage: " + e.toString());
                throw new DatabaseObjectNotSavedException();
            }
        } else {
            throw new IllegalArgumentException("Message null.");
        }
    }

    /**
     * Delete a user in the database
     *
     * @param message Message
     * @throws DatabaseObjectNotDeletedException Message could not be deleted
     * @throws IllegalArgumentException          Message null
     * @throws DatabaseConnectionException       Not connected to the database
     */
    public synchronized void deleteMessage(String key, Message message) throws IllegalArgumentException, DatabaseConnectionException, DatabaseObjectNotDeletedException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (message != null) {
            try {
                if (message.getID() == -1) {
                    throw new DatabaseObjectNotDeletedException();
                } else {
                    dbcon.execute("DELETE FROM 'Message' WHERE id = '" + message.getID() + "';");
                }
                message.setKey(key);
                server.notifyMessageUpdated(message, UpdateType.DELETE);
            } catch (Exception e) {
                Server.log.addErrorToLog("deleteMessage: " + e.toString());
                throw new DatabaseObjectNotDeletedException();
            }
        } else {
            throw new IllegalArgumentException("Message null.");
        }
    }

    /**
     * Escape special chars in string to avoid sql injection
     *
     * @param str String to escape
     * @return escaped string
     */
    private String escapeSQLString(String str) {
        str = str.replace("'", "''");
        return str;
    }

    /**
     * Checks username and password and returns the user, if login was succesfull
     *
     * @param username Username
     * @param password Password
     * @return User or null
     */
    public synchronized User loginUser(String username, String password) {
        try {
            User u = getUserByName(username);
            if (u.checkPassword(password)) {
                return u;
            }
        } catch (Exception e) {
            Server.log.addErrorToLog("loginUser: " + e.toString());
        }
        return null;
    }
}
