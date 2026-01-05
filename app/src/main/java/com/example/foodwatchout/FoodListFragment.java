package com.example.foodwatchout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        // IMPORTANT: keep your existing RecyclerView ID
        recyclerView = view.findViewById(R.id.recyclerViewAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadFoodData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFoodData(); // refresh data after returning to this screen
    }

    private void loadFoodData() {
        FoodDatabase db = FoodDatabase.getInstance(getContext());
        List<FoodItem> foodList = db.foodDao().getAllFood();

        adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);
    }
}

