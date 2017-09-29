package main.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dominik on 18.08.2017.
 */
public class Group implements Serializable {

    private int id;
    private String name;
    private User moderator;
    private ArrayList<User> members;

    /**
     * Create group (with given ID -> Update group in main.database)
     *
     * @param id        ID
     * @param name      Groupname
     * @param moderator Moderator
     * @param members   Members
     */
    public Group(int id, String name, User moderator, ArrayList<User> members) {
        setID(id);
        setName(name);
        setModerator(moderator);
        setMembers(members);
    }

    /**
     * Create group without members (with ID -1 -> Save as new group in main.database)
     *
     * @param name      Groupname
     * @param moderator Moderator
     */
    public Group(String name, User moderator) {
        this(-1, name, moderator, null);
    }

    /**
     * Create group (with ID -1 -> Save as new group in main.database)
     *
     * @param name      Groupname
     * @param moderator Moderator
     * @param members   Members
     */
    public Group(String name, User moderator, ArrayList<User> members) {
        this(-1, name, moderator, members);
    }

    /**
     * Get group id
     *
     * @return id
     */
    public int getID() {
        return id;
    }

    /**
     * Set group id
     *
     * @param id Positive or -1
     */
    private void setID(int id) {
        this.id = id;
    }

    /**
     * Get group name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set group name
     *
     * @param name Groupname
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get group moderator
     *
     * @return moderator
     */
    public User getModerator() {
        return moderator;
    }

    /**
     * Set group moderator
     *
     * @param moderator Moderator
     */
    public void setModerator(User moderator) {
        if (moderator != null) {
            if (moderator.getID() != -1) {
                this.moderator = moderator;
            } else {
                throw new IllegalArgumentException("Save user first in database!");
            }
        } else {
            this.moderator = null;
        }
    }

    /**
     * Get group members
     *
     * @return members
     */
    public ArrayList<User> getMembers() {
        return members;
    }

    /**
     * Set group members
     *
     * @param members Members
     */
    public void setMembers(ArrayList<User> members) {
        if (members != null) {
            for (User m : members) {
                try {
                    addMember(m);
                } catch (Exception e) {
                    System.err.println("Could not add " + m.getName() + " to group member list.");
                }
            }
        }
    }

    /**
     * Add a member to the group
     *
     * @param member Member
     */
    public void addMember(User member) throws IllegalArgumentException {
        if (member != null) {
            if (member.getID() != -1) {
                try {
                    if (this.members == null) {
                        this.members = new ArrayList<>();
                    }
                    if (this.members.contains(member)) {
                        this.members.remove(member);
                    }
                    this.members.add(member);
                } catch (Exception e) {
                    //Nothing to do here.
                }
            } else {
                throw new IllegalArgumentException("Save user first in database!");
            }
        } else {
            throw new IllegalArgumentException("User null.");
        }
    }

    /**
     * Remove a member from the group
     *
     * @param member Member
     */
    public void removeMember(User member) {
        try {
            if (this.members != null && this.members.size() > 0 && member != null && member.getID() != -1) {
                this.members.remove(member);
            }
        } catch (Exception e) {
            //Nothing to do here.
        }
    }

    /**
     * To String
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", moderator=" + moderator +
                ", members=" + members +
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

        Group group = (Group) o;

        return id == group.id;
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
