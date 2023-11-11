package com.example.myvietnampost.API;

import com.example.myvietnampost.model.apiModel.Division;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/api/?depth=3")
    Call<List<Division>> getDivisions();
}
