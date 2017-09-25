package main.objects;

import java.io.Serializable;

/**
 * @author D.Bergum
 * A user object as saved in main.database
 */
public class User implements Serializable {

    private int id;
    private String name;
    private String password;
    private int level;

    /**
     * Create User (with ID -1 -> Save as new user in main.database)
     * @param name
     * @param password
     * @param level
     * @throws IllegalArgumentException
     */
    public User(String name, String password, int level) throws IllegalArgumentException{
        this(-1, name, password, level);
    }

    /**
     * Create User (with given ID -> Update user in main.database)
     * @param id
     * @param name
     * @param password
     * @param level
     * @throws IllegalArgumentException
     */
    public User(int id, String name, String password, int level) throws IllegalArgumentException{
        setID(id);
        setName(name);
        setPassword(password);
        setLevel(level);
    }

    /**
     * Get user id
     * @return id
     */
    public int getID(){
        return this.id;
    }

    /**
     * Get user name
     * @return name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get user password
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Checks if given password equals saved password
     * @param password password
     * @return password equals
     */
    public boolean checkPassword(String password){
        return this.password.equalsIgnoreCase(password);
    }

    /**
     * Get user level
     * @return level
     */
    public int getLevel(){
        return this.level;
    }

    /**
     * Set user id
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
     * Set user name
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
     * Set user password
     * @param password Not null or empty
     * @throws IllegalArgumentException
     */
    public void setPassword(String password) throws IllegalArgumentException {
        if (password == null || password.isEmpty()){
            throw new IllegalArgumentException("Password null or empty.");
        }
        this.password = password;
    }

    /**
     * Set user level
     * @param level Positive
     * @throws IllegalArgumentException
     */
    public void setLevel(int level) throws IllegalArgumentException {
        if (level < 0){
            throw new IllegalArgumentException("Level negative.");
        }
        this.level = level;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='********'" +
                ", level=" + level +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
