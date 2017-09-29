package main.objects;

import main.rmiinterface.CachedFunctions;

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
    private CachedFunctions rmi;

    /**
     * Create message (with given ID -> Update message in main.database)
     *
     * @param id      Id
     * @param message Message
     * @param group   Group
     * @param author  Author
     */
    public Message(int id, String message, Group group, User author, Long timestamp, CachedFunctions rmi) {
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
     * @param message Message
     * @param author  Author
     */
    public Message(String message, User author) {
        this(-1, message, null, author, System.currentTimeMillis(), null);
    }

    /**
     * Create  group message (with ID -1 -> Save as new message in main.database)
     *
     * @param message Message
     * @param author  Author
     * @param group   Group
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
     * @throws IllegalArgumentException ID negative
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
     * @throws IllegalArgumentException Message null or empty
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
                    this.author = rmi.getSimpleUserById(key, this.authorId);
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
     *
     * @param author Author
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
     *
     * @param group Group
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
     * @return Timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Get authorId
     *
     * @return authorId
     */
    public int getAuthorId() {
        return authorId;
    }

    /**
     * Get groupId
     *
     * @return groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * Set groupId
     *
     * @param groupId groupId
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * Set AuthorId
     *
     * @param authorId authorId
     */
    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    /**
     * Set key
     *
     * @param key Key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Reset local cache
     */
    public void reset() {
        this.author = null;
        this.group = null;
    }


    /**
     * To String
     *
     * @return String
     */
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

    /**
     * Equals
     *
     * @param o Object
     * @return This equals o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return id == message.id;
    }

    /**
     * Hashcode
     *
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return id;
    }
}
