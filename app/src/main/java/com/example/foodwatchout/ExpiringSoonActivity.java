package com.example.foodwatchout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpiringSoonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtTodayCount, txtWeekCount, txtTotalCount;
    private Button btnViewAll;
    private FoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiring_soon);

        // ActionBar setup
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Expiring Within 7 Days");
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(Color.parseColor("#4CAF50")));
        }

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewExpiring);
        txtTodayCount = findViewById(R.id.txtTodayCount);
        txtWeekCount = findViewById(R.id.txtWeekCount);
        txtTotalCount = findViewById(R.id.txtTotalCount);
        btnViewAll = findViewById(R.id.btnManageAll);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnViewAll.setOnClickListener(v -> finish());

        loadExpiringItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpiringItems();
    }

    private void loadExpiringItems() {

        FoodDatabase db = FoodDatabase.getInstance(this);

        long today = System.currentTimeMillis();
        long sevenDaysLater = today + (7L * 24 * 60 * 60 * 1000);

        // Get items expiring within 7 days
        List<FoodItem> expiringSoonList =
                db.foodDao().getExpiringSoon(sevenDaysLater);

        // Calculate today range
        long startOfToday = today - (today % (24 * 60 * 60 * 1000));
        long endOfToday = startOfToday + (24 * 60 * 60 * 1000);

        int todayCount = 0;
        for (FoodItem item : expiringSoonList) {
            if (item.getExpiryDate() >= startOfToday &&
                    item.getExpiryDate() < endOfToday) {
                todayCount++;
            }
        }

        int weekCount = expiringSoonList.size();
        int totalCount = db.foodDao().getAllFood().size();

        // Update counters
        txtTodayCount.setText(String.valueOf(todayCount));
        txtWeekCount.setText(String.valueOf(weekCount));
        txtTotalCount.setText(String.valueOf(totalCount));

        // Show / hide RecyclerView
        if (expiringSoonList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            btnViewAll.setText("ADD FOOD ITEMS");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            btnViewAll.setText("VIEW ALL FOOD ITEMS");

            adapter = new FoodAdapter(expiringSoonList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
