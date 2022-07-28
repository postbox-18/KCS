package com.example.kcs.RetroFit;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("fcm/send")
    Call<RootList> createPost(@Body RootList rootList,@Header("Authorization") String authHeader);
}
