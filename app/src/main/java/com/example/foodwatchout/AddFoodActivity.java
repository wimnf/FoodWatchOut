package com.example.foodwatchout;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Calendar;

public class AddFoodActivity extends AppCompatActivity {

    private EditText editName, editDate;
    private Button btnSave;

    // Store selected expiry date as timestamp
    private long selectedExpiryDate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // =========================
        // TOOLBAR SETUP (IMPORTANT)
        // =========================
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Food Item");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        // Match status bar color with toolbar
        getWindow().setStatusBarColor(Color.parseColor("#4CAF50"));

        // =========================
        // INITIALIZE VIEWS
        // =========================
        editName = findViewById(R.id.editFoodName);
        editDate = findViewById(R.id.editExpiryDate);
        btnSave = findViewById(R.id.btnSaveFood);

        // Date picker
        editDate.setFocusable(false);
        editDate.setOnClickListener(v -> showDatePicker());

        // Save button
        btnSave.setOnClickListener(v -> saveFoodItem());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year, month, day, 0, 0, 0);
                    selectedCal.set(Calendar.MILLISECOND, 0);

                    selectedExpiryDate = selectedCal.getTimeInMillis();

                    String formattedDate = String.format(
                            "%04d-%02d-%02d",
                            year, month + 1, day
                    );
                    editDate.setText(formattedDate);
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );

        // Prevent past dates
        dialog.getDatePicker().setMinDate(c.getTimeInMillis());
        dialog.show();
    }

    private void saveFoodItem() {
        String name = editName.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter food name", Toast.LENGTH_SHORT).show();
            editName.requestFocus();
            return;
        }

        if (selectedExpiryDate == -1) {
            Toast.makeText(this, "Please select expiry date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to Room database
        FoodDatabase db = FoodDatabase.getInstance(this);
        FoodItem foodItem = new FoodItem(name, selectedExpiryDate);
        db.foodDao().insert(foodItem);

        Toast.makeText(this, "Food item saved successfully", Toast.LENGTH_SHORT).show();

        // Clear fields
        editName.setText("");
        editDate.setText("");
        selectedExpiryDate = -1;
        editName.requestFocus();
    }
}
