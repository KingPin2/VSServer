package main.database;

import main.objects.Board;
import main.objects.Message;
import main.objects.User;
import main.objects.Group;

import java.util.ArrayList;

/**
 * Created by Dominik on 18.08.2017.
 */
public class ObjectFactory {

    /**
     * Create user
     * @param name
     * @param password
     * @param level
     * @return user
     */
    public static User createUser(String name, String password, int level){
        return new User(name, password, level);
    }

    /**
     * Create group board
     * @param name
     * @param group
     * @return board
     */
    public static Board createGroupBoard(String name, Group group){
        return new Board(name, group);
    }

    /**
     * Create message board
     * @param name
     * @param user
     * @return board
     */
    public static Board createUserBoard(String name, User user){
        return new Board(name, user);
    }

    /**
     * Create message
     * @param message
     * @param author
     * @return message
     */
    public static Message createMessage(String message, User author){
        return new Message(message, author);
    }

    /**
     * Create group message
     * @param message
     * @param author
     * @param group
     * @return message
     */
    public static Message createGroupMessage(String message, User author, Group group){
        return new Message(message, author, group);
    }

    /**
     * Create empty group
     * @param name
     * @param moderator
     * @return group
     */
    public static Group createEmptyGroup(String name, User moderator){
        return new Group(name, moderator);
    }

    /**
     * Create group with members
     * @param name
     * @param moderator
     * @param members
     * @return group
     */
    public static Group createGroupWithMembers(String name, User moderator, ArrayList<User> members){
        return new Group(name, moderator, members);
    }
}
