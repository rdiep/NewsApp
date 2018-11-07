package com.newsreader.android.newsreader.network;

import com.newsreader.android.newsreader.model.newsapi.Articles;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetHackerNewsDataService {


    @GET ("https://newsapi.org/v2/top-headlines")
    Call<Articles> getHeadlines(
            @Query("country") String country,
            @Query("sources") String sources,
            @Query("page") int num,
            @Query("apiKey") String key

    );

    @GET ("https://newsapi.org/v2/everything")
    Call<Articles> getCustom(
            @Query("q") String topic,
            @Query("from") String date,
            @Query("sortBy") String sortBy,
            @Query("language") String language,
            @Query("page") int num,
            @Query("apiKey") String key

    );

    @GET ("https://newsapi.org/v2/top-headlines")
    Call<Articles> getBusiness(
            @Query("category") String cat,
            @Query("page") int num,
            @Query("apiKey") String key

    );

    @GET ("https://newsapi.org/v2/everything")
    Call<Articles> getBitCoin(
            @Query("q") String country,
            @Query("apiKey") String key

    );





}
