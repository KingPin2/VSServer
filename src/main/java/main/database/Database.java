package main.database;

import main.database.exceptions.*;
import main.objects.Board;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * @author D.Bergum
 * Manage object to database and database to object
 */
public class Database {

    DBConnection dbcon = null;

    /**
     * Connect to database
     *
     * @throws DatabaseConnectionException
     */
    public Database() throws DatabaseConnectionException {
        dbcon = new DBConnection();
        this.openDB();
    }

    /**
     * Connect to database
     */
    public synchronized void openDB() {
        try {
            if (!dbcon.isOpen()) {
                dbcon.openDB();
                initDB();
            }
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close connection to database
     */
    public synchronized void closeDB() {
        try {
            dbcon.closeDB();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
        }
    }

    private boolean testDatabase() {
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'User';");
            rs.close();
            dbcon.free();
            rs = dbcon.execute("SELECT * FROM 'Group';");
            rs.close();
            dbcon.free();
            rs = dbcon.execute("SELECT * FROM 'Board';");
            rs.close();
            dbcon.free();
            rs = dbcon.execute("SELECT * FROM 'Message';");
            rs.close();
            dbcon.free();
            rs = dbcon.execute("SELECT * FROM 'Group_User';");
            rs.close();
            dbcon.free();
        } catch (Exception e) {
            System.out.println("Database test FAILED.");
            return false;
        }
        System.out.println("Database test SUCCEEDED.");
        return true;
    }

    public synchronized void initDB() {
        try {
            if (!testDatabase()) {
                System.out.println("Initialize database.");

                try {
                    dbcon.execute("DROP TABLE `Board`;");
                } catch (Exception e) {
                }
                try {
                    dbcon.execute("DROP TABLE `Group`;");
                } catch (Exception e) {
                }
                try {
                    dbcon.execute("DROP TABLE `Group_User`;");
                } catch (Exception e) {
                }
                try {
                    dbcon.execute("DROP TABLE `Message`;");
                } catch (Exception e) {
                }
                try {
                    dbcon.execute("DROP TABLE `User`;");
                } catch (Exception e) {
                }

                dbcon.execute("CREATE TABLE IF NOT EXISTS `Board` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL UNIQUE, `groupId` INTEGER NOT NULL, `userId` INTEGER NOT NULL );");
                dbcon.execute("CREATE TABLE IF NOT EXISTS `Group` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL UNIQUE, `modId` INTEGER NOT NULL );");
                dbcon.execute("CREATE TABLE IF NOT EXISTS `Group_User` ( `groupId` INTEGER NOT NULL, `userId` INTEGER NOT NULL );");
                dbcon.execute("CREATE TABLE IF NOT EXISTS `Message` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `message` TEXT NOT NULL, `authorId` INTEGER NOT NULL, `groupId` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL );");
                dbcon.execute("CREATE TABLE IF NOT EXISTS `User` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL UNIQUE, `password` TEXT NOT NULL, `level` INTEGER NOT NULL );");
                saveUser(ObjectFactory.createUser("Admin", "admin", 2));
                System.out.println("-----------------");
                System.out.println("Default User:");
                System.out.println("Username: Admin");
                System.out.println("Passwort: admin");
                System.out.println("-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a user from database by id
     *
     * @param id UserId
     * @return User
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
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
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get a user from database by username
     *
     * @param username User Name
     * @return User
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
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
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all users from database
     *
     * @return Userlist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized ArrayList<User> getUsers() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<User> users = new ArrayList<User>();
            ResultSet rs = dbcon.execute("SELECT * FROM 'User';");
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"), rs.getInt("level")));
            }
            rs.close();
            dbcon.free();
            if (users.size() > 0) {
                return users;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get a user by level from database
     *
     * @param level UserLevel
     * @return User
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized ArrayList<User> getUsersByLevel(int level) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<User> users = new ArrayList<User>();
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
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Save a user in the database
     *
     * @param user User
     * @throws DatabaseObjectNotSavedException
     * @throws IllegalArgumentException
     * @throws DatabaseConnectionException
     */
    public synchronized void saveUser(User user) throws DatabaseObjectNotSavedException, IllegalArgumentException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (user != null) {
            try {
                if (user.getID() == -1) {
                    dbcon.execute("INSERT INTO 'User' (name, password, level) VALUES ('" + escapeSQLString(user.getName()) + "','" + escapeSQLString(user.getPassword()) + "','" + user.getLevel() + "');");
                } else {
                    dbcon.execute("UPDATE 'User' SET name = '" + escapeSQLString(user.getName()) + "', password = '" + escapeSQLString(user.getPassword()) + "', level = '" + user.getLevel() + "' WHERE id = '" + user.getID() + "';");
                }
            } catch (Exception e) {
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
     * @throws DatabaseObjectNotSavedException
     * @throws IllegalArgumentException
     * @throws DatabaseConnectionException
     */
    public synchronized void deleteUser(User user) throws IllegalArgumentException, DatabaseConnectionException, DatabaseObjectNotDeletedException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (user != null) {
            try {
                if (user.getID() == -1) {
                    throw new DatabaseObjectNotDeletedException();
                } else {
                    dbcon.execute("DELETE FROM 'User' WHERE id = '" + user.getID() + "';");
                }
            } catch (Exception e) {
                throw new DatabaseObjectNotDeletedException();
            }
        } else {
            throw new IllegalArgumentException("User null.");
        }
    }

    /**
     * Get a board from database by id
     *
     * @param id BoardId
     * @return Board
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized Board getBoardById(int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'Board' WHERE id = '" + id + "';");
            if (rs.next()) {
                int gId = rs.getInt("groupId");
                int uId = rs.getInt("userId");
                Board b = new Board(rs.getInt("id"), rs.getString("name"), null, null);
                rs.close();
                dbcon.free();
                if (gId != -1) {
                    b.setGroup(getGroupById(gId));
                }
                if (uId != -1) {
                    b.setUser(getUserById(uId));
                }
                return b;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all boards from database
     *
     * @return Boardlist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized ArrayList<Board> getBoards() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Board> boards = new ArrayList<Board>();
            ArrayList<Integer> gIds = new ArrayList<Integer>();
            ArrayList<Integer> uIds = new ArrayList<Integer>();
            ResultSet rs = dbcon.execute("SELECT * FROM 'Board';");
            while (rs.next()) {
                boards.add(new Board(rs.getInt("id"), rs.getString("name"), null, null));
                gIds.add(rs.getInt("groupId"));
                uIds.add(rs.getInt("userId"));
            }
            rs.close();
            dbcon.free();
            if (boards.size() > 0) {
                for (int i = 0; i < boards.size(); i++) {
                    if (gIds.get(i) != -1) {
                        boards.get(i).setGroup(getGroupById(gIds.get(i)));
                    }
                    if (uIds.get(i) != -1) {
                        boards.get(i).setUser(getUserById(uIds.get(i)));
                    }
                }
                return boards;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Save a board in the database
     *
     * @param board Board
     * @throws DatabaseObjectNotSavedException
     * @throws IllegalArgumentException
     * @throws DatabaseConnectionException
     */
    public synchronized void saveBoard(Board board) throws DatabaseObjectNotSavedException, IllegalArgumentException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (board != null) {
            try {
                int uId = -1;
                if (board.getUser() != null) {
                    uId = board.getUser().getID();
                }
                int gId = -1;
                if (board.getGroup() != null) {
                    gId = board.getGroup().getID();
                }
                if (board.getID() == -1) {
                    dbcon.execute("INSERT INTO 'Board' (name, groupId, userId) VALUES ('" + escapeSQLString(board.getName()) + "','" + gId + "','" + uId + "');");
                } else {
                    dbcon.execute("UPDATE 'Board' SET name = '" + escapeSQLString(board.getName()) + "', userId = '" + uId + "', groupId = '" + gId + "' WHERE id = '" + board.getID() + "';");
                }
            } catch (Exception e) {
                throw new DatabaseObjectNotSavedException();
            }
        } else {
            throw new IllegalArgumentException("Board null.");
        }
    }

    /**
     * Delete a board in the database
     *
     * @param board Board
     * @throws DatabaseObjectNotSavedException
     * @throws IllegalArgumentException
     * @throws DatabaseConnectionException
     */
    public synchronized void deleteBoard(Board board) throws IllegalArgumentException, DatabaseConnectionException, DatabaseObjectNotDeletedException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (board != null) {
            try {
                if (board.getID() == -1) {
                    throw new DatabaseObjectNotDeletedException();
                } else {
                    dbcon.execute("DELETE FROM 'Board' WHERE id = '" + board.getID() + "';");
                }
            } catch (Exception e) {
                throw new DatabaseObjectNotDeletedException();
            }
        } else {
            throw new IllegalArgumentException("Board null.");
        }
    }

    /**
     * Get a group from database by id
     *
     * @param id GroupId
     * @return Group
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
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
                    g.setModerator(getUserById(mId));
                }
                g.setMembers(getGroupMembers(g.getID()));
                return g;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get a group from database by name
     *
     * @param name Groupname
     * @return Group
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
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
                    g.setModerator(getUserById(mId));
                }
                g.setMembers(getGroupMembers(g.getID()));
                return g;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all groups for one user from database
     *
     * @return Grouplist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized ArrayList<Group> getGroupsByUser(User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Group> groups = new ArrayList<Group>();
            ArrayList<Integer> gIDs = new ArrayList<Integer>();
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
                groups.add(getGroupById(i));
            }
            if (groups.size() > 0) {
                return groups;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all groups for one moderator from database
     *
     * @return Grouplist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized ArrayList<Group> getGroupsByModerator(User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Group> groups = new ArrayList<Group>();
            ArrayList<Integer> gIDs = new ArrayList<Integer>();
            ResultSet rs = dbcon.execute("SELECT id FROM 'Group' WHERE modId = '" + u.getID() + "';");
            while (rs.next()) {
                gIDs.add(rs.getInt("id"));
            }
            rs.close();
            dbcon.free();
            for (Integer i : gIDs) {
                groups.add(getGroupById(i));
            }
            if (groups.size() > 0) {
                return groups;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all groups from database
     *
     * @return Grouplist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized ArrayList<Group> getGroups() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Group> groups = new ArrayList<Group>();
            ArrayList<Integer> mIds = new ArrayList<Integer>();
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
                        groups.get(i).setModerator(getUserById(mIds.get(i)));
                    }
                    groups.get(i).setMembers(getGroupMembers(groups.get(i).getID()));
                }
                return groups;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Save a group in the database
     *
     * @param group Group
     * @throws DatabaseObjectNotSavedException
     * @throws IllegalArgumentException
     * @throws DatabaseConnectionException
     */
    public synchronized void saveGroup(Group group) throws DatabaseObjectNotSavedException, IllegalArgumentException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (group != null) {
            try {
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
                } else {
                    dbcon.execute("UPDATE 'Group' SET name = '" + escapeSQLString(group.getName()) + "', modId = '" + group.getModerator().getID() + "' WHERE id = '" + group.getID() + "';");
                    groupId = group.getID();
                    deleteGroupMembers(group.getID());
                }
                if (groupId != -1) {
                    saveGroupMembers(groupId, group.getMembers());
                }
            } catch (Exception e) {
                e.printStackTrace();
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
     * @throws DatabaseObjectNotSavedException
     * @throws IllegalArgumentException
     * @throws DatabaseConnectionException
     */
    public synchronized void deleteGroup(Group group) throws IllegalArgumentException, DatabaseConnectionException, DatabaseObjectNotDeletedException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (group != null) {
            try {
                if (group.getID() == -1) {
                    throw new DatabaseObjectNotDeletedException();
                } else {
                    dbcon.execute("DELETE FROM 'Group' WHERE id = '" + group.getID() + "';");
                    deleteGroupMembers(group.getID());
                }
            } catch (Exception e) {
                throw new DatabaseObjectNotDeletedException();
            }
        } else {
            throw new IllegalArgumentException("Group null.");
        }
    }

    /**
     * Get all group members
     *
     * @param groupId
     * @return groupMembers
     * @throws DatabaseConnectionException
     * @throws DatabaseObjectNotFoundException
     */
    private synchronized ArrayList<User> getGroupMembers(int groupId) throws DatabaseConnectionException, DatabaseObjectNotFoundException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Integer> uIds = new ArrayList<Integer>();
            ResultSet rs = dbcon.execute("SELECT * FROM 'Group_User' WHERE groupId = '" + groupId + "';");
            while (rs.next()) {
                uIds.add(rs.getInt("userId"));
            }
            rs.close();
            dbcon.free();
            if (uIds.size() > 0) {
                ArrayList<User> members = new ArrayList<User>();

                for (int i : uIds) {
                    User u = getUserById(i);
                    if (members.contains(u)) {
                        members.remove(u);
                    }
                    members.add(u);
                }

                return members;
            }
            return null;
        } catch (Exception e) {
            dbcon.free();
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
            ArrayList<User> users = getUsers();
            ArrayList<User> notInGroup = new ArrayList<User>();
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
            return null;
        }
    }

    /**
     * Delete all group members
     *
     * @param groupId
     * @throws DatabaseConnectionException
     */
    private synchronized void deleteGroupMembers(int groupId) throws DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            dbcon.execute("DELETE FROM 'Group_User' WHERE groupId = '" + groupId + "';");
        } catch (Exception e) {
            dbcon.free();
        }
    }

    /**
     * Save all group members
     *
     * @param groupId
     * @param groupMembers
     * @throws DatabaseConnectionException
     * @throws DatabaseObjectNotSavedException
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
                throw new DatabaseObjectNotSavedException();
            }
        }
    }

    /**
     * Get a message from database by id
     *
     * @param id MessageId
     * @return Message
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized Message getMessageById(int id) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ResultSet rs = dbcon.execute("SELECT * FROM 'Message' WHERE id = '" + id + "';");
            if (rs.next()) {
                int gId = rs.getInt("groupId");
                int aId = rs.getInt("authorId");
                Message m = new Message(rs.getInt("id"), rs.getString("message"), null, null, rs.getLong("timestamp"));
                rs.close();
                dbcon.free();
                if (gId != -1) {
                    m.setGroup(getGroupById(gId));
                }
                if (aId != -1) {
                    m.setAuthor(getUserById(aId));
                }
                return m;
            } else {
                rs.close();
                throw new DatabaseObjectNotFoundException();
            }
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all messages for a user from database
     *
     * @param u User
     * @return Boardlist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized ArrayList<Message> getMessagesByUser(User u) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Message> messages = new ArrayList<Message>();
            ArrayList<Integer> mIds = new ArrayList<Integer>();
            ResultSet rs = dbcon.execute("SELECT id FROM 'Message' WHERE authorId='" + u.getID() + "' ORDER BY timestamp DESC;");
            while (rs.next()) {
                mIds.add(rs.getInt("id"));
            }
            rs.close();
            dbcon.free();
            for (Integer i : mIds) {
                messages.add(getMessageById(i));
            }
            if (messages.size() > 0) {
                return messages;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all messages for a group from database
     *
     * @param g Group
     * @return Boardlist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized ArrayList<Message> getMessagesByGroup(Group g) throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Message> messages = new ArrayList<Message>();
            ArrayList<Integer> mIds = new ArrayList<Integer>();
            ResultSet rs = dbcon.execute("SELECT id FROM 'Message' WHERE groupId='" + g.getID() + "' ORDER BY timestamp DESC;");
            while (rs.next()) {
                mIds.add(rs.getInt("id"));
            }
            rs.close();
            dbcon.free();
            for (Integer i : mIds) {
                messages.add(getMessageById(i));
            }
            if (messages.size() > 0) {
                return messages;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Get all messages from database
     *
     * @return Boardlist
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseConnectionException
     */
    public synchronized ArrayList<Message> getMessages() throws DatabaseObjectNotFoundException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        try {
            ArrayList<Message> messages = new ArrayList<Message>();
            ArrayList<Integer> gIds = new ArrayList<Integer>();
            ArrayList<Integer> aIds = new ArrayList<Integer>();
            ResultSet rs = dbcon.execute("SELECT * FROM 'Message';");
            while (rs.next()) {
                messages.add(new Message(rs.getInt("id"), rs.getString("message"), null, null, rs.getLong("timestamp")));
                gIds.add(rs.getInt("groupId"));
                aIds.add(rs.getInt("authorId"));
            }
            rs.close();
            dbcon.free();
            if (messages.size() > 0) {
                for (int i = 0; i < messages.size(); i++) {
                    if (gIds.get(i) != -1) {
                        messages.get(i).setGroup(getGroupById(gIds.get(i)));
                    }
                    if (aIds.get(i) != -1) {
                        messages.get(i).setAuthor(getUserById(aIds.get(i)));
                    }
                }
                return messages;
            }
            throw new DatabaseObjectNotFoundException();
        } catch (Exception e) {
            dbcon.free();
            throw new DatabaseObjectNotFoundException();
        }
    }

    /**
     * Save a message in the database
     *
     * @param message Message
     * @throws DatabaseObjectNotSavedException
     * @throws IllegalArgumentException
     * @throws DatabaseConnectionException
     */
    public synchronized void saveMessage(Message message) throws DatabaseObjectNotSavedException, IllegalArgumentException, DatabaseConnectionException {
        if (!dbcon.isOpen()) {
            throw new DatabaseConnectionException("Not connected to database.");
        }
        if (message != null) {
            try {
                int aId = -1;
                if (message.getAuthor() != null) {
                    aId = message.getAuthor().getID();
                }
                int gId = -1;
                if (message.getGroup() != null) {
                    gId = message.getGroup().getID();
                }
                if (message.getID() == -1) {
                    dbcon.execute("INSERT INTO 'Message' (message, groupId, authorId, timestamp) VALUES ('" + escapeSQLString(message.getMessage()) + "','" + gId + "','" + aId + "','" + message.getTimestamp() + "');");
                } else {
                    dbcon.execute("UPDATE 'Message' SET message = '" + escapeSQLString(message.getMessage()) + "', authorId = '" + aId + "', groupId = '" + gId + "', timestamp = '" + message.getTimestamp() + "' WHERE id = '" + message.getID() + "';");
                }
            } catch (Exception e) {
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
     * @throws DatabaseObjectNotSavedException
     * @throws IllegalArgumentException
     * @throws DatabaseConnectionException
     */
    public synchronized void deleteMessage(Message message) throws IllegalArgumentException, DatabaseConnectionException, DatabaseObjectNotDeletedException {
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
            } catch (Exception e) {
                throw new DatabaseObjectNotDeletedException();
            }
        } else {
            throw new IllegalArgumentException("Message null.");
        }
    }

    /**
     * Escape special chars in string to avoid sql injection
     *
     * @param str
     * @return escaped string
     */
    private String escapeSQLString(String str) {
        str = str.replace("'", "''");
        return str;
    }

    /**
     * Checks username and password and returns the user, if login was succesfull
     *
     * @param username
     * @param password
     * @return User or null
     */
    public synchronized User loginUser(String username, String password) {
        try {
            User u = getUserByName(username);
            if (u.checkPassword(password)) {
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
