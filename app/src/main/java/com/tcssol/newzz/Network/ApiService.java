package com.tcssol.newzz.Network;

import com.tcssol.newzz.Model.ResponseWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search")
    Call<ResponseWrapper> getEverything(
            @Query("q") String query,
            @Query("lang") String[] lang,
            @Query("countries") String[] countries,
            @Query("topic") String topic
    );

    @GET("latest_headlines")
    Call<ResponseWrapper> getTopHeadlines(
            @Query("when") String when,
            @Query("lang") String[] lang,
            @Query("countries") String[] country

    );
}

