package com.gymlog.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.gymlog.database.GymLogDatabase;

import java.util.Objects;

/**
 * Author: Rahim Siddiq
 * GymLog
 * 08/04/2024
 * User.java represents a user entity in the GymLog application.
 */
@Entity(tableName = GymLogDatabase.USER_TABLE)
public class User {

//  The unique identifier for the User.
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;
    private String password;
    private boolean isAdmin;

    /**
     * Constructs a new User with the specified username and password.
     * By default, the user is not an admin.
     * @param username the username of the user
     * @param password the password for the user account
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        isAdmin = false;
    }

    /**
     * Compares this User to the specified object.
     * @param o the object to compare this User against
     * @return true if the given object represents a User equivalent to this User, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && isAdmin == user.isAdmin && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    /**
     * Returns a hash code value for the User. This method is supported for the benefit of
     * hash tables such as those provided by HashMap.
     * @return a hash code value for this User
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isAdmin);
    }

    /**
     * Returns the ID of the User.
     * @return the ID of the User
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the User.
     * @param id the ID to set for the User
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the username of the User.
     * @return the username of the User
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the User.
     * @param username the username to set for the User
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the User.
     * @return the password of the User
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the User.
     * @param password the password to set for the User
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns whether the User is an admin.
     * @return true if the User is an admin, false otherwise
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets the admin status of the User.
     * @param admin the admin status to set for the User
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}