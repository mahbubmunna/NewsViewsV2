package me.moonss.tigersup.service.repository;

import me.moonss.tigersup.service.model.News;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    String HTTPS_API_NEWS_URL = "http://newsapi.org/v2/";

    @GET("top-headlines")
    Call<News> getLatestNews(@Query("sources") String sources, @Query("apiKey") String apiKey);
}
