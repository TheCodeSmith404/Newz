package com.tcssol.newzz.Network;

import com.tcssol.newzz.Model.ResponseWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("everything")
    Call<ResponseWrapper> getEverything(
            @Query("q") String query,
            @Query("from") String fromDate,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );
    @GET("top-headlines")
    Call<ResponseWrapper> getTopHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
}

