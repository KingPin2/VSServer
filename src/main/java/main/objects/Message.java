package main.objects;

import java.io.Serializable;

/**
 * Created by Dominik on 18.08.2017.
 */
public class Message implements Serializable{

    private int id;
    private String message;
    private User author;
    private Group group;
    private long timestamp;

    /**
     * Create message (with given ID -> Update message in main.database)
     * @param id
     * @param message
     * @param group
     * @param author
     */
    public Message(int id, String message, Group group, User author, Long timestamp) {
        setID(id);
        setMessage(message);
        setAuthor(author);
        setGroup(group);
        this.timestamp = timestamp;
    }


    /**
     * Create  message (with ID -1 -> Save as new message in main.database)
     * @param message
     * @param author
     */
    public Message(String message, User author){
        this(-1, message,null, author, System.currentTimeMillis());
    }

    /**
     * Create  group message (with ID -1 -> Save as new message in main.database)
     * @param message
     * @param author
     * @param group
     */
    public Message(String message, User author, Group group){
        this(-1, message, group, author, System.currentTimeMillis());
    }

    /**
     * Get author id
     * @return id
     */
    public int getID(){
        return this.id;
    }

    /**
     * Set Message id
     * @param id Positive or -1
     * @throws IllegalArgumentException
     */
    private void setID(int id) throws IllegalArgumentException {
        if (id < -1){
            throw new IllegalArgumentException("ID negative.");
        }
        this.id = id;
    }

    /**
     * Get Message message
     * @return name
     */
    public String getMessage(){
        return this.message;
    }

    /**
     * Set Message message
     * @param message Not null or empty
     * @throws IllegalArgumentException
     */
    public void setMessage(String message) throws IllegalArgumentException {
        if (message == null || message.isEmpty()){
            throw new IllegalArgumentException("Message null or empty.");
        }
        timestamp = System.currentTimeMillis();
        this.message = message;
    }

    /**
     * Get Message author
     * @return author
     */
    public User getAuthor(){
        return this.author;
    }

    /**
     * Set Message author
     */
    public void setAuthor(User author){
        if (author.getID() != -1) {
            timestamp = System.currentTimeMillis();
            this.author = author;
        } else {
            throw new IllegalArgumentException("Save user first in database!");
        }
    }

    /**
     * Get Message group
     * @return group
     */
    public Group getGroup(){
        return this.group;
    }

    /**
     * Set Message group
     */
    public void setGroup(Group group) {
        if (group.getID() != -1) {
        timestamp = System.currentTimeMillis();
        this.group = group;
        } else {
            throw new IllegalArgumentException("Save group first in database!");
        }
    }

    /**
     * Get Message timestamp
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", author=" + author +
                ", group=" + group +
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
