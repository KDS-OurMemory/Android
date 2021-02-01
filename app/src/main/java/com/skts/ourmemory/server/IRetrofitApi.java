package com.skts.ourmemory.server;

import com.skts.ourmemory.model.Post;
import com.skts.ourmemory.model.SendUserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRetrofitApi {
    @GET("/posts")
    Call<List<Post>> getData(@Query("result") int result);

    @POST("/SignUp")
    Call<Post> postData(@Body SendUserModel sendUserModel);
}
