package com.example.fetchrewardsapp;

import androidx.annotation.NonNull;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
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
        progressBar.setVisibility(View.VISIBLE);

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FetchApiService apiService = retrofit.create(FetchApiService.class);

        Call<List<FetchItem>> call = apiService.getItems();

        // execute the call asynchronously
        call.enqueue(new Callback<List<FetchItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<FetchItem>> call, @NonNull Response<List<FetchItem>> response) {
                progressBar.setVisibility(View.GONE);

                //check if the response is successful and has a body
                if (response.isSuccessful() && response.body() != null) {
                    List<FetchItem> items = response.body();
                    processDataAndSetupRecyclerView(items);
                } else {
                    //show a Toast message if the response is unsuccessful or empty
                    showError("Failed to retrieve data.");
                    Log.e("MainActivity", "Response unsuccessful or empty.");
                }
            }

            // network call fails handle
            @Override
            public void onFailure(@NonNull Call<List<FetchItem>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Error: " + t.getMessage());
                Log.e("MainActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    // helper func to display an error as Toast
    private void showError(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    private void processDataAndSetupRecyclerView(List<FetchItem> items) {
        // filter out items with null or empty names
        List<FetchItem> filteredItems = new ArrayList<>();
        for (FetchItem item : items) {
            if (item.getName() != null && !item.getName().trim().isEmpty()) {
                filteredItems.add(item);
            }
        }

        //sort items by listId and then by name
        filteredItems.sort((o1, o2) -> {
            if (o1.getListId() != o2.getListId()) {
                return Integer.compare(o1.getListId(), o2.getListId());
            } else {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        //group items by listId
        Map<Integer, List<FetchItem>> groupedMap = new HashMap<>();
        for (FetchItem item : filteredItems) {
            if (!groupedMap.containsKey(item.getListId())) {
                groupedMap.put(item.getListId(), new ArrayList<>());
            }
            Objects.requireNonNull(groupedMap.get(item.getListId())).add(item);
        }

        //prepare the display list with headers
        List<DisplayItem> displayItemList = new ArrayList<>();
        for (Map.Entry<Integer, List<FetchItem>> entry : groupedMap.entrySet()) {
            // add header
            displayItemList.add(new DisplayItem.Header(entry.getKey()));
            // add items
            for (FetchItem item : entry.getValue()) {
                displayItemList.add(new DisplayItem.Item(item.getName()));
            }
        }

        //initialize the adapter
        FetchItemAdapter adapter = new FetchItemAdapter(displayItemList);
        recyclerView.setAdapter(adapter);
    }
}
