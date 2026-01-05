package com.example.foodwatchout;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<FoodItem> foodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        // Use the modern setOnItemSelectedListener
        bottomNav.setOnItemSelectedListener(navListener);

        // Load FoodListFragment as the default starting screen
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FoodListFragment())
                    .commit();
        }
    }

    // Use the modern NavigationBarView.OnItemSelectedListener
    private final NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                int id = item.getItemId();

                if (id == R.id.menu_home) {
                    // If home is selected, create the fragment and continue below.
                    Fragment selectedFragment = new FoodListFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true; // We handled the selection.

                } else if (id == R.id.menu_add) {
                    // If add is selected, start the Activity and STOP.
                    startActivity(new Intent(MainActivity.this, AddFoodActivity.class));
                    // Returning false prevents the BottomNavigationView from changing the
                    // selected item's visual state.
                    return false;

                } else if (id == R.id.menu_expiring) {
                    // Same for expiring: start the Activity and STOP.
                    startActivity(new Intent(MainActivity.this, ExpiringSoonActivity.class));
                    return false;
                }

                // This will only be reached if the item ID is not handled above.
                return false;
            };
}