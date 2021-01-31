package com.skts.ourmemory.server;

import com.skts.ourmemory.model.Post;
import com.skts.ourmemory.model.TestModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRetrofitApi {
    @GET("/posts")
    Call<List<Post>> getData(@Query("result") int result);

    @POST("/SignUp")
    Call<Post> postData(@Body TestModel testModel);
}
