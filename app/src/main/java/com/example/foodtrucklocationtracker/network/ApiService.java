package com.example.foodtrucklocationtracker.network;

import com.example.foodtrucklocationtracker.model.FoodTruck;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    // This defines the GET request to your API endpoint
    @GET("trucks") // Example: https://yourserver.com/api/trucks
    Call<List<FoodTruck>> getFoodTrucks();
}
