package com.example.fetchrewardsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FetchItemAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = new ProgressBar(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    private void fetchData() {
        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FetchApiService apiService = retrofit.create(FetchApiService.class);
        Call<List<FetchItem>> call = apiService.getItems();

        call.enqueue(new Callback<List<FetchItem>>() {
            @Override
            public void onResponse(Call<List<FetchItem>> call, Response<List<FetchItem>> response) {
                // Hide loading indicator
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<FetchItem> items = response.body();
                    processDataAndSetupRecyclerView(items);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Response unsuccessful or empty.");
                }
            }

            @Override
            public void onFailure(Call<List<FetchItem>> call, Throwable t) {
                // Hide loading indicator
                progressBar.setVisibility(View.GONE);

                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    private void processDataAndSetupRecyclerView(List<FetchItem> items) {
        // Filter out items with null or empty names
        List<FetchItem> filteredItems = new ArrayList<>();
        for (FetchItem item : items) {
            if (item.getName() != null && !item.getName().trim().isEmpty()) {
                filteredItems.add(item);
            }
        }

        // Sort items by listId and then by name
        filteredItems.sort(new Comparator<FetchItem>() {
            @Override
            public int compare(FetchItem o1, FetchItem o2) {
                if (o1.getListId() != o2.getListId()) {
                    return Integer.compare(o1.getListId(), o2.getListId());
                } else {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            }
        });

        // Group items by listId
        Map<Integer, List<FetchItem>> groupedMap = new HashMap<>();
        for (FetchItem item : filteredItems) {
            if (!groupedMap.containsKey(item.getListId())) {
                groupedMap.put(item.getListId(), new ArrayList<>());
            }
            groupedMap.get(item.getListId()).add(item);
        }

        // Prepare the display list with headers
        List<DisplayItem> displayItemList = new ArrayList<>();
        for (Map.Entry<Integer, List<FetchItem>> entry : groupedMap.entrySet()) {
            // Add header
            displayItemList.add(new DisplayItem.Header(entry.getKey()));
            // Add items
            for (FetchItem item : entry.getValue()) {
                displayItemList.add(new DisplayItem.Item(item.getName()));
            }
        }

        // Initialize the adapter
        adapter = new FetchItemAdapter(displayItemList);
        recyclerView.setAdapter(adapter);
    }
}
