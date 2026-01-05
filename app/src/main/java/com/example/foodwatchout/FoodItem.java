package com.example.foodwatchout;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_table")
public class FoodItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private long expiryDate; // stored as timestamp (milliseconds)

    // Constructor (Room will use this)
    public FoodItem(String name, long expiryDate) {
        this.name = name;
        this.expiryDate = expiryDate;
    }

    // Getter & Setter for ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters
    public String getName() {
        return name;
    }

    public long getExpiryDate() {
        return expiryDate;
    }
}


