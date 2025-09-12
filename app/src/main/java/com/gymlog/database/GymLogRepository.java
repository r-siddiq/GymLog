package com.gymlog.database;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.gymlog.database.entities.GymLog;
import com.gymlog.MainActivity;
import com.gymlog.database.entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Author: Rahim Siddiq
 * GymLog
 * 08/04/2024
 * GymLogRepository provides a layer of abstraction over the data sources.
 * It manages queries and allows access to the database using the DAO interfaces.
 */
public class GymLogRepository {

    private final GymLogDAO gymLogDAO;
    private final UserDAO userDAO;
    private ArrayList<GymLog> allLogs;
    private static GymLogRepository repository;

    /**
     * Private constructor for GymLogRepository.
     * Initializes DAO interfaces and retrieves all records from the database.
     * @param application the application context
     */
    private GymLogRepository(Application application) {
        GymLogDatabase db = GymLogDatabase.getDatabase(application);
        this.gymLogDAO = db.gymLogDAO();
        this.userDAO = db.userDAO();
        this.allLogs = (ArrayList<GymLog>) this.gymLogDAO.getAllRecords();
    }

    /**
     * Returns the singleton instance of GymLogRepository.
     * If the instance is not yet created, it initializes it using the application context.
     * @param application the application context
     * @return the singleton instance of GymLogRepository
     */
    public static GymLogRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<GymLogRepository> future = GymLogDatabase.databaseWriteExecutor.submit(
                new Callable<GymLogRepository>() {
                    @Override
                    public GymLogRepository call() throws Exception {
                        return new GymLogRepository(application);
                    }
                });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting GymLogRepository, thread error");
        }
        return null;
    }

    /**
     * Retrieves all GymLog records from the database.
     * Executes the query in a separate thread using an executor service.
     * @return a list of all GymLog records
     */
    public ArrayList<GymLog> getAllLogs() {
        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<GymLog>>() {
                    @Override
                    public ArrayList<GymLog> call() throws Exception {
                        return (ArrayList<GymLog>) gymLogDAO.getAllRecords();
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in the repository");
        }
        return null;
    }

    /**
     * Inserts a GymLog record into the database.
     * The operation is executed in a separate thread.
     * @param gymLog the GymLog record to insert
     */
    public void insertGymLog(GymLog gymLog) {
        GymLogDatabase.databaseWriteExecutor.execute(() -> {
            gymLogDAO.insert(gymLog);
        });
    }

    /**
     * Inserts one or more User records into the database.
     * The operation is executed in a separate thread.
     * @param user the User records to insert
     */
    public void insertUser(User... user) {
        GymLogDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

    /**
     * Retrieves a User record by username.
     * @param username the username of the User
     * @return a LiveData object containing the User record
     */
    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    /**
     * Retrieves a User record by user ID.
     * @param userId the ID of the User
     * @return a LiveData object containing the User record
     */
    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    /**
     * Retrieves all GymLog records for a specific user as LiveData.
     * @param loggedInUserId the ID of the logged-in user
     * @return a LiveData list of GymLog records for the specified user
     */
    public LiveData<List<GymLog>> getAllLogsByUserIdLiveData(int loggedInUserId) {
        return gymLogDAO.getRecordsByUserIdLiveData(loggedInUserId);
    }

    /**
     * @deprecated This method is deprecated in favor of LiveData.
     * Retrieves all GymLog records for a specific user.
     * @param loggedInUserId the ID of the logged-in user
     * @return a list of GymLog records for the specified user
     */
    @Deprecated
    public ArrayList<GymLog> getAllLogsByUserId(int loggedInUserId) {
        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<GymLog>>() {
                    @Override
                    public ArrayList<GymLog> call() throws Exception {
                        return (ArrayList<GymLog>) gymLogDAO.getRecordsByUserId(loggedInUserId);
                    }
                });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in the repository");
        }
        return null;
    }
}