package com.gymlog.database;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gymlog.MainActivity;
import com.gymlog.database.entities.GymLog;
import com.gymlog.database.entities.User;
import com.gymlog.database.typeConverters.LocalDateTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Rahim Siddiq
 * GymLog
 * 08/04/2024
 * GymLogDatabase is a Room database for the GymLog application.
 * It contains two entities: {@link GymLog} and {@link User}, and provides DAOs for accessing them.
 */
@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {GymLog.class, User.class}, version = 1, exportSchema = false)
public abstract class GymLogDatabase extends RoomDatabase {

    public static final String USER_TABLE = "usertable";
    public static final String GYM_LOG_TABLE = "gymLogTable";
    private static final String DATABASE_NAME = "GymLogDatabase";
//  The singleton instance of the database.
    private static volatile GymLogDatabase INSTANCE;
//  The number of threads to use for database write operations.
    private static final int NUMBER_OF_THREADS = 4;
//  The executor service for database write operations.
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Returns the singleton instance of the GymLogDatabase.
     * If the instance is not yet created, it initializes the database with default values.
     * @param context the application context
     * @return the singleton instance of the GymLogDatabase
     */
    static GymLogDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GymLogDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GymLogDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Callback to add default values to the database upon creation.
     * Inserts default users into the user table.
     */
    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "Database Created");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();
                User admin = new User("admin1", "admin1");
                admin.setAdmin(true);
                dao.insert(admin);
                User testUser1 = new User("testUser1", "testUser1");
                dao.insert(testUser1);
            });
        }
    };

    /**
     * Provides access to GymLogDAO for interacting with GymLog entities.
     * @return the GymLogDAO instance
     */
    public abstract GymLogDAO gymLogDAO();

    /**
     * Provides access to UserDAO for interacting with User entities.
     * @return the UserDAO instance
     */
    public abstract UserDAO userDAO();
}
