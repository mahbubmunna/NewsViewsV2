package me.moonss.tigersup.service.repository;

import me.moonss.tigersup.service.model.Number;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NumberApiService {
    String HTTPS_API_NUMBER_URL = "http://numbersapi.com/";

    @GET("{number}?json")
    Call<Number> getNumberTrivia(@Path("number") String number);

}
