package main.objects;

import java.io.Serializable;

/**
 * Created by Dominik on 18.08.2017.
 */
public class Board implements Serializable{

    private int id;
    private String name;
    private Group group;
    private User user;

    /**
     * Create board (with given ID -> Update board in main.database)
     * @param id
     * @param name
     * @param group
     * @param user
     */
    public Board(int id, String name, Group group, User user) {
        setID(id);
        setName(name);
        setUser(user);
        setGroup(group);
    }

    /**
     * Create group board (with ID -1 -> Save as new board in main.database)
     * @param name
     * @param group
     */
    public Board(String name, Group group){
        this(-1,name,group,null);
    }

    /**
     * Create user board (with ID -1 -> Save as new board in main.database)
     * @param name
     * @param user
     */
    public Board(String name, User user){
        this(-1,name,null,user);
    }

    /**
     * Get user id
     * @return id
     */
    public int getID(){
        return this.id;
    }

    /**
     * Set board id
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
     * Get board name
     * @return name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Set board name
     * @param name Not null or empty
     * @throws IllegalArgumentException
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name null or empty.");
        }
        this.name = name;
    }

    /**
     * Get board user
     * @return user
     */
    public User getUser(){
        return this.user;
    }

    /**
     * Set board user
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Get board group
     * @return group
     */
    public Group getGroup(){
        return this.group;
    }

    /**
     * Set board group
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group=" + group +
                ", user=" + user +
                '}';
    }
}
