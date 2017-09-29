package main.database;

import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.util.ArrayList;

/**
 * Object factory for database objects
 *
 * @author Dominik Bergum, 3603490
 */
public class ObjectFactory {

    /**
     * Create user
     *
     * @param name     Name
     * @param password Password
     * @param level    Level
     * @return user
     */
    public static User createUser(String name, String password, int level) {
        return new User(name, password, level);
    }

    /**
     * Create message
     *
     * @param message Message
     * @param author  Author
     * @return message
     */
    public static Message createMessage(String message, User author) {
        return new Message(message, author);
    }

    /**
     * Create group message
     *
     * @param message Message
     * @param author  Author
     * @param group   Group
     * @return message
     */
    public static Message createGroupMessage(String message, User author, Group group) {
        return new Message(message, author, group);
    }

    /**
     * Create empty group
     *
     * @param name      Name
     * @param moderator Moderator
     * @return group
     */
    public static Group createEmptyGroup(String name, User moderator) {
        return new Group(name, moderator);
    }

    /**
     * Create group with members
     *
     * @param name      Name
     * @param moderator Moderator
     * @param members   Members
     * @return group
     */
    public static Group createGroupWithMembers(String name, User moderator, ArrayList<User> members) {
        return new Group(name, moderator, members);
    }
}
