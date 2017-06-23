package com.example.emily.triptalk;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by emily on 23.06.2017.
 */
public interface GitHubService {
    @GET("users")
    Call<List<User>> getUsers();
    @GET("users/{user}")
    Call<UserDetails> getUserDetails(@Path("user") String user);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
