package com.example.foodwatchout;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;

    public FoodAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    // Update list safely
    public void updateList(List<FoodItem> newList) {
        this.foodList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView name, expiry;
        View viewStatus;

        FoodViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foodName);
            expiry = itemView.findViewById(R.id.foodExpiry);
            viewStatus = itemView.findViewById(R.id.viewStatus);
        }
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        FoodItem item = foodList.get(position);

        holder.name.setText(item.getName());

        // Convert long date to readable format
        holder.expiry.setText(
                "Expires: " + formatDate(item.getExpiryDate())
        );

        long daysLeft = getDaysLeft(item.getExpiryDate());

        // Set status color dot
        if (daysLeft < 0) {
            // 🔴 Expired
            holder.viewStatus.setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(holder.itemView.getContext(), R.color.expired_red)
                    )
            );
        } else if (daysLeft <= 3) {
            // 🟠 About to expire
            holder.viewStatus.setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(holder.itemView.getContext(), R.color.warning_orange)
                    )
            );
        } else {
            // 🟢 Safe
            holder.viewStatus.setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(holder.itemView.getContext(), R.color.safe_green)
                    )
            );
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    // ✅ FIXED: accepts long (milliseconds)
    private long getDaysLeft(long expiryDateMillis) {
        long todayMillis = System.currentTimeMillis();
        long diff = expiryDateMillis - todayMillis;
        return TimeUnit.MILLISECONDS.toDays(diff);
    }

    // Format date for display only
    private String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(millis));
    }
}
