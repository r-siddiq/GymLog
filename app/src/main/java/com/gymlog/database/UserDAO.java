package com.gymlog.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.gymlog.database.entities.User;

/**
 * Author: Rahim Siddiq
 * GymLog
 * 08/04/2024
 * Data Access Object (DAO) for the User entity.
 */
@Dao
public interface UserDAO {

    /**
     * Inserts one or more User records into the database.
     * If a conflict occurs, the existing record is replaced with the new one.
     * @param user the User records to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    /**
     * Deletes a User record from the database.
     * @param user the User record to delete
     */
    @Delete
    void delete(User user);

    /**
     * Retrieves all User records from the database ordered by username.
     * @return a LiveData list of all User records
     */
    @Query("SELECT * FROM " + GymLogDatabase.USER_TABLE + " ORDER BY username")
    LiveData<User> getAllUsers();

    /**
     * Deletes all User records from the database.
     */
    @Query("DELETE FROM " + GymLogDatabase.USER_TABLE)
    void deleteAll();

    /**
     * Retrieves a User record by username.
     * @param username the username of the User
     * @return a LiveData object containing the User record
     */
    @Query("SELECT * FROM " + GymLogDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);

    /**
     * Retrieves a User record by user ID.
     * @param userId the ID of the User
     * @return a LiveData object containing the User record
     */
    @Query("SELECT * FROM " + GymLogDatabase.USER_TABLE + " WHERE id == :userId")
    LiveData<User> getUserByUserId(int userId);
}