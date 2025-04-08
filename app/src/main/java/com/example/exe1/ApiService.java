package com.example.exe1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("posts")
    Call<List<Post>> getPost();

    @GET("post/{id}")
    Call<List<Post>> getPostDetails();

}
