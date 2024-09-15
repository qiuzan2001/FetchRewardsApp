package com.example.fetchrewardsapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FetchApiService {
    @GET("hiring.json")
    Call<List<FetchItem>> getItems();
}
