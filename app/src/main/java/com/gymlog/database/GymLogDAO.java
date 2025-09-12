package com.gymlog.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.gymlog.database.entities.GymLog;
import java.util.List;

/**
 * Author: Rahim Siddiq
 * GymLog
 * 08/04/2024
 * Data Access Object (DAO) for the GymLog entity.
 */
@Dao
public interface GymLogDAO {

    /**
     * Inserts a GymLog record into the database.
     * If a conflict occurs, the existing record is replaced with the new one.
     * @param gymlog the GymLog record to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GymLog gymlog);

    /**
     * Retrieves all GymLog records from the database ordered by date in descending order.
     * @return a list of all GymLog records
     */
    @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " ORDER BY date DESC")
    List<GymLog> getAllRecords();

    /**
     * Retrieves all GymLog records for a specific user, ordered by date in descending order.
     * @param loggedInUserId the ID of the user whose records are to be retrieved
     * @return a list of GymLog records for the specified user
     */
    @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    List<GymLog> getRecordsByUserId(int loggedInUserId);

    /**
     * Retrieves all GymLog records for a specific user as LiveData, ordered by date in descending order.
     * This allows for observing changes to the data in real-time.
     * @param loggedInUserId the ID of the user whose records are to be retrieved
     * @return a LiveData list of GymLog records for the specified user
     */
    @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    LiveData<List<GymLog>> getRecordsByUserIdLiveData(int loggedInUserId);
}
