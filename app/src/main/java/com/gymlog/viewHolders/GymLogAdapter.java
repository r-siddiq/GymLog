package com.gymlog.viewHolders;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.gymlog.database.entities.GymLog;

/**
 * Author: Rahim Siddiq
 * GymLog
 * 08/04/2024
 * GymLogAdapter is a ListAdapter that manages GymLog items in a RecyclerView.
 * It utilizes the GymLogViewHolder for binding data to views.
 */
public class GymLogAdapter extends ListAdapter<GymLog, GymLogViewHolder> {

    /**
     * Constructs a new GymLogAdapter with the specified DiffUtil.ItemCallback.
     * @param diffCallback the callback to determine item differences
     */
    public GymLogAdapter(@NonNull DiffUtil.ItemCallback<GymLog> diffCallback) {
        super(diffCallback);
    }

    /**
     * Called when RecyclerView needs a new GymLogViewHolder of the given type to represent an item.
     * @param parent the ViewGroup into which the new View will be added
     * @param viewType the view type of the new View
     * @return a new GymLogViewHolder that holds a View of the given view type
     */
    @Override
    @NonNull
    public GymLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return GymLogViewHolder.create(parent);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * Updates the contents of the GymLogViewHolder to reflect the item at the given position.
     * @param holder the GymLogViewHolder which should be updated to represent the contents of the item
     * @param position the position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull GymLogViewHolder holder, int position) {
        GymLog current = getItem(position);
        holder.bind(current.toString());
    }

    /**
     * GymLogDiff is a DiffUtil.ItemCallback implementation for comparing GymLog items.
     */
    public static class GymLogDiff extends DiffUtil.ItemCallback<GymLog> {
        /**
         * Called to check whether two objects represent the same item.
         * @param oldItem the old item
         * @param newItem the new item
         * @return true if the two items represent the same object
         */
        @Override
        public boolean areItemsTheSame(@NonNull GymLog oldItem, @NonNull GymLog newItem) {
            return oldItem == newItem;
        }

        /**
         * Called to check whether two items have the same data.
         * This information is used to detect if the contents of an item have changed.
         * @param oldItem the old item
         * @param newItem the new item
         * @return true if the contents of the items are the same
         */
        @Override
        public boolean areContentsTheSame(@NonNull GymLog oldItem, @NonNull GymLog newItem) {
            return oldItem.equals(newItem);
        }
    }
}