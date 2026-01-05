package com.example.foodwatchout;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodDao {

    @Insert
    void insert(FoodItem foodItem);

    @Query("SELECT * FROM food_table ORDER BY expiryDate ASC")
    List<FoodItem> getAllFood();

    @Query("SELECT * FROM food_table WHERE expiryDate <= :limitDate")
    List<FoodItem> getExpiringSoon(long limitDate);
}
