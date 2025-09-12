package com.gymlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.gymlog.database.GymLogDatabase;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Author: Rahim Siddiq
 * GymLog
 * 08/04/2024
 * GymLog.java represents a log entry for a gym exercise session.
 */
@Entity(tableName = GymLogDatabase.GYM_LOG_TABLE)
public class GymLog {

//  The unique identifier for the GymLog.
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String exercise;
    private double weight;
    private int reps;
    private LocalDateTime date;
    private int userId;

    /**
     * Constructs a new GymLog with the specified exercise, weight, reps, and userId.
     * The date is automatically set to the current date and time.
     * @param exercise the name of the exercise
     * @param weight   the weight used during the exercise
     * @param reps     the number of repetitions performed
     * @param userId   the ID of the user associated with this log entry
     */
    public GymLog(String exercise, double weight, int reps, int userId) {
        this.exercise = exercise;
        this.weight = weight;
        this.reps = reps;
        this.userId = userId;
        date = LocalDateTime.now();
    }

    /**
     * Returns a string representation of the GymLog.
     * @return a string representing the GymLog, including exercise name, weight, reps, and date
     */
    @NonNull
    @Override
    public String toString() {
        return exercise + '\n' +
                "weight: " + weight + '\n' +
                "reps: " + reps + '\n' +
                "date: " + date.toString() + '\n' +
                "=-=-=-=-=-=-=-=-=-=-=-\n";
    }

    /**
     * Compares this GymLog to the specified object.
     * @param o the object to compare this GymLog against
     * @return true if the given object represents a GymLog equivalent to this GymLog, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GymLog gymLog = (GymLog) o;
        return id == gymLog.id && Double.compare(weight, gymLog.weight) == 0 && reps == gymLog.reps && userId == gymLog.userId && Objects.equals(exercise, gymLog.exercise) && Objects.equals(date, gymLog.date);
    }

    /**
     * Returns a hash code value for the GymLog. This method is supported for the benefit of
     * hash tables such as those provided by HashMap.
     * @return a hash code value for this GymLog
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, exercise, weight, reps, date, userId);
    }

    /**
     * Returns the ID of the GymLog.
     * @return the ID of the GymLog
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the GymLog.
     * @param id the ID to set for the GymLog
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the exercise name of the GymLog.
     * @return the exercise name of the GymLog
     */
    public String getExercise() {
        return exercise;
    }

    /**
     * Sets the exercise name of the GymLog.
     * @param exercise the exercise name to set for the GymLog
     */
    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    /**
     * Returns the weight used in the GymLog.
     * @return the weight used in the GymLog
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight used in the GymLog.
     * @param weight the weight to set for the GymLog
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Returns the number of repetitions in the GymLog.
     * @return the number of repetitions in the GymLog
     */
    public int getReps() {
        return reps;
    }

    /**
     * Sets the number of repetitions in the GymLog.
     * @param reps the number of repetitions to set for the GymLog
     */
    public void setReps(int reps) {
        this.reps = reps;
    }

    /**
     * Returns the date of the GymLog.
     * @return the date of the GymLog
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date of the GymLog.
     * @param date the date to set for the GymLog
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Returns the user ID associated with the GymLog.
     * @return the user ID associated with the GymLog
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the GymLog.
     * @param userId the user ID to set for the GymLog
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}