package com.gymlog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gymlog.database.GymLogRepository;
import com.gymlog.database.entities.GymLog;
import com.gymlog.database.entities.User;
import com.gymlog.databinding.ActivityMainBinding;
import com.gymlog.viewHolders.GymLogAdapter;
import com.gymlog.viewHolders.GymLogViewModel;
import java.util.ArrayList;

/**
 * Author: Rahim Siddiq
 * GymLog
 * 08/04/2024
 * MainActivity is the primary activity of the GymLog application.
 * It displays the user's gym log entries and allows interaction with the application's features.
 */
public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_USER_ID = "com.gymlog.MAIN_ACTIVITY_USER_ID";
    static final String SAVED_INSTANCE_STATE_USER_ID_KEY = "com.gymlog.SAVED_INSTANCE_STATE_USER_ID_KEY";
    private static final int LOGGED_OUT = -1;
    private ActivityMainBinding binding;
    private GymLogRepository repository;
    private GymLogViewModel gymLogViewModel;
    public static final String TAG = "RS_GYMLOG";
    String mExercise = "";
    double mWeight = 0.0;
    int mReps = 0;
    private int loggedInUserId = -1;
    private User user;

    /**
     * Called when the activity is starting. Initializes the activity, sets the content view,
     * and sets up the RecyclerView and ViewModel.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gymLogViewModel = new ViewModelProvider(this).get(GymLogViewModel.class);

        RecyclerView recyclerView = binding.logDisplayRecyclerView;
        final GymLogAdapter adapter = new GymLogAdapter(new GymLogAdapter.GymLogDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = GymLogRepository.getRepository(getApplication());
        loginUser(savedInstanceState);

        gymLogViewModel.getAllLogsById(loggedInUserId).observe(this, gymlogs -> {
            adapter.submitList(gymlogs);
        });

        // User is not logged in at this point, go to login screen
        if (loggedInUserId == -1) {
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }
        updateSharedPreference();

        binding.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInformationFromDisplay();
                insertGymLogRecord();
            }
        });
    }

    /**
     * Logs in the user by retrieving their ID from SharedPreferences or savedInstanceState.
     * Observes changes to the user and updates the UI accordingly.
     * @param savedInstanceState the Bundle containing the saved instance state
     */
    private void loginUser(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

        if (loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USER_ID_KEY)) {
            loggedInUserId = sharedPreferences.getInt(SAVED_INSTANCE_STATE_USER_ID_KEY, LOGGED_OUT);
        }
        if (loggedInUserId == LOGGED_OUT) {
            loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        }
        if (loggedInUserId == LOGGED_OUT) {
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this, user -> {
            this.user = user;
            if (this.user != null) {
                invalidateOptionsMenu();
            }
        });
    }

    /**
     * Called to retrieve per-instance state from an activity before being killed.
     * Saves the logged-in user's ID to the instance state.
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USER_ID_KEY, loggedInUserId);
        updateSharedPreference();
    }

    /**
     * Initializes the contents of the Activity's options menu.
     * @param menu the options menu in which you place your items
     * @return true for the menu to be displayed; false to hide the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    /**
     * Prepares the Screen's standard options menu to be displayed.
     * @param menu the options menu as last shown or first initialized by onCreateOptionsMenu().
     * @return true for the menu to be displayed; false to hide the menu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        if (user == null) {
            return false;
        }
        item.setTitle(user.getUsername());
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                showLogoutDialog();
                return false;
            }
        });
        return true;
    }

    /**
     * Displays a dialog to confirm user logout.
     * If confirmed, the user is logged out and returned to the login screen.
     */
    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout?");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertBuilder.create().show();
    }

    /**
     * Logs out the current user by updating the SharedPreferences
     * and returning to the login screen.
     */
    private void logout() {
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(MAIN_ACTIVITY_USER_ID, loggedInUserId);

        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    /**
     * Updates the SharedPreferences with the current logged-in user's ID.
     */
    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPreferencesEditor.apply();
    }

    /**
     * Creates an Intent for starting the MainActivity.
     * @param context the context from which the Intent is created
     * @param userId the ID of the logged-in user
     * @return an Intent to start the MainActivity
     */
    static Intent mainActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }

    /**
     * Inserts a new GymLog record into the database.
     * The record is created using the input data from the user interface.
     */
    private void insertGymLogRecord() {
        if (mExercise.isEmpty()) {
            return;
        }
        GymLog log = new GymLog(mExercise, mWeight, mReps, loggedInUserId);
        repository.insertGymLog(log);
    }

    /**
     * @deprecated This method is deprecated and may be removed in future versions.
     * Updates the display with all GymLog records for the logged-in user.
     */
    @Deprecated
    private void updateDisplay() {
        ArrayList<GymLog> allLogs = repository.getAllLogsByUserId(loggedInUserId);
        if (allLogs.isEmpty()) {
        }
        StringBuilder sb = new StringBuilder();
        for (GymLog log : allLogs) {
            sb.append(log);
        }
    }

    /**
     * Retrieves input data from the user interface and assigns it to member variables.
     * Parses the weight and repetitions input as numbers, handling any parsing errors.
     */
    private void getInformationFromDisplay() {
        mExercise = binding.exerciseInputEditText.getText().toString();
        try {
            mWeight = Double.parseDouble(binding.weightInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Error reading value from Weight edit text");
        }
        try {
            mReps = Integer.parseInt(binding.repInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Error reading value from Reps edit text");
        }
    }
}