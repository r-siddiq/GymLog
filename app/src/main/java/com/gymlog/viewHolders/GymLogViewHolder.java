package com.gymlog.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.gymlog.R;

/**
 * Author: Rahim Siddiq
 * GymLog
 * 08/04/2024
 * GymLogViewHolder is a ViewHolder for displaying GymLog items in a RecyclerView.
 * It binds data to a TextView to display each GymLog entry.
 */
public class GymLogViewHolder extends RecyclerView.ViewHolder {

    private final TextView gymLogViewItem;

    /**
     * Constructs a new GymLogViewHolder with the specified View.
     * @param gymLogView the View representing the GymLog item
     */
    private GymLogViewHolder(View gymLogView) {
        super(gymLogView);
        gymLogViewItem = gymLogView.findViewById(R.id.recyclerItemTextView);
    }

    /**
     * Binds the provided text to the TextView in the ViewHolder.
     * @param text the text to display in the TextView
     */
    public void bind(String text) {
        gymLogViewItem.setText(text);
    }

    /**
     * Creates a new instance of GymLogViewHolder.
     * Inflates the layout for the GymLog item and initializes the ViewHolder.
     * @param Parent the ViewGroup into which the new View will be added
     * @return a new GymLogViewHolder instance
     */
    static GymLogViewHolder create(ViewGroup Parent) {
        View view = LayoutInflater.from(Parent.getContext())
                .inflate(R.layout.gymlog_recycler_item, Parent, false);
        return new GymLogViewHolder(view);
    }
}