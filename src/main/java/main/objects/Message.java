package main.objects;

import main.rmiinterface.Functions;

import java.io.Serializable;

/**
 * Created by Dominik on 18.08.2017.
 */
public class Message implements Serializable {

    private int id;
    private String message;
    private int authorId;
    private int groupId;
    private String key;
    private Group group;
    private User author;
    private long timestamp;
    private Functions rmi;

    /**
     * Create message (with given ID -> Update message in main.database)
     *
     * @param id
     * @param message
     * @param group
     * @param author
     */
    public Message(int id, String message, Group group, User author, Long timestamp, Functions rmi) {
        setID(id);
        setAuthor(author);
        setGroup(group);
        setMessage(message);
        this.timestamp = timestamp;
        this.rmi = rmi;
        this.group = null;
        this.author = null;
        this.key = null;
    }


    /**
     * Create  message (with ID -1 -> Save as new message in main.database)
     *
     * @param message
     * @param author
     */
    public Message(String message, User author) {
        this(-1, message, null, author, System.currentTimeMillis(), null);
    }

    /**
     * Create  group message (with ID -1 -> Save as new message in main.database)
     *
     * @param message
     * @param author
     * @param group
     */
    public Message(String message, User author, Group group) {
        this(-1, message, group, author, System.currentTimeMillis(), null);
    }

    /**
     * Get author id
     *
     * @return id
     */
    public int getID() {
        return this.id;
    }

    /**
     * Set Message id
     *
     * @param id Positive or -1
     * @throws IllegalArgumentException
     */
    private void setID(int id) throws IllegalArgumentException {
        if (id < -1) {
            throw new IllegalArgumentException("ID negative.");
        }
        this.id = id;
    }

    /**
     * Get Message message
     *
     * @return name
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Set Message message
     *
     * @param message Not null or empty
     * @throws IllegalArgumentException
     */
    public void setMessage(String message) throws IllegalArgumentException {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message null or empty.");
        }
        timestamp = System.currentTimeMillis();
        this.message = message;
    }

    /**
     * Get Message author
     *
     * @return author
     */
    public User getAuthor() {
        try {
            if (this.authorId != -1) {
                if (author == null) {
                    this.author = rmi.getUserById(key, this.authorId);
                }
                return this.author;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Set Message author
     */
    public void setAuthor(User author) {
        if (author != null) {
            if (author.getID() != -1) {
                timestamp = System.currentTimeMillis();
                this.authorId = author.getID();
            } else {
                throw new IllegalArgumentException("Save user first in database!");
            }
        } else {
            this.authorId = -1;
        }
    }

    /**
     * Get Message group
     *
     * @return group
     */
    public Group getGroup() {
        try {
            if (this.groupId != -1) {
                if (group == null) {
                    this.group = rmi.getGroupById(key, this.groupId);
                }
                return this.group;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Set Message group
     */
    public void setGroup(Group group) {
        if (group != null) {
            if (group.getID() != -1) {
                timestamp = System.currentTimeMillis();
                this.groupId = group.getID();
            } else {
                throw new IllegalArgumentException("Save group first in database!");
            }
        } else {
            this.groupId = -1;
        }
    }

    /**
     * Get Message timestamp
     *
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void reset(){
        this.author = null;
        this.group = null;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", authorId=" + authorId +
                ", groupId=" + groupId +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return id == message.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
