package com.gymlog.viewHolders;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gymlog.database.GymLogRepository;
import com.gymlog.database.entities.GymLog;

import java.util.List;

/**
 * Author: Rahim Siddiq
 * GymLog
 * 08/04/2024
 * GymLogViewModel provides data to the UI and acts as a communication center between
 * the Repository and the UI components. It is responsible for managing and handling
 * data for GymLog entities in a lifecycle-conscious way.
 */
public class GymLogViewModel extends AndroidViewModel {

    private final GymLogRepository repository;

    /**
     * Constructs a new GymLogViewModel with the specified Application context.
     * Initializes the GymLogRepository for accessing data.
     * @param application the application context
     */
    public GymLogViewModel(Application application) {
        super(application);
        repository = GymLogRepository.getRepository(application);
    }

    /**
     * Retrieves all GymLog records for a specific user as LiveData.
     * This allows the UI to observe changes to the data in real-time.
     * @param userId the ID of the user whose logs are to be retrieved
     * @return a LiveData list of GymLog records for the specified user
     */
    public LiveData<List<GymLog>> getAllLogsById(int userId) {
        return repository.getAllLogsByUserIdLiveData(userId);
    }

    /**
     * Inserts a new GymLog record into the database.
     * The operation is handled asynchronously by the repository.
     * @param log the GymLog record to insert
     */
    public void insert(GymLog log) {
        repository.insertGymLog(log);
    }
}